package skills;

import java.io.File;
import java.lang.ref.WeakReference;

import main.QCraft;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public abstract class Skill {
	protected WeakReference<Player> player;
	protected final String skillName = this.getClass().getSimpleName();
	protected int level;
	protected int exp;
	protected int expNext;
	
	public Skill(Player player, int level, int exp) {
		this.player = new WeakReference<Player>(player);
		this.level = level;
		this.exp = exp;
		this.expNext = (int) (Math.pow(1.005, this.level) * 100);
	}

	public int getLevel() {
		return level;
	}

	public int getExp() {
		return exp;
	}

	public String getName() {
		return skillName;
	}

	public void addExp(int amount) {
		exp = exp + amount;
		player.get().sendMessage("You have gained " + amount + " exp in " + skillName);
		update();
	}
	
	public void primeActive() {
		active.prime(10);
	}

	public void info() {
		player.get().sendMessage(skillName);
		player.get().sendMessage("Level: " + level);
		player.get().sendMessage("EXP: " + exp + " / " + expNext);
		long time = active.getTime();
		String message = "Active Status: ";
		switch (active.getState()) {
		case READY:
			player.get().sendMessage(ChatColor.GREEN + message + "Ready");
			break;
		case PRIMED:
			player.get().sendMessage(ChatColor.RED + message + "Currently Primed. Time Remaining: " + time);
			break;
		case ACTIVE:
			player.get().sendMessage(ChatColor.RED + message + "Currently Active. Time Remaining: " + time);
			break;
		case COOLDOWN:
			player.get().sendMessage(ChatColor.RED + message + "On Cooldown. Time Remaining: " + time);
			break;
		default:
			break;
		}
	}

	public void setLevel(int level) {
		if (level <= 1000 && level >= 0) {
			this.level = level;
			exp = 0;
			expNext = (int) (Math.pow(1.005, level) * 100);
			info();
		} 
		else {
			player.get().sendMessage("Invalid level");
		}
	}

	private void update() {
		if (exp >= expNext && level < 1000) {
			level++;
			exp = 0;
			expNext = (int) (Math.pow(1.005, level) * 100);
			player.get().sendMessage("You are now level " + level + " in " + skillName);
		}
	}

	public void message(int level, String message) {
		if (this.level < level) {
			player.get().sendMessage(ChatColor.RED + message);
		}
		else {
			player.get().sendMessage(ChatColor.GREEN + message);
		}
	}
}
