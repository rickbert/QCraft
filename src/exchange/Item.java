package exchange;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class Item {
	private final String name;
	private final int id;
	private final byte dataValue;
	private final int baseValue;
	private final double supplyFactor;
	private int currentSupply;
	private final double profitFactor;
	private static final LinkedList<Item> items = new LinkedList<>();

	public Item(ResultSet query) throws SQLException {
		name = query.getString("name");
		id = query.getInt("id");
		dataValue = query.getByte("data value");
		baseValue = query.getInt("base value");
		supplyFactor = query.getDouble("supply factor");
		currentSupply = query.getInt("current supply");
		profitFactor = query.getDouble("profit factor");
		items.add(this);
	}
	
	public String name() {
		return name;
	}
	
	public int currentSupply() {
		return currentSupply;
	}
	
	public int buyPrice() {
		return (int) (baseValue * (0.5 + Math.exp(-currentSupply / supplyFactor) / 2) * (1 - profitFactor));
	}
	
	public int sellPrice() {
		return (int) (baseValue * (0.5 + Math.exp(-currentSupply / supplyFactor) / 2) * (1 + profitFactor));
	}
	
	public boolean increaseSupply(int quantity) {
		if (quantity > 0) {
			currentSupply = currentSupply + quantity;
			return true;
		}
		return false;
	}
	
	public boolean decreaseSupply(int quantity) {
		if (quantity > 0 && currentSupply - quantity >= 0) {
			currentSupply = currentSupply - quantity;
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return ChatColor.BLUE + name + ChatColor.YELLOW + ": " + ChatColor.GREEN + buyPrice()
				+ ChatColor.YELLOW + " / " + ChatColor.RED + sellPrice() + ChatColor.YELLOW
				+ " / " + ChatColor.WHITE + currentSupply;
	}
	
	public ItemStack getItemStack(int quantity) {
		return new ItemStack(id, quantity, dataValue);
	}
	
	public static Item getItem(String identifier) {
		for(Item item : items) {
			if (item.name().equals(identifier.toLowerCase())) {
				return item;
			}
		}
		return null;
	}
	
	public static LinkedList<Item> getItems() {
		return items;
	}
}
