package skills;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.UUID;

import javax.swing.Timer;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import main.QCraft;
import util.PlayerUtil;
import custom.Clock;
import custom.Clock.Time;

public class Active implements ActionListener {
	private String activeName;
	private final UUID id;
	private ActiveState activeState = ActiveState.READY;
	private final Timer timer = new Timer(10000, this);
	private final Clock clock = new Clock();


	public enum ActiveState {
		READY, PRIMED, ACTIVE, COOLDOWN;
		
		public static ActiveState getState(String state) {
			for (ActiveState activeState : ActiveState.values()) {
				if (activeState.name().toLowerCase().equals(state.toLowerCase())) {
					return activeState;
				}
			}
			return ActiveState.READY;
		}
	}

	public Active(SkillType skillType, UUID id) {
		this.id = id;
		timer.setRepeats(false);
		loadConfig(skillType);
	}
	
	private void loadConfig(SkillType skillType) {
		String playerName = "Players/" + PlayerUtil.getPlayer(id).getName() + ".yml";
		File playerFile = new File(QCraft.get().getDataFolder(), playerName);
		YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
		ConfigurationSection activeConfig = playerConfig.getConfigurationSection("skills");
		activeConfig = activeConfig.getConfigurationSection(skillType.name().toLowerCase());
		if (activeConfig.contains("active")) {
			String state = activeConfig.getString("active.state");
			this.activeState = ActiveState.getState(state);
			int duration = activeConfig.getInt("active.duration");
			timer.stop();
			timer.setInitialDelay(duration * 1000);
			clock.reset();
			clock.addTime(duration);
			timer.restart();
			
		}
		
		String skillName = skillType.name().toLowerCase() + ".yml";
		File skillFile = new File(QCraft.get().getDataFolder(), skillName);
		YamlConfiguration skillConfig = YamlConfiguration.loadConfiguration(skillFile);
		if (skillConfig.contains("active")) {
			activeName = skillConfig.getString("active.name");
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		timer.stop();
		switch (activeState) {
		case PRIMED:
			activeState = ActiveState.READY;
			PlayerUtil.message(id, activeName + " cancelled!");
			break;
		case ACTIVE:
			timer.setInitialDelay(timer.getDelay());
			timer.restart();
			activeState = ActiveState.COOLDOWN;
			PlayerUtil.message(id, activeName + " ended!");
			break;
		case COOLDOWN:
			activeState = ActiveState.READY;
			PlayerUtil.message(id, activeName + " ready!");
			break;
		default:
			break;
		}
	}

	public void prime(int primeDuration) {
		if (activeState.equals(ActiveState.READY)) {
			PlayerUtil.message(id, activeName + " primed");
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
			PlayerUtil.message(id, activeName + " activated");
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
		return Math.round((timer.getInitialDelay() / 1000) - clock.getTime(Time.SECONDS));
	}

	private void info() {
		switch (activeState) {
		case PRIMED:
			PlayerUtil.message(id, activeName + " currently primed");
			break;
		case ACTIVE:
			clock.reset();
			PlayerUtil.message(id, activeName + " currently active");
			break;
		case COOLDOWN:
			PlayerUtil.message(id, activeName + " on cooldown");
			break;
		default:
			break;
		}
	}
}
