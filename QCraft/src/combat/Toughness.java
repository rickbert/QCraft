package combat;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import skills.Skill;
import skills.Active.ActiveState;

public class Toughness extends Skill {

	public Toughness(Player player, int level, int exp) {
		super(player, level, exp);
	}
	
	@Override
	public void info() {
		super.info();
		message(0, "Active: Your punch disorents the next target for a shot time.");
	    message(0, "Passive: Increase fist damage done by " + level/5 + "%");
		message(250, "Level 250 Passive: Halves damage taken form enviormental effects");
		message(750, "Level 750 Passive: Damage taken increases resistance for a short time");
	}
	
	public void combat(EntityDamageByEntityEvent event) {
		Player damager = (Player) event.getDamager();
		if (active.getState().equals(ActiveState.PRIMED)) {
			active.activate(0, 300);
			LivingEntity target = (LivingEntity) event.getEntity();
			if (level < 250) {
				target.setVelocity(damager.getLocation().getDirection());
			}
			else if (level < 500) {
				target.setVelocity(damager.getLocation().getDirection().multiply(2));
			}
			else if (level < 750) {
				target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 240, 2));
			}
			else {
				target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 240, 3));
			}
		}
		double damage = event.getDamage();
		damage = damage * (1 + level / 500.0);
		event.setDamage(damage);
		addExp((int) event.getDamage());
	}

	public void resistDamage(EntityDamageEvent event) {
		switch (event.getCause()) {
		case DROWNING:
		case STARVATION:
		case SUFFOCATION:
			if (level() >= 250) {
				event.setDamage(event.getDamage() / 2);
			}
			break;
		default:
			break;
		}
	}
}
