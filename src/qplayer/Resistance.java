package qplayer;

import custom.Clock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import skills.SkillType;
import util.PlayerUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

final class Resistance implements ActionListener {
    private static final PotionEffectType RESISTANCE = PotionEffectType.DAMAGE_RESISTANCE;
    private final UUID id;
    private final Timer buffTimer, debuffTimer;
    private final Clock buffClock;
    private final Clock debuffClock;
    private final Clock appleClock;
    private int buffStacks, debuffStacks, appleDuration;

    public Resistance(UUID id) {
        this.id = id;

        buffStacks = 0;
        buffTimer = new Timer(10000, this);
        buffTimer.setRepeats(false);
        buffTimer.setActionCommand("buff");
        buffClock = new Clock();

        debuffStacks = 0;
        debuffTimer = new Timer(10000, this);
        debuffTimer.setRepeats(false);
        debuffTimer.setActionCommand("debuff");
        debuffClock = new Clock();

        appleClock = new Clock();
        appleDuration = 0;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Player player = PlayerUtil.getPlayer(id);
        switch (event.getActionCommand()) { //Check the action command to see if a buff/debuff is falling off. Ignore if null (refreshing effect)
            case "buff": //Buff has ended. Stop the buffClock and remove buffStacks
                buffTimer.stop();
                buffStacks = 0;
                player.sendMessage(ChatColor.GREEN + "Buff Fell Off");
                break;
            case "debuff": //Debuff has ended. Stop the debuffClock and remove debuffStacks
                debuffTimer.stop();
                debuffStacks = 0;
                player.sendMessage(ChatColor.RED + "Debuff Fell Off");
                break;
            default:
                break;
        }

        int amplifier = debuffStacks + buffStacks;

        if (appleDuration - appleClock.getTicks() > 0) { //Add 1 to amplifier if the target has an active god apple
            amplifier++;
        }

        player.sendMessage("Rank " + amplifier);

        if (buffTimer.isRunning() || debuffTimer.isRunning()) {
            switch (amplifier) { //Apply a potion effect depending on the amplifier
                case 2: //The target is either getting a buff for the full duration or getting whatever is left after the debuff wears off
                case 1: //The target gets either Resistance 1 (no active god apple) or Resistance 2 (active god apple)
                    player.addPotionEffect(new PotionEffect(RESISTANCE, (int) (200 - buffClock.getTicks()), amplifier - 1));
                    break;
                case 0: //Buff and debuff cancel each other out. Remove resistance effect
                    player.removePotionEffect(RESISTANCE);
                    break;
                default: //All other cases of amplifier (aka amplifier != 0, 1, or 2 --> amplifier is negative and so we need to debuff target
                    player.addPotionEffect(new PotionEffect(RESISTANCE, 200, amplifier), true);
                    break;
            }
        }
        else { //If both the buff and debuff have worn off, remove this resistance instance
            if (appleDuration - appleClock.getTicks() > 0) { //Also check to see if we should reapply an active god apple
                int duration = Math.round(appleDuration - appleClock.getTicks());
                PotionEffect effect = new PotionEffect(RESISTANCE, duration, 0);
                player.sendMessage("" + effect.getDuration());
                player.addPotionEffect(effect);
            }
        }
    }

    private void checkForApple() { //Check for an active god apple.
        for(PotionEffect effect : PlayerUtil.getPlayer(id).getActivePotionEffects()) {
            if (effect.getType().equals(RESISTANCE)) {
                if (effect.getAmplifier() == 0) {
                    if (effect.getDuration() > 200) {
                        appleDuration = effect.getDuration();
                        appleClock.reset();
                    }
                }
            }
        }
    }

    public void debuff() { //Debuff the target. Always check for apple first and then restart the debuffClock and debuffTimer
        QPlayer qplayer = PlayerUtil.getQPlayer(id);
        PlayerUtil.message(id, ChatColor.RED + "Debuff");
        checkForApple();
        debuffStacks--;
        debuffClock.reset();
        debuffTimer.restart();
        actionPerformed(new ActionEvent(this, 0, ""));
    }

    public void buff() { //Buff the target. Always check for apple first and then restart the buffClock and buffTimer
        QPlayer qplayer = PlayerUtil.getQPlayer(id);
        PlayerUtil.message(id, ChatColor.GREEN + "Buff");
        checkForApple();
        buffStacks = 1; //Buff stacks cannot go above 1
        buffClock.reset();
        buffTimer.restart();
        actionPerformed(new ActionEvent(this, 0, ""));
    }
}
