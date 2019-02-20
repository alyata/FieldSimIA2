;;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * The vector class consists of two points, 
 * the end and the beginning point. 
 * It uses absolute referencing contrary to actual vectors.
 * For any relative vectors, 
 * eg. velocity, force, the absolute referencing should not matter and it is recommended to start at (0,0).
 * @author alex
 */

public class Vector implements Serializable{
    private Point start;
    private Point end;
    
    /**
     * sets the vector with absolute point referencing, taking points as the parameter
     * @param start
     * @param end 
     */
    public Vector(Point start, Point end) 
    {
        this.start = start;
        this.end = end;
    }
    
    /**
     * sets the vector by defining a start point, a magnitude and a bearing. Useful for angle calculations
     * @param start
     * @param magnitude
     * @param bearing 
     */
    public Vector(Point start, double magnitude, double bearing)
    {
        this.start = start;
        //the coordinates below are relative coordinates
        double endX = magnitude*Math.cos(bearing);
        double endY = magnitude*Math.sin(bearing);
        this.end = start.add(new Point(endX, endY));
    }
    
    /**
     * returns the start point
     * @return the start point
     */
    public Point getStart() 
    {
        return start;
    }
    
    /**
     * sets the new start point
     * @param newPoint 
     */
    public void setStart(Point newPoint) 
    {
        start = newPoint;
    }
    
    /**
     * returns the end point
     * @return the end point
     */
    public Point getEnd() 
    {
        return end;
    }
    
    /**
     * sets the new end point
     * @param newPoint 
     */
    public void setEnd(Point newPoint) 
    {
        end = newPoint;
    }
    
    /**
     * returns the bearing in radians, relative to the positive horizontal
     * @return 
     */
    public double getBearing() 
    {
        return end.bearingFrom(start);
    }
    
    /**
     * returns the magnitude of the vector
     * 
     * @return 
     */
    public double getMagnitude() 
    {
        return end.distanceFrom(start);
    }
    
    /**
     * returns the horizontal component of the vector
     * 
     * @return 
     */
    public double getX() 
    {
        return this.end.getX() - this.start.getX();
    }
    
    /**
     * returns the vertical component of the vector
     * 
     * @return 
     */
    public double getY() 
    {
        return this.end.getY() - this.start.getY();
    }
   
    /**
     * adds a vector to this vector and returns the resultant vector
     * 
     * @param other
     * @return 
     */
    public Vector addVector(Vector other) 
    {
        Point newEnd = new Point(other.getX(), other.getY()).add(this.end);
        return new Vector(this.start, newEnd);
    }
    
    /**
     * multiplies the magnitude with a scalar, and return this new vector
     * @param scalar
     * @return 
     */
    public Vector multiplyScalar(double scalar)
    {
        return new Vector(this.getStart(), this.getMagnitude()*scalar, this.getBearing());
    }

    /**
     * divides the magnitude with a scalar, and return this new vector
     * @param scalar
     * @return 
     */
    public Vector divideScalar(double scalar)
    {
        return new Vector(this.getStart(), this.getMagnitude()/scalar, this.getBearing());
    }
    
    public void setMagnitude(double newMagnitude)
    {
        double ratio = newMagnitude/this.getMagnitude();
        this.end = this.start.add(new Point(ratio*this.getX(), ratio*this.getY()));
    }
    
    public void setBearing(double newBearing)
    {
        double currentMagnitude = this.getMagnitude();
        this.end = this.start.add(new Point(
                currentMagnitude*Math.cos(newBearing),
                currentMagnitude*Math.sin(newBearing)
        ));
    }
    /**
     * 
     * @return the String representation of the object
     */
    @Override
    public String toString() {
        return (start + " -> " + end);
    }
}
