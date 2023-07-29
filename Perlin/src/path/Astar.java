package path;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.TreeSet;

import perlin.Map;
import perlin.SubChunk;

public class Astar
{
    public static void findPath(Map map, int startX, int startY, int endX, int endY, Graphics g, int WIDTH, int HEIGHT)
    {
	int sideLength = map.getSideLength();
	if (!(btwn(0, startX, sideLength) && btwn(0, startY, sideLength) && btwn(0, endX, sideLength) && btwn(0, endY, sideLength)))
	{
	    System.out.println("Invalid path parameters");
	    System.exit(0);
	}
	SubChunk[][] subChunks = map.getSubChunks();
	
	ArrayList<SubChunk> openSet = new ArrayList<SubChunk>();
	openSet.add(subChunks[startY][startX]);
	
	SubChunk[][] predecessor = new SubChunk[sideLength][sideLength];
	predecessor[startY][startX] = null;
	
	double[][] gScore = new double[sideLength][sideLength];
	double[][] fScore = new double[sideLength][sideLength];	
	for (int y = 0; y < sideLength; y++)
	{
	    for (int x = 0; x < sideLength; x++)
	    {
		gScore[y][x] = Integer.MAX_VALUE;
		fScore[y][x] = Integer.MAX_VALUE;
	    }
	}

	gScore[startY][startX] = 0;
	fScore[startY][startX] = heur(startX, startY, endX, endY);
	
	while (openSet.size() != 0)
	{
	    int curX = 0, curY = 0;
	    double minFScore = Integer.MAX_VALUE;
	    for (int y = 0; y < sideLength; y++)
	    {
		for (int x = 0; x < sideLength; x++)
		{
		    if (fScore[y][x] < minFScore && openSet.contains(subChunks[y][x]))
		    {
			minFScore = fScore[y][x];
			curX = x;
			curY = y;
		    }
		}
	    }
	    
	    int subChunkPixelSize = (int)Math.min(WIDTH / sideLength, HEIGHT / sideLength);
//	    for (int s = 0; s < openSet.size(); s++)
//	    {
//		double openSetValue = 1 - openSet.get(s).getValue();
//		Color openSetColor = new Color((int)(openSetValue * 255), (int)(openSetValue * 63), (int)(openSetValue * 255));
//		g.setColor(openSetColor);
//		g.fillRect(openSet.get(s).getX() * subChunkPixelSize, openSet.get(s).getY() * subChunkPixelSize, subChunkPixelSize, subChunkPixelSize);
//	    }
//	    double currentColorValue = 1 - subChunks[curY][curX].getValue();
//	    Color currentColor = new Color((int)(currentColorValue * 255), (int)(currentColorValue * 63), (int)(currentColorValue * 63));
//	    g.setColor(currentColor);
//	    g.fillRect(subChunks[curY][curX].getX() * subChunkPixelSize, subChunks[curY][curX].getY() * subChunkPixelSize, subChunkPixelSize, subChunkPixelSize);
	    
	    if (curX == endX && curY == endY)
	    {
		int prevX, prevY;
		ArrayList<SubChunk> reversePath = new ArrayList<SubChunk>();
		reversePath.add(subChunks[curY][curX]);
		
		while (predecessor[curY][curX] != null)
		{
		    prevX = predecessor[curY][curX].getX();
		    prevY = predecessor[curY][curX].getY();
		    reversePath.add(subChunks[prevY][prevX]);
		    curX = prevX;
		    curY = prevY;
		}
		
		for (int s = 0; s < reversePath.size(); s++)
		{
//		    double pathColorValue = 1 - reversePath.get(s).getValue();
//		    Color pathColor = new Color((int)(pathColorValue * 63), (int)(pathColorValue * 63), (int)(pathColorValue * 255));
//		    g.setColor(pathColor);
		    g.setColor(Color.black);
		    g.fillRect(reversePath.get(s).getX() * subChunkPixelSize, reversePath.get(s).getY() * subChunkPixelSize, subChunkPixelSize, subChunkPixelSize);
		}		
		return;
	    }
	    
	    openSet.remove(subChunks[curY][curX]);	    
	    for (int dy = -1; dy <= 1; dy++)
	    {
		for (int dx = -1; dx <= 1; dx++)
		{
		    if (dx == 0 && dy == 0) continue;
//		    System.out.println(subChunks[curY + dy][curX + dx].getValue());
//		    System.out.println("x: " + (curX + dx) + " y: " + (curY + dy));
		    
		    double newGScore;
		    try
		    {
			newGScore = subChunks[curY + dy][curX + dx].getValue() * subChunks[curY + dy][curX + dx].getValue() * 10 + gScore[curY][curX];
//			newGScore = subChunks[curY + dy][curX + dx].getValue() + gScore[curY][curX];
		    }
		    catch(Exception ex) {continue;}
		    
		    if (subChunks[curY + dy][curX + dx].getValue() == 1) continue;
		    
		    if (newGScore < gScore[curY + dy][curX + dx])
		    {
			predecessor[curY + dy][curX + dx] = subChunks[curY][curX];
			gScore[curY + dy][curX + dx] = newGScore;
			fScore[curY + dy][curX + dx] = newGScore + heur(curX + dx, curY + dy, endX, endY);
			if (!openSet.contains(subChunks[curY + dy][curX + dx]))
			{
//			    System.out.println("New subChunk added, x: " + (curX + dx) + " y: " + (curY + dy));
			    openSet.add(subChunks[curY + dy][curX + dx]);
			}
		    }					    
		}
	    }
	}
    }
    

    private static double heur(int curX, int curY, int endX, int endY)
    {
	return Math.sqrt((curX - endX) * (curX - endX) + (curY - endY) * (curY - endY));
    }
    
    private static boolean btwn(int a, int b, int c) {return a <= b && b < c;}
}
