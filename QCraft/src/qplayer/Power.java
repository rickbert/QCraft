package qplayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;

import javax.swing.Timer;

import org.bukkit.entity.Player;

public class Power implements ActionListener {
	private final Timer timer = new Timer(1200000, this);
	private final WeakReference<Player> player;
	private int power;
	
	public Power(Player player, int power) {
		this.player = new WeakReference<Player>(player);
		this.power = power;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (power == 10) {
			timer.stop();
		}
		else {
			power++;
			player.get().sendMessage("Your power is now at " + power);
		}		
	}
	
	public int getPower() {
		return power;
	}
	
	public void lowerPower(int amount) {
		if (power - amount >= -10) {
			power = power - amount;
			player.get().sendMessage("Your power is now at " + power);
		}
		timer.start();
	}
}
