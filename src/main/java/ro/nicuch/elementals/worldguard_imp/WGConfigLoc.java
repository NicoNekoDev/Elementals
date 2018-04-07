package ro.nicuch.elementals.worldguard_imp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
//import org.bukkit.entity.Player;

//import ro.nicuch.elementals.Elementals;
//import ro.nicuch.elementals.elementals.ElementalsUtil;

public class WGConfigLoc {
	// TODO Portalele catre tinuturi
	//private static final WGRegion endPortalMax = new WGRegion(-96, 61, -180);
	//private static final WGRegion endPortalMin = new WGRegion(-98, 59, -182);
	private static final WGRegion icePortalMax = new WGRegion(980, 68, -85);
	private static final WGRegion icePortalMin = new WGRegion(978, 65, -87);
	private static final WGRegion lavaPortalMax = new WGRegion(980, 68, 8);
	private static final WGRegion lavaPortalMin = new WGRegion(978, 65, 6);
	
	private static final WGRegion mine_max = new WGRegion(18, 65, 5);
	private static final WGRegion mine_min = new WGRegion(-18, 54, -37);
	// TODO Creaza o alta arena pvp
	private static final Location pvpCenter = new Location(Bukkit.getWorld("spawn"), 1000, 0, 1000);
	private static final WGRegion quartz_mine_min = new WGRegion(5, 54, -8);
	private static final WGRegion quartz_mine_max = new WGRegion(17, 61, 4);
	private static final WGRegion lapis_mine_min = new WGRegion(5, 54, -22);
	private static final WGRegion lapis_mine_max = new WGRegion(17, 61, -10);
	private static final WGRegion iron_mine_min = new WGRegion(-17, 54, -8);
	private static final WGRegion iron_mine_max = new WGRegion(-5, 61, 4);
	private static final WGRegion gold_mine_min = new WGRegion(-17, 54, -22);
	private static final WGRegion gold_mine_max = new WGRegion(-5, 61, -10);
	private static final WGRegion emerald_mine_min = new WGRegion(5, 54, -36);
	private static final WGRegion emerald_mine_max = new WGRegion(17, 61, -24);
	private static final WGRegion diamond_mine_min = new WGRegion(-17, 54, -36);
	private static final WGRegion diamond_mine_max = new WGRegion(-5, 61, -24);
	private static final WGRegion mobArena_max = new WGRegion(96, 80, 119);
	private static final WGRegion mobArena_min = new WGRegion(62, 67, 85);

	public static boolean checkElytraParkourEnd(Location loc) {
		return false;
		/*
		 * return (elytraParkourEndMin.getX() <= loc.getBlockX() &&
		 * elytraParkourEndMin.getY() <= loc.getBlockY() &&
		 * elytraParkourEndMin.getZ() <= loc.getBlockZ() &&
		 * elytraParkourEndMax.getX() >= loc.getBlockX() &&
		 * elytraParkourEndMax.getY() >= loc.getBlockY() &&
		 * elytraParkourEndMax.getZ() >= loc.getBlockZ() &&
		 * loc.getWorld().equals(Bukkit.getWorld("spawn")));
		 */
	}

	public static boolean checkElytraParkourExit(Location loc) {
		return false;
		/*
		 * return (elytrParkourExitMin.getX() <= loc.getBlockX() &&
		 * elytrParkourExitMin.getY() <= loc.getBlockY() &&
		 * elytrParkourExitMin.getZ() <= loc.getBlockZ() &&
		 * elytrParkourExitMax.getX() >= loc.getBlockX() &&
		 * elytrParkourExitMax.getY() >= loc.getBlockY() &&
		 * elytrParkourExitMax.getZ() >= loc.getBlockZ() &&
		 * loc.getWorld().equals(Bukkit.getWorld("spawn")));
		 */
	}

	public static boolean checkElytraParkourStart(Location loc) {
		return false;
		/*
		 * return (elytrParkourStartMin.getX() <= loc.getBlockX() &&
		 * elytrParkourStartMin.getY() <= loc.getBlockY() &&
		 * elytrParkourStartMin.getZ() <= loc.getBlockZ() &&
		 * elytrParkourStartMax.getX() >= loc.getBlockX() &&
		 * elytrParkourStartMax.getY() >= loc.getBlockY() &&
		 * elytrParkourStartMax.getZ() >= loc.getBlockZ() &&
		 * loc.getWorld().equals(Bukkit.getWorld("spawn")));
		 */
	}

