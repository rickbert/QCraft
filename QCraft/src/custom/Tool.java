package custom;

import org.bukkit.inventory.ItemStack;

public enum Tool {
	AXE, HOE, NONE, PICKAXE, SPADE, SWORD, FISHING_ROD, OTHER;
	
	public static Tool getTool(ItemStack item) {
		String tool = item.getType().name();
		if (tool.contains("_AXE")) {
			return AXE;
		}
		else if (tool.contains("_HOE")) {
			return HOE;
		}
		else if (tool.contains("AIR")) {
			return NONE;
		}
		else if (tool.contains("_PICKAXE")) {
			return PICKAXE;
		}
		else if (tool.contains("_SPADE")) {
			return SPADE;
		}
		else if (tool.contains("_SWORD")) {
			return SWORD;
		}
		else if (tool.contains("FISHING_ROD")) {
			return FISHING_ROD;
		}
		else {
			return OTHER;
		}		
	}
}
