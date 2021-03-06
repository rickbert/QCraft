//package database;
//
//import main.QCraft;
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//import qplayer.QPlayer;
//import skills.Skill;
//import skills.SkillType;
//import util.PlayerUtil;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//class SkillsData {
//	private final QCraft plugin;
//	private final Connection connection = Data.connection();
//
//	public SkillsData(QCraft plugin) {
//		this.plugin = plugin;
//	}
//
//	public void loadPlayer(Player player) {
//		QPlayer qplayer = QCraft.getQPlayer(player);
//		String username = player.getName();
//
//		try {
//			plugin.log("Loading skills for " + username);
//			String skills = "SELECT * FROM skills WHERE username = ?";
//			PreparedStatement skillsQuery = connection.prepareStatement(skills);
//			skillsQuery.setString(1, username);
//			ResultSet skillsData = skillsQuery.executeQuery();
//			if (skillsData.next()) {
//				for (SkillType skill : SkillType.values()) {
//					String skillName = skill.name().toLowerCase();
//					String[] info = skillsData.getString(skillName).split(",");
//					int level = Integer.parseInt(info[0]);
//					int exp = Integer.parseInt(info[1]);
//					qplayer.setSkill(skill, level, exp);
//				}
//			}
//			else {
//				try {
//					String updateSkills = "INSERT INTO skills(username) VALUES(?)";
//					PreparedStatement skillsUpdate = connection.prepareStatement(updateSkills);
//					skillsUpdate.setString(1, username);
//					skillsUpdate.executeUpdate();
//					for (SkillType skill : SkillType.values()) {
//						qplayer.setSkill(skill, 0, 0);
//					}
//				}
//				catch (SQLException e) {
//					plugin.log("Unable to insert skills info for: " + username);
//				}
//			}
//		}
//		catch (SQLException e) {
//			plugin.log("Unable to load skills data for " + username);
//		}
//	}
//
//	public void savePlayer(Player player) {
//		QPlayer qplayer = QCraft.getQPlayer(player);
//		String username = player.getName();
//
//		try {
//			String skillsUpdate = "UPDATE skills SET ? = ? WHERE username = ?";
//			PreparedStatement updateSkills = connection.prepareStatement(skillsUpdate);
//			for (SkillType skillType : SkillType.values()) {
//				Skill skill = qplayer.getSkill(skillType);
//				int level = skill.level();
//				int exp = skill.exp();
//				String info = "" + level + "," + exp;
//				updateSkills.setString(1, skill.name().toLowerCase());
//				updateSkills.setString(2, info);
//				updateSkills.setString(3, username);
//				updateSkills.addBatch();
//			}
//			updateSkills.executeBatch();
//		}
//		catch (SQLException e) {
//			plugin.log("Unable to save Skills data for " + username);
//		}
//	}
//
//	public void quit() {
//		try {
//			String skillsUpdate = "UPDATE skills SET ? = ? WHERE username = ?";
//			PreparedStatement updateSkills = connection.prepareStatement(skillsUpdate);
//			for (Player player : Bukkit.getOnlinePlayers()) {
//				QPlayer qplayer = PlayerUtil.getQPlayer(player);
//				String username = player.getName();
//				for (SkillType skillType : SkillType.values()) {
//					Skill skill = qplayer.getSkill(skillType);
//					int level = skill.getLevel();
//					int exp = skill.getExp();
//					String info = "" + level + "," + exp;
//					updateSkills.setString(1, skill.getName().toLowerCase());
//					updateSkills.setString(2, info);
//					updateSkills.setString(3, username);
//					updateSkills.addBatch();
//				}
//			}
//			updateSkills.executeBatch();
//		}
//		catch (SQLException e) {
//			plugin.log("Unable to save Skills data for all players");
//		}
//	}
//}
