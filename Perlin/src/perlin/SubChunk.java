package perlin;

public class SubChunk
{
    private double value;
    private int x, y;
    public SubChunk(int x, int y)
    {
	this.x = x;
	this.y = y;
	value = 0;
    }
    
    public void setValue(double newValue) {value = newValue;}
    public double getValue() {return value;}
    public int getX() {return x;}
    public int getY() {return y;}
}
