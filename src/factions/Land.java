package factions;

import org.bukkit.Chunk;
import org.bukkit.World;

public class Land {
	private final World world;
	private final Chunk chunk;
	
	public Land(Chunk chunk) {
		world = chunk.getWorld();
		this.chunk = chunk;
	}
	
	public Land(World world, int x, int z) {
		this.world = world;
		chunk = world.getChunkAt(x, z);
	}
	
	World getWorld() {
		return world;
	}
	
	public Chunk getChunk() {
		return chunk;
	}
	
	public String serialize() {
		return world.getName() + "," + chunk.getX() + "," + chunk.getZ() + ":";
	}
	
	@Override	
	public boolean equals(Object object) {
		if (object == null || !(object instanceof Land)) {
			return false;
		}
		else if (object == this) {
			return true;
		}
		else {
			Land otherLand = (Land) object;
			World otherWorld = otherLand.getWorld();
			Chunk otherChunk = otherLand.getChunk();
			return world.equals(otherWorld) && chunk.equals(otherChunk);
		}
	}
}
