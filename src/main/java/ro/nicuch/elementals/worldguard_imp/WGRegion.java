package ro.nicuch.elementals.worldguard_imp;

public class WGRegion {
	private final int x;
	private final int y;
	private final int z;

	public WGRegion(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public WGRegion(int x, int z) {
		this(x, 0, z);
	}

	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}
}
