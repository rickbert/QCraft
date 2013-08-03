package industry;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.material.SpawnEgg;
import org.bukkit.util.Vector;
import skills.Active.ActiveState;
import skills.Skill;
import util.PlayerUtil;

import java.util.Random;
import java.util.UUID;

public class Fishing extends Skill {

    public Fishing(UUID id, YamlConfiguration playerConfig) {
        super(id, playerConfig);
    }

    @Override
    public void info() {
        super.info();
        message(0, "Differing chances to acquire an item on a successful fish");
        message(750, "Level 750 Active: Throw player on right click");
    }

    public void active(Player caught) {
        if (level >= 750) {
            if (active.getState().equals(ActiveState.PRIMED)) {
                Location playerLocation = PlayerUtil.getPlayer(id).getLocation();
                Location targetLocation = caught.getLocation();
                Location distance = playerLocation.subtract(targetLocation);
                double multiplier = playerLocation.distance(targetLocation);
                double x = distance.getX() / multiplier;
                double y = distance.getY() / multiplier;
                double z = distance.getZ() / multiplier;
                caught.setVelocity(new Vector(x, y, z));
                active.activate(0, 15);
            }
        }
    }

    public void catchEgg(LivingEntity caught) {
        Player player = PlayerUtil.getPlayer(id);
        EntityType eggType = caught.getType();
        if (eggType.isSpawnable()) {
            if (new Random().nextDouble() <= level / 10000.0) {
                World world = player.getWorld();
                world.dropItem(player.getLocation(), new SpawnEgg(eggType).toItemStack());
            }
        }
    }

    @Override
    protected void loadSkillInfo() {
        // TODO Auto-generated method stub

    }
}
