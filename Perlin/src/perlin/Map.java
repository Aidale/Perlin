package perlin;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import path.Astar;
import vector.Vector2D;
public class Map
{
    private Vector2D[][] chunkVectors;
    private int subChunkCount, chunkCount, sideLength; 
    private SubChunk[][] subChunks;
    private int[] pathParameters;
    
    public Map(int chunkCount, int subChunkCount, int seed)
    {
	this.subChunkCount = subChunkCount;
	this.chunkCount = chunkCount;
	sideLength = subChunkCount * chunkCount;
	chunkVectors = new Vector2D[chunkCount][chunkCount];
	subChunks = new SubChunk[sideLength][sideLength];
	Random rand = new Random(seed);
	for (int y = 0; y < chunkCount; y++)
	{
	    for (int x = 0; x < chunkCount; x++)
	    {
		chunkVectors[y][x] = new Vector2D(1, 2 * Math.PI * (double)(rand.nextFloat()), "polar");
	    }
	}
	for (int y = 0; y < sideLength; y++)
	{
	    for (int x = 0; x < sideLength; x++)
	    {
		subChunks[y][x] = new SubChunk(x, y);
	    }
	}
    }
    
    public void generatePerlinChunks()
    {
	for (int y = 0; y < chunkCount; y++)
	{
	    for (int x = 0; x < chunkCount; x++)
	    {
		Vector2D gradVec00 = chunkVectors[y][x],	
			 gradVec01 = chunkVectors[(y + 1) % chunkCount][x],
			 gradVec11 = chunkVectors[(y + 1) % chunkCount][(x + 1) % chunkCount],
			 gradVec10 = chunkVectors[y][(x + 1) % chunkCount];
			
		for (int subY = 0; subY < subChunkCount; subY++)
		{
		    for (int subX = 0; subX < subChunkCount; subX++)
		    {
			double  xCor = (double)subX / subChunkCount, xFade = fade(xCor),
				yCor = (double)subY / subChunkCount, yFade = fade(yCor);						
			
			Vector2D distVec00 = new Vector2D(xCor    , yCor    , "cartesian"),				
				 distVec01 = new Vector2D(xCor    , yCor - 1, "cartesian"),		
				 distVec11 = new Vector2D(xCor - 1, yCor - 1, "cartesian"),			
				 distVec10 = new Vector2D(xCor - 1, yCor    , "cartesian");
			
			double  dotProd00 = Vector2D.dotProduct(distVec00, gradVec00),
				dotProd01 = Vector2D.dotProduct(distVec01, gradVec01),
				dotProd10 = Vector2D.dotProduct(distVec10, gradVec10),
				dotProd11 = Vector2D.dotProduct(distVec11, gradVec11);
			
			double average = lerp(lerp(dotProd00, dotProd01, yFade), lerp(dotProd10, dotProd11, yFade), xFade);
//			System.out.println("x: " + (x * subChunkCount + subX) + ", y: " + (y * subChunkCount + subY) + ", average: " + average);
			subChunks[y * subChunkCount + subY][x * subChunkCount + subX].setValue(average);
		    }
		}
	    }
	}
	
	double minValue = Integer.MAX_VALUE,
	       maxValue = Integer.MIN_VALUE;
	for (int y = 0; y < sideLength; y++)
	{
	    for (int x = 0; x < sideLength; x++)
	    {
		double value = subChunks[y][x].getValue();		    
		if (minValue > value) minValue = value;
		if (maxValue < value) maxValue = value;
	    }	    	
	}
	
	for (int y = 0; y < sideLength; y++)
	{
	    for (int x = 0; x < sideLength; x++)
	    {	
		double value = unlerp(minValue, maxValue, subChunks[y][x].getValue());
//		value = value > 0.6 ? 1 : 0.1;
		subChunks[y][x].setValue(value);				    	
	    }	    	
	}
    }
    
    public void generateRandChunks()
    {
	for (int y = 0; y < subChunkCount * chunkCount; y++)
    	{
    	    for (int x = 0; x < subChunkCount * chunkCount; x++)
    	    { 
    		subChunks[y][x].setValue(Math.random() > 0.7 ? 1 : 0.1);   		
    	    }	
    	}
    }
    
    public void renderMap(Graphics g, int WIDTH, int HEIGHT)
    {
	int subChunkPixelSize = (int)Math.min(WIDTH / sideLength, HEIGHT / sideLength);
	for (int y = 0; y < sideLength; y++)
	{
	    for (int x = 0; x < sideLength; x++)
	    {	
//		int value = colorBound((1 - subChunks[y][x].getValue()));
		double value = 1 - subChunks[y][x].getValue();  
//		Color color = new Color(value, value, value);
		Color color = rainbow(value);
		g.setColor(color);
		g.fillRect(x * subChunkPixelSize, y * subChunkPixelSize, subChunkPixelSize, subChunkPixelSize);
	    }
	}	
    }	
    
    public void setPathParameters(int startX, int startY, int endX, int endY)
    {
	pathParameters = new int[]{startX, startY, endX, endY};
    }
    
    public void renderPath(Graphics g, int WIDTH, int HEIGHT)
    {
	Astar.findPath(this, pathParameters[0], pathParameters[1], pathParameters[2], pathParameters[3], g, WIDTH, HEIGHT);
    }
    
    public SubChunk[][] getSubChunks() {return subChunks;}
    private double fade(double a) {return a * a * a * (a * (a * 6 - 15) + 10);}	
    private double lerp(double x, double y, double input) {return x + (y - x) * input;}    
    private double unlerp(double x, double y, double input) {return (input - x) / (y - x);}    
    private int colorBound(int x) {return x > 255 ? 255 : x < 0 ? 0 : x;}
    public int getSideLength() {return sideLength;}
    private Color rainbow(double x)
    {	
	int max = 360;
	int value = (int)(x * max);
	value = value % max;		
	boolean[] colorRange = new boolean[] {0 <= value && value < max / 3, max / 3 <= value && value < max * 2 / 3};
	
	int r = colorBound(colorRange[0] ? 510 - 1530 * value / max : colorRange[1] ? 0 : 1530 * (value - max * 2 / 3) / max);
	int g = colorBound(colorRange[0] ? 1530 * value / max : colorRange[1] ? 510 - 1530 * (value - max / 3) / max : 0);
	int b = colorBound(colorRange[0] ? 0 : colorRange[1] ? 1530 * (value - max / 3) / max : 510 - 1530 * (value - max * 2 / 3) / max);
	
	return new Color(r, g, b);
    }    
}
