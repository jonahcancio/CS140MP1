import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.io.*;

public class CrockPot {
	static String taskListDemo = "adobo 1\n"
		+ "tinola 5\n"
		+ "karekare 8\n"
		+ "adobo 14\n";
	static String recipeDemo = "cook 30\n"
		+ "mix 5\n"
		+ "cook 30\n"
		+ "sauce 7\n"
		+ "cook 10\n";
	static ArrayList<Dish> gordonQueue = new ArrayList<Dish>();
	static HashMap<String, Integer> gordonMap = new HashMap<String, Integer>();

	public static void main(String args[]) throws IOException{
		FileReader taskFile = new FileReader("recipes/tasklist.txt");
		BufferedReader taskReader = new BufferedReader(taskFile);

		// stringDemoLoop();
		fileDemoLoop(taskReader);



	}

	static void fileDemoLoop(BufferedReader taskReader) throws IOException{
		String taskLine;
		while ((taskLine = taskReader.readLine()) != null) {
			String dishName = taskLine.split("\\s")[0];//get 1st word of taskLine
			gordonMap.put(dishName, gordonMap.containsKey(dishName)? gordonMap.get(dishName)+1 : 1);//adds 1 to map value if already in key map, else adds to key map with value of 1
			Dish newDish = new Dish(dishName + gordonMap.get(dishName)); //init dish with numbered name (<name><number>)

			newDish.startTime = Integer.parseInt(taskLine.split("\\s")[1]);//init startTime of dish with 2nd word of taskLine

			newDish.aQueue = new ArrayList<Action>();//init dish aQueue
			FileReader dishFile = new FileReader("recipes/" + dishName + ".txt");
			BufferedReader dishReader = new BufferedReader(dishFile);
			String actLine;
			while ((actLine = dishReader.readLine()) != null) {//add each action in recipe to aQueue of dish
				String actName = actLine.split("\\s")[0];//get 1st word
				int actTimeLeft = Integer.parseInt(actLine.split("\\s")[1]);//parse 2nd word
				newDish.aQueue.add(new Action(actName, actTimeLeft));//combine into Action and add to aQueue
			}

			gordonQueue.add(newDish);
		}

		consoleLogGordonQueue();
	}

	static void stringDemoLoop() {
		for (String taskLine : taskListDemo.split("\\n")) {
			String dishName = taskLine.split("\\s")[0];//get 1st word of taskLine
			gordonMap.put(dishName, gordonMap.containsKey(dishName)? gordonMap.get(dishName)+1 : 1);//adds 1 to map value if already in key map, else adds to key map with value of 1
			Dish newDish = new Dish(dishName + gordonMap.get(dishName));//init dish with numbered name (<name><number>)

			newDish.startTime = Integer.parseInt(taskLine.split("\\s")[1]);//init startTime of dish with 2nd word of taskLine

			newDish.aQueue = new ArrayList<Action>();//init dish aQueue
			for(String actLine : recipeDemo.split("\n")) {//add each action in recipe to aQueue of dish
				String actName = actLine.split("\\s")[0];//get 1st word
				int actTimeLeft = Integer.parseInt(actLine.split("\\s")[1]);//parse 2nd word
				newDish.aQueue.add(new Action(actName, actTimeLeft));//combine into Action and add to aQueue
			}

			gordonQueue.add(newDish);
		}
	}

	static void consoleLogGordonQueue() {
		for (Dish dish : gordonQueue){
			System.out.println(dish.name + ":");
			for(Action act : dish.aQueue) {
				System.out.println("\t=> " + act.name + " - " + act.timeLeft);
			}
			System.out.println("");
		}
	}

}
