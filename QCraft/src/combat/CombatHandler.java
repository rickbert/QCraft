package combat;

import industry.Fishing;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import qplayer.Bleed;
import qplayer.QPlayer;
import skills.SkillType;
import custom.Tool;
import main.QCraft;

public class CombatHandler implements Listener {

	public CombatHandler(QCraft plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler (ignoreCancelled = true)
	private void combat(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			if (Bleed.isBleeding) {
				Bleed.isBleeding = false;
			}
			else {
				if (event.getDamager() instanceof Player) {
					if (event.getEntity() instanceof Player) {
						Player player = (Player) event.getEntity();
						Resistance.buff(player.getName());
					}
					Player damager = (Player) event.getDamager();
					QPlayer qplayer = QCraft.getQPlayer(damager);
					String weapon = damager.getItemInHand().getType().name();
					if (weapon.contains("_SWORD")) {
						Swords swords = (Swords) qplayer.getSkill(SkillType.SWORDS);
						swords.combat(event);
					}
					else {
						Toughness toughness = (Toughness) qplayer.getSkill(SkillType.TOUGHNESS);
						toughness.combat(event);
					}
				}
				if (event.getDamager() instanceof Arrow) {
					Arrow arrow = (Arrow) event.getDamager();
					if (arrow.getShooter() instanceof Player) {
						QPlayer qplayer = QCraft.getQPlayer((Player) arrow.getShooter());
						Archery archery = (Archery) qplayer.getSkill(SkillType.ARCHERY);
						archery.combat(event);
					}
				}
			}
		}		
	}
	
	@EventHandler (ignoreCancelled = true)
	private void toughness(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Toughness toughness = (Toughness) QCraft.getQPlayer(player).getSkill(SkillType.TOUGHNESS);
			toughness.resistDamage(event);
		}
	}
	
	@EventHandler (ignoreCancelled = true)
	private void combatActives(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof Player) {
			Player player = event.getPlayer();
			QPlayer qplayer = QCraft.getQPlayer(player);
			switch (Tool.getTool(player.getItemInHand())) {
			case SWORD:
				Swords swords = (Swords) qplayer.getSkill(SkillType.SWORDS);
				swords.primeActive();
				break;
			case FISHING_ROD:
				Fishing fishing = (Fishing) qplayer.getSkill(SkillType.FISHING);
				fishing.primeActive();
			case NONE:
			default:
				Toughness toughness = (Toughness) qplayer.getSkill(SkillType.TOUGHNESS);
				toughness.primeActive();
				break;
			}
		}
	}
}
