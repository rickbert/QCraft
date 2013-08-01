package combat;

import java.util.Random;

import main.QCraft;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import skills.Skill;
import skills.SkillType;
import skills.Active.ActiveState;

public class Swords extends Skill {
	
	public Swords(Player player, int level, int exp) {
		super(player, level, exp);
	}
	
	@Override
	public void info() {
		super.info();	
		message(1, "Active: Execute for damage based off missing health.");
		message(1, "Passive: " + (int) level/2000 + "% Increased Damage." );		
		message(250, "Passive: " + (int) level*.3/10 + "% chance to critically strike.");
		message(750, "Level 750 Passive: critical strikes apply a bleed to the enemy");
	}

	public void combat(EntityDamageByEntityEvent event) {
		Player damager = (Player) event.getDamager();
		if (damager.getItemInHand().getType().name().contains("_SWORD")) {
			LivingEntity target = (LivingEntity) event.getEntity();
			double damage = event.getDamage();
			if (active.getState().equals(ActiveState.PRIMED)) {
				int cooldownDuration = 425 - level / 4;
				active.activate(0, cooldownDuration);
				damager.sendMessage("DEMACIA!");
				Damageable entity = (Damageable) event.getEntity();
				double missingHealth = entity.getMaxHealth() - entity.getHealth();
				damage = Math.round((damage * level / 2000.0) + (damage * 0.05 * missingHealth));
			}
			else {
				damage = damage * (1 + level / 2000.0);
				if (level >= 250) {
					if (criticalHit(damager)) {
						damage = damage * 1.5;
						damager.sendMessage("Critical Strike!");
						if (level >= 750) {
							Bleed.applyBleed(damager, target);
						}
					}
				}				
			}
			event.setDamage(damage);
			addExp((int) event.getDamage());
		}
	}

	private boolean criticalHit(Player player) {
		int level = QCraft.getQPlayer(player).getSkill(SkillType.SWORDS).level();
		double chance = level * .3 / 1000.0;
		double proc = new Random().nextDouble();
		return proc <= chance;
	}
}
