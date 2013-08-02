package skills;

import main.QCraft;

import org.bukkit.entity.Player;

import qplayer.QPlayer;
import util.PlayerUtil;

public class SkillCommands {

	public static boolean processCommand(String[] args, Player player) {
		QPlayer qplayer = PlayerUtil.getQPlayer(player);
		if (args[0].toLowerCase().equals("setlevel") && args.length == 3) {
			SkillType skill = SkillType.getSkillType(args[1]);
			try {
				qplayer.getSkill(skill).setLevel(Integer.parseInt(args[2]));
			}
			catch (Exception e) {
				QCraft.get().log("Unable to set level");
			}
			return true;
		}
		else if (args.length == 1) {
			SkillType skill = SkillType.getSkillType(args[0]);
			QCraft.get().log(skill.name());
			try {
				qplayer.getSkill(skill).info();
			}
			catch (Exception e) {
				QCraft.get().log("Unable to retrieve info for " + args[0]);
			}
			return true;
		}
		return false;
	}
}
