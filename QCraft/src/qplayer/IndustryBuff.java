package qplayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;

import javax.swing.Timer;

import main.QCraft;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import custom.Tool;
import skills.SkillType;

public class IndustryBuff implements ActionListener {
	private static final PotionEffectType SPEED = PotionEffectType.FAST_DIGGING;
	private final Timer timer = new Timer(750, this);
	private final WeakReference<Player> player;
	private Block block;

	public IndustryBuff(Player player) {
		this.player = new WeakReference<Player>(player);
		timer.setRepeats(false);
	}

	private boolean hasBuff() {
		for (PotionEffect effect : player.get().getActivePotionEffects()) {
			if (effect.getType().equals(PotionEffectType.FAST_DIGGING)) {
				return effect.getDuration() <= 15;
			}
		}
		return false;
	}

	public void buff(Block targetBlock, Tool tool) {
		if (block == null) {
			block = targetBlock;
		}

		if (hasBuff()) {
			if (block.equals(targetBlock) == false) {
				removeBuff();
				block = targetBlock;
			}
		}

		QPlayer qplayer = QCraft.getQPlayer(player.get());
		int amplifier = -1;

		switch (tool) {
		case SPADE:
			amplifier = amplifier + qplayer.getSkill(SkillType.ARCHAEOLOGY).level() / 250;
			break;
		case HOE:
			amplifier = amplifier + qplayer.getSkill(SkillType.FARMING).level() / 250;
			break;
		case PICKAXE:		
			amplifier = amplifier + qplayer.getSkill(SkillType.MINING).level() / 250;
			break;
		case AXE:
			amplifier = amplifier + qplayer.getSkill(SkillType.WOODCUTTING).level() / 250;
			break;
		default:
			break;
		}

		amplifier = Math.min(2, amplifier);

		if (amplifier >= 0) {
			player.get().addPotionEffect(new PotionEffect(SPEED, 15, amplifier));
		}
		
		timer.restart();
	}

	public void removeBuff() {
		if (hasBuff()) {
			player.get().removePotionEffect(SPEED);
			block = null;
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		removeBuff();
	}
}
