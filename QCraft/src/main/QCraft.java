package main;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import qplayer.PlayerParser;
import qplayer.QPlayer;
import skills.SkillsCommands;

public class QCraft extends JavaPlugin implements Listener {
	private static QCraft instance;
	private static HashMap<Player, QPlayer> players;
	private ConfigLoader configLoader;
	
	//Create our database data fetching class
	@Override
	public void onEnable() {
		instance = this;
		players = new HashMap<Player, QPlayer>();
		this.getServer().getPluginManager().registerEvents(this, this);
		configLoader = new ConfigLoader();
	}

	//Clean up and update our data
	@Override
	public void onDisable() {
	}
	
	public static QCraft get() {
		return instance;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (command.getName().toLowerCase()) {
		case "qskills":
			if (sender instanceof Player) {
				SkillsCommands.processCommand(args, (Player) sender);
			}
		}
		
		return false;
	}
	
	public void log(String message) {
		this.getLogger().log(Level.INFO, message);
	}
	
	public static QPlayer getQPlayer(Player player) {
		return players.get(player);
	}
	
	public static void cleanupPlayer(QPlayer player) {
		for (Entry<Player, QPlayer> entry : players.entrySet()) {
			if (entry.getValue().equals(player)) {
				players.remove(entry.getKey());
			}
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	private void loadPlayer(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		players.put(player, PlayerParser.loadPlayer(player));
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	private void savePlayer(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		players.get(player);
		players.remove(player);
	}
}
