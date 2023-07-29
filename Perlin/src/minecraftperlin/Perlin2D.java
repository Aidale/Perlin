package minecraftperlin;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Perlin2D
{
    private float[][] chunkVectors;
//    private Vector2f[][] chunkVectors;
    private float[][] normalizedHeightMap;
    private float[][] heightMap;
    
    private int chunkCount, chunkSize, mapSize;
    
    public Perlin2D(int seed, int chunkCount)
    {
	this.chunkCount = chunkCount;
	this.chunkSize = 16;
	this.mapSize = chunkSize * chunkCount;
	Random random = new Random(seed);
	
	normalizedHeightMap = new float[mapSize][mapSize];
	heightMap = new float[chunkCount * chunkSize][chunkCount * chunkSize];
	chunkVectors = new float[chunkCount][chunkCount];
//	chunkVectors = new Vector2f[chunkCount][chunkCount];

	for (int x = 0; x < chunkCount; x++)
	{
	    for (int y = 0; y < chunkCount; y++)
	    {
		chunkVectors[x][y] = (float)(2 * Math.PI * random.nextFloat());
//		double angle = 2 * Math.PI * random.nextFloat();
//		chunkVectors[x][y] = new Vector2f((float)Math.cos(angle), (float)Math.sin(angle));
	    }
	}
	generateMap();
    }
    
    private void generateMap()
    {
	float minValue = Float.MAX_VALUE;
	float maxValue = Float.MIN_VALUE;
	
	for (int x = 0; x < chunkCount; x++)
	{
	    for (int y = 0; y < chunkCount; y++)
	    {
		Vector2f gradVec00 = toCartesian(chunkVectors[x][y]),	                              
			 gradVec01 = toCartesian(chunkVectors[x][(y + 1) % chunkCount]),                   
			 gradVec11 = toCartesian(chunkVectors[(x + 1) % chunkCount][(y + 1) % chunkCount]),
			 gradVec10 = toCartesian(chunkVectors[(x + 1) % chunkCount][y]);                   
//		Vector2f gradVec00 = chunkVectors[x][y];
//		Vector2f gradVec01 = chunkVectors[x][(y + 1) % chunkCount];
//		Vector2f gradVec11 = chunkVectors[(x + 1) % chunkCount][(y + 1) % chunkCount];
//		Vector2f gradVec10 = chunkVectors[(x + 1) % chunkCount][y];
		
		for (int cx = 0; cx < chunkSize; cx++)
		{
		    for (int cy = 0; cy < chunkSize; cy++)
		    {
			float xCor = (float)cx / chunkSize, xFade = fade(xCor),
			      yCor = (float)cy / chunkSize, yFade = fade(yCor);
			
			Vector2f distVec00 = new Vector2f(xCor, yCor),
				 distVec01 = new Vector2f(xCor, yCor - 1),
				 distVec11 = new Vector2f(xCor - 1, yCor - 1),
				 distVec10 = new Vector2f(xCor - 1, yCor);
			
			float dotProd00 = Vector2f.dot(distVec00, gradVec00),
			      dotProd01 = Vector2f.dot(distVec01, gradVec01),
			      dotProd11 = Vector2f.dot(distVec11, gradVec11),
			      dotProd10 = Vector2f.dot(distVec10, gradVec10);
//			float dotProd00 = Vector2f.dot(gradVec00, distVec00),
//		              dotProd01 = Vector2f.dot(gradVec01, distVec01),
//		              dotProd11 = Vector2f.dot(gradVec11, distVec11),
//		              dotProd10 = Vector2f.dot(gradVec10, distVec10);
			
			float average = lerp(lerp(dotProd00, dotProd01, yFade), lerp(dotProd10, dotProd11, yFade), xFade);
//			float average = lerp(lerp(dotProd00, dotProd10, xFade), lerp(dotProd01, dotProd11, xFade), yFade);
			normalizedHeightMap[x * chunkSize + cx][y * chunkSize + cy] = average;
//			System.out.println("x: " + (x * chunkSize + cx) + ", y: " + (y * chunkSize + cy) + ", average: " + average);
			
			if (minValue > average) minValue = average;
			if (maxValue < average) maxValue = average;
		    }
		}
	    }
	}
	
	
	for (int x = 0; x < mapSize; x++)
	{
	    for (int y = 0; y < mapSize; y++)
	    {
		//makes everything on a scale from 0 to 1
		heightMap[x][y] = unlerp(minValue, maxValue, normalizedHeightMap[x][y]);
//		System.out.println("heightMap[" + x + "][" + y + "] = " + heightMap[x][y]);
	    }
	}
    }
    
    public void renderMap(Graphics g, int WIDTH, int HEIGHT)
    {
	int blockPixelSize = (int)Math.min(WIDTH / mapSize, HEIGHT / mapSize);
	for (int x = 0; x < mapSize; x++)
	{
	    for (int y = 0; y < mapSize; y++)
	    {
		int grayscale = (int)(heightMap[x][y] * 255);
		g.setColor(new Color(grayscale, grayscale, grayscale));
		g.fillRect(x * blockPixelSize, y * blockPixelSize, blockPixelSize, blockPixelSize);
	    }
	}
    }
   
    private float fade(float a) 
    {
	return a * a * a * (a * (a * 6 - 15) + 10);
    }
    private float lerp(float x, float y, float input)
    {
	return x + (y - x) * input;
    }    
    private float unlerp(float x, float y, float input)
    {
	return (input - x) / (y - x);
    }  
    private static Vector2f toCartesian(float angle)
    {
	return new Vector2f((float)Math.cos(angle), (float)Math.sin(angle));
    }
}
