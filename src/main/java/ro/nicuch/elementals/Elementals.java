package ro.nicuch.elementals;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import ro.nicuch.elementals.casebox.CaseListener;
import ro.nicuch.elementals.deathmessage.DeathMessageListener;
import ro.nicuch.elementals.dungeon.DungeonListener;
import ro.nicuch.elementals.elementals.ElementalsListener;
import ro.nicuch.elementals.elementals.ElementalsPlaceholders;
import ro.nicuch.elementals.elementals.ElementalsUtil;
import ro.nicuch.elementals.elementals.commands.AdminChatCommand;
import ro.nicuch.elementals.elementals.commands.AdminCommand;
import ro.nicuch.elementals.elementals.commands.ChatCommand;
import ro.nicuch.elementals.elementals.commands.CurseEnchantCommand;
import ro.nicuch.elementals.elementals.commands.CustomEnchantCommand;
import ro.nicuch.elementals.elementals.commands.GiveAllCommand;
import ro.nicuch.elementals.elementals.commands.PointsCommand;
import ro.nicuch.elementals.elementals.commands.ProtectionCommand;
import ro.nicuch.elementals.elementals.commands.RandomTpCommand;
import ro.nicuch.elementals.elementals.commands.ShowCommand;
import ro.nicuch.elementals.elementals.commands.SortCommand;
import ro.nicuch.elementals.elementals.commands.SoundCommand;
import ro.nicuch.elementals.elementals.commands.TestCommand;
import ro.nicuch.elementals.enchants.EnchantListener;
import ro.nicuch.elementals.enchants.EnchantUtil;
import ro.nicuch.elementals.enchants.EnchantUtil.CEnchantType;
import ro.nicuch.elementals.hooks.NCPHookListener;
import ro.nicuch.elementals.infernal.InfernalMobsListener;
import ro.nicuch.elementals.infernal.InfernalMobsUtil;
import ro.nicuch.elementals.item.ItemListener;
import ro.nicuch.elementals.item.ItemUtil;
import ro.nicuch.elementals.protection.Field;
import ro.nicuch.elementals.protection.FieldListener;
import ro.nicuch.elementals.protection.FieldUtil;
import ro.nicuch.elementals.worldguard_imp.WGDoubleRegion;

public class Elementals extends JavaPlugin {
	public static enum Nano {
		A, B, C, D;
	}

	private static Connection database;
	private final static ConcurrentMap<UUID, User> players = new ConcurrentHashMap<UUID, User>();
	private static Economy vault;
	private static Permission perm;
	// private static IEssentials ess;
	private static GroupManager gm;

	private final static WGDoubleRegion l1 = new WGDoubleRegion(979.5, 66.0, 7.5);
	private final static WGDoubleRegion l2 = new WGDoubleRegion(979.5, 66.0, -85.5);
	private static int particleInt = 0;

