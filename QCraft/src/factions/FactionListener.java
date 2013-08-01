package factions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import main.QCraft;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import qplayer.Power;
import sql.FactionData;

public class FactionListener implements Listener {
	private final FactionData factionData;

	public FactionListener(QCraft plugin) {
		factionData = new FactionData(plugin);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public static int getPower(String player) {
		return power.containsKey(player)? power.get(player).getPower() : 0;
	}

	@EventHandler
	private void playerLogin(PlayerJoinEvent event) {
		if (FactionData.inFaction(event.getPlayer())) {
			String query = "SELECT power FROM factions WHERE username = ?";
			try {
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setString(1, event.getPlayer().getName());
				ResultSet data = statement.executeQuery();
				int playerPower = 0;
				if (data.next()) {
					playerPower = data.getInt("power");
				}
				power.put(event.getPlayer().getName(), new Power(event.getPlayer(), playerPower));
			} 
			catch (SQLException e) {
				Logger.getLogger("Minecraft").info("Unable to get Power for " + event.getPlayer().getName());
			}
		}
	}

	@EventHandler
	private void playerQuit(PlayerQuitEvent event) {
		if (FactionData.inFaction(event.getPlayer())) {
			String update= "UPDATE factions SET power = ? WHERE username = ?";
			try {
				PreparedStatement statement = connection.prepareStatement(update);
				statement.setInt(1, power.get(event.getPlayer().getName()).getPower());
				statement.setString(2, event.getPlayer().getName());
				statement.executeUpdate();
			}
			catch (SQLException e) {
				Logger.getLogger("Minecraft").info("Unable to update Power for " + event.getPlayer().getName());
			}
			power.remove(event.getPlayer());
		}
	}

	@EventHandler
	private void playerDied(PlayerDeathEvent event) {
		if (FactionData.inFaction(event.getEntity())) {
			if (event.getEntity().getKiller() != null) {
				power.get(event.getEntity().getName()).lowerPower(5);
			}
			else {
				power.get(event.getEntity().getName()).lowerPower(2);
			}
		}
	}

	@EventHandler (priority = EventPriority.LOWEST)
	private void friendlyFire(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (!(FactionData.inFaction(player))) {
				return;
			}
			Faction faction = FactionData.getFaction((Player) event.getEntity());
			Entity damager = event.getDamager();
			if (damager instanceof Player) {
				if (FactionData.getFaction((Player) damager).equals(faction)) {
					event.setCancelled(true);
				}
			}
			else if (damager instanceof Arrow) {
				if (((Arrow) damager).getShooter() instanceof Player) {
					LivingEntity shooter = ((Arrow) damager).getShooter();
					if (FactionData.getFaction((Player) shooter).equals(faction)) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler (priority = EventPriority.LOWEST)
	private void blockDamage(BlockDamageEvent event) {
		Player player = event.getPlayer();
		Chunk chunk = event.getBlock().getChunk();
		Faction playerFaction = FactionData.getFaction(player);
		Faction landFaction = FactionData.getFaction(chunk);
		if (!(playerFaction.equals(landFaction))) {
			event.setCancelled(true);
		}
	}

	@EventHandler (priority = EventPriority.LOWEST)
	private void blockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Chunk chunk = event.getBlock().getChunk();
		Faction playerFaction = FactionData.getFaction(player);
		Faction landFaction = FactionData.getFaction(chunk);
		if (!(playerFaction.equals(landFaction))) {
			event.setCancelled(true);
		}
	}

	//	@EventHandler (priority = EventPriority.LOWEST)
	//	private void playerInteract(PlayerInteractEvent event) {
	//		Player player = event.getPlayer();
	//		Chunk chunk = event.getClickedBlock().getChunk();
	//		Faction playerFaction = FactionHandler.getFaction(player);
	//		Faction landFaction = FactionHandler.getFaction(chunk);
	//		if (playerFaction != landFaction) {
	//			event.setCancelled(true);
	//		}
	//	}
}
