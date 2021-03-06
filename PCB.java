package system;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PCB implements Runnable{ // Each process is represented by a PCB
    /*
    * 3.1.3 Process Control Block: it is linked to a process as it is in the queue
        * Each process is represented in the operating system by a
        * process control block ( PCB )—also called a task control block.
        * A PCB is shown in Figure 3.3. It contains many pieces of information
        * associated with a specific process
            * Process state: New, Ready, Running, Waiting, Terminated
            * Program counter : indicates the address of the next instruction
                            * to be executed
            * CPU registers : The 32 registers as in EECS-2021
            * CPU-Scheduling info: includes
                * a process priority,
                * pointers to scheduling queues, and
                * any other scheduling parameters. (See Ch 6)
            * Memory-management info
            * Accounting info
                * CPU time used
                * Real time used
                * Time limits
                * account numbers
                * Process numbers
            * I/O status info: list of I/O devices allocated to the process, a list of open files
        *Each PCB includes a pointer field that points to the next PCB in the ready queue.
            *
    * */

    /*The above fields will be the attributes of this class*/
/*<<<<<<< HEAD

    private final int pid;// process identifier
    private int arrivalTime;
//    private ProcessState currentState;
    private int timeSlice;//CPU Scheduling info, amt of time given for the
                    // process is paused for RR schedueling
    private byte processPriority; // priority 0 to 127, must be positive
=======*/
    static int  pid;                // Process identifier
    int arivalTimel;                // Time when process p first makes request for execution from CPU scheduler   
    int processState;               // The state of the process. 0 for New, 1 for Ready, 2 for Running, and 3 for Waiting
    int timeSlice;                  // CPU Scheduling info, amt of time given for the process is paused and other information
    int processPriority;            // Non-negative integer representing the priority of the process. lower number = higher priority
    int CPUTime;                    // Non-negative integer representing time needed for process to execute on CPU
    int IOTime;                     // Non-negative integer representing time needed for I/O operation
    int completionTime;             // Time when the process has completed
    int turnAroundTime;             // Total time taken by process between starting state and ending state
    int waitingTime;                // Time for which process is in the ready queue, no yet executing
    PCB nextProcess;                // Reference to the next process in the queue. AKA program counter
<<<<<<< HEAD:src/system/PCB.java
//>>>>>>> 28a0dd924b47e6779e464c01cb1465e4e0b12dc9
    private JobQueue<Integer> arr;
=======
    private ArrayList<Integer> arr;
>>>>>>> 9767ef5f16632bb47ce3bc35126c78a050e06f03:PCB.java
    ScheduledExecutorService runIt;
    int timeReq = 10;
    int k;

    //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><> CONSTRUCTOR
    //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

    public PCB(){
        this(0,1);
    }

    public PCB(int pid, int k){
        this.pid = pid;
        arr = new ArrayList<Integer>();
        this.k = k;
    }

    //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><> UTILITY
    //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

    private void count(){
        for(int i=0;i<10;i++){
            arr.add((i+1)*k);
            print();
            sleep();
        }
    }

    public void sleep(){
        try {
            Thread.sleep(300L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void print(){
        System.out.println(arr);
    }

    //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><> OPERATIONS
    //<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>

    @Override
    public void run() {
        count();
    }
}
