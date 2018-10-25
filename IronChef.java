import java.io.IOException;
import java.io.*;

public class IronChef{
    public static void main(String args[]) throws IOException{

      //Read desired scheduling scheme
        FileReader taskFile = new FileReader("./tasklist.txt");
        BufferedReader taskReader = new BufferedReader(taskFile);

        String schedulingScheme = "";
        schedulingScheme = taskReader.readLine();

        taskReader.close();
        taskFile.close();

        //Select appropriate scheduling scheme
        Scheduler sched = null;
        if(schedulingScheme.equals("FCFS")){
          sched = new FCFS();
        }else if(schedulingScheme.equals("PRIORITY")){
          sched = new PRIORITY();
        }

        sched.crockpot.initGordonQueue();
        //sched.crockpot.consoleLogGordonQueue();
        sched.crockpot.resetGordonTable();

        String readyString ="none";
        String cookString = "none";
        String assistantString="none";
        String remarksString="none";

        boolean isDone = false;

        //Create column labels
        sched.crockpot.beginGordonTable();
        int i = 1;
        while(true){
            //initialize string
            readyString ="";
            cookString = "";
            assistantString="";
            remarksString="";
            sched.crockpot.beginGordonRow();



            sched.incrementTime();

            remarksString = remarksString + sched.readyUpdate();//Updates Ready Queue


            remarksString = remarksString + sched.assistantUpdate();

            remarksString = remarksString + sched.cookUpdate();

            if(remarksString.equals("")){
                remarksString = "none";
            }
            //Debug
            // System.out.print(sched.crockpot.gordonQueue.isEmpty());
            // System.out.print(sched.AssistantQueue.isEmpty());
            // System.out.print(sched.dishBeingCooked == null);
            //System.out.print(remarksString + "\n");
            // if(sched.dishBeingCooked != null){
            // System.out.print(sched.dishBeingCooked.aQueue.get(sched.dishBeingCooked.currentActionIndex).timeLeft);
            // }
            // System.out.print(i);
            // i++;
            // System.out.print("\n");

            //String updates
            if(sched.AssistantQueue.isEmpty()){//Assistant
                assistantString = "none";
            }else{
                for(Dish assist: sched.AssistantQueue){
                    //System.out.println(assist.currentActionIndex);
                    if(!(assist.currentActionIndex >= assist.aQueue.size())){
                      assistantString = assistantString + assist.name + "(" + assist.aQueue.get(assist.currentActionIndex).name + "=" + assist.aQueue.get(assist.currentActionIndex).timeLeft + "), ";
                    }
                }
            }

            if(sched.dishBeingCooked != null && (sched.dishBeingCooked.currentActionIndex < sched.dishBeingCooked.aQueue.size())){//Cook
                cookString = sched.dishBeingCooked.name + ("(" + sched.dishBeingCooked.aQueue.get(sched.dishBeingCooked.currentActionIndex).name + "=" + Integer.toString(sched.dishBeingCooked.aQueue.get(sched.dishBeingCooked.currentActionIndex).timeLeft) + ")");
            }else{
                cookString = "none";
            }

            if(sched.ReadyQueue.isEmpty()){
                readyString = "none";
            }else{
                for(Dish ready : sched.ReadyQueue){//Ready
                    readyString = readyString + (ready.name + "(" + ready.aQueue.get(ready.currentActionIndex).name + "=" + ready.aQueue.get(ready.currentActionIndex).timeLeft + "), ");
                }
            }


            sched.crockpot.addGordonColumn(Integer.toString(sched.time));//update time column
            sched.crockpot.addGordonColumn(cookString);//update cook column
            sched.crockpot.addGordonColumn(readyString);//update ReadyColumn
            sched.crockpot.addGordonColumn(assistantString);//update assistant column
            sched.crockpot.addGordonColumn(remarksString);//update remarks column

            sched.crockpot.endGordonRow();

            if((sched.crockpot.gordonQueue.isEmpty()) && (sched.AssistantQueue.isEmpty()) && (sched.ReadyQueue.isEmpty()) && (sched.dishBeingCooked == null)){//if no more dishes in tasklist, no more dishes being assisted and cooked, finish
                break;
            }

            sched.crockpot.htmlizeGordonTable();
        }
        sched.crockpot.endGordonTable();
        sched.crockpot.htmlizeGordonTable();

    }
}
