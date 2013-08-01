package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import main.QCraft;

import org.apache.commons.io.FileUtils;

import skills.SkillType;

public class ConfigLoader {
	
	public static void load() {
		File pluginFolder = QCraft.get().getDataFolder();
		
		File exchangeFolder = new File(pluginFolder, "Exchange");
		if (!exchangeFolder.exists()) {
			exchangeFolder.mkdirs();
		}
		
		File factionsFolder = new File(pluginFolder, "Factions");
		if (!factionsFolder.exists()) {
			factionsFolder.mkdirs();
		}
		
		File playersFolder = new File(pluginFolder, "Players");
		if (!playersFolder.exists()) {
			playersFolder.mkdirs();
		}
		
		File skillsFolder = new File(pluginFolder, "Skills");
		if (!skillsFolder.exists()) {
			skillsFolder.mkdirs();
		}
		
		for (SkillType skill : SkillType.values()) {
			String skillName = skill.name().toLowerCase() + ".yml";
			File skillFile = new File(skillsFolder, skillName);
			if (!skillFile.exists()) {
				try {
					InputStream skillStream = QCraft.get().getResource("Skills/" + skillName);
					FileUtils.copyInputStreamToFile(skillStream, skillFile);
				} 
				catch (IOException e) {
					QCraft.get().log("Unable to create config file for " + skillName);
				}
			}
		}
	}
	
	public static void regenerate() {
		File pluginFolder = QCraft.get().getDataFolder();
		pluginFolder.delete();
		load();		
	}
}
