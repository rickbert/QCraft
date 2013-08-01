package skills;

import combat.Archery;
import combat.Swords;
import combat.Toughness;
import industry.Archaeology;
import industry.Farming;
import industry.Fishing;
import industry.Mining;
import industry.Woodcutting;

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
	
	public Class<?> getClass(SkillType skillType) {
		return skillClass;
	}
}
