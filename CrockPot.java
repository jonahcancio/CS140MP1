import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.io.*;
import java.lang.StringBuilder;

public class CrockPot {
	static String outputFileName = "JandelOutput.html";
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
	static String gordonTable = "";
	static String gordonHtml = "";

	public static void main(String args[]) throws IOException{
		FileReader taskFile = new FileReader("recipes/tasklist.txt");
		BufferedReader taskReader = new BufferedReader(taskFile);


		fileDemoLoop(taskReader);
		initGordonHtml();

		initGordonTable();
		insertGordonTable();
		System.out.println(gordonHtml);
		outputGordonHtml();

		taskReader.close();
		taskFile.close();
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
			System.out.println(dish.name + " - " + dish.startTime +":");
			for(Action act : dish.aQueue) {
				System.out.println("\t=> " + act.name + " - " + act.timeLeft);
			}
			System.out.println("");
		}
	}


	static void initGordonTable() {
		gordonTable += "<table class='ui celled padded table'>" + System.lineSeparator();
		gordonTable += "\t<thead>" + System.lineSeparator()
			+ "\t\t<tr>" +  System.lineSeparator()
			+ "\t\t\t<th>Time</th>" +  System.lineSeparator()
			+ "\t\t\t<th>Dish</th>" +  System.lineSeparator()
			+ "\t\t\t<th>Actions</th>" +  System.lineSeparator()
			+ "\t\t\t<th>TimeLefts</th>" +  System.lineSeparator()
			+ "\t\t\t<th>Remarks</th>" +  System.lineSeparator()
			+ "\t\t</tr>" +  System.lineSeparator()
			+ "\t</thead>" +  System.lineSeparator();

		gordonTable += "\t<tbody>" + System.lineSeparator();
		int i = 1;
		for (Dish dish : gordonQueue){
			gordonTable += "\t\t<tr>" +  System.lineSeparator();
			gordonTable += "\t\t\t<td>" + i + "</td>" + System.lineSeparator();
			gordonTable += "\t\t\t<td>" + dish.name + " - " + dish.startTime + "</td>" + System.lineSeparator();
			gordonTable += "\t\t\t<td>" + System.lineSeparator();
			for(Action act : dish.aQueue) {
				gordonTable += "\t\t\t\t<p>" + act.name + " - " + act.timeLeft + "</p" + System.lineSeparator();
			}
			gordonTable += "\t\t\t</td>" + System.lineSeparator();
			i++;
			gordonTable += "\t\t</tr>" +  System.lineSeparator();
		}
		gordonTable += "\t</tbody>" + System.lineSeparator();
		gordonTable += "</table>" + System.lineSeparator();
	}

	static void initGordonHtml() throws IOException {
		FileReader outputFile = new FileReader(outputFileName);
		BufferedReader outputReader = new BufferedReader(outputFile);
		String htmlLine;
		while ( (htmlLine = outputReader.readLine() ) != null) {
			gordonHtml += htmlLine + System.lineSeparator();
		}
		outputReader.close();
		outputFile.close();
	}

	static void outputGordonHtml() throws IOException{
		FileWriter outputFile = new FileWriter(outputFileName);
		outputFile.write(gordonHtml);
		outputFile.close();
	}

	static void insertGordonTable() throws IOException{
		clearHtmlTable();

		String startTag = "<!--START HERE-->" + System.lineSeparator();
		StringBuilder gordonBuilder = new StringBuilder(gordonHtml);
		gordonBuilder.insert(gordonHtml.indexOf(startTag) + startTag.length(), gordonTable);
		gordonHtml = gordonBuilder.toString();
	}

	static void clearHtmlTable() {
		String startTag = "<!--START HERE-->" + System.lineSeparator();
		String endTag = "<!--END HERE-->";
		StringBuilder gordonBuilder = new StringBuilder(gordonHtml);
		int startIndex = gordonBuilder.indexOf(startTag) + startTag.length();
		int endIndex = gordonBuilder.indexOf(endTag);
		gordonBuilder.delete(startIndex, endIndex);
		gordonHtml = gordonBuilder.toString();
	}
}
