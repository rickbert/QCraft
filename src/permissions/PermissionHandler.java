package permissions;

import main.QCraft;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;

class PermissionHandler implements Listener {
	private static HashMap<Player, PermissionAttachment> permissions;
	private final QCraft plugin;
	
	public PermissionHandler(QCraft plugin) {
		this.plugin = plugin;
		permissions = new HashMap<>();
	}
	
	@EventHandler
	private void playerJoin(PlayerJoinEvent event) {
		permissions.put(event.getPlayer(), event.getPlayer().addAttachment(plugin));
	}
	
	@EventHandler
	private void playerQuit(PlayerQuitEvent event) {
		permissions.remove(event.getPlayer());
	}
	
	public static void addPermission(Player player, String permission) {
		permissions.get(player).setPermission(permission, true);
	}
	
	public static void removePermission(Player player, String permission) {
		permissions.get(player).unsetPermission(permission);
	}

}
