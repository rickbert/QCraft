package skills;

import main.QCraft;

import org.bukkit.entity.Player;

import qplayer.QPlayer;

public class SkillsCommands {

	public static boolean processCommand(String[] args, Player player) {
		QPlayer qplayer = QCraft.getQPlayer(player);
		if (args[0].toLowerCase().equals("setlevel") && arg) {
			
		}
		else {
			for (SkillType skill : SkillType.values()) {
				if (skill.name().toLowerCase().equals(args[0])) {
					qplayer.getSkill(skill).info();
					return true;
				}
			}
		}
		return false;
	}
}