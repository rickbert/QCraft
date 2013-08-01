package industry;

import java.util.HashSet;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import skills.Skill;
import skills.Active.ActiveState;

public class Woodcutting extends Skill { 

	private enum Wood {
		OAK, BIRCH, SPRUCE, JUNGLE;
	}

	public Woodcutting(Player player, int level, int exp) {
		super(player, level, exp);
	}

	@Override
	public void info() {
		super.info();
		message(1, "Active: Fell a single tree up to  " + (int) (10 + level / 25) + " logs or " + (int) (10 + 3*level/20) + " jungle logs");
		message(250, "Passive: Increased chopping speed level " + (int) level/250 );
		message(250, "Level 250 Passive: Instant leave breaking");
		message(750, "Level 750 Passive: Chance to get an apple or sappling when breaking a leaf");
	}

	public void active(Block block) {
		if (block.getType().equals(Material.LEAVES)) {
			if (level >= 250) {
				block.breakNaturally();
			}
			if (level >= 750) {
				if (getDrop(block) != null) {
					World world = block.getWorld();
					world.dropItemNaturally(block.getLocation(), getDrop(block));
				}
			}
		}
	}

	public void treeFeller(Block block) {
		if (active.getState().equals(ActiveState.READY)) {
			HashSet<Block> tree = new HashSet<Block>();		
			tree = markTree(tree, block);
			switch (tree.iterator().next().getData()) {
			case 0:
			case 1:
			case 2:
				if (tree.size() <= 10 + level / 25) {
					for (Block log : tree) {
						log.breakNaturally();
						addExp(20);
					}
					player.get().sendMessage("Tree felled!");
				}
				else {
					player.get().sendMessage("Tree too big for Tree Feller!");
				}
				break;
			case 3:
				if (tree.size() <= 10 + level * 3 / 20) {
					for (Block log : tree) {
						log.breakNaturally();
						addExp(10);
					}
					player.get().sendMessage("Tree felled!");
				}
				else {
					player.get().sendMessage("Tree too big for Tree Feller!");
				}
				break;
			default:
				break;			
			}
		}
		else {
			player.get().sendMessage("Woodcutting active on cooldown");
		}
	}

	private HashSet<Block> markTree(HashSet<Block> tree, Block block) {
		if (block.getType().equals(Material.LOG) && !tree.contains(block)) {
			tree.add(block);
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					for (int z = -1; z <= 1; z++) {
						markTree(tree, block.getRelative(x, y, z));
					}
				}
			}
		} 
		return tree;
	}

	public void blockBreak(Block block) {
		if (block.getType().equals(Material.LOG)) {
			switch (getWoodType(block)) {
			case OAK:
			case SPRUCE:
			case BIRCH:
				addExp(20);
				break;
			case JUNGLE:
				addExp(10);
				break;
			}	
		}
	}

	public static boolean isLoggable(Block block) {
		return block.getType().equals(Material.LOG) || block.getType().equals(Material.LEAVES);
	}

	public static Wood getWoodType(Block block) {
		switch (block.getData()) {
		case 0:
		case 4:
		case 8:
		case 12:
		default:
			return Wood.OAK;
		case 1:
		case 5:
		case 9:
		case 13:
			return Wood.SPRUCE;
		case 2:
		case 6:
		case 10:
		case 14:
			return Wood.BIRCH;
		case 3:
		case 7:
		case 11:
		case 15:
			return Wood.JUNGLE;
		}
	}

	private ItemStack getDrop(Block block) {
		double chance = new Random().nextDouble();
		switch (block.getData()) {
		case 0:
			if (chance <= 0.005) {
				return new ItemStack(Material.APPLE);
			}
		case 1:
		case 2:
			if (chance <= 0.05) {
				return new ItemStack(Material.SAPLING, 1, block.getData());
			}
			break;
		case 3:
			if (chance <= 0.025) {
				return new ItemStack(Material.SAPLING, 1, block.getData());
			}
			break;
		default:
			break;
		}
		return null;
	}
}