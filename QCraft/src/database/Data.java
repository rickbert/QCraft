package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import qplayer.QPlayer;
import skills.SkillType;

/* test
 * 
 */
public class Data {
	private static Connection connection;
	
	public static Connection connection() {
		if (connection == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://localhost/qcraft";
				connection = DriverManager.getConnection(url, "root", "paulisbest");
				Bukkit.getLogger().info("QCraft-SQL Database Connection Successful!");
			} 
			catch (Exception e) {
				Bukkit.getLogger().info("QCraft-SQL Database Connection Unsuccessful!");
			}
		}
		return connection;
	}
	
	public void quit() {
		try {
			connection.close();
		} 
		catch (Exception e) {
			Bukkit.getLogger().info("Unable to close SQL Database!");
		}
	}
	
	@EventHandler
	private void loadPlayer(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String username = player.getName();
		QPlayer qplayer = new QPlayer(plugin, player);
		
		try {
			plugin.log("Loading skills...");
			String skills = "SELECT * FROM skills WHERE username = ?";
			PreparedStatement skillsQuery = connection.prepareStatement(skills);
			skillsQuery.setString(1, username);
			ResultSet skillsData = skillsQuery.executeQuery();
			if (skillsData.next()) {
				for (SkillType skill : SkillType.values()) {
					String skillName = skill.name().toLowerCase();
					String[] info = skillsData.getString(skillName).split(",");
					int level = Integer.parseInt(info[0]);
					int exp = Integer.parseInt(info[1]);
					qplayer.setSkill(skill, level, exp);
				}
			}
			else {
				try {
					String updateSkills = "INSERT INTO skills(username) VALUES(?)";
					PreparedStatement skillsUpdate = connection.prepareStatement(updateSkills);
					skillsUpdate.setString(1, username);
					skillsUpdate.executeUpdate();
					for (SkillType skill : SkillType.values()) {
						qplayer.setSkill(skill, 0, 0);
					}
				}
				catch (SQLException e) {
					plugin.log("Unable to insert skills info for: " + username);
				}
			}
			
			plugin.log("Loading faction...");
			String faction = "SELECT username, power FROM factions WHERE username = ?";
			PreparedStatement factionQuery = connection.prepareStatement(faction);
			factionQuery.setString(1, username);
			ResultSet factionData = factionQuery.executeQuery();
			if (factionData.next()) {
				qplayer.setPower(factionData.getInt("power"));
			}

			Bukkit.getLogger().info("Loading money...");
			String money = "SELECT balance FROM money WHERE username = ?";
			PreparedStatement moneyQuery = connection.prepareStatement(money);
			ResultSet moneyData = moneyQuery.executeQuery();
			if (moneyData.next()) {
				qplayer.setMoney(moneyData.getInt("balance"));
			}
			else {
				try {
					String updateMoney = "INSERT INTO money(username, balance) VALUES(?, ?)";
					PreparedStatement moneyUpdate = connection.prepareStatement(updateMoney);
					moneyUpdate.setString(1, username);
					moneyUpdate.setInt(2, 0);
					moneyUpdate.executeUpdate();
				}
				catch (SQLException e) {
					plugin.log("Unable to insert money info for " + username);
				}
			}
		}
		catch (Exception e) {
			plugin.log("Unable to load data for " + username);
		}
	}
}
