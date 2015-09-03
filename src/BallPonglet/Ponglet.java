package BallPonglet;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.crypto.spec.PSource;

public class Ponglet extends Applet implements Runnable
{
	private static final long	serialVersionUID	= 1L;
	private Thread				ticker;
	private boolean				running				= false;
	private BallPonglet			ball;
	private Paddle				gPaddle;
	private Paddle				pPaddle;
	private int					MAX_SCORE			= 2;
	private int					pScore				= 0;
	private int					gScore				= 0;
	private static final int	WAIT				= 1;
	private static final int	SERVE				= 2;
	private static final int	RETURN				= 4;
	private static final int	PGUTTER				= 8;
	private static final int	GGUTTER				= 16;
	private static final int	PSCORE				= 32;
	private static final int	GSCORE				= 64;
	private static final int	PW0N				= 128;
	private static final int	GWON				= 256;
	private int					gstate				= WAIT;
	private boolean				mouse_in			= false;
	private Graphics			offscr;
	private Image				offscreenImage;
	private int					width, height;
	private Rectangle			table;
	private int					delay;
	private int					win_show;
	private Font				font;
	private FontMetrics			fontMet;
	private Dimension			msePad;
	private Point				player;
	private Point				game;
	private int					fontHeight;
	private int					trackX;
	private int					ballSize			= 10;

	public void init()
	{
		width = width = size().width;
		height = size().height;
		table = new Rectangle(width - 5, height - 5);
		msePad = new Dimension(width, height - width);
		player = new Point(width - width / 4, 5);
		game = new Point(width / 4, 5);
		// Create offscreen image
		offscreenImage = createImage(width, height);
		offscr = offscreenImage.getGraphics();
		// Setup text font for displaying the score
		font = new Font("TimesRoman", Font.PLAIN, 14);
		fontMet = getFontMetrics(font);
		fontHeight = fontMet.getAscent();
		// Draw Scores
		offscr.setFont(font);
		centerText(offscr, game, Color.white, "" + gScore);
		centerText(offscr, player, Color.gray, "" + pScore);

		pPaddle = new Paddle(((int) (trackX = width / 2)), height - 3, 20, 3, Color.green);
	}

	public void run()
	{
		while (running)
		{
			System.out.println(mouse_in + " " + gstate);
			switch (gstate)
			{
			case WAIT:
				if (!mouse_in)
					delay = 20;
				else
					if (--delay < 0)
					{
						// Serve the ball
						int sLoc = rndlnt(table.width - ballSize) + (ballSize - 1);
						ball = new BallPonglet(sLoc - ballSize, rnd(5f) + 0, 5f, rnd(4f), ballSize, Color.blue);
						gstate = SERVE;
						win_show = 100;
						delay = 20;

					}
				break;
			case SERVE:
				// Check for ball in position for player to hit
				gstate = pPaddle.checkReturn(ball, true, SERVE, RETURN, PGUTTER);
				if (gstate == RETURN)
					gPaddle = new Paddle(((int) (trackX = width / 2)), 3, 20, 3, Color.red);
				break;
			case RETURN:
				// Implement our simple-minded computer opponent
				if (Math.abs(gPaddle.x - ball.x) >= 1)
					gPaddle.move((int) (trackX += (gPaddle.x < ball.x ? 1.5f : -1.5f)), table);
				// Check for ball in position for game to hit
				gstate = gPaddle.checkReturn(ball, false, RETURN, SERVE, GGUTTER);
				break;
			case PGUTTER:
				// Wait for computer s scoring ball to move off table
				if ((int) ball.y > (table.height + ball.radius))
					gstate = GSCORE;
				break;
			// The code for GGUTTER iS nearly identical:
			case GGUTTER:
				// Wait for player s scoring ball to move off table
				if ((int) ball.y < (table.y - ball.radius))
					gstate = PSCORE;
				break;
			case PSCORE:// Increment player s score and check if she has won
				gstate = (++pScore >= MAX_SCORE ? PW0N : WAIT);
				break;
			case GSCORE:// Increment computer s score and check if it has won
				gstate = (++gScore >= MAX_SCORE ? GWON : WAIT);
				break;
			case PW0N:
			case GWON:
				// Delay while we show who won
				if (--win_show < 0)
				{
					gstate = WAIT;
					gScore = pScore = 0;
				}
				break;
			}
			repaint();
			try
			{
				ticker.sleep(1000 / 15);
			} catch (Exception e)
			{
				stop();
			}
		}
	}

