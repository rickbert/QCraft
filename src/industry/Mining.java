package industry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import skills.Active.ActiveState;
import skills.Skill;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class Mining extends Skill {
	private static final HashMap<Material, Integer> minable = new HashMap<>();
	private static final HashMap<Material, Drop> drops = new HashMap<>();
	private static final HashMap<Material, Integer> bonus = new HashMap<>();
	private static YamlConfiguration skillConfig;

	public Mining(UUID id, YamlConfiguration playerConfig) {
		super(id, playerConfig);
	}
	
	@Override
	public void info() {
		super.info();
		message(1, "Active: Instant mining for  " + (int) (2 + 3 * level / 1000.0) + " seconds");
		message(250, "Passive: Increased mining speed level " + (level / 250));
		message(500, "Level 500 Passive: " + (int) (30 * level / 1000.0) + "% chance of double drop");
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
		
		Material type = block.getType();
		if (bonus.containsKey(type)) {
			addExp(bonus.get(type) * block.getDrops().size());
		}
		if (drops.containsKey(type)) {
			double roll = new Random().nextDouble() * (1000 / (double) level);
			if (active.getState().equals(ActiveState.ACTIVE)) {
				roll = roll  - 0.1;
			}
			Drop drop = drops.get(type);
			if (drop.success(level, roll)) {
				World world = block.getWorld();
				Location location = block.getLocation();
				ItemStack item = drop.getItem();
				item.setAmount(block.getDrops().size() * drop.getMultiplier());
				world.dropItemNaturally(location, item);
				addExp(drop.getExp() * item.getAmount());
			}
		}			
	}

    public int buff() {
        return -1 + Math.min(level / 250, 2);
    }

	public static boolean isMinable(Block block) {
		return minable.containsKey(block.getType());
	}

	private int getExp(Block block) {
		return minable.containsKey(block.getType())? minable.get(block.getType()) : 0;
	}

	@Override
	protected void loadSkillInfo() {
//		if (skillConfig == null) {
//			File skillFile = new File(QCraft.get().getDataFolder(), "Skills/mining.yml");
//			skillConfig = YamlConfiguration.loadConfiguration(skillFile);
//			ConfigurationSection blocks = skillConfig.getConfigurationSection("blocks");
//			for (String block : blocks.getKeys(false)) {
//				try {
//					int exp = Math.max(skillConfig.getInt("exp.default"), blocks.getInt(block + ".exp"));
//					minable.put(Material.getMaterial(block), exp);
//				}
//				finally {}
//				
//				try {
//					Material material = Material.getMaterial(block);
//					int bonusExp = blocks.getInt(block + ".bonus");
//					bonus.put(material, bonusExp);					
//				}
//				finally {}
//
//				try {
//					ConfigurationSection dropSection = blocks.getConfigurationSection(block + ".drop");
//					Material material = Material.getMaterial(dropSection.getString("item"));
//					int level = dropSection.getInt("level");
//					double chance = dropSection.getDouble("chance");				
//					int dropExp = Math.max(skillConfig.getInt("exp.drops"), dropSection.getInt("exp"));
//					drops.put(material, new Drop(material, level, chance, dropExp));
//				}
//				finally {}
//			}
//		}		
	}
}
