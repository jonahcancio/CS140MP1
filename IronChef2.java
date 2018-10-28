import java.io.IOException;
import java.io.*;

public class IronChef2{
    public static void main(String args[]) throws IOException {
        CrockPot crockpot = new CrockPot("recipes"); //initialize crockpot to read tasklist from recipes directory

        //Read desired scheduling scheme
        crockpot.initGordonQueue(); //DO THIS FIRST BEFORE INITIALIZING SCHEDULER; IT SETS crockpot.schedulingScheme accordingly
        //crockpot.consoleLogGordonQueue();
        crockpot.resetGordonTable();

        //Select appropriate scheduling scheme
        Scheduler sched = null;
        if(crockpot.schedulingScheme.equals("FCFS")) {
            sched = new FCFS(crockpot);
        } else if(crockpot.schedulingScheme.equals("PRIORITY")) {
            sched = new PRIORITY(crockpot);
        } else if(crockpot.schedulingScheme.equals("SJF")) {
            sched = new SJF(crockpot);
        } else { //check for round robin
            String rr[] = crockpot.schedulingScheme.split(" ");
            if (rr.length == 3) { //see if 3 arguments given
                int q = Integer.parseInt(rr[1]);
                int cs = Integer.parseInt(rr[2]);
                sched = new RoundRobin(crockpot, q, cs);
            } else { //terminate on error
                System.err.println("Invalid input :(");
                System.exit(1);
            }
        }

        String readyString ="none";
        String cookString = "none";
        String assistantString="none";
        String remarksString="none";

        boolean isDone = false;


        if(crockpot.schedulingScheme.equals("FCFS")) {
            crockpot.beginGordonTable(crockpot.schedulingScheme);
        } else if(crockpot.schedulingScheme.equals("PRIORITY")) {
            crockpot.beginGordonTable(crockpot.schedulingScheme);
        } else if(crockpot.schedulingScheme.equals("SJF")) {
            crockpot.beginGordonTable(crockpot.schedulingScheme);
        } else { //check for round robin
            String rr[] = crockpot.schedulingScheme.split(" ");
            if (rr.length == 3) { //see if 3 arguments given
                crockpot.beginGordonTable("RR (quantum = " + rr[1] + ", context switch = " + rr[2] + ")");
            } else { //terminate on error
                System.err.println("Invalid input :(");
                System.exit(1);
            }
        }

        //Create column labels
        //crockpot.beginGordonTable();
        int i = 1;
        while(true){
            //initialize string
            readyString ="";
            cookString = "";
            assistantString="";
            remarksString="";
            crockpot.beginGordonRow();



            sched.incrementTime();

            remarksString = remarksString + sched.readyUpdate();//Updates Ready Queue


            remarksString = remarksString + sched.assistantUpdate();

            remarksString = remarksString + sched.cookUpdate();

            if(remarksString.equals("")){
                remarksString = "none";
            }
            //Debug
            // System.out.print(crockpot.gordonQueue.isEmpty());
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

            if(sched.isContextSwitching == true){
                cookString = "context switch";
                remarksString = "CS. " + remarksString;
            }else if(sched.dishBeingCooked != null && (sched.dishBeingCooked.currentActionIndex < sched.dishBeingCooked.aQueue.size())){//Cook
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

            crockpot.addGordonColumn(Integer.toString(sched.time));//update time column
            crockpot.addGordonColumn(cookString);//update cook column
            crockpot.addGordonColumn(readyString);//update ReadyColumn
            crockpot.addGordonColumn(assistantString);//update assistant column
            crockpot.addGordonColumn(remarksString);//update remarks column

            crockpot.endGordonRow();

            if((crockpot.gordonQueue.isEmpty()) && (sched.AssistantQueue.isEmpty()) && (sched.ReadyQueue.isEmpty()) && (sched.dishBeingCooked == null)){//if no more dishes in tasklist, no more dishes being assisted and cooked, finish
                break;
            }

            crockpot.htmlizeGordonTable();
        }
        crockpot.endGordonTable();
        crockpot.htmlizeGordonTable();

    }
}
