

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Color;
import java.awt.Font;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.HIDE_ON_CLOSE;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex
 */
public class MainFrame extends javax.swing.JFrame {
    private int PREFERRED_WIDTH;
    private int PREFERRED_HEIGHT;
    private boolean isPaused;
    public final SimulationWindow sim; //MainFrame contains a SimulationWindow, an inner class
    
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame(int PREFERRED_WIDTH, int PREFERRED_HEIGHT) {
        this.PREFERRED_WIDTH = PREFERRED_WIDTH;
        this.PREFERRED_HEIGHT = PREFERRED_HEIGHT;
        
        sim = new SimulationWindow("sim", PREFERRED_WIDTH, PREFERRED_HEIGHT);
        sim.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        this.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT + 100));//set the preferred dimension of the frame
        this.setContentPane(sim);//sets the content pane of the frame to be the simulation window
        
        setDefaultCloseOperation(HIDE_ON_CLOSE);   // Handle the CLOSE button
        pack();              // Either pack() the components; or setSize()
        setTitle("ELECTRIC FIELDS");  // "super" JFrame sets the title
        this.setMaximumSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        setVisible(true);    // "super" JFrame show
        setResizable(false);
        isPaused = true;
    }
    
    public void advanceFrame()
    {
        sim.paintComponent(sim.getGraphics());
        sim.simulation.updateSimulation();
            //scales the program time
            try {
                TimeUnit.MILLISECONDS.sleep(10);//buffer time to prevent frames that are too fast, otherwise this should allow a variable time program(larger renders = slower time)
            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
            }
        });
    }
    //wrapper class: wraps a simulation panel over a simulation object
    public class SimulationWindow extends JPanel
    {
        private Simulation simulation;//wrapped simulation object
        
        public SimulationWindow(String name, double HORIZONTAL_SIZE, double VERTICAL_SIZE) 
        {
            super();
            this.simulation = new Simulation(name, HORIZONTAL_SIZE, VERTICAL_SIZE);
        }
        //paintComponent is overriden to graph the elements required.
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);     // paint parent's background
            //setBackground(Color.LIGHT_GRAY);  // set background color for this JPanel
            Graphics2D g2d = (Graphics2D)g;//convert to Graphics2D to support coordinate transform

                g2d.transform(new AffineTransform(1,0,0,-1,0,0));//flips the user space of the Graphics2D object
                g2d.translate(0,-PREFERRED_HEIGHT);//translates the origin to the bottom left corner, !!should change coordinates to a field
                    
            //coordinate of particle shifted to bottom left corner of the particle
            double particle1_x = getSimulation().getParticles()[0].getPosition().getX() - getSimulation().getParticles()[0].getRadius();
            double particle1_y = getSimulation().getParticles()[0].getPosition().getY() - getSimulation().getParticles()[0].getRadius();
            g2d.setColor(Color.red); //second particle is red
            g2d.fillOval(
                    (int)particle1_x, 
                    (int)particle1_y,
                    2*(int)getSimulation().getParticles()[0].getRadius(),
                    2*(int)getSimulation().getParticles()[0].getRadius()
            );
            
            //coordinate of particle shifted to bottom left corner of the particle
            double particle2_x = getSimulation().getParticles()[1].getPosition().getX()-getSimulation().getParticles()[1].getRadius();
            double particle2_y = getSimulation().getParticles()[1].getPosition().getY()-getSimulation().getParticles()[1].getRadius();
            g2d.setColor(Color.blue); //second particle is blue
            g2d.fillOval(
                    (int)particle2_x, 
                    (int)particle2_y,
                    2*(int)getSimulation().getParticles()[1].getRadius(),
                    2*(int)getSimulation().getParticles()[1].getRadius()
            );
            
            
            
            //draw the fieldLines
            g2d.setColor(Color.black);
            for(FieldLine fieldline : simulation.getFieldLines())
            {
                for(Vector vector : fieldline.getVectors())
                {
                    g2d.drawLine(
                            (int)vector.getStart().getX(),
                            (int)vector.getStart().getY(),
                            (int)vector.getEnd().getX(),
                            (int)vector.getEnd().getY()
                    );
                }
            }
            g2d.setColor(Color.BLACK);
            g2d.scale(1, -1);
            g2d.setFont(new Font("def", 2, 30));
            g2d.drawString(this.simulation.getParticles()[0].getName(),
                    (int)particle1_x + (int)getSimulation().getParticles()[0].getRadius(),
                    -(int)particle1_y - (int)getSimulation().getParticles()[0].getRadius()); //render the name
            g2d.drawString(this.simulation.getParticles()[1].getName(),
                    (int)particle2_x + (int)getSimulation().getParticles()[1].getRadius(),
                    -(int)particle2_y - (int)getSimulation().getParticles()[1].getRadius());
            g2d.scale(1, -1);
            
        }

        /**
         * @return the simulation
         */
        public Simulation getSimulation() {
            return simulation;
        }

        /**
         * @param simulation the simulation to set
         */
        public void setSimulation(Simulation simulation) {
            this.simulation = simulation;
        }
        
        
    }                  

    /**
     * @return the isPaused
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * @param isPaused the isPaused to set
     */
    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

}
