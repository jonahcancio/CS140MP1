import java.util.*;

public class Scheduler{
    int time;
    Dish dishBeingCooked;
    ArrayList<Dish> ReadyQueue = new ArrayList<Dish>();
    ArrayList<Dish> AssistantQueue = new ArrayList<Dish>();
    ArrayList<String> RemarksQueue = new ArrayList<String>();

    public Scheduler(){
        time = 0;
    }

    public Dish whatIsCookNext (Dish dishBeingCooked){
        Dish nextDish = new Dish("Hello");//this is only temporary so that the code compiles

        return nextDish;
    }

    public void incrementTime(){
        time = time + 1;
    }

    public void assistantUpdate(){//updates the assistant queue at everytime increment (do this before the cookingUpdate)
        for(Dish dish : AssistantQueue){//check for any dish done with current assistance task
            if(dish.aQueue.get(dish.currentActionIndex).timeLeft == 0){
                if(dish.aQueue.get(dish.currentActionIndex).name.equals("cook")){//if the next task requires cooking, remove it from the Assistant queue, add it to ready queue for cooking
                    AssistantQueue.remove(dish);
                    ReadyQueue.add(dish);
                }
            }
        }

        for(Dish dishAssisted : AssistantQueue){//subtracts one from all assistant actions
            Action i;
            i = dishAssisted.aQueue.get(dishAssisted.currentActionIndex);
            if(dishAssisted.aQueue.get(dishAssisted.currentActionIndex).timeLeft > 0){
                i.timeLeft--;
            }
        }

        for(Dish dish : AssistantQueue){//check for any dish done with current assistance task, move on to the next action, this has to be done after subtracting the currently on going assistance tasks
            if(dish.aQueue.get(dish.currentActionIndex).timeLeft == 0){
                dish.currentActionIndex++;
            }
        }
    }

    public void cookUpdate(){//updates ReadyQueue and current dish at every time increment
        if(dishBeingCooked.aQueue.get(dishBeingCooked.currentActionIndex).timeLeft == 0){//if dish is done cooking, move onto the next task
            dishBeingCooked.currentActionIndex += 1;
            while(!dishBeingCooked.aQueue.get(dishBeingCooked.currentActionIndex).equals("cook") && ReadyQueue.get(0) != null){//loop until we get a dish that needs cooking
                dishBeingCooked = ReadyQueue.get(0);
                if(!dishBeingCooked.aQueue.get(dishBeingCooked.currentActionIndex).equals("cook")){//if dish doesn't need cooking, move it to assistant Queue
                    AssistantQueue.add(dishBeingCooked);
                    dishBeingCooked = null;
                    ReadyQueue.remove(0);
                }
            }
        }else{//if not done cooking, subtract 1 from time
            dishBeingCooked.aQueue.get(dishBeingCooked.currentActionIndex).timeLeft--;
        }
    }
}
