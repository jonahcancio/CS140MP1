import java.util.*;

public class PRIORITY extends Scheduler{

    public PRIORITY(CrockPot crockpot) {
        super(crockpot);
    }

  ArrayList<Dish> alphabetSortDishes(ArrayList<Dish> dishList){//sort dishes in list alphabetically
    //ArrayList<Dish> sortedDishList = new ArrayList<Dish>();
    if(!dishList.isEmpty()){
      // while(!dishList.isEmpty()){//Iterate until list is empty (meaning all elements are in the output list)
      //   //Finding the "smallest" dish
      //   int key;
      //   Dish ref;
      //   key = 0;
      //   ref = dishList.get(0);
      //
      //   for(int i = 0; i<dishList.size(); i++){
      //     Dish dish = dishList.get(i);
      //     if(ref.name.compareTo(dish.name) > 0){//If we find a "smaller" dish
      //       key = i;
      //       ref = dish;
      //     }
      //   }
      //   dishList.remove(key);//remove "smallest" dish from old list
      //   sortedDishList.add(ref);//add "smallest" dish to new list
      // }

      Collections.sort(dishList, new SortbyName());
    }else{
      System.out.println("hello");
      return dishList;
    }
    return dishList;
  }

  private class SortbyName implements Comparator<Dish>{
    public int compare(Dish a, Dish b){
      String aName = a.name;
      String bName = b.name;
      return aName.compareTo(b.name);
    }
  }

    public Dish whatIsCookNext(Dish dishBeingCooked){
        Dish nextDish;
        nextDish = ReadyQueue.get(0);
        ReadyQueue.remove(0);
        return nextDish;
    }

    public String readyUpdate(){//adds additional dishes to the ReadyQueue based on the tasklist
        String readyString = "";
        int i = 0;
        while(i < crockpot.gordonQueue.size()){
            Dish dish = crockpot.gordonQueue.get(i);
            if(time == dish.startTime){
                readyString = readyString + dish.name + " arrives, ";
                ReadyQueue.add(dish);
                ReadyQueue = alphabetSortDishes(ReadyQueue);
                crockpot.gordonQueue.remove(i);
            }else{
                i++;
            }
        }

        return readyString;
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
            //System.out.println(dish.currentActionIndex);
            if(dish.aQueue.get(dish.currentActionIndex).timeLeft == 1){
                assistantString = assistantString + dish.name + " " + dish.aQueue.get(dish.currentActionIndex).name + " done, ";
                dish.currentActionIndex++;
                //System.out.println(dish.currentActionIndex);
                if(dish.currentActionIndex >= dish.aQueue.size()){//if no more actions left, remove from queue forever
                    AssistantQueue.remove(i);
                    assistantString = assistantString + dish.name + " " + " dish done, ";
                }else{
                    ReadyQueue.add(dish);
                    ReadyQueue = alphabetSortDishes(ReadyQueue);
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
                //System.out.println(dish.currentActionIndex);
            }
        }

        return assistantString;
    }
}
