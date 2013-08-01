package skills;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;

import javax.swing.Timer;

import org.bukkit.entity.Player;

import custom.Clock;

public class Active implements ActionListener {
	private WeakReference<Player> player;
	private final Timer timer;
	private final Clock clock;
	private ActiveState activeState;
	
	public enum ActiveState {
		READY, PRIMED, ACTIVE, COOLDOWN;
	}

	public Active(Skill skill, Player player) {
		this.player = new WeakReference<Player>(player);
		timer = new Timer(10000, this);
		timer.setRepeats(false);
		clock = new Clock();
		activeState = ActiveState.READY;
		activeName = skill.name() + " Active";
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		timer.stop();
		switch (activeState) {
		case PRIMED:
			activeState = ActiveState.READY;
			player.get().sendMessage(activeName + " cancelled!");
			break;
		case ACTIVE:
			timer.setInitialDelay(timer.getDelay());
			timer.restart();
			activeState = ActiveState.COOLDOWN;
			player.get().sendMessage(activeName + " ended!");
			break;
		case COOLDOWN:
			activeState = ActiveState.READY;
			player.get().sendMessage(activeName + " ready!");
			break;
		default:
			break;
		}
	}

	public void prime(int primeDuration) {
		if (activeState.equals(ActiveState.READY)) {
			player.get().sendMessage(activeName + " primed");
			activeState = ActiveState.PRIMED;
			primeDuration = primeDuration * 1000;
			timer.setInitialDelay(primeDuration);
			timer.restart();
		}
		else {
			info();
		}
	}

	public void activate(int activeDuration, int cooldownDuration) {
		if (activeState.equals(ActiveState.PRIMED)) {
			clock.reset();
			player.get().sendMessage(activeName + " activated");
			activeState = ActiveState.ACTIVE;
			activeDuration = activeDuration * 1000;
			cooldownDuration = cooldownDuration * 1000;
			timer.setInitialDelay(activeDuration);
			timer.setDelay(cooldownDuration);
			timer.restart();
		}
		else {
			info();
		}
	}

	public ActiveState getState() {
		return activeState;
	}
	
	public long getTime() {
		return Math.round((timer.getInitialDelay() / 1000) - clock.getTime(Clock.SECONDS));
	}

	private void info() {
		switch (activeState) {
		case PRIMED:
			player.get().sendMessage(activeName + " currently primed");
			break;
		case ACTIVE:
			clock.reset();
			player.get().sendMessage(activeName + " currently active");
			break;
		case COOLDOWN:
			player.get().sendMessage(activeName + " on cooldown");
			break;
		default:
			break;
		}
	}
}
