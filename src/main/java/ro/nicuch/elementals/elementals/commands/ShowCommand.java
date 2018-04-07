package ro.nicuch.elementals.elementals.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ro.nicuch.elementals.Elementals;
import ro.nicuch.elementals.User;
import ro.nicuch.elementals.elementals.ElementalsUtil;
import ro.nicuch.elementals.hover.HoverUtil;

public class ShowCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		User user = Elementals.getUser((Player) sender);
		/*
		 * TODO Add later ess com.earth2me.essentials.User essUser =
		 * Elementals.getEssentials().getUserMap() .getUser(user.getBase().getName());
		 * if (essUser.isMuted()) {
		 * sender.sendMessage("§cNu poti folosi comanda cat timp ai mute!"); return
		 * true; }
		 */
		ItemStack item = user.getBase().getInventory().getItemInMainHand();
		if (item.getType().equals(Material.AIR)) {
			sender.sendMessage("§cTrebuie sa ti un obiect in mana!");
			return true;
		}
		ElementalsUtil.broadcastRawMessage(HoverUtil.rawMessage(
				"[\"\",{\"text\":\"[\",\"color\":\"red\"},{\"text\":\"/show\",\"color\":\"blue\"},{\"text\":\"]\",\"color\":\"red\"},{\"text\":\" "
						+ user.getBase().getDisplayName()
						+ " \"},{\"text\":\"> \",\"bold\":true,\"color\":\"yellow\"},{\"text\":\""
						+ item.getItemMeta().getDisplayName()
						+ "\",\"hoverEvent\":{\"action\":\"show_item\",\"value\":\"",
				"\"}}]", item));
		// HoverUtil.convertItemToHover("§c[§b/show§c] §r" +
		// user.getBase().getDisplayName() + " §e§l> §r", null,
		// user.getBase().getInventory().getItemInMainHand()));
		return true;
	}
}
