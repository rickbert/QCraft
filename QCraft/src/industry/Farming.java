package industry;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import skills.Skill;

public class Farming extends Skill {

	public Farming(UUID id) {
		super(id);
	}

	@Override
	public void info() {
		super.info();
		message(1, "Work in progress");
		message(250, "Passive: " + level/10 + "% chance for double drop");
		message(250, "Level 250 Passive: Crops grow faster");
		message(750, "Level 750 Passive: Extra food value from crops.");
	}

	private static void convertSeeds(Block block) {
		switch (block.getType()) {
		case COBBLESTONE:
			block.setType(Material.MOSSY_COBBLESTONE);
			break;
		case COBBLE_WALL:
			block.setData((byte) 1);
			break;
		case STONE:
			block.setData((byte) 1);
			break;
		case DIRT:
			block.setType(Material.GRASS);
			break;
		default:
			break;
		}
	}

	public void active(Block block) {
		double chance = new Random().nextDouble();
		Material item = player.get().getItemInHand().getType();
		switch (item) {
		case SEEDS:
			switch (block.getType()) {
			case COBBLESTONE:
			case DIRT:
			case STONE:
				reduceHand(player.get());
				if (chance <= 0.1) {
					convertSeeds(block);
				}
				else {
					block.setType(Material.AIR);
				}
			default:
				break;
			}
			break;
		case BROWN_MUSHROOM:
			if (block == null) {
				block = player.get().getTargetBlock(null, 5);
			}
			if (block.getType().equals(Material.DIRT) || block.getType().equals(Material.GRASS)) {
				reduceHand(player.get());
				if (chance <= 0.1) {
					block.setType(Material.MYCEL);
				}
				else {
					block.setType(Material.AIR);
				}
			}
		default:
			break;
		}
	}

	public void active(Entity entity) {		
		if (entity.getType().equals(EntityType.COW)) {
			if (player.get().getItemInHand().getType().equals(Material.RED_MUSHROOM)) {
				double chance = new Random().nextDouble();
				reduceHand(player.get());
				if (chance <= 0.1) {
					World world = entity.getWorld();
					Location spawn = entity.getLocation();
					world.spawnEntity(spawn, EntityType.MUSHROOM_COW);
				}
				else {
					((LivingEntity) entity).setHealth(0.0);
				}
			}
		}
	}

	private void reduceHand(Player player) {
		if (player.getItemInHand().getAmount() - 1 == 0) {
			player.setItemInHand(null);
		}
		else {
			int amount = player.getItemInHand().getAmount() - 1;
			player.getItemInHand().setAmount(amount);
		}
	}

	public void blockBreak(Block block) {
		addExp(getExp(block));
		double chance = level / 10;
		boolean proc = new Random().nextDouble() <= chance;
		if (proc) {
			World world = block.getWorld();
			Location location = block.getLocation();
			for(ItemStack drop : block.getDrops()) {
				world.dropItemNaturally(location, drop);
			}
		}
	}			

	public void blockPlace(Block block) {
		if (level >= 250) {
			switch (block.getType()) {
			case CROPS:
			case CARROT:
			case POTATO:
				block.setData((byte) 7);
				break;
			case NETHER_WARTS:
				block.setData((byte) 3);
				break;
			default:
				break;
			}

		}
	}

	public void itemConsume(ItemStack food) {
		if (level >= 750) {
			int hunger = player.get().getFoodLevel();
			float saturation = player.get().getSaturation();
			switch (food.getType()) {
			case CARROT_ITEM:
				player.get().setFoodLevel(hunger + 4);
				player.get().setSaturation(saturation + 4.8f);
				break;
			case POTATO_ITEM:
				player.get().setFoodLevel(hunger + 1);
				player.get().setSaturation(saturation + 0.6f);
				break;
			case BREAD:
				player.get().setFoodLevel(hunger + 5);
				player.get().setSaturation(saturation + 6f);
				break;
			default:
				break;			
			}
		}
	}

	public static boolean isCrop(Block block) {
		switch (block.getType()) {
		case CROPS:
		case NETHER_WARTS:
		case POTATO:
		case CARROT:
			return isGrown(block);
		case PUMPKIN:
		case MELON_BLOCK:		
		case SUGAR_CANE_BLOCK:
		case CACTUS:		
			return true;
		default:
			return false;
		}
	}

	private static boolean isGrown(Block block) {
		switch (block.getType()) {
		case CROPS:
		case POTATO:
		case CARROT:
			return block.getData() == 7;
		case NETHER_WARTS:
			return block.getData() == 3;
		default:
			return true;
		}
	}

	private int getExp(Block block) {
		switch (block.getType()) {
		case CROPS:
		case POTATO:
		case CARROT:
		case NETHER_WARTS:
		case MELON_BLOCK:
		case PUMPKIN:
			return 10;
		case CACTUS:
			return 10 * checkBlocks(block, Material.CACTUS);
		case SUGAR_CANE_BLOCK:
			return 5 * checkBlocks(block, Material.SUGAR_CANE_BLOCK);
		default:
			return 0;
		}
	}

	private int checkBlocks(Block block, Material material) {
		if (block.getType().equals(material)) {
			return 1 + checkBlocks(block.getRelative(0, 1, 0), material);
		}
		return 0;
	}

	@Override
	protected void loadSkillInfo() {
		// TODO Auto-generated method stub
		
	}
}
