public class FCFS extends Scheduler{

    public Dish whatIsCookNext(){
        Dish nextDish = ReadyQueue.get(0);//first come first served basis
        ReadyQueue.remove(0);
        return nextDish;
    }
    
}
