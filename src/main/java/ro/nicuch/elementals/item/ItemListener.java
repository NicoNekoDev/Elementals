package ro.nicuch.elementals.item;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wood;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.events.fake.FakeBlockBreakEvent;
import com.google.common.collect.Sets;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.citizensnpcs.api.CitizensAPI;
import ro.nicuch.elementals.Elementals;
import ro.nicuch.elementals.elementals.ElementalsUtil;
import ro.nicuch.elementals.elementals.NanoBlockBreakEvent;
import ro.nicuch.elementals.protection.Field;
import ro.nicuch.elementals.protection.FieldUtil;

public class ItemListener implements Listener {
    private static final Set<UUID> interactList = Sets.newHashSet();

    @EventHandler
    public void event(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        World world = player.getWorld();
        String world_name = world.getName();
        Location p_loc = player.getLocation();
        if (!(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)))
            return;
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand == null)
            return;
        if (!hand.hasItemMeta())
            return;
        ItemMeta meta = hand.getItemMeta();
        if (!meta.hasDisplayName())
            return;
        String displayName = meta.getDisplayName();
        if (interactList.contains(uuid))
            return;
        interactList.add(uuid);
        switch (displayName) {
            case "�aTicket Bonus":
                if (!meta.hasLore())
                    break;
                if (meta.getLore().isEmpty())
                    break;
                String line = meta.getLore().get(0);
                if (line.equals("�c+ 5000$")) {
                    Elementals.getVault().depositPlayer(player, 5000);
                    player.sendMessage("�a5000$ au fost adaugati in contul tau.");
                } else if (line.equals("�c+ 10000$")) {
                    Elementals.getVault().depositPlayer(player, 10000);
                    player.sendMessage("�a10000$ au fost adaugati in contul tau.");
                } else if (line.equals("�c+ 25000$")) {
                    Elementals.getVault().depositPlayer(player, 25000);
                    player.sendMessage("�a25000$ au fost adaugati in contul tau.");
                } else if (line.equals("�c+ 50000$")) {
                    Elementals.getVault().depositPlayer(player, 50000);
                    player.sendMessage("�a50000$ au fost adaugati in contul tau.");
                } else if (line.equals("�c+ 25 level Mining")) {
                    ExperienceAPI.addLevel(player, SkillType.MINING.getName(), 25);
                    player.sendMessage("�aNivelul tau la Mining a crescut cu 25 de nivele!");
                }
                break;
            case "�dCartea Fermierului":
                if (world_name.equals("spawn")) {
                    player.sendMessage("�a�l[�6PikaCraft�a�l] �cNu poti folosi aici asta.");
                    return;
                }
                for (Block block : ItemUtil.getSphere(player.getLocation().getBlock(), 25)) {
                    Material blockType = block.getType();
                    Location loc = block.getLocation();
                    if (blockType.equals(Material.CROPS) || blockType.equals(Material.POTATO)
                            || blockType.equals(Material.CARROT) || blockType.equals(Material.BEETROOT_BLOCK)) {
                        Crops crop = (Crops) block.getState().getData();
                        if (crop.getState().equals(CropState.RIPE))
                            continue;
                        crop.setState(CropState.RIPE);
                        block.getState().setData(crop);
                        block.getState().update(true);
                        world.spawnParticle(Particle.VILLAGER_HAPPY, loc.clone().add(0.25, 0.2, 0.25), 1);
                        world.spawnParticle(Particle.VILLAGER_HAPPY, loc.clone().add(0.25, 0.2, 0.75), 1);
                        world.spawnParticle(Particle.VILLAGER_HAPPY, loc.clone().add(0.75, 0.2, 0.25), 1);
                        world.spawnParticle(Particle.VILLAGER_HAPPY, loc.clone().add(0.75, 0.2, 0.75), 1);
                    } else if (blockType.equals(Material.SUGAR_CANE_BLOCK) || blockType.equals(Material.CACTUS)) {
                        if (block.getRelative(BlockFace.DOWN, 1).getType().equals(blockType)
                                && block.getRelative(BlockFace.DOWN, 2).getType().equals(blockType))
                            continue;
                        Block relative = block.getRelative(BlockFace.UP);
                        Location r_loc = relative.getLocation();
                        if (!relative.isEmpty())
                            continue;
                        relative.getState().setType(block.getType());
                        relative.getState().update(true);
                        world.spawnParticle(Particle.VILLAGER_HAPPY, r_loc.clone().add(0.25, 0.2, 0.25), 1);
                        world.spawnParticle(Particle.VILLAGER_HAPPY, r_loc.clone().add(0.25, 0.2, 0.75), 1);
                        world.spawnParticle(Particle.VILLAGER_HAPPY, r_loc.clone().add(0.75, 0.2, 0.25), 1);
                        world.spawnParticle(Particle.VILLAGER_HAPPY, r_loc.clone().add(0.75, 0.2, 0.75), 1);
                    }
                }
                ItemUtil.getRoundLocation(p_loc.clone().add(0, 1.1, 0), 15, 1)
                        .forEach((Location loc) -> world.spawnParticle(Particle.HEART, loc, 1));
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�aCartea Copacilor":
                if (world_name.equals("spawn")) {
                    player.sendMessage("�a�l[�6PikaCraft�a�l] �cNu poti folosi aici asta.");
                    return;
                }
                for (Block block : ItemUtil.getSphere(player.getLocation().getBlock(), 25)) {
                    Location b_loc = block.getLocation();
                    if (!block.getType().equals(Material.SAPLING))
                        continue;
                    Wood state = (Wood) block.getState().getData();
                    block.setType(Material.AIR);
                    for (Location loc : ItemUtil.getRoundLocation(block.getLocation().add(0, 0.5, 0), 4, 1))
                        block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 1);
                    switch (state.getSpecies()) {
                        case REDWOOD:
                            world.generateTree(b_loc, TreeType.REDWOOD);
                            break;
                        case BIRCH:
                            world.generateTree(b_loc, TreeType.BIRCH);
                            break;
                        case JUNGLE:
                            world.generateTree(b_loc, TreeType.SMALL_JUNGLE);
                            break;
                        case ACACIA:
                            world.generateTree(b_loc, TreeType.ACACIA);
                            break;
                        case DARK_OAK:
                            world.generateTree(b_loc, TreeType.DARK_OAK);
                            break;
                        default:
                            world.generateTree(b_loc, TreeType.TREE);
                            break;
                    }
                }
                ItemUtil.getRoundLocation(p_loc.clone().add(0, 1.1, 0), 15, 1)
                        .forEach((Location loc) -> world.spawnParticle(Particle.VILLAGER_HAPPY, loc, 1));
                world.playSound(p_loc, Sound.BLOCK_LAVA_EXTINGUISH, 1.0f, 1.0f);
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�eCartea Fulgerelor":
                if (player.getWorld().getName().equals("spawn")) {
                    player.sendMessage("�a�l[�6PikaCraft�a�l] �cNu poti folosi aici asta.");
                    return;
                }
                if (FieldUtil.isFieldAtLocation(player.getLocation()) && !player.isOp()) {
                    Field field = FieldUtil.getFieldByLocation(player.getLocation());
                    if (!(field.isMember(uuid) || field.isOwner(uuid)))
                        break;
                }
                ItemUtil.getRoundLocation(p_loc.clone().add(0, 1.1, 0), 15, 8)
                        .forEach((Location loc) -> loc.getWorld().strikeLightning(loc));
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�cCartea Vindecarii":
                player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                ItemUtil.getRoundLocation(p_loc.clone().add(0, 1.1, 0), 15, 1)
                        .forEach((Location loc) -> world.spawnParticle(Particle.HEART, loc, 1));
                world.playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1.0f, 1.0f);
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�bCartea Transformarii �f(�aCreeper�f)":
                MobDisguise creeper = new MobDisguise(DisguiseType.CREEPER);
                ItemUtil.disguise(player, creeper);
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�bCartea Transformarii �f(�2Zombie�f)":
                MobDisguise zombie = new MobDisguise(DisguiseType.ZOMBIE);
                ItemUtil.disguise(player, zombie);
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�bCartea Transformarii �f(�7Skeleton�f)":
                MobDisguise skeleton = new MobDisguise(DisguiseType.SKELETON);
                ItemUtil.disguise(player, skeleton);
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�bCartea Transformarii �f(�4Spider�f)":
                MobDisguise spider = new MobDisguise(DisguiseType.SPIDER);
                ItemUtil.disguise(player, spider);
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�bCartea Transformarii �f(�5Witch�f)":
                MobDisguise witch = new MobDisguise(DisguiseType.WITCH);
                ItemUtil.disguise(player, witch);
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�bCartea Transformarii �f(�8Enderman�f)":
                MobDisguise enderman = new MobDisguise(DisguiseType.ENDERMAN);
                ItemUtil.disguise(player, enderman);
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�bCartea Transformarii �f(�cPigman�f)":
                MobDisguise pigman = new MobDisguise(DisguiseType.PIG_ZOMBIE);
                ItemUtil.disguise(player, pigman);
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�bCartea Transformarii �f(�eHusk�f)":
                MobDisguise husk = new MobDisguise(DisguiseType.HUSK);
                ItemUtil.disguise(player, husk);
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�bCartea Transformarii �f(�9Stray�f)":
                MobDisguise stray = new MobDisguise(DisguiseType.STRAY);
                ItemUtil.disguise(player, stray);
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�eCartea Confuziei":
                if (world_name.equals("spawn")) {
                    player.sendMessage("�a�l[�6PikaCraft�a�l] �cNu poti folosi aici asta.");
                    return;
                }
                ItemUtil.getRoundLocation(p_loc.clone().add(0, 1.1, 0), 15, 1)
                        .forEach((Location loc) -> world.spawnParticle(Particle.VILLAGER_ANGRY, loc, 1));
                for (Entity entity : player.getNearbyEntities(25, 25, 25)) {
                    if (CitizensAPI.getNPCRegistry().isNPC(entity))
                        continue;
                    if (entity instanceof Monster && entity instanceof LivingEntity) {
                        ElementalsUtil.setTag(entity, "corupted");
                        ItemUtil.findTarget((Monster) entity);
                    }
                }
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�eCartea Stralucirii":
                if (world_name.equals("spawn")) {
                    player.sendMessage("�a�l[�6PikaCraft�a�l] �cNu poti folosi aici asta.");
                    return;
                }
                ItemUtil.getRoundLocation(p_loc.clone().add(0, 1.1, 0), 15, 1)
                        .forEach((Location loc) -> world.spawnParticle(Particle.VILLAGER_ANGRY, loc, 1));
                for (Entity entity : player.getNearbyEntities(25, 25, 25)) {
                    if (CitizensAPI.getNPCRegistry().isNPC(entity))
                        continue;
                    if ((entity instanceof Creature || entity instanceof Player) && entity instanceof LivingEntity)
                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 60 * 3, 0),
                                true);
                }
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�fCartea Levitatiei":
                if (world_name.equals("spawn")) {
                    player.sendMessage("�a�l[�6PikaCraft�a�l] �cNu poti folosi aici asta.");
                    return;
                }
                ItemUtil.getRoundLocation(p_loc.clone().add(0, 1.1, 0), 15, 1)
                        .forEach((Location loc) -> world.spawnParticle(Particle.VILLAGER_ANGRY, loc, 1));
                for (Entity entity : player.getNearbyEntities(25, 25, 25)) {
                    if (CitizensAPI.getNPCRegistry().isNPC(entity))
                        continue;
                    if ((entity instanceof Creature || entity instanceof Player) && entity instanceof LivingEntity)
                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20 * 15, 0),
                                true);
                }
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�9Cartea Impingerii":
                if (world_name.equals("spawn")) {
                    player.sendMessage("�a�l[�6PikaCraft�a�l] �cNu poti folosi aici asta.");
                    return;
                }
                ItemUtil.getRoundLocation(p_loc.clone().add(0, 1.1, 0), 15, 1).forEach((Location loc) -> world
                        .spawnParticle(Particle.FALLING_DUST, loc, 1, new MaterialData(Material.WOOL)));
                for (Entity entity : player.getNearbyEntities(25, 25, 25)) {
                    if (CitizensAPI.getNPCRegistry().isNPC(entity))
                        continue;
                    if (((entity instanceof Creature || entity instanceof Player) && entity instanceof LivingEntity)
                            && !FieldUtil.isFieldAtLocation(entity.getLocation())) {
                        ItemUtil.forcePush(player, entity);
                    }
                }
                ItemUtil.decreaseItemAmount(player);
                break;
            case "�6Cartea Schimbarii":
                if (world_name.equals("spawn")) {
                    player.sendMessage("�a�l[�6PikaCraft�a�l] �cNu poti folosi aici asta.");
                    return;
                }
                Entity entity = ItemUtil.getTargetEntity(player, 25);
                if (entity == null)
                    break;
                if (!(entity instanceof Creature || entity instanceof Player))
                    break;
                if (entity.isInsideVehicle())
                    break;
                if (CitizensAPI.getNPCRegistry().isNPC(entity))
                    break;
                if (FieldUtil.isFieldAtLocation(p_loc) && !player.isOp())
                    break;
                if (FieldUtil.isFieldAtLocation(entity.getLocation()) && !player.isOp())
                    break;
                Location playerLoc = player.getLocation().clone();
                Location entityLoc = entity.getLocation().clone();
                player.teleport(entityLoc);
                entity.teleport(playerLoc);
                ItemUtil.getRoundLocation(p_loc.clone().add(0, 1.1, 0), 15, 1)
                        .forEach((Location loc) -> world.spawnParticle(Particle.VILLAGER_ANGRY, loc, 1));
                ItemUtil.decreaseItemAmount(player);
            case "�7Cartea Furisarii":
                if (world_name.equals("spawn")) {
                    player.sendMessage("�a�l[�6PikaCraft�a�l] �cNu poti folosi aici asta.");
                    return;
                }
                Entity ent = ItemUtil.getTargetEntity(player, 25);
                if (ent == null)
                    break;
                if (!(ent instanceof Creature || ent instanceof Player))
                    break;
                if (CitizensAPI.getNPCRegistry().isNPC(ent))
                    break;
                Location entLoc = ent.getLocation();
                if (FieldUtil.isFieldAtLocation(entLoc) && !player.isOp())
                    break;
                double angle = Math.toRadians(ItemUtil.yawCorrection(entLoc.getYaw()));
                double x = Math.sin(angle) * 3;
                double z = Math.cos(angle) * -3;
                player.teleport(entLoc.clone().add(x, 0, z));
                ItemUtil.getRoundLocation(p_loc.add(0, 1.1, 0), 15, 1)
                        .forEach((Location loc) -> world.spawnParticle(Particle.VILLAGER_ANGRY, loc, 1));
                ItemUtil.decreaseItemAmount(player);
            default:
                break;
        }
        Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> interactList.remove(uuid), 5L);
    }

    @EventHandler
    public void event(BlockPlaceEvent event) {
        Block eBlock = event.getBlock();
        if (eBlock.getWorld().getName().equals("spawn"))
            return;
        ItemStack hand = event.getPlayer().getInventory().getItemInMainHand();
        if (!hand.getType().equals(Material.STAINED_GLASS))
            return;
        if (!hand.hasItemMeta())
            return;
        ItemMeta meta = hand.getItemMeta();
        if (!meta.hasDisplayName())
            return;
        if (!meta.getDisplayName().equals("�9Clepsidra Albastra"))
            return;
        event.getBlockPlaced().setType(Material.AIR);
        ItemUtil.getSphere(eBlock, 15).forEach((Block block) -> {
            Material blockType = block.getType();
            if (blockType.equals(Material.IRON_ORE) || blockType.equals(Material.DIAMOND_ORE)
                    || blockType.equals(Material.GOLD_ORE) || blockType.equals(Material.EMERALD_ORE)
                    || blockType.equals(Material.REDSTONE_ORE) || blockType.equals(Material.GLOWING_REDSTONE_ORE)
                    || blockType.equals(Material.LAPIS_ORE) || blockType.equals(Material.QUARTZ_ORE)) {
                ItemUtil.setArmorStand(event.getPlayer(), block, block.getLocation());
            }
        });
    }

    @EventHandler
    public void event0(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (ItemUtil.containsArmorStand(player))
            ItemUtil.removeArmorStand(player);
    }

    @EventHandler
    public void event(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material blockType = block.getType();
        if (!(blockType.equals(Material.IRON_ORE) || blockType.equals(Material.DIAMOND_ORE)
                || blockType.equals(Material.GOLD_ORE) || blockType.equals(Material.EMERALD_ORE)
                || blockType.equals(Material.REDSTONE_ORE) || blockType.equals(Material.GLOWING_REDSTONE_ORE)
                || blockType.equals(Material.LAPIS_ORE) || blockType.equals(Material.QUARTZ_ORE)))
            return;
        Bukkit.getOnlinePlayers().forEach((Player player) -> {
            if (ItemUtil.containsArmorStand(player, block))
                ItemUtil.removeArmorStand(player, block);
        });
    }

    @EventHandler
    public void event(NanoBlockBreakEvent event) {
        Block block = event.getBlock();
        Material blockType = block.getType();
        if (!(blockType.equals(Material.IRON_ORE) || blockType.equals(Material.DIAMOND_ORE)
                || blockType.equals(Material.GOLD_ORE) || blockType.equals(Material.EMERALD_ORE)
                || blockType.equals(Material.REDSTONE_ORE) || blockType.equals(Material.GLOWING_REDSTONE_ORE)
                || blockType.equals(Material.LAPIS_ORE) || blockType.equals(Material.QUARTZ_ORE)))
            return;
        Bukkit.getOnlinePlayers().forEach((Player player) -> {
            if (ItemUtil.containsArmorStand(player, block))
                ItemUtil.removeArmorStand(player, block);
        });
    }

    @EventHandler
    public void event(FakeBlockBreakEvent event) {
        Block block = event.getBlock();
        Material blockType = block.getType();
        if (!(blockType.equals(Material.IRON_ORE) || blockType.equals(Material.DIAMOND_ORE)
                || blockType.equals(Material.GOLD_ORE) || blockType.equals(Material.EMERALD_ORE)
                || blockType.equals(Material.REDSTONE_ORE) || blockType.equals(Material.GLOWING_REDSTONE_ORE)
                || blockType.equals(Material.LAPIS_ORE) || blockType.equals(Material.QUARTZ_ORE)))
            return;
        Bukkit.getOnlinePlayers().forEach((Player player) -> {
            if (ItemUtil.containsArmorStand(player, block))
                ItemUtil.removeArmorStand(player, block);
        });
    }

    @EventHandler
    public void event(EntityTargetLivingEntityEvent event) {
        if (CitizensAPI.getNPCRegistry().isNPC(event.getEntity()))
            return;
        if (CitizensAPI.getNPCRegistry().isNPC(event.getTarget()))
            return;
        if (ElementalsUtil.hasTag(event.getEntity(), "corupted"))
            return;
        if (event.getTarget() == null)
            return;
        if (!event.getTarget().getType().equals(EntityType.PLAYER))
            return;
        if (!ItemUtil.isDisguised(event.getTarget().getUniqueId()))
            return;
        switch (event.getEntity().getType()) {
            case CREEPER:
                if (ItemUtil.getDisguiseType(event.getTarget().getUniqueId()).equals(DisguiseType.CREEPER))
                    event.setCancelled(true);
                break;
            case ZOMBIE:
                if (ItemUtil.getDisguiseType(event.getTarget().getUniqueId()).equals(DisguiseType.ZOMBIE))
                    event.setCancelled(true);
                break;
            case HUSK:
                if (ItemUtil.getDisguiseType(event.getTarget().getUniqueId()).equals(DisguiseType.HUSK))
                    event.setCancelled(true);
                break;
            case SKELETON:
                if (ItemUtil.getDisguiseType(event.getTarget().getUniqueId()).equals(DisguiseType.SKELETON))
                    event.setCancelled(true);
                break;
            case STRAY:
                if (ItemUtil.getDisguiseType(event.getTarget().getUniqueId()).equals(DisguiseType.STRAY))
                    event.setCancelled(true);
                break;
            case SPIDER:
                if (ItemUtil.getDisguiseType(event.getTarget().getUniqueId()).equals(DisguiseType.SPIDER))
                    event.setCancelled(true);
                break;
            case ENDERMAN:
                if (ItemUtil.getDisguiseType(event.getTarget().getUniqueId()).equals(DisguiseType.ENDERMAN))
                    event.setCancelled(true);
                break;
            case PIG_ZOMBIE:
                if (ItemUtil.getDisguiseType(event.getTarget().getUniqueId()).equals(DisguiseType.PIG_ZOMBIE))
                    event.setCancelled(true);
                break;
            case WITCH:
                if (ItemUtil.getDisguiseType(event.getTarget().getUniqueId()).equals(DisguiseType.WITCH))
                    event.setCancelled(true);
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void event0(EntityTargetLivingEntityEvent event) {
        if (!(event.getEntity() instanceof Monster))
            return;
        if (event.getTarget() == null)
            return;
        if (event.getTarget().getType().equals(EntityType.PLAYER))
            if (!ItemUtil.isDisguised(event.getTarget().getUniqueId()))
                ItemUtil.findTarget((Monster) event.getEntity());
    }

    @EventHandler
    public void event(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Monster))
            return;
        event.getEntity().getNearbyEntities(25, 25, 25).forEach((Entity entity) -> {
            if (entity instanceof Monster && entity instanceof LivingEntity
                    && ElementalsUtil.hasTag(entity, "corupted"))
                ItemUtil.findTarget((Monster) entity);
        });
    }

    @EventHandler
    public void event(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (ItemUtil.isDisguised(uuid)) {
            ItemUtil.getDisguiseTask(uuid).cancel();
            ItemUtil.removeDisguise(uuid);
        }
    }
}
