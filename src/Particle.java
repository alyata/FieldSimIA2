
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Models the electrically charged particles found in physics
 * Includes some methods for physical interactions, but is relatively limited
 * Handle physical interactions in a container class instead
 * WORK IN PROGRESS
 * @author alex
 */
public class Particle implements Serializable{
    private Point position;
    private double radius;
    private double charge;
    private double mass;
    private String name;
    private Field field;
    private Vector velocity;
    private Vector acceleration;
    
    
    /**
     * Constructor for particle class
     * @param name
     * @param radius
     * @param charge
     * @param mass
     * @param position 
     */
    public Particle(String name, double radius, double charge, double mass, Point position) {
        this.position = position;
        this.radius = radius;
        this.charge = charge;
        this.name = name;
        this.mass = mass;
        this.field = new Field(position, charge);
        //these ensure the particle is able to reference something when updating the fields in updateVelocity() and updatePosition()
        this.acceleration = new Vector(new Point(0,0), new Point(0,0));
        this.velocity = new Vector(new Point(0,0), new Point(0,0));
    }

    /**
     * @return the position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Point position) {
        this.position = position;
        field.setSource(position);
    }

    /**
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * @return the charge
     */
    public double getCharge() {
        return charge;
    }

    /**
     * @param charge the charge to set
     */
    public void setCharge(double charge) {
        this.charge = charge;
        field.setCharge(charge);
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
     * @return the field
     */
    public Field getField() {
        return field;
    }

    /**
     * @return the velocity
     */
    public Vector getVelocity() {
        return velocity;
    }

    /**
     * update the velocity using the acceleration
     */
    public void updateVelocity() {
        this.setVelocity(getVelocity().addVector(acceleration));
    }
    /**
     * update position of the particle using velocity
     */
    public void updatePosition() {
        this.position = position.add(getVelocity().getEnd());
        field.setSource(position);
    }

    /**
     * @return the mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * @param mass the mass to set
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * @return the acceleration
     */
    public Vector getAcceleration() {
        return acceleration;
    }

    /**
     * @param acceleration the acceleration to set
     */
    public void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * @param velocity the velocity to set
     */
    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }
}
