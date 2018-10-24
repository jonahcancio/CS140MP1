import java.util.Collections;
import java.util.Comparator;

public class SJF extends Scheduler{

	@Override
    public Dish whatIsCookNext(Dish dishBeingCooked){
        Dish nextDish = ReadyQueue.get(0); //ReadyQueue should already be sorted; first dish will have shortest job so take it
		ReadyQueue.remove(0); //pop out the Dish you took
        return nextDish;
    }

	@Override
	public String readyUpdate() {//adds additional dishes to the ReadyQueue based on the tasklist
        String readyString = super.readyUpdate(); //calls the original readyUpdate and maintains the same readyString
		Collections.sort(this.ReadyQueue, new SortByShortJob()); //sorts readyQueue by shortest job first
        return readyString;
    }

	//Comparator class to be used for sorting ReadyQueue
	private class SortByShortJob implements Comparator<Dish> {
		//implementing compare: should return negative if a comes before b, 0 if a equals b, and positive if a comes after b
	    @Override
	    public int compare(Dish a, Dish b) {
			Action aCook = a.aQueue.get(a.currentActionIndex); //get the cook action of Dish a
			Action bCook = b.aQueue.get(b.currentActionIndex); //get the cook action of Dish b
	        return aCook.timeLeft - bCook.timeLeft; //return result that matches criteria in comment above
	    }
	}
}
