
import java.io.Serializable;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This is the simulation class, to handle the processing behind the program's graphical simulation.
 * Main container class
 * Physics processing is mostly handled by this class
 * WORK IN PROGRESS
 * @author alex
 */
public class Simulation implements Serializable{
    private int particleCount;
    private Particle[] particles;
    private String name;
    private ArrayList<FieldLine> fieldLines;
    //resultant electric field is calculated everytime, instead of stored as a variable in the object
    public final double HORIZONTAL_SIZE;
    public final double VERTICAL_SIZE;
    
    /**
     * Constructor for Simulation class
     * @param name 
     */
    public Simulation(String name, double HORIZONTAL_SIZE, double VERTICAL_SIZE)
    {
        this.HORIZONTAL_SIZE = HORIZONTAL_SIZE;
        this.VERTICAL_SIZE = VERTICAL_SIZE;
        particleCount = 0;
        this.name = name;
        particles = new Particle[2];
        fieldLines = new ArrayList<>();
        updateSimulation();
        
    }
    
    /**
     * updates the simulation after a single time step
     * wraps the updateParticles and updateFieldLines method
     */
    public void updateSimulation()
    {
        updateParticles(); //updates their position based on the force applied on them, based on a change of one time unit
        updateFieldLines(); //recalculates the field lines and updates it
    }
    
    /**
     * updates the particles after a single time step
     * calls methods from the particles to update it
     */
    public void updateParticles()
    {
        if(particleCount == 2)
        {
            //gets the field of second particle, and then uses it to evaluate the force experienced by the first particle
            Vector particle1Force = particles[1].getField().fieldStrengthAtPoint(particles[0].getPosition()).multiplyScalar(particles[0].getCharge());
            particles[0].setAcceleration(particle1Force.divideScalar(particles[0].getMass()));
            //gets the field of the first particle, and then uses it to evaluate the force experienced by the second particle
            Vector particle2Force = particles[0].getField().fieldStrengthAtPoint(particles[1].getPosition()).multiplyScalar(particles[1].getCharge());
            particles[1].setAcceleration(particle2Force.divideScalar(particles[1].getMass()));
            //updates the velocity of both particles
            particles[0].updateVelocity();
            particles[1].updateVelocity();
            //updates the position of both particles
            particles[0].updatePosition();
            particles[1].updatePosition();
        } else for(Particle particle:particles) {
            if (particle != null){
                particle.setAcceleration(new Vector(new Point(0,0), 0, 0));
                particle.updateVelocity();
                particle.updatePosition();
            }
        }  
    }
    
