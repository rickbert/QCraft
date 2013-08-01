package qplayer;

public class Money {
	private int balance;

	public Money(int balance) {
		this.balance = balance;
	}

	public int getBalance() {
		return balance;
	}

	public boolean deposit(int amount) {
		if (amount > 0) {
			balance = balance + amount;
			return true;
		}
		return false;
	}

	public boolean withdraw(int amount) {
		if (balance - amount >= 0) {
			balance = balance - amount;
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "You have $: " + balance;
	}
}
