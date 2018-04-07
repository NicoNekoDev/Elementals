package ro.nicuch.elementals.dungeon;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.gmail.nossr50.events.fake.FakeBlockBreakEvent;
import com.google.common.collect.Lists;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import ro.nicuch.elementals.Elementals;
import ro.nicuch.elementals.User;
import ro.nicuch.elementals.elementals.ElementalsUtil;

public class DungeonListener implements Listener {

	@EventHandler
	public void event(EntityChangeBlockEvent event) {
		if (!event.getEntity().getType().equals(EntityType.ENDER_DRAGON))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event0(EntityDeathEvent event) {
		if (CitizensAPI.getNPCRegistry().isNPC(event.getEntity())) {
			NPC npc = CitizensAPI.getNPCRegistry().getNPC(event.getEntity());
			ItemStack item = new ItemStack(Material.IRON_SWORD);
			ItemMeta meta = item.getItemMeta();
			if (npc.getName().equals("§9Ice Herobrine"))
				meta.setDisplayName("§1Ice King Sword");
			else if (npc.getName().equals("§4Lava Herobrine"))
				meta.setDisplayName("§4Lava King Sword");
			else
				return;
			item.setItemMeta(meta);
			event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), item);
		} else if (event.getEntity().getType().equals(EntityType.ENDER_DRAGON)) {
			if (event.getEntity().getKiller() == null)
				return;
			Player player = event.getEntity().getKiller();
			ItemStack item = new ItemStack(Material.IRON_SWORD);
			ItemStack egg = new ItemStack(Material.DRAGON_EGG);
			ItemMeta meta = item.getItemMeta();
			ItemMeta egg_meta = egg.getItemMeta();
			egg_meta.setDisplayName("§5Ou de Dragon");
			egg_meta.setLore(Lists.newArrayList("§aObtinut: §6" + ElementalsUtil.getCurrentDate()));
			meta.setDisplayName("§5End King Sword");
			item.setItemMeta(meta);
			egg.setItemMeta(egg_meta);
			player.getWorld().dropItem(player.getLocation(), item);
			player.getWorld().dropItem(player.getLocation(), egg);
			Bukkit.broadcastMessage("§5Dragonul §ca fost omorat de " + player.getDisplayName() + "§c!");
		}
	}

	@EventHandler
	public void event(FakeBlockBreakEvent event) {
		User user = Elementals.getUser(event.getPlayer());
		if (!DungeonUtil.checkEndDungeon(event.getBlock().getLocation()))
			return;
		if (user.hasPermission("elementals.admin"))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event(BlockBreakEvent event) {
		User user = Elementals.getUser(event.getPlayer());
		if (!DungeonUtil.checkEndDungeon(event.getBlock().getLocation()))
			return;
		if (user.hasPermission("elementals.admin"))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event(BlockPlaceEvent event) {
		User user = Elementals.getUser(event.getPlayer());
		if (!DungeonUtil.checkEndDungeon(event.getBlock().getLocation()))
			return;
		if (user.hasPermission("elementals.admin"))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event(CreatureSpawnEvent event) {
		if (CitizensAPI.getNPCRegistry().isNPC(event.getEntity()))
			return;
		if (DungeonUtil.checkIceDungeon(event.getLocation())) {
			if (!event.getEntity().getType().equals(EntityType.ZOMBIE)) {
				event.setCancelled(true);
				return;
			}
			Zombie zombie = (Zombie) event.getEntity();
			zombie.setCustomName("§1Zombie de Gheata");
			zombie.setCustomNameVisible(true);
			zombie.setCanPickupItems(false);
			zombie.getEquipment().setHelmetDropChance(0);
			zombie.getEquipment().setChestplateDropChance(0);
			zombie.getEquipment().setLeggingsDropChance(0);
			zombie.getEquipment().setBootsDropChance(0);
			zombie.getEquipment().setItemInMainHandDropChance(0);
			ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
			ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
			ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
			ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
			ItemStack sword = new ItemStack(Material.IRON_SWORD);
			LeatherArmorMeta helmet$meta = (LeatherArmorMeta) helmet.getItemMeta();
			helmet$meta.setColor(Color.BLUE);
			helmet.setItemMeta(helmet$meta);
			helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			LeatherArmorMeta chestplate$meta = (LeatherArmorMeta) chestplate.getItemMeta();
			chestplate$meta.setColor(Color.BLUE);
			chestplate.setItemMeta(chestplate$meta);
			chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			LeatherArmorMeta leggings$meta = (LeatherArmorMeta) leggings.getItemMeta();
			leggings$meta.setColor(Color.BLUE);
			leggings.setItemMeta(leggings$meta);
			leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			LeatherArmorMeta boots$meta = (LeatherArmorMeta) boots.getItemMeta();
			boots$meta.setColor(Color.BLUE);
			boots.setItemMeta(helmet$meta);
			boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
			zombie.getEquipment().setHelmet(helmet);
			zombie.getEquipment().setChestplate(chestplate);
			zombie.getEquipment().setLeggings(leggings);
			zombie.getEquipment().setBoots(boots);
			zombie.getEquipment().setItemInMainHand(sword);
			ElementalsUtil.setTag(zombie, "ice_monster");
		} else if (DungeonUtil.checkLavaDungeon(event.getLocation())) {
			if (!event.getEntity().getType().equals(EntityType.PIG_ZOMBIE)) {
				event.setCancelled(true);
				return;
			}
			PigZombie zombie = (PigZombie) event.getEntity();
			zombie.setAngry(true);
			zombie.setCustomName("§4Zombie de Lava");
			zombie.setCustomNameVisible(true);
			zombie.setCanPickupItems(false);
			zombie.getEquipment().setHelmetDropChance(0);
			zombie.getEquipment().setChestplateDropChance(0);
			zombie.getEquipment().setLeggingsDropChance(0);
			zombie.getEquipment().setBootsDropChance(0);
			zombie.getEquipment().setItemInMainHandDropChance(0);
			ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
			ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
			ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
			ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
			ItemStack sword = new ItemStack(Material.IRON_SWORD);
			LeatherArmorMeta helmet$meta = (LeatherArmorMeta) helmet.getItemMeta();
			helmet$meta.setColor(Color.RED);
			helmet.setItemMeta(helmet$meta);
			helmet.addEnchantment(Enchantment.PROTECTION_FIRE, 2);
			LeatherArmorMeta chestplate$meta = (LeatherArmorMeta) chestplate.getItemMeta();
			chestplate$meta.setColor(Color.RED);
			chestplate.setItemMeta(chestplate$meta);
			chestplate.addEnchantment(Enchantment.PROTECTION_FIRE, 2);
			LeatherArmorMeta leggings$meta = (LeatherArmorMeta) leggings.getItemMeta();
			leggings$meta.setColor(Color.RED);
			leggings.setItemMeta(leggings$meta);
			leggings.addEnchantment(Enchantment.PROTECTION_FIRE, 2);
			LeatherArmorMeta boots$meta = (LeatherArmorMeta) boots.getItemMeta();
			boots$meta.setColor(Color.RED);
			boots.setItemMeta(helmet$meta);
			boots.addEnchantment(Enchantment.PROTECTION_FIRE, 2);
			sword.addEnchantment(Enchantment.KNOCKBACK, 1);
			zombie.getEquipment().setHelmet(helmet);
			zombie.getEquipment().setChestplate(chestplate);
			zombie.getEquipment().setLeggings(leggings);
			zombie.getEquipment().setBoots(boots);
			zombie.getEquipment().setItemInMainHand(sword);
			ElementalsUtil.setTag(zombie, "lava_monster");
		} else if (DungeonUtil.checkEndDungeon(event.getLocation())) {
			if (!event.getEntity().getType().equals(EntityType.ENDERMAN)) {
				if (!(event.getEntity().getType().equals(EntityType.ENDER_DRAGON)
						|| event.getEntity().getType().equals(EntityType.ENDER_CRYSTAL)
						|| event.getEntity().getType().equals(EntityType.ENDERMITE)
						|| event.getEntity().getType().equals(EntityType.SHULKER)))
					event.setCancelled(true);
				return;
			}
			Enderman enderman = (Enderman) event.getEntity();
			enderman.setCustomName("§5Enderman de End");
			enderman.setCustomNameVisible(true);
			ElementalsUtil.setTag(enderman, "end_monster");
		}
	}

	@EventHandler
	public void event(PlayerMoveEvent event) {
		if (event.getFrom().getBlock().equals(event.getTo().getBlock()))
			return;
		if (CitizensAPI.getNPCRegistry().isNPC(event.getPlayer()))
			return;
		if (!DungeonUtil.checkEndDungeonPart(event.getTo()))
			return;
		if (!(event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)
				|| event.getPlayer().getGameMode().equals(GameMode.ADVENTURE)))
			return;
		for (Entity entity : event.getPlayer().getNearbyEntities(8, 8, 8))
			if (entity.getType().equals(EntityType.ENDERMAN))
				if (ElementalsUtil.hasTag(entity, "end_monster"))
					if (((Enderman) entity).getTarget() == null)
						((Enderman) entity).setTarget(event.getPlayer());
	}

	@EventHandler
	public void event(EntityDamageEvent event) {
		if (CitizensAPI.getNPCRegistry().isNPC(event.getEntity()))
			return;
		if (!event.getEntity().getType().equals(EntityType.PLAYER))
			return;
		if (!DungeonUtil.dungeonProtection(event.getEntity().getLocation()))
			return;
		event.setCancelled(true);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void event(EntityDeathEvent event) {
		if (event.getEntity().getKiller() == null)
			return;
		if (!event.getEntity().getKiller().getType().equals(EntityType.PLAYER))
			return;
		if (!event.getEntity().getKiller().getInventory().getItemInMainHand().hasItemMeta())
			return;
		if (!event.getEntity().getKiller().getInventory().getItemInMainHand().getItemMeta().hasDisplayName())
			return;
		int n = ElementalsUtil.nextInt(3);
		ItemStack drop = new ItemStack(Material.AIR);
		ItemMeta meta = drop.getItemMeta();
		if (DungeonUtil.checkIceDungeon(event.getEntity().getLocation())) {
			if (event.getEntity().getType().equals(EntityType.ZOMBIE)
					&& ElementalsUtil.hasTag(event.getEntity(), "ice_monster")) {
				if (!event.getEntity().getKiller().getInventory().getItemInMainHand().getItemMeta().getDisplayName()
						.equals("§1Ice King Sword"))
					return;
				drop = new ItemStack(Material.INK_SACK, n, (short) 0, (byte) 4);
				meta = drop.getItemMeta();
				meta.setDisplayName("§9§lCub de Gheata");
			}
		} else if (DungeonUtil.checkLavaDungeon(event.getEntity().getLocation())) {
			if (event.getEntity().getType().equals(EntityType.PIG_ZOMBIE)
					&& ElementalsUtil.hasTag(event.getEntity(), "lava_monster")) {
				if (!event.getEntity().getKiller().getInventory().getItemInMainHand().getItemMeta().getDisplayName()
						.equals("§4Lava King Sword"))
					return;
				drop = new ItemStack(Material.BLAZE_POWDER, n);
				meta = drop.getItemMeta();
				meta.setDisplayName("§4§lCoama de Foc");
			}
		} else if (DungeonUtil.checkEndDungeon(event.getEntity().getLocation())) {
			if (event.getEntity().getType().equals(EntityType.ENDERMAN)
					&& ElementalsUtil.hasTag(event.getEntity(), "end_monster")) {
				if (!event.getEntity().getKiller().getInventory().getItemInMainHand().getItemMeta().getDisplayName()
						.equals("§5End King Sword"))
					return;
				drop = new ItemStack(Material.CHORUS_FRUIT, n);
				meta = drop.getItemMeta();
				meta.setDisplayName("§5§lFloare de Void");
			}
		}
		drop.setItemMeta(meta);
		if (drop.getType().equals(Material.AIR))
			return;
		event.getDrops().add(drop);
	}
}