    /**
     * updates the fieldLines in the simulation after a single time step
     */
    public void updateFieldLines()
    {
        fieldLines.clear(); //clear the current set of field lines to replace with new ones
        if (particleCount == 2 && (particles[0].getCharge() != 0 && particles[1].getCharge() != 0)) //boolean short-circuit prevents index error
        {
            if (Math.signum(particles[0].getCharge()) != Math.signum(particles[1].getCharge())) 
            //compares the charges of the particles to determine what type of field lines to draw
            //Opposite sign charges
            {
                int INDEX_OF_POSITIVE_PARTICLE = -1;
                int INDEX_OF_NEGATIVE_PARTICLE = -1;
                if(Math.signum(particles[0].getCharge()) == 1.0)
                {
                    INDEX_OF_POSITIVE_PARTICLE = 0;
                    INDEX_OF_NEGATIVE_PARTICLE = 1;
                } else {
                    INDEX_OF_POSITIVE_PARTICLE = 1;
                    INDEX_OF_NEGATIVE_PARTICLE = 0;
                }
                //in a system of two oppositely charged particles there is always a straight field line from one particle to the other
                //this is drawn as a single large vector instead of a number of tiny vectors
                Vector midVector = new Vector(particles[INDEX_OF_POSITIVE_PARTICLE].getPosition(), particles[INDEX_OF_NEGATIVE_PARTICLE].getPosition());
                FieldLine midLine = new FieldLine();
                midLine.resetVectors();//debug purpose
                midLine.addVector(midVector);
                //the reference bearing is used to decide at what angles to draw the remaining field lines
                double referenceBearing = midVector.getBearing();
                //creates the new fieldLines intended for angles from 20 to 80 relative to the midLine.
                FieldLine fieldLine20   = new FieldLine();
                FieldLine fieldLineN20  = new FieldLine();
                FieldLine fieldLine40   = new FieldLine();
                FieldLine fieldLineN40  = new FieldLine();
                FieldLine fieldLine60   = new FieldLine();
                FieldLine fieldLineN60  = new FieldLine();
                FieldLine fieldLine80   = new FieldLine();
                FieldLine fieldLineN80  = new FieldLine();
                
                //Adds in the first vector into the fieldLine
                fieldLine20.addVector(new Vector(particles[INDEX_OF_POSITIVE_PARTICLE].getPosition(),
                    /*The vector size is 3 to represent the smallest possible line for a single pixel*/3, 
                    referenceBearing + Math.toRadians(20)));
                fieldLineN20.addVector(new Vector(particles[INDEX_OF_POSITIVE_PARTICLE].getPosition(),
                    3, 
                    referenceBearing - Math.toRadians(20)));
                fieldLine40.addVector(new Vector(particles[INDEX_OF_POSITIVE_PARTICLE].getPosition(),
                    3, 
                    referenceBearing + Math.toRadians(40)));
                fieldLineN40.addVector(new Vector(particles[INDEX_OF_POSITIVE_PARTICLE].getPosition(),
                    3, 
                    referenceBearing - Math.toRadians(40)));
                fieldLine60.addVector(new Vector(particles[INDEX_OF_POSITIVE_PARTICLE].getPosition(),
                    3, 
                    referenceBearing + Math.toRadians(60)));
                fieldLineN60.addVector(new Vector(particles[INDEX_OF_POSITIVE_PARTICLE].getPosition(),
                    3, 
                    referenceBearing - Math.toRadians(60)));
                fieldLine80.addVector(new Vector(particles[INDEX_OF_POSITIVE_PARTICLE].getPosition(),
                    3, 
                    referenceBearing + Math.toRadians(80)));
                fieldLineN80.addVector(new Vector(particles[INDEX_OF_POSITIVE_PARTICLE].getPosition(),
                    3, 
                    referenceBearing - Math.toRadians(80)));
                
                //adds the initiated fieldlines
                this.fieldLines.add(fieldLine20);
                this.fieldLines.add(fieldLineN20);
                this.fieldLines.add(fieldLine40);
                this.fieldLines.add(fieldLineN40);
                this.fieldLines.add(fieldLine60);
                this.fieldLines.add(fieldLineN60);
                this.fieldLines.add(fieldLine80);
                this.fieldLines.add(fieldLineN80);
                //calculates the remaining vectors in each fieldline
                for(FieldLine fl: fieldLines)
                {
                    while(this.particleCheck(INDEX_OF_NEGATIVE_PARTICLE, fl.getEnd())) 
                    {
                        Vector vectorToAdd = this.getResultantField().fieldStrengthAtPoint(fl.getEnd());
                        vectorToAdd.setMagnitude(3);//the magnitude is set to 3
                        fl.addVector(vectorToAdd);
                    }
                }
                
                this.fieldLines.add(midLine); //add after the calculation to prevent error in calculating using the midLine(division by infinity)
            } else
            //Same sign charges
            //the process is similar, but repeats the process for each particle because the field lines never actually touch the other particle
            //creates two sets of field lines
            {
                //check wether it is both negative, required to invert vector
                boolean bothNegative = Math.signum(particles[0].getCharge()) == -1.0 && Math.signum(particles[1].getCharge()) == -1.0;
                //in a system of two same charge particles there is no straight field line from one particle to the other
                //we create the line anyway as a single large vector instead of a number of tiny vectors, to act as a reference
                Vector midVector = new Vector(particles[0].getPosition(), particles [1].getPosition());
                FieldLine midLine = new FieldLine();
                midLine.addVector(midVector);
                //the reference bearing is used to decide at what angles to draw the remaining field lines
                double referenceBearing = midVector.getBearing();
                //creates the new fieldLines intended for angles from 20 to 80 relative to the midLine.
                FieldLine fieldLine20   = new FieldLine();
                FieldLine fieldLineN20  = new FieldLine();
                FieldLine fieldLine40   = new FieldLine();
                FieldLine fieldLineN40  = new FieldLine();
                FieldLine fieldLine60   = new FieldLine();
                FieldLine fieldLineN60  = new FieldLine();
                FieldLine fieldLine80   = new FieldLine();
                FieldLine fieldLineN80  = new FieldLine();
                
                //Adds in the first vector into the fieldLine
                fieldLine20.addVector(new Vector(particles[0].getPosition(),
                    /*The vector size is 3 to represent the smallest possible line for a single pixel*/3, 
                    referenceBearing + Math.toRadians(20)));
                fieldLineN20.addVector(new Vector(particles[0].getPosition(),
                    3, 
                    referenceBearing - Math.toRadians(20)));
                fieldLine40.addVector(new Vector(particles[0].getPosition(),
                    3, 
                    referenceBearing + Math.toRadians(40)));
                fieldLineN40.addVector(new Vector(particles[0].getPosition(),
                    3, 
                    referenceBearing - Math.toRadians(40)));
                fieldLine60.addVector(new Vector(particles[0].getPosition(),
                    3, 
                    referenceBearing + Math.toRadians(60)));
                fieldLineN60.addVector(new Vector(particles[0].getPosition(),
                    3, 
                    referenceBearing - Math.toRadians(60)));
                fieldLine80.addVector(new Vector(particles[0].getPosition(),
                    3, 
                    referenceBearing + Math.toRadians(80)));
                fieldLineN80.addVector(new Vector(particles[0].getPosition(),
                    3, 
                    referenceBearing - Math.toRadians(80)));
                
                //adds the initiated fieldlines
                this.fieldLines.add(fieldLine20);
                this.fieldLines.add(fieldLineN20);
                this.fieldLines.add(fieldLine40);
                this.fieldLines.add(fieldLineN40);
                this.fieldLines.add(fieldLine60);
                this.fieldLines.add(fieldLineN60);
                this.fieldLines.add(fieldLine80);
                this.fieldLines.add(fieldLineN80);
                
                //calculates the remaining vectors in each fieldline
                for(FieldLine fl: fieldLines)
                {
                    while(this.boundaryCheck(fl.getEnd()) && this.particleCheck(1, fl.getEnd())) 
                    {
                        Vector vectorToAdd = this.getResultantField().fieldStrengthAtPoint(fl.getEnd());
                        vectorToAdd.setMagnitude(3);//the magnitude is set to 3
                        if(bothNegative)
                        {
                            vectorToAdd.setBearing(vectorToAdd.getBearing() + Math.PI);
                        }
                        fl.addVector(vectorToAdd);
                    }
                }
                
                //repeat the process for the second particle
                Vector midVector2 = new Vector(particles[1].getPosition(), particles [0].getPosition());
                //the reference bearing is used to decide at what angles to draw the remaining field lines
                double referenceBearing2 = midVector2.getBearing();
                //creates the new fieldLines intended for angles from 20 to 80 relative to the midLine.
                FieldLine fieldLine20_2   = new FieldLine();
                FieldLine fieldLineN20_2  = new FieldLine();
                FieldLine fieldLine40_2   = new FieldLine();
                FieldLine fieldLineN40_2  = new FieldLine();
                FieldLine fieldLine60_2   = new FieldLine();
                FieldLine fieldLineN60_2  = new FieldLine();
                FieldLine fieldLine80_2   = new FieldLine();
                FieldLine fieldLineN80_2  = new FieldLine();
                
                //Adds in the first vector into the fieldLine
                fieldLine20_2.addVector(new Vector(particles[1].getPosition(),
                    /*The vector size is 3 to represent the smallest possible line for a single pixel*/3, 
                    referenceBearing2 + Math.toRadians(20)));
                fieldLineN20_2.addVector(new Vector(particles[1].getPosition(),
                    3, 
                    referenceBearing2 - Math.toRadians(20)));
                fieldLine40_2.addVector(new Vector(particles[1].getPosition(),
                    3, 
                    referenceBearing2 + Math.toRadians(40)));
                fieldLineN40_2.addVector(new Vector(particles[1].getPosition(),
                    3, 
                    referenceBearing2 - Math.toRadians(40)));
                fieldLine60_2.addVector(new Vector(particles[1].getPosition(),
                    3, 
                    referenceBearing2 + Math.toRadians(60)));
                fieldLineN60_2.addVector(new Vector(particles[1].getPosition(),
                    3, 
                    referenceBearing2 - Math.toRadians(60)));
                fieldLine80_2.addVector(new Vector(particles[1].getPosition(),
                    3, 
                    referenceBearing2 + Math.toRadians(80)));
                fieldLineN80_2.addVector(new Vector(particles[1].getPosition(),
                    3, 
                    referenceBearing2 - Math.toRadians(80)));
                
                //adds the initiated fieldlines
                this.fieldLines.add(fieldLine20_2);
                this.fieldLines.add(fieldLineN20_2);
                this.fieldLines.add(fieldLine40_2);
                this.fieldLines.add(fieldLineN40_2);
                this.fieldLines.add(fieldLine60_2);
                this.fieldLines.add(fieldLineN60_2);
                this.fieldLines.add(fieldLine80_2);
                this.fieldLines.add(fieldLineN80_2);
                
                //calculates the remaining vectors in each fieldline
                for(FieldLine fl: fieldLines)
                {
                    while(this.boundaryCheck(fl.getEnd()) && this.particleCheck(0, fl.getEnd())) 
                    {
                        Vector vectorToAdd = this.getResultantField().fieldStrengthAtPoint(fl.getEnd());
                        vectorToAdd.setMagnitude(3);//the magnitude is set to 3
                        if(bothNegative)
                        {
                            vectorToAdd.setBearing(vectorToAdd.getBearing() + Math.PI);
                        }
                        fl.addVector(vectorToAdd);
                    }
                }
            }
        }
        
    }
    
