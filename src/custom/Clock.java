package custom;

import java.util.concurrent.TimeUnit;

public class Clock {
	private long start = System.nanoTime();

	private long time() {
		return System.nanoTime() - start;
	}

	public double getTime() {
		return TimeUnit.SECONDS.convert(start, TimeUnit.NANOSECONDS);
	}
	
	public void addTime(int duration) {
		long addition = TimeUnit.NANOSECONDS.convert(duration, TimeUnit.SECONDS);
		start = start + addition;
	}
	
	public long getTicks() {
		return Math.round(getTime() * 20);
	}
	
	public void reset() {
		start = System.nanoTime();
	}
}
