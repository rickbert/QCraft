package skills;

import java.util.UUID;

import org.bukkit.ChatColor;
import util.PlayerUtil;

public abstract class Skill {
	protected final UUID id;
	protected final String skillName = this.getClass().getSimpleName();
	protected Active active;
	protected int level;
	protected int exp;
	protected int expNext;

	public Skill(UUID id, int level, int exp) {
		this.id = id;
		active = new Active(SkillType.getSkillType(skillName), id);
		this.level = level;
		this.exp = exp;
		this.expNext = (int) (Math.pow(1.005, this.level) * 100);
		loadConfig();
	}

	public String getName() {
		return skillName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		if (level <= 1000 && level >= 0) {
			this.level = level;
			exp = 0;
			expNext = (int) (Math.pow(1.005, level) * 100);
			info();
		} 
		else {
			PlayerUtil.message(id, "Invalid level");
		}
	}

	public int getExp() {
		return exp;
	}

	protected void addExp(int amount) {
		if (amount > 0) {
			exp = exp + amount;
			PlayerUtil.message(id, "You have gained " + amount + " exp in " + skillName);
			update();
		}
	}

	public void primeActive() {
		active.prime(10);
	}

	public void info() {
		PlayerUtil.message(id, skillName);
		PlayerUtil.message(id, "Level: " + level);
		PlayerUtil.message(id, "EXP: " + exp + " / " + expNext);
		if (active != null) {
			long time = active.getTime();
			String message = "Active Status: ";
			switch (active.getState()) {
			case READY:
				PlayerUtil.message(id, ChatColor.GREEN + message + "Ready");
				break;
			case PRIMED:
				PlayerUtil.message(id, ChatColor.RED + message + "Currently Primed. Time Remaining: " + time);
				break;
			case ACTIVE:
				PlayerUtil.message(id, ChatColor.RED + message + "Currently Active. Time Remaining: " + time);
				break;
			case COOLDOWN:
				PlayerUtil.message(id, ChatColor.RED + message + "On Cooldown. Time Remaining: " + time);
				break;
			default:
				break;
			}
		}
		else {
			message(0, "No active");
		}
	}



	protected void update() {
		if (exp >= expNext && level < 1000) {
			level++;
			exp = 0;
			expNext = (int) (Math.pow(1.005, level) * 100);
			PlayerUtil.message(id, "You are now level " + level + " in " + skillName);
		}
	}

	protected void message(int level, String message) {
		if (this.level < level) {
			PlayerUtil.message(id, ChatColor.RED + message);
		}
		else {
			PlayerUtil.message(id, ChatColor.GREEN + message);
		}
	}

	protected abstract void loadConfig();
	protected abstract void save();
}
