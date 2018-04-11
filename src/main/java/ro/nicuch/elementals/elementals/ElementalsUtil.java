package ro.nicuch.elementals.elementals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Husk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.material.Colorable;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import com.flowpowered.nbt.ByteTag;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.IntTag;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import net.citizensnpcs.api.CitizensAPI;
import ro.nicuch.elementals.Elementals;
import ro.nicuch.elementals.User;
import ro.nicuch.elementals.elementals.commands.RandomTpCommand;
import ro.nicuch.elementals.item.ItemUtil;
import ro.nicuch.elementals.protection.FieldUtil;
import ro.nicuch.tag.register.TagRegister;

public class ElementalsUtil {
    private static final Set<UUID> delayRandomTP = Sets.newHashSet();
    private static final Map<UUID, Integer> delayRandomTPCmd = Maps.newHashMap();
    private static boolean create;
    private static final Set<UUID> delayChat = Sets.newHashSet();
    private static boolean stopChat = false;
    private static String motd = new String(
            "§6PikaCraft §b- §cMinecraft la un alt nivel!\n§bUpdate: §ahttps://www.pikacraftmc.ro/");
    private static final List<String> autoMsg = Lists.newArrayList(
            "§aUtilizarea hack-urilor este pedepsita cu §4§lBAN§a!",
            "§bNu uita sa votezi in fiecare zi folosind comanda §f[§6/vote§f]§b! §aVei primi un premiu de fiecare data cand votezi. §6:)",
            "§bDaca esti nou pe server nu uita sa citesti regulamentul serverului!"
                    + " §ahttps://www.pikacraftmc.ro/wiki/regulament/",
            "§f[§6/sort§f] §bPoti sorta inventare! §a:D",
            "§aDaca ai nevoie de bani, scrie §f[§6/jobs§f] §asi i-ati un job!",
            "§6Protectia se face folosind cubul de §bDiamant§6! §aNu uita sa o pui altfel casa ta va fi distrusa!",
            "§aStaff-ul nu raspunde de itemele pierdute!",
            "§cDaca descoperiti un bug, va rugam sa-l raportati! §6Ve-ti primi un bonus daca bug-ul nu a fost raportat deja!"
                    + " §ahttps://www.pikacraftmc.ro/forums/raportati-bug-uri-probleme.7/",
            "§cPoti raporta §eJucatorii§c/§5Donatorii§c/§4Staff-ul §cserverului in cazul in care acestia §bincalca regulamentul§c!"
                    + " §ahttps://www.pikacraftmc.ro/forums/reclamatii.6/",
            "§6Asteptam sugestiile si ideile voastre! §ahttps://www.pikacraftmc.ro/forums/propuneri.8/",
            "§ePikaaa-pii! Pikaaa-chu! §a:)", "§cVa asteptam pe forum! §ahttps://www.pikacraftmc.ro/");

    public static List<String> getAutoMessages() {
        return autoMsg;
    }

    public enum NanoType {
        PICKAXE, SPADE, AXE
    }

    public static EntityType nameToEntity(String name) {
        switch (name) {
            case "§aSpawner <Zombie>":
                return EntityType.ZOMBIE;
            case "§aSpawner <Schelete>":
                return EntityType.SKELETON;
            case "§aSpawner <Blaze>":
                return EntityType.BLAZE;
            case "§aSpawner <Porc Zombie>":
                return EntityType.PIG_ZOMBIE;
            case "§aSpawner <Vrajitoare>":
                return EntityType.WITCH;
            case "§aSpawner <Iepure>":
                return EntityType.RABBIT;
            case "§aSpawner <Vaca>":
                return EntityType.COW;
            case "§aSpawner <Oaie>":
                return EntityType.SHEEP;
            case "§aSpawner <Urs Polar>":
                return EntityType.POLAR_BEAR;
            case "§aSpawner <Schelete Negru>":
                return EntityType.WITHER_SKELETON;
            case "§aSpawner <Golem de Fier>":
                return EntityType.IRON_GOLEM;
            case "§aSpawner <Porc>":
                return EntityType.PIG;
            case "§aSpawner <Paianjen>":
                return EntityType.SPIDER;
            case "§aSpawner <Paianjen Mic>":
                return EntityType.CAVE_SPIDER;
            case "§aSpawner <Enderman>":
                return EntityType.ENDERMAN;
            default:
                return EntityType.UNKNOWN;
        }
    }

    public static String entityToName(EntityType type) {
        switch (type) {
            case ZOMBIE:
                return "§aSpawner <Zombie>";
            case SKELETON:
                return "§aSpawner <Schelete>";
            case BLAZE:
                return "§aSpawner <Blaze>";
            case PIG_ZOMBIE:
                return "§aSpawner <Porc Zombie>";
            case WITCH:
                return "§aSpawner <Vrajitoare>";
            case RABBIT:
                return "§aSpawner <Iepure>";
            case COW:
                return "§aSpawner <Vaca>";
            case SHEEP:
                return "§aSpawner <Oaie>";
            case POLAR_BEAR:
                return "§aSpawner <Urs Polar>";
            case WITHER_SKELETON:
                return "§aSpawner <Schelete Negru>";
            case IRON_GOLEM:
                return "§aSpawner <Golem de Fier>";
            case PIG:
                return "§aSpawner <Porc>";
            case SPIDER:
                return "§aSpawner <Paianjen>";
            case CAVE_SPIDER:
                return "§aSpawner <Paianjen Mic>";
            case ENDERMAN:
                return "§aSpawner <Enderman>";
            default:
                return "§aSpawner <Necunoscut>";
        }
    }

    @SuppressWarnings("deprecation")
    public static void sortShulkerBox(User user) {
        Block block = user.getBase().getTargetBlock((Set<Material>) null, 3);
        Material blockType = block.getType();
        if (!(blockType.equals(Material.BLACK_SHULKER_BOX) || blockType.equals(Material.BLUE_SHULKER_BOX)
                || blockType.equals(Material.BROWN_SHULKER_BOX) || blockType.equals(Material.CYAN_SHULKER_BOX)
                || blockType.equals(Material.GRAY_SHULKER_BOX) || blockType.equals(Material.RED_SHULKER_BOX)
                || blockType.equals(Material.GREEN_SHULKER_BOX) || blockType.equals(Material.YELLOW_SHULKER_BOX)
                || blockType.equals(Material.MAGENTA_SHULKER_BOX) || blockType.equals(Material.LIGHT_BLUE_SHULKER_BOX)
                || blockType.equals(Material.LIME_SHULKER_BOX) || blockType.equals(Material.ORANGE_SHULKER_BOX)
                || blockType.equals(Material.PINK_SHULKER_BOX) || blockType.equals(Material.SILVER_SHULKER_BOX)
                || blockType.equals(Material.WHITE_SHULKER_BOX) || blockType.equals(Material.PURPLE_SHULKER_BOX))) {
            user.getBase().sendMessage("§cTrebuie sa te uiti la un shulker box!");
            return;
        }
        ShulkerBox shulker = (ShulkerBox) block.getState();
        if (FieldUtil.isFieldAtLocation(shulker.getLocation())) {
            if (!(FieldUtil.getFieldByLocation(shulker.getLocation()).isMember(user.getBase().getUniqueId())
                    || FieldUtil.getFieldByLocation(shulker.getLocation()).isOwner(user.getBase().getUniqueId())
                    || user.hasPermission("elementals.protection.override"))) {
                user.getBase().sendMessage("§cNu poti sorta acest shulker box!");
                return;
            }
        }
        Inventory inv = shulker.getInventory();
        List<ItemStack> sortedList = Lists.newArrayList();
        for (ItemStack item : inv.getContents()) {
            if (item == null || item.getType().equals(Material.AIR))
                continue;
            sortedList.add(item.clone());
        }
        inv.clear();
        Collections.sort(sortedList, ((ItemStack o1, ItemStack o2) -> {
            if (o1.getData().getData() < o2.getData().getData())
                return -1;
            else if (o1.getData().getData() > o2.getData().getData())
                return 1;
            else
                return 0;
        }));
        Collections.sort(sortedList, ((ItemStack o1, ItemStack o2) -> {
            if (!o1.hasItemMeta() && o2.hasItemMeta())
                return -1;
            else if (!o2.hasItemMeta() && o1.hasItemMeta())
                return 1;
            else if (!(o1.hasItemMeta() && o2.hasItemMeta()))
                return 0;
            if (!o1.getItemMeta().hasDisplayName() && o2.getItemMeta().hasDisplayName())
                return -1;
            else if (!o2.getItemMeta().hasDisplayName() && o1.getItemMeta().hasDisplayName())
                return 1;
            else if (!(o1.getItemMeta().hasDisplayName() && o2.getItemMeta().hasDisplayName()))
                return 0;
            if (o1.getItemMeta().getDisplayName() == null && o2.getItemMeta().getDisplayName() != null)
                return -1;
            else if (o2.getItemMeta().getDisplayName() == null && o1.getItemMeta().getDisplayName() != null)
                return 1;
            else if (!(o1.getItemMeta().getDisplayName() == null && o2.getItemMeta().getDisplayName() == null))
                return 0;
            return o1.getItemMeta().getDisplayName().compareTo(o2.getItemMeta().getDisplayName());
        }));
        Collections.sort(sortedList, ((ItemStack o1, ItemStack o2) -> {
            return o1.getType().compareTo(o2.getType());
        }));
        for (ItemStack item : sortedList)
            inv.addItem(item);
        user.getBase().sendMessage("§aShulker Box-ul a fost sortat!");
    }

