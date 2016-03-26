package ru.mipt.acsl;

public class DogPointComponent
{
	public static final String FQN = "ru.mipt.acsl.mcc.DogPoint";
	public static class AllMessage
	{
		private double latitude;
		private double longitude;
		private double altitude;
		public static final String FQN = DogPointComponent.FQN + ".all";
		public AllMessage(double latitude, double longitude, double altitude)
		{
			this.latitude = latitude;
			this.longitude = longitude;
			this.altitude = altitude;
		}
		public double getLatitude()
		{
			return latitude;
		}
		public double getLongitude()
		{
			return longitude;
		}
		public double getAltitude()
		{
			return altitude;
		}

	}

}