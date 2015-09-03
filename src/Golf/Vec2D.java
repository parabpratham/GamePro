package Golf;

public class Vec2D
{

	public double	dx, dy;

	public void setVec(double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}

	public double mag()
	{
		return (double) Math.sqrt(dx * dx + dy * dy);
	}

	public void addVec(Vec2D vec)
	{
		dx += vec.dx;
		dy += vec.dy;
	}

	public void subVec(Vec2D vec)
	{
		dx -= vec.dx;
		dy -= vec.dy;
	}

	public void unitVec()
	{
		double mag = mag();
		setVec(dx / mag, dy / mag);
	}

	public void mulVec(double scale)
	{
		setVec(dx * scale, dy * scale);
		
	}

}
