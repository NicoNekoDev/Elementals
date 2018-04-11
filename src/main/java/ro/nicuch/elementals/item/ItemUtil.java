package ro.nicuch.elementals.item;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import ro.nicuch.elementals.Elementals;
import ro.nicuch.elementals.User;
import ro.nicuch.elementals.elementals.ElementalsUtil;

public class ItemUtil {
    private static final ConcurrentMap<UUID, DisguiseClass> disguise = new ConcurrentHashMap<UUID, DisguiseClass>();
    private static final ConcurrentMap<UUID, ConcurrentHashMap<Block, EntityArmorStand>> stands = new ConcurrentHashMap<UUID, ConcurrentHashMap<Block, EntityArmorStand>>();

    public static int getEMCOfMaterial(Material mat) {
        switch (mat) {
            case STONE:
            case COBBLESTONE:
            case DIRT:
            case GRASS:
                return 1;
            case WOOD:
                return 4;
            case LOG:
            case LOG_2:
            case WORKBENCH:
            case SAPLING:
                return 16;
            case SAND:
                return 14;
            case ACACIA_DOOR_ITEM:
            case BIRCH_DOOR_ITEM:
            case DARK_OAK_DOOR_ITEM:
            case JUNGLE_DOOR_ITEM:
            case SPRUCE_DOOR_ITEM:
            case WOODEN_DOOR:
                return 12;
            case AIR:
            case BARRIER:
            case BEDROCK:
            case ACACIA_DOOR:
            case BIRCH_DOOR:
            case DARK_OAK_DOOR:
            case JUNGLE_DOOR:
            case SPRUCE_DOOR:
            case WOOD_DOOR:
            case IRON_DOOR_BLOCK:
            case WATER:
            case STATIONARY_WATER:
            case LAVA:
            case STATIONARY_LAVA:
            default:
                return 0;
        }
    }

    public static int getEMCOfItemStack(ItemStack item) {
        return getEMCOfMaterial(item.getType()) * item.getAmount();
    }

    public static boolean hasEMCOfMaterial(Material mat) {
        switch (mat) {
            case AIR:
            case BARRIER:
            case BEDROCK:
            case ACACIA_DOOR:
            case BIRCH_DOOR:
            case DARK_OAK_DOOR:
            case JUNGLE_DOOR:
            case SPRUCE_DOOR:
            case WOOD_DOOR:
            case IRON_DOOR_BLOCK:
            case WATER:
            case STATIONARY_WATER:
            case LAVA:
            case STATIONARY_LAVA:
                return false;
            default:
                return true;
        }
    }

    public static void findTarget(Monster monster) {
        if (!ElementalsUtil.hasTag(monster, "corupted"))
            return;
        if (monster.getTarget() != null) {
            if (!monster.getTarget().getType().equals(EntityType.PLAYER))
                return;
        }
        List<Entity> list = Lists.newArrayList();
        monster.getNearbyEntities(25, 25, 25).forEach((Entity entity) -> {
            if (!entity.isDead() && entity.isValid() && entity instanceof LivingEntity) {
                if (entity instanceof Monster) {
                    list.add(entity);
                } else if (entity instanceof Player) {
                    if (isDisguised(entity.getUniqueId()))
                        list.add(entity);
                }
            }
        });
        if (list.isEmpty())
            return;
        monster.setTarget((LivingEntity) list.get(new Random().nextInt(list.size())));
        list.clear();
    }

    public static boolean isDisguised(UUID uuid) {
        return disguise.containsKey(uuid);
    }

    public static DisguiseType getDisguiseType(UUID uuid) {
        return disguise.get(uuid).getDisguiseType();
    }

    public static BukkitTask getDisguiseTask(UUID uuid) {
        return disguise.get(uuid).getBukkitTask();
    }

    public static void removeDisguise(UUID uuid) {
        disguise.remove(uuid);
    }

    public static void setDisguise(UUID uuid, DisguiseClass dis) {
        disguise.put(uuid, dis);
    }

