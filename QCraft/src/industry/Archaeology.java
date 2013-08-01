package industry;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import skills.Skill;

public class Archaeology extends Skill {
	private HashMap<Material, Integer> diggable = new HashMap<Material, Integer>();

	public Archaeology(Player player, int level, int exp) {
		super(player, level, exp);
		
		int defaultExp = skillConfig.getConfigurationSection("exp").getInt("default");
		for (String block : skillConfig.getConfigurationSection("blocks").getKeys(false)) {
			String expPath = "blocks." + block;
			int blockExp = skillConfig.getConfigurationSection(expPath).getInt("exp");
			Material material = Material.getMaterial(block);
			if (skillConfig.getConfigurationSection(path))
			
		}
	}

	@Override
	public void info() {
		super.info();
		message(1, "Active: Instant digging for " + (int) (5 + 5 * level/1000) + " seconds with double discovery chance");
		message(250, "Passive: Increased dig speed level " + (int) level/250 );
		message(1, "Level 1 Passive: Chance of discovery in Dirt");
		message(250, "Level 250 Passive: Chance of discovery in Sand");
		message(500, "Level 500 Passive: Chance of discovery in Soul Sand");
		message(750, "Level 750 Passive: Chance of discovery in Gravel");
	}

	public void active(Block block) {
		int activeDuration = 5 + 5 * level / 1000;
		switch (active.getState()) {
		case PRIMED:
			active.activate(activeDuration, 300);
			break;
		case ACTIVE:
			block.breakNaturally();
			break;
		default:
			break;
		}
	}
	
	public void blockBreak(Block block) {
		addExp(getExp(block));
		ItemStack drop = getDrop(block);
		if (drop != null) {
			World world = block.getWorld();
			Location location = block.getLocation();
			world.dropItemNaturally(location, drop);	
		}
	}

	public static boolean isDiggable(Block block) {
		switch (block.getType()) {
		case GRASS:
		case SAND:
		case GRAVEL:
		case SOUL_SAND:
		case DIRT:
			return true;
		default:
			return false;
		}
	}

	private int getExp(Block block) {
		switch (block.getType()) {
		case SAND:
		case GRAVEL:
		case GRASS:
		case DIRT:
		case SOUL_SAND:
			return 10;
		default:
			return 0;
		}
	}
	
	private ItemStack getDrop(Block block) {
		double chance = new Random().nextDouble();
		switch (block.getType()) {
		case DIRT:
			if (level >= 1) {
				if (chance <= 0.025) {
					return new ItemStack(Material.GLOWSTONE_DUST);
				}
			}
			break;
		case SAND:
			if (level >= 250) {
				if (chance <= 0.025) {
					return new ItemStack(Material.SULPHUR);
				}
			}
		case SOUL_SAND:
			if (level >= 500) {
				if (chance <= 0.05) {
					return new ItemStack(Material.NETHER_WARTS);
				}
			}
			break;
		case GRAVEL:
			if (level >= 750) {
				if (chance <= 0.01) {
					return new ItemStack(Material.DIAMOND);
				}				
			}
			break;
		default:
			break;
		}
		return null;
	}
}