package skills;

import java.io.File;
import java.util.UUID;

import main.QCraft;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import util.PlayerUtil;

public abstract class Skill {
	protected final UUID id;
	protected final String skillName = this.getClass().getSimpleName();
	protected Active active;
	protected int level;
	protected int exp;
	protected int expNext;

	public Skill(UUID id) {
		this.id = id;
		active = new Active(SkillType.getSkillType(skillName), id);
		loadSkillInfo();
		loadPlayerInfo();
		this.expNext = (int) (Math.pow(1.005, this.level) * 100);
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

	protected abstract void loadSkillInfo();
	
	protected void loadPlayerInfo() {
		String playerName = PlayerUtil.getPlayer(id).getName();
		File playerFile = new File(QCraft.get().getDataFolder(), "Players/" + playerName + ".yml");
		YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
		String path = "skills." + skillName.toLowerCase();
		level = Math.max(1, playerConfig.getInt(path + ".level"));
		exp = Math.max(1, playerConfig.getInt(path + ".exp"));
	}
	
	public void save(YamlConfiguration playerConfig) {
		String path = "skills." + skillName.toLowerCase();
		playerConfig.set(path + ".level", level);	
		playerConfig.set(path + ".exp", exp);
		path = path + ".active";
		try {
			playerConfig.set(path + ".state", active.getState().toString().toLowerCase());
			playerConfig.set(path + ".duration", active.getTime());
		}
		catch (Exception e) {
			playerConfig.set(path + ".state", "ready");
			playerConfig.set(path + ".duration", 0);
		}
	}
}
