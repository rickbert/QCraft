//package database;
//
//import exchange.Exchange;
//import exchange.Item;
//import main.QCraft;
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerJoinEvent;
//import org.bukkit.event.player.PlayerQuitEvent;
//import qplayer.Money;
//
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.logging.Logger;
//
//class EconomyData implements Listener, ActionListener {
//	private final Connection connection = Data.connection();
//	private final Timer timer = new Timer(10000, this);
//
//	public EconomyData(QCraft plugin) {
//		Bukkit.getPluginManager().registerEvents(this, plugin);
//		try {
//			String query = "SELECT * FROM exchange";
//			ResultSet data = connection.prepareStatement(query).executeQuery();
//			while (data.next()) {
//				new Item(data);
//			}
//		}
//		catch (SQLException e) {
//			Logger.getLogger("Minecraft").info("Unable to retrieve Exchange items");
//		}
//		new Exchange(plugin);
////		timer.start();
//	}
//
//	@EventHandler
//	private void playerJoin(PlayerJoinEvent event) {
//		Player player = event.getPlayer();
//		String name = player.getName();
//		try {
//			String query = "SELECT balance FROM money WHERE username = '" + name + "'";
//			ResultSet data = connection.prepareStatement(query).executeQuery();
//			if (data.next()) {
//				new Money(player, data.getInt("balance"));
//			}
//			else {
//				try {
//					new Money(player, 0);
//					query = "INSERT INTO money(username, amount) VALUES ('" + name + "',0)";
//					connection.prepareStatement(query).executeUpdate();
//				}
//				catch (SQLException e) {
//					Logger.getLogger("Minecraft").info("Unable to generate money entry for: " + name);
//				}
//			}
//		}
//		catch (SQLException e) {
//			Logger.getLogger("Minecraft").info("Unable to retrieve money entry for: " + name);
//		}
//	}
//
//	@EventHandler
//	private void playerQuit(PlayerQuitEvent event) {
//		Player player = event.getPlayer();
//		String name = player.getName();
//		int balance = Money.getMoney(player).balance();
//		String update = "UPDATE money SET balance = ? WHERE username = ?";
//		try {
//			PreparedStatement statement = connection.prepareStatement(update);
//			statement.setInt(1, balance);
//			statement.setString(2, name);
//			statement.executeUpdate();
//		}
//		catch (SQLException e) {
//			Logger.getLogger("Unable to update money entry for: " + name);
//		}
//		Money.removeMoney(player);
//	}
//
//	@Override
//	public void actionPerformed(ActionEvent event) {
//		Logger.getLogger("Minecraft").info("Updating Exchange...");
//		try {
//			String statement = "UPDATE `exchange` SET `current supply`= ? WHERE `name`= ?";
//			PreparedStatement update = connection.prepareStatement(statement);
//			for (Item item : Item.getItems()) {
//				update.setInt(1, item.currentSupply());
//				update.setString(2, item.name());
//				update.addBatch();
//			}
//			update.executeBatch();
//			Logger.getLogger("Minecraft").info("Exchange update complete!");
//		}
//		catch (SQLException e) {
//			Logger.getLogger("Minecraft").info("Unable to update Exchange");
//		}
//	}
//
//	public void log() {
//		timer.start();
//	}
//}
