package factions;

import org.bukkit.Location;
import org.bukkit.World;

class Home {
	private final World world;
	private final int x, y, z;
	
	public Home(Location location) {
		world = location.getWorld();
		x = (int) location.getX();
		y = (int) location.getY();
		z = (int) location.getZ();
	}
	
	public Location getLocation() {
		return new Location(world, x, y, z);
	}
	
	public String serialize() {
		return world.getName() + "," + x + "," + y + "," + "z";
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof Home)) {
			return false;
		}
		else {
			Home otherHome = (Home) object;
			return getLocation().equals(otherHome.getLocation());
		}
	}
}
