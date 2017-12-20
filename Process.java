
public class Process {

	private int PID;
	private int arrivalTime;
	private int completionTime;
	private int cpuBurst;
	private int waitTime;
	private int responseTime;
	private int turnaroundTime;
	private int cpuBurstLeft;
	
	
	public Process(int pid, int arrival, int cpub) {
		setPID(pid);
		setarrivalTime(arrival);
		setcpuBurst(cpub);
		setCPUBurstLeft(cpub); //for round robin & srtf to keep track of remaining cpuBurst time when a process is preempted
							   //without overwriting original CPUBust data member
	}
	
	public int getPID(){
		return PID; //get Process ID
	}
	
	public void setPID(int pid){
		PID = pid; //set Process ID
	}
	
	public int getarrivalTime(){
		return arrivalTime; //get arrival time of process
	}
	
	public void setarrivalTime(int aT){
		arrivalTime = aT; //set arrival time of process
	}
	
	public int getcpuBurst(){
		return cpuBurst; //get CPU Burst of process
	}
	
	public void setcpuBurst(int cpub){
		cpuBurst = cpub; //set CPU burst of process
	}
	
	public int getcompletionTime(){
		return completionTime; //get the time when the process is finished with its CPU Burst
	}
	
	public void setcompletionTime(int cT){
		completionTime = cT; //set the time when the process is finished with its CPU Burst
	}
	
	public int getWaitTime(){
		return waitTime; //get the wait time of the process
	}
	
	public void setWaitTime(int wt){
		waitTime = wt; //set the wait time of the process
	}
		
	public int getResponseTime(){
		return responseTime; //get the response time of the process
	}
	
	public void setResponseTime(int rt){
		responseTime = rt; //set the response time of the process
	}
	
	public int getturnaroundTime(){
		return turnaroundTime;  //get the turnaround time of the process
	}
	
	public void setturnaroundTime(int tt){
		turnaroundTime = tt; //set the turnaround time of the process
	}
	
	public int getCPUBurstLeft(){
		return cpuBurstLeft; //the remaining count of CPU Burst left for a process (for RR and SRTF)
	}
	
	public void setCPUBurstLeft(int bl){
		cpuBurstLeft = bl; //set the remaining count of CPU Burst left for a process when preempted (for RR and SRTF)
	}


}
