package industry;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import skills.Skill;

public class Mining extends Skill {

	public Mining(Player player, int level, int exp) {
		super(player, level, exp);
	}
	@Override
	public void info() {
		super.info();
		message(1, "Active: Instant mining for  " + (int) (2+3*level/1000) + " seconds");
		message(250, "Passive: Increased mining speed level " + (int) level/250 );
		message(500, "Level 500 Passive: " + (int) 30* level/1000 + "% chance of double drop");
	}

	public void active(Block block) {
		int activeDuration = 2 + 3 * level / 1000;
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

	public static boolean isMinable(Block block) {
		switch (block.getType()) {
		case STONE:
		case OBSIDIAN:
		case NETHERRACK:
		case DIAMOND_ORE:
		case ENDER_STONE:
		case IRON_ORE:
		case REDSTONE_ORE:
		case EMERALD_ORE:
		case COAL_ORE:
		case LAPIS_ORE:
		case GOLD_ORE:
			return true;
		default:
			return false;
		}
	}

	private int getExp(Block block) {
		switch (block.getType()) {
		case NETHERRACK:
			return 8;
		case ENDER_STONE:
			if (block.getBiome() == Biome.SKY)
				return 13;
			else
				return 0;
		case STONE:
		case DIAMOND_ORE:
		case IRON_ORE:
		case REDSTONE_ORE:
		case EMERALD_ORE:
		case COAL_ORE:
		case LAPIS_ORE:
		case GOLD_ORE:
			return 10;
		case OBSIDIAN:
			return 30;
		default:
			return 0;
		}
	}

	private ItemStack getDrop(Block block) {
		if (level >= 500) {
			double chance = .3 * level / 1000;
			boolean proc = new Random().nextDouble() <= chance;
			if (proc) {
				switch(block.getType()) {
				case DIAMOND_ORE:
				case EMERALD_ORE:
				case REDSTONE_ORE:
				case GOLD_ORE:
				case LAPIS_ORE:
					return block.getDrops().iterator().next();
				default:
					break;		
				}
			}
		}
		return null;
	}
}
