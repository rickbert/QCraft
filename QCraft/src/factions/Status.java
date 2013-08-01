package factions;

public enum Status {
	RECRUIT, MEMBER, MODERATOR, ADMIN;
	
	public static Status getStatus(String status) {
		switch (status) {
		case "RECRUIT":
			return RECRUIT;
		case "MEMBER":
			return MEMBER;
		case "MODERATOR":
			return MODERATOR;
		case "ADMIN":
			return ADMIN;
		default:
			return RECRUIT;
		}
	}
}
