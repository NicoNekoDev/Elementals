package ro.nicuch.elementals.hover;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class HoverUtil {

	public static String rawMessage(String first, String second, ItemStack item) {
		return first + CraftItemStack.asNMSCopy(item).getTag().toString() + second;
	}
}
