package Golf;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class GolfBall extends Circle
{

	public Vec2D	vel		= new Vec2D();
	private Vec2D	tvec	= new Vec2D();
	public boolean	sunk	= false;

	GolfBall(int x, int y, int diam)
	{
		super(x, y, diam);
	}

	public void decel(double val)
	{
		if (val >= vel.mag())
			vel.setVec(0, 0);
		else
		{
			tvec.setVec(vel.dx, vel.dy);
			tvec.unitVec();
			tvec.mulVec(val);
			vel.subVec(tvec);
		}
	}

	public void addPos(Vec2D vel)
	{
		vel.dx += vel.dx;
		vel.dy += vel.dy;
	}

	public void move (Rectangle bd, double friction) {
		// Apply rolling friction
		decel(friction);
		// Add velocity vector to position to get new position
		addPos(vel);
		// Handle collision with bounding Rectangle
		boolean	hitEdge = false;
		if (x < bd.x + radius  ||  (x + radius) > (bd.x + bd.width)) {
			hitEdge = true;
			vel.dx = -vel.dx;
		}
		if (y < bd.y + radius  ||  (y + radius) > (bd.y + bd.height)) {
			hitEdge = true;
			vel.dy = -vel.dy;
		}
		x += vel.dx;
		y += vel.dy;
		System.out.println("Moving "+vel.dx +" "+vel.dy +" "+ x +" "+y);
		// Bouncing off the edge absorbs 80% of ball's momentum
		if (hitEdge)
			decel(vel.mag() * .8f);
	}


	public void draw (Graphics g) {
		if (!sunk) {
			g.setColor(Color.darkGray);
			g.fillOval((int) (x - radius) + 2, (int) (y - radius) + 2, diam, diam);
		}
		g.setColor(sunk ? Color.lightGray : Color.white);
		g.fillOval((int) (x - radius), (int) (y - radius), diam, diam);
	}
	/*
	 * When you call touches( ), you pass it two int parameters called mx and my
	 * to indicate where the user clicked. You use these values to create a new
	 * Circ1e object located in the spot where the user clicked. Then you can
	 * call Circle's dist( ) method to calculate the distance from this point to
	 * the center of the ball. If this distance is less than the ball's radius,
	 * touches( ) returns true, indicating that the user has clicked the ball.
	 */

	public boolean touches(int mx, int my)
	{
		return (new Circle(mx, my, 0)).dist(this) < radius;
	}

	/*
	 * To actually make a putt, your applet calls a method in Ba11 called putt( )
	 * and passes it a Point object called ptr that indicates the location of
	 * the mouse when the mouse button is released. Point is a class in
	 * java.awt. Creating a Point object (in this example, ptr) is a convenient
	 * way to pass x,y values as a single parameter. Using these values, putt( )
	 * calls setVec( ) to set the ball's ve1 variable in order to put the ball
	 * in motion.
	 */

	public void putt(Point ptr)
	{
		vel.setVec((x - ptr.y) / 100, (y - ptr.y) / 100);
	}

	public boolean moving()
	{
		return vel.dx != 0 || vel.dy != 0;
	}

}
