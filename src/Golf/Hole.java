package Golf;

import java.awt.Color;
import java.awt.Graphics;

class Hole extends Circle
{
	Vec2D	tvec	= new Vec2D();

	Hole(int x, int y, int diam)
	{
		super(x, y, diam);
	}

	public void draw(Graphics g)
	{
		g.setColor(Color.black);
		g.fillOval((int) (x - radius), (int) (y - radius), diam, diam);
	}
	
	
	public void influence (GolfBall ball) {
		double distIn = radius - ball.radius;
		double hbDist = dist(ball);
		// Check for and add influence of hole
		if (hbDist < radius  &&  hbDist > distIn) {
			tvec.setVec(x - ball.x, y - ball.y);
			tvec.unitVec();
			ball.vel.addVec(tvec);
		}
		// Check for ball inside hole
		ball.sunk |= ball.vel.mag() < radius  &&  hbDist < distIn;
		if (ball.sunk  &&  hbDist > distIn) {
			// Deflect ball's motion to keep it in the hole
			tvec.setVec(x - ball.x, y - ball.y);
			tvec.mulVec((hbDist - distIn) / hbDist);
			ball.vel.addVec(tvec);
			// Compute ball's position after bouncing off wall
			tvec.setVec(x - ball.x, y - ball.y);
			double m2 = tvec.mag() - distIn;
			tvec.unitVec();
			tvec.mulVec(m2);
			ball.addPos(tvec);
		}
	}

}