import java.util.*;

public class PRIORITY extends Scheduler{

  ArrayList<Dish> alphabetSortDishes(ArrayList<Dish> dishList){//sort dishes in list alphabetically
    ArrayList<Dish> sortedDishList = new ArrayList<Dish>();

    while(dishList.get(0) != null){//Iterate until list is empty (meaning all elements are in the output list)
      //Finding the "smallest" dish
      int key;
      Dish ref;
      key = 0;
      ref = dishList.get(0);

      for(int i = 0; i<dishList.size(); i++){
        Dish dish = dishList.get(i);

        if(ref.name.compareTo(dish.name) > 0){//If we find a "smaller" string
          key = i;
          ref = dish;
        }

        }
      }

      dishList.remove(key);//remove "smallest" dish from old list
      sortedDishList.add(ref);//add "smallest" dish to new list
    }
    return sortedDishList;
  }

  public Dish whatIsCookNext(Dish dishBeingCooked, ArrayList<Dish> dishList){
      Dish nextDish;
      //sort dishList alphabetically
      dishList = alphabetSortDishes(dishList);

      //get 0
      nextDish = dishList.get(0);
      return nextDish;
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
          dishBeingCooked = whatIsCookNext(dishBeingCooked, ReadyQueue);
      }else if(dishBeingCooked != null){//if there is a dish cooking, subtract one from its time
          dishBeingCooked.aQueue.get(dishBeingCooked.currentActionIndex).timeLeft--;
      }

      return cookString;
  }
}
