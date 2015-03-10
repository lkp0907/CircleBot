package gplee.a889056;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlayView {
    enum STATUS{
        STOP,
        START,
        PAUSE,
        RESET
    };
    private JFrame frame;
    DrawCircle panel;
    private Timer timer;
    private float count=0;
    private float delta=0;
    private int speed=20;
    private STATUS statusFlag=STATUS.STOP;
    private boolean rotating = false;
    private List<Color> colorList;
    /**
     * Launch the application.
     */
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PlayView window = new PlayView();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Create the application.
     */
    public PlayView() {
        initialize();
    }
    
    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        Map<String,String> team = new HashMap<String, String>();
        List<Map<String , String>> teamList  = new ArrayList<Map<String,String>>();
        colorList = new ArrayList<Color>();
        
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        
        
        team.put("NO", "1");
        team.put("NAME", "A");
        teamList.add(team);
        team.put("NO", "2");
        team.put("NAME", "B");
        teamList.add(team);
        team.put("NO", "3");
        team.put("NAME", "C");
        teamList.add(team);
        
        
        makeExampleCircle(teamList);
        
        //		panel.setDoubleBuffered(true);
        
        JLabel lblCircleAnimate = new JLabel("Circle Animate (by GiPyeongLee)");
        lblCircleAnimate.setHorizontalAlignment(JLabel.CENTER);
        frame.getContentPane().add(lblCircleAnimate, BorderLayout.NORTH);
        
        JButton btnNewButton = new JButton("PAUSE");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Stop Button Pushed
                // it is actually Pause
                if (rotating) {
                    statusFlag = STATUS.PAUSE;
                    rotating = false;
                    timer.stop();
                    
                    panel.repaint();
                }
            }
        });
        frame.getContentPane().add(btnNewButton, BorderLayout.EAST);
        
        timer = new Timer(speed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                count += 1;
                
                if(delta<10){
                    delta+=0.04;
                }
                count+=delta;
                if (count > 360) count = 0;
                
                panel.repaint();
            }
        });
        
        JButton btnNewButton_1 = new JButton("START");
        btnNewButton_1.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                // Start Button Pushed
                if(!rotating){
                    rotating = true;
                    statusFlag=STATUS.START;
                    timer.start();
                }
                
            }
        });
        frame.getContentPane().add(btnNewButton_1, BorderLayout.WEST);
        
        JButton btnNewButton_2 = new JButton("STOP");
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Click Reset button ( redraw Circle )
                statusFlag=STATUS.STOP;
                rotating = false;
                count=0;
                delta=0;
                timer.stop();
                panel.repaint();
                colorList.clear();
            }
        });
        btnNewButton_2.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        frame.getContentPane().add(btnNewButton_2, BorderLayout.SOUTH);
    }
    
    private class DrawCircle extends JPanel {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private Graphics2D g2d;
        private List<Map<String , String>> teamData;
        
        DrawCircle(List<Map<String , String>> data){
            
            this.teamData = data;
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g); // prevent 프리킹 ~
            g2d = (Graphics2D) g;
            int width = this.getWidth();
            int height = this.getHeight();
            int circleRadius = width>height?width/2:height/2;
            Font font = new Font("돋움", Font.PLAIN, circleRadius/12);
            
            
            g2d.setFont(font);
            AffineTransform originTransform = g2d.getTransform();
            originTransform.rotate(Math.toRadians(count),width/2,height/2);
            //			g2d.rotate(Math.toRadians(count),width/2,height/2);
            g2d.setTransform(originTransform);
            
            drawCirclePiece(0,"학관",0,45);
            drawCirclePiece(1,"기숙사",45,45);
            drawCirclePiece(2,"한솥",90,45);
            drawCirclePiece(3,"흑룡강",135,30);
            drawCirclePiece(4,"신안골",165,45);
            drawCirclePiece(5,"자취밥",210,30);
            drawCirclePiece(6,"밥버거",240,40);
            drawCirclePiece(7,"편의점",280,80);
            
            g2d.dispose();
            //			g2d.rotate(Math.toRadians(count),this.getWidth()/2,this.getHeight()/2);
            
            //	g2d.draw(new Ellipse2D.Double(0, 100, 30, 30));
        }
        
        private void drawCirclePiece(int idx,String string,int startDegree,int endDegree){
            Random rand = new Random();
            int width = this.getWidth();
            int height = this.getHeight();
            int circleRadius = width>height?width/2:height/2;
            int centerX = (width-circleRadius)/2;
            int centerY = (height-circleRadius)/2;
            double degree = endDegree;
            double textDegree = startDegree+endDegree/2;
            double textPDegree = (startDegree+endDegree/2)%90;
            if(rotating){
                Color color = colorList.get(idx);
                g2d.setColor(color);
            }else if(statusFlag==STATUS.STOP){
                Color color = new Color(rand.nextInt(255)%256,rand.nextInt(255)%256,rand.nextInt(255)%256,125);
                g2d.setColor(color);
                colorList.add(color);
            }else if(statusFlag==STATUS.PAUSE){
                Color color = colorList.get(idx);
                g2d.setColor(color);
            }
            g2d.fillArc(centerX,centerY,circleRadius,circleRadius, startDegree,(int)degree);
            textPDegree = Math.toRadians(textPDegree);
            
            g2d.setColor(Color.BLACK);
            
            FontMetrics metrics = g2d.getFontMetrics();
            Rectangle2D r = metrics.getStringBounds(string, g2d);
            
            
            if(textDegree<90){
                double positionX = Math.cos(textPDegree)*circleRadius/3;
                double positionY = Math.sin(textPDegree)*circleRadius/3;	
                g2d.drawString(string,width/2+(int)positionX-(int)r.getWidth()/2,height/2-(int)positionY+(int)r.getHeight()/2);
                
            }
            else if(textDegree<180){
                double positionX = Math.sin(textPDegree)*circleRadius/3;
                double positionY = Math.cos(textPDegree)*circleRadius/3;
                g2d.drawString(string,width/2-(int)positionX-(int)r.getWidth()/2,height/2-(int)positionY+(int)r.getHeight()/2);
            }
            else if(textDegree<270){
                double positionX = Math.cos(textPDegree)*circleRadius/3;
                double positionY = Math.sin(textPDegree)*circleRadius/3;	
                g2d.drawString(string,width/2-(int)positionX-(int)r.getWidth()/2,height/2+(int)positionY+(int)r.getHeight()/2);	
            }
            else if(textDegree<360){
                double positionX = Math.sin(textPDegree)*circleRadius/3;
                double positionY = Math.cos(textPDegree)*circleRadius/3;
                g2d.drawString(string,width/2+(int)positionX-(int)r.getWidth()/2,height/2+(int)positionY+(int)r.getHeight()/2);
            }
            
            
            
        }
    }
    
    private void makeExampleCircle(List<Map<String , String>> teamList){
        panel = new DrawCircle(teamList);
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
    }
}
