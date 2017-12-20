import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Assignment2 {

	public static void main(String args[]){
		ArrayList<Process> jobs = new ArrayList<Process>(); //array to store processes read in from file
		Process process; 
		
		try {
			FileInputStream fstream = new FileInputStream("assignment2.txt");
			Scanner input = new Scanner(fstream);
			String line;
			Scanner info;
			int pid, arrival, cpub; //will store values PID, Arrival Time and CPUBurst for a Process read in from file
			
			while(input.hasNext()){
				line = input.nextLine();
				info = new Scanner(line);
				pid = Integer.parseInt(info.next()); //PID
				arrival = Integer.parseInt(info.next()); //Arrival Time
				cpub = Integer.parseInt(info.next()); //CPU Burst
				
				process = new Process(pid, arrival, cpub); //create new Process object
				jobs.add(process); //add process to array
			}
	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//Scheduling Algorithm will be selected depending on what was written as the first command line argument
		//First Come First Serve
		if(args[0].equalsIgnoreCase("FCFS")){
			FCFS fcfs = new FCFS();
			fcfs.runFCFS(jobs);
		}

		//Shortest Remaining Job First 
		else if(args[0].equalsIgnoreCase("SRTF")){
			SRTF srtf = new SRTF();
			srtf.runSRTF(jobs);
		}

		//Round Robin
		else if(args[0].equalsIgnoreCase("RR")){
			RR rr = new RR();
			rr.runRR(jobs, Integer.parseInt(args[1]));
		}
	}
}
