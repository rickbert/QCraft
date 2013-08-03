package qplayer;

import custom.Tool;
import industry.Archaeology;
import industry.Farming;
import industry.Mining;
import industry.Woodcutting;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import skills.SkillType;
import util.PlayerUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

class IndustryBuff implements ActionListener {
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

	public void buff(Block targetBlock, Tool tool) {
        QPlayer qplayer = PlayerUtil.getQPlayer(id);
		int amplifier = -1;

        switch (tool) {
            case PICKAXE:
                ((Mining) qplayer.getSkill(SkillType.MINING)).buff();
                break;
            case AXE:
                ((Woodcutting) qplayer.getSkill(SkillType.WOODCUTTING)).buff();
                break;
            case HOE:
                ((Farming) qplayer.getSkill(SkillType.FARMING)).buff();
                break;
            case SPADE:
                ((Archaeology) qplayer.getSkill(SkillType.ARCHAEOLOGY)).buff();
                break;
            default:
                break;
        }
		if (amplifier >= 0) {
			if (block == null) {
				block = targetBlock;
			}

			if (hasBuff()) {
				if (!block.equals(targetBlock)) {
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
