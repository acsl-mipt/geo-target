package ru.mipt.acsl;

class Route
{
	private int name;
	private boolean isRing;
	private RoutePoints points;
	public Route(int name, boolean isRing, RoutePoints points)
	{
		this.name = name;
		this.isRing = isRing;
		this.points = points;
	}
	public int getName()
	{
		return name;
	}
	public boolean getIsRing()
	{
		return isRing;
	}
	public RoutePoints getPoints()
	{
		return points;
	}

}