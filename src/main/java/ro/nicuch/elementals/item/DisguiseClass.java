package ro.nicuch.elementals.item;

import org.bukkit.scheduler.BukkitTask;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;

public class DisguiseClass {
    private final BukkitTask task;
    private final DisguiseType type;

    public DisguiseClass(BukkitTask task, DisguiseType type) {
        this.task = task;
        this.type = type;
    }

    public BukkitTask getBukkitTask() {
        return this.task;
    }

    public DisguiseType getDisguiseType() {
        return this.type;
    }

}
