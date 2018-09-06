import java.util.ArrayList;

public class Dish {
	String name;
	ArrayList<Action> aQueue;
	int currentActionIndex;
	boolean hasStarted;
	int startTime;

	public Dish(String name) {
		this.name = name;
		currentActionIndex = 0;
	}
}

class Action {
	String name;
	int timeLeft;

	public Action(String name, int timeLeft) {
		this.name = name;
		this.timeLeft = timeLeft;
	}
}
