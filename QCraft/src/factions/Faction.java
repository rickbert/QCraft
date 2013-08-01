package factions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Faction {
	private String name;
	private HashMap<String, Status> members = new HashMap<String, Status>();
	private HashSet<Land> land = new HashSet<Land>();
	private Home home = null;
	private int balance = 0;

	public Faction(String name) {
		this.name = name;
	}

	public String name() {
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}

	public String admin() {
		for (Entry<String, Status> entry : members.entrySet()) {
			if (entry.getValue().equals(Status.ADMIN)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public boolean setMemberStatus(String player, Status status) {
		if (members.containsKey(player)) {
			switch (status) {
			case RECRUIT:
				members.put(player, Status.RECRUIT);
				return true;
			case MEMBER:
				members.put(player, Status.MEMBER);
				return true;
			case MODERATOR:
				members.put(player, Status.MODERATOR);
				return true;
			case ADMIN:
				members.put(admin(), Status.MODERATOR);
				members.put(player, Status.ADMIN);	
				return true;				
			}		
		}
		return false;
	}

	public Status getStatus(Player player) {
		if (members.containsKey(player)) {
			return members.get(player);
		}
		return null;
	}

	public Set<String> getMembers() {
		return members.keySet();
	}

	public boolean addMember(String player) {
		if (!members.containsKey(player)) {
			members.put(player, Status.RECRUIT);
			return true;
		}
		return false;
	}

	public boolean addMember(String player, Status status) {
		if (members.containsKey(player) == false) {
			members.put(player, status);
			return true;
		}
		return false;
	}

	public boolean removeMember(Player player) {
		if (members.containsKey(player.getName())) {
			members.remove(player.getName());
			return true;
		}
		return false;
	}

	public HashSet<Land> getLand() {
		return land;
	}

	public boolean addLand(Land newLand) {
		if (!land.contains(newLand)) {
			land.add(newLand);
			return true;
		}
		return false;
	}

	public boolean removeLand(Land landToRemove) {
		if (!land.contains(landToRemove)) {
			land.remove(landToRemove);
			return true;
		}
		return false;
	}

	public String serializeLand() {
		if (land.size() > 0) {
			String serialize = "";
			Iterator<Land> iterator = land.iterator();
			while(iterator.hasNext()) {
				serialize = serialize + iterator.next().serialize();
			}
			return serialize;
		}
		return null;
	}

	public Location getHome() {
		return !(home == null)? home.getLocation() : null;
	}

	public void setHome(Location location) {
		home = new Home(location);
	}

	public int getBalance() {
		return balance;
	}

	public boolean setBalance(int amount) {
		if (amount >= 0) {
			balance = amount;
			return true;
		}
		return false;
	}

	public String serializeHome() {
		return !(home == null)? home.serialize() : null;
	}

	public int getPower() {
		int power = 0;
		for(String player: members.keySet()) {
			power = power + FactionListener.getPower(player);
		}
		return power;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof Faction)) {
			return false;
		}
		else {
			Faction otherFaction = (Faction) object;
			try {
				if (name == null && otherFaction.name() == null) {
					return true;
				}
				boolean sameName = name.equals(otherFaction.name());
				boolean sameMembers = name.equals(otherFaction.getMembers());
				boolean sameLand = land.equals(otherFaction.getLand());
				boolean sameHome = home.equals(otherFaction.getHome());
				return sameName && sameMembers && sameLand && sameHome;

			}
			catch (Exception e) {
				return false;
			}

		}
	}
}
