package industry;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import skills.Skill;
import skills.Active.ActiveState;

public class Fishing extends Skill {

	public Fishing(Player player, int level, int exp) {
		super(player, level, exp);
	}

	@Override
	public void info() {
		super.info();
		message(0, "Differing chances to acquire an item on a successful fish");
		message(750, "Level 750 Active: Throw player on right click");
	}

	public void active(Player caught) {
		if (active.getState().equals(ActiveState.PRIMED)) {
			Location playerLocation = player.get().getLocation();
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
