package industry;

import custom.Tool;
import main.QCraft;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.material.SpawnEgg;
import qplayer.QPlayer;
import skills.SkillType;
import util.PlayerUtil;

import java.util.Random;

public class IndustryListener implements Listener {

	public IndustryListener(QCraft plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler (ignoreCancelled = true)
	private void industryBuff(BlockDamageEvent event) {
		Player player = event.getPlayer();
		QPlayer qplayer = PlayerUtil.getQPlayer(player);
		Block block = event.getBlock();
		Tool tool = Tool.getTool(player.getItemInHand());
		switch (tool) {
		case AXE:
			if (Woodcutting.isLoggable(block)) {
				Woodcutting woodcutting = (Woodcutting) qplayer.getSkill(SkillType.WOODCUTTING);
				woodcutting.active(block);
				qplayer.applyIndustryBuff(block, tool);
			}
			break;
		case PICKAXE:
			if (Mining.isMinable(block)) {
				Mining mining = (Mining) qplayer.getSkill(SkillType.MINING);
				mining.active(block);
				qplayer.applyIndustryBuff(block, tool);
			}
			break;
		case SPADE:
			if (Archaeology.isDiggable(block)) {
				Archaeology archaeology = (Archaeology) qplayer.getSkill(SkillType.ARCHAEOLOGY);
				archaeology.beginDigging(block);
				qplayer.applyIndustryBuff(block, tool);
			}
			break;
		case HOE:
			if (Farming.isCrop(block)) {
				qplayer.applyIndustryBuff(block, tool);
			}			
			break;
		default:
			break;
		}
	}

	@EventHandler (ignoreCancelled = true)
	private void refreshIndustryBuff(PlayerAnimationEvent event) {
		Player player = event.getPlayer();
		QPlayer qplayer = PlayerUtil.getQPlayer(player);
		Block block = player.getTargetBlock(null, 5);
		Tool tool = Tool.getTool(player.getItemInHand());
		switch (tool) {
		case AXE:
		case PICKAXE:
		case SPADE:
		case HOE:
			qplayer.applyIndustryBuff(block, tool);
			break;
		default:
			break;
		}
	}

	@EventHandler (ignoreCancelled = true)
	private void blockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		QPlayer qplayer = PlayerUtil.getQPlayer(player);
		Block block = event.getBlock();
		if (Farming.isCrop(block)) {
			Farming farming = (Farming) qplayer.getSkill(SkillType.FARMING);
			farming.blockBreak(block);
		}
		switch (Tool.getTool(player.getItemInHand())) {
		case AXE:
			if (Woodcutting.isLoggable(block)) {
				Woodcutting woodcutting = (Woodcutting) qplayer.getSkill(SkillType.WOODCUTTING);
				woodcutting.blockBreak(block);
			}
			break;
		case PICKAXE:
			if (Mining.isMinable(block)) {
				Mining mining = (Mining) qplayer.getSkill(SkillType.MINING);
				mining.blockBreak(block);
			}
			break;
		case SPADE:
			if (Archaeology.isDiggable(block)) {
				Archaeology archaeology = (Archaeology) qplayer.getSkill(SkillType.MINING);
				archaeology.endDigging(block);
			}
			break;
		default:
			break;
		}
	}

	@EventHandler (ignoreCancelled = true)
	private void industryActives(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			QPlayer qplayer = PlayerUtil.getQPlayer(player);
			Block block = event.getClickedBlock();
			Farming farming = (Farming) qplayer.getSkill(SkillType.FARMING);
			farming.active(block);
			switch (Tool.getTool(player.getItemInHand())) {
			case AXE:
				if (block.getType().equals(Material.LOG)) {
					Woodcutting woodcutting = (Woodcutting) qplayer.getSkill(SkillType.WOODCUTTING);
					woodcutting.treeFeller(block);
				}
				break;
			case PICKAXE:
				if (Mining.isMinable(block)) {
					Mining mining = (Mining) qplayer.getSkill(SkillType.MINING);
					mining.primeActive();
				}
				break;
			case SPADE:
				if (Archaeology.isDiggable(block)) {
					Archaeology archaeology = (Archaeology) qplayer.getSkill(SkillType.ARCHAEOLOGY);
					archaeology.primeActive();
				}
				break;
			default:
				break;
			}
		}
	}

	@EventHandler (ignoreCancelled = true)
	private void farmingGrowthPassive(BlockPlaceEvent event) {
		QPlayer qplayer = PlayerUtil.getQPlayer(event.getPlayer());
		Farming farming = (Farming) qplayer.getSkill(SkillType.FARMING);
		farming.blockPlace(event.getBlock());
	}

	@EventHandler (ignoreCancelled = true)
	private void farmingHungerPassive(PlayerItemConsumeEvent event) {
		QPlayer qplayer = PlayerUtil.getQPlayer(event.getPlayer());
		Farming farming = (Farming) qplayer.getSkill(SkillType.FARMING);
		farming.itemConsume(event.getItem());
	}

	@EventHandler (ignoreCancelled = true)
	private void farmingBlockConversion(PlayerInteractEvent event) {
		QPlayer qplayer = PlayerUtil.getQPlayer(event.getPlayer());
		Farming farming = (Farming) qplayer.getSkill(SkillType.FARMING);
		farming.active(event.getClickedBlock());
	}

	@EventHandler (ignoreCancelled = true)
	private void farmingCowConversion(PlayerInteractEntityEvent event) {
		QPlayer qplayer = PlayerUtil.getQPlayer(event.getPlayer());
		Farming farming = (Farming) qplayer.getSkill(SkillType.FARMING);
		farming.active(event.getRightClicked());
	}

	@EventHandler (ignoreCancelled = true)
	private void fishing(PlayerFishEvent event) {
		QPlayer qplayer = PlayerUtil.getQPlayer(event.getPlayer());
		Fishing fishing = (Fishing) qplayer.getSkill(SkillType.FISHING);
		if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
			fishing.addExp(100);
		}
		if (event.getCaught() instanceof LivingEntity) {
			Player player = event.getPlayer();
			LivingEntity caught = (LivingEntity) event.getCaught();
			if (caught instanceof Player) {
				fishing.active((Player) caught);
			}
			else {
                fishing.catchEgg(caught);
			}
		}
	}
}