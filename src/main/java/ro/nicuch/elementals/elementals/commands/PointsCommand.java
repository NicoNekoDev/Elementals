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

public class PointsCommand implements CommandExecutor, TabCompleter {
	private static final String[] COMMANDS = { "give", "take", "set", "reset" };

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			if (!sender.isOp())
				return true;
			switch (args[0]) {
			case "give":
				if (args.length < 2) {
					sender.sendMessage("§cTrebuie sa scri numele unui jucator!");
					break;
				} else {
					if (args.length < 3)
						sender.sendMessage("§cTrebuie sa scri suma!");
					else
						ElementalsUtil.changePoints((Player) sender, args[1], args[2], true);
				}
				break;
			case "take":
				if (args.length < 2) {
					sender.sendMessage("§cTrebuie sa scri numele unui jucator!");
					break;
				} else {
					if (args.length < 3)
						sender.sendMessage("§cTrebuie sa scri suma!");
					else
						ElementalsUtil.changePoints((Player) sender, args[1], args[2], false);
				}
				break;
			case "set":
				if (args.length < 2) {
					sender.sendMessage("§cTrebuie sa scri numele unui jucator!");
					break;
				} else {
					if (args.length < 3)
						sender.sendMessage("§cTrebuie sa scri suma!");
					else
						ElementalsUtil.setPoints((Player) sender, args[1], args[2], true);
				}
				break;
			case "reset":
				if (args.length < 2) {
					sender.sendMessage("§cTrebuie sa scri numele unui jucator!");
					break;
				} else
					ElementalsUtil.setPoints((Player) sender, args[1], "0", false);
				break;
			default:
				break;
			}
		} else {
			User user = Elementals.getUser((Player) sender);
			sender.sendMessage("§6§lP§9§lika§6§lP§9§loints§a§l: §c" + user.getPoints());
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> completions = Lists.newArrayList();
		if (args.length == 1) {
			String partialCommand = args[0];
			List<String> commands = Lists.newArrayList(COMMANDS);
			StringUtil.copyPartialMatches(partialCommand, commands, completions);
		}
		if (args.length == 2) {
			String partialJobs = args[1];
			StringUtil.copyPartialMatches(partialJobs, ElementalsUtil.getPlayersNames(), completions);
		}
		Collections.sort(completions);
		if (sender.isOp())
			return completions;
		else
			return null;
	}
}
