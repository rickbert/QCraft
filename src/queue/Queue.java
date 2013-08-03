package queue;

import java.util.LinkedList;

class Queue {
	private static final LinkedList<Queable> items = new LinkedList<>();
	
	public static void resolve() {
        for (Queable item : items) {
            if (item.isValid()) {
                item.execute();
            } else {
                items.remove(item);
            }
        }
	}
}
