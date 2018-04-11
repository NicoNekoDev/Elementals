package ro.nicuch.elementals.dungeon;

public class DungeonProtection {
    private final int x;
    private final int y;
    private final int z;

    public DungeonProtection(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public DungeonProtection(int x, int z) {
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
