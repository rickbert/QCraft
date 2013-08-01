package combat;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;





import skills.Skill;

public class Archery extends Skill {

	public Archery(Player player, int level, int exp) {
		super(player, level, exp);
	}

	@Override
	public void info() {
		super.info();
		message(0, "Passive: Increase damage done by " + level/10 + "%");
		message(250, "Level 250 Passive: Headshots do extra damage and blind");
		message(500, "Level 500 Passive: Long range shots do extra damage");
		message(750, "Level 750 Passive: Arrows lower opponent's defenses");
	}

	public void combat(EntityDamageByEntityEvent event) {
		Arrow arrow = (Arrow) event.getDamager();
		LivingEntity target = (LivingEntity) event.getEntity();
		Player shooter = (Player) arrow.getShooter();
		double damage = event.getDamage();
		damage = damage * (1 + level / 1000.0);
		if (level >= 250 && headshot(target, arrow)) {
			damage = damage * 1.25;
			int amplifier = level / 250 - 1;
			PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 60, amplifier);
			target.addPotionEffect(blind);
		}
		if (level >= 500) {
			if (shooter.getLocation().distance(target.getLocation()) > 45 ){
				damage++;
			}
		}
		Resistance.buff(((Player) target).getName());
		if (level >= 750) {
			Resistance.debuff(((Player) target).getName());
		}
		event.setDamage(damage);
		addExp((int) event.getDamage());
	}

	private boolean headshot(LivingEntity target, Arrow arrow) {
		double head = target.getEyeLocation().getY();
		double hit = arrow.getLocation().getY();
		return hit >= head;
	}
}
