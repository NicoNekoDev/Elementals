package ro.nicuch.elementals.infernal;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.BlockProjectileSource;

import net.citizensnpcs.api.CitizensAPI;
import ro.nicuch.elementals.elementals.ElementalsUtil;
import ro.nicuch.elementals.enchants.EnchantUtil;
import ro.nicuch.elementals.infernal.InfernalMobsUtil.PowerType;

public class InfernalMobsListener implements Listener {

    @EventHandler
    public void event(EntityDamageEvent event) {
        if (CitizensAPI.getNPCRegistry().isNPC(event.getEntity()))
            return;
        if (!ElementalsUtil.hasTag(event.getEntity(), "infernal"))
            return;
        if (!(event.getEntity() instanceof LivingEntity))
            return;
        LivingEntity living = (LivingEntity) event.getEntity();
        if (!InfernalMobsUtil.hasBossBar(living))
            return;
        InfernalMobsUtil.recalculateBossBarHealth(InfernalMobsUtil.getBossBar(living), living);
    }

    @EventHandler
    public void event(EntityDamageByEntityEvent event) {
        Entity damaged = event.getEntity();
        if (CitizensAPI.getNPCRegistry().isNPC(damaged))
            return;
        if (CitizensAPI.getNPCRegistry().isNPC(event.getDamager()))
            return;
        if (!ElementalsUtil.hasTag(damaged, "infernal"))
            return;
        Entity damager = null;
        if (event.getDamager() instanceof Projectile) {
            Projectile proj = (Projectile) event.getDamager();
            if (proj.getShooter() == null)
                return;
            if (proj.getShooter() instanceof BlockProjectileSource)
                return;
            damager = (Entity) proj.getShooter();
        } else
            damager = event.getDamager();
        if (!(damager instanceof Player))
            return;
        Player player = (Player) damager;
        if (InfernalMobsUtil.hasPower(damaged, PowerType.POISONOUS)) {
            if (!EnchantUtil.checkPotion(player, PotionEffectType.POISON, 1, 8 * 20)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10 * 20, 1));
            }
        }
        if (InfernalMobsUtil.hasPower(damaged, PowerType.BLINDING)) {
            if (!EnchantUtil.checkPotion(player, PotionEffectType.BLINDNESS, 1, 8 * 20)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 1));
            }
        }
        if (InfernalMobsUtil.hasPower(damaged, PowerType.QUICKSAND)) {
            if (!EnchantUtil.checkPotion(player, PotionEffectType.SLOW, 1, 8 * 20)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 1));
            }
        }
    }

    @EventHandler
    public void event(EntityDeathEvent event) {
        if (!ElementalsUtil.hasTag(event.getEntity(), "infernal"))
            return;
        if (!InfernalMobsUtil.hasBossBar(event.getEntity()))
            return;
        InfernalMobsUtil.getBossBar(event.getEntity()).setProgress(0);
    }
}
