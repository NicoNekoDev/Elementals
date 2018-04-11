package ro.nicuch.elementals.infernal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.DoubleTag;
import com.google.common.collect.Lists;

import ro.nicuch.elementals.Elementals;
import ro.nicuch.elementals.elementals.ElementalsUtil;
import ro.nicuch.tag.register.TagRegister;

public class InfernalMobsUtil {
    private final static ConcurrentMap<Entity, BossBar> bars = new ConcurrentHashMap<Entity, BossBar>();

    public static enum InfernalEquipType {

        PHOENIX(armor("�cPhoenix")), GRAVITITE(armor("�dGravitite")), OBSIDIAN(armor("�7Obsidian")), ZANITE(
                armor("�5Zanite"));

        private final ItemStack[] items;

        private InfernalEquipType(ItemStack[] items) {
            this.items = items;
        }

        public ItemStack[] getEquipment() {
            return this.items;
        }

        private static ItemStack[] armor(String name) {
            ItemStack[] armor = new ItemStack[4];
            ItemStack h = new ItemStack(Material.DIAMOND_HELMET);
            ItemStack c = new ItemStack(Material.DIAMOND_CHESTPLATE);
            ItemStack l = new ItemStack(Material.DIAMOND_LEGGINGS);
            ItemStack b = new ItemStack(Material.DIAMOND_BOOTS);
            ItemMeta h_m = h.getItemMeta();
            ItemMeta c_m = h.getItemMeta();
            ItemMeta l_m = h.getItemMeta();
            ItemMeta b_m = h.getItemMeta();
            h_m.setDisplayName(name + " Helmet");
            c_m.setDisplayName(name + " Chestplate");
            l_m.setDisplayName(name + " Leggings");
            b_m.setDisplayName(name + " Boots");
            h.setItemMeta(h_m);
            c.setItemMeta(c_m);
            l.setItemMeta(l_m);
            b.setItemMeta(b_m);
            armor[0] = h;
            armor[1] = c;
            armor[2] = l;
            armor[3] = b;
            return armor;
        }
    }

    public static enum PowerType {
        POISONOUS("poisinous", "Poisonous"), BLINDING("blinding", "Blinding"), WITHERING("withering",
                "Withering"), TOSSES("tosses", "Tosses"), THIEF("thief", "Thief"), QUICKSAND("quicksand",
                "Quicksand"), SAPPER("sapper", "Sapper"), CLOACKED("cloacked", "Cloaked"), ENDER("ender",
                "Ender"), LIFESTEAL("lifesteal", "Lifesteal"), SPRINT("sprint", "Sprint"), STORM(
                "storm", "Storm"), WEAKNESS("weakness", "Weakness"), EXPLODE("explode",
                "Explode"), BERSERK("berserk", "Berserk"), SPAWNER("spawner",
                "Spawner"), MOLTEN("molten", "Molten"), NECROMANCER(
                "necromancer", "Necromancer"), FIREWORK("firework",
                "Firework"), GRAVITY("gravity",
                "Gravity"), MORPH("morph",
                "Morph"), CONFUSING("confusing",
                "Confusing");

        private final String tag;
        private final String powerName;

        private PowerType(String tag, String powerName) {
            this.tag = tag;
            this.powerName = powerName;
        }

        public String getTag() {
            return this.tag;
        }

        public String getPowerName() {
            return this.powerName;
        }
    }

    public static boolean hasPower(Entity entity, PowerType power) {
        return ElementalsUtil.hasTag(entity, power.getTag());
    }

    public static boolean hasBossBar(Entity entity) {
        return bars.containsKey(entity);
    }

    public static BossBar getBossBar(Entity entity) {
        return bars.get(entity);
    }

    public static void setDelay(Entity entity, PowerType power, int max, int offset) {
        long now = System.currentTimeMillis();
        long then = now + (max * 1000) + (ElementalsUtil.nextInt(offset) * 1000);
        CompoundTag tag = TagRegister.isStored(entity) ? TagRegister.getStored(entity) : TagRegister.create(entity);
        tag.putDouble(power.getTag() + "_delay", new DoubleTag("delay", then));
    }

