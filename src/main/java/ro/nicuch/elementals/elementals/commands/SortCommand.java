package ro.nicuch.elementals.elementals.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.google.common.collect.Lists;

import ro.nicuch.elementals.Elementals;
import ro.nicuch.elementals.User;
import ro.nicuch.elementals.elementals.ElementalsUtil;

public class SortCommand implements CommandExecutor, TabCompleter {
	private static final String[] COMMANDS = { "chest", "inventory", "enderchest", "shulker" };

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length != 0) {
			User user = Elementals.getUser((Player) sender);
			switch (args[0].toLowerCase()) {
			case "chest":
			case "trapchest":
				ElementalsUtil.sortChest(user);
				break;
			case "inventory":
			case "inv":
				ElementalsUtil.sortInventory(user);
				break;
			case "enderchest":
			case "ec":
				ElementalsUtil.sortEnderChest(user);
				break;
			case "shulker":
				ElementalsUtil.sortShulkerBox(user);
				break;
			default:
				sender.sendMessage("§a/sort chest -- §fSorteaza un cufar.");
				sender.sendMessage("§6/sort inventory -- §fSorteaza-ti inventarul.");
				sender.sendMessage("§a/sort enderchest -- §fSorteaza-ti ender chest-ul.");
				sender.sendMessage("§6/sort shulker -- §fSorteaza un shulker box.");
				break;
			}
		} else {
			sender.sendMessage("§a/sort chest -- §fSorteaza un cufar.");
			sender.sendMessage("§6/sort inventory -- §fSorteaza-ti inventarul.");
			sender.sendMessage("§a/sort enderchest -- §fSorteaza-ti ender chest-ul.");
			sender.sendMessage("§6/sort shulker -- §fSorteaza un shulker box.");
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> completions = Lists.newArrayList();
		if (args.length == 1)
			StringUtil.copyPartialMatches(args[0], Lists.newArrayList(COMMANDS), completions);
		Collections.sort(completions);
		return completions;
	}
}
