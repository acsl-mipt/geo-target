package ru.mipt.acsl;

class RoutesInfo
{
	private int nextPoint;
	private Routes routes;
	private Route activeRoute;
	public RoutesInfo(int nextPoint, Routes routes, Route activeRoute)
	{
		this.nextPoint = nextPoint;
		this.routes = routes;
		this.activeRoute = activeRoute;
	}
	public int getNextPoint()
	{
		return nextPoint;
	}
	public Routes getRoutes()
	{
		return routes;
	}
	public Route getActiveRoute()
	{
		return activeRoute;
	}

}