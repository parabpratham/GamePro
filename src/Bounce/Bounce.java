package Bounce;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class Bounce extends Applet implements Runnable {

	private static final long serialVersionUID = 1L;
	private Thread ticker;
	private boolean running = false;
	private Rectangle bounds;
	private Ball ball1;
	private Ball ball2;
	private Ball ball3;
	private int width, height;
	private Graphics offscr;
	private Image offscreenImage;

	public void init() {
		width = width = size().width;
		height = size().height;
		bounds = new Rectangle(width, height);
		// Initialize Ball position and velocity
		ball1 = new Ball(width / 4f, height / 3f, 2.5f, 4.3f, 12, Color.red);
		ball2 = new Ball(width / 3f, height / 4f, -2.5f, 4.3f, 12, Color.blue);
		ball3 = new Ball(width / 2f, height / 1f, -4.5f, 2.3f, 12, Color.blue);
	}

	public void run() {

		while (running) {
			repaint();
			try {
				ticker.sleep(1000 / 15);
			} catch (Exception e) {
				stop();
			}
		}
	}

	public synchronized void start() {
		if (ticker == null || !(ticker.isAlive())) {
			running = true;
			ticker = new Thread(this);
			ticker.setPriority(Thread.MIN_PRIORITY + 1);
			ticker.start();
		}
	}

	public synchronized void stop() {
		running = false;
	}

	public void paint(Graphics g) {
		if (offscr == null) {
			offscreenImage = createImage(width, height);
			offscr = offscreenImage.getGraphics();
		}
		// Draw checkerboard background
		int x2 = width/2 - 1;
		int y2 = height/2 - 1;
		offscr.setColor(Color.blue);
		offscr.fillRect(0, 0, x2, y2);
		offscr.fillRect(x2, y2, width - x2, height - y2);
		offscr.setColor(Color.red);
		offscr.fillRect(x2, 0, width - x2, height - y2);
		offscr.fillRect(0, y2, x2, y2);
		ball2.move(bounds);
		ball2.draw(offscr);
		ball1.move(bounds);
		ball1.draw(offscr);
		ball3.move(bounds);
		ball3.draw(offscr);
		g.drawImage(offscreenImage, 0, 0, null);
	}
	@Override
	public void update(Graphics g) {
		// TODO Auto-generated method stub
		paint(g);
	}
}
