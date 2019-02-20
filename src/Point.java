
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * The point class models a point in 2D Cartesian space
 * simple class to wrap 2 double values together
 * @author Alex
 */
public class Point implements Serializable//Point class to describe a point in the simulation.
{
  private double x;
  private double y;
  
  /**
   * Constructor for point class
   * @param x
   * @param y 
   */
  public Point(double x, double y)
  {
    this.x = x;
    this.y = y;
  }
  
  /**
   * sets the x value
   * @param x 
   */
  public void setX(double x)
  {
    this.x = x;
  }
  
  /**
   * sets the y value
   * @param y 
   */
  public void setY(double y)
  {
    this.y = y;
  }
  
  /**
   * 
   * @return x value
   */
  public double getX()
  {
    return this.x;
  }
  
  /**
   * 
   * @return y value
   */
  public double getY()
  {
    return this.y;
  }
  /**
   * adds the coordinates of point other to this point and returns the resultant
   * @param other
   * @return the resultant point
   */
  public Point add(Point other)
  {
      return new Point(x + other.getX(), y + other.getY());
  }
  
  //the two methods below are useful for determining field strength in field calculations
  
  /**
   * returns the double distance from another point
   * @param other
   * @return the resultant point
   */
  public double distanceFrom(Point other) 
  {
      return Math.hypot(x - other.getX(), y - other.getY());
  }
  
  /**
   * returns the bearing of this point from another point
   * @param other
   * @return 
   */
  public double bearingFrom(Point other) 
  {
      double bearing = Math.atan2(y-(other.getY()), (x - other.getX()));
      return bearing;
  }
  
  @Override
  public String toString()
  {
    return "(" + this.x + "," + this.y + ")" ;
  }
}