	public static boolean checkEndDungeonPortal(Location loc) {
		return false;
		//return (endPortalMin.getX() <= loc.getBlockX() && endPortalMin.getY() <= loc.getBlockY()
		//		&& endPortalMin.getZ() <= loc.getBlockZ() && endPortalMax.getX() >= loc.getBlockX()
		//		&& endPortalMax.getY() >= loc.getBlockY() && endPortalMax.getZ() >= loc.getBlockZ()
		//		&& loc.getWorld().equals(Bukkit.getWorld("spawn")));
	}

	public static boolean checkIceDungeonPortal(Location loc) {
		return (icePortalMin.getX() <= loc.getBlockX() && icePortalMin.getY() <= loc.getBlockY()
				&& icePortalMin.getZ() <= loc.getBlockZ() && icePortalMax.getX() >= loc.getBlockX()
				&& icePortalMax.getY() >= loc.getBlockY() && icePortalMax.getZ() >= loc.getBlockZ()
				&& loc.getWorld().equals(Bukkit.getWorld("spawn")));
	}

	public static boolean checkLavaDungeonPortal(Location loc) {
		return (lavaPortalMin.getX() <= loc.getBlockX() && lavaPortalMin.getY() <= loc.getBlockY()
				&& lavaPortalMin.getZ() <= loc.getBlockZ() && lavaPortalMax.getX() >= loc.getBlockX()
				&& lavaPortalMax.getY() >= loc.getBlockY() && lavaPortalMax.getZ() >= loc.getBlockZ()
				&& loc.getWorld().equals(Bukkit.getWorld("spawn")));
	}

	/*
	 * public static void teleportPvP( Player player) { int random =
	 * ElementalsUtil.nextInt(9); switch (random) { case 1:
	 * player.teleport(pvp1); break; case 2: player.teleport(pvp2); break; case
	 * 3: player.teleport(pvp3); break; case 4: player.teleport(pvp4); break;
	 * case 5: player.teleport(pvp5); break; case 6: player.teleport(pvp6);
	 * break; case 7: player.teleport(pvp7); break; case 8:
	 * player.teleport(pvp8); break; case 9: player.teleport(pvp9); break;
	 * default: player.teleport(pvp9); break; } com.earth2me.essentials.User
	 * user = Elementals.getEssentials().getUser(player);
	 * user.resetInvulnerabilityAfterTeleport(); }
	 */

	public static boolean checkInMine(Location loc) {
		return (mine_min.getX() <= loc.getBlockX() && mine_min.getY() <= loc.getBlockY()
				&& mine_min.getZ() <= loc.getBlockZ() && mine_max.getX() >= loc.getBlockX()
				&& mine_max.getY() >= loc.getBlockY() && mine_max.getZ() >= loc.getBlockZ());
	}

	public static boolean checkPvP(Location loc) {
		return (pvpCenter.distance(loc) <= 38);
	}

	public static boolean diamondMine(Location loc) {
		return (diamond_mine_max.getX() >= loc.getX() && diamond_mine_min.getX() <= loc.getX()
				&& diamond_mine_max.getZ() >= loc.getZ() && diamond_mine_min.getZ() <= loc.getZ()
				&& diamond_mine_max.getY() >= loc.getY() && diamond_mine_min.getY() <= loc.getY()
				&& loc.getWorld().equals(Bukkit.getWorld("spawn")));
	}

	public static boolean emeraldMine(Location loc) {
		return (emerald_mine_max.getX() >= loc.getX() && emerald_mine_min.getX() <= loc.getX()
				&& emerald_mine_max.getZ() >= loc.getZ() && emerald_mine_min.getZ() <= loc.getZ()
				&& emerald_mine_max.getY() >= loc.getY() && emerald_mine_min.getY() <= loc.getY()
				&& loc.getWorld().equals(Bukkit.getWorld("spawn")));
	}

	public static boolean lapisMine(Location loc) {
		return (lapis_mine_max.getX() >= loc.getX() && lapis_mine_min.getX() <= loc.getX()
				&& lapis_mine_max.getZ() >= loc.getZ() && lapis_mine_min.getZ() <= loc.getZ()
				&& lapis_mine_max.getY() >= loc.getY() && lapis_mine_min.getY() <= loc.getY()
				&& loc.getWorld().equals(Bukkit.getWorld("spawn")));
	}

	public static WGRegion getDiamondMineMax() {
		return diamond_mine_max;
	}

	public static WGRegion getDiamondMineMin() {
		return diamond_mine_min;
	}