    @SuppressWarnings("deprecation")
    public static void sortInventory(User user) {
        List<ItemStack> sortedList = Lists.newArrayList();
        ItemStack s0 = user.getBase().getInventory().getItem(0) == null ? new ItemStack(Material.AIR)
                : user.getBase().getInventory().getItem(0).clone();
        ItemStack s1 = user.getBase().getInventory().getItem(1) == null ? new ItemStack(Material.AIR)
                : user.getBase().getInventory().getItem(1).clone();
        ItemStack s2 = user.getBase().getInventory().getItem(2) == null ? new ItemStack(Material.AIR)
                : user.getBase().getInventory().getItem(2).clone();
        ItemStack s3 = user.getBase().getInventory().getItem(3) == null ? new ItemStack(Material.AIR)
                : user.getBase().getInventory().getItem(3).clone();
        ItemStack s4 = user.getBase().getInventory().getItem(4) == null ? new ItemStack(Material.AIR)
                : user.getBase().getInventory().getItem(4).clone();
        ItemStack s5 = user.getBase().getInventory().getItem(5) == null ? new ItemStack(Material.AIR)
                : user.getBase().getInventory().getItem(5).clone();
        ItemStack s6 = user.getBase().getInventory().getItem(6) == null ? new ItemStack(Material.AIR)
                : user.getBase().getInventory().getItem(6).clone();
        ItemStack s7 = user.getBase().getInventory().getItem(7) == null ? new ItemStack(Material.AIR)
                : user.getBase().getInventory().getItem(7).clone();
        ItemStack s8 = user.getBase().getInventory().getItem(8) == null ? new ItemStack(Material.AIR)
                : user.getBase().getInventory().getItem(8).clone();
        for (int i = 9; i < 36; i++) {
            ItemStack item = user.getBase().getInventory().getItem(i);
            if (item == null || item.getType().equals(Material.AIR))
                continue;
            sortedList.add(item.clone());
            user.getBase().getInventory().setItem(i, new ItemStack(Material.AIR));
        }
        user.getBase().getInventory().setItem(0, new ItemStack(Material.STAINED_GLASS_PANE));
        user.getBase().getInventory().setItem(1, new ItemStack(Material.STAINED_GLASS_PANE));
        user.getBase().getInventory().setItem(2, new ItemStack(Material.STAINED_GLASS_PANE));
        user.getBase().getInventory().setItem(3, new ItemStack(Material.STAINED_GLASS_PANE));
        user.getBase().getInventory().setItem(4, new ItemStack(Material.STAINED_GLASS_PANE));
        user.getBase().getInventory().setItem(5, new ItemStack(Material.STAINED_GLASS_PANE));
        user.getBase().getInventory().setItem(6, new ItemStack(Material.STAINED_GLASS_PANE));
        user.getBase().getInventory().setItem(7, new ItemStack(Material.STAINED_GLASS_PANE));
        user.getBase().getInventory().setItem(8, new ItemStack(Material.STAINED_GLASS_PANE));
        Collections.sort(sortedList, ((ItemStack o1, ItemStack o2) -> {
            if (o1.getData().getData() < o2.getData().getData())
                return -1;
            else if (o1.getData().getData() > o2.getData().getData())
                return 1;
            else
                return 0;
        }));
        Collections.sort(sortedList, ((ItemStack o1, ItemStack o2) -> {
            if (!o1.hasItemMeta() && o2.hasItemMeta())
                return -1;
            else if (!o2.hasItemMeta() && o1.hasItemMeta())
                return 1;
            else if (!(o1.hasItemMeta() && o2.hasItemMeta()))
                return 0;
            if (!o1.getItemMeta().hasDisplayName() && o2.getItemMeta().hasDisplayName())
                return -1;
            else if (!o2.getItemMeta().hasDisplayName() && o1.getItemMeta().hasDisplayName())
                return 1;
            else if (!(o1.getItemMeta().hasDisplayName() && o2.getItemMeta().hasDisplayName()))
                return 0;
            if (o1.getItemMeta().getDisplayName() == null && o2.getItemMeta().getDisplayName() != null)
                return -1;
            else if (o2.getItemMeta().getDisplayName() == null && o1.getItemMeta().getDisplayName() != null)
                return 1;
            else if (!(o1.getItemMeta().getDisplayName() == null && o2.getItemMeta().getDisplayName() == null))
                return 0;
            return o1.getItemMeta().getDisplayName().compareTo(o2.getItemMeta().getDisplayName());
        }));
        Collections.sort(sortedList, ((ItemStack o1, ItemStack o2) -> {
            return o1.getType().compareTo(o2.getType());
        }));
        for (ItemStack item : sortedList)
            user.getBase().getInventory().addItem(item);
        user.getBase().getInventory().setItem(0, s0);
        user.getBase().getInventory().setItem(1, s1);
        user.getBase().getInventory().setItem(2, s2);
        user.getBase().getInventory().setItem(3, s3);
        user.getBase().getInventory().setItem(4, s4);
        user.getBase().getInventory().setItem(5, s5);
        user.getBase().getInventory().setItem(6, s6);
        user.getBase().getInventory().setItem(7, s7);
        user.getBase().getInventory().setItem(8, s8);
        user.getBase().sendMessage("§aInventarul a fost sortat!");
    }

    @SuppressWarnings("deprecation")
    public static void sortEnderChest(User user) {
        List<ItemStack> sortedList = Lists.newArrayList();
        for (ItemStack item : user.getBase().getEnderChest()) {
            if (item == null || item.getType().equals(Material.AIR))
                continue;
            sortedList.add(item.clone());
        }
        user.getBase().getEnderChest().clear();
        Collections.sort(sortedList, ((ItemStack o1, ItemStack o2) -> {
            if (o1.getData().getData() < o2.getData().getData())
                return -1;
            else if (o1.getData().getData() > o2.getData().getData())
                return 1;
            else
                return 0;
        }));
        Collections.sort(sortedList, ((ItemStack o1, ItemStack o2) -> {
            if (!o1.hasItemMeta() && o2.hasItemMeta())
                return -1;
            else if (!o2.hasItemMeta() && o1.hasItemMeta())
                return 1;
            else if (!(o1.hasItemMeta() && o2.hasItemMeta()))
                return 0;
            if (!o1.getItemMeta().hasDisplayName() && o2.getItemMeta().hasDisplayName())
                return -1;
            else if (!o2.getItemMeta().hasDisplayName() && o1.getItemMeta().hasDisplayName())
                return 1;
            else if (!(o1.getItemMeta().hasDisplayName() && o2.getItemMeta().hasDisplayName()))
                return 0;
            if (o1.getItemMeta().getDisplayName() == null && o2.getItemMeta().getDisplayName() != null)
                return -1;
            else if (o2.getItemMeta().getDisplayName() == null && o1.getItemMeta().getDisplayName() != null)
                return 1;
            else if (!(o1.getItemMeta().getDisplayName() == null && o2.getItemMeta().getDisplayName() == null))
                return 0;
            return o1.getItemMeta().getDisplayName().compareTo(o2.getItemMeta().getDisplayName());
        }));
        Collections.sort(sortedList, ((ItemStack o1, ItemStack o2) -> {
            return o1.getType().compareTo(o2.getType());
        }));
        for (ItemStack item : sortedList)
            user.getBase().getEnderChest().addItem(item);
        user.getBase().sendMessage("§aEnder Chest-ul a fost sortat!");
    }

