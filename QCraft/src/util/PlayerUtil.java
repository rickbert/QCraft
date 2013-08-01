package util;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerUtil {
	private static HashMap<UUID, Player> players = new HashMap<UUID, Player>();
	
	public static void loadPlayer(Player player) {
		players.put(player.getUniqueId(), player);
	}
	
	public static void savePlayer(Player player) {
		players.remove(player.getUniqueId());
	}
	
	public static Player getPlayer(UUID id) {
		return players.get(id);
	}
	
	public static void reload() {
		players.clear();
		for (Player player : Bukkit.getOnlinePlayers()) {
			players.put(player.getUniqueId(), player);
		}
	}
}
