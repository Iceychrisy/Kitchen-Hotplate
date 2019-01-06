/*
 * Chris Lipscombe 14876717
 * DSA - Assignment 1, Due Monday 27th
 */
package Hotplate;

import java.util.ArrayList;

/**
 *
 * @author btg2757
 * 
 */

public class Element implements Runnable{
    private ArrayList<Element> neighbours = new ArrayList();
    private double currentTemp;
    private double heatConstant;
    private double  averageTemp;
    private boolean stopRequested = false;
    
    public Element (double currentTemp, double heatConstant){
        this.currentTemp = currentTemp;
        this.heatConstant = heatConstant;    
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
        }
    }
    
    public void addNeighbour(Element element){
        neighbours.add(element);
    }
    
    public synchronized void applyTempToElement(double appliedTemp){
        currentTemp += (appliedTemp - currentTemp) * heatConstant;
    } 
    
    public synchronized double averageTemp(){
        double totalTemp = 0;
        
        for(Element e : neighbours){
            totalTemp += e.currentTemp;
        }
        averageTemp = totalTemp / neighbours.size();
        
        return averageTemp;
    } 
}