    public boolean withinBounds(Point point)
    {
        
        return particleCheck(0, point) && boundaryCheck(point) && particleCheck(1, point);
    }
    
    public boolean boundaryCheck(Point point)
    {
        //check that the point is within bounds of the simulation
        return point.getX() < HORIZONTAL_SIZE && point.getY() < VERTICAL_SIZE;
    }
    
    public boolean particleCheck(int index, Point point)
    {
        return point.distanceFrom(particles[index].getPosition()) > particles[0].getRadius();
    }

    /**
     * @return the particles
     */
    public Particle[] getParticles() {
        return particles;
    }

    /**
     * @param particles the particles to set
     */
    public void setParticles(Particle[] particles) {
        this.particles = particles;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the resultantField
     */
    public Field getResultantField() {
        Field resultantField = new Field(new Point(0,0),0);
        for(int i = 0; i < particleCount; i++)
        {
            resultantField.addField(particles[i].getField());
        }
        return resultantField;
    }

    /**
     * @return the fieldLines
     */
    public ArrayList<FieldLine> getFieldLines() {
        return fieldLines;
    }
    /**
     * adds a new particle to the simulation after checking the simulation still has space
     * @param newParticle 
     */
    public void addParticle(Particle newParticle)
    {
        if(particleCount < 2)
        {
            particles[particleCount] = newParticle;
            particleCount++;
        } else {
            //throw new ArrayIndexOutOfBoundsException(); please uncomment when done testing
        }
    }
    //TODO: Add exception handling code in other methods that interact with this method, replace println for error handling with GUI error handling
    /**
     * 
     * @param particle
     * @return index of the particle
     */
    public int getParticleIndex(Particle particle)
    {
        try {
            for (int i = 0; i < particles.length; i++)
            {
                if(particle.equals(particles[i]))
                {
                    return i;
                }
            }
            throw new NullPointerException();
        } catch (NullPointerException n) {
            System.out.println("particle does not exist");
            return -1;
        }
    }
    
    /**
     * removes the particle at the given index
     * @param index 
     */
    public void removeParticle(int index)
    {
        try {
            if (index < particleCount && index >= 0)
            {
                particles[index] = null;
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException n) {
            System.out.println("Invalid index");
        }
    }
    
    //wrapper method to remove using particle reference instead
    /**
     * wrap the removeParticle method but removes by checking for a particular particle instead
     * use not recommended
     * @param particle 
     */
    public void removeParticle(Particle particle)
    {
        removeParticle(getParticleIndex(particle));
    }
}
