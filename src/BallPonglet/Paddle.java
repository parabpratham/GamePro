package BallPonglet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Paddle
{

	public int		x, y, width, height;
	private Color	color;

	Paddle(int x, int y, int width, int height, Color color)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	public void move(int x, Rectangle bd)
	{
		if (x > (width >> 1) && x < (bd.width - (width) - 1))
			this.x = x;
	}

	public int checkReturn(BallPonglet ball, boolean plyr, int rl, int r2, int r3)
	{
		if ((plyr && ball.y > (y - ball.radius)) || (!plyr && ball.y < (y + ball.radius)))
			if ((int) Math.abs(ball.x - x) < (width / 2 + ball.radius))
			{
				ball.dy = -ball.dy;
				// Put a little english on the ball
				ball.dx += (int) (ball.dx * Math.abs(ball.x - x) / (width / 2));
				return r2;
			} else
				return r3;

		return rl;
	}

	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillRect(x - (width >> 1), y, width, height);
	}
}
