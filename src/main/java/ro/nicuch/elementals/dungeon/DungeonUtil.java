package ro.nicuch.elementals.dungeon;

import org.bukkit.Location;

public class DungeonUtil {
	private static final DungeonProtection lavaDungeonMax = new DungeonProtection(-756, 174);
	private static final DungeonProtection lavaDungeonMin = new DungeonProtection(-1168, -224);
	private static final DungeonProtection iceDungeonMax = new DungeonProtection(-763, 394);
	private static final DungeonProtection iceDungeonMin = new DungeonProtection(-950, 221);
	/*
	private static final DungeonProtection endDungeonMax = new DungeonProtection(700, 700);
	private static final DungeonProtection endDungeonMin = new DungeonProtection(-700, -700);
	private static final DungeonProtection endDungeonMax_part = new DungeonProtection(300, 300);
	private static final DungeonProtection endDungeonMin_part = new DungeonProtection(-300, -300);
	*/
	private static final DungeonProtection lavaDungeonProtMax = new DungeonProtection(-1617, 77, -139);
	private static final DungeonProtection lavaDungeonProtMin = new DungeonProtection(-1623, 72, -145);
	private static final DungeonProtection iceDungeonProtMax = new DungeonProtection(-934, 74, 382);
	private static final DungeonProtection iceDungeonProtMin = new DungeonProtection(-938, 72, 378);
	/*
	private static final DungeonProtection endDungeonProtMax = new DungeonProtection(503, 66, 503);
	private static final DungeonProtection endDungeonProtMin = new DungeonProtection(497, 63, 497);
	*/

	public static boolean checkLavaDungeon(Location loc) {
		return (loc.getWorld().getName().equals("spawn") && lavaDungeonMin.getX() <= loc.getBlockX()
				&& lavaDungeonMin.getZ() <= loc.getBlockZ() && lavaDungeonMax.getX() >= loc.getBlockX()
				&& lavaDungeonMax.getZ() >= loc.getBlockZ());
	}

	public static boolean checkIceDungeon(Location loc) {
		return (loc.getWorld().getName().equals("spawn") && iceDungeonMin.getX() <= loc.getBlockX()
				&& iceDungeonMin.getZ() <= loc.getBlockZ() && iceDungeonMax.getX() >= loc.getBlockX()
				&& iceDungeonMax.getZ() >= loc.getBlockZ());
	}

	public static boolean checkEndDungeon(Location loc) {
		return false;
	//	return (loc.getWorld().getName().equals("world_the_end") && endDungeonMin.getX() <= loc.getBlockX()
	//			&& endDungeonMin.getZ() <= loc.getBlockZ() && endDungeonMax.getX() >= loc.getBlockX()
	//			&& endDungeonMax.getZ() >= loc.getBlockZ());
	}

	public static boolean checkEndDungeonPart(Location loc) {
		return false;
	//	return (loc.getWorld().getName().equals("world_the_end") && endDungeonMin_part.getX() <= loc.getBlockX()
	//			&& endDungeonMin_part.getZ() <= loc.getBlockZ() && endDungeonMax_part.getX() >= loc.getBlockX()
	//			&& endDungeonMax_part.getZ() >= loc.getBlockZ());
	}

	public static boolean dungeonProtection(Location loc) {
		return (/*(loc.getWorld().getName().equals("world_the_end") && endDungeonProtMin.getX() <= loc.getBlockX()
				&& endDungeonProtMin.getY() <= loc.getBlockY() && endDungeonProtMin.getZ() <= loc.getBlockZ()
				&& endDungeonProtMax.getX() >= loc.getBlockX() && endDungeonProtMax.getY() >= loc.getBlockY()
				&& endDungeonProtMax.getZ() >= loc.getBlockZ())
				|| */
				(loc.getWorld().getName().equals("spawn") && iceDungeonProtMin.getX() <= loc.getBlockX()
						&& iceDungeonProtMin.getY() <= loc.getBlockY() && iceDungeonProtMin.getZ() <= loc.getBlockZ()
						&& iceDungeonProtMax.getX() >= loc.getBlockX() && iceDungeonProtMax.getY() >= loc.getBlockY()
						&& iceDungeonProtMax.getZ() >= loc.getBlockZ())
				|| (loc.getWorld().getName().equals("spawn") && lavaDungeonProtMin.getX() <= loc.getBlockX()
						&& lavaDungeonProtMin.getY() <= loc.getBlockY() && lavaDungeonProtMin.getZ() <= loc.getBlockZ()
						&& lavaDungeonProtMax.getX() >= loc.getBlockX() && lavaDungeonProtMax.getY() >= loc.getBlockY()
						&& lavaDungeonProtMax.getZ() >= loc.getBlockZ()));
	}
}