	public static WGRegion getEmeraldMineMax() {
		return emerald_mine_max;
	}

	public static WGRegion getEmeraldMineMin() {
		return emerald_mine_min;
	}

	public static WGRegion getLapisMineMax() {
		return lapis_mine_max;
	}

	public static WGRegion getLapisMineMin() {
		return lapis_mine_min;
	}

	public static WGRegion getGoldMineMax() {
		return gold_mine_max;
	}

	public static WGRegion getGoldMineMin() {
		return gold_mine_min;
	}

	public static WGRegion getIronMineMax() {
		return iron_mine_max;
	}

	public static WGRegion getIronMineMin() {
		return iron_mine_min;
	}

	public static WGRegion getQuartzMineMax() {
		return quartz_mine_max;
	}

	public static WGRegion getQuartzMineMin() {
		return quartz_mine_min;
	}

	public static boolean goldMine(Location loc) {
		return (gold_mine_max.getX() >= loc.getX() && gold_mine_min.getX() <= loc.getX()
				&& gold_mine_max.getZ() >= loc.getZ() && gold_mine_min.getZ() <= loc.getZ()
				&& gold_mine_max.getY() >= loc.getY() && gold_mine_min.getY() <= loc.getY()
				&& loc.getWorld().equals(Bukkit.getWorld("spawn")));
	}

	public static boolean ironMine(Location loc) {
		return (iron_mine_max.getX() >= loc.getX() && iron_mine_min.getX() <= loc.getX()
				&& iron_mine_max.getZ() >= loc.getZ() && iron_mine_min.getZ() <= loc.getZ()
				&& iron_mine_max.getY() >= loc.getY() && iron_mine_min.getY() <= loc.getY()
				&& loc.getWorld().equals(Bukkit.getWorld("spawn")));
	}

	public static boolean quartzMine(Location loc) {
		return (quartz_mine_max.getX() >= loc.getX() && quartz_mine_min.getX() <= loc.getX()
				&& quartz_mine_max.getZ() >= loc.getZ() && quartz_mine_min.getZ() <= loc.getZ()
				&& quartz_mine_max.getY() >= loc.getY() && quartz_mine_min.getY() <= loc.getY()
				&& loc.getWorld().equals(Bukkit.getWorld("spawn")));
	}

	public static boolean checkMaze(Location loc) {
		return false;
		/*
		 * return (maze_min.getX() <= loc.getBlockX() && maze_min.getY() <=
		 * loc.getBlockY() && maze_min.getZ() <= loc.getBlockZ() &&
		 * maze_max.getX() >= loc.getBlockX() && maze_max.getY() >=
		 * loc.getBlockY() && maze_max.getZ() >= loc.getBlockZ() &&
		 * loc.getWorld().equals(Bukkit.getWorld("spawn")));
		 */
	}

	public static boolean checkParkour(Location loc) {
		return false;
		/*
		 * return (parkour_min.getX() <= loc.getBlockX() && parkour_min.getY()
		 * <= loc.getBlockY() && parkour_min.getZ() <= loc.getBlockZ() &&
		 * parkour_max.getX() >= loc.getBlockX() && parkour_max.getY() >=
		 * loc.getBlockY() && parkour_max.getZ() >= loc.getBlockZ() &&
		 * loc.getWorld().equals(Bukkit.getWorld("spawn")));
		 */
	}

	public static boolean checkMobArena(Location loc) {
		return (mobArena_min.getX() <= loc.getBlockX() && mobArena_min.getY() <= loc.getBlockY()
				&& mobArena_min.getZ() <= loc.getBlockZ() && mobArena_max.getX() >= loc.getBlockX()
				&& mobArena_max.getY() >= loc.getBlockY() && mobArena_max.getZ() >= loc.getBlockZ()
				&& loc.getWorld().equals(Bukkit.getWorld("spawn")));
	}

	public static boolean checkSpleefArena(Location loc) {
		return false;
		/*
		 * return (spleefArena_min.getX() <= loc.getBlockX() &&
		 * spleefArena_min.getY() <= loc.getBlockY() && spleefArena_min.getZ()
		 * <= loc.getBlockZ() && spleefArena_max.getX() >= loc.getBlockX() &&
		 * spleefArena_max.getY() >= loc.getBlockY() && spleefArena_max.getZ()
		 * >= loc.getBlockZ() &&
		 * loc.getWorld().equals(Bukkit.getWorld("spawn")));
		 */
	}
}
