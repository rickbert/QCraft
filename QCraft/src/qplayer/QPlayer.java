package qplayer;

import factions.Faction;
import industry.IndustryBuff;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import java.util.HashMap;

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
	private final WeakReference<Player> player;
	private Money money = new Money(0);
	private Faction faction = null;
	private Power power = null;
	private HashMap<SkillType, Skill> skills = new HashMap<SkillType, Skill>();
	private IndustryBuff industryBuff;
	private PermissionAttachment permissions;

	public QPlayer(Player player) {
		this.player = new WeakReference<Player>(player);
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
		this.power = new Power(player.get(), power);
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
	
	public void setPermission(String name, boolean value) {
		permissions.setPermission(name, value);
	}
	
	public void removePermission(String name) {
		permissions.unsetPermission(name);
	}
}