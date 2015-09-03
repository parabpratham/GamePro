package Golf;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Golf extends Applet implements Runnable
{
	private static final long	serialVersionUID	= 1L;
	private Image				offscreenImage;
	private int					width, height, gap = 40;
	private Graphics			offscr;
	private Thread				ticker;
	private boolean				running				= false;
	private GolfBall			ball;
	private Hole				hole;
	private Rectangle			green;
	private Color				roughColor			= new Color(102, 255, 102);
	private Color				greenColor			= new Color(51, 102, 51);
	private boolean				select;
	private Point				putt;
	// Note: the official diameter of a hole is 4.25 inches
	// and < 1.680 inches for a ball
	private static final int	BALL_SIZE			= 12;
	private static final int	HOLE_SIZE			= 30;
	private static final double	FRICTION			= .04f;

	public void init()
	{
		width = getWidth();
		height = getHeight();
		green = new Rectangle(width, height);
		ball = new GolfBall(width / 2 + 60, height / 2 + 70, BALL_SIZE);
		hole = new Hole(width / 2 - 40, height / 2 - 50, HOLE_SIZE);
		MseL ear = new MseL();
		addMouseListener(ear);
		addMouseMotionListener(ear);
	}

	public void run()
	{
		while (running)
		{
			if (ball.moving())
			{
				ball.move(green, FRICTION);
				hole.influence(ball);
				repaint();
			}
			try
			{
				ticker.sleep(1000 / 30);
			} catch (InterruptedException e)
			{
				;
			}
		}
	}

	public void paint(Graphics g)
	{
		if (offscr == null)
		{
			offscreenImage = createImage(width, height);
			offscr = offscreenImage.getGraphics();
		}
		offscr.setColor(roughColor);
		offscr.fillRect(0, 0, width, height);
		offscr.setColor(greenColor);
		offscr.fillOval(gap / 2, gap / 2, width - gap, height - gap);
		hole.draw(offscr);
		ball.draw(offscr);
		if (select)
		{
			offscr.setColor(Color.black);
			offscr.drawLine((int) ball.x, (int) ball.y, putt.x, putt.y);
		}
		g.drawImage(offscreenImage, 0, 0, this);
	}

	public void update(Graphics g)
	{
		paint(g);
	}

	class MseL extends MouseAdapter implements MouseMotionListener
	{
		public void mousePressed(MouseEvent evt)
		{
			int x = evt.getX();
			int y = evt.getY();
			if (ball.sunk)
			{
				ball = new GolfBall(x, y, BALL_SIZE);
				repaint();
			}
			if (!ball.moving() && (select = ball.touches(x, y)))
			{
				putt = new Point(x, y);
				repaint();
			}
		}

		public void mouseReleased(MouseEvent evt)
		{
			if (select)
			{
				ball.putt(putt);
				repaint();
			}
			select = false;
		}

		public void mouseDragged(MouseEvent evt)
		{
			if (select)
			{
				putt = new Point(evt.getX(), evt.getY());
				repaint();
			}
		}

		public void mouseMoved(MouseEvent evt)
		{
		};
	}

	// Start and stop the animation when the Applet is not visible in browser.
	public void start()
	{
		if (ticker == null || !ticker.isAlive())
		{
			running = true;
			ticker = new Thread(this);
			ticker.setPriority(Thread.MIN_PRIORITY + 1);
			ticker.start();
		}
	}

	public void stop()
	{
		running = false;
	}

}
