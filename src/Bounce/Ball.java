package Bounce;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

class Ball {
	public float x, y, dx, dy;
	private Color color;
	private int size;

	Ball(float x, float y, float dx, float dy, int size, Color color) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.color = color;
		this.size = size;
	}

	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int) x, (int) y, size, size);
	}

	public void move(Rectangle bounds) {
		// Add velocity values dx/dy to position to get ,
		// ball s new position
		x += dx;
		y += dy;
		// Check for collision with left edge
		if (x < bounds.x && dx < 0) {
			dx = -dx;
			x -= 2 * (x - bounds.x);
		}
		// Check for collision with right edge
		else if ((x + size) > (bounds.x + bounds.width) && dx > 0) {
			dx = -dx;
			x -= 2 * ((x + size) - (bounds.x + bounds.width));
		}
		// Check for collision with top edge
		if (y < bounds.y && dy < 0) {
			dy = -dy;
			y -= 2 * (y - bounds.y);
		}
		// Check for collision with bottom edge.
		else if ((y + size) > (bounds.y + bounds.height) && dy > 0) {
			dy = -dy;
			y -= 2 * ((y + size) - (bounds.y + bounds.height));
		}
		
		//Change Colour
		int x2 = bounds.width/2 - 1;
		int y2 = bounds.height/2 - 1;
		if((x>=0 && x<=x2) && (y>=0 && y<=y2))
			this.color = color.white;
		else if((x>=x2 && x<=bounds.width) && (y>=y2 && y<=bounds.height))
			this.color = color.white;
		else 
			this.color = color.black;
		System.out.println(x+" , "+y + " "+this.color);
	}

}