CPU Scheduling Simulator
=======================
Overview:
-----------
One of the main tasks of an operating system is scheduling processes to run on the CPU. In this assignment, you will build a program which schedules simulated CPU processes. Your simulator program will implement three of the CPU scheduling algorithms discussed in this course. The simulator selects a process to run from the ready queue based on the scheduling algorithm chosen at runtime. Since the assignment intends to simulate a CPU scheduler, it does not require any actual process creation or execution. When the CPU scheduler chooses the next process, the simulator will simply print out which process was selected to run at that time. The simulator output
is similar to the Gantt chart style.

**CPU Scheduling Algorithms Implemented:**
- First Come First Serve
- Shortest Remaining Time First
- Round Robin

Here is the example input:

pid | arrival time | cpu burst

1 0 10
2 0 9
3 3 5
4 7 4
5 10 6
6 10 7

The command to launch your program using *First Come First Serve* scheduling:

    java Assignment2 FCFS
    
The command to launch your program using *Shortest Remaining Time First* scheduling:

    java Assignment2 SRTF
    
The command to launch your program using *Round Robin* scheduling with a time quantum of 4:

    java Assignment2 RR 4