    public static void disguise(Player player, MobDisguise mob) {
        DisguiseAPI.disguiseEntity(player, mob);
        UUID uuid = player.getUniqueId();
        World world = player.getWorld();
        ItemUtil.getRoundLocation(player.getLocation().add(0, 1.1, 0), 15, 1)
                .forEach((Location loc) -> world.spawnParticle(Particle.VILLAGER_HAPPY, loc, 1));
        world.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 1.0f, 1.0f);
        if (isDisguised(uuid))
            disguise.get(uuid).getBukkitTask().cancel();
        BukkitTask task = Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> {
            if (player.isOnline()) {
                if (DisguiseAPI.isDisguised(player))
                    DisguiseAPI.undisguiseToAll(player);
                removeDisguise(uuid);
            }
        }, 5 * 60 * 20L);
        setDisguise(uuid, new DisguiseClass(task, mob.getType()));
    }

    public static void decreaseItemAmount(Player player) {
        if (player.getGameMode().equals(GameMode.CREATIVE))
            return;
        ItemStack hand = player.getInventory().getItemInMainHand();
        int amount = hand.getAmount();
        if (amount > 1)
            hand.setAmount(amount - 1);
        else
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        player.updateInventory();
    }

    public static List<Block> getSphere(Block b, int r) {
        List<Block> list = Lists.newArrayList();
        int cx = b.getX();
        int cy = b.getY();
        int cz = b.getZ();
        for (int x = cx - r; x <= cx + r; x++) {
            for (int z = cz - r; z <= cz + r; z++) {
                for (int y = cy - r; y < cy + r; y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (cy - y) * (cy - y);
                    if (dist < r * r)
                        list.add(b.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return list;
    }

    public static List<Block> getCircleByHighestBlock(Block b, int r) {
        List<Block> list = Lists.newArrayList();
        int x1 = b.getX() - r;
        int z1 = b.getZ() - r;
        int x2 = b.getX() + r;
        int z2 = b.getZ() + r;
        for (int x = x1; x < x2; x++)
            for (int z = z1; z < z2; z++)
                if ((Math.pow(x - b.getX(), 2) + Math.pow(z - b.getZ(), 2)) <= (Math.pow(r, 2)))
                    list.add(b.getWorld().getHighestBlockAt(x, z));
        return list;
    }

    public static List<Location> getRoundLocation(Location loc, int count, double r) {
        List<Location> list = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            double angle = i * ((2 * Math.PI) / count);
            double b = r * Math.sin(angle);
            double c = r * Math.cos(angle);
            list.add(loc.clone().add(c, 0, b));
        }
        return list;
    }

    public static EntityArmorStand setArmorStand(Player player, Block block, Location loc) {
        UUID uuid = player.getUniqueId();
        EntityArmorStand stand = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle());
        loc.add(0.5, -1.175, 0.5);
        stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        stand.setNoGravity(true);
        stand.setMarker(true);
        stand.setInvisible(true);
        stand.setFlag(6, true);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(stand));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityEquipment(stand.getId(),
                EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(new ItemStack(Material.STONE))));
        Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> {
            if (stands.containsKey(uuid))
                ((CraftPlayer) player).getHandle().playerConnection
                        .sendPacket(new PacketPlayOutEntityDestroy(stand.getId()));
        }, 3 * 60 * 20L);
        if (stands.containsKey(uuid)) {
            stands.get(uuid).put(block, stand);
        } else {
            ConcurrentHashMap<Block, EntityArmorStand> map = new ConcurrentHashMap<Block, EntityArmorStand>();
            map.put(block, stand);
            stands.put(uuid, map);
        }
        return stand;
    }

    public static boolean containsArmorStand(Player player, Block block) {
        if (!containsArmorStand(player))
            return false;
        if (!stands.get(player.getUniqueId()).containsKey(block))
            return false;
        return true;
    }

    public static boolean containsArmorStand(Player player) {
        if (!stands.containsKey(player.getUniqueId()))
            return false;
        return true;
    }

    public static void removeArmorStand(Player player, Block block) {
        UUID uuid = player.getUniqueId();
        ((CraftPlayer) player).getHandle().playerConnection
                .sendPacket(new PacketPlayOutEntityDestroy(stands.get(uuid).get(block).getId()));
        stands.get(uuid).remove(block);
    }

    public static void removeArmorStand(Player player) {
        UUID uuid = player.getUniqueId();
        stands.get(uuid).values()
                .forEach((EntityArmorStand stand) -> ((CraftPlayer) player).getHandle().playerConnection
                        .sendPacket(new PacketPlayOutEntityDestroy(stand.getId())));
        stands.remove(uuid);
    }

    public static void unload() {
        stands.keySet()
                .forEach((UUID uuid) -> stands.get(uuid).values().forEach(
                        (EntityArmorStand stand) -> ((CraftPlayer) Bukkit.getPlayer(uuid)).getHandle().playerConnection
                                .sendPacket(new PacketPlayOutEntityDestroy(stand.getId()))));
    }

    public static void forcePush(Player player, Entity entity) {
        if (entity instanceof Player) {
            User user = Elementals.getUser(entity.getUniqueId());
            if (user.hasPermission("elementals.override.forcepush"))
                return;
        }
        Vector vector = player.getLocation().toVector().subtract(entity.getLocation().toVector());
        double force = Math.abs((30 - player.getLocation().distance(entity.getLocation())) * 0.125);
        vector.normalize();
        vector.multiply(-force);
        entity.setVelocity(vector);
    }

    public static Entity getTargetEntity(Player player, int distance) {
        for (Block block : player.getLineOfSight((Set<Material>) null, distance)) {
            for (Entity entity : player.getNearbyEntities(distance + 1, distance + 1, distance + 1)) {
                if ((block.getX() <= entity.getLocation().getX() && entity.getLocation().getX() <= block.getX() + 1)
                        && (block.getZ() <= entity.getLocation().getZ()
                        && entity.getLocation().getZ() <= block.getZ() + 1)
                        && (block.getY() - 1.25 <= entity.getLocation().getY()
                        && entity.getLocation().getY() <= block.getY() + 1.5)) {
                    return entity;
                }
            }
        }
        return null;
    }

    public static double yawCorrection(double yaw) {
        if (yaw < 0)
            yaw += 360;
        return yaw / 90 * 90;
    }
}
