package util;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import qplayer.QPlayer;

public class PlayerUtil {
	private static HashMap<UUID, Player> players = new HashMap<UUID, Player>();
	private static HashMap<UUID, QPlayer> qplayers = new HashMap<UUID, QPlayer>();
	
	public static void loadPlayer(Player player) {
		players.put(player.getUniqueId(), player);
		qplayers.put(player.getUniqueId(), new QPlayer(player));
	}
	
	public static void savePlayer(Player player) {
		players.remove(player.getUniqueId());
		qplayers.remove(player.getUniqueId());
	}
	
	public static Player getPlayer(UUID id) {
		return players.get(id);
	}
	
	public static QPlayer getQPlayer(UUID id) {
		return qplayers.get(id);
	}
	
	public static QPlayer getQPlayer(Player player) {
		return qplayers.get(player.getUniqueId());
	}
	
	public static void message(UUID id, String message) {
		if (players.containsKey(id)) {
			players.get(id).sendMessage(message);
		}
	}
	
	public static void reload() {
		players.clear();
		for (Player player : Bukkit.getOnlinePlayers()) {
			players.put(player.getUniqueId(), player);
		}
	}
}
