package ro.nicuch.elementals.hooks;

import java.util.List;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.collect.Lists;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import fr.xephi.authme.api.v3.AuthMeApi;

public class NCPHookListener implements Listener {
	private static final List<UUID> list = Lists.newArrayList();

	@EventHandler
	public void event(PlayerJoinEvent event) {
		if (AuthMeApi.getInstance().isAuthenticated(event.getPlayer()))
			return;
		NCPExemptionManager.exemptPermanently(event.getPlayer().getUniqueId(), CheckType.MOVING_SURVIVALFLY);
		if (list.contains(event.getPlayer().getUniqueId()))
			return;
		list.add(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void event(PlayerMoveEvent event) {
		if (event.getFrom().getChunk().equals(event.getTo().getChunk()))
			return;
		if (!list.contains(event.getPlayer().getUniqueId()))
			return;
		if (!AuthMeApi.getInstance().isAuthenticated(event.getPlayer()))
			return;
		NCPExemptionManager.unexempt(event.getPlayer().getUniqueId(), CheckType.MOVING_SURVIVALFLY);
		list.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void event(PlayerQuitEvent event) {
		if (!list.contains(event.getPlayer().getUniqueId()))
			return;
		list.remove(event.getPlayer().getUniqueId());
	}
}
