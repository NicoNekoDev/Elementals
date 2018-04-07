package ro.nicuch.elementals.elementals.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ro.nicuch.elementals.Elementals;
import ro.nicuch.elementals.User;
import ro.nicuch.elementals.elementals.ElementalsUtil;

public class ChatCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			User user = Elementals.getUser((Player) sender);
			if (!user.hasPermission("elementals.clearchat")) {
				user.getBase().sendMessage("§cNu ai permisiunea!");
				return true;
			}
		}
		if (args.length == 0) {
			sender.sendMessage("§cPrea putine argumente");
			return true;
		}
		switch (args[0]) {
		case "stop":
			ElementalsUtil.toggleChat(true);
			Bukkit.broadcastMessage("§9Chatul a fost §coprit§9!");
			break;
		case "start":
			ElementalsUtil.toggleChat(false);
			Bukkit.broadcastMessage("§9Chatul a fost §apornit§9!");
			break;
		case "clear":
			for (int i = 0; i < 50; i++) {
				Bukkit.broadcastMessage("");
			}
			Bukkit.broadcastMessage("§9Chat-ul a fost sters!");
			break;
		default:
			sender.sendMessage("/chat clear");
			sender.sendMessage("/chat stop");
			sender.sendMessage("/chat start");
			break;
		}
		return true;
	}
}
