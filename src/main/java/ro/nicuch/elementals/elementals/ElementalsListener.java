package ro.nicuch.elementals.elementals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.bukkit.BanEntry;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import com.gmail.nossr50.events.experience.McMMOPlayerLevelDownEvent;
import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
import com.gmail.nossr50.events.fake.FakeBlockBreakEvent;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.vexsoftware.votifier.model.VotifierEvent;

import fr.xephi.authme.events.LoginEvent;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.citizensnpcs.api.CitizensAPI;
import ro.nicuch.elementals.Elementals;
import ro.nicuch.elementals.User;
import ro.nicuch.elementals.casebox.CaseUtil;
import ro.nicuch.elementals.casebox.CaseUtil.CaseType;
import ro.nicuch.elementals.dungeon.DungeonUtil;
import ro.nicuch.elementals.elementals.ElementalsUtil.NanoType;
import ro.nicuch.elementals.item.ItemUtil;
import ro.nicuch.elementals.protection.Field;
import ro.nicuch.elementals.protection.FieldUtil;
import ro.nicuch.elementals.worldguard_imp.WGConfigLoc;

public class ElementalsListener implements Listener {
	private final static List<UUID> interactList = Lists.newArrayList();

	@EventHandler
	public void event(PlayerFishEvent event) {
		if (CitizensAPI.getNPCRegistry().isNPC(event.getPlayer()))
			return;
		ItemStack handItem = event.getPlayer().getInventory().getItemInMainHand();
		if (!handItem.hasItemMeta())
			return;
		ItemMeta meta = handItem.getItemMeta();
		if (!meta.hasDisplayName())
			return;
		String displayName = meta.getDisplayName();
		if (!displayName.equals("§bGrappling Hook"))
			return;
		if (!event.getState().equals(State.IN_GROUND))
			return;
		Player player = event.getPlayer();
		User user = Elementals.getUser(player);
		if (player.getWorld().getName().equals("spawn") && (!user.hasPermission("elementals.admin")))
			return;
		Location l1 = player.getLocation();
		Location l2 = event.getHook().getLocation();
		double g = -0.08D;
		double d = l2.distance(l1);
		double vX = (1.0D + 0.07D * d) * (l2.getX() - l1.getX()) / d;
		double vY = (1.0D + 0.03D * d) * (l2.getY() - l1.getY()) / d - 0.5D * g * d;
		double vZ = (1.0D + 0.07D * d) * (l2.getZ() - l1.getZ()) / d;
		Vector vec = new Vector(vX, vY, vZ);
		event.getPlayer().setVelocity(vec);
	}

	@EventHandler
	public void event0(McMMOPlayerLevelUpEvent event) {
		OverloadedWorldHolder wh = Elementals.getGM().getWorldsHolder().getDefaultWorld();
		String playerName = event.getPlayer().getName();
		String group = wh.getUser(playerName).getGroupName();
		if (!(group.equals("Incepator") || group.equals("Avansat") || group.equals("Expert") || group.equals("Pro")))
			return;
		User user = Elementals.getUser(event.getPlayer());
		int level = user.getServerLevel();
		if (level < 25) {
			if (!group.equals("Incepator"))
				wh.getUser(playerName).setGroup(wh.getGroup("Incepator"));
		} else if (level >= 25 && level < 50) {
			if (!group.equals("Avansat"))
				wh.getUser(playerName).setGroup(wh.getGroup("Avansat"));
		} else if (level >= 50 && level < 75) {
			if (!group.equals("Expert"))
				wh.getUser(playerName).setGroup(wh.getGroup("Expert"));
		} else if (level >= 75) {
			if (!group.equals("Pro"))
				wh.getUser(playerName).setGroup(wh.getGroup("Pro"));
		}
	}

	@EventHandler
	public void event(McMMOPlayerLevelDownEvent event) {
		OverloadedWorldHolder wh = Elementals.getGM().getWorldsHolder().getDefaultWorld();
		String playerName = event.getPlayer().getName();
		String group = wh.getUser(playerName).getGroupName();
		if (!(group.equals("Incepator") || group.equals("Avansat") || group.equals("Expert") || group.equals("Pro")))
			return;
		User user = Elementals.getUser(event.getPlayer());
		int level = user.getServerLevel();
		if (level < 25) {
			if (!group.equals("Incepator"))
				wh.getUser(playerName).setGroup(wh.getGroup("Incepator"));
		} else if (level >= 25 && level < 50) {
			if (!group.equals("Avansat"))
				wh.getUser(playerName).setGroup(wh.getGroup("Avansat"));
		} else if (level >= 50 && level < 75) {
			if (!group.equals("Expert"))
				wh.getUser(playerName).setGroup(wh.getGroup("Expert"));
		} else if (level >= 75) {
			if (!group.equals("Pro"))
				wh.getUser(playerName).setGroup(wh.getGroup("Pro"));
		}
	}

