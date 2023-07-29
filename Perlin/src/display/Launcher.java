package display;

import java.util.Random;

import perlin.Map;

public class Launcher
{
    public static void main(String[] args)
    {
	int WIDTH = 800, HEIGHT = 800;
	Random rand = new Random((int)(Math.random() * 1000));
	int chunkCount = 4;
	int subChunkCount = 16;
	int sideLength = chunkCount * subChunkCount;
	Map map = new Map(chunkCount, subChunkCount, 0);
	map.generatePerlinChunks();
	map.setPathParameters(rand.nextInt(sideLength), rand.nextInt(sideLength), rand.nextInt(sideLength), rand.nextInt(sideLength));
//	map.generateRandChunks();
	Display d = new Display(map, WIDTH, HEIGHT);
	d.repaint();
    }
}
