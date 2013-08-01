package industry;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Drop {
	private final Material material;
	private final int level;
	private final double chance;
	private final int exp;
	
	public Drop(Material material, int level, double chance, int exp) {
		this.material = material;
		this.level = level;
		this.chance = chance / 100.0;
		this.exp = exp;
	}
	
	public boolean success(int levelCheck, double roll) {
		return level >= levelCheck && roll <= chance;
	}
	
	public ItemStack getItem() {
		return new ItemStack(material);
	}
	
	public int getExp() {
		return exp;
	}
}