    @SuppressWarnings("deprecation")
    public static void sortChest(User user) {
        Block block = user.getBase().getTargetBlock((Set<Material>) null, 3);
        if (!(block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST))) {
            user.getBase().sendMessage("§cTrebuie sa te uiti la un cufar!");
            return;
        }
        Chest chest = (Chest) block.getState();
        if (FieldUtil.isFieldAtLocation(chest.getLocation())) {
            if (!(FieldUtil.getFieldByLocation(chest.getLocation()).isMember(user.getBase().getUniqueId())
                    || FieldUtil.getFieldByLocation(chest.getLocation()).isOwner(user.getBase().getUniqueId())
                    || user.hasPermission("elementals.protection.override"))) {
                user.getBase().sendMessage("§cNu poti sorta acest cufar!");
                return;
            }
        }
        Inventory inv = chest.getInventory();
        List<ItemStack> sortedList = Lists.newArrayList();
        for (ItemStack item : inv.getContents()) {
            if (item == null || item.getType().equals(Material.AIR))
                continue;
            sortedList.add(item.clone());
        }
        inv.clear();
        Collections.sort(sortedList, ((ItemStack o1, ItemStack o2) -> {
            if (o1.getData().getData() < o2.getData().getData())
                return -1;
            else if (o1.getData().getData() > o2.getData().getData())
                return 1;
            else
                return 0;
        }));
        Collections.sort(sortedList, ((ItemStack o1, ItemStack o2) -> {
            if (!o1.hasItemMeta() && o2.hasItemMeta())
                return -1;
            else if (!o2.hasItemMeta() && o1.hasItemMeta())
                return 1;
            else if (!(o1.hasItemMeta() && o2.hasItemMeta()))
                return 0;
            if (!o1.getItemMeta().hasDisplayName() && o2.getItemMeta().hasDisplayName())
                return -1;
            else if (!o2.getItemMeta().hasDisplayName() && o1.getItemMeta().hasDisplayName())
                return 1;
            else if (!(o1.getItemMeta().hasDisplayName() && o2.getItemMeta().hasDisplayName()))
                return 0;
            if (o1.getItemMeta().getDisplayName() == null && o2.getItemMeta().getDisplayName() != null)
                return -1;
            else if (o2.getItemMeta().getDisplayName() == null && o1.getItemMeta().getDisplayName() != null)
                return 1;
            else if (!(o1.getItemMeta().getDisplayName() == null && o2.getItemMeta().getDisplayName() == null))
                return 0;
            return o1.getItemMeta().getDisplayName().compareTo(o2.getItemMeta().getDisplayName());
        }));
        Collections.sort(sortedList, ((ItemStack o1, ItemStack o2) -> {
            return o1.getType().compareTo(o2.getType());
        }));
        for (ItemStack item : sortedList)
            inv.addItem(item);
        user.getBase().sendMessage("§aCufarul a fost sortat!");
    }

    // TODO Test for line
    public static List<Location> getPlayerDirectionLine(Player player, double distance, double addition, boolean bdet) {
        List<Location> list = Lists.newArrayList();
        double pitch = Math.toRadians(player.getLocation().getPitch() + 90);
        double yaw = Math.toRadians(player.getLocation().getYaw() + 90);
        double dx = Math.sin(pitch) * Math.cos(yaw);
        double dz = Math.sin(pitch) * Math.sin(yaw);
        double dy = Math.cos(pitch);
        double dxa = dx * addition;
        double dya = dy * addition;
        double dza = dz * addition;
        double xM = dxa;
        double yM = dya;
        double zM = dza;
        Location loc = player.getEyeLocation().clone();
        while (player.getEyeLocation().distance(loc) <= distance) {
            loc = loc.add(xM, yM, zM);
            list.add(loc.clone());
            if (loc.getBlock().getType().isSolid() && bdet)
                break;
            xM += dxa;
            yM += dya;
            zM += dza;
        }
        return list;
    }

    public static Entity getTargetEntity(Player player, double distance) {
        for (Location loc : getPlayerDirectionLine(player, distance, 0.025, true)) {
            for (Entity entity : player.getNearbyEntities(distance + 1, distance + 1, distance + 1)) {
                if (getBoundingBox(entity).check(loc))
                    return entity;
            }
        }
        return null;
    }

    public static BoundingBox getBoundingBox(Entity entity) {
        switch (entity.getType()) {
            case ARMOR_STAND:
                return new BoundingBox(0.5, 1.8, 0.5, entity.getLocation());
            case ARROW:
            case SPECTRAL_ARROW:
            case TIPPED_ARROW:
                return new BoundingBox(0.5, 0.5, 0.5, entity.getLocation());
            case BAT:
            case VEX:
                return new BoundingBox(0.4, 0.4, 0.4, entity.getLocation());
            case BLAZE:
                return new BoundingBox(0.6, 1.8, 0.6, entity.getLocation());
            case BOAT:
                return new BoundingBox(1.5, 0.6, 1.5, entity.getLocation());
            case CAVE_SPIDER:
                return new BoundingBox(0.9, 0.6, 0.9, entity.getLocation());
            case CHICKEN:
                Chicken chicken = (Chicken) entity;
                if (chicken.isAdult())
                    return new BoundingBox(0.6, 0.6, 0.6, entity.getLocation());
                else
                    return new BoundingBox(0.3, 0.3, 0.3, entity.getLocation());
            case COW:
            case MUSHROOM_COW:
                Cow cow = (Cow) entity;
                if (cow.isAdult())
                    return new BoundingBox(1.4, 1.4, 1.4, entity.getLocation());
                else
                    return new BoundingBox(0.7, 0.7, 0.7, entity.getLocation());
            case SHEEP:
                Sheep sheep = (Sheep) entity;
                if (sheep.isAdult())
                    return new BoundingBox(1.4, 1.4, 1.4, entity.getLocation());
                else
                    return new BoundingBox(0.7, 0.7, 0.7, entity.getLocation());
            case CREEPER:
                return new BoundingBox(0.55, 1.8, 0.55, entity.getLocation());
            case DRAGON_FIREBALL:
            case FIREBALL:
            case WITHER_SKULL:
                return new BoundingBox(0.8, 0.8, 0.8, entity.getLocation());
            case DROPPED_ITEM:
            case EXPERIENCE_ORB:
            case EGG:
            case SNOWBALL:
            case ENDER_PEARL:
            case SHULKER_BULLET:
            case ENDER_SIGNAL:
                return new BoundingBox(0.25, 0.25, 0.25, entity.getLocation());
            case ENDER_CRYSTAL:
            case EVOKER_FANGS:
                return new BoundingBox(0.95, 1.2, 0.95, entity.getLocation());
            case ENDER_DRAGON:
                return new BoundingBox(2.5, 2.5, 2.5, entity.getLocation());
            case ENDERMAN:
                return new BoundingBox(0.6, 2.9, 0.6, entity.getLocation());
            case ENDERMITE:
            case SILVERFISH:
                return new BoundingBox(0.213, 0.15, 0.213, entity.getLocation());
            case GHAST:
                return new BoundingBox(3.5, 3.5, 3.5, entity.getLocation());
            case GIANT:
                return new BoundingBox(3.6, 12, 3.6, entity.getLocation());
            case GUARDIAN:
                return new BoundingBox(0.85, 0.85, 0.85, entity.getLocation());
            case ELDER_GUARDIAN:
                return new BoundingBox(2.5, 2.5, 2.5, entity.getLocation());
            case HORSE:
            case MULE:
            case DONKEY:
            case ZOMBIE_HORSE:
            case SKELETON_HORSE:
                AbstractHorse horse = (AbstractHorse) entity;
                if (horse.isAdult())
                    return new BoundingBox(1.4, 1.6, 1.4, entity.getLocation());
                else
                    return new BoundingBox(0.7, 0.8, 0.7, entity.getLocation());
            case IRON_GOLEM:
                return new BoundingBox(1.4, 2.7, 1.4, entity.getLocation());
            case ITEM_FRAME:
            case FIREWORK:
            case FISHING_HOOK:
            case LIGHTNING:
            case PAINTING:
            case SPLASH_POTION:
            case LINGERING_POTION:
            case THROWN_EXP_BOTTLE:
            case UNKNOWN:
            case AREA_EFFECT_CLOUD:
            case WEATHER:
                return new BoundingBox(0.0, 0.0, 0.0, entity.getLocation());
            case MAGMA_CUBE:
            case SLIME:
                Slime slime = (Slime) entity;
                return new BoundingBox(0.5 * slime.getSize(), 0.5 * slime.getSize(), 0.5 * slime.getSize(),
                        entity.getLocation());
            case MINECART:
            case MINECART_CHEST:
            case MINECART_COMMAND:
            case MINECART_FURNACE:
            case MINECART_HOPPER:
            case MINECART_MOB_SPAWNER:
            case MINECART_TNT:
                return new BoundingBox(0.98, 0.7, 0.98, entity.getLocation());
            case OCELOT:
                Ocelot ocelot = (Ocelot) entity;
                if (ocelot.isAdult())
                    return new BoundingBox(0.875, 0.5, 0.875, entity.getLocation());
                else
                    return new BoundingBox(0.432, 0.25, 0.432, entity.getLocation());
            case PIG:
                Pig pig = (Pig) entity;
                if (pig.isAdult())
                    return new BoundingBox(0.875, 0.875, 0.875, entity.getLocation());
                else
                    return new BoundingBox(0.432, 0.432, 0.432, entity.getLocation());
            case POLAR_BEAR:
                PolarBear polar_bear = (PolarBear) entity;
                if (polar_bear.isAdult())
                    return new BoundingBox(1.3, 1.4, 1.3, entity.getLocation());
                else
                    return new BoundingBox(0.65, 0.7, 0.65, entity.getLocation());
            case PRIMED_TNT:
            case FALLING_BLOCK:
                return new BoundingBox(0.98, 0.98, 0.98, entity.getLocation());
            case RABBIT:
                Rabbit rabbit = (Rabbit) entity;
                if (rabbit.isAdult())
                    return new BoundingBox(0.4, 0.4, 0.4, entity.getLocation());
                else
                    return new BoundingBox(0.2, 0.2, 0.2, entity.getLocation());
            case SHULKER:
                return new BoundingBox(1.0, 1.0, 1.0, entity.getLocation());
            case SMALL_FIREBALL:
                return new BoundingBox(0.3125, 0.3125, 0.3125, entity.getLocation());
            case SPIDER:
                return new BoundingBox(1.5, 1, 1.5, entity.getLocation());
            case SQUID:
                return new BoundingBox(0.9, 1.15, 0.9, entity.getLocation());
            case WITCH:
                return new BoundingBox(0.6, 1.8, 0.6, entity.getLocation());
            case WITHER:
                return new BoundingBox(3.0, 2.5, 3.0, entity.getLocation());
            case WOLF:
                Wolf wolf = (Wolf) entity;
                if (wolf.isAdult())
                    return new BoundingBox(0.85, 0.425, 0.85, entity.getLocation());
                else
                    return new BoundingBox(0.425, 0.24, 0.425, entity.getLocation());
            case ZOMBIE:
            case PIG_ZOMBIE:
            case ZOMBIE_VILLAGER:
                Zombie zombie = (Zombie) entity;
                if (zombie.isBaby())
                    return new BoundingBox(0.3, 0.9, 0.3, entity.getLocation());
                else
                    return new BoundingBox(0.6, 1.8, 0.6, entity.getLocation());
            case HUSK:
                Husk husk = (Husk) entity;
                if (husk.isBaby())
                    return new BoundingBox(0.3, 1.0, 0.3, entity.getLocation());
                else
                    return new BoundingBox(0.6, 2.0, 0.6, entity.getLocation());
            case VILLAGER:
                Villager villager = (Villager) entity;
                if (villager.isAdult())
                    return new BoundingBox(0.6, 1.8, 0.6, entity.getLocation());
                else
                    return new BoundingBox(0.3, 0.9, 0.3, entity.getLocation());
            case SKELETON:
            case STRAY:
            case PLAYER:
            case SNOWMAN:
            case VINDICATOR:
            case EVOKER:
                return new BoundingBox(0.6, 1.8, 0.6, entity.getLocation());
            case WITHER_SKELETON:
                return new BoundingBox(0.6, 2.0, 0.6, entity.getLocation());
            case LLAMA:
                Llama llama = (Llama) entity;
                if (llama.isAdult())
                    return new BoundingBox(1.4, 1.6, 1.4, entity.getLocation());
                else
                    return new BoundingBox(0.7, 0.8, 0.7, entity.getLocation());
            default:
                return new BoundingBox(0.6, 1.8, 0.6, entity.getLocation());
        }
    }

    public static void generateMaze(Block block, int mat, int width, int leght, int height, int size) {
        try {
            WorldEditPlugin we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
            we.getWorldEdit().runScript(we.wrapPlayer(new FakePlayer(block.getLocation().add(0.5, 0, 0.5))),
                    new File(we.getDataFolder() + File.separator + "craftscripts" + File.separator + "maze.js"),
                    new String[]{"", String.valueOf(mat), String.valueOf(width), String.valueOf(leght),
                            String.valueOf(height), String.valueOf(size), "i,e"});
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date());
    }

    public static void tickMotd() {
        int random = nextInt(2);
        switch (random) {
            case 0:
                motd = new String(
                        "§6PikaCraft §b- §aMinecraft la un alt nivel!\n§bUpdate: §ahttps://www.pikacraftmc.ro/");
                break;
            case 1:
                motd = new String(
                        "§6PikaCraft §b- §5Minecraft la un alt nivel!\n§bUpdate: §ahttps://www.pikacraftmc.ro/");
                break;
            default:
                motd = new String(
                        "§6PikaCraft §b- §cMinecraft la un alt nivel!\n§bUpdate: §ahttps://www.pikacraftmc.ro/");
                break;
        }
    }

    public static String getMotd() {
        return motd;
    }

    public static Vector getRandomVector() {
        double x = nextDouble(2) - nextDouble(2);
        double y = nextDouble(2) - nextDouble(2);
        double z = nextDouble(2) - nextDouble(2);
        return new Vector(x, y, z);
    }

    public static Firework randomFirework(Location loc) {
        Firework fw = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta fm = fw.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder()
                .withColor(Color.fromBGR(ElementalsUtil.nextInt(255), ElementalsUtil.nextInt(255),
                        ElementalsUtil.nextInt(255)))
                .with(Type.values()[ElementalsUtil.nextInt(Type.values().length)]).flicker(ElementalsUtil.nextBoolean())
                .trail(ElementalsUtil.nextBoolean()).withColor(Color.fromBGR(ElementalsUtil.nextInt(255),
                        ElementalsUtil.nextInt(255), ElementalsUtil.nextInt(255)))
                .build());
        fm.setPower(1);
        fw.setFireworkMeta(fm);
        return fw;
    }

    public static boolean isInt(String arg) {
        try {
            Integer.parseInt(arg);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static int nextInt(int max) {
        return (int) nextDouble(max);
    }

    public static double nextDouble(double max) {
        return Math.random() * max;
    }

    public static boolean nextBoolean() {
        return (nextDouble(1) >= 0.5);
    }

    public static String arrayToString(String[] args, String spliter) {
        return Arrays.stream(args).collect(Collectors.joining(spliter));
    }

    // TODO nano
    public static void breakNano(User user, int pitch, int yaw, Block block, NanoType type) {
        switch (type) {
            case PICKAXE:
                if (user.getNanoPickType().equals(Elementals.Nano.B)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.NORTH);
                        if (b1.getType().equals(block.getType()))
                            breakNaturaly(b1, user.getBase());
                        Block b2 = block.getRelative(BlockFace.SOUTH);
                        if (b2.getType().equals(block.getType()))
                            breakNaturaly(b2, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.EAST);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.WEST);
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.SOUTH);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.NORTH);
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                        }
                    }
                } else if (user.getNanoPickType().equals(Elementals.Nano.C)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.EAST);
                        if (b1.getType().equals(block.getType()))
                            breakNaturaly(b1, user.getBase());
                        Block b2 = block.getRelative(BlockFace.WEST);
                        if (b2.getType().equals(block.getType()))
                            breakNaturaly(b2, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                        }
                    }
                } else if (user.getNanoPickType().equals(Elementals.Nano.D)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.EAST);
                        Block b2 = block.getRelative(BlockFace.WEST);
                        Block b3 = block.getRelative(BlockFace.NORTH);
                        Block b4 = block.getRelative(BlockFace.SOUTH);
                        Block b5 = block.getRelative(BlockFace.NORTH_EAST);
                        Block b6 = block.getRelative(BlockFace.NORTH_WEST);
                        Block b7 = block.getRelative(BlockFace.SOUTH_EAST);
                        Block b8 = block.getRelative(BlockFace.SOUTH_WEST);
                        if (b1.getType().equals(block.getType()))
                            breakNaturaly(b1, user.getBase());
                        if (b2.getType().equals(block.getType()))
                            breakNaturaly(b2, user.getBase());
                        if (b3.getType().equals(block.getType()))
                            breakNaturaly(b3, user.getBase());
                        if (b4.getType().equals(block.getType()))
                            breakNaturaly(b4, user.getBase());
                        if (b5.getType().equals(block.getType()))
                            breakNaturaly(b5, user.getBase());
                        if (b6.getType().equals(block.getType()))
                            breakNaturaly(b6, user.getBase());
                        if (b7.getType().equals(block.getType()))
                            breakNaturaly(b7, user.getBase());
                        if (b8.getType().equals(block.getType()))
                            breakNaturaly(b8, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            Block b3 = block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST);
                            Block b4 = block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST);
                            Block b5 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST);
                            Block b6 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST);
                            Block b7 = block.getRelative(BlockFace.EAST);
                            Block b8 = block.getRelative(BlockFace.WEST);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                            if (b3.getType().equals(block.getType()))
                                breakNaturaly(b3, user.getBase());
                            if (b4.getType().equals(block.getType()))
                                breakNaturaly(b4, user.getBase());
                            if (b5.getType().equals(block.getType()))
                                breakNaturaly(b5, user.getBase());
                            if (b6.getType().equals(block.getType()))
                                breakNaturaly(b6, user.getBase());
                            if (b7.getType().equals(block.getType()))
                                breakNaturaly(b7, user.getBase());
                            if (b8.getType().equals(block.getType()))
                                breakNaturaly(b8, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            Block b3 = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH);
                            Block b4 = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH);
                            Block b5 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH);
                            Block b6 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH);
                            Block b7 = block.getRelative(BlockFace.SOUTH);
                            Block b8 = block.getRelative(BlockFace.NORTH);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                            if (b3.getType().equals(block.getType()))
                                breakNaturaly(b3, user.getBase());
                            if (b4.getType().equals(block.getType()))
                                breakNaturaly(b4, user.getBase());
                            if (b5.getType().equals(block.getType()))
                                breakNaturaly(b5, user.getBase());
                            if (b6.getType().equals(block.getType()))
                                breakNaturaly(b6, user.getBase());
                            if (b7.getType().equals(block.getType()))
                                breakNaturaly(b7, user.getBase());
                            if (b8.getType().equals(block.getType()))
                                breakNaturaly(b8, user.getBase());
                        }
                    }
                }
                break;
            case AXE:
                if (user.getNanoAxeType().equals(Elementals.Nano.B)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.NORTH);
                        if (b1.getType().equals(block.getType()))
                            breakNaturaly(b1, user.getBase());
                        Block b2 = block.getRelative(BlockFace.SOUTH);
                        if (b2.getType().equals(block.getType()))
                            breakNaturaly(b2, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.EAST);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.WEST);
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.SOUTH);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.NORTH);
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                        }
                    }
                } else if (user.getNanoAxeType().equals(Elementals.Nano.C)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.EAST);
                        if (b1.getType().equals(block.getType()))
                            breakNaturaly(b1, user.getBase());
                        Block b2 = block.getRelative(BlockFace.WEST);
                        if (b2.getType().equals(block.getType()))
                            breakNaturaly(b2, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                        }
                    }
                } else if (user.getNanoAxeType().equals(Elementals.Nano.D)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.EAST);
                        Block b2 = block.getRelative(BlockFace.WEST);
                        Block b3 = block.getRelative(BlockFace.NORTH);
                        Block b4 = block.getRelative(BlockFace.SOUTH);
                        Block b5 = block.getRelative(BlockFace.NORTH_EAST);
                        Block b6 = block.getRelative(BlockFace.NORTH_WEST);
                        Block b7 = block.getRelative(BlockFace.SOUTH_EAST);
                        Block b8 = block.getRelative(BlockFace.SOUTH_WEST);
                        if (b1.getType().equals(block.getType()))
                            breakNaturaly(b1, user.getBase());
                        if (b2.getType().equals(block.getType()))
                            breakNaturaly(b2, user.getBase());
                        if (b3.getType().equals(block.getType()))
                            breakNaturaly(b3, user.getBase());
                        if (b4.getType().equals(block.getType()))
                            breakNaturaly(b4, user.getBase());
                        if (b5.getType().equals(block.getType()))
                            breakNaturaly(b5, user.getBase());
                        if (b6.getType().equals(block.getType()))
                            breakNaturaly(b6, user.getBase());
                        if (b7.getType().equals(block.getType()))
                            breakNaturaly(b7, user.getBase());
                        if (b8.getType().equals(block.getType()))
                            breakNaturaly(b8, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            Block b3 = block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST);
                            Block b4 = block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST);
                            Block b5 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST);
                            Block b6 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST);
                            Block b7 = block.getRelative(BlockFace.EAST);
                            Block b8 = block.getRelative(BlockFace.WEST);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                            if (b3.getType().equals(block.getType()))
                                breakNaturaly(b3, user.getBase());
                            if (b4.getType().equals(block.getType()))
                                breakNaturaly(b4, user.getBase());
                            if (b5.getType().equals(block.getType()))
                                breakNaturaly(b5, user.getBase());
                            if (b6.getType().equals(block.getType()))
                                breakNaturaly(b6, user.getBase());
                            if (b7.getType().equals(block.getType()))
                                breakNaturaly(b7, user.getBase());
                            if (b8.getType().equals(block.getType()))
                                breakNaturaly(b8, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            Block b3 = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH);
                            Block b4 = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH);
                            Block b5 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH);
                            Block b6 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH);
                            Block b7 = block.getRelative(BlockFace.SOUTH);
                            Block b8 = block.getRelative(BlockFace.NORTH);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                            if (b3.getType().equals(block.getType()))
                                breakNaturaly(b3, user.getBase());
                            if (b4.getType().equals(block.getType()))
                                breakNaturaly(b4, user.getBase());
                            if (b5.getType().equals(block.getType()))
                                breakNaturaly(b5, user.getBase());
                            if (b6.getType().equals(block.getType()))
                                breakNaturaly(b6, user.getBase());
                            if (b7.getType().equals(block.getType()))
                                breakNaturaly(b7, user.getBase());
                            if (b8.getType().equals(block.getType()))
                                breakNaturaly(b8, user.getBase());
                        }
                    }
                }
                break;
            case SPADE:
                if (user.getNanoSpadeType().equals(Elementals.Nano.B)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.NORTH);
                        if (b1.getType().equals(block.getType()))
                            breakNaturaly(b1, user.getBase());
                        Block b2 = block.getRelative(BlockFace.SOUTH);
                        if (b2.getType().equals(block.getType()))
                            breakNaturaly(b2, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.EAST);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.WEST);
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.SOUTH);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.NORTH);
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                        }
                    }
                } else if (user.getNanoSpadeType().equals(Elementals.Nano.C)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.EAST);
                        if (b1.getType().equals(block.getType()))
                            breakNaturaly(b1, user.getBase());
                        Block b2 = block.getRelative(BlockFace.WEST);
                        if (b2.getType().equals(block.getType()))
                            breakNaturaly(b2, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                        }
                    }
                } else if (user.getNanoSpadeType().equals(Elementals.Nano.D)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.EAST);
                        Block b2 = block.getRelative(BlockFace.WEST);
                        Block b3 = block.getRelative(BlockFace.NORTH);
                        Block b4 = block.getRelative(BlockFace.SOUTH);
                        Block b5 = block.getRelative(BlockFace.NORTH_EAST);
                        Block b6 = block.getRelative(BlockFace.NORTH_WEST);
                        Block b7 = block.getRelative(BlockFace.SOUTH_EAST);
                        Block b8 = block.getRelative(BlockFace.SOUTH_WEST);
                        if (b1.getType().equals(block.getType()))
                            breakNaturaly(b1, user.getBase());
                        if (b2.getType().equals(block.getType()))
                            breakNaturaly(b2, user.getBase());
                        if (b3.getType().equals(block.getType()))
                            breakNaturaly(b3, user.getBase());
                        if (b4.getType().equals(block.getType()))
                            breakNaturaly(b4, user.getBase());
                        if (b5.getType().equals(block.getType()))
                            breakNaturaly(b5, user.getBase());
                        if (b6.getType().equals(block.getType()))
                            breakNaturaly(b6, user.getBase());
                        if (b7.getType().equals(block.getType()))
                            breakNaturaly(b7, user.getBase());
                        if (b8.getType().equals(block.getType()))
                            breakNaturaly(b8, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            Block b3 = block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST);
                            Block b4 = block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST);
                            Block b5 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST);
                            Block b6 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST);
                            Block b7 = block.getRelative(BlockFace.EAST);
                            Block b8 = block.getRelative(BlockFace.WEST);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                            if (b3.getType().equals(block.getType()))
                                breakNaturaly(b3, user.getBase());
                            if (b4.getType().equals(block.getType()))
                                breakNaturaly(b4, user.getBase());
                            if (b5.getType().equals(block.getType()))
                                breakNaturaly(b5, user.getBase());
                            if (b6.getType().equals(block.getType()))
                                breakNaturaly(b6, user.getBase());
                            if (b7.getType().equals(block.getType()))
                                breakNaturaly(b7, user.getBase());
                            if (b8.getType().equals(block.getType()))
                                breakNaturaly(b8, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            Block b3 = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH);
                            Block b4 = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH);
                            Block b5 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH);
                            Block b6 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH);
                            Block b7 = block.getRelative(BlockFace.SOUTH);
                            Block b8 = block.getRelative(BlockFace.NORTH);
                            if (b1.getType().equals(block.getType()))
                                breakNaturaly(b1, user.getBase());
                            if (b2.getType().equals(block.getType()))
                                breakNaturaly(b2, user.getBase());
                            if (b3.getType().equals(block.getType()))
                                breakNaturaly(b3, user.getBase());
                            if (b4.getType().equals(block.getType()))
                                breakNaturaly(b4, user.getBase());
                            if (b5.getType().equals(block.getType()))
                                breakNaturaly(b5, user.getBase());
                            if (b6.getType().equals(block.getType()))
                                breakNaturaly(b6, user.getBase());
                            if (b7.getType().equals(block.getType()))
                                breakNaturaly(b7, user.getBase());
                            if (b8.getType().equals(block.getType()))
                                breakNaturaly(b8, user.getBase());
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    public static void breakNanoX(User user, int pitch, int yaw, Block block, NanoType type) {
        switch (type) {
            case PICKAXE:
                if (user.getNanoPickType().equals(Elementals.Nano.B)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.NORTH);
                        if (b1.getType().equals(Material.REDSTONE_ORE)
                                || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b1, user.getBase());
                        Block b2 = block.getRelative(BlockFace.SOUTH);
                        if (b2.getType().equals(Material.REDSTONE_ORE)
                                || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b2, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.EAST);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.WEST);
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.SOUTH);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.NORTH);
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                        }
                    }
                } else if (user.getNanoPickType().equals(Elementals.Nano.C)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.EAST);
                        if (b1.getType().equals(Material.REDSTONE_ORE)
                                || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b1, user.getBase());
                        Block b2 = block.getRelative(BlockFace.WEST);
                        if (b2.getType().equals(Material.REDSTONE_ORE)
                                || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b2, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                        }
                    }
                } else if (user.getNanoPickType().equals(Elementals.Nano.D)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.EAST);
                        Block b2 = block.getRelative(BlockFace.WEST);
                        Block b3 = block.getRelative(BlockFace.NORTH);
                        Block b4 = block.getRelative(BlockFace.SOUTH);
                        Block b5 = block.getRelative(BlockFace.NORTH_EAST);
                        Block b6 = block.getRelative(BlockFace.NORTH_WEST);
                        Block b7 = block.getRelative(BlockFace.SOUTH_EAST);
                        Block b8 = block.getRelative(BlockFace.SOUTH_WEST);
                        if (b1.getType().equals(Material.REDSTONE_ORE)
                                || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b1, user.getBase());
                        if (b2.getType().equals(Material.REDSTONE_ORE)
                                || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b2, user.getBase());
                        if (b3.getType().equals(Material.REDSTONE_ORE)
                                || b3.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b3, user.getBase());
                        if (b4.getType().equals(Material.REDSTONE_ORE)
                                || b4.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b4, user.getBase());
                        if (b5.getType().equals(Material.REDSTONE_ORE)
                                || b5.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b5, user.getBase());
                        if (b6.getType().equals(Material.REDSTONE_ORE)
                                || b6.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b6, user.getBase());
                        if (b7.getType().equals(Material.REDSTONE_ORE)
                                || b7.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b7, user.getBase());
                        if (b8.getType().equals(Material.REDSTONE_ORE)
                                || b8.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b8, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            Block b3 = block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST);
                            Block b4 = block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST);
                            Block b5 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST);
                            Block b6 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST);
                            Block b7 = block.getRelative(BlockFace.EAST);
                            Block b8 = block.getRelative(BlockFace.WEST);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                            if (b3.getType().equals(Material.REDSTONE_ORE)
                                    || b3.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b3, user.getBase());
                            if (b4.getType().equals(Material.REDSTONE_ORE)
                                    || b4.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b4, user.getBase());
                            if (b5.getType().equals(Material.REDSTONE_ORE)
                                    || b5.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b5, user.getBase());
                            if (b6.getType().equals(Material.REDSTONE_ORE)
                                    || b6.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b6, user.getBase());
                            if (b7.getType().equals(Material.REDSTONE_ORE)
                                    || b7.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b7, user.getBase());
                            if (b8.getType().equals(Material.REDSTONE_ORE)
                                    || b8.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b8, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            Block b3 = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH);
                            Block b4 = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH);
                            Block b5 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH);
                            Block b6 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH);
                            Block b7 = block.getRelative(BlockFace.SOUTH);
                            Block b8 = block.getRelative(BlockFace.NORTH);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                            if (b3.getType().equals(Material.REDSTONE_ORE)
                                    || b3.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b3, user.getBase());
                            if (b4.getType().equals(Material.REDSTONE_ORE)
                                    || b4.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b4, user.getBase());
                            if (b5.getType().equals(Material.REDSTONE_ORE)
                                    || b5.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b5, user.getBase());
                            if (b6.getType().equals(Material.REDSTONE_ORE)
                                    || b6.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b6, user.getBase());
                            if (b7.getType().equals(Material.REDSTONE_ORE)
                                    || b7.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b7, user.getBase());
                            if (b8.getType().equals(Material.REDSTONE_ORE)
                                    || b8.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b8, user.getBase());
                        }
                    }
                }
                break;
            case AXE:
                if (user.getNanoAxeType().equals(Elementals.Nano.B)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.NORTH);
                        if (b1.getType().equals(Material.REDSTONE_ORE)
                                || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b1, user.getBase());
                        Block b2 = block.getRelative(BlockFace.SOUTH);
                        if (b2.getType().equals(Material.REDSTONE_ORE)
                                || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b2, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.EAST);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.WEST);
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.SOUTH);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.NORTH);
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                        }
                    }
                } else if (user.getNanoAxeType().equals(Elementals.Nano.C)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.EAST);
                        if (b1.getType().equals(Material.REDSTONE_ORE)
                                || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b1, user.getBase());
                        Block b2 = block.getRelative(BlockFace.WEST);
                        if (b2.getType().equals(Material.REDSTONE_ORE)
                                || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b2, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                        }
                    }
                } else if (user.getNanoAxeType().equals(Elementals.Nano.D)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.EAST);
                        Block b2 = block.getRelative(BlockFace.WEST);
                        Block b3 = block.getRelative(BlockFace.NORTH);
                        Block b4 = block.getRelative(BlockFace.SOUTH);
                        Block b5 = block.getRelative(BlockFace.NORTH_EAST);
                        Block b6 = block.getRelative(BlockFace.NORTH_WEST);
                        Block b7 = block.getRelative(BlockFace.SOUTH_EAST);
                        Block b8 = block.getRelative(BlockFace.SOUTH_WEST);
                        if (b1.getType().equals(Material.REDSTONE_ORE)
                                || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b1, user.getBase());
                        if (b2.getType().equals(Material.REDSTONE_ORE)
                                || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b2, user.getBase());
                        if (b3.getType().equals(Material.REDSTONE_ORE)
                                || b3.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b3, user.getBase());
                        if (b4.getType().equals(Material.REDSTONE_ORE)
                                || b4.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b4, user.getBase());
                        if (b5.getType().equals(Material.REDSTONE_ORE)
                                || b5.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b5, user.getBase());
                        if (b6.getType().equals(Material.REDSTONE_ORE)
                                || b6.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b6, user.getBase());
                        if (b7.getType().equals(Material.REDSTONE_ORE)
                                || b7.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b7, user.getBase());
                        if (b8.getType().equals(Material.REDSTONE_ORE)
                                || b8.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b8, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            Block b3 = block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST);
                            Block b4 = block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST);
                            Block b5 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST);
                            Block b6 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST);
                            Block b7 = block.getRelative(BlockFace.EAST);
                            Block b8 = block.getRelative(BlockFace.WEST);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                            if (b3.getType().equals(Material.REDSTONE_ORE)
                                    || b3.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b3, user.getBase());
                            if (b4.getType().equals(Material.REDSTONE_ORE)
                                    || b4.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b4, user.getBase());
                            if (b5.getType().equals(Material.REDSTONE_ORE)
                                    || b5.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b5, user.getBase());
                            if (b6.getType().equals(Material.REDSTONE_ORE)
                                    || b6.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b6, user.getBase());
                            if (b7.getType().equals(Material.REDSTONE_ORE)
                                    || b7.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b7, user.getBase());
                            if (b8.getType().equals(Material.REDSTONE_ORE)
                                    || b8.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b8, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            Block b3 = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH);
                            Block b4 = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH);
                            Block b5 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH);
                            Block b6 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH);
                            Block b7 = block.getRelative(BlockFace.SOUTH);
                            Block b8 = block.getRelative(BlockFace.NORTH);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                            if (b3.getType().equals(Material.REDSTONE_ORE)
                                    || b3.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b3, user.getBase());
                            if (b4.getType().equals(Material.REDSTONE_ORE)
                                    || b4.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b4, user.getBase());
                            if (b5.getType().equals(Material.REDSTONE_ORE)
                                    || b5.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b5, user.getBase());
                            if (b6.getType().equals(Material.REDSTONE_ORE)
                                    || b6.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b6, user.getBase());
                            if (b7.getType().equals(Material.REDSTONE_ORE)
                                    || b7.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b7, user.getBase());
                            if (b8.getType().equals(Material.REDSTONE_ORE)
                                    || b8.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b8, user.getBase());
                        }
                    }
                }
                break;
            case SPADE:
                if (user.getNanoSpadeType().equals(Elementals.Nano.B)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.NORTH);
                        if (b1.getType().equals(Material.REDSTONE_ORE)
                                || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b1, user.getBase());
                        Block b2 = block.getRelative(BlockFace.SOUTH);
                        if (b2.getType().equals(Material.REDSTONE_ORE)
                                || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b2, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.EAST);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.WEST);
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.SOUTH);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.NORTH);
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                        }
                    }
                } else if (user.getNanoSpadeType().equals(Elementals.Nano.C)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.EAST);
                        if (b1.getType().equals(Material.REDSTONE_ORE)
                                || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b1, user.getBase());
                        Block b2 = block.getRelative(BlockFace.WEST);
                        if (b2.getType().equals(Material.REDSTONE_ORE)
                                || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b2, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                        }
                    }
                } else if (user.getNanoSpadeType().equals(Elementals.Nano.D)) {
                    if (pitch < -65 || pitch > 65) {
                        Block b1 = block.getRelative(BlockFace.EAST);
                        Block b2 = block.getRelative(BlockFace.WEST);
                        Block b3 = block.getRelative(BlockFace.NORTH);
                        Block b4 = block.getRelative(BlockFace.SOUTH);
                        Block b5 = block.getRelative(BlockFace.NORTH_EAST);
                        Block b6 = block.getRelative(BlockFace.NORTH_WEST);
                        Block b7 = block.getRelative(BlockFace.SOUTH_EAST);
                        Block b8 = block.getRelative(BlockFace.SOUTH_WEST);
                        if (b1.getType().equals(Material.REDSTONE_ORE)
                                || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b1, user.getBase());
                        if (b2.getType().equals(Material.REDSTONE_ORE)
                                || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b2, user.getBase());
                        if (b3.getType().equals(Material.REDSTONE_ORE)
                                || b3.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b3, user.getBase());
                        if (b4.getType().equals(Material.REDSTONE_ORE)
                                || b4.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b4, user.getBase());
                        if (b5.getType().equals(Material.REDSTONE_ORE)
                                || b5.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b5, user.getBase());
                        if (b6.getType().equals(Material.REDSTONE_ORE)
                                || b6.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b6, user.getBase());
                        if (b7.getType().equals(Material.REDSTONE_ORE)
                                || b7.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b7, user.getBase());
                        if (b8.getType().equals(Material.REDSTONE_ORE)
                                || b8.getType().equals(Material.GLOWING_REDSTONE_ORE))
                            breakNaturaly(b8, user.getBase());
                    } else {
                        if ((yaw > 315 && yaw <= 360 || yaw >= 0 && yaw <= 45) || (yaw > 135 && yaw <= 225)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            Block b3 = block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST);
                            Block b4 = block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST);
                            Block b5 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST);
                            Block b6 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST);
                            Block b7 = block.getRelative(BlockFace.EAST);
                            Block b8 = block.getRelative(BlockFace.WEST);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                            if (b3.getType().equals(Material.REDSTONE_ORE)
                                    || b3.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b3, user.getBase());
                            if (b4.getType().equals(Material.REDSTONE_ORE)
                                    || b4.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b4, user.getBase());
                            if (b5.getType().equals(Material.REDSTONE_ORE)
                                    || b5.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b5, user.getBase());
                            if (b6.getType().equals(Material.REDSTONE_ORE)
                                    || b6.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b6, user.getBase());
                            if (b7.getType().equals(Material.REDSTONE_ORE)
                                    || b7.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b7, user.getBase());
                            if (b8.getType().equals(Material.REDSTONE_ORE)
                                    || b8.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b8, user.getBase());
                        } else if ((yaw > 45 && yaw <= 145) || (yaw > 225 && yaw <= 315)) {
                            Block b1 = block.getRelative(BlockFace.UP);
                            Block b2 = block.getRelative(BlockFace.DOWN);
                            Block b3 = block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH);
                            Block b4 = block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH);
                            Block b5 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH);
                            Block b6 = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH);
                            Block b7 = block.getRelative(BlockFace.SOUTH);
                            Block b8 = block.getRelative(BlockFace.NORTH);
                            if (b1.getType().equals(Material.REDSTONE_ORE)
                                    || b1.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b1, user.getBase());
                            if (b2.getType().equals(Material.REDSTONE_ORE)
                                    || b2.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b2, user.getBase());
                            if (b3.getType().equals(Material.REDSTONE_ORE)
                                    || b3.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b3, user.getBase());
                            if (b4.getType().equals(Material.REDSTONE_ORE)
                                    || b4.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b4, user.getBase());
                            if (b5.getType().equals(Material.REDSTONE_ORE)
                                    || b5.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b5, user.getBase());
                            if (b6.getType().equals(Material.REDSTONE_ORE)
                                    || b6.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b6, user.getBase());
                            if (b7.getType().equals(Material.REDSTONE_ORE)
                                    || b7.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b7, user.getBase());
                            if (b8.getType().equals(Material.REDSTONE_ORE)
                                    || b8.getType().equals(Material.GLOWING_REDSTONE_ORE))
                                breakNaturaly(b8, user.getBase());
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    public static void breakNaturaly(Block block, Player player) {
        NanoBlockBreakEvent event = new NanoBlockBreakEvent(player, block);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;
        Material mat = block.getState().getType();
        MaterialData data = block.getState().getData();
        if (player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
            block.setType(Material.AIR);
            ItemStack item = new ItemStack(mat);
            item.setData(data);
            block.getWorld().dropItem(block.getLocation(), item);
            block.getWorld().spawnParticle(Particle.BLOCK_DUST, block.getLocation().clone().add(.5, .5, .5), 3,
                    block.getState().getData());
        } else
            block.breakNaturally(player.getInventory().getItemInMainHand());
    }

    public static void broadcastRawMessage(String raw) {
        Bukkit.getOnlinePlayers().forEach((Player player) -> player.sendRawMessage(raw));
    }

    @SuppressWarnings("deprecation")
    public static void changePoints(CommandSender sender, String name, String amount, boolean give) {
        int a = 0;
        try {
            a = Integer.valueOf(amount);
        } catch (NumberFormatException exception) {
            sender.sendMessage("§cSuma trebuie sa fie un numar!");
            return;
        }
        try {
            OfflinePlayer off = Bukkit.getOfflinePlayer(name);
            if (off.isOnline()) {
                User user = Elementals.getUser(off.getPlayer());
                if (give) {
                    user.addPoints(a);
                    sender.sendMessage("§3Ai adaugat §a" + a + " §3points in contul lui §c" + name + "§3!");
                } else {
                    user.removePoints(a);
                    sender.sendMessage("§3Ai scos §a" + a + " §3points din contul lui §c" + name + "§3!");
                }
            } else {
                ResultSet rs = Elementals.getBase()
                        .prepareStatement(
                                "SELECT points FROM pikapoints WHERE uuid='" + off.getUniqueId().toString() + "';")
                        .executeQuery();
                if (rs.next()) {
                    int points = rs.getInt("points");
                    if (give) {
                        points += a;
                        sender.sendMessage("§3Ai adaugat §a" + a + " §3points in contul lui §c" + name + "§3!");
                    } else {
                        points -= a;
                        sender.sendMessage("§3Ai scos §a" + a + " §3points din contul lui §c" + name + "§3!");
                    }
                    Elementals.getBase().prepareStatement("UPDATE pikapoints SET points='" + points + "' WHERE uuid='"
                            + off.getUniqueId().toString() + "';").executeUpdate();
                } else {
                    sender.sendMessage("§cJucatorul nu a fost gasit!");
                    return;
                }
            }
        } catch (SQLException ex) {
            sender.sendMessage("§cEroare la baza de date.");
            return;
        } catch (NullPointerException ex) {
            sender.sendMessage("§cJucatorul nu a fost gasit!");
            return;
        }
    }

    public static void delayChatPlayer(User user) {
        UUID uuid = user.getBase().getUniqueId();
        delayChat.add(uuid);
        Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> delayChat.remove(uuid), 20L);
    }

    public static void delayRandomTPPlayer(User user) {
        UUID uuid = user.getBase().getUniqueId();
        delayRandomTP.add(uuid);
        Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> delayRandomTP.remove(uuid), 30 * 60 * 20L);
    }

    public static synchronized void delayRandomTPCmdPlayer(User user, int taskID) {
        delayRandomTPCmd.putIfAbsent(user.getBase().getUniqueId(), taskID);
    }

    public static synchronized boolean hasRandomTpCmdDelay(User user) {
        return delayRandomTPCmd.containsKey(user.getBase().getUniqueId());
    }

    public static synchronized void cancelRandomTpCmd(User user) {
        Bukkit.getScheduler().cancelTask(delayRandomTPCmd.get(user.getBase().getUniqueId()));
        if (RandomTpCommand.hasTeleportReq(user))
            RandomTpCommand.removeTeleportReq(user);
        user.getBase().sendMessage("§cTeleportarea a fost anulata...");
    }

    public static synchronized void removeTandomTpCmdDelay(User user) {
        delayRandomTPCmd.remove(user.getBase().getUniqueId());
    }

    public static List<String> getPlayersNames() {
        List<String> args = Lists.newArrayList();
        Bukkit.getOnlinePlayers().forEach((Player player) -> args.add(player.getName()));
        return args;
    }

    public static boolean hasChatDelay(User user) {
        return delayChat.contains(user.getBase().getUniqueId());
    }

    public static boolean hasRandomTpDelay(User user) {
        return delayRandomTP.contains(user.getBase().getUniqueId());
    }

    public static boolean isCreateInUse() {
        return create;
    }

    @SuppressWarnings("deprecation")
    public static void setPoints(CommandSender sender, String name, String amount, boolean set) {
        int a = 0;
        try {
            a = Integer.valueOf(amount);
        } catch (NumberFormatException exception) {
            sender.sendMessage("§cSuma trebuie sa fie un numar!");
            return;
        }
        try {
            OfflinePlayer off = Bukkit.getOfflinePlayer(name);
            if (off.isOnline()) {
                User user = Elementals.getUser(off.getPlayer());
                if (set) {
                    user.setPoints(a);
                    sender.sendMessage("§3Ai setat §a" + a + " §3points in contul lui §c" + name + "§3!");
                } else {
                    user.setPoints(0);
                    sender.sendMessage("§3Ai resetat la 0 contul lui §c" + name + "§3!");
                }
            } else {
                try {
                    Elementals.getBase().prepareStatement("UPDATE pikapoints SET points='" + (set ? a : 0)
                            + "' WHERE uuid='" + off.getUniqueId().toString() + "';").executeUpdate();
                    if (set)
                        sender.sendMessage("§3Ai setat §a" + a + " §3points in contul lui §c" + name + "§3!");
                    else
                        sender.sendMessage("§3Ai resetat la 0 contul lui §c" + name + "§3!");
                } catch (SQLException e) {
                    sender.sendMessage("§cJucatorul nu a fost gasit!");
                    return;
                }
            }
        } catch (NullPointerException ex) {
            sender.sendMessage("§cJucatorul nu a fost gasit!");
            return;
        }
    }

    public static Location teleportLoc(Player player) {
        Location loc;
        World world = Bukkit.getWorld("world");
        int x = 0;
        int y = 64;
        int z = 0;
        do {
            x = -10000 + nextInt(20000);
            z = -10000 + nextInt(20000);
            world.loadChunk(x, z);
            if (world.getHighestBlockAt(x, z).getType().isSolid())
                y = world.getHighestBlockYAt(x, z) + 1;
            else
                y = world.getHighestBlockYAt(x, z);
            loc = new Location(world, x, y, z);
        } while (world.getBiome(x, z) == Biome.OCEAN || world.getBiome(x, z) == Biome.DEEP_OCEAN
                || world.getBiome(x, z) == Biome.HELL || world.getBiome(x, z) == Biome.SKY
                || world.getBiome(x, z) == Biome.RIVER || FieldUtil.isFieldAtLocation(loc));
        loc.getBlock().setType(Material.AIR);
        loc.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
        player.sendMessage("§bAi fost teleportat la x:" + x + " y:" + y + " z:" + z + "!");
        return loc;
    }

    public static void toggleCreate(boolean b) {
        create = b;
    }

    public static int yawCorrection(int n) {
        if (n < 0)
            n += 360;
        return n;
    }

    public static double yawCorrectionN(float n) {
        double yaw = n;
        if (yaw < 0)
            yaw += 360;
        return yaw;
    }

    public static int foundBlocks(Block block) {
        int i = 1;
        for (int x = (block.getX() - 2); x < (block.getX() + 2); x++) {
            for (int y = (block.getY() - 2); y < (block.getY() + 2); y++) {
                for (int z = (block.getZ() - 2); z < (block.getZ() + 2); z++) {
                    if (y < 0)
                        continue;
                    Block founded = block.getWorld().getBlockAt(x, y, z);
                    if (founded.isEmpty())
                        continue;
                    if (!founded.getType().equals(block.getType()))
                        continue;
                    if (hasTag(founded, "found"))
                        continue;
                    setTag(founded, "found");
                    i++;
                }
            }
        }
        return i;
    }

    public static void toggleChat(boolean b) {
        stopChat = b;
    }

    public static boolean isChatStopped() {
        return stopChat;
    }

    // TODO we have a lag-hole here
    public static void procesWorld(World world) {
        for (Entity entity : world.getEntities()) {
            boolean corupted = hasTag(entity, "corupted");
            if (isValidEntity(entity, false)) {
                if (entity.getCustomName() != null)
                    if (!entity.getCustomName().startsWith("§cCount"))
                        continue;
                int originalCount = getEntityCount((LivingEntity) entity);
                int removedCount = 0;
                for (Entity other : entity.getNearbyEntities(15, 15, 15))
                    if (isValidEntity(other, false)) {
                        if (other.getCustomName() != null)
                            if (!other.getCustomName().startsWith("§cCount"))
                                continue;
                        if (match(entity, other)) {
                            int otherCount = getEntityCount((LivingEntity) other);
                            if (!corupted)
                                corupted = hasTag(other, "corupted");
                            if (originalCount + removedCount + otherCount <= getMaximEntityCount(entity.getType())) {
                                other.remove();
                                removedCount += otherCount;
                            }
                        }
                    }
                if (removedCount > 0) {
                    int finalCount = originalCount + removedCount;
                    setEntityCount((LivingEntity) entity, finalCount);
                    entity.setCustomName("§cCount§f: §a" + finalCount);
                    entity.setCustomNameVisible(false);
                }
                if (corupted && (entity instanceof Monster)) {
                    setTag(entity, "corupted");
                    ItemUtil.findTarget((Monster) entity);
                }
            }
        }
    }

    private static boolean match(Entity entity, Entity other) {
        if (entity.getType() != other.getType())
            return false;
        if (((entity instanceof Ageable && other instanceof Ageable))
                && (((Ageable) entity).isAdult() != ((Ageable) other).isAdult()))
            return false;
        if (((entity instanceof Colorable && other instanceof Colorable))
                && (((Colorable) entity).getColor() != ((Colorable) other).getColor()))
            return false;
        return true;
    }

    public static boolean isValidEntity(Entity entity, boolean isDead) {
        if (!isDead) {
            if (!entity.isValid())
                return false;
            if (!(entity instanceof LivingEntity))
                return false;
        }
        if (CitizensAPI.getNPCRegistry().isNPC(entity))
            return false;
        if (hasTag(entity, "herobrine") || hasTag(entity, "ice_monster") || hasTag(entity, "lava_monster")
                || hasTag(entity, "end_monster"))
            return false;
        if (entity.isInsideVehicle())
            return false;
        switch (entity.getType()) {
            case BAT:
            case CHICKEN:
            case COW:
            case ENDERMITE:
            case GHAST:
            case GIANT:
            case MUSHROOM_COW:
            case PIG:
            case POLAR_BEAR:
            case RABBIT:
            case SHEEP:
            case SILVERFISH:
            case WITCH:
            case BLAZE:
            case CAVE_SPIDER:
            case CREEPER:
            case ENDERMAN:
            case GUARDIAN:
            case SPIDER:
            case SQUID:
            case PIG_ZOMBIE:
            case SKELETON:
            case ZOMBIE:
            case WITHER_SKELETON:
            case HUSK:
            case STRAY:
            case ZOMBIE_VILLAGER:
            case VINDICATOR:
                return true;
            default:
                return false;
        }
    }

    public static int getMaximEntityCount(EntityType type) {
        switch (type) {
            case BAT:
            case CHICKEN:
            case COW:
            case ENDERMITE:
            case GHAST:
            case GIANT:
            case MUSHROOM_COW:
            case PIG:
            case POLAR_BEAR:
            case RABBIT:
            case SHEEP:
            case SILVERFISH:
            case WITCH:
            case WITHER_SKELETON:
            case ZOMBIE_VILLAGER:
            case VINDICATOR:
                return 5;
            case BLAZE:
            case CAVE_SPIDER:
            case CREEPER:
            case ENDERMAN:
            case GUARDIAN:
            case SPIDER:
            case SQUID:
                return 10;
            case PIG_ZOMBIE:
            case SKELETON:
            case ZOMBIE:
            case HUSK:
            case STRAY:
                return 15;
            default:
                return 1;
        }
    }

    public static int getEntityCount(Entity entity) {
        if (!TagRegister.isStored(entity))
            return 1;
        CompoundTag tag = TagRegister.getStored(entity);
        if (!tag.containsKey("count"))
            return 1;
        if (!tag.isInt("count"))
            return 1;
        return tag.getInt("count").getValue();
    }

    public static void setEntityCount(Entity entity, int count) {
        CompoundTag tag = TagRegister.isStored(entity) ? TagRegister.getStored(entity) : TagRegister.create(entity);
        tag.putInt("count", new IntTag("count", count));
    }

    public static void setTag(Entity entity, String arg) {
        CompoundTag tag = TagRegister.isStored(entity) ? TagRegister.getStored(entity) : TagRegister.create(entity);
        tag.putByte(arg, new ByteTag(arg, (byte) 1));
    }

    public static boolean hasTag(Entity entity, String arg) {
        if (!TagRegister.isStored(entity))
            return false;
        return TagRegister.getStored(entity).containsKey(arg);
    }

    public static void setTag(Block block, String arg) {
        CompoundTag tag = TagRegister.isStored(block) ? TagRegister.getStored(block) : TagRegister.create(block);
        tag.put(arg, new ByteTag(arg, (byte) 1));
    }

    public static boolean hasTag(Block block, String arg) {
        if (!TagRegister.isStored(block))
            return false;
        return TagRegister.getStored(block).containsKey(arg);
    }

    public static void removeTag(Entity entity, String arg) {
        if (!TagRegister.isStored(entity))
            return;
        TagRegister.getStored(entity).remove(arg);
    }

    public static void removeTag(Block block, String arg) {
        if (!TagRegister.isStored(block))
            return;
        TagRegister.getStored(block).remove(arg);
    }

    public static void addDir(File dirObj, ZipOutputStream out) throws IOException {
        File[] files = dirObj.listFiles();
        byte[] tmpBuf = new byte[1024];
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                addDir(files[i], out);
                continue;
            }
            FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
            out.putNextEntry(new ZipEntry(files[i].getAbsolutePath()));
            int len;
            while ((len = in.read(tmpBuf)) > 0)
                out.write(tmpBuf, 0, len);
            out.closeEntry();
            in.close();
        }
    }

    public static void zipDir(String zipFileName, String... dir) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        for (String abc : dir)
            addDir(new File(abc), out);
        out.close();
    }
}
