//package factions;
//
//import database.FactionData;
//import main.QCraft;
//import org.bukkit.Bukkit;
//import org.bukkit.Chunk;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandExecutor;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//import qplayer.Money;
//
//public class FactionCommands implements CommandExecutor {
//
//	public FactionCommands(QCraft plugin) {
//		plugin.getCommand("qfactions").setExecutor(this);
//	}
//
//	@Override
//	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//		if (sender instanceof Player) {
//			if (command.getName().equalsIgnoreCase("qfactions")) {
//				Player player = (Player) sender;
//
//				switch (args[0].toLowerCase()) {
//				case "faction":
//					Faction faction = new Faction(null);
//					if (args.length == 2) {
//						faction = FactionData.getFaction(args[1]);
//						if (faction.name() == null) {
//							player.sendMessage("Faction does not exist");
//							return true;
//						}
//					}
//					else if (args.length == 1){
//						faction = FactionData.getFaction(player);
//						if (faction.name() == null) {
//							player.sendMessage("You are not in a faction!");
//							return true;
//						}
//					}
//					player.sendMessage(faction.name());
//					player.sendMessage("Power: " + faction.getPower() + " / " + faction.getMembers().size() * 10);
//					player.sendMessage((faction.getPower() >= faction.getLand().size()? ChatColor.GREEN : ChatColor.RED)
//										+ "Land Owned: " + faction.getLand().size() + " / "
//										+ faction.getMembers().size() * 10);
//					player.sendMessage("Balance: " + faction.getBalance());
//					player.sendMessage("Members:");
//					String online = ChatColor.GREEN + "Online: ";
//					String offline = ChatColor.RED + "Offline: ";
//					for (String member : faction.getMembers()) {
//						if (Bukkit.getPlayer(member).isOnline()) {
//							online = online + member + ", ";
//						}
//						else {
//							offline = offline + member + ", ";
//						}
//					}
//					online = online.substring(0, online.length() - 2);
//					offline = offline.substring(0, offline.length() - 2);
//					player.sendMessage(online);
//					player.sendMessage(offline);
//					return true;
//				case "bank":
//					if (FactionData.inFaction(player)) {
//						Faction ownFaction = FactionData.getFaction(player);
//						if (args.length == 2) {
//							int amount = Integer.parseInt(args[1]);
//							int balance = ownFaction.getBalance();
//							Money money = Money.getMoney(player);
//							if (money.withdraw(amount)) {
//								ownFaction.setBalance(balance + amount);
//								player.sendMessage("You have deposited " + amount + " in the faction bank");
//								player.sendMessage("Your balance: " + money.balance());
//							}
//							else {
//								player.sendMessage("You do not have enough money");
//							}
//						}
//						player.sendMessage("Faction balance: " + ownFaction.getBalance());
//
//					}
//					else {
//						player.sendMessage("You are not in a faction!");
//					}
//					return true;
//				case "make":
//					if (!FactionData.inFaction(player) && args.length == 2) {
//						Faction newFaction = new Faction(args[1]);
//						newFaction.addMember(player.getName(), Status.ADMIN);
//						FactionData.addFaction(newFaction);
//						player.sendMessage("Faction " + args[1] + " created!");
//					}
//					return true;
//				case "claim":
//					Chunk chunk = player.getLocation().getChunk();
//					Faction playerFaction = FactionData.getFaction(player);
//					Faction landFaction = FactionData.getFaction(chunk);
//					if (landFaction.name() == null) {
//						if (playerFaction.getPower() > playerFaction.getLand().size()) {
//							playerFaction.addLand(new Land(chunk));
//						}
//					}
//					return true;
//				case "leave":
//					if (FactionData.inFaction(player)) {
//						Faction oldFaction = FactionData.getFaction(player);
//						oldFaction.removeMember(player);
//					}
//				}
//			}
//		}
//		return false;
//	}
//
//}
