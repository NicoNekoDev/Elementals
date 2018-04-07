package ro.nicuch.elementals.elementals.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.jline.internal.Preconditions;
import org.bukkit.entity.Player;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;
import ro.nicuch.elementals.Elementals;
import ro.nicuch.elementals.User;

public class AdminCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Poti folosi comanda doar ca jucator!");
			return true;
		}
		User user = Elementals.getUser((Player) sender);
		if (!user.hasPermission("elementals.admin.command"))
			return false;
		if (args.length > 0) {
			switch (args[0]) {
			case "npc":
				if (args.length > 1) {
					NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);
					if (npc == null) {
						sender.sendMessage("§cNu ai un NPC selectat!");
						break;
					}
					switch (args[1]) {
					case "off-hand":
						Preconditions.checkNotNull(user.getBase().getInventory().getItemInMainHand());
						npc.getTrait(Equipment.class).set(EquipmentSlot.OFF_HAND,
								user.getBase().getInventory().getItemInMainHand().clone());
						break;
					default:
						sender.sendMessage("§cPrea putine argumente!");
						break;
					}
				} else
					sender.sendMessage("§cPrea putine argumente!");
				break;
			case "reset":
				if (args.length > 1) {
					switch (args[1]) {
					case "kills":
						if (args.length > 2) {
							OfflinePlayer offline = Bukkit.getPlayerExact(args[2]);
							if (offline.isOnline()) {
								try {
									offline.getPlayer().setStatistic(Statistic.PLAYER_KILLS, 0);
									Elementals.getBase().prepareStatement("UPDATE topkills SET kills='0' WHERE uuid='"
											+ offline.getUniqueId().toString() + "';").executeUpdate();
									sender.sendMessage("§aKill-urile au fost resetate pentru "
											+ offline.getPlayer().getDisplayName());
								} catch (Exception exception) {
									exception.printStackTrace();
								}
							} else
								sender.sendMessage("§cJucatorul trebuie sa fie online!");
						} else
							sender.sendMessage("§cPrea putine argumente!");
						break;
					default:
						sender.sendMessage("§cPrea putine argumente!");
						break;
					}
				} else
					sender.sendMessage("§cPrea putine argumente!");
				break;
			case "fake":
				if (args.length > 1) {
					switch (args[1]) {
					case "join":
						if (args.length > 2) {
							OfflinePlayer offline = Bukkit.getPlayerExact(args[2]);
							if (offline.isOnline()) {
								for (User users : Elementals.getOnlineUsers())
									if (users.hasSounds())
										users.getBase().playSound(users.getBase().getLocation(), Sound.BLOCK_NOTE_PLING,
												1, 1);
								Bukkit.broadcastMessage(offline.getPlayer().getDisplayName() + " §bs-a conectat.");
							} else
								sender.sendMessage("§cJucatorul trebuie sa fie online!");
						} else
							sender.sendMessage("§cPrea putine argumente!");
						break;
					case "leave":
						if (args.length > 2) {
							OfflinePlayer offline = Bukkit.getPlayerExact(args[2]);
							if (offline.isOnline()) {
								Player online = Bukkit.getPlayer(args[2]);
								for (User users : Elementals.getOnlineUsers())
									if (users.hasSounds())
										users.getBase().playSound(users.getBase().getLocation(), Sound.BLOCK_NOTE_BASS,
												1, 1);
								Bukkit.broadcastMessage(online.getDisplayName() + " §bs-a deconectat.");
							} else
								sender.sendMessage("§cJucatorul trebuie sa fie online!");
						} else
							sender.sendMessage("§cPrea putine argumente!");
						break;
					case "afk-on":
						if (args.length > 2) {
							OfflinePlayer offline = Bukkit.getPlayerExact(args[2]);
							if (offline.isOnline()) {
								Player online = Bukkit.getPlayer(args[2]);
								Bukkit.broadcastMessage(online.getDisplayName() + " §5este AFK.");
							} else
								sender.sendMessage("§cJucatorul trebuie sa fie online!");
						} else
							sender.sendMessage("§cPrea putine argumente!");
						break;
					case "afk-off":
						if (args.length > 2) {
							OfflinePlayer offline = Bukkit.getPlayerExact(args[2]);
							if (offline.isOnline()) {
								Player online = Bukkit.getPlayer(args[2]);
								Bukkit.broadcastMessage(online.getDisplayName() + " §5nu mai este AFK.");
							} else
								sender.sendMessage("§cJucatorul trebuie sa fie online!");
						} else
							sender.sendMessage("§cPrea putine argumente!");
						break;
					default:
						sender.sendMessage("§cPrea putine argumente!");
						break;
					}
				} else
					sender.sendMessage("§cPrea putine argumente!");
				break;
			default:
				sender.sendMessage("§cPrea putine argumente!");
				break;
			}
		} else
			sender.sendMessage("§cPrea putine argumente!");
		return true;
	}
}
