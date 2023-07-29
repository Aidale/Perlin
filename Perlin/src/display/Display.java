package display;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import perlin.Map;

public class Display extends JPanel
{
    private Map map;
    private int WIDTH, HEIGHT;
    
    public Display(Map map, int WIDTH, int HEIGHT)
    {
	JFrame frame = new JFrame("A* Path finding");	
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.add(this);
	this.map = map;	
	this.WIDTH = WIDTH;
	this.HEIGHT = HEIGHT;
    }
    
    public void paintComponent(Graphics g)
    {
	map.renderMap(g, WIDTH, HEIGHT);
//	map.renderPath(g, WIDTH, HEIGHT);
    }
}
