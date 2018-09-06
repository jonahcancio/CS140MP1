import java.util.ArrayList;

public class Dish {
	String name;
	ArrayList<Action> aQueue;
	int currentActionIndex;
	boolean hasStarted;
	int startTime;
	int order;

	public Dish(String name) {
		this.name = name;
	}
}

class Action {
	String name;
	int timeLeft;
}
