public class FCFS extends Scheduler{

    public FCFS(CrockPot crockpot) {
        super(crockpot);
    }

    public Dish whatIsCookNext(Dish dishBeingCooked){
        Dish nextDish = ReadyQueue.get(0);//first come first served basis
        //System.out.print(nextDish.name);
        ReadyQueue.remove(0);
        return nextDish;
    }

}
