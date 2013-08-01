package util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import main.QCraft;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;

import skills.SkillType;

public class SkillUtil {

	public static void load() {
		if (blockExperience.isEmpty() || entityExperience.isEmpty()) {
			for (SkillType skill : SkillType.values()) {
				String skillName = skill.name().toLowerCase() + ".yml";
				File skillFile = new File(QCraft.get().getDataFolder(), skillName);
				YamlConfiguration skillConfig = YamlConfiguration.loadConfiguration(skillFile);
				Set<String> keys = skillConfig.getKeys(false);
				if (keys.contains("blocks")) {
					int defaultExp = skillConfig.getConfigurationSection("exp").getInt("default");
					Map<String, Object> map = skillConfig.getConfigurationSection("blocks").getValues(false);
					
				}
				else if (keys.contains("entities")) {
					
				}
			}
		}
	}

}
