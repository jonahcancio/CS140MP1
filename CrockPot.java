import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CrockPot {
	static ArrayList<Dish> gordonQueue = new ArrayList<Dish>();
	static HashMap<String, Integer> gordonMap = new HashMap<String, Integer>();

	public static void main(String args[]) {
		String demo = "adobo 1\n"
			+ "tinola 5\n"
			+ "karekare 8\n"
			+ "adobo 14\n";

		for (String line : demo.split("\\n")) {
			String dishName = line.split("\\s")[0];
			gordonMap.put(dishName, gordonMap.containsKey(dishName)? gordonMap.get(dishName)+1 : 1);
			gordonQueue.add(new Dish(dishName + gordonMap.get(dishName)));
		}

		for (Dish dish : gordonQueue){
			System.out.println(dish.name);
		}

	}
}
