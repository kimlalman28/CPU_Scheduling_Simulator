import java.util.ArrayList;

public class SRTF extends Scheduler{

	private int systemCount;
	private int idleCount;

	public SRTF(){
		super(); //class parent class Scheduler
		systemCount = 0; //set time in system to 0
		idleCount = 0; //counter for time no process is allocated the cpu
	}
	
	
	public void runSRTF(ArrayList<Process> jobs){
		double avgCPUU;
		double avgWT;
		double avgRT;
		double avgTT;
		System.out.println();
		System.out.println("Scheduling Algorithm: Shortest Remaining Time First");
		System.out.println("=========================================================");
		
		shortestRemainingTimeFirst(jobs); //runs the srjf algorithm
		
		//calculate averages
		avgCPUU = averageCPUUsage();
		avgWT = averageWaitTime(jobs);
		avgRT = averageResponseTime(jobs);
		avgTT = averageTurnaroundTime(jobs);
		
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
	
	private void shortestRemainingTimeFirst(ArrayList<Process> jobs){
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
		Process shortestProcess;
		int shortestProcessIndex = 0;
		
		while(true){
			//this loop will check if the Ready Queue is empty BUT there are still processes 
			//in the job pool that still need to arrive to be placed in the ReadyQueue
			while((job != totalProcesses) && super.readyQueueEmpty()){
				if(jobs.get(job).getarrivalTime() == systemCount){
					super.addProcess(jobs.get(job));
					currProcess = super.getProcess(0);
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
				// while loop to check if multiple processes have the same arrival time
				while((job != totalProcesses) && (jobs.get(job).getarrivalTime() == systemCount)){
					super.addProcess(jobs.get(job));
					job++;
				}
				//check if RQ is empty 
				if(!super.readyQueueEmpty()){
					shortestProcess = currProcess; //set the shortest process as the current process
					//if the RQ is of size 1, the shortest process will be at the head of the RQ
					if(super.readyQueueSize() == 1){
						shortestProcessIndex = 0;
						//if this is the first time the process is allocated the CPU, set the response time
						if(shortestProcess.getCPUBurstLeft() == shortestProcess.getcpuBurst()){
							shortestProcess.setResponseTime(systemCount - shortestProcess.getarrivalTime());
						}
					}
					
					else{ 
						//if the RQ size is >1, loop through all the processes currently in the RQ and find the process
						//with the smallest CPU Burst. Set that process as the shortest process and set the response time for that process
						for(int i=0; i<super.readyQueueSize(); i++){
							if(super.getProcess(i).getCPUBurstLeft() < shortestProcess.getCPUBurstLeft()){
								shortestProcess = super.getProcess(i);
								//find the position in the RQ of the shortest process
								shortestProcessIndex = super.getShortestProcessIndex(shortestProcess);
								if(shortestProcess.getCPUBurstLeft() == shortestProcess.getcpuBurst()){
									shortestProcess.setResponseTime(systemCount - shortestProcess.getarrivalTime());
								}							
							}
						 }//end for loop
					}//end else

					//if the current process still have CPU Burst to complete, the process s running
					if(shortestProcess.getCPUBurstLeft() != 0){
						System.out.println("<system time   " + systemCount + "> process    " + shortestProcess.getPID() +" is running" );
						systemCount++;
						shortestProcess.setCPUBurstLeft(shortestProcess.getCPUBurstLeft()-1);
					}
					
					//if the cpuBurst reaches 0, the process is complete
					if(shortestProcess.getCPUBurstLeft() == 0){
						shortestProcess.setcompletionTime(systemCount); //set completion time
						shortestProcess.setturnaroundTime(shortestProcess.getcompletionTime() - shortestProcess.getarrivalTime()); //set turnaround time
						shortestProcess.setWaitTime(shortestProcess.getcompletionTime() - (shortestProcess.getarrivalTime() + shortestProcess.getcpuBurst())); //set wait time
						System.out.println("<system time   " + systemCount + "> process    " + shortestProcess.getPID() +" is finished....." );
						super.removeProcess(shortestProcessIndex); //remove the process with respect to its position in the RQ
						//if the RQ is not empty, set the current process as the process at the head until  min comparison is ran again
						if(!super.readyQueueEmpty()){ 
							currProcess = super.getProcess(0);
							shortestProcess = currProcess;

						}
					}
				}
				//if the RQ is empty and all jobs are complete, break from outer while loop
				if(super.readyQueueEmpty() && job == totalProcesses) break;		
		} //end while loop
	} //end srtf method
	
	
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
