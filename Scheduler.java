
public class Scheduler {
	private CircularLinkedList<Process> readyQueue;

	
	public Scheduler(){
		//constructor of class
		//when super() is called from children classes, a new CLL of Processes is created
		readyQueue = new CircularLinkedList<Process>();
	}
	
	public void addProcess(Process p){
		readyQueue.add(p); //add a process to the ready queue
	}
	
	public void removeProcess(int p){
		readyQueue.remove(p); //remove a process from the ready queue
	}
	
	public Process getProcess(int p){
		return readyQueue.get(p); //get a process from a specific position in the ready queue
	}
	
	public boolean readyQueueEmpty(){
		return readyQueue.isEmpty(); //check if the ready queue is empty
	}
	
	public int readyQueueSize(){
		return readyQueue.size(); //get the size of the ready queue
	}
	
	public void rotateReadyQueue(){
		readyQueue.rotate(); //rotate the process at the from of the ready queue to the back of the ready queue (RR)
	}
	
	public int getShortestProcessIndex(Process p){
		return readyQueue.indexOf(p); //get the position of a process in the ready queue (SRTF)
	}

}

