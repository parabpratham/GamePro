package Golf;

public class Circle
{

	public float	x, y;
	protected double	radius;
	protected int	diam;

	Circle(int x, int y, int diam)
	{
		this.x = x;
		this.y = y;
		radius = (double) (this.diam = diam) / 2;
	}

	protected double dist(Circle loc)
	{
		double xSq = loc.x - x;
		double ySq = loc.y - y;
		return (double) Math.sqrt((xSq * xSq) + (ySq * ySq));
	}
	
}
