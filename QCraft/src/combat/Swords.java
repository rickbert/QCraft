package combat;

import java.io.File;
import java.util.Random;
import java.util.UUID;

import main.QCraft;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import skills.Skill;
import skills.Active.ActiveState;
import util.PlayerUtil;

/**
 * Swords Skill
 * @author Kyusik
 * QCraft
 */

public class Swords extends Skill {
	private static double defaultMultiplier = 3;
	private static double spawnerMultiplier = 1;
	private static double playerMultiplier = 5;
	private static YamlConfiguration skillConfig;
	
	public Swords(UUID id, int level, int exp) {
		super(id, level, exp);
	}
	
	@Override
	public void info() {
		super.info();	
		message(1, "Active: Execute for damage based off missing health");
		message(1, "Passive: " + (int) (level / 2000) + "% Increased Damage" );		
		message(250, "Passive: " + (int) (level * 0.3 / 10) + "% chance to critically strike.");
		message(750, "Level 750 Passive: critical strikes apply a bleed to the enemy");
	}

	public void combat(EntityDamageByEntityEvent event) {
		Player damager = (Player) event.getDamager();
		if (damager.getItemInHand().getType().name().contains("_SWORD")) {
			LivingEntity target = (LivingEntity) event.getEntity();
			double damage = event.getDamage();
			double expGain = event.getDamage();
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
						if (level >= 750 && target instanceof Player) {
							PlayerUtil.getQPlayer(damager).applyBleed(damager, (Player) target);
						}
					}
				}				
			}
			event.setDamage(damage);
			
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
	}

	private boolean criticalHit(Player player) {
		double chance = level * 0.3 / 1000.0;
		double proc = new Random().nextDouble();
		return proc <= chance;
	}
	
	@Override
	public void loadConfig() {
		if (skillConfig == null) {
			File skillFile = new File(QCraft.get().getDataFolder(), "Skills/swords.yml");
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
