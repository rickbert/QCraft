package custom;

import java.util.concurrent.TimeUnit;

public class Clock {
	public enum Time {
		NANOSECONDS, MILLISECONDS, SECONDS;
	}

	private long start = System.nanoTime();

	private long time() {
		return System.nanoTime() - start;
	}

	public double getTime(Time time) {
		switch (time) {
		case MILLISECONDS:
			return time() / 1000000.0;
		case SECONDS:
			return time() / 1000000000.0;
		default:
			return start;
		}
	}
	
	public void addTime(int duration) {
		long addition = TimeUnit.NANOSECONDS.convert(duration, TimeUnit.SECONDS);
		start = start + addition;
	}
	
	public long getTicks() {
		return Math.round(getTime(Time.SECONDS) * 20);
	}
	
	public void reset() {
		start = System.nanoTime();
	}
}
