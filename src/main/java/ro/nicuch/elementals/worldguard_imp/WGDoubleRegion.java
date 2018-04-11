package ro.nicuch.elementals.worldguard_imp;

public class WGDoubleRegion {
    private final double x;
    private final double y;
    private final double z;

    public WGDoubleRegion(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public WGDoubleRegion(double x, double z) {
        this(x, 0, z);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
}
