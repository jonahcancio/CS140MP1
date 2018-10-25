public class RoundRobin extends Scheduler{

	int qMax;
	int csMax;
	int csTimeLeft;
	int qTimeLeft;
	boolean isContextSwitching;
	Dish dishMustRunNext;

	public RoundRobin(int q, int cs) {
		super();
		this.qMax = q;
		this.csMax = cs;
		this.qTimeLeft = q;
		isContextSwitching = false;
	}

	private void startContextSwitch() {
		isContextSwitching = true;
		csTimeLeft = csMax;

	}

	private void stopContextSwitch() {
		isContextSwitching = false;
		qTimeLeft = qMax;
	}

    public Dish whatIsCookNext(){
        Dish nextDish = ReadyQueue.get(0);//first come first served basis
        //System.out.print(nextDish.name);
        // ReadyQueue.remove(0);
        return nextDish;
    }

	@Override
	public String cookUpdate(){//updates ReadyQueue and current dish at every time increment
        String cookString = "";
		Action cookAction;
		if (isContextSwitching) {
			csTimeLeft--;
			if(csTimeLeft <= 0) {
				dishMustRunNext = null;
				if(!ReadyQueue.isEmpty()) {
					dishMustRunNext = ReadyQueue.get(0);
				}
				stopContextSwitch();
			}
		}

		if(dishBeingCooked != null) {
			cookAction = dishBeingCooked.aQueue.get(dishBeingCooked.currentActionIndex);
			cookAction.timeLeft--;
			qTimeLeft--;
	        if(cookAction.timeLeft <= 0) {//if dish is done cooking
	            cookString = dishBeingCooked.name + " cook done, ";
	            dishBeingCooked.currentActionIndex++;
	            if(dishBeingCooked.currentActionIndex < dishBeingCooked.aQueue.size()){
	                AssistantQueue.add(dishBeingCooked);
	            }
				dishBeingCooked = null;
				startContextSwitch();
	        }else if(qTimeLeft <= 0) {//if round robin term is done
				cookString = dishBeingCooked.name + " term ends, ";
				ReadyQueue.add(dishBeingCooked);
				dishBeingCooked = null;
				startContextSwitch();
			}
		}

		if (dishMustRunNext == null && !ReadyQueue.isEmpty()) {
			dishMustRunNext = ReadyQueue.get(0);
		}

		if(dishBeingCooked == null) {
			if(!isContextSwitching) {
				dishBeingCooked = dishMustRunNext;
				if(!ReadyQueue.isEmpty()) {
					ReadyQueue.remove(0);
				}
			}
		}

        return cookString;
    }
}
