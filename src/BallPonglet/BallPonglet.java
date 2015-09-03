package BallPonglet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class BallPonglet
{

	public float	x, y, dx, dy;
	public int		size, radius;
	private Color	color;

	BallPonglet(float x, float y, float dx, float dy, int size, Color color)
	{
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.color = color;
		this.size = size;
		radius = size - 1;
		System.out.println(dx + "-" + dy);
	}

	public void move(Rectangle pd)
	{
		// Add velocity to position to get new position
		x += dx;
		y += dy;
		// Check for collision with bounding Rectangle
		if ((x < pd.x && dx < 0f) || ((x + size) > (pd.x + pd.width) && dx > 0f))
			x += (dx = -dx);
		// System.out.println(x+"-"+y);
	}

	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillOval((int) x, (int) y, size, size);
	}
}
