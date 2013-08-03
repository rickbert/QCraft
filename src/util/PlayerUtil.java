package util;

import main.QCraft;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import qplayer.QPlayer;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerUtil {
	private static final HashMap<UUID, Player> players = new HashMap<>();
	private static final HashMap<UUID, QPlayer> qplayers = new HashMap<>();
	
	public static void loadPlayer(Player player) {
		players.put(player.getUniqueId(), player);
		try {
			qplayers.put(player.getUniqueId(), new QPlayer(player));
		} catch (IOException e) {
			QCraft.get().log(e.toString());
		}
	}
	
	public static void savePlayer(Player player) {
		try {
			qplayers.get(player.getUniqueId()).save();
		} catch (IOException e) {
			QCraft.get().log(e.toString());
		}
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
		for (Player player : Bukkit.getOnlinePlayers()) {
			savePlayer(player);
		}
		players.clear();
		qplayers.clear();
		for (Player player : Bukkit.getOnlinePlayers()) {
			loadPlayer(player);
		}
	}
}
