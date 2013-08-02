package combat;

import java.io.File;
import java.util.UUID;

import main.QCraft;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import qplayer.QPlayer;
import skills.Skill;
import util.PlayerUtil;

/**
 * Archery Skill
 * @author qsik * 
 * QCraft
 **/

public class Archery extends Skill {
	private static double defaultMultiplier = 3;
	private static double spawnerMultiplier = 1;
	private static double playerMultiplier = 5;
	private static YamlConfiguration skillConfig;

	public Archery(UUID id) {
		super(id);
		active = null;
	}

	@Override
	public void info() {
		super.info();
		message(0, "Passive: Increase damage done by " + (level / 10) + "%");
		message(250, "Level 250 Passive: Headshots do extra damage and blind");
		message(500, "Level 500 Passive: Long range shots do extra damage");
		message(750, "Level 750 Passive: Arrows lower opponent's defenses");
	}

	//Combat Function. Called by our Combat Listener when a player hits a Living Entity with an arrow
	public void combat(EntityDamageByEntityEvent event) {
		Arrow arrow = (Arrow) event.getDamager();
		LivingEntity target = (LivingEntity) event.getEntity();
		Player shooter = (Player) arrow.getShooter();
		double damage = event.getDamage();
		double expGain = event.getDamage(); //save the damage number now before we modify it for exp purposes
		damage = damage * (1 + level / 1000.0);
		QPlayer qplayer = PlayerUtil.getQPlayer(shooter);
		qplayer.increaseResistance(); //Damage source causes increase in resistance

		if (level >= 250 && headshot(target, arrow)) {
			damage = damage * 1.25;
			int amplifier = level / 250 - 1;
			//Blind effect below
			PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 60, amplifier);
			target.addPotionEffect(blind);
		}

		//Add a little bonus for long distance shots. Damage proportional to distance traveled
		if (level >= 500) {
			double distance = shooter.getLocation().distance(target.getLocation());
			damage = damage * (1 + (distance / 100.0)); //Damage increased by 0.1 for every 10 meters
		}

		if (level >= 750) {
			qplayer.decreaseResistance(); //arrows can reduce resistance to negative values (take increased damage)
		}

		event.setDamage(damage);

		double multiplier = defaultMultiplier;
		if (target.getCustomName().equals("spawner")) {
			multiplier = spawnerMultiplier;
		}
		else if (target instanceof Player) {
			multiplier = playerMultiplier;
		}
		expGain = expGain * multiplier;
		addExp((int) expGain);
	}

	//Method checks if the arrow hit at or above the eyes of the target
	private boolean headshot(LivingEntity target, Arrow arrow) {
		double head = target.getEyeLocation().getY();
		double hit = arrow.getLocation().getY();
		return hit >= head;
	}

	@Override
	public void loadSkillInfo() {
		if (skillConfig == null) {
			File skillFile = new File(QCraft.get().getDataFolder(), "Skills/archery.yml");
			skillConfig = YamlConfiguration.loadConfiguration(skillFile);
			ConfigurationSection multipliers = skillConfig.getConfigurationSection("multipliers");
			for (String key : multipliers.getKeys(false)) {
				double multiplier = multipliers.getDouble(key);
				switch (key) {
				case "spawner":
					spawnerMultiplier = multiplier;
					break;
				case "player":
					playerMultiplier = multiplier;
				case "default":
					defaultMultiplier = multiplier;
				default:
					break;
				}
			}
		}
	}
}
