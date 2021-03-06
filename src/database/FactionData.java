//package database;
//
//import factions.*;
//import main.QCraft;
//import org.bukkit.Bukkit;
//import org.bukkit.Chunk;
//import org.bukkit.Location;
//import org.bukkit.World;
//import org.bukkit.entity.Player;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.HashSet;
//import java.util.logging.Logger;
//
//public class FactionData {
//	private static final HashSet<Faction> factions = new HashSet<>();
//
//	public FactionData(QCraft plugin) {
//		new FactionListener(connection, plugin);
//		new FactionCommands(plugin);
//		try {
//			String factionQuery = "SELECT * FROM factions GROUP BY faction";
//			ResultSet factionData = connection.prepareStatement(factionQuery).executeQuery();
//			while (factionData.next()) {
//				Faction faction = new Faction(factionData.getString("faction"));
//
//				if (factionData.getString("land") != null) {
//					String[] allLand = factionData.getString("land").split(":");
//					for(String landInfo : allLand) {
//						String[] land = landInfo.split(",");
//						World world = Bukkit.getWorld(land[0]);
//						int x = Integer.parseInt(land[1]);
//						int z = Integer.parseInt(land[2]);
//						faction.addLand(new Land(world, x, z));
//					}
//				}
//
//				if (factionData.getString("home") != null) {
//					String[] home = factionData.getString("home").split(",");
//					World world = Bukkit.getWorld(home[0]);
//					int x = Integer.parseInt(home[1]);
//					int y = Integer.parseInt(home[2]);
//					int z = Integer.parseInt(home[3]);
//					faction.setHome(new Location(world, x, y, z));
//				}
//
//				faction.setBalance(factionData.getInt("balance"));
//
//
//				String playerQuery = "SELECT username, status FROM factions WHERE faction = ?";
//				PreparedStatement statement = connection.prepareStatement(playerQuery);
//				statement.setString(1, faction.name());
//				ResultSet playerData = statement.executeQuery();
//				while (playerData.next()) {
//					String player = playerData.getString("username");
//					Status status = Status.getStatus(playerData.getString("status"));
//					faction.addMember(player, status);
//				}
//
//				factions.add(faction);
//			}
//		}
//		catch (SQLException e) {
//			Logger.getLogger("Minecraft").info("Unable to load faction data");
//		}
//	}
//
//	public void quit() {
//		String update = "UPDATE factions SET land = ?, home = ?, balance = ?  WHERE faction = ?";
//		try {
//			PreparedStatement statement = connection.prepareStatement(update);
//			for(Faction faction : factions) {
//				statement.setString(1, faction.serializeLand());
//				statement.setString(2, faction.serializeHome());
//				statement.setInt(3, faction.getBalance());
//				statement.setString(4, faction.name());
//				statement.addBatch();
//			}
//			statement.executeBatch();
//		}
//		catch (SQLException e) {
//			Logger.getLogger("Minecraft").info("Unable to update factions");
//		}
//	}
//
//	public static boolean addFaction(Faction faction) {
//        return factions.add(faction);
//    }
//
//	public static boolean removeFaction(Faction faction) {
//        return factions.remove(faction);
//    }
//
//	public static Faction getFaction(String name) {
//		for (Faction faction : factions) {
//			if (faction.name().equals(name)) {
//				return faction;
//			}
//		}
//		return new Faction(null);
//	}
//
//	public static Faction getFaction(Player player) {
//		for(Faction faction : factions) {
//			for(String member : faction.getMembers()) {
//				if (player.getName().equals(member)) {
//					return faction;
//				}
//			}
//		}
//		return new Faction(null);
//	}
//
//	public static Faction getFaction(Chunk chunk) {
//		for(Faction faction : factions) {
//			for(Land land : faction.getLand()) {
//				if (land.getChunk().equals(chunk)) {
//					return faction;
//				}
//			}
//		}
//		return new Faction(null);
//	}
//
//	public static boolean inFaction(Player player) {
//		return getFaction(player).name() != null;
//	}
//
//	public static void disband(Faction faction) {
//		factions.remove(faction);
//	}
//}
