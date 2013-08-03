package skills;

import combat.Archery;
import combat.Swords;
import combat.Toughness;
import industry.*;

import java.util.UUID;

public enum SkillType {
	ARCHAEOLOGY (Archaeology.class),
	ARCHERY (Archery.class), 
	FARMING (Farming.class),
	FISHING (Fishing.class),
	MINING (Mining.class),
	SWORDS (Swords.class),
	TOUGHNESS (Toughness.class),
	WOODCUTTING (Woodcutting.class);
	
	private final Class<?> skillClass;
	
	SkillType(Class<?> skillClass) {
		this.skillClass = skillClass;
	}
	
	public Skill instance(UUID id) throws Exception {
		return (Skill) skillClass.getDeclaredConstructor(UUID.class).newInstance(id);
	}
	
	public static SkillType getSkillType(String skill) {
		skill = skill.toLowerCase();
		for (SkillType skillType : SkillType.values()) {
			if (skillType.name().toLowerCase().equals(skill.toLowerCase())) {
				return skillType;
			}
		}
		return null;
	}
}
