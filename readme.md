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
