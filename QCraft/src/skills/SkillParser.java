package skills;

import java.io.File;
import java.util.HashMap;

import main.QCraft;

import org.bukkit.configuration.file.YamlConfiguration;

public final class SkillParser {
	private static HashMap<SkillType, YamlConfiguration> skillConfig = new HashMap<SkillType, YamlConfiguration>();

	public SkillParser() {
		for (SkillType skill : SkillType.values()) {
			String skillName = skill.name().toLowerCase() + ".yml";
			File skillFile = new File(QCraft.get().getDataFolder(), skillName);
			YamlConfiguration config = YamlConfiguration.loadConfiguration(skillFile);
			skillConfig.put(skill, config);
		}
	}
	
	public static YamlConfiguration getConfig(SkillType skill) {
		return skillConfig.get(skill);
	}
}
