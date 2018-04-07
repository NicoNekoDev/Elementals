package ro.nicuch.elementals.elementals.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ro.nicuch.elementals.Elementals;
import ro.nicuch.elementals.User;

public class GiveAllCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		User user = Elementals.getUser((Player) sender);
		if (!user.hasPermission("elementals.giveall")) {
			user.getBase().sendMessage("§cNu ai permisiunea!");
			return true;
		}
		for (ItemStack item : user.getBase().getInventory().getContents())
			if (item != null)
				Bukkit.getOnlinePlayers().forEach((Player player) -> {
					if (player != sender)
						player.getInventory().addItem(item);
				});
		return true;
	}
}
