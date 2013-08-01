package qplayer;

import factions.Faction;

import java.util.HashMap;
import java.util.UUID;

import main.QCraft;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import custom.Tool;
import skills.Skill;
import skills.SkillType;

/*
 * Our wrapper class which stores all our custom data associated with players
 */

public class QPlayer {
	private final UUID id;
	private Money money = new Money(0);
	private Faction faction = null;
	private Power power = null;
	private HashMap<SkillType, Skill> skills = new HashMap<SkillType, Skill>();
	private HashMap<UUID, Bleed> bleeds = new HashMap<UUID, Bleed>();
	private final IndustryBuff industryBuff;
	private final Resistance resistance;
	private final PermissionAttachment permissions;

	public QPlayer(Player player) {
		this.id = player.getUniqueId();
		permissions = player.addAttachment(QCraft.get());
		industryBuff = new IndustryBuff(id);
		resistance = new Resistance(id);
	}
	
	public void setMoney(int amount) {
		money = new Money(amount);
	}
	
	public Money getMoney() {
		return money;
	}
	
	public void setFaction(Faction faction) {
		this.faction = faction;
		if (faction == null) {
			power = null;
		}
		else {
			if (power == null) {
				setPower(10);
			}
		}
	}
	
	public Faction getFaction() {
		return faction;
	}
	
	public void setPower(int power) {
		this.power = new Power(id, power);
	}
	
	public int getPower() {
		return power.getPower();
	}
	
	public void setSkill(SkillType skill, int level, int exp) {
	}
	
	public void setSkills(HashMap<SkillType, Skill> skills) {
		this.skills = skills;
	}
	
	public Skill getSkill(SkillType skill) {
		return skills.get(skill);
	}
	
	public void applyBleed(Player damager, Player target) {
		bleeds.put(damager.getUniqueId(), new Bleed(damager, target));
	}
	
	public void removeBleed(UUID damagerId) {
		bleeds.remove(damagerId);
	}
	
	public void applyIndustryBuff(Block targetBlock, Tool tool) {
		industryBuff.buff(targetBlock, tool);
	}
	
	public void removeIndustryBuff() {
		industryBuff.removeBuff();
	}
	
	public void increaseResistance() {
		resistance.buff();
	}
	
	public void decreaseResistance() {
		resistance.debuff();
	}
	
	public void setPermission(String permission, boolean value) {
		permissions.setPermission(permission, value);
	}
	
	public void unsetPermission(String permission) {
		permissions.unsetPermission(permission);
	}
	
	public void save() {
		
	}
}
