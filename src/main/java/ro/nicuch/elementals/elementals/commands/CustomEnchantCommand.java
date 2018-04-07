package ro.nicuch.elementals.elementals.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.google.common.collect.Lists;

import ro.nicuch.elementals.Elementals;
import ro.nicuch.elementals.User;
import ro.nicuch.elementals.enchants.EnchantUtil;
import ro.nicuch.elementals.enchants.EnchantUtil.CEnchantType;

public class CustomEnchantCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		User user = Elementals.getUser((Player) sender);
		if (!user.hasPermission("elementals.customenchant")) {
			sender.sendMessage("§cNu ai permisiunea!");
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage("§cTrebuie sa definesti enchantul!");
			return true;
		}
		if (user.getBase().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
			sender.sendMessage("§cTrebuie sa ai un item in mana!");
			return true;
		}
		try {
			CEnchantType type = CEnchantType.valueOf(args[0].toUpperCase());
			user.getBase().getInventory().setItemInMainHand(
					EnchantUtil.enchantItem(user.getBase().getInventory().getItemInMainHand(), type));
		} catch (Exception ex) {
			sender.sendMessage("§cEnchant invalid!");
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> completions = Lists.newArrayList();
		if (args.length == 1) {
			String partialCommand = args[0];
			List<String> commands = Lists.newArrayList();
			for (CEnchantType type : CEnchantType.values())
				commands.add(type.name().toLowerCase());
			StringUtil.copyPartialMatches(partialCommand, commands, completions);
		}
		Collections.sort(completions);
		return completions;
	}
}
