package permissions;

import main.QCraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Admin implements CommandExecutor{
	public Admin(QCraft plugin) {
		plugin.getCommand("zap").setExecutor(this);
		plugin.getCommand("tnt").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (command.getName().equalsIgnoreCase("zap")) {
				if (player.getName().equals("qsik777")) {
					Bukkit.getPlayer(args[0]).setHealth(0.0);
				}
			}
			if (command.getName().equalsIgnoreCase("tnt")) {
				if (player.isOp()) {
					for (int y = -48; y <= 48; y++) {
						for (int x = -48; x <= 48; x++) {
							for (int z = -48; z <= 48; z++) {
								Location root = player.getLocation();
								if (root.getBlock().getRelative(x, y, z).getType().equals(Material.LEAVES)) {
									root.getBlock().getRelative(x, y, z).setType(Material.TNT);
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

}
