package qplayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.UUID;

import javax.swing.Timer;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import util.PlayerUtil;
import custom.Clock;
import custom.Clock.Time;

public class Bleed implements ActionListener {
	private final UUID damagerId;
	private final UUID targetId;
	private final Timer timer = new Timer(1000, this);
	private final Clock clock = new Clock();
	public static boolean bleeding = false;

	public Bleed(Player damager, Player target) {
		this.damagerId = damager.getUniqueId();
		this.targetId = target.getUniqueId();
		timer.setInitialDelay(0);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (clock.getTime(Time.SECONDS) < 5) {
			Player damager = PlayerUtil.getPlayer(damagerId);
			Player target = PlayerUtil.getPlayer(targetId);
			if (clock.getTime(Time.SECONDS) < 1) {
				target.sendMessage("You are bleeding!");
			}

			double damage = 1;
			Iterator<PotionEffect> effects = target.getActivePotionEffects().iterator();
			while (effects.hasNext()) {
				PotionEffect effect = effects.next();
				if (effect.getType().equals(PotionEffectType.REGENERATION) && effect.getAmplifier() == 3) {
					damage = damage * 2;
				}
			}

			bleeding = true;
			target.damage(damage, damager);
		}
		else {
			timer.stop();
			PlayerUtil.getQPlayer(targetId).removeBleed(damagerId);
		}
	}

	public void reset() {
		clock.reset();
		timer.restart();
	}
}
