package qplayer;

import util.PlayerUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class Power implements ActionListener {
	private final Timer timer = new Timer(1200000, this);
	private final UUID id;
	private int power;
	
	public Power(UUID id, int power) {
		this.id = id;
		this.power = power;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (power == 10) {
			timer.stop();
		}
		else {
			power++;
			PlayerUtil.getPlayer(id).sendMessage("Your power is now at " + power);
		}		
	}
	
	public int getPower() {
		return power;
	}
	
	public void lowerPower(int amount) {
		if (power - amount >= -10) {
			power = power - amount;
			PlayerUtil.getPlayer(id).sendMessage("Your power is now at " + power);
		}
		timer.start();
	}
}
