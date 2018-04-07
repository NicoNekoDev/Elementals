package ro.nicuch.elementals.casebox;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import ro.nicuch.elementals.casebox.CaseUtil.CaseType;
import ro.nicuch.elementals.dungeon.DungeonUtil;
import ro.nicuch.elementals.elementals.ElementalsUtil;
import ro.nicuch.elementals.enchants.EnchantUtil;

public class CaseListener implements Listener {
	
	//TODO Update to crates

	@EventHandler
	public void event(EntityDeathEvent event) {
		if (!(event.getEntity() instanceof Monster))
			return;
		if (ElementalsUtil.hasTag(event.getEntity(), "spawner"))
			return;
		if (DungeonUtil.checkEndDungeon(event.getEntity().getLocation())
				&& ElementalsUtil.hasTag(event.getEntity(), "end_monster")) {
			if (ElementalsUtil.nextInt(250) == 1)
				event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
						CaseUtil.getCaseKey(CaseType.DRAGONCASE));
			else if (ElementalsUtil.nextInt(185) == 1)
				event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
						CaseUtil.getCase(CaseType.DRAGONCASE));
		} else if (DungeonUtil.checkLavaDungeon(event.getEntity().getLocation())
				&& ElementalsUtil.hasTag(event.getEntity(), "lava_monster")) {
			if (ElementalsUtil.nextInt(250) == 1)
				event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
						CaseUtil.getCaseKey(CaseType.BLAZECASE));
			else if (ElementalsUtil.nextInt(185) == 1)
				event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
						CaseUtil.getCase(CaseType.BLAZECASE));
		} else if (DungeonUtil.checkIceDungeon(event.getEntity().getLocation())
				&& ElementalsUtil.hasTag(event.getEntity(), "ice_monster")) {
			if (ElementalsUtil.nextInt(250) == 1) {
				event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
						CaseUtil.getCaseKey(CaseType.ENDERCASE));
				return;
			} else if (ElementalsUtil.nextInt(185) == 1) {
				event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
						CaseUtil.getCase(CaseType.ENDERCASE));
				return;
			}
		} else {
			if (ElementalsUtil.nextInt(700) == 1)
				event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
						CaseUtil.getCaseKey(CaseType.BLAZECASE));
			else if (ElementalsUtil.nextInt(550) == 1)
				event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
						CaseUtil.getCase(CaseType.BLAZECASE));
			else if (ElementalsUtil.nextInt(700) == 1)
				event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
						CaseUtil.getCaseKey(CaseType.ENDERCASE));
			else if (ElementalsUtil.nextInt(550) == 1)
				event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
						CaseUtil.getCase(CaseType.ENDERCASE));
			else if (ElementalsUtil.nextInt(700) == 1)
				event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
						CaseUtil.getCaseKey(CaseType.DRAGONCASE));
			else if (ElementalsUtil.nextInt(550) == 1)
				event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
						CaseUtil.getCase(CaseType.DRAGONCASE));
		}
	}

	@EventHandler
	public void event(InventoryClickEvent event) {
		if (event.getInventory() == null)
			return;
		CaseType type = null;
		switch (event.getInventory().getName()) {
		case "§9§ki§r §6Blaze Case §9§ki":
			type = CaseType.BLAZECASE;
			break;
		case "§9§ki§r §5Ender Case §9§ki":
			type = CaseType.ENDERCASE;
			break;
		case "§9§ki§r §0Dragon Case §9§ki":
			type = CaseType.DRAGONCASE;
			break;
		case "§9§kii§r §5Ender Case §9§kii":
		case "§9§kii§r §6Blaze Case §9§kii":
		case "§9§kii§r §2Crate Chest §9§kii":
		case "§9§kii§r §0Dragon Case §9§kii":
		case "§aRealizari":
			event.setResult(Result.DENY);
			return;
		default:
			return;
		}
		switch (event.getRawSlot()) {
		case 13:
			return;
		case 26:
			event.setResult(Result.DENY);
			if (event.getInventory().getItem(13) == null)
				break;
			if (!EnchantUtil.isSimilar(event.getInventory().getItem(13), CaseUtil.getCaseKey(type)))
				break;
			if (event.getWhoClicked().getInventory().getItemInMainHand() == null)
				break;
			if (!EnchantUtil.isSimilar(event.getWhoClicked().getInventory().getItemInMainHand(),
					CaseUtil.getCase(type)))
				break;
			if (event.getInventory().getItem(13).getAmount() > 1) {
				ItemStack keys = event.getInventory().getItem(13).clone();
				keys.setAmount(keys.getAmount() - 1);
				event.getWhoClicked().getWorld().dropItem(event.getWhoClicked().getLocation(), keys);
			}
			if (event.getWhoClicked().getInventory().getItemInMainHand().getAmount() > 1)
				event.getWhoClicked().getInventory().getItemInMainHand()
						.setAmount(event.getWhoClicked().getInventory().getItemInMainHand().getAmount() - 1);
			else
				event.getWhoClicked().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
			CaseUtil.openCase(type, (Player) event.getWhoClicked());
			break;
		default:
			event.setResult(Result.DENY);
			return;
		}
	}

	@EventHandler
	public void event(PlayerInteractEvent event) {
		if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
			return;
		if (!(EnchantUtil.isSimilar(event.getPlayer().getInventory().getItemInMainHand(),
				CaseUtil.getCase(CaseType.BLAZECASE))
				|| EnchantUtil.isSimilar(event.getPlayer().getInventory().getItemInMainHand(),
						CaseUtil.getCase(CaseType.ENDERCASE))
				|| EnchantUtil.isSimilar(event.getPlayer().getInventory().getItemInMainHand(),
						CaseUtil.getCase(CaseType.DRAGONCASE))))
			return;
		event.setUseItemInHand(Result.DENY);
		event.setUseInteractedBlock(Result.DENY);
		if (CaseUtil.isOpeningCase(event.getPlayer())) {
			event.getPlayer().sendMessage("§cNu poti deschide inca §aCase-ul§c!");
			return;
		}
		Inventory inv = null;
		CaseType type = null;
		switch (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()) {
		case "§9§ki§r §6Blaze Case §9§ki":
			inv = Bukkit.createInventory(event.getPlayer(), InventoryType.CHEST, "§9§ki§r §6Blaze Case §9§ki");
			type = CaseType.BLAZECASE;
			break;
		case "§9§ki§r §5Ender Case §9§ki":
			inv = Bukkit.createInventory(event.getPlayer(), InventoryType.CHEST, "§9§ki§r §5Ender Case §9§ki");
			type = CaseType.ENDERCASE;
			break;
		case "§9§ki§r §0Dragon Case §9§ki":
			inv = Bukkit.createInventory(event.getPlayer(), InventoryType.CHEST, "§9§ki§r §0Dragon Case §9§ki");
			type = CaseType.ENDERCASE;
			break;
		default:
			return;
		}
		CaseUtil.setNullItems(type, inv, false);
		ItemStack item = CaseUtil.getNullItem(DyeColor.LIME);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§c[§aDeschide§c]");
		List<String> lore = Lists.newArrayList();
		lore.add("§eClick pentru a deschide case-ul!");
		lore.add("§cTrebuie sa pui un " + CaseUtil.getCaseKey(type).getItemMeta().getDisplayName()
				+ "§c in spatiul liber!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(26, item);
		event.getPlayer().openInventory(inv);
	}
}
