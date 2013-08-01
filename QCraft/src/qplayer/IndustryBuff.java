package qplayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.Timer;

import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import custom.Tool;
import skills.SkillType;
import util.PlayerUtil;

public class IndustryBuff implements ActionListener {
	private static final PotionEffectType SPEED = PotionEffectType.FAST_DIGGING;
	private final Timer timer = new Timer(750, this);
	private final UUID id;
	private Block block;

	public IndustryBuff(UUID id) {
		this.id = id;
		timer.setRepeats(false);
	}

	private boolean hasBuff() {
		for (PotionEffect effect : PlayerUtil.getPlayer(id).getActivePotionEffects()) {
			if (effect.getType().equals(PotionEffectType.FAST_DIGGING)) {
				return effect.getDuration() <= 15;
			}
		}
		return false;
	}

	private int buffLevel(Block targetBlock, Tool tool) {
		QPlayer qplayer = PlayerUtil.getQPlayer(id);
		int amplifier = -1;

		switch (tool) {
		case SPADE:
			amplifier = amplifier + qplayer.getSkill(SkillType.ARCHAEOLOGY).getLevel() / 250;
			break;
		case HOE:
			amplifier = amplifier + qplayer.getSkill(SkillType.FARMING).getLevel() / 250;
			break;
		case PICKAXE:		
			amplifier = amplifier + qplayer.getSkill(SkillType.MINING).getLevel() / 250;
			break;
		case AXE:
			amplifier = amplifier + qplayer.getSkill(SkillType.WOODCUTTING).getLevel() / 250;
			break;
		default:
			break;
		}

		amplifier = Math.min(2, amplifier);

		return amplifier;
	}

	public void buff(Block targetBlock, Tool tool) {
		int amplifier = buffLevel(targetBlock, tool);
		if (amplifier >= 0) {
			if (block == null) {
				block = targetBlock;
			}

			if (hasBuff()) {
				if (block.equals(targetBlock) == false) {
					removeBuff();
					block = targetBlock;
				}
			}

			PlayerUtil.getPlayer(id).addPotionEffect(new PotionEffect(SPEED, 15, amplifier));
			timer.restart();
		}
	}

	public void removeBuff() {
		if (hasBuff()) {
			block = null;
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		removeBuff();
	}
}
