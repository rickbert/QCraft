package qplayer;

import factions.Faction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import main.QCraft;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import custom.Tool;
import skills.Skill;
import skills.SkillType;

/*
 * Our class to store all the information QCraft will need (Money, Faction, Active, and Skill data)
 */

public class QPlayer {
	private final UUID id;
	private Money money = new Money(0);
	private Faction faction = null;
	private Power power = null;
	private HashMap<SkillType, Skill> skills = new HashMap<SkillType, Skill>();
	private HashSet<Bleed> bleeds = new HashSet<Bleed>();
	private IndustryBuff industryBuff;

	public QPlayer(QCraft plugin, Player player) {
		this.id = player.getUniqueId();
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
		skills.put(skill, new Skill(skill, player.get(), level, exp));
	}
	
	public void setSkills(HashMap<SkillType, Skill> skills) {
		this.skills = skills;
	}
	
	public Skill getSkill(SkillType skill) {
		return skills.get(skill);
	}
	
	public void applyIndustryBuff(Block block, Tool tool) {
		industryBuff.buff(block, tool);
	}
	
	public void removeIndustryBuff() {
		industryBuff.removeBuff();
	}
}
