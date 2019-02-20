/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Simulation sim = new Simulation("sim", 1920, 1080);
        sim.addParticle(new Particle("particle 1", 20, 1.0E-2, 200, new Point(100,300)));
        sim.addParticle(new Particle("particle 2", 20, 2.0E-3, 200, new Point(200,300)));
        for(int i = 0; i < 1000; i++)
        {
            sim.updateSimulation();
        }
    }
    
}
