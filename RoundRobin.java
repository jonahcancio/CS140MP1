public class RoundRobin extends Scheduler{

	int qMax; //the q from input file
	int csMax; //the cs from input file
	int csTimeLeft; //used to time context switch
	int qTimeLeft; //used to time cook quantums
	//boolean isContextSwitching;
	Dish dishBuffer; //Buffer

	public RoundRobin(CrockPot crockpot, int q, int cs) {
		super(crockpot);
		this.qMax = q;
		this.csMax = cs;
		this.qTimeLeft = q;
		isContextSwitching = false;
	}

	//called whenever context switching should start
	private void stopContextSwitch() {
		isContextSwitching = false;
		qTimeLeft = qMax;
	}

	//called whenever context switching should end
	private void startContextSwitch() {
		isContextSwitching = true;
		csTimeLeft = csMax;
		dishBuffer = whatIsCookNext();
	}

	//returns first item in readyQueue if it exists else returns null
	private Dish whatIsCookNext() {
		if (ReadyQueue.isEmpty()) {
			return null;
		} else {
			return ReadyQueue.get(0);
		}
	}

	@Override
	public String cookUpdate(){//updates ReadyQueue and current dish at every time increment
        String cookString = "";
		Action cookAction;


		if(dishBeingCooked != null) { //if a dish is current being cooked
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
		}else if(isContextSwitching) { //if no dish is cooking because context switch ongoing
			csTimeLeft--;
			if(csTimeLeft <= 0) { //if context switch time has ended
				stopContextSwitch();
			}
		}

		if (dishBuffer == null) { //if program just started and no dish in buffer yet
			dishBuffer = whatIsCookNext();
		}

		if(dishBeingCooked == null && !isContextSwitching && dishBuffer != null) { //if no dish is being cooked, no longer context switching, and next dish available in buffer
			dishBeingCooked = dishBuffer;
			cookString = dishBeingCooked.name + " chosen, ";
			if(!ReadyQueue.isEmpty()) { //remove dish in dishBuffer from queue
				ReadyQueue.remove(dishBuffer);
			}
		}

        return cookString;
    }
}
