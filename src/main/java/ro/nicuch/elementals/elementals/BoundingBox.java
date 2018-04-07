package ro.nicuch.elementals.elementals;

import org.bukkit.Location;

public class BoundingBox {

	private final double xMax;
	private final double yMax;
	private final double zMax;
	private final double xMin;
	private final double yMin;
	private final double zMin;

	public BoundingBox(double x, double y, double z, Location entLoc) {
		double xN = (x / 2);
		double zN = (z / 2);
		this.xMax = entLoc.getX() + xN;
		this.yMax = entLoc.getY() + y;
		this.zMax = entLoc.getZ() + zN;
		this.xMin = entLoc.getX() - xN;
		this.yMin = entLoc.getY();
		this.zMin = entLoc.getZ() - zN;
	}

	public boolean check(Location loc) {
		return loc.getX() <= this.xMax && loc.getX() >= this.xMin && loc.getY() <= this.yMax && loc.getY() >= this.yMin
				&& loc.getZ() <= this.zMax && loc.getZ() >= this.zMin;
	}
}
