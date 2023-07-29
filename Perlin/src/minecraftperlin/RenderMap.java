package minecraftperlin;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class RenderMap extends JPanel
{
    public static void main(String[] args)
    {
	new RenderMap(720, 720).repaint();
    }
    
    private Perlin2D perlin;
    private int WIDTH, HEIGHT;
    public RenderMap(int WIDTH, int HEIGHT)
    {
	this.WIDTH = WIDTH;
	this.HEIGHT = HEIGHT;
	perlin = new Perlin2D(0, 8);
	JFrame frame = new JFrame("Perlin2D Map");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.add(this);
    }
    
    public void paintComponent(Graphics g)
    {
	perlin.renderMap(g, WIDTH, HEIGHT);
    }
}
