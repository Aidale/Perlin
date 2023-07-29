package vector;

public class Vector2D
{
    private double xMag, yMag;
    private double mag, angle;
    public Vector2D(double arg1, double arg2, String type)
    {
	if (type.equals("polar"))
	{
	    mag = arg1;
	    angle = arg2;
	    xMag = mag * Math.cos(angle);
	    yMag = mag * Math.sin(angle);		   
	}
	else if (type.equals("cartesian"))
	{
	    xMag = arg1;
	    yMag = arg2;
	    angle = Math.atan2(yMag, xMag);
	    mag = Math.sqrt(xMag * xMag + yMag * yMag);
	}
    }
    
    public static double dotProduct(Vector2D vec1, Vector2D vec2)
    {
	return vec1.mag * vec2.mag * Math.cos(vec1.angle - vec2.angle);
    }   
    
    public double getXMag()
    {
	return xMag;
    }
    
    public double getYMag()
    {
	return yMag;
    }
}
