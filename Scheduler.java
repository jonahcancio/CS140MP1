import java.util.*;

public class Scheduler{
    int time;
    Dish dishBeingCooked;
    ArrayList<Dish> ReadyQueue = new ArrayList<Dish>();
    ArrayList<Dish> AssistantQueue = new ArrayList<Dish>();
    ArrayList<String> RemarksQueue = new ArrayList<String>();
    ArrayList<Integer> IndexToBeRemoved= new ArrayList<Integer>();

    CrockPot crockpot;

    public Scheduler(){
        time = 0;
        crockpot = new CrockPot();
    }

    public Dish whatIsCookNext (Dish dishBeingCooked){
        return null;//will be decided by the type of scheduling (in this case FCFS)
    }

    public void incrementTime(){
        time = time + 1;
    }

    public String assistantUpdate(){//updates the assistant queue at everytime increment (do this before the cookingUpdate)
        String assistantString = "";
        int i = 0;
        while(i < ReadyQueue.size()){//look for any dishes that require assistance
            if(!ReadyQueue.get(i).aQueue.get(ReadyQueue.get(i).currentActionIndex).name.equals("cook")){
                AssistantQueue.add(ReadyQueue.get(i));
                ReadyQueue.remove(i);
            }else{
                i++;
            }
        }

        i = 0;
        while(i < AssistantQueue.size()){//check for any dish done with current assistance task
            Dish dish = AssistantQueue.get(i);
            if(dish.aQueue.get(dish.currentActionIndex).timeLeft == 1){
                assistantString = assistantString + dish.name + " " + dish.aQueue.get(dish.currentActionIndex).name + " done, ";
                dish.currentActionIndex++;
                if(dish.currentActionIndex >= dish.aQueue.size()){//if no more actions left, remove from queue forever
                    AssistantQueue.remove(i);
                    assistantString = assistantString + dish.name + " " + " dish done, ";
                }else{
                    ReadyQueue.add(dish);
                    AssistantQueue.remove(i);
                }
            }else{
            i++;
            }
            //System.out.print(i);
        }

        for(Dish dishAssisted : AssistantQueue){//subtracts one from all assistant actions
            Action a;
            a = dishAssisted.aQueue.get(dishAssisted.currentActionIndex);
            if(dishAssisted.aQueue.get(dishAssisted.currentActionIndex).timeLeft > 0){
                a.timeLeft--;
            }
        }

        for(Dish dish : AssistantQueue){//check for any dish done with current assistance task, move on to the next action, this has to be done after subtracting the currently on going assistance tasks
            if(dish.aQueue.get(dish.currentActionIndex).timeLeft == 0){
                dish.currentActionIndex++;
            }
        }

        return assistantString;
    }

    public String cookUpdate(){//updates ReadyQueue and current dish at every time increment
        String cookString = "";
        if(dishBeingCooked != null && dishBeingCooked.aQueue.get(dishBeingCooked.currentActionIndex).timeLeft == 1){//if dish is done cooking
            cookString = dishBeingCooked.name + " cook done, ";
            dishBeingCooked.currentActionIndex++;
            if(dishBeingCooked.currentActionIndex < dishBeingCooked.aQueue.size()){
                AssistantQueue.add(dishBeingCooked);
            }
            dishBeingCooked = null;
        }

        if(dishBeingCooked == null && !ReadyQueue.isEmpty()){//if there is no dish being cooked and there are dishes to be cooked, cook next dish
            dishBeingCooked = whatIsCookNext(dishBeingCooked);
        }else if(dishBeingCooked != null){//if there is a dish cooking, subtract one from its time
            dishBeingCooked.aQueue.get(dishBeingCooked.currentActionIndex).timeLeft--;
        }

        return cookString;
    }

    public String readyUpdate(){//adds additional dishes to the ReadyQueue based on the tasklist
        String readyString = "";
        int i = 0;
        while(i < crockpot.gordonQueue.size()){
            Dish dish = crockpot.gordonQueue.get(i);
            if(time == dish.startTime){
                readyString = readyString + dish.name + " arrives, ";
                ReadyQueue.add(dish);
                crockpot.gordonQueue.remove(i);
            }else{
                i++;
            }
        }

        return readyString;
    }
}
