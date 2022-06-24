package rubik_cube.cube;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Queue implements IQueue<String>{

	private final int MAX;
	private int length;
	private final ArrayList<String> list;

	public Queue(int max) {
		this.MAX = max;
		this.length = 0;
		this.list = new ArrayList<>();
	}

	@Override
	public String enqueue(String x) {
		try {
			//insert in tail
			this.list.add(this.length, x);
			this.length ++;
			if(this.length > this.MAX) {
				System.out.println("too much element");
				this.dequeue();
			}
			return x;
		} catch(IllegalArgumentException e) {
			System.out.println("max_Queue ENQUEUE error!");
			return null;
		}
	}

	@Override
	public String dequeue() {
		try {
			//remove from head
			String x = this.list.get(0);
			this.list.remove(0);
			this.length --;
			return x;
		} catch(IllegalArgumentException e) {
			System.out.println("max_Queue ENQUEUE error!");
			return null;
		}
	}

	@Override
	public String peek() {
		try {
			//get from head without removing
			return this.list.get(0);
		} catch(IllegalArgumentException e) {
			System.out.println("max_Queue ENQUEUE error!");
			return null;
		}
	}

	@Override
	public boolean isEmpty() {
		return this.list.isEmpty();
	}

	@Override
	public void clear() {
		this.list.clear();
		this.length = 0;
	}

	@NonNull
	public String toString() {
		StringBuilder str = new StringBuilder("h: ");
		for (String s : this.list) {
			str.append(s);
		}
		return str.toString();
	}
}
