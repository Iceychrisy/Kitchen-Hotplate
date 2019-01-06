/*
 * Chris Lipscombe 14876717
 * DSA - Assignment 1, Due Monday 27th
 */
package Hotplate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.JSlider;
import java.awt.GridLayout;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author btg2757
 * 
 */

public class HotplateGUI extends JPanel implements ActionListener{
    private JButton quitButton;
    private JSlider temptureSlider;
    private Timer timer;
    private DrawingCanvas canvas;
    private final int  x = 40; //30
    private final int y = 40; //30
    private Element[][] element;
 
    public HotplateGUI(){   
        super();
        setLayout(new BorderLayout());
        try{  
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e){}
        
        element = new Element[x][y];
        //Making elements and signing to the 2D array.
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                element[i][j] = new Element(0, 0.5);
            }
        }
        
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                //Checking possison of the element and if its near the border.
                if(i > 0){
                    //Top
                    element[i][j].addNeighbour(element[i-1][j]) ;
                } 
                if(i != (x-1)){
                    //Bottom
                    element[i][j].addNeighbour(element[i+1][j]);
                } 
                if(j > 0){
                    //Left
                    element[i][j].addNeighbour(element[i][j-1]);
                } 
                if(j != (y-1)){
                    //Right
                    element[i][j].addNeighbour(element[i][j+1]);
                } 
            }
        }
        
        for(int i = 0; i < x; i++){
                for(int j = 0; j < y; j++){
                    //Creating threads and assigning them to elements.
                    Thread t1 = new Thread(element[i][j]);
                    t1.start();
                }
        }
        
        canvas = new DrawingCanvas();
        
        quitButton = new JButton(" Quit ");    
        quitButton.addActionListener(this);
        
        temptureSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, 250);
        temptureSlider.setMajorTickSpacing(100);
        temptureSlider.setMinorTickSpacing(50);
        temptureSlider.setPaintTicks(true);
        temptureSlider.setPaintLabels(true);
        
        timer = new Timer(20, this);
        
        canvas.addMouseMotionListener(mouseBuffer);
       
        JPanel panel = new JPanel();
  
        panel.add(quitButton);
        panel.add(temptureSlider);      

        add(panel,BorderLayout.SOUTH);
        add(canvas,BorderLayout.NORTH);  
        
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        //Actions.
        if(source == quitButton){
            System.exit(0);
        }
        
        else if(source == timer){
            canvas.repaint();
        }
    }
    
    MouseAdapter mouseBuffer = new MouseAdapter(){
      @Override
        public void mouseDragged(MouseEvent mE){
            //Mouse Action.
            int canvasWidth = canvas.getWidth();
            int canvasHeight = canvas.getHeight();
            int rectangleWidth = canvasWidth/x;
            int rectangleHeight = canvasHeight/y;

            int getMouseXPoint = mE.getX();
            int getMouseYPoint = mE.getY();
            //Finding the offset of the canvas.
            int offsettingX = (canvasWidth - (rectangleWidth * x)) / 2;
            int offsettingY = (canvasHeight - (rectangleHeight * y)) / 2;
            //Finding mouse on the canvas.
            int tempX = (int) Math.ceil((getMouseXPoint - offsettingX) / rectangleWidth);
            int tempY = (int) Math.ceil((getMouseYPoint - offsettingY)/ rectangleHeight);
               
            if(tempX >= 0 && tempX <= (x - 1)){
                if(tempY >= 0 && tempY <= (y-1)){
                    //Applying tempture to the square / grid.
                    System.out.println("X: " + tempX + " Y: " + tempY + " Coordinates.");
                    element[tempX][tempY].applyTempToElement(temptureSlider.getValue());
                }
            }
        }
    };

    private class DrawingCanvas extends JPanel{
        public DrawingCanvas(){
            //Building JFrame.
            setPreferredSize(new Dimension(600,600));
            setBackground(Color.WHITE);
        }

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            
            int canvasWidth = getWidth();
            int canvasHeight = getHeight();
            int rectangleWidth = canvasWidth/x;
            int rectangleHeight = canvasHeight/y;
            
            int offsettingX = (canvasWidth - (rectangleWidth * x)) / 2;
            int offsettingY = (canvasHeight - (rectangleHeight * y)) / 2;
            
            int changeBlockColor = 0;
            
            for(int i = 0; i < x; i++){
                for(int j = 0; j < y; j++){
                    //Checking what color the square / grid is.
                    changeBlockColor = (int) element[i][j].getTemperatune();
                    if(changeBlockColor > 255){
                        changeBlockColor = 255;
                    }
                    if(changeBlockColor < 0){
                        changeBlockColor = 0;
                    }
                    //Making the canvas square / grid blue or red
                    g.drawRect((i * rectangleWidth) + offsettingX, (j * rectangleHeight) + offsettingY, rectangleWidth, rectangleHeight);
                    g.setColor(new Color(changeBlockColor, 0, 255 - changeBlockColor));
                    g.fillRect((i * rectangleWidth) + offsettingX, (j * rectangleHeight) + offsettingY, rectangleWidth, rectangleHeight);
                }
            }
        }
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame(" - - - Chris's Kitchen Element For DSA Assignment 1 - - - ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new HotplateGUI());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}