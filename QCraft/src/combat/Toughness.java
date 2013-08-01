package combat;

import java.io.File;
import java.util.UUID;

import main.QCraft;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import skills.Skill;
import skills.Active.ActiveState;

/**
 * Toughness Skill
 * @author Kyusik
 * QCraft
 */

public class Toughness extends Skill {
	private static double defaultMultiplier = 3;
	private static double spawnerMultiplier = 1;
	private static double playerMultiplier = 5;
	private static YamlConfiguration skillConfig;

	public Toughness(UUID id, int level, int exp) {
		super(id, level, exp);
	}
	
	@Override
	public void info() {
		super.info();
		message(0, "Active: Your punch disorents the next target for a short time.");
	    message(0, "Passive: Increases nonweapon damage done by " + (level / 5) + "%");
		message(250, "Level 250 Passive: Halves damage taken from enviormental effects");
		message(750, "Level 750 Passive: Gives a resistance buff after taking damage");
	}
	
	public void combat(EntityDamageByEntityEvent event) {
		Player damager = (Player) event.getDamager();
		LivingEntity target = (LivingEntity) event.getEntity();
		if (active.getState().equals(ActiveState.PRIMED)) {
			active.activate(0, 300);
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
		
		double expGain = event.getDamage();
		double multiplier = defaultMultiplier;
		if (target.getCustomName().equals("spawner")) {
			multiplier = spawnerMultiplier;
		}
		else if (target instanceof Player) {
			multiplier = playerMultiplier;
		}
		expGain = Math.round(expGain * multiplier);
		addExp((int) expGain);
	}

	public void resistDamage(EntityDamageEvent event) {
		switch (event.getCause()) {
		case DROWNING:
		case STARVATION:
		case SUFFOCATION:
			if (level >= 250) {
				event.setDamage(event.getDamage() / 2);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void loadConfig() {
		if (skillConfig == null) {
			File skillFile = new File(QCraft.get().getDataFolder(), "Skills/toughness.yml");
			skillConfig = YamlConfiguration.loadConfiguration(skillFile);
		}
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

	@Override
	protected void save() {
		// TODO Auto-generated method stub
		
	}
}
