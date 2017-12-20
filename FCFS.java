import java.util.ArrayList;

public class FCFS extends Scheduler {
	
	private int systemCount;
	private int idleCount;

	public FCFS(){
		super(); //class parent class Scheduler
		systemCount = 0; //set time in system to 0
		idleCount = 0; //counter for time no process is allocated the cpu
	}
	
	
	public void runFCFS(ArrayList<Process> jobs){
		double avgCPUU;
		double avgWT;
		double avgRT;
		double avgTT;
		System.out.println();
		System.out.println("Scheduling Algorithm: First Come First Serve");
		System.out.println("=========================================================");
		
		firstComeFirstServe(jobs); //run algorithm for fcfs
		
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
	
	public void firstComeFirstServe(ArrayList<Process> jobs){
		int totalProcesses = jobs.size(); //total jobs that will be allocated the cpu
		int job=0; //get first job
		int cpuBurstCounter = 0; //this will keep track of the current processes cpuBurst that has been completed
		Process currProcess;
		
		//loop will continue if the first process that will be put in the Ready Queue arrives at a time later than 0
		while(jobs.get(job).getarrivalTime() != systemCount){
			System.out.println("<system time   " + systemCount + "> idle");
			idleCount++;
			systemCount++;
		}
		
		//add the first process that arrives to the Ready Queue
		super.addProcess(jobs.get(job));
		job++; //increment to next job that will be added to the RQ
		currProcess = super.getProcess(0);
		currProcess.setResponseTime(systemCount - currProcess.getarrivalTime());
		
		//while loop that will continue until all processes have been completed
		while(true){
			//this loop will check if the Ready Queue is empty BUT there are still processes 
			//in the job pool that still need to arrive to be placed in the ReadyQueue
			while((job != totalProcesses) && super.readyQueueEmpty()){
				if(jobs.get(job).getarrivalTime() == systemCount){
					super.addProcess(jobs.get(job));
					currProcess = super.getProcess(0);
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
			// while loop to check if multiple processes have the same arrival time
			while((job != totalProcesses) && (jobs.get(job).getarrivalTime() == systemCount)){
				super.addProcess(jobs.get(job));
				job++;
			}
			//if the original CPU Burst is equal to the temporary cpu burst counter, the process is complete
			if(cpuBurstCounter == currProcess.getcpuBurst()){
				currProcess.setcompletionTime(systemCount); //set completion time
				currProcess.setturnaroundTime(currProcess.getcompletionTime() - currProcess.getarrivalTime()); //set turnaround time (completion-arrival)
				currProcess.setWaitTime(currProcess.getcompletionTime() - (currProcess.getarrivalTime() + currProcess.getcpuBurst())); //set wait time  (completion - (arrival+cpuburst)
				System.out.println("<system time   " + systemCount + "> process    " + currProcess.getPID() +" is finished....." );
				super.removeProcess(0); //remove the process from the RQ
				cpuBurstCounter = 0; //set the cpu burst counter back to 0 for next process
				if(super.readyQueueEmpty() && (job == totalProcesses)){
					//check if RQ is empty and that there are no more jobs left to run
					break;
				}
				//if the RQ is not empty, get the next process at the head of the RQ
				else if(!super.readyQueueEmpty()){
					currProcess = super.getProcess(0);
					currProcess.setResponseTime(systemCount - currProcess.getarrivalTime()); //set the response time
				}
			}
			//if the process is not complete, it is currently running
			else if(cpuBurstCounter != currProcess.getcpuBurst()){
				System.out.println("<system time   " + systemCount + "> process    " + currProcess.getPID() +" is running" );
				cpuBurstCounter++;
				systemCount++;
			}
	}
} //end fcfs method
	
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
		return avgRT;
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
} //end fcfs class
