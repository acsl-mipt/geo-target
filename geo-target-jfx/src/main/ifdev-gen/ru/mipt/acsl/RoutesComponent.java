package ru.mipt.acsl;

public class RoutesComponent
{
	public static final String FQN = "ru.mipt.acsl.Routes";
	public static class AllMessage
	{
		private int nextPoint;
		private Routes routes;
		private Route activeRoute;
		public static final String FQN = RoutesComponent.FQN + ".all";
		public AllMessage(int nextPoint, Routes routes, Route activeRoute)
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

}