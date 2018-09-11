import java.io.IOException;

public class IronChef{
    public static void main(String args[]) throws IOException{
        Scheduler sched = new FCFS();
        sched.crockpot.initGordonQueue();
        sched.crockpot.consoleLogGordonQueue();
        sched.crockpot.resetGordonTable();

        String readyString ="";
        String cookString ="";
        String assistantString="";
        String remarksString="";
        //Create column labels
        sched.crockpot.beginGordonTable();

        while(true){
            sched.crockpot.beginGordonRow();

            if((sched.crockpot.gordonQueue.isEmpty()) && (sched.AssistantQueue.isEmpty()) && (sched.dishBeingCooked == null)){//if no more dishes in tasklist, no more dishes being assisted and cooked, finish
                break;
            }

            sched.incrementTime();


            sched.readyUpdate();//Updates Ready Queue

            readyString = "";
            for(Dish ready : sched.ReadyQueue){
                readyString = readyString + (ready.name + "(" + ready.aQueue.get(0).name + "), ");
            }


            sched.assistantUpdate();
            sched.cookUpdate();

            sched.crockpot.addGordonColumn(Integer.toString(sched.time));//update time column
            sched.crockpot.addGordonColumn(cookString);//update cook column
            sched.crockpot.addGordonColumn(readyString);//update ReadyColumn
            sched.crockpot.addGordonColumn(assistantString);//update assistant column
            sched.crockpot.addGordonColumn(remarksString);//update remarks column

            sched.crockpot.endGordonRow();

            sched.crockpot.htmlizeGordonTable();
        }

    }
}
