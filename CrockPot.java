import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.io.*;
import java.lang.StringBuilder;

public class CrockPot {
	String outputFileName;
	String templateFileName;
	String taskFileName;
	String schedulingScheme;
	String recipesDirectory;

	ArrayList<Dish> gordonQueue;
	HashMap<String, Integer> gordonMap;
	String gordonTable;
	String gordonHtml;

	public CrockPot() {
		templateFileName = "template.html";
		outputFileName = "output.html";
		taskFileName = "tasklist.txt";
		recipesDirectory = ".";
		gordonQueue = new ArrayList<Dish>();
		gordonMap = new HashMap<String, Integer>();
		gordonTable = "";
		gordonHtml = "";
	}

	public CrockPot(String template, String output) {
		this();
		templateFileName = template;
		outputFileName = output;
	}

	public CrockPot(String recipes, String template, String output) {
		this();
		templateFileName = template;
		outputFileName = output;
		recipesDirectory = recipes;
	}

	public CrockPot(String recipes) {
		this();
		recipesDirectory = recipes;
	}

	public static void main(String args[]) throws IOException{
		CrockPot myself = new CrockPot("JandelOutput.html", "bazinga.html");

		myself.initGordonQueue();
		myself.consoleLogGordonQueue();
		// myself.initGordonTable();
		// System.out.println(myself.gordonTable);
		//
		// myself.htmlizeGordonTable();
		// // myself.washOutGordonFromHtml();

	}

	//reads tasklist and dish texts and initializes gordonQueue with all the Dishes and Actions, CALL THIS AT THE START of program
	public void initGordonQueue() throws IOException {
		FileReader taskFile = new FileReader(/*recipesDirectory + "/" +*/ taskFileName);
		BufferedReader taskReader = new BufferedReader(taskFile);

		String taskLine;
		schedulingScheme = taskReader.readLine();
		//System.out.println(schedulingScheme);
		while ((taskLine = taskReader.readLine()) != null) {
			String dishName = taskLine.split("\\s")[0];//get 1st word of taskLine
			gordonMap.put(dishName, gordonMap.containsKey(dishName)? gordonMap.get(dishName)+1 : 1);//adds 1 to map value if already in key map, else adds to key map with value of 1
			Dish newDish = new Dish(dishName + gordonMap.get(dishName)); //init dish with numbered name (<name><number>)

			newDish.startTime = Integer.parseInt(taskLine.split("\\s")[1]);//init startTime of dish with 2nd word of taskLine

			newDish.aQueue = new ArrayList<Action>();//init dish aQueue
			FileReader dishFile = new FileReader(recipesDirectory + "/" + dishName + ".txt");
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

//TABLE CREATION function start HERE

	//reset gordonTable string to nothing
	public void resetGordonTable() {
		gordonTable = "";
	}

	//append beginning table html tags to gordonTable string, ALWAYS CALL BEFORE the other gordonTable methods
	public void beginGordonTable(String s) {
		gordonTable += "<table class='ui celled padded striped red table'>" + System.lineSeparator();
	  gordonTable += "<thead><th align = \"center\" colspan = \"5\"><h1>Algorithm: " + s + "</h1></th></thead>" + System.lineSeparator();
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

	//append ending table html tags to gordonString, ALWAYS CALL AFTER the other gordonTable methods
	public void endGordonTable() {
		gordonTable += "\t</tbody>" + System.lineSeparator();
		gordonTable += "</table>" + System.lineSeparator();
	}

	//append beginning row html tags to gordonString, ALWAYS CALL BEFORE every row
	public void beginGordonRow() {
		gordonTable += "\t\t<tr>" +  System.lineSeparator();
	}

	//append ending row html tags to gordonString, ALWAYS CALL AFTER every row
	public void endGordonRow() {
		gordonTable += "\t\t</tr>" +  System.lineSeparator();
	}

	//append html column to gordongString with content specified in String content
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

//TABLE CREATION functions end HERE
//HTML reading and writing functions begin HERE

	//edits the output html file, inserts the gordonTable string between <!--START HERE--> and <!--END HERE-->
	public void htmlizeGordonTable() throws IOException{
		initGordonHtml();
		clearHtmlTable();
		insertGordonTable();
		outputGordonHtml();
	}

	//edits the output html file, removes all text between <!--START HERE--> and <!--END HERE-->
	public void washOutGordonFromHtml() throws IOException{
		initGordonHtml();
		clearHtmlTable();
		outputGordonHtml();
	}

	void initGordonHtml() throws IOException {
		FileReader templateFile = new FileReader(templateFileName);
		BufferedReader templateReader = new BufferedReader(templateFile);
		String htmlLine;
		gordonHtml = "";
		while ( (htmlLine = templateReader.readLine() ) != null) {
			gordonHtml += htmlLine + System.lineSeparator();
		}
		templateReader.close();
		templateFile.close();
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
//HTML reading and writing functions end HERE

//DEBUGGER functions begin HERE
	String taskListDemo = "adobo 1\n"
		+ "tinola 5\n"
		+ "karekare 8\n"
		+ "adobo 14\n";
	String recipeDemo = "cook 30\n"
		+ "mix 5\n"
		+ "cook 30\n"
		+ "sauce 7\n"
		+ "cook 10\n";

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
		beginGordonTable("asfd");
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

//DEBUGGER functions end HERE
}
