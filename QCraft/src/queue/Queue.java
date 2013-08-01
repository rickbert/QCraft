package queue;

import java.util.Iterator;
import java.util.LinkedList;

public class Queue {
	private static LinkedList<Queable> items = new LinkedList<Queable>();
	
	public static void resolve() {
		Iterator<Queable> iterator = items.iterator();
		while (iterator.hasNext()) {
			Queable item = iterator.next();
			if (item.isValid()) {
				item.execute();
			}
			else {
				items.remove(item);
			}
		}
	}
}