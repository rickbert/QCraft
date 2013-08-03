package main;

import combat.CombatListener;
import industry.IndustryListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import skills.SkillCommands;
import util.ConfigLoader;
import util.PlayerUtil;

import java.util.logging.Level;

public class QCraft extends JavaPlugin implements Listener {
	private static QCraft instance;
	
	//Create our database data fetching class
	@Override
	public void onEnable() {
		instance = this;
		this.getServer().getPluginManager().registerEvents(this, this);
		ConfigLoader.load();
		new CombatListener(this);
		new IndustryListener(this);
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
				try {
					SkillCommands.processCommand(args, (Player) sender);
				}
				catch (Exception ignored) {}
			}
		}
		
		return false;
	}
	
	public void log(String message) {
		this.getLogger().log(Level.INFO, message);
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	private void loadPlayer(PlayerJoinEvent event) {
		PlayerUtil.loadPlayer(event.getPlayer());
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	private void savePlayer(PlayerQuitEvent event) {
		PlayerUtil.savePlayer(event.getPlayer());
	}
}
