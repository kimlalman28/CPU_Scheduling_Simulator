import java.util.ArrayList;

public class RR extends Scheduler{

	private int systemCount;
	private int idleCount;

	public RR(){
		super(); //class parent class Scheduler
		systemCount = 0; //set time in system to 0
		idleCount = 0; //counter for time no process is allocated the cpu
	}
	
	
	public void runRR(ArrayList<Process> jobs, int timeQuantum){
		double avgCPUU;
		double avgWT;
		double avgRT;
		double avgTT;
		System.out.println();
		System.out.println("Scheduling Algorithm: Round Robin");
		System.out.println("=========================================================");
		
		roundRobin(jobs, timeQuantum); //runs the round robin algorithm
		
		//calculate averages
		avgCPUU = averageCPUUsage(); //calculates average CPU Usage
		avgWT = averageWaitTime(jobs); //calculates average Wait Time
		avgRT = averageResponseTime(jobs); //calculates average Response Time
		avgTT = averageTurnaroundTime(jobs); //calculates average Turnaround Time
		
		System.out.println("<system time   " + systemCount + "> All processes finished...");
		System.out.println("=========================================================");
		System.out.print("Average CPU usage: ");
			System.out.printf("%.2f", avgCPUU);	
			System.out.println("%");
		System.out.print("Average waiting time: ");
			System.out.printf("%.2f", avgWT);
			System.out.println();
		System.out.print("Average response time: ");
			System.out.printf("%.2f", avgRT);
			System.out.println();
		System.out.print("Average turnaround time: ");
			System.out.printf("%.2f", avgTT);
			System.out.println();
		System.out.println("=========================================================");
	}
	
	public void roundRobin(ArrayList<Process> jobs, int tq){
		int totalProcesses = jobs.size(); //will store the number of distinct processes that will be allocated the cpu
		int job=0; //first job to arrive
		Process currProcess;
		
		//loop will continue if the first process that will be put in the Ready Queue arrives at a time later than 0
		while(jobs.get(job).getarrivalTime() != systemCount){
			System.out.println("<system time   " + systemCount + "> idle");
			idleCount++;
			systemCount++;
		}
		
		super.addProcess(jobs.get(job)); //add the job to the Ready Queue
		job++; //increment to next job that will arrive
		currProcess = super.getProcess(0); //set current process to the first process in the ReadyQueue
		currProcess.setResponseTime(systemCount - currProcess.getarrivalTime()); //set the ResponseTime
		// while loop to check if multiple processes have the same arrival time
		while((job != totalProcesses) && (jobs.get(job).getarrivalTime() == systemCount)){ 
		super.addProcess(jobs.get(job)); //add job(s) to the ReadyQueue
		job++; //increment to next job that will arrive
	}
		//outer while loop will keep looping until a condition is met (all processes have completed their cpu burst)
		while(true){
			//this loop will check if the Ready Queue is empty BUT there are still processes 
			//in the job pool that still need to arrive to be placed in the ReadyQueue
			while((job != totalProcesses) && super.readyQueueEmpty()){
				if(jobs.get(job).getarrivalTime() == systemCount){
					super.addProcess(jobs.get(job)); //add the process to the Ready Queue 
					currProcess = super.getProcess(0); //since the RQ was empty, this is now the process at the head of the RQ
					currProcess.setResponseTime(systemCount - currProcess.getarrivalTime());
					job++;
					break; 
				}
				else{
					//if no process arrives, cpu is in idle
					System.out.println("<system time   " + systemCount + "> idle");
					idleCount++;
					systemCount++;
				}
			}
			//for loop will depend on the Time Quantum (tq)
			for(int i=0; i<tq; i++){	
				//if the process has not completed it cpu burst, it should run
				if(currProcess.getCPUBurstLeft() != 0){
					System.out.println("<system time   " + systemCount + "> process    " + currProcess.getPID() +" is running" );
					systemCount++;
					//update the amount of cpu burst is left for the process
					currProcess.setCPUBurstLeft(currProcess.getCPUBurstLeft()-1);
					
					// while loop to check if multiple processes have the same arrival time, check for any new processes
					while((job != totalProcesses) && (jobs.get(job).getarrivalTime() == systemCount)){
						super.addProcess(jobs.get(job));
						job++;
					}
					//if process has completed its cpu burst
					if(currProcess.getCPUBurstLeft() == 0){
						currProcess.setcompletionTime(systemCount); //set complete time of process
						currProcess.setturnaroundTime(currProcess.getcompletionTime() - currProcess.getarrivalTime()); //set turnaround time
						currProcess.setWaitTime(currProcess.getcompletionTime() - (currProcess.getarrivalTime() + currProcess.getcpuBurst())); //set wait time
						System.out.println("<system time   " + systemCount + "> process    " + currProcess.getPID() +" is finished....." );
						break; //break from for loop since process does not need to complete tq
					}
				}
			}// end for loop
			
			if(!super.readyQueueEmpty()){
				//if there are multiple processes in the readyQueue, when the tq is completed, one process switches to another
				//this would inform when the switch occurs and with which processes
				if(super.readyQueueSize() > 1){
					System.out.println("<system time   " + systemCount + "> switching from process " + currProcess.getPID() + " to process " +super.getProcess(1).getPID());
				}
				//if current process if complete, remove the process from the RQ
				if(currProcess.getCPUBurstLeft() == 0){
					super.removeProcess(0);
					//if RQ is not empty, get the next process currently at the head of the Q
					if(!super.readyQueueEmpty()){
						currProcess = super.getProcess(0);
						if(currProcess.getCPUBurstLeft() == currProcess.getcpuBurst()){
							currProcess.setResponseTime(systemCount - currProcess.getarrivalTime());
						}
					}
				}
				else{
					//if no process was removed, rotate the current process at the head of the RQ to the end of the RQ
					super.rotateReadyQueue();
					currProcess = super.getProcess(0);
					//if this is the first time this process is running, set the response time for that process
					if(currProcess.getCPUBurstLeft() == currProcess.getcpuBurst()){
						currProcess.setResponseTime(systemCount - currProcess.getarrivalTime());
					}
				}					
			}
			//if the ready queue is empty and all jobs have completed its cpu burst, break out of out while loop
			else if(super.readyQueueEmpty() && job == totalProcesses){
				break;
			}
		} //end while loop
	} //end rr method
	
	//calculate average CPU Usage
	private double averageCPUUsage(){
		double avgCPUU = ((double)(systemCount-idleCount)/systemCount);
		return (100.0*avgCPUU);
	}
	
	//calculate average Wait Time
	private double averageWaitTime(ArrayList<Process> jobs){
		int sum=0;
		double avgWT = 0.0;
		for(int i=0; i<jobs.size(); i++){
			sum = sum + jobs.get(i).getWaitTime();
		}
		avgWT = (double)sum / jobs.size();
		return avgWT;
	}
	
	//calculate average Response Time
	private double averageResponseTime(ArrayList<Process> jobs){
		int sum=0;
		double avgRT = 0.0;
		for(int i=0; i<jobs.size(); i++){
			sum = sum + jobs.get(i).getResponseTime();
		}
		avgRT = (double)sum / jobs.size();
		return Math.round(avgRT);
	}
	
	//calculate average Turnaround Time
	private double averageTurnaroundTime(ArrayList<Process> jobs){
		int sum=0;
		double avgTT = 0.0;
		for(int i=0; i<jobs.size(); i++){
			sum = sum + jobs.get(i).getturnaroundTime();
		}
		avgTT = (double)sum / jobs.size();
		return avgTT;
	}
}
