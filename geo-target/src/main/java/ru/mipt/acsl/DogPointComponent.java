package ru.mipt.acsl;

public class DogPointComponent
{
	public static class AllMessage
	{
		private float latitude;
		private float longitude;
		private float altitude;
		public AllMessage(float latitude, float longitude, float altitude)
		{
			this.latitude = latitude;
			this.longitude = longitude;
			this.altitude = altitude;
		}
		public float getLatitude()
		{
			return latitude;
		}
		public float getLongitude()
		{
			return longitude;
		}
		public float getAltitude()
		{
			return altitude;
		}

	}

}