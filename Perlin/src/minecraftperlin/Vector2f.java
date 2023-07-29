package minecraftperlin;

public class Vector2f
{
    public float x, y;
    public Vector2f(float x, float y)
    {
	this.x = x;
	this.y = y;
    }
    
    public float length()
    {
	return (float)Math.sqrt(x * x + y * y);
    }
    
    public static float dot(Vector2f v1, Vector2f v2)
    {
	return (float)(v1.length() * v2.length() * Math.cos(Vector2f.angle(v1, v2)));
    }
    
    public static float angle(Vector2f v1, Vector2f v2)
    {
	return (float)(Math.atan2(v1.y, v1.x) - Math.atan2(v2.y, v2.x));
    }
}
