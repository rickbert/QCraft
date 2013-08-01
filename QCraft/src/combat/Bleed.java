package combat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.Timer;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Bleed implements ActionListener {
	private final Player damager;
	private final LivingEntity target;
	private final Timer timer;
	private int ticks;
	private static HashMap<Player, HashMap<LivingEntity, Bleed>> bleeds = new HashMap<Player, HashMap<LivingEntity, Bleed>>();
	public static boolean isBleeding = false;

	public Bleed(Player damager, LivingEntity target) {
		this.damager = damager;
		this.target = target;
		ticks = 0;
		timer = new Timer(1000, this);
		timer.setInitialDelay(0);
		timer.setActionCommand("start");
		timer.start();
	}
	
	public static void applyBleed(Player player, LivingEntity target) {
		if (bleeds.containsKey(player)) {
			if (bleeds.get(player).containsKey(target)) {
				bleeds.get(player).get(target).reset();
			}
			else {
				bleeds.get(player).put(target, new Bleed(player, target));
			}
		}
		else {
			HashMap<LivingEntity, Bleed> bleed = new HashMap<LivingEntity, Bleed>();
			bleed.put(target, new Bleed(player, target));
			bleeds.put(player, bleed);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
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
