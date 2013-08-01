package qplayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import javax.swing.Timer;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import custom.Clock;

public class Bleed implements ActionListener {
	private final UUID id;
	private final Timer timer = new Timer(1000, this);
	private final Clock clock = new Clock();
	public static boolean bleeding = false;

	public Bleed(Player player) {
		this.id = player.getUniqueId();
		timer.setInitialDelay(0);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (clock.getTime(Clock.SECONDS)) <= 4) {
			
		}
		if (target.isValid() && ticks <= 5) {
			if (event.getActionCommand().equals("start")) {
				timer.setActionCommand("");
				damager.sendMessage("Target is bleeding!");
				if (target instanceof Player) {
					((Player) target).sendMessage("You are bleeding!");
				}
			}

			double damage = 1;
			Iterator<PotionEffect> effects = target.getActivePotionEffects().iterator();
			while (effects.hasNext()) {
				PotionEffect effect = effects.next();
				if (effect.getType().equals(PotionEffectType.REGENERATION) && effect.getAmplifier() == 3) {
					damage = damage * 2;
				}
			}

			isBleeding = true;
			target.damage(damage, damager);

			ticks++;
		} 
		else {
			timer.stop();
			bleeds.get(damager).remove(target);
		}

	}

	private void reset() {
		ticks = 0;
		timer.setActionCommand("start");
		timer.restart();
	}
}
