package ro.nicuch.elementals.casebox;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import ro.nicuch.elementals.Elementals;
import ro.nicuch.elementals.elementals.ElementalsUtil;
import ro.nicuch.elementals.enchants.EnchantUtil;
import ro.nicuch.elementals.enchants.EnchantUtil.CEnchantType;

public class CaseUtil {
	public static enum CaseType {
		BLAZECASE, ENDERCASE, CRATE, DRAGONCASE;
	}

	private static final List<UUID> listCase = Lists.newArrayList();

	public static void changeItemsToNext(CaseType type, Player player, Inventory inv, long time) {
		switch (type) {
		case DRAGONCASE:
			Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> {
				ItemStack i0 = inv.getItem(2).clone();
				ItemStack i1 = inv.getItem(3).clone();
				ItemStack i2 = inv.getItem(4).clone();
				ItemStack i3 = inv.getItem(5).clone();
				ItemStack i4 = inv.getItem(6).clone();
				ItemStack i5 = inv.getItem(15).clone();
				ItemStack i6 = inv.getItem(24).clone();
				ItemStack i7 = inv.getItem(23).clone();
				ItemStack i8 = inv.getItem(22).clone();
				ItemStack i9 = inv.getItem(21).clone();
				ItemStack i10 = inv.getItem(20).clone();
				ItemStack i11 = inv.getItem(11).clone();
				player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_BLAST, 1f, 1f);
				inv.setItem(2, i11);
				inv.setItem(3, i0);
				inv.setItem(4, i1);
				inv.setItem(5, i2);
				inv.setItem(6, i3);
				inv.setItem(15, i4);
				inv.setItem(24, i5);
				inv.setItem(23, i6);
				inv.setItem(22, i7);
				inv.setItem(21, i8);
				inv.setItem(20, i9);
				inv.setItem(11, i10);
			}, time);
			break;
		default:
			Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> {
				ItemStack i0 = inv.getItem(9).clone();
				ItemStack i1 = inv.getItem(10).clone();
				ItemStack i2 = inv.getItem(11).clone();
				ItemStack i3 = inv.getItem(12).clone();
				ItemStack i4 = inv.getItem(13).clone();
				ItemStack i5 = inv.getItem(14).clone();
				ItemStack i6 = inv.getItem(15).clone();
				ItemStack i7 = inv.getItem(16).clone();
				ItemStack next = nextItem(type);
				player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_BLAST, 1f, 1f);
				inv.setItem(9, next);
				inv.setItem(10, i0);
				inv.setItem(11, i1);
				inv.setItem(12, i2);
				inv.setItem(13, i3);
				inv.setItem(14, i4);
				inv.setItem(15, i5);
				inv.setItem(16, i6);
				inv.setItem(17, i7);
			}, time);
			break;
		}
	}

	public static ItemStack getCase(CaseType type) {
		ItemStack case$ = new ItemStack(Material.AIR);
		ItemMeta meta = case$.getItemMeta();
		switch (type) {
		case BLAZECASE:
			case$ = new ItemStack(Material.CHEST, 1);
			meta = case$.getItemMeta();
			meta.setDisplayName("§9§ki§r §6Blaze Case §9§ki");
			break;
		case ENDERCASE:
			case$ = new ItemStack(Material.CHEST, 1);
			meta = case$.getItemMeta();
			meta.setDisplayName("§9§ki§r §5Ender Case §9§ki");
			break;
		case DRAGONCASE:
			case$ = new ItemStack(Material.CHEST, 1);
			meta = case$.getItemMeta();
			meta.setDisplayName("§9§ki§r §0Dragon Case §9§ki");
			break;
		case CRATE:
			break;
		default:
			break;
		}
		case$.setItemMeta(meta);
		return case$;
	}

	// TODO change this in 1.13
	@SuppressWarnings("deprecation")
	public static ItemStack getCaseKey(CaseType type) {
		ItemStack key = new ItemStack(Material.AIR);
		ItemMeta meta = key.getItemMeta();
		switch (type) {
		case BLAZECASE:
			key = new ItemStack(Material.INK_SACK, 1, (short) 0, (byte) 14);
			meta = key.getItemMeta();
			meta.setDisplayName("§6Blaze Case Key");
			break;
		case ENDERCASE:
			key = new ItemStack(Material.INK_SACK, 1, (short) 0, (byte) 5);
			meta = key.getItemMeta();
			meta.setDisplayName("§5Ender Case Key");
			break;
		case DRAGONCASE:
			key = new ItemStack(Material.INK_SACK, 1, (short) 0, (byte) 0);
			meta = key.getItemMeta();
			meta.setDisplayName("§0Dragon Case Key");
			break;
		case CRATE:
			break;
		default:
			break;
		}
		key.setItemMeta(meta);
		return key;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getNullItem(DyeColor color) {
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, color.getDyeData());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§r");
		item.setItemMeta(meta);
		return item;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getNullItem(DyeColor color, String name) {
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, color.getDyeData());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}

	public static void setOpeningCase(Player player) {
		if (!isOpeningCase(player))
			listCase.add(player.getUniqueId());
	}

	public static boolean isOpeningCase(Player player) {
		return listCase.contains(player.getUniqueId());
	}

	public static void removeOpeningCase(Player player) {
		listCase.remove(player.getUniqueId());
	}

	@SuppressWarnings("deprecation")
	public static ItemStack nextItem(CaseType type) {
		ItemStack next = new ItemStack(Material.AIR);
		ItemMeta meta = next.getItemMeta();
		double sansa = ElementalsUtil.nextDouble(100);
		switch (type) {
		case BLAZECASE:
			if (sansa < 1)
				next = getCaseKey(CaseType.BLAZECASE);
			else if (sansa >= 1 && sansa < 2)
				next = getCase(CaseType.BLAZECASE);
			else if (sansa >= 2 && sansa < 7)
				next = new ItemStack(Material.NETHER_STAR);
			else if (sansa >= 7 && sansa < 25)
				next = new ItemStack(Material.SKULL_ITEM, 1, (short) 0, (byte) 1);
			else if (sansa >= 25 && sansa < 67.5) {
				next = new ItemStack(Material.DIAMOND, 5);
				meta = next.getItemMeta();
				meta.setDisplayName("§bDiamond §aof §6Pikachu");
			} else {
				next = new ItemStack(Material.DIAMOND, 10);
				meta = next.getItemMeta();
				meta.setDisplayName("§bDiamond §aof §6Pikachu");
			}
			next.setItemMeta(meta);
			break;
		case ENDERCASE:
			next = EnchantUtil.getBook(CEnchantType.getEnchantsForEnderCase()
					.get(ElementalsUtil.nextInt(CEnchantType.getEnchantsForEnderCase().size())));
			break;
		case CRATE:
			if (sansa < 7)
				next = new ItemStack(Material.NETHER_STAR);
			else if (sansa >= 7 && sansa < 25)
				next = new ItemStack(Material.SKULL_ITEM, 1, (short) 0, (byte) 1);
			else if (sansa >= 25 && sansa < 67.5) {
				next = new ItemStack(Material.DIAMOND, 5);
				meta = next.getItemMeta();
				meta.setDisplayName("§bDiamond §aof §6Pikachu");
			} else {
				next = new ItemStack(Material.DIAMOND, 10);
				meta = next.getItemMeta();
				meta.setDisplayName("§bDiamond §aof §6Pikachu");
			}
			next.setItemMeta(meta);
			break;
		case DRAGONCASE:
			if (sansa < 1)
				next = getCaseKey(CaseType.DRAGONCASE);
			else if (sansa >= 1 && sansa < 2)
				next = getCase(CaseType.DRAGONCASE);
			else if (sansa >= 2 && sansa < 9) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§dCartea Fermierului");
			} else if (sansa >= 9 && sansa < 16) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§aCartea Copacilor");
			} else if (sansa >= 16 && sansa < 23) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§eCartea Stralucirii");
			} else if (sansa >= 23 && sansa < 30) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§eCartea Confuziei");
			} else if (sansa >= 30 && sansa < 37) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§fCartea Levitatiei");
			} else if (sansa >= 37 && sansa < 44) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§9Cartea Impingerii");
			} else if (sansa >= 44 && sansa < 48) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§6Cartea Schimbarii");
			} else if (sansa >= 48 && sansa < 55) {
				next = new ItemStack(Material.ENCHANTED_BOOK, 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§eCartea Fulgerelor");
			} else if (sansa >= 55 && sansa < 60) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§7Cartea Furisarii");
			} else if (sansa >= 60 && sansa < 64) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§bCartea Transformarii §f(§aCreeper§f)");
			} else if (sansa >= 64 && sansa < 68) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§bCartea Transformarii §f(§2Zombie§f)");
			} else if (sansa >= 68 && sansa < 72) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§bCartea Transformarii §f(§7Skeleton§f)");
			} else if (sansa >= 72 && sansa < 76) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§bCartea Transformarii §f(§4Spider§f)");
			} else if (sansa >= 76 && sansa < 80) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§bCartea Transformarii §f(§5Witch§f)");
			} else if (sansa >= 80 && sansa < 84) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§bCartea Transformarii §f(§8Enderman§f)");
			} else if (sansa >= 84 && sansa < 88) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§bCartea Transformarii §f(§cPigman§f)");
			} else if (sansa >= 88 && sansa < 92) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§bCartea Transformarii §f(§eHusk§f)");
			} else if (sansa >= 92 && sansa < 96) {
				next = new ItemStack(Material.ENCHANTED_BOOK, ElementalsUtil.nextInt(2) + 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§bCartea Transformarii §f(§9Stray§f)");
			} else if (sansa >= 96) {
				next = new ItemStack(Material.ENCHANTED_BOOK, 1);
				meta = next.getItemMeta();
				meta.setDisplayName("§cCartea Vindecarii");
			}
			next.setItemMeta(meta);
			break;
		default:
			break;
		}
		return next;
	}

	public static void openCase(CaseType type, Player player) {
		setOpeningCase(player);
		switch (type) {
		case BLAZECASE:
			Inventory inv1 = Bukkit.createInventory(player, InventoryType.CHEST, "§9§kii§r §6Blaze Case §9§kii");
			setNullItems(type, inv1, true);
			player.openInventory(inv1);
			for (int i = 0; i < 50; i++)
				changeItemsToNext(type, player, inv1, i * 4L);
			Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> {
				inv1.setItem(4, getNullItem(DyeColor.RED, "§c\\/"));
				inv1.setItem(22, getNullItem(DyeColor.RED, "§c/\\"));
			}, 200L);
			Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> {
				player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1f, 1f);
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
				player.getInventory().addItem(inv1.getItem(13).clone());
				player.closeInventory();
				removeOpeningCase(player);
			}, 230L);
			break;
		case ENDERCASE:
			Inventory inv2 = Bukkit.createInventory(player, InventoryType.CHEST, "§9§kii§r §5Ender Case §9§kii");
			setNullItems(type, inv2, true);
			player.openInventory(inv2);
			for (int i = 0; i < 50; i++)
				changeItemsToNext(type, player, inv2, i * 4L);
			Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> {
				inv2.setItem(4, getNullItem(DyeColor.RED, "§c\\/"));
				inv2.setItem(22, getNullItem(DyeColor.RED, "§c/\\"));
			}, 200L);
			Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> {
				player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1f, 1f);
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
				player.getInventory().addItem(inv2.getItem(13).clone());
				player.closeInventory();
				removeOpeningCase(player);
			}, 230L);
			break;
		case CRATE:
			Inventory inv3 = Bukkit.createInventory(player, InventoryType.CHEST, "§9§kii§r §2Crate Chest §9§kii");
			setNullItems(type, inv3, true);
			player.openInventory(inv3);
			for (int i = 0; i < 50; i++)
				changeItemsToNext(type, player, inv3, i * 4L);
			Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> {
				inv3.setItem(4, getNullItem(DyeColor.RED, "§c\\/"));
				inv3.setItem(22, getNullItem(DyeColor.RED, "§c/\\"));
			}, 200L);
			Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> {
				player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1f, 1f);
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
				player.getInventory().addItem(inv3.getItem(13).clone());
				player.closeInventory();
				removeOpeningCase(player);
			}, 230L);
			break;
		case DRAGONCASE:
			Inventory inv4 = Bukkit.createInventory(player, InventoryType.CHEST, "§9§kii§r §0Dragon Case §9§kii");
			setNullItems(type, inv4, true);
			player.openInventory(inv4);
			for (int i = 0; i < 50; i++)
				changeItemsToNext(type, player, inv4, i * 4L);
			Bukkit.getScheduler().runTaskLater(Elementals.get(),
					() -> inv4.setItem(13, getNullItem(DyeColor.RED, "§c/\\")), 200L);
			Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> {
				player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1f, 1f);
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
				player.getInventory().addItem(inv4.getItem(4).clone());
				player.closeInventory();
				removeOpeningCase(player);
			}, 230L);
			break;
		default:
			break;
		}
	}

	public static void setNullItems(CaseType type, Inventory inv, boolean open) {
		switch (type) {
		case DRAGONCASE:
			inv.setItem(0, getNullItem(DyeColor.GRAY));
			inv.setItem(9, getNullItem(DyeColor.GRAY));
			inv.setItem(18, getNullItem(DyeColor.GRAY));
			inv.setItem(1, getNullItem(DyeColor.GRAY));
			inv.setItem(10, getNullItem(DyeColor.GRAY));
			inv.setItem(19, getNullItem(DyeColor.GRAY));
			inv.setItem(12, getNullItem(DyeColor.GRAY));
			inv.setItem(14, getNullItem(DyeColor.GRAY));
			inv.setItem(7, getNullItem(DyeColor.GRAY));
			inv.setItem(16, getNullItem(DyeColor.GRAY));
			inv.setItem(25, getNullItem(DyeColor.GRAY));
			inv.setItem(8, getNullItem(DyeColor.GRAY));
			inv.setItem(17, getNullItem(DyeColor.GRAY));
			inv.setItem(26, getNullItem(DyeColor.GRAY));
			if (open) {
				inv.setItem(2, nextItem(type));
				inv.setItem(11, nextItem(type));
				inv.setItem(20, nextItem(type));
				inv.setItem(3, nextItem(type));
				inv.setItem(21, nextItem(type));
				inv.setItem(4, nextItem(type));
				inv.setItem(22, nextItem(type));
				inv.setItem(5, nextItem(type));
				inv.setItem(23, nextItem(type));
				inv.setItem(6, nextItem(type));
				inv.setItem(15, nextItem(type));
				inv.setItem(24, nextItem(type));
				inv.setItem(13, getNullItem(DyeColor.GREEN, "§a/\\"));
			} else {
				inv.setItem(2, nextItem(type));
				inv.setItem(11, nextItem(type));
				inv.setItem(20, nextItem(type));
				inv.setItem(3, nextItem(type));
				inv.setItem(21, nextItem(type));
				inv.setItem(4, nextItem(type));
				inv.setItem(22, nextItem(type));
				inv.setItem(5, nextItem(type));
				inv.setItem(23, nextItem(type));
				inv.setItem(6, nextItem(type));
				inv.setItem(15, nextItem(type));
				inv.setItem(24, nextItem(type));
			}
			break;
		case BLAZECASE:
		case CRATE:
		case ENDERCASE:
			inv.setItem(0, getNullItem(DyeColor.GRAY));
			inv.setItem(1, getNullItem(DyeColor.GRAY));
			inv.setItem(2, getNullItem(DyeColor.GRAY));
			inv.setItem(3, getNullItem(DyeColor.GRAY));
			inv.setItem(5, getNullItem(DyeColor.GRAY));
			inv.setItem(6, getNullItem(DyeColor.GRAY));
			inv.setItem(7, getNullItem(DyeColor.GRAY));
			inv.setItem(8, getNullItem(DyeColor.GRAY));
			inv.setItem(18, getNullItem(DyeColor.GRAY));
			inv.setItem(19, getNullItem(DyeColor.GRAY));
			inv.setItem(20, getNullItem(DyeColor.GRAY));
			inv.setItem(21, getNullItem(DyeColor.GRAY));
			inv.setItem(23, getNullItem(DyeColor.GRAY));
			inv.setItem(24, getNullItem(DyeColor.GRAY));
			inv.setItem(25, getNullItem(DyeColor.GRAY));
			inv.setItem(26, getNullItem(DyeColor.GRAY));
			if (open) {
				inv.setItem(4, getNullItem(DyeColor.GREEN, "§a\\/"));
				inv.setItem(22, getNullItem(DyeColor.GREEN, "§a/\\"));
				inv.setItem(9, nextItem(type));
				inv.setItem(10, nextItem(type));
				inv.setItem(11, nextItem(type));
				inv.setItem(12, nextItem(type));
				inv.setItem(13, nextItem(type));
				inv.setItem(14, nextItem(type));
				inv.setItem(15, nextItem(type));
				inv.setItem(16, nextItem(type));
				inv.setItem(17, nextItem(type));
			} else {
				inv.setItem(22, getNullItem(DyeColor.GRAY));
				inv.setItem(4, getNullItem(DyeColor.GRAY));
				inv.setItem(9, getNullItem(DyeColor.GRAY));
				inv.setItem(10, getNullItem(DyeColor.GRAY));
				inv.setItem(11, getNullItem(DyeColor.GRAY));
				inv.setItem(12, getNullItem(DyeColor.GRAY));
				inv.setItem(14, getNullItem(DyeColor.GRAY));
				inv.setItem(15, getNullItem(DyeColor.GRAY));
				inv.setItem(16, getNullItem(DyeColor.GRAY));
				inv.setItem(17, getNullItem(DyeColor.GRAY));
			}
			break;
		default:
			break;
		}
	}
}
