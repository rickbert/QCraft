package custom;

public class Clock {
	public static final int NANOSECONDS = 0;
	public static final int MILLISECONDS = 1; 
	public static final int SECONDS = 2;

	private long start = System.nanoTime();

	private long time() {
		return System.nanoTime() - start;
	}

	public double getTime(int unit) {
		switch (unit) {
		case 1:
			return time() / 1000000.0;
		case 2:
			return time() / 1000000000.0;
		case 0:
		default:
			return time() / 1;
		}
	}
	
	public long getTicks() {
		return Math.round(getTime(Clock.SECONDS) * 20);
	}
	
	public void reset() {
		start = System.nanoTime();
	}
}
