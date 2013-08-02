package industry;

import java.io.File;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import main.QCraft;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import skills.Skill;
import skills.Active.ActiveState;

public class Archaeology extends Skill {
	private static HashMap<Material, Integer> diggable = new HashMap<Material, Integer>();
	private static HashMap<Material, Drop> drops = new HashMap<Material, Drop>();
	private static YamlConfiguration skillConfig;

	public Archaeology(UUID id) {
		super(id);	
	}

	@Override
	public void info() {
		super.info();
		message(1, "Active: Instant digging for " + (int) (5 + 5 * level / 1000) + " seconds with double discovery chance");
		message(250, "Passive: Increased dig speed level " + (int) (level / 250));
		message(1, "Level 1 Passive: Chance of discovery in Dirt");
		message(250, "Level 250 Passive: Chance of discovery in Sand");
		message(500, "Level 500 Passive: Chance of discovery in Soul Sand");
		message(750, "Level 750 Passive: Chance of discovery in Gravel");
	}

	public void beginDigging(Block block) {
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

	public void endDigging(Block block) {
		addExp(getExp(block));
		
		Material type = block.getType();
		if (drops.containsKey(type)) {
			double roll = new Random().nextDouble();
			if (active.getState().equals(ActiveState.ACTIVE)) {
				roll = roll / 2.0;
			}
			Drop drop = drops.get(type);
			if (drop.success(level, roll)) {
				World world = block.getWorld();
				Location location = block.getLocation();
				world.dropItemNaturally(location, drop.getItem());
				addExp(drop.getExp());
			}
		}
	}

	public static boolean isDiggable(Block block) {
		return diggable.containsKey(block.getType());
	}

	private int getExp(Block block) {
		return diggable.containsKey(block.getType())? diggable.get(block.getType()) : 0;
	}

	@Override
	protected void loadSkillInfo() {
		if (skillConfig == null) {
			File skillFile = new File(QCraft.get().getDataFolder(), "Skills/archaeology.yml");
			skillConfig = YamlConfiguration.loadConfiguration(skillFile);
			ConfigurationSection blocks = skillConfig.getConfigurationSection("blocks");
			for (String block : blocks.getKeys(false)) {
				try {
					int exp = Math.max(skillConfig.getInt("exp.default"), blocks.getInt(block + ".exp"));
					diggable.put(Material.getMaterial(block), exp);
				}
				finally {}

				try {
					ConfigurationSection dropSection = blocks.getConfigurationSection(block + ".drop");
					Material material = Material.getMaterial(dropSection.getString("item"));
					int level = dropSection.getInt("level");
					double chance = dropSection.getDouble("chance");
					int dropExp = Math.max(skillConfig.getInt("exp.drops"), dropSection.getInt("exp"));
					drops.put(material, new Drop(material, level, chance, dropExp));
				}
				finally {}
			}
		}
	}
}
