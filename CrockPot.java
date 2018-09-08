import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.io.*;
import java.lang.StringBuilder;

public class CrockPot {
	String outputFileName;
	String taskFileName;
	String taskListDemo = "adobo 1\n"
		+ "tinola 5\n"
		+ "karekare 8\n"
		+ "adobo 14\n";
	String recipeDemo = "cook 30\n"
		+ "mix 5\n"
		+ "cook 30\n"
		+ "sauce 7\n"
		+ "cook 10\n";
	ArrayList<Dish> gordonQueue;
	HashMap<String, Integer> gordonMap;
	String gordonTable;
	String gordonHtml;

	public CrockPot() {
		outputFileName = "JandelOutput.html";
		taskFileName = "recipes/Tasklist.txt";
		gordonQueue = new ArrayList<Dish>();
		gordonMap = new HashMap<String, Integer>();
		gordonTable = "";
		gordonHtml = "";
	}

	public static void main(String args[]) throws IOException{
		CrockPot myself = new CrockPot();

		myself.initGordonQueue();
		myself.initGordonTable();
		System.out.println(myself.gordonTable);

		myself.htmlizeGordonTable();
		// myself.washOutGordonFromHtml();

	}


	public void initGordonQueue() throws IOException {
		FileReader taskFile = new FileReader(taskFileName);
		BufferedReader taskReader = new BufferedReader(taskFile);

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

		taskReader.close();
		taskFile.close();
	}

	public void resetGordonTable() {
		gordonTable = "";
	}

	public void beginGordonTable() {
		gordonTable += "<table class='ui celled padded table'>" + System.lineSeparator();
		gordonTable += "\t<thead>" + System.lineSeparator()
			+ "\t\t<tr>" +  System.lineSeparator()
			+ "\t\t\t<th>Time</th>" +  System.lineSeparator()
			+ "\t\t\t<th>Cook</th>" +  System.lineSeparator()
			+ "\t\t\t<th>Ready</th>" +  System.lineSeparator()
			+ "\t\t\t<th>Assistants</th>" +  System.lineSeparator()
			+ "\t\t\t<th>Remarks</th>" +  System.lineSeparator()
			+ "\t\t</tr>" +  System.lineSeparator()
			+ "\t</thead>" +  System.lineSeparator();
		gordonTable += "\t<tbody>" + System.lineSeparator();
	}

	public void endGordonTable() {
		gordonTable += "\t</tbody>" + System.lineSeparator();
		gordonTable += "</table>" + System.lineSeparator();
	}

	public void beginGordonRow() {
		gordonTable += "\t\t<tr>" +  System.lineSeparator();
	}

	public void endGordonRow() {
		gordonTable += "\t\t</tr>" +  System.lineSeparator();
	}

	public void addGordonColumn(String content) {
		String startTag = "";
		String endTag = "";
		if (content.indexOf(System.lineSeparator()) < 0) {
			startTag =  "\t\t\t<td>";
			endTag = "</td>";
		} else {
			startTag =  "\t\t\t<td>" + System.lineSeparator();
			endTag = "\t\t\t</td>";
		}
		gordonTable += startTag + content + endTag + System.lineSeparator();
	}

//html reading and writing functions begin HERE

	public void htmlizeGordonTable() throws IOException{
		initGordonHtml();
		clearHtmlTable();
		insertGordonTable();
		outputGordonHtml();
	}

	public void washOutGordonFromHtml() throws IOException{
		initGordonHtml();
		clearHtmlTable();
		outputGordonHtml();
	}

	void initGordonHtml() throws IOException {
		FileReader outputFile = new FileReader(outputFileName);
		BufferedReader outputReader = new BufferedReader(outputFile);
		String htmlLine;
		gordonHtml = "";
		while ( (htmlLine = outputReader.readLine() ) != null) {
			gordonHtml += htmlLine + System.lineSeparator();
		}
		outputReader.close();
		outputFile.close();
	}

	void clearHtmlTable() {
		String startTag = "<!--START HERE-->" + System.lineSeparator();
		String endTag = "<!--END HERE-->";
		StringBuilder gordonBuilder = new StringBuilder(gordonHtml);
		int startIndex = gordonBuilder.indexOf(startTag) + startTag.length();
		int endIndex = gordonBuilder.indexOf(endTag);
		gordonBuilder.delete(startIndex, endIndex);
		gordonHtml = gordonBuilder.toString();
	}

	void insertGordonTable() throws IOException{
		String startTag = "<!--START HERE-->" + System.lineSeparator();
		StringBuilder gordonBuilder = new StringBuilder(gordonHtml);
		gordonBuilder.insert(gordonHtml.indexOf(startTag) + startTag.length(), gordonTable);
		gordonHtml = gordonBuilder.toString();
	}

	void outputGordonHtml() throws IOException{
		FileWriter outputFile = new FileWriter(outputFileName);
		outputFile.write(gordonHtml);
		outputFile.close();
	}
//html reading and writing functions end HERE

//debuf functions begin HERE
	void stringDemoLoop() {
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

	void consoleLogGordonQueue() {
		for (Dish dish : gordonQueue){
			System.out.println(dish.name + " - " + dish.startTime +":");
			for(Action act : dish.aQueue) {
				System.out.println("\t=> " + act.name + " - " + act.timeLeft);
			}
			System.out.println("");
		}
	}

	void initGordonTable() {
		resetGordonTable();
		beginGordonTable();
		int i = 0;
		for (Dish dish : gordonQueue){
			beginGordonRow();
			addGordonColumn(Integer.toString(dish.startTime));
			addGordonColumn(dish.name);
			String actNamesString = "";
			String actTimesString = "";
			for(Action act : dish.aQueue) {
				actNamesString += "\t\t\t\t<p>" + act.name + "</p>" + System.lineSeparator();
				actTimesString += "\t\t\t\t<p>" + act.timeLeft + "</p>" + System.lineSeparator();
			}
			addGordonColumn(actNamesString);
			addGordonColumn(actTimesString);
			i++;
			endGordonRow();
		}
		endGordonTable();
	}

//debug functions end HERE
}