    public static boolean checkDelay(Entity entity, PowerType power) {
        long now = System.currentTimeMillis();
        if (!TagRegister.isStored(entity))
            return false;
        CompoundTag tag = TagRegister.getStored(entity);
        String tagKey = power.getTag() + "_delay";
        if (!tag.containsKey(tagKey))
            return false;
        if (!tag.isLong(tagKey))
            return false;
        long delay = tag.getLong(tagKey).getValue();
        return (now > delay);
    }

    public static void removeDelay(Entity entity, PowerType power) {
        if (!TagRegister.isStored(entity))
            return;
        CompoundTag tag = TagRegister.getStored(entity);
        String tagKey = power.getTag() + "_delay";
        if (!tag.containsKey(tagKey))
            return;
        if (!tag.isLong(tagKey))
            return;
        tag.remove(tagKey);
    }

    public static BossBar createBossBar(LivingEntity entity) {
        StringBuilder bossBarName = new StringBuilder("�6Infernal ");
        for (PowerType power : PowerType.values())
            if (ElementalsUtil.hasTag(entity, power.getTag()))
                bossBarName.append(power.getPowerName() + " ");
        switch (entity.getType()) {
            case ZOMBIE:
                bossBarName.append("Zombie");
                break;
            default:
                bossBarName.append("Entity");
                break;
        }
        BossBar bar = recalculateBossBarHealth(
                Bukkit.createBossBar(bossBarName.toString(), BarColor.PURPLE, BarStyle.SEGMENTED_10), entity);
        bars.putIfAbsent(entity, bar);
        return bar;
    }

    public static BossBar recalculateBossBarHealth(BossBar bar, LivingEntity entity) {
        double progress = entity.getHealth() / entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        bar.setProgress(progress);
        bar.setVisible(true);
        return bar;
    }

    public static List<PowerType> getRandomPowers(int total) {
        List<PowerType> types = Arrays.asList(PowerType.values());
        Collections.sort(types, (PowerType a, PowerType b) -> {
            int n = ElementalsUtil.nextInt(3);
            if (n == 0)
                return 0;
            else if (n == 1)
                return 1;
            else
                return -1;
        });
        List<PowerType> returns = Lists.newArrayList();
        for (int i = 0; i < total; i++)
            returns.add(types.get(i));
        return returns;
    }

    public static void unload() {
        for (BossBar bar : bars.values())
            for (Player player : bar.getPlayers())
                bar.removePlayer(player);
    }

    public static void load() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Elementals.get(), () -> {
            Bukkit.getOnlinePlayers().forEach((Player player) -> {
                for (Entity entity : player.getNearbyEntities(25, 25, 25)) {
                    if (!(entity instanceof LivingEntity)) {
                        continue;
                    }
                    if (entity.getLocation().distance(player.getLocation()) > 24) {
                        continue;
                    }
                    LivingEntity living = (LivingEntity) entity;
                    if (ElementalsUtil.hasTag(living, "infernal")) {
                        BossBar bar = hasBossBar(living) ? getBossBar(living) : createBossBar(living);
                        if (!bar.getPlayers().contains(player)) {
                            bar.addPlayer(player);
                        }
                    }
                }
            });
            for (Entity ent : bars.keySet()) {
                BossBar bar = bars.get(ent);
                if (!(ent instanceof LivingEntity)) {
                    bar.setVisible(false);
                    bar.removeAll();
                    bars.remove(ent);
                    continue;
                }
                LivingEntity entity = (LivingEntity) ent;
                if (!entity.isValid()) {
                    bar.setVisible(false);
                    bar.removeAll();
                    bars.remove(entity);
                    continue;
                }
                if (entity.isDead()) {
                    bar.setVisible(false);
                    bar.removeAll();
                    bars.remove(entity);
                    continue;
                }
                for (Player player : bar.getPlayers()) {
                    if (!player.isOnline()) {
                        bar.removePlayer(player);
                        continue;
                    }
                    if (entity.getLocation().distance(player.getLocation()) > 25) {
                        bar.removePlayer(player);
                        continue;
                    }
                }
                if (bar.getPlayers().isEmpty()) {
                    bar.setVisible(false);
                    bars.remove(entity);
                    continue;
                }
                bar.setVisible(true);
            }
        }, 80L, 80L);
    }
}
