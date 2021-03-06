//package exchange;
//
//import main.QCraft;
//import org.bukkit.Bukkit;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandExecutor;
//import org.bukkit.command.CommandSender;
//import org.bukkit.conversations.*;
//import org.bukkit.entity.Player;
//import org.bukkit.event.inventory.InventoryType;
//import org.bukkit.inventory.Inventory;
//import qplayer.Money;
//
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.logging.Logger;
//
//public class Exchange implements CommandExecutor, ConversationAbandonedListener {
//	private final ConversationFactory factory;
//
//	public Exchange(QCraft plugin) {
//		factory = new ConversationFactory(plugin)
//		.withModality(false)
//		.withFirstPrompt(new exchangePrompt())
//		.withEscapeSequence("/exit")
//		.withTimeout(30)
//		.addConversationAbandonedListener(this);
//		plugin.getCommand("qconomy").setExecutor(this);
//	}
//
//	@Override
//	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//		if (command.getName().equalsIgnoreCase("qconomy")) {
//			switch (args[0].toLowerCase()) {
//			case "money":
//			case "balance":
//				if (sender instanceof Player) {
//
//                }
//				else {
//					Player player = Bukkit.getPlayer(args[1]);
//				}
//				break;
//			case "pay":
//				if (!(sender instanceof Player)) {
//					Player player = Bukkit.getPlayer(args[1]);
//				}
//				break;
//			case "exchange":
//				if (sender instanceof Conversable) {
//					factory.buildConversation((Conversable)sender).begin();
//					return true;
//				}
//				break;
//			default:
//				break;
//			}
//		}
//		return false;
//	}
//
//	@Override
//	public void conversationAbandoned(ConversationAbandonedEvent event) {
//		if (event.getContext().getSessionData("reset") != null) {
//			Item item = (Item) event.getContext().getSessionData("item");
//			int quantity = (int) event.getContext().getSessionData("reset");
//			item.increaseSupply(quantity);
//		}
//		event.getContext().getForWhom().sendRawMessage("Exited out of the Exchange");
//	}
//
//	private String[] convertItems() {
//		Collection<Item> items = Item.getItems();
//		String[] array = new String[items.size()];
//		Iterator<Item> iterator = items.iterator();
//		for(int i = 0; i < array.length; i++) {
//			array[i] = iterator.next().name().toLowerCase();
//		}
//		return array;
//	}
//
//	private class exchangePrompt extends FixedSetPrompt {
//
//		public exchangePrompt() {
//			super(convertItems());
//		}
//
//		@Override
//		public String getPromptText(ConversationContext context) {
//			if (context.getSessionData("reset") != null) {
//				Item item = (Item) context.getSessionData("item");
//				int quantity = (int) context.getSessionData("reset");
//				item.increaseSupply(quantity);
//				context.setSessionData("reset", 0);
//			}
//			String info = "\n" + ChatColor.BLUE + "Item" + ChatColor.YELLOW + ": "
//					+ ChatColor.GREEN + "Buy Price " + ChatColor.YELLOW
//					+ "/ " + ChatColor.RED + "Sell Price"
//					+ ChatColor.YELLOW + " / " + ChatColor.WHITE + "Current Supply";
//			for(Item item: Item.getItems()){
//				info = info + "\n" + item.toString();
//			}
//			return "Welcome to the Server Exchange!" + info;
//		}
//
//		@Override
//		protected boolean isInputValid(ConversationContext context, String input) {
//			input = input.toLowerCase();
//			if (input.equals("/balance") || input.equals("/money") || input.equals("/cancel")) {
//				return true;
//			}
//
//			String[] query = input.split(" ");
//			if (query.length != 3) {
//				context.setSessionData("error", "arguments length");
//				return false;
//			}
//
//			boolean validCommand = query[0].equals("/buy") || query[0].equals("/sell");
//			if (!validCommand) {
//				context.setSessionData("error", "invalid command");
//				return false;
//			}
//
//			boolean validItem = fixedSet.contains(query[1]);
//			if (!validItem) {
//				context.setSessionData("error", "invalid item");
//				return false;
//			}
//
//			boolean validQuantity = false;
//			try {
//				validQuantity = Integer.parseInt(query[2]) > 0;
//				if (!validQuantity) {
//					context.setSessionData("error", "quantity below 1");
//					return false;
//				}
//			}
//			catch (NumberFormatException e) {
//				context.setSessionData("error", "number format");
//				return false;
//			}
//
//			Player player = (Player) context.getForWhom();
//			Item item = Item.getItem(query[1]);
//			int quantity = Integer.parseInt(query[2]);
//
//			switch (query[0]) {
//			case "/buy":
//				if (quantity > item.currentSupply()) {
//					context.setSessionData("error", "not enough supply");
//					return false;
//				}
//
//				int price = quantity * item.buyPrice();
//				if (price > 0) {
//					context.setSessionData("error", "not enough money");
//					return false;
//				}
//
//				Inventory check = Bukkit.createInventory(null, InventoryType.PLAYER);
//				check.setContents(player.getInventory().getContents());
//				if (!check.addItem(item.getItemStack(quantity)).isEmpty()) {
//					context.setSessionData("error", "inventory space");
//					return false;
//				}
//				break;
//			case "/sell":
//				if (!player.getInventory().containsAtLeast(item.getItemStack(0), quantity)) {
//					context.setSessionData("error", "invalid sale");
//					return false;
//				}
//				break;
//			}
//
//			return validCommand && validItem && validQuantity;
//		}
//
//		@Override
//		protected Prompt acceptValidatedInput(ConversationContext context, String input) {
//			input = input.toLowerCase();
//			if (input.equals("/cancel")) {
//				return Prompt.END_OF_CONVERSATION;
//			}
//			else if (input.equals("/balance") || input.equals("/money")) {
//				Player player = (Player) context.getForWhom();
//				return this;
//			}
//
//			String[] query = input.split(" ");
//			Item item = Item.getItem(query[1]);
//			context.setSessionData("item", item);
//			int quantity = Integer.parseInt(query[2]);
//			context.setSessionData("quantity", quantity);
//
//			switch (query[0]) {
//			case "/buy":
//				context.setSessionData("type", "buy");
//				context.setSessionData("price", item.buyPrice() * quantity);
//				context.setSessionData("reset", quantity);
//				item.decreaseSupply(quantity);
//				break;
//			case "/sell":
//				context.setSessionData("type", "sell");
//				context.setSessionData("price", item.sellPrice() * quantity);
//				break;
//			}
//
//			return new transactionPrompt();
//		}
//
//		@Override
//		protected String getFailedValidationText(ConversationContext context, String input) {
//			String error = (String) context.getSessionData("error");
//			switch (error) {
//			case "arguments length":
//				return "You did not type all parameters for a sale (Missing /buy or /sell, an item, or a quantity)";
//			case "invalid command":
//				return "You did not type /buy, /sell, or cancel";
//			case "invalid item":
//				return "Not a valid item";
//			case "quantity below 1":
//				return "You must input a quantity of at least 1";
//			case "number format":
//				return "You did not input a number";
//			case "not enough money":
//				return "You do not have enough money";
//			case "inventory space":
//				return "You do not have enough space in your inventory";
//			case "not enough supply":
//				return "The Exchange does not have that quantity to buy";
//			case "invalid sale":
//				return "You do not have the necessary items to sell";
//			default:
//				return "";
//			}
//		}
//	}
//
//	private class transactionPrompt extends FixedSetPrompt {
//
//		public transactionPrompt() {
//			super("yes", "no", "cancel");
//		}
//
//		@Override
//		public String getPromptText(ConversationContext context) {
//			String type = (String) context.getSessionData("type");
//			Item item = (Item) context.getSessionData("item");
//			int quantity = (int) context.getSessionData("quantity");
//			int price = (int) context.getSessionData("price");
//			String transaction = type + " " + quantity + " " + item.name();
//			transaction = transaction + (quantity > 1? "s" : "");
//			return "Do you wish to " + transaction + " for $" + price + "?";
//		}
//
//		@Override
//		public boolean isInputValid(ConversationContext context, String input) {
//			input = input.toLowerCase();
//			switch (input) {
//			case "/y":
//			case "/yes":
//			case "/n":
//			case "/no":
//			case "/cancel":
//				return true;
//			default:
//				return false;
//			}
//		}
//
//		@Override
//		protected Prompt acceptValidatedInput(ConversationContext context, String input) {
//			Player player = (Player) context.getForWhom();
//			Item item = (Item) context.getSessionData("item");
//			int quantity = (int) context.getSessionData("quantity");
//			Money money = Money.getMoney(player);
//			String type = (String) context.getSessionData("type");
//			int price = (int) context.getSessionData("price");
//			switch (input.toLowerCase()) {
//			case "/y":
//			case "/yes":
//				if (type.equals("buy")) {
//					money.withdraw(price);
//					player.getInventory().addItem(item.getItemStack(quantity));
//					context.setSessionData("reset", 0);
//				}
//				else if (type.equals("sell")) {
//					money.deposit(price);
//					player.getInventory().removeItem(item.getItemStack(quantity));
//					item.increaseSupply(quantity);
//				}
//				player.sendMessage(Chat.GREEN + "Sale successful!");
//				player.sendMessage(Chat.GREEN + "Your balance is now " + money.toString());
//				return new exchangePrompt();
//			case "/n":
//			case "/no":
//			case "/cancel":
//				return new exchangePrompt();
//			default:
//				return Prompt.END_OF_CONVERSATION;
//			}
//		}
//	}
//}