import java.io.IOException;

public class IronChef{
    public static void main(String args[]) throws IOException{
        Scheduler sched = new FCFS();
        sched.crockpot.initGordonQueue();
        sched.crockpot.consoleLogGordonQueue();
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


            sched.readyUpdate();//Updates Ready Queue


            sched.assistantUpdate();

            sched.cookUpdate();

            if((sched.crockpot.gordonQueue.isEmpty()) && (sched.AssistantQueue.isEmpty()) && (sched.dishBeingCooked == null)){//if no more dishes in tasklist, no more dishes being assisted and cooked, finish
                break;
            }

            //Debug
            // System.out.print(sched.crockpot.gordonQueue.isEmpty());
            // System.out.print(sched.AssistantQueue.isEmpty());
            // System.out.print(sched.dishBeingCooked == null);
            // if(sched.dishBeingCooked != null){
            // System.out.print(sched.dishBeingCooked.aQueue.get(sched.dishBeingCooked.currentActionIndex).timeLeft);
            // }
            // System.out.print(i);
            // i++;
            // System.out.print("\n");

            //String updates
            for(Dish assist: sched.AssistantQueue){
                assistantString = assistantString + assist.name + "(" + assist.aQueue.get(assist.currentActionIndex).name + "=" + assist.aQueue.get(assist.currentActionIndex).timeLeft + "), ";
            }

            if(sched.dishBeingCooked != null && (sched.dishBeingCooked.currentActionIndex < sched.dishBeingCooked.aQueue.size())){
                cookString = sched.dishBeingCooked.name + ("(" + sched.dishBeingCooked.aQueue.get(sched.dishBeingCooked.currentActionIndex).name + "=" + Integer.toString(sched.dishBeingCooked.aQueue.get(sched.dishBeingCooked.currentActionIndex).timeLeft) + ")");
            }else{
                cookString = "none";
            }


            readyString = "";
            for(Dish ready : sched.ReadyQueue){
                readyString = readyString + (ready.name + "(" + ready.aQueue.get(ready.currentActionIndex).name + "=" + ready.aQueue.get(ready.currentActionIndex).timeLeft + "), ");
            }

            sched.crockpot.addGordonColumn(Integer.toString(sched.time));//update time column
            sched.crockpot.addGordonColumn(cookString);//update cook column
            sched.crockpot.addGordonColumn(readyString);//update ReadyColumn
            sched.crockpot.addGordonColumn(assistantString);//update assistant column
            sched.crockpot.addGordonColumn(remarksString);//update remarks column

            sched.crockpot.endGordonRow();
        }
        sched.crockpot.htmlizeGordonTable();

    }
}