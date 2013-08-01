package qplayer;

import java.io.InputStream;
import java.util.HashMap;

import main.QCraft;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import skills.Skill;
import skills.SkillType;

public class PlayerParser {
	
	public static QPlayer loadPlayer(Player player) {
		QPlayer qplayer = new QPlayer(player);
		InputStream playerData = QCraft.get().getResource("Players/" + player.getName() + ".yml");
		if (playerData == null) {
			
		}
		FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerData);
		qplayer.setMoney(parseMoney(playerConfig));
		qplayer.setFaction(parseFaction(playerConfig));
		qplayer.setSkills(parseSkills(player, playerConfig));
		return qplayer;		
	}
	
	public static int parseMoney(FileConfiguration config) {
		return config.getConfigurationSection("money").getInt("balance");		
	}
	
	public static String parseFaction(FileConfiguration config) {
		return config.getConfigurationSection("faction").getString("name");
	}
	
	public static HashMap<SkillType, Skill> parseSkills(Player player, FileConfiguration config) {
		HashMap<SkillType, Skill> skills = new HashMap<SkillType, Skill>();
		ConfigurationSection skillsSection = config.getConfigurationSection("skills");
		for (String skillPath : skillsSection.getKeys(false)) {
			ConfigurationSection skillInfo = skillsSection.getConfigurationSection(skillPath);
			int level = skillInfo.getInt("level");
			int exp = skillInfo.getInt("exp");
			Skill skill = new Skill(player, level, exp);
			SkillType skillType = SkillType.getSkillType(skillPath);
			skills.put(skillType, skill);			
		}
		return skills;		
	}
	
	public static void savePlayer(QPlayer player) {
		
	}
}
