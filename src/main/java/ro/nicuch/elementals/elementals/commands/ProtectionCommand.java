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
import ro.nicuch.elementals.protection.FieldUtil;

public class ProtectionCommand implements CommandExecutor, TabCompleter {
	private static final String[] COMMANDS = { "info", "allow", "allowall", "remove", "removeall", "disable", "enable",
			"visualise", "loc" };

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		User user = Elementals.getUser((Player) sender);
		if (args.length > 0) {
			switch (args[0]) {
			case "allow":
				if (args.length < 2)
					sender.sendMessage("§cTrebuie sa scri si numele unui jucator!");
				else
					FieldUtil.allowInField(user, args[1], false, true);
				break;
			case "allowall":
				if (args.length < 2)
					sender.sendMessage("§cTrebuie sa scri si numele unui jucator!");
				else
					FieldUtil.allowInField(user, args[1], true, true);
				break;
			case "remove":
				if (args.length < 2)
					sender.sendMessage("§cTrebuie sa scri si numele unui jucator!");
				else
					FieldUtil.allowInField(user, args[1], false, false);
				break;
			case "removeall":
				if (args.length < 2)
					sender.sendMessage("§cTrebuie sa scri si numele unui jucator!");
				else
					FieldUtil.allowInField(user, args[1], true, false);
				break;
			case "disable":
				if (user.isIgnoringPlacingFields()) {
					sender.sendMessage("§cAi dezactivat transformarea blocurilor de diamant din protectii deja.");
					break;
				}
				user.setIgnorePlacingFields(true);
				sender.sendMessage("§bAcum poti pune blocuri de diamant fara sa se transforme in protectii.");
				break;
			case "enable":
				if (!user.isIgnoringPlacingFields()) {
					sender.sendMessage("§cNu ai dezactivat transformarea blocurilor de diamant in protectii.");
					break;
				}
				user.setIgnorePlacingFields(false);
				sender.sendMessage("§bAcum blocurile de diamant care le pui se vor transforma in protectii.");
				break;
			case "visualise":
				FieldUtil.visualiseField(user);
				break;
			case "info":
				FieldUtil.infoField(user);
				break;
			case "loc":
			case "location":
				FieldUtil.locFields(user);
				break;
			case "take":
				FieldUtil.takeProtection(user);
				break;
			default:
				sender.sendMessage("§b/ps take §c-- §fDistruge protectia fara a folosi Pickaxe.");
				sender.sendMessage("§9/ps allow <jucator> §c-- §fAdauga un membru in protectie.");
				sender.sendMessage("§b/ps allowall <jucator> §c-- §fAdauga un membru in toate protectiile.");
				sender.sendMessage("§9/ps remove <jucator> §c-- §fSterge un membru din protectie.");
				sender.sendMessage("§b/ps removell <jucator> §c-- §fSterge un membru din toate protectiile.");
				sender.sendMessage("§9/ps enable §c-- §fBlocurile de diamant devin protectii.");
				sender.sendMessage("§b/ps disable §c-- §fPune blocuri fara sa se transforme in protectii.");
				sender.sendMessage("§9/ps visualise §c-- §fVizualizeaza protectia.");
				sender.sendMessage("§b/ps info §c-- §fAfiseaza informatii despre protectie.");
				sender.sendMessage("§b/ps loc §c-- §fAfiseaza locatiile protectiilor tale.");
				break;
			}
		} else {
			sender.sendMessage("§b/ps take §c-- §fDistruge protectia fara a folosi Pickaxe.");
			sender.sendMessage("§9/ps allow <jucator> -- §fAdauga un membru in protectie.");
			sender.sendMessage("§b/ps allowall <jucator> -- §fAdauga un membru in toate protectiile.");
			sender.sendMessage("§9/ps remove <jucator> -- §fSterge un membru din protectie.");
			sender.sendMessage("§b/ps removell <jucator> -- §fSterge un membru din toate protectiile.");
			sender.sendMessage("§9/ps enable -- §fBlocurile de diamant devin protectii.");
			sender.sendMessage("§b/ps disable -- §fPune blocuri fara sa se transforme in protectii.");
			sender.sendMessage("§9/ps visualise -- §fVizualizeaza protectia.");
			sender.sendMessage("§b/ps info §c-- §fAfiseaza informatii despre protectie.");
			sender.sendMessage("§b/ps loc §c-- §fAfiseaza locatiile protectiilor tale.");
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> completions = Lists.newArrayList();
		if (args.length == 1) {
			StringUtil.copyPartialMatches(args[0], Lists.newArrayList(COMMANDS), completions);
		}
		if (args.length == 2) {
			if (ElementalsUtil.getPlayersNames().size() == 0)
				return completions;
			StringUtil.copyPartialMatches(args[1], ElementalsUtil.getPlayersNames(), completions);
		}
		Collections.sort(completions);
		return completions;
	}
}
