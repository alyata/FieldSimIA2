import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * class to wrap vectors into an arraylist of fieldLines
 * Includes convenience methods to ease creation of field lines
 * @author alex
 */
public class FieldLine {
    
    private ArrayList<Vector> vectors;
    
    /**
     * Constructor for the class
     */
    public FieldLine() 
    {
        vectors = new ArrayList<>();
    }
    
    /**
     * 
     * @return the arraylist of vectors
     */
    public ArrayList<Vector> getVectors() 
    {
        return vectors;
    }
    
    /**
     * resets the vectors in the field line, making it empty again
     * use this to save memory space and reuse field lines
     */
    public void resetVectors() 
    {
        vectors.clear();
    }
    
    /**
     * absolute referencing of coordinates to add a vector into the object
     * used for first vector
     * @param newVector 
     */
    public void addVector(Vector newVector) 
    {
        
        vectors.add(newVector);
    }
    
    /**
     * uses relative referencing, newPoint relative to the last vector, to add new vectors into the object
     * used for subsequent vectors
     * @param newPoint 
     */
    public void addVector(Point newPoint) 
    {
        Point startPoint = vectors.get(vectors.size() - 1).getEnd();
        vectors.add(new Vector(startPoint, newPoint));
    }
    
    /**
     * returns the end point of the last vector on this fieldLine
     * @return 
     */
    public Point getEnd()
    {
        return vectors.get(vectors.size()-1).getEnd();
    }
    
    /**
     * returns the start point of the first vector on this fieldLine
     * @return 
     */
    public Point getStart()
    {
        return vectors.get(0).getStart();
    }
}
