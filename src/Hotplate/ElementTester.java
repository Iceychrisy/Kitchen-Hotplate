package Hotplate;

/*
 * Chris Lipscombe 14876717
 * DSA - Assignment 1, Due Monday 27th
 */
import java.util.ArrayList;

/**
 *
 * @author btg2757
 * 
 */

public class ElementTester implements Runnable{
    private ArrayList<ElementTester> neighbours = new ArrayList();
    private double currentTemp;
    private double heatConstant;
    private double  averageTemp;
    private boolean stopRequested = false;
    private String tempString;
    
    public ElementTester (double currentTemp, double heatConstant, String tempString){
        this.currentTemp = currentTemp;
        this.heatConstant = heatConstant;
        this.tempString = tempString;    
    }
    
    public void start(){
        
    }
    
    public synchronized double getTemperatune(){ 
        return this.currentTemp;
    }
    
    public void requestStop(){
        this.stopRequested = true;
    }
    
    public void run(){
        while(!stopRequested){
            try{

                    Thread.sleep(50);
                
            }catch(InterruptedException ex){}
        currentTemp += (averageTemp() - currentTemp) * heatConstant;
        System.out.println(tempString + " is : " + this.currentTemp);
    }
    }
    
    public void addNeighbour(ElementTester element){
        neighbours.add(element);
    }
    
    public synchronized void applyTempToElement(double appliedTemp){
        currentTemp += (appliedTemp - currentTemp) * heatConstant;
    } 
    
    public synchronized double averageTemp(){
        double totalTemp = 0;
        
        for(ElementTester e : neighbours){
            totalTemp += e.currentTemp;
        }
        averageTemp = totalTemp / neighbours.size();
        
        return averageTemp;
    }
    
    public static void main(String[] args){
        ElementTester E1 = new ElementTester(0, 0.5, "E1");
        ElementTester E2 = new ElementTester(40, 0.1, "E2");
        
        E1.addNeighbour(E2);
        E2.addNeighbour(E1);
        
        Thread t1 = new Thread(E1);
        Thread t2 = new Thread(E2);
        
        t1.start();
        t2.start();
    }
}