	public float rnd(float range)
	{
		return (float) Math.random() * range;
	}

	public int rndlnt(int range)
	{
		return (int) (Math.random() * range);
	}

	public boolean mouseEnter(Event evt, int x, int y)
	{
		pPaddle.move(x, table);
		mouse_in = true;
		return true;
	}

	public boolean mouseExit(Event evt, int x, int y)
	{
		mouse_in = false;
		return true;
	}

	public boolean mouseMove(Event evt, int x, int y)
	{
		pPaddle.move(x, table);
		return true;
	}

	public void paint(Graphics g)
	{
		if (offscr == null)
		{
			offscreenImage = createImage(width, height);
			offscr = offscreenImage.getGraphics();
		}
		// Fill offscreen buffer with a background B/W checkerboard
		int x2 = table.width - 1;
		int y2 = table.height - 1;
		offscr.setColor(Color.gray);
		offscr.fillRect(0, 0, x2, y2);
		offscr.fillRect(x2, y2, table.width - x2, table.height - y2);
		offscr.setColor(Color.white);
		offscr.fillRect(x2, 0, table.width - x2, table.height - y2);
		offscr.fillRect(0, y2, x2, y2);
		if (gstate == PW0N || gstate == GWON)
		{
			Point winner = gstate == GWON ? game : player;
			Point loc = new Point(winner.x, winner.y + 15);
			String winnerS = gstate == GWON ? "Game" : "Player";
			centerText(offscr, loc, Color.black, winnerS + " Win");
		} else
		{
			// Draw ball
			if (gstate == SERVE || gstate == RETURN || gstate == PGUTTER)
			{
				ball.move(table);
				ball.draw(offscr);
			}

			// Draw players paddle
			if (mouse_in && (gstate == SERVE || gstate == RETURN || gstate == PGUTTER || gstate == GGUTTER))
				pPaddle.draw(offscr);
			// Draw computer s paddle

			if (gstate == PSCORE || gstate == GSCORE)
			{
				String text = "";
				text = "Player : " + pScore + " Comp : " + gScore;
				Point loc = new Point(table.width - 2, table.height + ((msePad.height - fontHeight)) - 2);
				centerText(offscr, loc, Color.black, text);
			}

			if (gstate == RETURN)
				gPaddle.draw(offscr);

		}

		// Fill in mouse pad area
		offscr.setColor(Color.yellow);
		offscr.fillRect(0, msePad.width, table.width, msePad.height);
		if (!mouse_in)
		{
			Point loc = new Point(table.width - 1, table.height + ((msePad.height - fontHeight)) - 1);
			centerText(offscr, loc, Color.black, "Move Mouse Here");
		}

		g.drawImage(offscreenImage, 0, 0, this);
	}

	private void centerText(Graphics g, Point loc, Color clr, String str)
	{
		g.setColor(clr);
		g.drawString(str, loc.x - -(fontMet.stringWidth(str) / 2), loc.y + fontHeight);
	}

	public synchronized void start()
	{
		if (ticker == null || !(ticker.isAlive()))
		{
			running = true;
			ticker = new Thread(this);
			ticker.setPriority(Thread.MIN_PRIORITY + 1);
			ticker.start();
		}
	}

	public synchronized void stop()
	{
		running = false;
	}

	@Override
	public void update(Graphics g)
	{
		// TODO Auto-generated method stub
		paint(g);
	}
}
