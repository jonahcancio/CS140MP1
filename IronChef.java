public class IronChef{
    public static void main(String args[]){
        CrockPot crockpot = new CrockPot();

        boolean isDone = false;

        Scheduler sched = new FCFS();

        while(!isDone){
            sched.incrementTime();
            sched.readyUpdate();
            sched.assistantUpdate();
            sched.cookUpdate();

            if((crockpot.gordonQueue.get(0) == null) && (sched.AssistantQueue.get(0) == null) && (sched.dishBeingCooked == null)){//if no more dishes in tasklist, no more dishes being assisted and cooked, finish
                isDone = true;
            }
        }
    }
}