	@EventHandler
	public void event(EntityShootBowEvent event) {
		LivingEntity entity = event.getEntity();
		if (!entity.getType().equals(EntityType.PLAYER))
			return;
		ItemStack bow = event.getBow();
		if (!bow.hasItemMeta())
			return;
		ItemMeta meta = bow.getItemMeta();
		if (!meta.hasDisplayName())
			return;
		String displayName = meta.getDisplayName();
		if (!displayName.equals("§bNaNo Arc"))
			return;
		Projectile proj = (Projectile) event.getProjectile();
		Vector vely = proj.getVelocity().clone();
		int fireTick = proj.getFireTicks();
		Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> {
			Projectile proj1 = ((ProjectileSource) entity).launchProjectile(Arrow.class);
			proj1.setVelocity(vely);
			proj1.setFireTicks(fireTick);
			NanoBowShotEvent a = new NanoBowShotEvent(entity, bow, proj1);
			Bukkit.getPluginManager().callEvent(a);
			if (a.isCancelled())
				proj1.remove();
			if (!a.getProjectile().equals(proj1))
				proj1.remove();
			a.getProjectile().setMetadata("nano", new FixedMetadataValue(Elementals.get(), true));
		}, 5L);
		Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> {
			Projectile proj2 = ((ProjectileSource) entity).launchProjectile(Arrow.class);
			proj2.setVelocity(vely);
			proj2.setFireTicks(fireTick);
			NanoBowShotEvent b = new NanoBowShotEvent(entity, bow, proj2);
			Bukkit.getPluginManager().callEvent(b);
			if (b.isCancelled())
				proj2.remove();
			if (!b.getProjectile().equals(proj2))
				proj2.remove();
			b.getProjectile().setMetadata("nano", new FixedMetadataValue(Elementals.get(), true));
		}, 10L);
	}

	@EventHandler
	public void event0(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (!entity.getType().equals(EntityType.PLAYER))
			return;
		Location loc = entity.getLocation();
		if (!FieldUtil.isFieldAtLocation(loc))
			return;
		Field field = FieldUtil.getFieldByLocation(loc);
		UUID uuid = entity.getUniqueId();
		if (field.isMember(uuid) || field.isOwner(uuid))
			return;
		switch (event.getCause()) {
		case DROWNING:
			entity.getLocation().getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
			break;
		case FALLING_BLOCK:
		case FIRE:
		case FIRE_TICK:
		case MAGIC:
			event.setCancelled(true);
			break;
		case SUFFOCATION:
			entity.teleport(
					entity.getWorld().getHighestBlockAt(entity.getLocation()).getRelative(BlockFace.UP).getLocation());
			break;
		default:
			break;
		}
	}

	@EventHandler
	public void event0(PlayerInteractAtEntityEvent event) {
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if (item == null)
			return;
		if (!item.getType().equals(Material.NAME_TAG))
			return;
		ItemMeta meta = item.getItemMeta();
		if (meta == null)
			return;
		if (meta.getDisplayName() == null)
			return;
		if (!ElementalsUtil.isValidEntity(event.getRightClicked(), false))
			return;
		boolean corupted = ElementalsUtil.hasTag(event.getRightClicked(), "corupted");
		int entityCount = ElementalsUtil.getEntityCount(event.getRightClicked());
		if (entityCount > 1) {
			LivingEntity clone = (LivingEntity) event.getRightClicked().getWorld().spawnEntity(
					event.getRightClicked().getLocation().getBlock().getLocation().clone().add(0.5, 0, 0.5),
					event.getRightClicked().getType());
			switch (event.getRightClicked().getType()) {
			case SHEEP:
				Sheep sheep = (Sheep) event.getRightClicked();
				((Sheep) clone).setSheared(sheep.isSheared());
				break;
			default:
				break;
			}
			if (entityCount > 2) {
				int finalCount = entityCount - 1;
				ElementalsUtil.setEntityCount(clone, finalCount);
				clone.setCustomName("§cCount§f: §a" + finalCount);
				clone.setCustomNameVisible(false);
			}
			ElementalsUtil.removeTag(event.getRightClicked(), "count");
			if (corupted && (clone instanceof Monster)) {
				ElementalsUtil.setTag(clone, "corupted");
				ItemUtil.findTarget((Monster) clone);
			}
		}
	}

	@EventHandler
	public void event2(PlayerTeleportEvent event) {
		if (CitizensAPI.getNPCRegistry().isNPC(event.getPlayer()))
			return;
		User user = Elementals.getUser(event.getPlayer());
		if (user.hasPermission("elementals.admin"))
			return;
		if (DungeonUtil.checkEndDungeon(event.getTo())) {
			if (user.getServerLevel() < 75)
				event.setCancelled(true);
		} else if (DungeonUtil.checkLavaDungeon(event.getTo())) {
			if (user.getServerLevel() < 45)
				event.setCancelled(true);
		} else if (DungeonUtil.checkIceDungeon(event.getTo())) {
			if (user.getServerLevel() < 30)
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void event2(PlayerMoveEvent event) {
		if (event.getFrom().getBlock().equals(event.getTo().getBlock()))
			return;
		if (CitizensAPI.getNPCRegistry().isNPC(event.getPlayer()))
			return;
		User user = Elementals.getUser(event.getPlayer());
		if (ElementalsUtil.hasRandomTpCmdDelay(user)) {
			ElementalsUtil.cancelRandomTpCmd(user);
			ElementalsUtil.removeTandomTpCmdDelay(user);
		}
		if (user.hasPermission("elementals.admin"))
			return;
		World spawn = Bukkit.getWorld("spawn");
		Location dStart = new Location(spawn, 979.5, 64.0, -40.5, 180, 0);
		if (event.getTo().getBlockY() >= 255) {
			event.setTo(new Location(spawn, 0.5, 64, 0.5, 90, 0));
			return;
		}
		if (DungeonUtil.checkEndDungeon(event.getTo())) {
			if (user.getServerLevel() < 75)
				event.setTo(dStart);
		} else if (DungeonUtil.checkLavaDungeon(event.getTo())) {
			if (user.getServerLevel() < 45)
				event.setTo(dStart);
		} else if (DungeonUtil.checkIceDungeon(event.getTo())) {
			if (user.getServerLevel() < 30)
				event.setTo(dStart);
		}
	}

	@EventHandler
	public void event0(EntityExplodeEvent event) {
		if (!event.getEntity().getType().equals(EntityType.CREEPER))
			return;
		event.setYield(Math.round(event.getYield()) * ElementalsUtil.getEntityCount(event.getEntity()));
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void event(PlayerShearEntityEvent event) {
		if (!event.getEntity().getType().equals(EntityType.SHEEP))
			return;
		Sheep sheep = (Sheep) event.getEntity();
		int count = ElementalsUtil.getEntityCount(event.getEntity()) - 1;
		for (int n = 0; n < count; n++)
			event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), new ItemStack(Material.WOOL,
					ElementalsUtil.nextInt(2) + 1, (short) 0, sheep.getColor().getWoolData()));
	}

	@EventHandler
	public void event1(CreatureSpawnEvent event) {
		if (DungeonUtil.checkLavaDungeon(event.getLocation()) || DungeonUtil.checkLavaDungeon(event.getLocation())
				|| DungeonUtil.checkIceDungeon(event.getLocation()))
			return;
		if (!event.getSpawnReason().equals(SpawnReason.SPAWNER))
			return;
		ElementalsUtil.setTag(event.getEntity(), "spawner");
	}

	@EventHandler
	public void event0(PlayerCommandPreprocessEvent event) {
		User user = Elementals.getUser(event.getPlayer());
		if (user.isInElytraParkour() && !user.hasPermission("elementals.admin")) {
			user.getBase().sendMessage("§cNu poti folosi comenzi in §aElytra Parkour§c!");
			event.setCancelled(true);
		}
	}

	// TODO Elytra parkour?
	/*
	 * @EventHandler public void event( NPCRightClickEvent event) { if
	 * (event.getNPC().getId() != 2200) return; User user =
	 * Elementals.getUser(event.getClicker()); if (user.isInElytraParkour()) {
	 * user.getBase().sendMessage("§cAi iesit din §aElytra Parkour§c!");
	 * user.toggleElytraParkour(false); } else {
	 * user.getBase().sendMessage("§cAi intrat in §aElytra Parkour§c!");
	 * user.toggleElytraParkour(true); } }
	 */

	@EventHandler
	public void event(CreatureSpawnEvent event) {
		if (!event.getLocation().getWorld().getName().equals("spawn"))
			return;
		if (!(event.getSpawnReason().equals(SpawnReason.TRAP)
				|| event.getSpawnReason().equals(SpawnReason.REINFORCEMENTS)))
			return;
		event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void event2(EntityDeathEvent event) {
		if (!ElementalsUtil.isValidEntity(event.getEntity(), true))
			return;
		boolean corupted = ElementalsUtil.hasTag(event.getEntity(), "corupted");
		int entityCount = ElementalsUtil.getEntityCount(event.getEntity());
		if (entityCount > 1) {
			switch (event.getEntity().getType()) {
			case CREEPER:
				return;
			default:
				break;
			}
			LivingEntity clone = (LivingEntity) event.getEntity().getWorld().spawnEntity(
					event.getEntity().getLocation().getBlock().getLocation().clone().add(0.5, 0, 0.5),
					event.getEntity().getType());
			switch (event.getEntity().getType()) {
			case SHEEP:
				Sheep sheep = (Sheep) event.getEntity();
				((Sheep) clone).setSheared(sheep.isSheared());
				break;
			default:
				break;
			}
			if (entityCount > 2) {
				int finalCount = entityCount - 1;
				ElementalsUtil.setEntityCount(clone, finalCount);
				clone.setCustomName("§cCount§f: §a" + finalCount);
				clone.setCustomNameVisible(false);
			}
			if (corupted && (clone instanceof Monster)) {
				ElementalsUtil.setTag(clone, "corupted");
				ItemUtil.findTarget((Monster) clone);
			}
		}
	}

	// TODO Pachet de resurse?
	// @EventHandler
	public void event(LoginEvent event) {
		event.getPlayer().sendMessage("§b===============================");
		event.getPlayer().sendMessage("§c>>>");
		event.getPlayer().sendMessage("§c>>> §aSe incearca trimiterea pachetului nostru de textura...");
		event.getPlayer().sendMessage("§c>>> §ePuteti sa-l acceptati sau nu, dar acesta este recomandat!");
		event.getPlayer().sendMessage("§c>>>");
		event.getPlayer().sendMessage("§b===============================");
		Bukkit.getScheduler().runTaskLater(Elementals.get(),
				() -> event.getPlayer().setResourcePack("https://www.pikacraftmc.ro/resource/pack.zip"), 65L);
	}

	@EventHandler
	public void event(PlayerResourcePackStatusEvent event) {
		User user = Elementals.getUser(event.getPlayer());
		switch (event.getStatus()) {
		case ACCEPTED:
		case SUCCESSFULLY_LOADED:
			user.toggleResourcePack(true);
			break;
		case DECLINED:
		case FAILED_DOWNLOAD:
			user.toggleResourcePack(false);
			break;
		default:
			break;
		}
	}

	@EventHandler
	public void event1(PlayerTeleportEvent event) {
		if (CitizensAPI.getNPCRegistry().isNPC(event.getPlayer()))
			return;
		if (!(event.getPlayer().getWorld().getName().equals("spawn") || DungeonUtil.checkEndDungeon(event.getTo())))
			return;
		if (!(event.getCause().equals(TeleportCause.CHORUS_FRUIT)
				|| event.getCause().equals(TeleportCause.ENDER_PEARL)))
			return;
		if (DungeonUtil.checkEndDungeonPart(event.getTo()))
			return;
		User user = Elementals.getUser(event.getPlayer());
		if (user.hasPermission("elementals.admin"))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event0(CreatureSpawnEvent event) {
		if (!event.getLocation().getWorld().getName().equals("world"))
			return;
		if (!(event.getEntity().getType().equals(EntityType.ZOMBIE)
				|| event.getEntity().getType().equals(EntityType.SKELETON)))
			return;
		if (event.getSpawnReason().equals(SpawnReason.SPAWNER) || event.getSpawnReason().equals(SpawnReason.CUSTOM))
			return;
		if (event.getEntity().getPassengers() != null)
			return;
		if (!event.getEntity().getPassengers().isEmpty())
			return;
		if (ElementalsUtil.nextInt(50) != 1)
			return;
		EntityEquipment equip = event.getEntity().getEquipment();
		equip.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
		equip.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		equip.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
		equip.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
		equip.setHelmetDropChance(0);
		equip.setChestplateDropChance(0);
		equip.setLeggingsDropChance(0);
		equip.setBootsDropChance(0);
		if (event.getEntity().getType().equals(EntityType.ZOMBIE)) {
			event.getEntity()
					.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2, true, false), true);
			event.getEntity().getEquipment().setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
			event.getEntity().getEquipment().setItemInMainHandDropChance(0);
		}
		PlayerDisguise herobrine = new PlayerDisguise("Herobrine");
		DisguiseAPI.disguiseToAll(event.getEntity(), herobrine);
		ElementalsUtil.setTag(event.getEntity(), "herobrine");
	}

	@EventHandler
	public void event1(EntityDeathEvent event) {
		if (!ElementalsUtil.hasTag(event.getEntity(), "herobrine"))
			return;
		if (ElementalsUtil.nextInt(15) != 1)
			return;
		event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
				new ItemStack(Material.DIAMOND, ElementalsUtil.nextInt(2)));
	}

	@EventHandler
	public void event0(PlayerTeleportEvent event) {
		if (CitizensAPI.getNPCRegistry().isNPC(event.getPlayer()))
			return;
		if (event.getCause().equals(TeleportCause.PLUGIN))
			return;
		User user = Elementals.getUser(event.getPlayer());
		if (user.hasPermission("elementals.admin"))
			return;
		if (event.isCancelled())
			return;
		if (WGConfigLoc.checkMaze(event.getTo())) {
			event.setTo(new Location(event.getTo().getWorld(), -0.5, 55, -146.5));
			event.getPlayer().sendMessage("§cNu ai voie sa te teleportezi in §aLabirint§c!");
		} else if (WGConfigLoc.checkParkour(event.getTo())) {
			event.setTo(new Location(Bukkit.getWorld("spawn"), 212.5, 39, 146.5, 90, 0));
			event.getPlayer().sendMessage("§cNu ai voie sa te teleportezi in §aParkour§c!");
		}
	}

	@EventHandler
	public void event0(PlayerMoveEvent event) {
		if (event.getFrom().getBlock().equals(event.getTo().getBlock()))
			return;
		if (CitizensAPI.getNPCRegistry().isNPC(event.getPlayer()))
			return;
		User user = Elementals.getUser(event.getPlayer());
		Location getToLoc = event.getTo().getBlock().getLocation().clone().add(0.5, 0.5, 0.5);
		if (!user.hasPermission("elementals.gm.toggle"))
			if (user.getBase().getWorld().getName().equals("spawn")) {
				if (WGConfigLoc.checkInMine(getToLoc) || WGConfigLoc.checkMobArena(getToLoc)
						|| WGConfigLoc.checkSpleefArena(getToLoc))
					user.getBase().setGameMode(GameMode.SURVIVAL);
				else
					user.getBase().setGameMode(GameMode.ADVENTURE);
			} else
				user.getBase().setGameMode(GameMode.SURVIVAL);
	}

	@EventHandler
	public void event(PlayerTeleportEvent event) {
		if (CitizensAPI.getNPCRegistry().isNPC(event.getPlayer()))
			return;
		User user = Elementals.getUser(event.getPlayer());
		Location getToLoc = event.getTo().getBlock().getLocation().clone().add(0.5, 0.5, 0.5);
		if (!user.hasPermission("elementals.gm.toggle"))
			if (user.getBase().getWorld().getName().equals("spawn")) {
				if (WGConfigLoc.checkInMine(getToLoc) || WGConfigLoc.checkMobArena(getToLoc)
						|| WGConfigLoc.checkSpleefArena(getToLoc))
					user.getBase().setGameMode(GameMode.SURVIVAL);
				else
					user.getBase().setGameMode(GameMode.ADVENTURE);
			} else
				user.getBase().setGameMode(GameMode.SURVIVAL);
	}

	@EventHandler
	public void event(ServerListPingEvent event) {
		event.setMotd(ElementalsUtil.getMotd());
	}

	@EventHandler
	public void event3(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;
		Material clickedBlockType = event.getClickedBlock().getType();
		if (!(clickedBlockType.equals(Material.DISPENSER) || clickedBlockType.equals(Material.TNT)
				|| clickedBlockType.equals(Material.COMMAND) || clickedBlockType.equals(Material.COMMAND_REPEATING)
				|| clickedBlockType.equals(Material.TRAP_DOOR) || clickedBlockType.equals(Material.COMMAND_CHAIN)
				|| clickedBlockType.equals(Material.DAYLIGHT_DETECTOR)
				|| clickedBlockType.equals(Material.DAYLIGHT_DETECTOR_INVERTED)
				|| clickedBlockType.equals(Material.DIODE_BLOCK_OFF) || clickedBlockType.equals(Material.DIODE_BLOCK_ON)
				|| clickedBlockType.equals(Material.REDSTONE_COMPARATOR_OFF)
				|| clickedBlockType.equals(Material.REDSTONE_COMPARATOR_ON) || clickedBlockType.equals(Material.BEACON)
				|| clickedBlockType.equals(Material.BED_BLOCK)))
			return;
		if (!event.getPlayer().getWorld().getName().equals("spawn"))
			return;
		if (WGConfigLoc.checkMobArena(event.getClickedBlock().getLocation()))
			return;
		User user = Elementals.getUser(event.getPlayer());
		if (user.hasPermission("elementals.admin"))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event0(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;
		User user = Elementals.getUser(event.getPlayer());
		if (ElementalsUtil.hasChatDelay(user) && !user.hasPermission("elementals.chat.override")) {
			event.getPlayer().sendMessage("§cTrebuie sa astepti o secunda pentru a putea vorbi!");
			event.setCancelled(true);
			return;
		}
		if (ElementalsUtil.isChatStopped() && !user.hasPermission("elementals.chat.bypass")) {
			event.getPlayer().sendMessage("§cNu se poate vorbi acum!");
			event.setCancelled(true);
			return;
		}
		ElementalsUtil.delayChatPlayer(user);
		List<String> names = ElementalsUtil.getPlayersNames();
		List<String> recipeNames = Lists.newArrayList();
		List<String> msg = Lists.newArrayList(event.getMessage().split(" "));
		boolean swear = false;
		for (String arg : msg) {
			switch (arg.toLowerCase().replace(".", "").replace("?", "").replace("!", "").replace(")", "")
					.replace("]", "").replace("*", "").replace("-", "").replace("_", "").replace(",", "")
					.replace("'", "").replace("/", "").replace("|", "").replace("(", "").replace("[", "")
					.replace("@", "").replace("#", "").replace("$", "").replace("%", "").replace("^", "")
					.replace("{", "").replace("}", "").replace(";", "").replace(":", "").replace("<", "")
					.replace(">", "").replace("\\", "").replace("~", "").replace("~", "").replace("=", "")
					.replace("+", "")) {
			case "cac":
			case "fut":
			case "fot":
			case "cur":
			case "plm":
			case "pla":
			case "pis":
			case "mue":
			case "coi":
			case "fgm":
			case "fmm":
			case "stf":
				msg.set(msg.indexOf(arg), "***");
				swear = true;
				break;
			case "pula":
			case "muie":
			case "muia":
			case "caca":
			case "mata":
			case "mati":
			case "pola":
			case "pwla":
			case "laba":
			case "fute":
			case "fufa":
			case "puli":
			case "puta":
			case "popo":
			case "fuck":
			case "gaoz":
			case "tate":
			case "suge":
			case "buci":
			case "buca":
			case "sugi":
			case "suga":
			case "dick":
			case "cyka":
			case "suka":
			case "shit":
			case "futi":
			case "futa":
				msg.set(msg.indexOf(arg), "****");
				swear = true;
				break;
			case "futel":
			case "futui":
			case "putza":
			case "pulan":
			case "pizda":
			case "futut":
			case "curva":
			case "curve":
			case "prost":
			case "cacat":
			case "pisat":
			case "pulii":
			case "pizde":
			case "coaie":
			case "muita":
			case "muite":
			case "muist":
			case "matii":
			case "tarfi":
			case "tarfa":
			case "tarfe":
			case "tarfo":
			case "curvo":
			case "curvi":
			case "tatze":
			case "labar":
			case "sugeo":
			case "pussy":
			case "curut":
			case "penis":
			case "dracu":
			case "draci":
			case "idiot":
			case "futea":
			case "kurwa":
			case "futeo":
				msg.set(msg.indexOf(arg), "*****");
				swear = true;
				break;
			case "chizda":
			case "idioti":
			case "stupid":
			case "cretin":
			case "dracii":
			case "ratati":
			case "tarfii":
			case "labari":
			case "curvii":
			case "fututa":
			case "gaoaza":
			case "gaoaze":
			case "lindic":
			case "muista":
			case "muisti":
			case "prosti":
			case "fututi":
			case "sloboz":
			case "curist":
			case "pulele":
			case "futete":
			case "futute":
				msg.set(msg.indexOf(arg), "******");
				swear = true;
				break;
			case "proaste":
			case "proasta":
			case "idioate":
			case "idiotii":
			case "stupizi":
			case "cretini":
			case "curvari":
			case "tarfele":
			case "gaozari":
			case "labarii":
			case "curvism":
			case "sugator":
			case "muistii":
			case "poponar":
			case "labagiu":
			case "fucking":
			case "pizdele":
			case "futemas":
			case "pizdita":
				msg.set(msg.indexOf(arg), "*******");
				swear = true;
				break;
			case "stupizii":
			case "cretinii":
			case "curvarii":
			case "gaozarii":
			case "sugatori":
			case "cacacios":
			case "bulangiu":
			case "retardat":
			case "futeteas":
				msg.set(msg.indexOf(arg), "********");
				swear = true;
				break;
			case "cacaturile":
			case "fofoloanca":
			case "handicapat":
				msg.set(msg.indexOf(arg), "**********");
				swear = true;
				break;
			case "handicapati":
			case "handicapatii":
				msg.set(msg.indexOf(arg), "**********");
				swear = true;
				break;
			default:
				break;
			}
			if (names.contains(arg))
				recipeNames.add(arg);
		}
		names.clear();
		String message = String.join(" ", msg);
		msg.clear();
		StringBuilder builder = new StringBuilder(message);
		if (!(message.endsWith(".") || message.endsWith("?") || message.endsWith("!") || message.endsWith(")")
				|| message.endsWith("]") || message.endsWith(":D") || message.endsWith("xD") || message.endsWith("*")
				|| message.endsWith("-") || message.endsWith("_") || message.endsWith(",") || message.endsWith("'")
				|| message.endsWith("/") || message.endsWith("|") || message.endsWith("(") || message.endsWith("[")
				|| message.endsWith("@") || message.endsWith("#") || message.endsWith("$") || message.endsWith("%")
				|| message.endsWith("^") || message.endsWith("{") || message.endsWith("}") || message.endsWith(";")
				|| message.endsWith(":") || message.endsWith("<") || message.endsWith(">") || message.endsWith("\\")
				|| message.endsWith("~") || message.endsWith("~") || message.endsWith("=") || message.endsWith("+")
				|| message.endsWith(":P") || message.endsWith(":O") || message.endsWith(":S") || message.endsWith(":3")
				|| message.endsWith(":p") || message.endsWith(":o") || message.endsWith(":s") || message.endsWith("<3")
				|| message.endsWith(":c")))
			builder.insert(message.length(), ".");
		if (!(message.startsWith(".") || message.startsWith("?") || message.startsWith("!") || message.startsWith(")")
				|| message.startsWith("]") || message.startsWith(":D") || message.startsWith("xD")
				|| message.startsWith("*") || message.startsWith("-") || message.startsWith("_")
				|| message.startsWith(",") || message.startsWith("'") || message.startsWith("/")
				|| message.startsWith("|") || message.startsWith("(") || message.startsWith("[")
				|| message.startsWith("@") || message.startsWith("#") || message.startsWith("$")
				|| message.startsWith("%") || message.startsWith("^") || message.startsWith("{")
				|| message.startsWith("}") || message.startsWith(";") || message.startsWith(":")
				|| message.startsWith("<") || message.startsWith(">") || message.startsWith("\\")
				|| message.startsWith("~") || message.startsWith("~") || message.startsWith("=")
				|| message.startsWith("+") || message.startsWith("<3")))
			builder.replace(0, 1, message.substring(0, 1).toUpperCase());
		String display = user.getBase().getDisplayName();
		OverloadedWorldHolder wh = Elementals.getGM().getWorldsHolder().getDefaultWorld();
		String playerName = event.getPlayer().getName();
		String group = wh.getUser(playerName).getGroupName();
		int level = user.getServerLevel();
		if (group.equals("Incepator") || group.equals("Avansat") || group.equals("Expert") || group.equals("Pro")) {
			if (level < 25) {
				if (!group.equals("Incepator"))
					wh.getUser(playerName).setGroup(wh.getGroup("Incepator"));
			} else if (level >= 25 && level < 50) {
				if (!group.equals("Avansat"))
					wh.getUser(playerName).setGroup(wh.getGroup("Avansat"));
			} else if (level >= 50 && level < 75) {
				if (!group.equals("Expert"))
					wh.getUser(playerName).setGroup(wh.getGroup("Expert"));
			} else if (level >= 75) {
				if (!group.equals("Pro"))
					wh.getUser(playerName).setGroup(wh.getGroup("Pro"));
			}
		}
		switch (group) {
		case "Helper":
			event.setFormat("§b[§9Helper§b] §9§l(§clvl." + level + "§9§l) " + display + " §e➽ §b%2$s");
			break;
		case "Builder":
			if (event.getPlayer().getName().equals("yapito")) // Adi :))
				event.setFormat("§8[§fBuilder§8] " + display + " §e➽ §f%2$s");
			else
				event.setFormat("§8[§fBuilder§8] " + display + " §e➽ §b%2$s");
			break;
		case "Moderator":
			event.setFormat("§5[§2Moderator§5] §9§l(§clvl." + level + "§9§l) " + display + " §e➽ §b%2$s");
			break;
		case "Admin":
			event.setFormat("§8[§cAdmin§8] " + display + " §e➽ §a%2$s");
			break;
		case "Iron":
			event.setFormat("§f[§7IronVip§f] §9§l(§clvl." + level + "§9§l) " + display + " §e➽ §f%2$s");
			break;
		case "Gold":
			event.setFormat("§f[§6GoldVip§f] §9§l(§clvl." + level + "§9§l) " + display + " §e➽ §f%2$s");
			break;
		case "Diamond":
			event.setFormat("§f[§bDiamondVip§f] §9§l(§clvl." + level + "§9§l) " + display + " §e➽ §f%2$s");
			break;
		case "Rainbow":
			event.setFormat("§f[§4§lR§6§la§e§li§a§ln§9§lB§5§lo§d§lw§f] " + display + " §e➽ §f%2$s");
			break;
		case "Owner":
			event.setFormat("§6[§4Owner§6] " + display + " §e➽ §a%2$s");
			break;
		// TODO Grade Custom
		case "Legend":
			event.setFormat("§f[§4§lLe§e§lge§1§lnd§f] " + display + " §e➽ §a%2$s");
			break;
		case "Incepator":
			event.setFormat("§8[§aIncepator§8] §9§l(§clvl." + level + "§9§l) " + display + " §e➽ §7%2$s");
			break;
		case "Avansat":
			event.setFormat("§8[§6Avansat§8] §9§l(§clvl." + level + "§9§l) " + display + " §e➽ §7%2$s");
			break;
		case "Expert":
			event.setFormat("§8[§cExpert§8] §9§l(§clvl." + level + "§9§l) " + display + " §e➽ §7%2$s");
			break;
		case "Pro":
			event.setFormat("§8[§bPro§8] §9§l(§clvl." + level + "§9§l) " + display + " §e➽ §7%2$s");
			break;
		default:
			event.setFormat("§8[§eJucator§8] §9§l(§clvl." + level + "§9§l) " + display + " §e➽ §7%2$s");
			break;
		}
		final boolean s = swear;
		Elementals.getOnlineUsers().forEach((User user$) -> {
			if (user$.hasPermission("elementals.chat.word") && s) {
				user$.getBase().sendMessage(
						"§a[§cINJURATURA§a] §e" + event.getPlayer().getName() + " §c" + event.getMessage());
				if (user$.hasSounds())
					user$.getBase().playSound(user$.getBase().getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1, 1);
			}
			if (recipeNames.contains(user$.getBase().getName()) && user$.hasSounds())
				user$.getBase().playSound(user$.getBase().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
			else if (user$.hasSounds())
				user$.getBase().playSound(user$.getBase().getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);

		});

		event.setMessage(builder.toString());
	}

	@EventHandler
	public void event3(BlockBreakEvent event) {
		if (event.getBlock().getWorld().getName().equals("spawn"))
			return;
		if (event.isCancelled())
			return;
		User user = Elementals.getUser(event.getPlayer());
		if (FieldUtil.isFieldAtLocation(event.getBlock().getLocation())) {
			Field field = FieldUtil.getFieldByLocation(event.getBlock().getLocation());
			if (!(field.isMember(user.getBase().getUniqueId()) || field.isOwner(user.getBase().getUniqueId())
					|| user.hasPermission("protection.override")))
				return;
		}
		if (!(event.getBlock().getType().equals(Material.DIAMOND_ORE)
				|| event.getBlock().getType().equals(Material.EMERALD_ORE)
				|| event.getBlock().getType().equals(Material.GOLD_ORE)
				|| event.getBlock().getType().equals(Material.IRON_ORE)
				|| event.getBlock().getType().equals(Material.LAPIS_ORE)))
			return;
		ElementalsUtil.removeTag(event.getBlock(), "found");
	}

	@EventHandler
	public void event(BlockPlaceEvent event) {
		if (event.getBlock().getWorld().getName().equals("spawn"))
			return;
		User user = Elementals.getUser(event.getPlayer());
		if (FieldUtil.isFieldAtLocation(event.getBlock().getLocation())) {
			Field field = FieldUtil.getFieldByLocation(event.getBlock().getLocation());
			if (!(field.isMember(user.getBase().getUniqueId()) || field.isOwner(user.getBase().getUniqueId())
					|| user.hasPermission("protection.override")))
				return;
		}
		if (!(event.getBlock().getType().equals(Material.DIAMOND_ORE)
				|| event.getBlock().getType().equals(Material.EMERALD_ORE)
				|| event.getBlock().getType().equals(Material.GOLD_ORE)
				|| event.getBlock().getType().equals(Material.IRON_ORE)
				|| event.getBlock().getType().equals(Material.LAPIS_ORE)))
			return;
		ElementalsUtil.setTag(event.getBlock(), "found");
	}

	@EventHandler
	public void event(BlockDamageEvent event) {
		if (event.getBlock().getWorld().getName().equals("spawn"))
			return;
		if (event.isCancelled())
			return;
		if (!(event.getBlock().getType().equals(Material.DIAMOND_ORE)
				|| event.getBlock().getType().equals(Material.EMERALD_ORE)
				|| event.getBlock().getType().equals(Material.GOLD_ORE)
				|| event.getBlock().getType().equals(Material.IRON_ORE)
				|| event.getBlock().getType().equals(Material.LAPIS_ORE)))
			return;
		if (ElementalsUtil.hasTag(event.getBlock(), "found"))
			return;
		ElementalsUtil.setTag(event.getBlock(), "found");
		int n = ElementalsUtil.foundBlocks(event.getBlock());
		switch (event.getBlock().getType()) {
		case DIAMOND_ORE:
			switch (n) {
			case 1:
				Bukkit.broadcastMessage(event.getPlayer().getDisplayName() + " §ba descoperit o bucata de Diamant.");
				break;
			default:
				Bukkit.broadcastMessage(
						event.getPlayer().getDisplayName() + " §ba descoperit " + n + " bucati de Diamant.");
				break;
			}
			break;
		case GOLD_ORE:
			switch (n) {
			case 1:
				Bukkit.broadcastMessage(event.getPlayer().getDisplayName() + " §ea descoperit o bucata de Aur.");
				break;
			default:
				Bukkit.broadcastMessage(
						event.getPlayer().getDisplayName() + " §ea descoperit " + n + " bucati de Aur.");
				break;
			}
			break;
		case IRON_ORE:
			switch (n) {
			case 1:
				Bukkit.broadcastMessage(event.getPlayer().getDisplayName() + " §7a descoperit o bucata de Fier.");
				break;
			default:
				Bukkit.broadcastMessage(
						event.getPlayer().getDisplayName() + " §7a descoperit " + n + " bucati de Fier.");
				break;
			}
			break;
		case LAPIS_ORE:
			switch (n) {
			case 1:
				Bukkit.broadcastMessage(event.getPlayer().getDisplayName() + " §9a descoperit o bucata de Lapis.");
				break;
			default:
				Bukkit.broadcastMessage(
						event.getPlayer().getDisplayName() + " §9a descoperit " + n + " bucati de Lapis.");
				break;
			}
			break;
		case EMERALD_ORE:
			switch (n) {
			case 1:
				Bukkit.broadcastMessage(event.getPlayer().getDisplayName() + " §aa descoperit o bucata de Emerald.");
				break;
			default:
				Bukkit.broadcastMessage(
						event.getPlayer().getDisplayName() + " §aa descoperit " + n + " bucati de Emerald.");
				break;
			}
			break;
		default:
			break;
		}
	}

	@EventHandler
	public void event(BlockBreakEvent event) {
		if (event.getPlayer().getInventory().getItemInMainHand() == null)
			return;
		if (!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
			return;
		if (!event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName())
			return;
		if (event.getPlayer().getWorld().getName().equals("spawn")
				|| DungeonUtil.checkEndDungeon(event.getBlock().getLocation()))
			return;
		User user = Elementals.getUser(event.getPlayer());
		int yaw = ElementalsUtil.yawCorrection((int) event.getPlayer().getLocation().getYaw());
		int pitch = (int) event.getPlayer().getLocation().getPitch();
		Material blockType = event.getBlock().getType();
		String displayName = event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName();
		if (displayName.equals("§bNaNo Tarnacop")) {
			if (blockType.equals(Material.STONE) || blockType.equals(Material.COBBLESTONE)
					|| blockType.equals(Material.GOLD_ORE) || blockType.equals(Material.IRON_ORE)
					|| blockType.equals(Material.COAL_ORE) || blockType.equals(Material.LAPIS_ORE)
					|| blockType.equals(Material.LAPIS_BLOCK) || blockType.equals(Material.SANDSTONE)
					|| blockType.equals(Material.GOLD_BLOCK) || blockType.equals(Material.IRON_BLOCK)
					|| blockType.equals(Material.DOUBLE_STONE_SLAB2) || blockType.equals(Material.STONE_SLAB2)
					|| blockType.equals(Material.BRICK) || blockType.equals(Material.MOSSY_COBBLESTONE)
					|| blockType.equals(Material.OBSIDIAN) || blockType.equals(Material.DIAMOND_ORE)
					|| blockType.equals(Material.COBBLESTONE_STAIRS) || blockType.equals(Material.ICE)
					|| blockType.equals(Material.NETHERRACK) || blockType.equals(Material.GLOWSTONE)
					|| blockType.equals(Material.GLASS) || blockType.equals(Material.STAINED_GLASS)
					|| blockType.equals(Material.THIN_GLASS) || blockType.equals(Material.STAINED_GLASS_PANE)
					|| blockType.equals(Material.BRICK_STAIRS) || blockType.equals(Material.SMOOTH_BRICK)
					|| blockType.equals(Material.SMOOTH_STAIRS) || blockType.equals(Material.NETHER_BRICK)
					|| blockType.equals(Material.NETHER_BRICK_STAIRS) || blockType.equals(Material.NETHER_FENCE)
					|| blockType.equals(Material.ENDER_STONE) || blockType.equals(Material.SANDSTONE_STAIRS)
					|| blockType.equals(Material.EMERALD_ORE) || blockType.equals(Material.EMERALD_BLOCK)
					|| blockType.equals(Material.COBBLE_WALL) || blockType.equals(Material.MOSSY_COBBLESTONE)
					|| blockType.equals(Material.QUARTZ_ORE) || blockType.equals(Material.QUARTZ_BLOCK)
					|| blockType.equals(Material.QUARTZ_STAIRS) || blockType.equals(Material.STAINED_CLAY)
					|| blockType.equals(Material.HARD_CLAY) || blockType.equals(Material.PRISMARINE)
					|| blockType.equals(Material.SEA_LANTERN) || blockType.equals(Material.BRICK)
					|| blockType.equals(Material.BRICK) || blockType.equals(Material.BRICK)
					|| blockType.equals(Material.BRICK) || blockType.equals(Material.COAL_BLOCK)
					|| blockType.equals(Material.PACKED_ICE) || blockType.equals(Material.RED_SANDSTONE)
					|| blockType.equals(Material.RED_SANDSTONE_STAIRS) || blockType.equals(Material.FURNACE)
					|| blockType.equals(Material.BURNING_FURNACE) || blockType.equals(Material.DISPENSER)
					|| blockType.equals(Material.PISTON_BASE) || blockType.equals(Material.PISTON_STICKY_BASE)
					|| blockType.equals(Material.REDSTONE_LAMP_OFF) || blockType.equals(Material.REDSTONE_LAMP_ON)
					|| blockType.equals(Material.REDSTONE_BLOCK) || blockType.equals(Material.HOPPER)
					|| blockType.equals(Material.DROPPER) || blockType.equals(Material.STONE_PLATE)
					|| blockType.equals(Material.GOLD_PLATE) || blockType.equals(Material.IRON_PLATE)
					|| blockType.equals(Material.IRON_TRAPDOOR) || blockType.equals(Material.BEACON))
				ElementalsUtil.breakNano(user, pitch, yaw, event.getBlock(), NanoType.PICKAXE);
			else if (blockType.equals(Material.REDSTONE_ORE) || blockType.equals(Material.GLOWING_REDSTONE_ORE))
				ElementalsUtil.breakNanoX(user, pitch, yaw, event.getBlock(), NanoType.PICKAXE);
		} else if (displayName.equals("§bNaNo Lopata")) {
			if (blockType.equals(Material.GRASS) || blockType.equals(Material.DIRT) || blockType.equals(Material.SAND)
					|| blockType.equals(Material.GRAVEL) || blockType.equals(Material.SOUL_SAND)
					|| blockType.equals(Material.MYCEL) || blockType.equals(Material.CLAY)
					|| blockType.equals(Material.SNOW) || blockType.equals(Material.SNOW_BLOCK))
				ElementalsUtil.breakNano(user, pitch, yaw, event.getBlock(), NanoType.SPADE);
		} else if (displayName.equals("§bNaNo Topor")) {
			if (blockType.equals(Material.WOOD) || blockType.equals(Material.LOG) || blockType.equals(Material.LOG_2)
					|| blockType.equals(Material.ACACIA_FENCE) || blockType.equals(Material.ACACIA_FENCE_GATE)
					|| blockType.equals(Material.ACACIA_STAIRS) || blockType.equals(Material.FENCE)
					|| blockType.equals(Material.FENCE_GATE) || blockType.equals(Material.WOOD_STAIRS)
					|| blockType.equals(Material.WOOD_STEP) || blockType.equals(Material.DOUBLE_STEP)
					|| blockType.equals(Material.WOOD_DOUBLE_STEP) || blockType.equals(Material.STEP)
					|| blockType.equals(Material.DARK_OAK_STAIRS) || blockType.equals(Material.DARK_OAK_FENCE)
					|| blockType.equals(Material.DARK_OAK_FENCE_GATE) || blockType.equals(Material.SPRUCE_WOOD_STAIRS)
					|| blockType.equals(Material.SPRUCE_FENCE) || blockType.equals(Material.SPRUCE_FENCE_GATE)
					|| blockType.equals(Material.JUNGLE_WOOD_STAIRS) || blockType.equals(Material.JUNGLE_FENCE)
					|| blockType.equals(Material.JUNGLE_FENCE_GATE) || blockType.equals(Material.BIRCH_WOOD_STAIRS)
					|| blockType.equals(Material.BIRCH_FENCE) || blockType.equals(Material.BIRCH_FENCE_GATE)
					|| blockType.equals(Material.TRAP_DOOR) || blockType.equals(Material.WORKBENCH)
					|| blockType.equals(Material.STANDING_BANNER) || blockType.equals(Material.WALL_BANNER)
					|| blockType.equals(Material.SIGN_POST) || blockType.equals(Material.WALL_SIGN)
					|| blockType.equals(Material.NOTE_BLOCK) || blockType.equals(Material.WOOD_PLATE))
				ElementalsUtil.breakNano(user, pitch, yaw, event.getBlock(), NanoType.AXE);
		}
	}

	@EventHandler
	public void event(BlockBurnEvent event) {
		if (!event.getBlock().getWorld().getName().equals("spawn"))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event(BlockIgniteEvent event) {
		if (!event.getBlock().getWorld().getName().equals("spawn"))
			return;
		if (event.getCause().equals(IgniteCause.FLINT_AND_STEEL)) {
			User user = Elementals.getUser(event.getPlayer());
			if (user.hasPermission("elementals.admin"))
				return;
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void event(BlockSpreadEvent event) {
		if (!event.getBlock().getWorld().getName().equals("spawn"))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event(EntityDamageEvent event) {
		if (CitizensAPI.getNPCRegistry().isNPC(event.getEntity()))
			return;
		if (!event.getEntity().getType().equals(EntityType.PLAYER))
			return;
		User user = Elementals.getUser(event.getEntity().getUniqueId());
		if (ElementalsUtil.hasRandomTpCmdDelay(user)) {
			ElementalsUtil.cancelRandomTpCmd(user);
			ElementalsUtil.removeTandomTpCmdDelay(user);
		}
		if (!event.getEntity().getWorld().getName().equals("spawn"))
			return;
		if (WGConfigLoc.checkPvP(event.getEntity().getLocation()))
			return;
		if (DungeonUtil.checkIceDungeon(event.getEntity().getLocation())
				|| DungeonUtil.checkLavaDungeon(event.getEntity().getLocation()))
			return;
		// if (WGConfigLoc.checkMobArena(event.getEntity().getLocation()))
		// if (Elementals.getMobArena().getArenaMaster().getArenaWithPlayer((Player)
		// event.getEntity()) != null)
		// return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event(FakeBlockBreakEvent event) {
		if (event.getPlayer().getInventory().getItemInMainHand() == null)
			return;
		if (!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
			return;
		if (!event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName())
			return;
		if (event.getPlayer().getWorld().getName().equals("spawn")
				|| DungeonUtil.checkEndDungeon(event.getBlock().getLocation()))
			return;
		User user = Elementals.getUser(event.getPlayer());
		int yaw = ElementalsUtil.yawCorrection((int) event.getPlayer().getLocation().getYaw());
		int pitch = (int) event.getPlayer().getLocation().getPitch();
		Material blockType = event.getBlock().getType();
		if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()
				.equals("§bNaNo Tarnacop")) {
			if (blockType.equals(Material.STONE) || blockType.equals(Material.COBBLESTONE)
					|| blockType.equals(Material.GOLD_ORE) || blockType.equals(Material.IRON_ORE)
					|| blockType.equals(Material.COAL_ORE) || blockType.equals(Material.LAPIS_ORE)
					|| blockType.equals(Material.LAPIS_BLOCK) || blockType.equals(Material.SANDSTONE)
					|| blockType.equals(Material.GOLD_BLOCK) || blockType.equals(Material.IRON_BLOCK)
					|| blockType.equals(Material.DOUBLE_STONE_SLAB2) || blockType.equals(Material.STONE_SLAB2)
					|| blockType.equals(Material.BRICK) || blockType.equals(Material.MOSSY_COBBLESTONE)
					|| blockType.equals(Material.OBSIDIAN) || blockType.equals(Material.DIAMOND_ORE)
					|| blockType.equals(Material.COBBLESTONE_STAIRS) || blockType.equals(Material.ICE)
					|| blockType.equals(Material.NETHERRACK) || blockType.equals(Material.GLOWSTONE)
					|| blockType.equals(Material.GLASS) || blockType.equals(Material.STAINED_GLASS)
					|| blockType.equals(Material.THIN_GLASS) || blockType.equals(Material.STAINED_GLASS_PANE)
					|| blockType.equals(Material.BRICK_STAIRS) || blockType.equals(Material.SMOOTH_BRICK)
					|| blockType.equals(Material.SMOOTH_STAIRS) || blockType.equals(Material.NETHER_BRICK)
					|| blockType.equals(Material.NETHER_BRICK_STAIRS) || blockType.equals(Material.NETHER_FENCE)
					|| blockType.equals(Material.ENDER_STONE) || blockType.equals(Material.SANDSTONE_STAIRS)
					|| blockType.equals(Material.EMERALD_ORE) || blockType.equals(Material.EMERALD_BLOCK)
					|| blockType.equals(Material.COBBLE_WALL) || blockType.equals(Material.MOSSY_COBBLESTONE)
					|| blockType.equals(Material.QUARTZ_ORE) || blockType.equals(Material.QUARTZ_BLOCK)
					|| blockType.equals(Material.QUARTZ_STAIRS) || blockType.equals(Material.STAINED_CLAY)
					|| blockType.equals(Material.HARD_CLAY) || blockType.equals(Material.PRISMARINE)
					|| blockType.equals(Material.SEA_LANTERN) || blockType.equals(Material.BRICK)
					|| blockType.equals(Material.BRICK) || blockType.equals(Material.BRICK)
					|| blockType.equals(Material.BRICK) || blockType.equals(Material.COAL_BLOCK)
					|| blockType.equals(Material.PACKED_ICE) || blockType.equals(Material.RED_SANDSTONE)
					|| blockType.equals(Material.RED_SANDSTONE_STAIRS) || blockType.equals(Material.FURNACE)
					|| blockType.equals(Material.BURNING_FURNACE) || blockType.equals(Material.DISPENSER)
					|| blockType.equals(Material.PISTON_BASE) || blockType.equals(Material.PISTON_STICKY_BASE)
					|| blockType.equals(Material.REDSTONE_LAMP_OFF) || blockType.equals(Material.REDSTONE_LAMP_ON)
					|| blockType.equals(Material.REDSTONE_BLOCK) || blockType.equals(Material.HOPPER)
					|| blockType.equals(Material.DROPPER) || blockType.equals(Material.STONE_PLATE)
					|| blockType.equals(Material.GOLD_PLATE) || blockType.equals(Material.IRON_PLATE)
					|| blockType.equals(Material.IRON_TRAPDOOR) || blockType.equals(Material.BEACON))
				ElementalsUtil.breakNano(user, pitch, yaw, event.getBlock(), NanoType.PICKAXE);
			else if (blockType.equals(Material.REDSTONE_ORE) || blockType.equals(Material.GLOWING_REDSTONE_ORE))
				ElementalsUtil.breakNanoX(user, pitch, yaw, event.getBlock(), NanoType.PICKAXE);
		} else if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()
				.equals("§bNaNo Lopata")) {
			if (blockType.equals(Material.GRASS) || blockType.equals(Material.DIRT) || blockType.equals(Material.SAND)
					|| blockType.equals(Material.GRAVEL) || blockType.equals(Material.SOUL_SAND)
					|| blockType.equals(Material.MYCEL) || blockType.equals(Material.CLAY)
					|| blockType.equals(Material.SNOW) || blockType.equals(Material.SNOW_BLOCK))
				ElementalsUtil.breakNano(user, pitch, yaw, event.getBlock(), NanoType.SPADE);
		} else if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()
				.equals("§bNaNo Topor")) {
			if (blockType.equals(Material.WOOD) || blockType.equals(Material.LOG) || blockType.equals(Material.LOG_2)
					|| blockType.equals(Material.ACACIA_FENCE) || blockType.equals(Material.ACACIA_FENCE_GATE)
					|| blockType.equals(Material.ACACIA_STAIRS) || blockType.equals(Material.FENCE)
					|| blockType.equals(Material.FENCE_GATE) || blockType.equals(Material.WOOD_STAIRS)
					|| blockType.equals(Material.WOOD_STEP) || blockType.equals(Material.DOUBLE_STEP)
					|| blockType.equals(Material.WOOD_DOUBLE_STEP) || blockType.equals(Material.STEP)
					|| blockType.equals(Material.DARK_OAK_STAIRS) || blockType.equals(Material.DARK_OAK_FENCE)
					|| blockType.equals(Material.DARK_OAK_FENCE_GATE) || blockType.equals(Material.SPRUCE_WOOD_STAIRS)
					|| blockType.equals(Material.SPRUCE_FENCE) || blockType.equals(Material.SPRUCE_FENCE_GATE)
					|| blockType.equals(Material.JUNGLE_WOOD_STAIRS) || blockType.equals(Material.JUNGLE_FENCE)
					|| blockType.equals(Material.JUNGLE_FENCE_GATE) || blockType.equals(Material.BIRCH_WOOD_STAIRS)
					|| blockType.equals(Material.BIRCH_FENCE) || blockType.equals(Material.BIRCH_FENCE_GATE)
					|| blockType.equals(Material.TRAP_DOOR) || blockType.equals(Material.WORKBENCH)
					|| blockType.equals(Material.STANDING_BANNER) || blockType.equals(Material.WALL_BANNER)
					|| blockType.equals(Material.SIGN_POST) || blockType.equals(Material.WALL_SIGN)
					|| blockType.equals(Material.NOTE_BLOCK) || blockType.equals(Material.WOOD_PLATE))
				ElementalsUtil.breakNano(user, pitch, yaw, event.getBlock(), NanoType.AXE);
		}
	}

	@EventHandler
	public void event(HangingBreakByEntityEvent event) {
		if (CitizensAPI.getNPCRegistry().isNPC(event.getEntity()))
			return;
		if (!(event.getEntity().getWorld().getName().equals("spawn"))
				|| DungeonUtil.checkEndDungeon(event.getEntity().getLocation()))
			return;
		if (event.getRemover().getType().equals(EntityType.PLAYER)) {
			User user = Elementals.getUser((Player) event.getRemover());
			if (!user.hasPermission("elementals.admin"))
				event.setCancelled(true);
		} else if (event.getRemover().getType().equals(EntityType.ARROW)
				|| event.getRemover().getType().equals(EntityType.EGG)
				|| event.getRemover().getType().equals(EntityType.ENDER_PEARL)
				|| event.getRemover().getType().equals(EntityType.SNOWBALL)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void event(PlayerArmorStandManipulateEvent event) {
		if (CitizensAPI.getNPCRegistry().isNPC(event.getRightClicked()))
			return;
		if (!(event.getPlayer().getWorld().getName().equals("spawn"))
				|| DungeonUtil.checkEndDungeon(event.getRightClicked().getLocation()))
			return;
		User user = Elementals.getUser(event.getPlayer());
		if (user.hasPermission("elementals.admin"))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event(HangingPlaceEvent event) {
		if (CitizensAPI.getNPCRegistry().isNPC(event.getEntity()))
			return;
		if (!(event.getPlayer().getWorld().getName().equals("spawn"))
				|| DungeonUtil.checkEndDungeon(event.getEntity().getLocation()))
			return;
		User user = Elementals.getUser(event.getPlayer());
		if (user.hasPermission("elementals.admin"))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event(LeavesDecayEvent event) {
		if (event.getBlock().getWorld().getName().equals("spawn")) {
			event.setCancelled(true);
			return;
		}
		double random = ElementalsUtil.nextDouble(45);
		if (random <= 1)
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(),
					new ItemStack(Material.GOLDEN_APPLE));
		else if (random > 1 && random <= 3)
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(),
					new ItemStack(Material.APPLE));
	}

	@EventHandler
	public void event(McMMOPlayerLevelUpEvent event) {
		if (CitizensAPI.getNPCRegistry().isNPC(event.getPlayer()))
			return;
		User user = Elementals.getUser(event.getPlayer());
		if (user.getServerLevel() < 10)
			return;
		if ((user.getServerLevel() % 200) != 0)
			return;
		user.addPoints(5);
		event.getPlayer().sendMessage("§2Au fost adaugate §65 §2PikaPoints in contul tau!");
	}

	@EventHandler
	public void event(PlayerBucketEmptyEvent event) {
		if (!(event.getPlayer().getWorld().getName().equals("spawn")
				|| DungeonUtil.checkEndDungeon(event.getBlockClicked().getLocation())))
			return;
		User user = Elementals.getUser(event.getPlayer());
		if (user.hasPermission("elementals.admin"))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event(PlayerBucketFillEvent event) {
		if (!(event.getPlayer().getWorld().getName().equals("spawn")
				|| DungeonUtil.checkEndDungeon(event.getBlockClicked().getLocation())))
			return;
		User user = Elementals.getUser(event.getPlayer());
		if (user.hasPermission("elementals.admin"))
			return;
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(PlayerCommandPreprocessEvent event) {
		User user = Elementals.getUser(event.getPlayer());
		if (event.getMessage().startsWith("/minecraft") || event.getMessage().startsWith("/?")
				|| event.getMessage().startsWith("/bukkit") || event.getMessage().startsWith("/about")) {
			if (user.hasPermission("elementals.command.minecraft"))
				return;
			event.getPlayer().sendMessage("§4Aici nu e nici un bug ca sa-l poti vedea.");
			event.setCancelled(true);
		} else if (event.getMessage().startsWith("/version") || event.getMessage().startsWith("/ver")
				|| event.getMessage().startsWith("/icanhasbukkit")) {
			event.getPlayer().sendMessage(
					"§aServerul ruleaza pe Spigot versiunea 1.10.2-R0.1-SNAPSHOT\n modificata de §4ownerul §1§lni§e§lcu§4§lch§a.");
			event.setCancelled(true);
		} else if (event.getMessage().startsWith("/spawn")) {
			if (!event.getPlayer().isInsideVehicle())
				return;
			event.getPlayer().sendMessage("§cNu poti folosi comanda cat timp esti intr-un vehicul!");
			event.setCancelled(true);
		} else if (event.getMessage().startsWith("/sethome")) {
			if (event.getPlayer().getWorld().getName().equals("spawn")) {
				if (user.hasPermission("elementals.command.minecraft"))
					return;
				event.getPlayer().sendMessage("§cNu poti folosi comanda aceasta aici!");
				event.setCancelled(true);
			} else if (FieldUtil.isFieldAtLocation(event.getPlayer().getLocation()))
				if (!(FieldUtil.getFieldByLocation(event.getPlayer().getLocation())
						.isMember(event.getPlayer().getUniqueId())
						|| FieldUtil.getFieldByLocation(event.getPlayer().getLocation()).isOwner(
								event.getPlayer().getUniqueId())
						|| user.hasPermission("protection.override"))) {
					event.getPlayer().sendMessage("§cNu poti folosi comanda aceasta aici!");
					event.setCancelled(true);
				}
		}
	}

	@EventHandler
	public void event(PlayerDeathEvent event) {
		if (CitizensAPI.getNPCRegistry().isNPC(event.getEntity()))
			return;
		User user = Elementals.getUser(event.getEntity());
		double random = 0;
		if (!user.getLastDamageCause().equals(DamageCause.SUICIDE)) {
			random = ElementalsUtil.nextDouble(100);
			if (random <= 33.3) {
				@SuppressWarnings("deprecation")
				ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 0, (byte) 3);
				SkullMeta meta = (SkullMeta) head.getItemMeta();
				meta.setOwningPlayer(user.getBase());
				head.setItemMeta(meta);
				event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), head);
			}
		}
		event.setKeepInventory(true);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(event.getEntity().getFirstPlayed()));
		cal.add(Calendar.DATE, 5);
		if (new Date().before(cal.getTime())) {
			event.setKeepLevel(true);
			event.setDroppedExp(0);
			event.getEntity().sendMessage("§eInventarul tau a fost salvat de zeul dragon!");
			event.getEntity().sendMessage("§cNu poti pierde inventarul timp de 5 zile de la prima intrare.");
			return;
		}
		List<ItemStack> items = Lists.newArrayList();
		for (int n = 0; n < event.getEntity().getInventory().getStorageContents().length; n++) {
			ItemStack item = event.getEntity().getInventory().getStorageContents()[n];
			if (item == null)
				continue;
			if (item.getType().equals(Material.AIR))
				continue;
			if (item.containsEnchantment(Enchantment.VANISHING_CURSE))
				continue;
			random = ElementalsUtil.nextDouble(100);
			if (random <= 66.6) {
				event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), item);
			} else {
				items.add(item);
			}
		}
		ItemStack helmet = event.getEntity().getInventory().getHelmet();
		if (helmet != null) {
			if (!helmet.getType().equals(Material.AIR)) {
				if (!helmet.containsEnchantment(Enchantment.VANISHING_CURSE)) {
					random = ElementalsUtil.nextDouble(100);
					if (random <= 66.6)
						event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
								event.getEntity().getInventory().getHelmet());
					else
						items.add(event.getEntity().getInventory().getHelmet());
				}
			}
		}
		ItemStack boots = event.getEntity().getInventory().getBoots();
		if (boots != null) {
			if (!boots.getType().equals(Material.AIR)) {
				if (!boots.containsEnchantment(Enchantment.VANISHING_CURSE)) {
					random = ElementalsUtil.nextDouble(100);
					if (random <= 66.6)
						event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
								event.getEntity().getInventory().getBoots());
					else
						items.add(event.getEntity().getInventory().getBoots());
				}
			}
		}
		ItemStack chestplate = event.getEntity().getInventory().getChestplate();
		if (chestplate != null) {
			if (!chestplate.getType().equals(Material.AIR)) {
				if (!chestplate.containsEnchantment(Enchantment.VANISHING_CURSE)) {
					random = ElementalsUtil.nextDouble(100);
					if (random <= 66.6)
						event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
								event.getEntity().getInventory().getChestplate());
					else
						items.add(event.getEntity().getInventory().getChestplate());
				}
			}
		}
		ItemStack leggings = event.getEntity().getInventory().getLeggings();
		if (leggings != null) {
			if (!leggings.getType().equals(Material.AIR)) {
				if (!leggings.containsEnchantment(Enchantment.VANISHING_CURSE)) {
					random = ElementalsUtil.nextDouble(100);
					if (random <= 66.6)
						event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
								event.getEntity().getInventory().getLeggings());
					else
						items.add(event.getEntity().getInventory().getLeggings());
				}
			}
		}
		ItemStack offhand = event.getEntity().getInventory().getItemInOffHand();
		if (offhand != null) {
			if (!offhand.getType().equals(Material.AIR)) {
				if (!offhand.containsEnchantment(Enchantment.VANISHING_CURSE)) {
					random = ElementalsUtil.nextDouble(100);
					if (random <= 66.6)
						event.getEntity().getWorld().dropItem(event.getEntity().getLocation(),
								event.getEntity().getInventory().getItemInOffHand());
					else
						items.add(event.getEntity().getInventory().getItemInOffHand());
				}
			}
		}
		event.getEntity().getInventory().clear();
		items.forEach((ItemStack item) -> event.getEntity().getInventory().setItem(items.indexOf(item), item));
		items.clear();
	}

	@EventHandler
	public void event(PlayerInteractEntityEvent event) {
		if (!(event.getRightClicked().getWorld().getName().equals("spawn"))
				|| DungeonUtil.checkEndDungeon(event.getRightClicked().getLocation()))
			return;
		if (!event.getRightClicked().getType().equals(EntityType.ITEM_FRAME))
			return;
		User user = Elementals.getUser(event.getPlayer());
		if (user.hasPermission("elementals.admin"))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event0(PlayerInteractEntityEvent event) {
		if (!event.getRightClicked().getType().equals(EntityType.PARROT))
			return;
		Parrot parrot = (Parrot) event.getRightClicked();
		if (parrot.getOwner() == null)
			return;
		if (parrot.getOwner().getUniqueId().equals(event.getPlayer().getUniqueId()))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;
		if (!(event.getClickedBlock().getWorld().getName().equals("spawn")
				|| DungeonUtil.checkEndDungeon(event.getClickedBlock().getLocation())))
			return;
		Preconditions.checkNotNull(event.getPlayer().getInventory().getItemInMainHand());
		if (!(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.MONSTER_EGG)
				|| event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.ARMOR_STAND)))
			return;
		User user = Elementals.getUser(event.getPlayer());
		if (user.hasPermission("elementals.admin"))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event(PlayerJoinEvent event) {
		Elementals.createUser(event.getPlayer());
		Elementals.getOnlineUsers().forEach((User user) -> {
			if (user.hasSounds())
				user.getBase().playSound(user.getBase().getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
		});
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(PlayerLoginEvent event) {
		if (event.getPlayer().getName().contains("§"))
			event.setResult(Result.KICK_OTHER);
		if (event.getResult().equals(Result.KICK_BANNED)) {
			BanEntry entry = Bukkit.getBanList(Type.NAME).getBanEntry(event.getPlayer().getName());
			event.setKickMessage("\n" + "§a[§6PikaCraft§a]\n" + "§aNu te poti conecta!\n" + "§6Motiv: §c"
					+ entry.getReason() + " §e@ " + entry.getSource() + "\n"
					+ ((entry.getExpiration() == null) ? ""
							: ("§9Expira pe: §e"
									+ new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(entry.getExpiration()) + "\n"))
					+ "§ahttps://www.pikacraftmc.ro/");
		}
		if (event.getResult().equals(Result.KICK_WHITELIST))
			event.setKickMessage("\n" + "§a[§6PikaCraft§a]\n" + "§aNu te poti conecta!\n"
					+ "§aServerul este in mentenanta!\n §cReveniti mai tarziu!");
		else if (event.getResult().equals(Result.KICK_FULL))
			event.setKickMessage(
					"\n" + "§a[§6PikaCraft§a]\n" + "§aNu te poti conecta!\n" + "§eServerul este plin.");
	}

	@EventHandler
	public void event1(PlayerMoveEvent event) {
		if (event.getFrom().getBlock().equals(event.getTo().getBlock()))
			return;
		if (CitizensAPI.getNPCRegistry().isNPC(event.getPlayer()))
			return;
		User user = Elementals.getUser(event.getPlayer());
		if (event.getPlayer().isFlying() && !user.hasPermission("elementals.admin")) {
			if (DungeonUtil.checkIceDungeon(event.getTo()) || DungeonUtil.checkLavaDungeon(event.getTo())
					|| DungeonUtil.checkEndDungeon(event.getTo())) {
				event.getPlayer().setFlying(false);
				event.getPlayer().setAllowFlight(false);
				event.getPlayer().sendMessage("§cNu ai voie sa zbori in §4Dungeon§c!");
			}
			if (user.isInElytraParkour()) {
				event.getPlayer().setFlying(false);
				event.getPlayer().setAllowFlight(false);
				event.getPlayer().sendMessage("§cNu ai voie sa zbori in §aElytra Parkour§c!");
			}
		}
	}

	@EventHandler
	public void event(PlayerMoveEvent event) {
		if (event.getFrom().getBlock().equals(event.getTo().getBlock()))
			return;
		if (CitizensAPI.getNPCRegistry().isNPC(event.getPlayer()))
			return;
		Player player = event.getPlayer();
		User user = Elementals.getUser(player);
		World spawn = Bukkit.getWorld("spawn");
		World end = Bukkit.getWorld("world_the_end");
		if (!event.getFrom().getWorld().getName().equals("spawn"))
			return;
		if (user.isInPvp()) {
			if (player.isFlying()) {
				player.setFlying(false);
				player.setAllowFlight(false);
				user.getBase().sendMessage("§cNu poti zbura cat timp esti in pvp!");
			}
		}
		if (WGConfigLoc.checkElytraParkourExit(event.getTo())) {
			if (user.isInElytraParkour()) {
				user.getBase().sendMessage("§cAi iesit din §aElytra Parkour§c!");
				user.toggleElytraParkour(false);
			}
		} else if (WGConfigLoc.checkElytraParkourStart(event.getTo())) {
			if (user.isInElytraParkour())
				user.toggleElytraParkourStart(true);
			else
				event.setTo(new Location(spawn, 231.5, 67, 146.5, -90, 0));
		} else if (WGConfigLoc.checkElytraParkourEnd(event.getTo())) {
			if (user.isInElytraParkourStart()) {
				user.toggleElytraParkour(false);
				user.toggleElytraParkourStart(false);
				user.getBase().sendMessage("§bWow! §eAi terminat §aElytra Parkour§e!");
			}
		}
		if (WGConfigLoc.checkIceDungeonPortal(event.getTo())) {
			if (user.getServerLevel() >= 30) {
				event.setTo(new Location(spawn, -935.5, 72.0, 380.5, 180, 0));
				player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 1, 1);
				player.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 0);
				player.sendMessage("§aTeleportare in: §9Tinutul de Gheata");
			} else {
				player.sendMessage("§cTrebuie sa ai minim level 30 pe server!");
			}
		}
		if (WGConfigLoc.checkLavaDungeonPortal(event.getTo())) {
			if (user.getServerLevel() >= 45) {
				player.teleport(new Location(spawn, -771.5, 73.0, -189.5, 0, 0), TeleportCause.PLUGIN);
				player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 1, 1);
				player.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 0);
				player.sendMessage("§aTeleportare in: §4Tinutul de Lava");
			} else {
				player.sendMessage("§cTrebuie sa ai minim level 45 pe server!");
			}
		}
		if (WGConfigLoc.checkEndDungeonPortal(event.getTo())) {
			if (user.getServerLevel() >= 75) {
				player.teleport(new Location(end, 500.5, 64, 500.5, 90, 0), TeleportCause.PLUGIN);
				player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 1, 1);
				player.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 0);
				player.sendMessage("§aTeleportare in: §5End");
			} else {
				player.sendMessage("§cTrebuie sa ai minim level 75 pe server!");
			}
		}
		if (event.getTo().getBlockX() <= 210 && event.getTo().getBlockY() <= 34 && event.getTo().getBlockZ() <= 151
				&& event.getTo().getBlockX() >= 155 && event.getTo().getBlockY() >= 33
				&& event.getTo().getBlockZ() >= 136) {
			player.setFallDistance(0);
			player.setFireTicks(0);
			player.teleport(new Location(spawn, 212.5, 39, 146.5, 90, 0), TeleportCause.PLUGIN);
		} else if (event.getTo().getBlockX() <= 210 && event.getTo().getBlockY() <= 34
				&& event.getTo().getBlockZ() <= 135 && event.getTo().getBlockX() >= 155
				&& event.getTo().getBlockY() >= 33 && event.getTo().getBlockZ() >= 104) {
			player.setFallDistance(0);
			player.setFireTicks(0);
			player.teleport(new Location(spawn, 157.5, 39, 137.5, 202, 0), TeleportCause.PLUGIN);
		} else if (event.getTo().getBlockX() <= 210 && event.getTo().getBlockY() <= 34
				&& event.getTo().getBlockZ() <= 103 && event.getTo().getBlockX() >= 155
				&& event.getTo().getBlockY() >= 33 && event.getTo().getBlockZ() >= 72) {
			player.setFallDistance(0);
			player.setFireTicks(0);
			player.teleport(new Location(spawn, 157.5, 40, 105.5, 180, 0), TeleportCause.PLUGIN);
		} else if (event.getTo().getBlockX() <= 210 && event.getTo().getBlockY() <= 34
				&& event.getTo().getBlockZ() <= 71 && event.getTo().getBlockX() >= 155
				&& event.getTo().getBlockY() >= 33 && event.getTo().getBlockZ() >= 61) {
			player.setFallDistance(0);
			player.setFireTicks(0);
			player.teleport(new Location(spawn, 157.5, 39, 73.5, 180, 0), TeleportCause.PLUGIN);
		}
		if (player.isFlying() && !user.hasPermission("elementals.admin"))
			if (WGConfigLoc.checkParkour(event.getTo())) {
				if (event.getTo().getBlockX() <= 210 && event.getTo().getBlockZ() <= 151
						&& event.getTo().getBlockX() >= 155 && event.getTo().getBlockZ() >= 136) {
					player.teleport(new Location(spawn, 212.5, 39, 146.5, 90, 0), TeleportCause.PLUGIN);
				} else if (event.getTo().getBlockX() <= 210 && event.getTo().getBlockZ() <= 135
						&& event.getTo().getBlockX() >= 155 && event.getTo().getBlockZ() >= 104) {
					player.teleport(new Location(spawn, 157.5, 39, 137.5, 225, 0), TeleportCause.PLUGIN);
				} else if (event.getTo().getBlockX() <= 210 && event.getTo().getBlockZ() <= 103
						&& event.getTo().getBlockX() >= 155 && event.getTo().getBlockZ() >= 72) {
					player.teleport(new Location(spawn, 157.5, 40, 105.5, 225, 0), TeleportCause.PLUGIN);
				} else if (event.getTo().getBlockX() <= 210 && event.getTo().getBlockZ() <= 71
						&& event.getTo().getBlockX() >= 155 && event.getTo().getBlockZ() >= 61) {
					player.teleport(new Location(spawn, 157.5, 39, 73.5, 225, 0), TeleportCause.PLUGIN);
				}
				player.setFlying(false);
				player.setAllowFlight(false);
				player.sendMessage("§cNu ai voie sa zbori in §aParkour§c!");
			} else if (WGConfigLoc.checkMaze(event.getTo())) {
				player.setFlying(false);
				player.setAllowFlight(false);
				player.sendMessage("§cNu ai voie sa zbori in §aLabirint§c!");
			} else if (WGConfigLoc.checkPvP(event.getTo())) {
				player.setFlying(false);
				player.setAllowFlight(false);
				player.sendMessage("§cNu ai voie sa zbori in §aArena PvP§c!");
			}
	}

	@EventHandler
	public void event(EntityExplodeEvent event) {
		if (!event.getLocation().getWorld().getName().equals("spawn"))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event(PlayerQuitEvent event) {
		User user = Elementals.getUser(event.getPlayer());
		if (user.isInPvp())
			user.getBase().setHealth(0);
		if (user.isInElytraParkour())
			user.getBase().teleport(new Location(Bukkit.getWorld("spawn"), 231.5, 67, 146.5, -90, 0));
		Elementals.removeUser(event.getPlayer());
		Elementals.getOnlineUsers().forEach((User $user) -> {
			if ($user.hasSounds())
				$user.getBase().playSound($user.getBase().getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
		});
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void event(VotifierEvent event) {
		OfflinePlayer offline = Bukkit.getPlayerExact(event.getVote().getUsername());
		if (offline.isOnline()) {
			Player player = Bukkit.getPlayer(event.getVote().getUsername());
			player.getInventory().addItem(new ItemStack(Material.DIAMOND, 5));
			ItemStack key = new ItemStack(Material.PRISMARINE_SHARD);
			ItemMeta meta = key.getItemMeta();
			meta.setDisplayName(ChatColor.GREEN + "Crate Key");
			key.setItemMeta(meta);
			player.getInventory().addItem(key);
			Elementals.getVault().depositPlayer(offline, 500);
			Bukkit.getPlayer(event.getVote().getUsername()).updateInventory();
			// TODO effect la warp vote
			Bukkit.broadcastMessage(ChatColor.WHITE + "[" + ChatColor.GOLD + "/warp vote" + ChatColor.WHITE
					+ "] §aJucatorul §9" + event.getVote().getUsername()
					+ " §aa votat pentru server si a primit un premiu! Multumim! §e:)");
		} else
			Bukkit.broadcastMessage("§aJucatorul " + event.getVote().getUsername()
					+ " nu este online si nu poate primi premiul. §c:(");
	}

	@EventHandler
	public void event0(BlockBreakEvent event) {
		if (event.getPlayer().getWorld().getName().equals("spawn")) {
			User user = Elementals.getUser(event.getPlayer());
			if (user.hasPermission("elementals.admin"))
				return;
			if (WGConfigLoc.lapisMine(event.getBlock().getLocation())) {
				if (user.hasPermission("elementals.mine.lapis"))
					return;
				event.getPlayer().sendMessage("§cTrebuie sa ai gradul de §7IronVip §cpentru a putea sparge.");
			} else if (WGConfigLoc.ironMine(event.getBlock().getLocation())) {
				if (user.hasPermission("elementals.mine.iron"))
					return;
				event.getPlayer().sendMessage("§cTrebuie sa ai gradul de §7IronVip §cpentru a putea sparge.");
			} else if (WGConfigLoc.quartzMine(event.getBlock().getLocation())) {
				if (user.hasPermission("elementals.mine.quartz"))
					return;
				event.getPlayer().sendMessage("§cTrebuie sa ai gradul de §eGoldVip §cpentru a putea sparge.");
			} else if (WGConfigLoc.goldMine(event.getBlock().getLocation())) {
				if (user.hasPermission("elementals.mine.gold"))
					return;
				event.getPlayer().sendMessage("§cTrebuie sa ai gradul de §eGoldVip §cpentru a putea sparge.");
			} else if (WGConfigLoc.diamondMine(event.getBlock().getLocation())) {
				if (user.hasPermission("elementals.mine.diamond"))
					return;
				event.getPlayer().sendMessage("§cTrebuie sa ai gradul de §bDiamondVip §cpentru a putea sparge.");
			} else if (WGConfigLoc.emeraldMine(event.getBlock().getLocation())) {
				if (user.hasPermission("elementals.mine.emerald"))
					return;
				event.getPlayer().sendMessage("§cTrebuie sa ai gradul de §bDiamondVip §cpentru a putea sparge.");
				// }
				// else if (WGConfigLoc.checkMobArena(event.getBlock().getLocation())) {
				// if
				// (Elementals.getMobArena().getArenaMaster().getArenaWithPlayer(event.getPlayer())
				// != null)
				// return;
				// } else if (WGConfigLoc.checkSpleefArena(event.getBlock().getLocation())) {
				// TODO
			}
			event.setCancelled(true);
		} else if (event.getPlayer().getWorld().getName().equals("spawnx")) {
			User user = Elementals.getUser(event.getPlayer());
			if (user.hasPermission("elementals.admin"))
				return;
			event.setCancelled(true);
		}
	}

	// @EventHandler
	// public void event0(BlockPlaceEvent event) {
	// if (!event.getPlayer().getWorld().getName().equals("spawn"))
	// return;
	// if (WGConfigLoc.checkMobArena(event.getBlock().getLocation())) {
	// if
	// (Elementals.getMobArena().getArenaMaster().getArenaWithPlayer(event.getPlayer())
	// != null)
	// return;
	// }
	// User user = Elementals.getUser(event.getPlayer());
	// if (user.hasPermission("elementals.admin"))
	// return;
	// event.setCancelled(true);
	// }

	@EventHandler
	public void event0(EntityDamageByEntityEvent event) {
		if (!event.getEntity().getType().equals(EntityType.PLAYER))
			return;
		if (!event.getDamager().getType().equals(EntityType.PLAYER))
			return;
		if (CitizensAPI.getNPCRegistry().isNPC(event.getEntity()))
			return;
		if (CitizensAPI.getNPCRegistry().isNPC(event.getDamager()))
			return;
		if (event.isCancelled())
			return;
		if (event.getEntity().getWorld().getName().equals("spawn"))
			if (!WGConfigLoc.checkPvP(event.getEntity().getLocation()))
				return;
		if (FieldUtil.isFieldAtLocation(event.getEntity().getLocation()))
			return;
		User damaged = Elementals.getUser((Player) event.getEntity());
		User damager = Elementals.getUser((Player) event.getDamager());
		if (!damaged.getBase().isOp()) {
			if (!damaged.isInPvp())
				damaged.getBase().sendMessage(
						"§6Esti in §cPvP§6! Nu te deconecta timp de 10 secunde altfel pierzi inventarul!");
			damaged.setPvpTicks(11);
			damaged.togglePvp(true);
			damaged.getBase().setFlying(false);
			damaged.getBase().setAllowFlight(false);
		}
		if (!damager.getBase().isOp()) {
			if (!damager.isInPvp())
				damager.getBase().sendMessage(
						"§6Esti in §cPvP§6! Nu te deconecta timp de 10 secunde altfel pierzi inventarul!");
			damager.setPvpTicks(11);
			damager.togglePvp(true);
			damager.getBase().setFlying(false);
			damager.getBase().setAllowFlight(false);
		}
	}

	@EventHandler
	public static void easterEgg(EntityDamageEvent event) {
		if (!event.getEntity().getType().equals(EntityType.CHICKEN))
			return;
		if (event.getEntity().getCustomName() == null)
			return;
		if (!event.getEntity().getCustomName().equals("Gina"))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event0(FakeBlockBreakEvent event) {
		if (!event.getBlock().getWorld().getName().equals("world"))
			return;
		if (!(event.getBlock().getType().equals(Material.LEAVES)
				|| event.getBlock().getType().equals(Material.LEAVES_2)))
			return;
		double random = ElementalsUtil.nextDouble(45);
		if (random <= 1)
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(),
					new ItemStack(Material.GOLDEN_APPLE));
		else if (random > 1 && random <= 3)
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(),
					new ItemStack(Material.APPLE));
	}

	@EventHandler
	public void event(InventoryClickEvent event) {
		if (event.isCancelled())
			return;
		if (event.getClickedInventory() == null)
			return;
		if (!event.getClickedInventory().getType().equals(InventoryType.ANVIL))
			return;
		if (!event.getSlotType().equals(SlotType.RESULT))
			return;
		if (event.getClickedInventory().getItem(0) == null)
			return;
		if (!event.getClickedInventory().getItem(0).hasItemMeta())
			return;
		if (!event.getClickedInventory().getItem(0).getItemMeta().hasDisplayName())
			return;
		String displayName = event.getClickedInventory().getItem(0).getItemMeta().getDisplayName();
		if (displayName.equals("§bNaNo Tarnacop")) {
			ItemStack is = event.getClickedInventory().getItem(2);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName("§bNaNo Tarnacop");
			is.setItemMeta(meta);
			event.setCurrentItem(is);
		} else if (displayName.equals("§bNaNo Sabie")) {
			ItemStack is = event.getClickedInventory().getItem(2);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName("§bNaNo Sabie");
			is.setItemMeta(meta);
			event.setCurrentItem(is);
		} else if (displayName.equals("§bNaNo Lopata")) {
			ItemStack is = event.getClickedInventory().getItem(2);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName("§bNaNo Lopata");
			is.setItemMeta(meta);
			event.setCurrentItem(is);
		} else if (displayName.equals("§bNaNo Topor")) {
			ItemStack is = event.getClickedInventory().getItem(2);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName("§bNaNo Topor");
			is.setItemMeta(meta);
			event.setCurrentItem(is);
		} else if (displayName.equals("§bNaNo Casca")) {
			ItemStack is = event.getClickedInventory().getItem(2);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName("§bNaNo Casca");
			is.setItemMeta(meta);
			event.setCurrentItem(is);
		} else if (displayName.equals("§bNaNo Armura")) {
			ItemStack is = event.getClickedInventory().getItem(2);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName("§bNaNo Armura");
			is.setItemMeta(meta);
			event.setCurrentItem(is);
		} else if (displayName.equals("§bNaNo Pantaloni")) {
			ItemStack is = event.getClickedInventory().getItem(2);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName("§bNaNo Pantaloni");
			is.setItemMeta(meta);
			event.setCurrentItem(is);
		} else if (displayName.equals("§bNaNo Pantofi")) {
			ItemStack is = event.getClickedInventory().getItem(2);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName("§bNaNo Pantofi");
			is.setItemMeta(meta);
			event.setCurrentItem(is);
		} else if (displayName.equals("§bNaNo Arc")) {
			ItemStack is = event.getClickedInventory().getItem(2);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName("§bNaNo Arc");
			is.setItemMeta(meta);
			event.setCurrentItem(is);
		} else if (displayName.equals("§4Lava King Sword")) {
			ItemStack is = event.getClickedInventory().getItem(2);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName("§4Lava King Sword");
			is.setItemMeta(meta);
			event.setCurrentItem(is);
		} else if (displayName.equals("§1Ice King Sword")) {
			ItemStack is = event.getClickedInventory().getItem(2);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName("§1Ice King Sword");
			is.setItemMeta(meta);
			event.setCurrentItem(is);
		} else if (displayName.equals("§5End King Sword")) {
			ItemStack is = event.getClickedInventory().getItem(2);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName("§5End King Sword");
			is.setItemMeta(meta);
			event.setCurrentItem(is);
		} else if (displayName.startsWith("§")) {
			ItemStack is = event.getClickedInventory().getItem(2);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName(displayName);
			is.setItemMeta(meta);
			event.setCurrentItem(is);
		} else {
			ItemMeta meta = event.getClickedInventory().getItem(2).getItemMeta();
			ItemMeta meta2 = event.getCurrentItem().getItemMeta();
			if (meta.hasLore())
				meta2.setLore(meta.getLore());
			event.getCurrentItem().setItemMeta(meta2);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event0(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;
		if (!event.getClickedBlock().getWorld().getName().equals("spawn"))
			return;
		if (!event.getClickedBlock().getType().equals(Material.CHEST))
			return;
		if (!((event.getClickedBlock().getX() == 773 && event.getClickedBlock().getY() == 64
				&& event.getClickedBlock().getZ() == 31)
				|| (event.getClickedBlock().getX() == 780 && event.getClickedBlock().getY() == 64
						&& event.getClickedBlock().getZ() == 38)
				|| (event.getClickedBlock().getX() == 787 && event.getClickedBlock().getY() == 64
						&& event.getClickedBlock().getZ() == 31)))
			return;
		event.setCancelled(true);
		if (event.getItem() == null) {
			event.getPlayer().sendMessage("§cPoti deschide crate-ul cu un §aCrate Key§c!");
			return;
		}
		if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.PRISMARINE_SHARD)) {
			event.getPlayer().sendMessage("§cPoti deschide crate-ul cu un §aCrate Key§c!");
			return;
		}
		if (!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
			event.getPlayer().sendMessage("§cPoti deschide crate-ul cu un §aCrate Key§c!");
			return;
		}
		if (!event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
			event.getPlayer().sendMessage("§cPoti deschide crate-ul cu un §aCrate Key§c!");
			return;
		}
		if (!event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()
				.equals("§aCrate Key")) {
			event.getPlayer().sendMessage("§cPoti deschide crate-ul cu un §aCrate Key§c!");
			return;
		}
		if (CaseUtil.isOpeningCase(event.getPlayer())) {
			event.getPlayer().sendMessage("§cTrebuie sa astepti ca sa se deschida crate-ul!");
			return;
		}
		if (event.getPlayer().getInventory().getItemInMainHand().getAmount() > 1) {
			event.getPlayer().getInventory().getItemInMainHand()
					.setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
		} else
			event.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
		CaseUtil.openCase(CaseType.CRATE, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event1(BlockBreakEvent event) {
		if (event.isCancelled())
			return;
		Block block = event.getBlock();
		Player player = event.getPlayer();
		double random = ElementalsUtil.nextDouble(100);
		ItemStack offhandItem = player.getInventory().getItemInOffHand();
		if (offhandItem != null && offhandItem.getType().equals(Material.TOTEM)) {
			if (random > 33.3)
				return;
		} else if (random > 20)
			return;
		if (!block.getType().equals(Material.MOB_SPAWNER))
			return;
		if (player.getGameMode().equals(GameMode.CREATIVE))
			return;
		UUID uuid = player.getUniqueId();
		Location loc = block.getLocation();
		if (FieldUtil.isFieldAtLocation(loc)) {
			Field field = FieldUtil.getFieldByLocation(loc);
			if (!(field.isOwner(uuid) || field.isMember(uuid) || player.isOp()))
				return;
		}
		ItemStack handItem = player.getInventory().getItemInMainHand();
		if (handItem == null)
			return;
		if (!handItem.containsEnchantment(Enchantment.SILK_TOUCH))
			return;
		event.setExpToDrop(0);
		ItemStack spawner = new ItemStack(Material.MOB_SPAWNER);
		ItemMeta meta = spawner.getItemMeta();
		meta.setDisplayName(ElementalsUtil.entityToName(((CreatureSpawner) block.getState()).getSpawnedType()));
		spawner.setItemMeta(meta);
		block.getWorld().dropItem(loc.add(0.5, 0.25, 0.5), spawner);
	}

	@EventHandler
	public void event4(BlockPlaceEvent event) {
		if (!event.getBlockPlaced().getType().equals(Material.MOB_SPAWNER))
			return;
		ItemStack handItem = event.getItemInHand();
		if (handItem == null)
			return;
		if (!handItem.hasItemMeta())
			return;
		ItemMeta meta = handItem.getItemMeta();
		if (!meta.hasDisplayName())
			return;
		((CreatureSpawner) event.getBlock().getState())
				.setSpawnedType(ElementalsUtil.nameToEntity(meta.getDisplayName()));
	}

	@EventHandler
	public void event1(EntityDamageByEntityEvent event) {
		if (CitizensAPI.getNPCRegistry().isNPC(event.getEntity()))
			return;
		if (!(event.getEntity().getWorld().getName().equals("spawn")
				|| DungeonUtil.checkEndDungeon(event.getEntity().getLocation())))
			return;
		if (!(event.getEntity().getType().equals(EntityType.ITEM_FRAME)
				|| event.getEntity().getType().equals(EntityType.ARMOR_STAND)))
			return;
		if (event.getDamager() instanceof Player) {
			User user = Elementals.getUser((Player) event.getDamager());
			if (user.hasPermission("elementals.admin"))
				return;
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void event1(EntityDamageEvent event) {
		if (!event.getEntity().getType().equals(EntityType.ARMOR_STAND))
			return;
		if (!event.getCause().equals(DamageCause.FIRE_TICK))
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void event1(PlayerInteractEvent event) {
		if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
			return;
		Preconditions.checkNotNull(event.getPlayer().getInventory().getItemInMainHand());
		if (!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta())
			return;
		if (!event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName())
			return;
		if (!event.getPlayer().isSneaking())
			return;
		if (interactList.contains(event.getPlayer().getUniqueId()))
			return;
		interactList.add(event.getPlayer().getUniqueId());
		User user = Elementals.getUser(event.getPlayer());
		if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()
				.equals("§bNaNo Tarnacop")) {
			if (user.getNanoPickType().equals(Elementals.Nano.A)) {
				user.setNanoPickType(Elementals.Nano.B);
				user.getBase().sendMessage("§bNaNo Tarnacop §f-> §61x3");
			} else if (user.getNanoPickType().equals(Elementals.Nano.B)) {
				user.setNanoPickType(Elementals.Nano.C);
				user.getBase().sendMessage("§bNaNo Tarnacop §f-> §63x1");
			} else if (user.getNanoPickType().equals(Elementals.Nano.C)) {
				user.setNanoPickType(Elementals.Nano.D);
				user.getBase().sendMessage("§bNaNo Tarnacop §f-> §63x3");
			} else if (user.getNanoPickType().equals(Elementals.Nano.D)) {
				user.setNanoPickType(Elementals.Nano.A);
				user.getBase().sendMessage("§bNaNo Tarnacop §f-> §61x1");
			}
		} else if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()
				.equals("§bNaNo Lopata")) {
			if (user.getNanoSpadeType().equals(Elementals.Nano.A)) {
				user.setNanoSpadeType(Elementals.Nano.B);
				user.getBase().sendMessage("§bNaNo Lopata §f-> §61x3");
			} else if (user.getNanoSpadeType().equals(Elementals.Nano.B)) {
				user.setNanoSpadeType(Elementals.Nano.C);
				user.getBase().sendMessage("§bNaNo Lopata §f-> §63x1");
			} else if (user.getNanoSpadeType().equals(Elementals.Nano.C)) {
				user.setNanoSpadeType(Elementals.Nano.D);
				user.getBase().sendMessage("§bNaNo Lopata §f-> §63x3");
			} else if (user.getNanoSpadeType().equals(Elementals.Nano.D)) {
				user.setNanoSpadeType(Elementals.Nano.A);
				user.getBase().sendMessage("§bNaNo Lopata §f-> §61x1");
			}
		} else if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()
				.equals("§bNaNo Topor")) {
			if (user.getNanoAxeType().equals(Elementals.Nano.A)) {
				user.setNanoAxeType(Elementals.Nano.B);
				user.getBase().sendMessage("§bNaNo Topor §f-> §61x3");
			} else if (user.getNanoAxeType().equals(Elementals.Nano.B)) {
				user.setNanoAxeType(Elementals.Nano.C);
				user.getBase().sendMessage("§bNaNo Topor §f-> §63x1");
			} else if (user.getNanoAxeType().equals(Elementals.Nano.C)) {
				user.setNanoAxeType(Elementals.Nano.D);
				user.getBase().sendMessage("§bNaNo Topor §f-> §63x3");
			} else if (user.getNanoAxeType().equals(Elementals.Nano.D)) {
				user.setNanoAxeType(Elementals.Nano.A);
				user.getBase().sendMessage("§bNaNo Topor §f-> §61x1");
			}
		}
		Bukkit.getScheduler().runTaskLater(Elementals.get(), () -> interactList.remove(event.getPlayer().getUniqueId()),
				5L);
	}

	@EventHandler
	public void event2(BlockBreakEvent event) {
		if (!event.getBlock().getWorld().getName().equals("world"))
			return;
		if (!(event.getBlock().getType().equals(Material.LEAVES)
				|| event.getBlock().getType().equals(Material.LEAVES_2)))
			return;
		if (FieldUtil.isFieldAtLocation(event.getBlock().getLocation())) {
			Field field = FieldUtil.getFieldByLocation(event.getBlock().getLocation());
			if (!(field.isOwner(event.getPlayer().getUniqueId()) || field.isMember(event.getPlayer().getUniqueId())
					|| event.getPlayer().isOp()))
				return;
		}
		double random = ElementalsUtil.nextDouble(45);
		if (random <= 1)
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(),
					new ItemStack(Material.GOLDEN_APPLE));
		else if (random > 1 && random <= 3)
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(),
					new ItemStack(Material.APPLE));
	}

	/*
	 * @EventHandler public void event2( PlayerInteractEvent event) { if
	 * (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return; if
	 * (!event.getClickedBlock().getType().equals(Material.WALL_SIGN)) return; if
	 * (!(event.getClickedBlock().getX() == -257 && event.getClickedBlock().getY()
	 * == 65 && event.getClickedBlock().getZ() == -45)) return;
	 * WGConfigLoc.teleportPvP(event.getPlayer()); }
	 */
}