	private static WorldEditPlugin we;
	private static String board = "player";
	// private static CoreProtectAPI coreProtect;
	// private static MobArena mobArena;

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		long start = System.currentTimeMillis();
		getServer().setSpawnRadius(1);
		new File(this.getDataFolder() + File.separator + "regiuni").mkdirs();
		new File("backup").mkdirs();
		gm = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager");
		// ess = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");
		we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		// coreProtect = ((CoreProtect)
		// Bukkit.getPluginManager().getPlugin("CoreProtect")).getAPI();
		// mobArena = (MobArena) Bukkit.getPluginManager().getPlugin("MobArena");
		vault = this.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
		perm = this.getServer().getServicesManager().getRegistration(Permission.class).getProvider();
		new WorldCreator("spawn").environment(Environment.NORMAL).generateStructures(false).createWorld();
		new WorldCreator("adi").environment(Environment.NORMAL).generateStructures(false).createWorld();
		createDataBase();
		Bukkit.getWorlds().forEach((World world) -> {
			world.setDifficulty(Difficulty.HARD);
			for (Chunk chunk : world.getLoadedChunks())
				if (chunk.isLoaded())
					FieldUtil.loadFieldsInChunk(chunk);
		});
		this.commandsCreator();
		this.eventCreator();
		this.randomMsg();
		this.timer();
		// this.mineReset();
		this.enchantament();
		this.portalParticles();
		this.mobMerger();
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			new ElementalsPlaceholders(this).hook();
		}
		Bukkit.getOnlinePlayers().forEach((Player player) -> createUser(player));
		InfernalMobsUtil.load();
		sendConsoleMessage("§bPluginul a pornit! (" + (System.currentTimeMillis() - start) + "ms)");
		Bukkit.broadcastMessage("§bElementals a pornit! (" + (System.currentTimeMillis() - start) + "ms)");
	}

	@Override
	public void onDisable() {
		FieldUtil.getLoadedFields().forEach((Field field) -> field.save());
		getOnlineUsers().forEach((User user) -> user.save());
		try {
			database.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ItemUtil.unload();
		InfernalMobsUtil.unload();
		sendConsoleMessage("§bPluginul s-a oprit!");
	}

	public static GroupManager getGM() {
		return gm;
	}

	// public static MobArena getMobArena() {
	// return mobArena;
	// }

	// public static CoreProtectAPI getCoreProtect() {
	// return coreProtect;
	// }

	public static void createUser(Player player) {
		players.putIfAbsent(player.getUniqueId(), new User(player));
	}

	public static boolean existUser(UUID uuid) {
		return players.containsKey(uuid);
	}

	public static boolean existUser(Player player) {
		return existUser(player.getUniqueId());
	}

	public static Plugin get() {
		return Bukkit.getPluginManager().getPlugin("Elementals");
	}

	public static Connection getBase() {
		return database;
	}

	// public static IEssentials getEssentials() {
	// return ess;
	// }

	public static Collection<User> getOnlineUsers() {
		return players.values();
	}

	public static User getUser(Player player) {
		return getUser(player.getUniqueId());
	}

	public static User getUser(UUID uuid) {
		if (existUser(uuid))
			return players.get(uuid);
		else
			throw new NullPointerException("This user is offline or null.");
	}

	public static WorldEditPlugin getWorldEdit() {
		return we;
	}

	public static Economy getVault() {
		return vault;
	}

	public static Permission getPermission() {
		return perm;
	}

	public static void removeUser(Player player) {
		removeUser(player.getUniqueId());
	}

	public static void removeUser(UUID uuid) {
		if (existUser(uuid)) {
			User user = getUser(uuid);
			players.remove(uuid);
			user.save();
			user.remove();
		}
	}

	public static void sendConsoleMessage(String arg) {
		Bukkit.getServer().getConsoleSender().sendMessage(arg);
	}

	private void commandsCreator() {
		this.getCommand("ps").setExecutor(new ProtectionCommand());
		this.getCommand("ps").setTabCompleter(new ProtectionCommand());
		this.getCommand("adminchat").setExecutor(new AdminChatCommand());
		this.getCommand("adminchat").setAliases(AdminChatCommand.getAliases());
		this.getCommand("chat").setExecutor(new ChatCommand());
		this.getCommand("randomtp").setExecutor(new RandomTpCommand());
		this.getCommand("randomtp").setAliases(RandomTpCommand.getAliases());
		this.getCommand("sound").setExecutor(new SoundCommand());
		this.getCommand("sound").setTabCompleter(new SoundCommand());
		this.getCommand("giveall").setExecutor(new GiveAllCommand());
		this.getCommand("test").setExecutor(new TestCommand());
		this.getCommand("points").setExecutor(new PointsCommand());
		this.getCommand("points").setTabCompleter(new PointsCommand());
		this.getCommand("admin").setExecutor(new AdminCommand());
		this.getCommand("show").setExecutor(new ShowCommand());
		this.getCommand("ce").setExecutor(new CustomEnchantCommand());
		this.getCommand("ce").setTabCompleter(new CustomEnchantCommand());
		this.getCommand("sort").setExecutor(new SortCommand());
		this.getCommand("sort").setExecutor(new SortCommand());
		this.getCommand("curse").setExecutor(new CurseEnchantCommand());
	}

	private void createDataBase() {
		FileConfiguration cfg = this.getConfig();
		String username = cfg.getString("db_user");
		String password = cfg.getString("db_pass");
		String ip = cfg.getString("db_ip");
		String db_name = cfg.getString("db_name");
		String url = "jdbc:mysql://" + ip + "/" + db_name + "?autoReconnect=true";
		try {
			database = DriverManager.getConnection(url, username, password);
			database.prepareStatement(
					"CREATE TABLE IF NOT EXISTS protection(id VARCHAR(50) PRIMARY KEY, x INT, y INT, z INT, world VARCHAR(50), owner VARCHAR(50), maxx INT, maxz INT, minx INT, minz INT, chunkx INT, chunkz INT);")
					.executeUpdate();
			database.prepareStatement(
					"CREATE TABLE IF NOT EXISTS pikapoints(uuid VARCHAR(50) PRIMARY KEY, points INT);").executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	private void eventCreator() {
		PluginManager manager = this.getServer().getPluginManager();
		manager.registerEvents(new ElementalsListener(), this);
		manager.registerEvents(new FieldListener(), this);
		manager.registerEvents(new DeathMessageListener(), this);
		manager.registerEvents(new CaseListener(), this);
		manager.registerEvents(new EnchantListener(), this);
		manager.registerEvents(new ItemListener(), this);
		manager.registerEvents(new NCPHookListener(), this);
		manager.registerEvents(new DungeonListener(), this);
		manager.registerEvents(new InfernalMobsListener(), this);
	}

	private void portalParticles() {
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			particleInt++;
			if (particleInt > 160)
				particleInt = 0;
			double angle = particleInt * ((2 * Math.PI) / 20);
			double a = particleInt * 0.01;
			double b = 1.25 * Math.sin(angle);
			double c = 1.25 * Math.cos(angle);
			World world = Bukkit.getWorld("spawn");
			if (world.getPlayers().isEmpty())
				return;
			world.spawnParticle(Particle.VILLAGER_HAPPY,
					new Location(world, l1.getX() + c, l1.getY() + a, l1.getZ() + b), 1);
			world.spawnParticle(Particle.VILLAGER_HAPPY,
					new Location(world, l2.getX() + c, l2.getY() + a, l2.getZ() + b), 1);
			world.spawnParticle(Particle.VILLAGER_HAPPY,
					new Location(world, l1.getX() + c, l1.getY() + a + 1.5, l1.getZ() + b), 1);
			world.spawnParticle(Particle.VILLAGER_HAPPY,
					new Location(world, l2.getX() + c, l2.getY() + a + 1.5, l2.getZ() + b), 1);
			world.spawnParticle(Particle.VILLAGER_HAPPY,
					new Location(world, l1.getX() + c, l1.getY() + a + 3, l1.getZ() + b), 1);
			world.spawnParticle(Particle.VILLAGER_HAPPY,
					new Location(world, l2.getX() + c, l2.getY() + a + 3, l2.getZ() + b), 1);
		}, 1L, 3L);
	}

	// TODO we have a lag-hole here
	private void mobMerger() {
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			Bukkit.getWorlds().forEach((World world) -> {
				if (!world.getName().equals("spawn"))
					ElementalsUtil.procesWorld(world);
			});
		}, 10 * 20L, 10 * 20L);
	}

	// TODO replace this with world edit or data-structures
	/*
	 * private void mineReset() {
	 * Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> { for (Player
	 * player : Bukkit.getOnlinePlayers()) if
	 * (WGConfigLoc.checkInMine(player.getLocation()))
	 * Bukkit.getScheduler().runTask(this, () -> { player.teleport(new
	 * Location(player.getWorld(), player.getLocation().getX(), 62,
	 * player.getLocation().getZ(), player.getLocation().getYaw(),
	 * player.getLocation().getPitch())); }); for (int x =
	 * WGConfigLoc.getIronMineMin().getX(); x <=
	 * WGConfigLoc.getIronMineMax().getX(); x++) for (int y =
	 * WGConfigLoc.getIronMineMin().getY(); y <=
	 * WGConfigLoc.getIronMineMax().getY(); y++) for (int z =
	 * WGConfigLoc.getIronMineMin().getZ(); z <=
	 * WGConfigLoc.getIronMineMax().getZ(); z++) { int x$ = x; int y$ = y; int z$ =
	 * z; Bukkit.getScheduler().runTask(this, () -> { BlockState block =
	 * Bukkit.getWorld("spawn").getBlockAt(x$, y$, z$).getState();
	 * block.setType(Material.IRON_ORE); block.update(true, false); }); } for (int x
	 * = WGConfigLoc.getLapisMineMin().getX(); x <=
	 * WGConfigLoc.getLapisMineMax().getX(); x++) for (int y =
	 * WGConfigLoc.getLapisMineMin().getY(); y <=
	 * WGConfigLoc.getLapisMineMax().getY(); y++) for (int z =
	 * WGConfigLoc.getLapisMineMin().getZ(); z <=
	 * WGConfigLoc.getLapisMineMax().getZ(); z++) { int x$ = x; int y$ = y; int z$ =
	 * z; Bukkit.getScheduler().runTask(this, () -> { BlockState block =
	 * Bukkit.getWorld("spawn").getBlockAt(x$, y$, z$).getState();
	 * block.setType(Material.LAPIS_ORE); block.update(true, false); }); } for (int
	 * x = WGConfigLoc.getQuartzMineMin().getX(); x <=
	 * WGConfigLoc.getQuartzMineMax().getX(); x++) for (int y =
	 * WGConfigLoc.getQuartzMineMin().getY(); y <=
	 * WGConfigLoc.getQuartzMineMax().getY(); y++) for (int z =
	 * WGConfigLoc.getQuartzMineMin().getZ(); z <= WGConfigLoc.getQuartzMineMax()
	 * .getZ(); z++) { int x$ = x; int y$ = y; int z$ = z;
	 * Bukkit.getScheduler().runTask(this, () -> { BlockState block =
	 * Bukkit.getWorld("spawn").getBlockAt(x$, y$, z$).getState();
	 * block.setType(Material.QUARTZ_ORE); block.update(true, false); }); } for (int
	 * x = WGConfigLoc.getGoldMineMin().getX(); x <=
	 * WGConfigLoc.getGoldMineMax().getX(); x++) for (int y =
	 * WGConfigLoc.getGoldMineMin().getY(); y <=
	 * WGConfigLoc.getGoldMineMax().getY(); y++) for (int z =
	 * WGConfigLoc.getGoldMineMin().getZ(); z <=
	 * WGConfigLoc.getGoldMineMax().getZ(); z++) { int x$ = x; int y$ = y; int z$ =
	 * z; Bukkit.getScheduler().runTask(this, () -> { BlockState block =
	 * Bukkit.getWorld("spawn").getBlockAt(x$, y$, z$).getState();
	 * block.setType(Material.GOLD_ORE); block.update(true, false); }); } for (int x
	 * = WGConfigLoc.getDiamondMineMin().getX(); x <=
	 * WGConfigLoc.getDiamondMineMax().getX(); x++) for (int y =
	 * WGConfigLoc.getDiamondMineMin().getY(); y <=
	 * WGConfigLoc.getDiamondMineMax().getY(); y++) for (int z =
	 * WGConfigLoc.getDiamondMineMin().getZ(); z <= WGConfigLoc.getDiamondMineMax()
	 * .getZ(); z++) { int x$ = x; int y$ = y; int z$ = z;
	 * Bukkit.getScheduler().runTask(this, () -> { BlockState block =
	 * Bukkit.getWorld("spawn").getBlockAt(x$, y$, z$).getState();
	 * block.setType(Material.DIAMOND_ORE); block.update(true, false); }); } for
	 * (int x = WGConfigLoc.getEmeraldMineMin().getX(); x <=
	 * WGConfigLoc.getEmeraldMineMax().getX(); x++) for (int y =
	 * WGConfigLoc.getEmeraldMineMin().getY(); y <=
	 * WGConfigLoc.getEmeraldMineMax().getY(); y++) for (int z =
	 * WGConfigLoc.getEmeraldMineMin().getZ(); z <= WGConfigLoc.getEmeraldMineMax()
	 * .getZ(); z++) { int x$ = x; int y$ = y; int z$ = z;
	 * Bukkit.getScheduler().runTask(this, () -> { BlockState block =
	 * Bukkit.getWorld("spawn").getBlockAt(x$, y$, z$).getState();
	 * block.setType(Material.EMERALD_ORE); block.update(true, false); }); }
	 * System.gc(); }, 10 * 20L, 60 * 10 * 20L); }
	 */

	// TODO You can do in other ways here.
	private void enchantament() {
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			Bukkit.getOnlinePlayers().forEach((Player player) -> {
				ItemStack ih = player.getInventory().getHelmet();
				ItemStack icp = player.getInventory().getChestplate();
				ItemStack il = player.getInventory().getLeggings();
				ItemStack ib = player.getInventory().getBoots();
				if (!(ih == null || ih.getType().equals(Material.AIR))) {
					if (EnchantUtil.checkPotion(player, PotionEffectType.NIGHT_VISION, 300)
							&& EnchantUtil.hasEnchant(ih, CEnchantType.GLOWING))
						player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 305, 0), true);
				}
				if (!(icp == null || icp.getType().equals(Material.AIR))) {
					if (EnchantUtil.checkPotion(player, PotionEffectType.REGENERATION, 30)
							&& EnchantUtil.hasEnchant(icp, CEnchantType.REGENERATION1))
						player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 25, 0), true);
					if (EnchantUtil.checkPotion(player, PotionEffectType.REGENERATION, 30)
							&& EnchantUtil.hasEnchant(icp, CEnchantType.REGENERATION2))
						player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 25, 1), true);
				}
				if (!(il == null || il.getType().equals(Material.AIR))) {
					if (EnchantUtil.checkPotion(player, PotionEffectType.FIRE_RESISTANCE, 30)
							&& EnchantUtil.hasEnchant(il, CEnchantType.OBSIDIANSHIELD))
						player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 25, 0), true);
					if (EnchantUtil.checkPotion(player, PotionEffectType.JUMP, 30)
							&& EnchantUtil.hasEnchant(il, CEnchantType.ROCKETS1))
						player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 25, 0), true);
					if (EnchantUtil.checkPotion(player, PotionEffectType.JUMP, 30)
							&& EnchantUtil.hasEnchant(il, CEnchantType.ROCKETS2))
						player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 25, 1), true);
				}
				if (!(ib == null || ib.getType().equals(Material.AIR))) {
					if (EnchantUtil.checkPotion(player, PotionEffectType.SPEED, 30)
							&& EnchantUtil.hasEnchant(ib, CEnchantType.WHEELS1))
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 25, 0), true);
					if (EnchantUtil.checkPotion(player, PotionEffectType.SPEED, 30)
							&& EnchantUtil.hasEnchant(ib, CEnchantType.WHEELS2))
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 25, 1), true);
				}
			});
		}, 20L, 20L);
	}

	private void randomMsg() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
			Bukkit.broadcastMessage("§c>> §2=================================/n" + "§c>>/n" + "§c>> "
					+ ElementalsUtil.getAutoMessages()
							.get(ElementalsUtil.nextInt(ElementalsUtil.getAutoMessages().size()))
					+ "/n" + "§c>>/n" + "§c>> §2=================================");
		}, 1L, 2 * 60 * 20L);
	}

	/*
	 * private void autoRestart() { Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART in doua minute!"
	 * ); Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> { String now
	 * = new SimpleDateFormat("HH-mm-ss").format(new Date()); switch (now) { case
	 * "09-59-00": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART intr-un minut!"
	 * ); break; case "11-59-30": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART in 30 secunde!"
	 * ); break; case "09-59-45": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART in 15 secunde!"
	 * ); break; case "09-59-46": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART in 14 secunde!"
	 * ); break; case "09-59-47": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART in 13 secunde!"
	 * ); break; case "09-59-48": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART in 12 secunde!"
	 * ); break; case "09-59-49": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART in 11 secunde!"
	 * ); break; case "09-59-50": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART in 10 secunde!"
	 * ); break; case "09-59-51": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART in 9 secunde!"
	 * ); break; case "09-59-52": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART in 8 secunde!"
	 * ); break; case "09-59-53": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART in 7 secunde!"
	 * ); break; case "09-59-54": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART in 6 secunde!"
	 * ); break; case "09-59-55": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART in 5 secunde!"
	 * ); break; case "09-59-56": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART in 4 secunde!"
	 * ); break; case "09-59-57": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART in 3 secunde!"
	 * ); break; case "09-59-58": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART in 2 secunde!"
	 * ); break; case "09-59-59": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eRESTART intr-o secunda!"
	 * ); break; case "10-00-00": Bukkit.
	 * broadcastMessage("§a§l[§6PikaCraft §f- §dCONSOLE§a§l] §eServerul se restarteaza."
	 * ); for (User user : getOnlineUsers()) { user.togglePvp(false);
	 * user.getBase().
	 * kickPlayer("§cServerul se restarteaza! Reveniti peste un minut!"); }
	 * Bukkit.spigot().restart(); break; default: break; } }, 20L, 20L); }
	 */

	@SuppressWarnings("deprecation")
	private void timer() {
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			String now = new SimpleDateFormat("HH-mm-ss").format(new Date());
			switch (now) {
			case "09-00-00":
				/*
				 * Bukkit.getScheduler().runTaskAsynchronously(get(), () -> { try { Bukkit.
				 * broadcastMessage("§a§l[§6PikaCraft - §fCONSOLA§a§l] §9§lSe creaza un backup..."
				 * ); long start = System.currentTimeMillis(); Calendar cal =
				 * Calendar.getInstance(); cal.add(Calendar.DAY_OF_MONTH, -3); for (File files :
				 * new File("backup").listFiles()) if (files.getName().startsWith(new
				 * SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()))) files.delete();
				 * ElementalsUtil.zipDir("backup" + File.separator + new
				 * SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date()) + "_backup.zip",
				 * "world", "plugins", "world_nether"); System.gc(); int end = (int)
				 * (System.currentTimeMillis() - start) / 1000; Bukkit.broadcastMessage(
				 * "§a§l[§6PikaCraft - §fCONSOLA§a§l] §9§lBackup creat. §f(" + end + " sec)"); }
				 * catch (Exception exception) { exception.printStackTrace(); } });
				 */
				break;
			case "09-58-00":
				// autoRestart();
				break;
			case "15-50-00":
				// TODO Labritint
				/*
				 * for ( Player player : Bukkit.getOnlinePlayers()) if
				 * (WGConfigLoc.checkMaze(player.getLocation())) player.teleport(new
				 * Location(Bukkit.getWorld("spawn"), -0.5, 55, -146.5));
				 * ElementalsUtil.generateMaze(Bukkit.getWorld("spawn"). getBlockAt(5, 55,
				 * -148), 98, 25, 25, 3, 3); Bukkit.broadcastMessage(
				 * "§f[§6/Warp Labirint§f] §aLabirintul a fost resetat! Va mai fi resetat peste 24 de ore."
				 * );
				 */
				break;
			default:
				break;
			}
			// ElementalsUtil.rainbowBlock();
			getOnlineUsers().forEach((User user) -> {
				FeatherBoardAPI.showScoreboard(user.getBase(), board);
				if (user.getPvpTicks() > 1)
					user.setPvpTicks(user.getPvpTicks() - 1);
				else if (user.getPvpTicks() == 1) {
					user.togglePvp(false);
					user.getBase().sendMessage("§6Nu mai esti in §cPvP§6! Te poti deconecta.");
					user.setPvpTicks(user.getPvpTicks() - 1);
				}
				// Fuck you Spigot!
				if (user.getBase().getHealth() > user.getBase().getMaxHealth())
					user.getBase().setHealth(user.getBase().getMaxHealth());
				if (user.getBase().isGliding())
					NCPExemptionManager.exemptPermanently(user.getBase().getUniqueId(), CheckType.MOVING_SURVIVALFLY);
				else {
					if (user.isInElytraParkourStart()) {
						user.toggleElytraParkourStart(false);
						user.getBase().teleport(new Location(Bukkit.getWorld("spawn"), 231.5, 67, 146.5, -90, 0));
					}
					NCPExemptionManager.unexempt(user.getBase().getUniqueId(), CheckType.MOVING_SURVIVALFLY);
				}
			});
			ElementalsUtil.tickMotd();
		}, 20L, 20L);
	}
}