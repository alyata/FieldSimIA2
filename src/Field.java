
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Represents the electrical field of (a) charged particle(s) in the simulation
 * Each Field object is made of up to 2 more fields. This allows the Field class to have a recursive structure.
 * Constructing resultant fields are done recursively.
 * @author alex
 */
public class Field implements Serializable{
    private Field[] members;
    private int memberCount;
    public static final double COULOMB = 8.98755E+9;
    //use to define base case
    private Point source;
    private double charge;
    
    /**
     * The constructor for the field class
     * @param source
     * @param charge 
     */
    public Field(Point source, double charge)
    {
        this.source = source;
        this.charge = charge;
        this.members = new Field[2];
        memberCount = 0;
    }
    
    /**
     * Recursive algorithm to derive the field strength at a point. 
     * @param position
     * @return the field strength at the given point
     */
    public Vector fieldStrengthAtPoint(Point position)
    {
        //base case
        if(members[0] == null && members[1] == null) {
            //bearingAdjustment adjusts the direction of the fieldStength based on the sign of the electrical charge
            double bearingAdjustment = 0.0;
            if (charge > 0) {
                bearingAdjustment = Math.PI;
            }
            double magnitude = (COULOMB*Math.abs(charge))/Math.pow(position.distanceFrom(source), 2);
            double bearing = source.bearingFrom(position) + bearingAdjustment;
            return new Vector(position, magnitude, bearing);
        }
        //recursive case
        else
        {
            try 
            {
                Vector firstVector = members[0].fieldStrengthAtPoint(position);
                Vector secondVector = members[1].fieldStrengthAtPoint(position);
                return firstVector.addVector(secondVector);
            } catch(NullPointerException exception) 
            {
                if (members[0] == null) {
                    return members[1].fieldStrengthAtPoint(position);
                } else {
                    return members[0].fieldStrengthAtPoint(position);
                }
            }
        }
    }
    
    /**
     * add a field to become a member of this field
     * Makes it a recursive field
     * @param newField 
     */
    public void addField(Field newField)
    {
        if(memberCount < 2)
        {
            members[memberCount] = newField;
            memberCount++;
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * @return the source
     */
    public Point getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(Point source) {
        this.source = source;
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
    }

    /**
     * @return the members
     */
    public Field[] getMembers() {
        return members;
    }
}
