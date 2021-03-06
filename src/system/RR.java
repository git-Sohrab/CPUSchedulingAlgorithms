package system;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RR{

    private static LinkedBlockingQueue<Process> list;
    private ArrayList<Process> copyList;    
    private int runLimit;
    // private int tempBurst;
    
    public RR(){
        runLimit = 2;
        // tempBurst =0;
        copyList = new ArrayList<Process>();
    }

    public void setList(LinkedBlockingQueue<Process> l){
       list = l;       
    }

    public static LinkedBlockingQueue<Process> getList(){
        return RR.list;
    }

    // public void cpuProccess(){
    //     // Iterator<Process> it = list.iterator();
    //     Process p;
    //     while(true){
    //         if(list.size()!=0){
    //             p = list.poll();
    //             for(int i=0;i<runLimit;i++){
    //                 if(p.getNumOfprint() == p.getTimeSpec().getFirst().getCpuTime()){ // breaks if the print is done                       
    //                     break;
    //                 }else{
    //                     p.run();                    
    //                 }
    //              }
    //              if(p.getNumOfprint() != p.getTimeSpec().getFirst().getCpuTime()){                        
    //                 list.offer(p);
    //              }                           
    //             // System.out.println("");
    //         }else{
    //             // System.exit(0);
    //             break;                
    //         }

    //         sleep();
    //     }
    // }

    public void makeCopy(Process p){
        if(!copyList.contains(p))
            copyList.add(p);
    }
    private static long time() {
		long timeMillis = System.currentTimeMillis();
		long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(timeMillis);
        return timeSeconds;        
	}


    int i=0;
    public void cpuProccess(){
        // Iterator<Process> it = list.iterator();
        Process p;
        while(true){
            if(list.size()!=0){
                System.out.println("RR LIST "+list);
                p = list.poll();
                makeCopy(p);
                System.out.println("POLLED : "+p);
                //for(int i=0;i<p.getTimeSpec().size();i++){ // sequence of instructions
                    // System.out.println("CPU BURST HAPPENS" + p);
                    p.setProcessState(2); // enters state 2 (running) before running
                    p.upCpuBurst(-time());
                    for(int j = 0; j<runLimit/*p.getTimeSpec().getFirst().getCpuTime()*/;j++){
                        
                        // System.out.printf("This is CPU time: ",p.getTimeSpec()[i].getCpuTime());

                        if(p.getTimeSpec().getFirst().getCpuTime()==0){ // breaks if the print is done                       
                            break;
                        }else{
                            p.run(); // fills array
                            p.getTimeSpec().getFirst().downCpuTime();
                            sleep();                    
                        }
                    }
                    p.upCpuBurst(time());

                    if(p.getTimeSpec().getFirst().getCpuTime() != 0){                        
                         list.offer(p);
                    }  
                    
                    // System.out.println("IO BURST HAPPENS" + p);
                    /* This loop deals when there is IO burst following the above CPU burst*/
                    if(p.getTimeSpec().getFirst().getIoTime() != 0 && p.getTimeSpec().getFirst().getCpuTime() == 0){ // if there is an IO burst following CPU burst
                        // move this process into the Device queue
                        // the device thread runs every second and it executes any IO from the queue's head
                        p.setProcessState(3); // waiting state as it enters IO waiting queue.
                        Device d = Device.getDevice(p.getTimeSpec().getFirst().getDevice(), p, RR.getList());
                        // System.out.println(d == null);
                        // Device.setReturnQueue(FCFS.getList());
                        d.deviceEnqueu(p);
                    }
                    /*for(int k = 0; k<p.getTimeSpec()[i].getIoTime();k++){
                        p.ioRun(p.getTimeSpec()[i].getDevice());
                        // System.out.printf("This is I/O time: ",p.getTimeSpec()[i].getIoTime());
                    }*/
                //}
                i = 0;
            }else{
                // System.exit(0);
                // break;     
                // System.out.println("==>"+i); 
                i++;            
            }
            
            if(i == 30){
                break;
            }  
            // System.out.println("{}{}{}{}{}<><><> "+list);
    
            sleep();
        }

        System.out.println("\n\n\nBEHOLD, BEHOLD, BEHOLD\n");
        for (Process pr: copyList) {
            // System.out.printf("Process %c IO ==> %d \n CPU ==> %d\n",pr.chr ,pr.getIoBurst(), pr.getCpuBurst());
            pr.setProcessState(4);
            System.out.println(pr);
        }

        double avgCpuBurst = 0;
        double avgWaiting=0;
        double turnaroundTime=0;
        Process temp;
        for(int i = 0; i<copyList.size();i++){
            temp = copyList.get(i);
            turnaroundTime += (temp.getTerminationTime() - temp.getCreationTime());
            avgCpuBurst+=(temp.getCpuBurst());
        }
        avgCpuBurst = avgCpuBurst/copyList.size();
        turnaroundTime = turnaroundTime/copyList.size();
        avgWaiting = turnaroundTime - avgCpuBurst;
        System.out.printf("Avg Waiting: %.2f \nAvg Turnaround: %.2f\n",avgWaiting,turnaroundTime);
        System.exit(0);
    }

    public void sleep(){
        try {
            Thread.sleep(800L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return list.element().toString();
    }
}