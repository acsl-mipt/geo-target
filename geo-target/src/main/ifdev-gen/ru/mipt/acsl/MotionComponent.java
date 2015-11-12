package ru.mipt.acsl;

public class MotionComponent
{
	public static final String FQN = "ru.mipt.acsl.mcc.Motion";
	public static class AllMessage
	{
		private double latitude;
		private double longitude;
		private double altitude;
		private double accuracy;
		private double speed;
		private double pitch;
		private double heading;
		private double roll;
		private short throttle;
		public static final String FQN = MotionComponent.FQN + ".all";
		public AllMessage(double latitude, double longitude, double altitude, double accuracy, double speed, double pitch, double heading, double roll, short throttle)
		{
			this.latitude = latitude;
			this.longitude = longitude;
			this.altitude = altitude;
			this.accuracy = accuracy;
			this.speed = speed;
			this.pitch = pitch;
			this.heading = heading;
			this.roll = roll;
			this.throttle = throttle;
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
		public double getAccuracy()
		{
			return accuracy;
		}
		public double getSpeed()
		{
			return speed;
		}
		public double getPitch()
		{
			return pitch;
		}
		public double getHeading()
		{
			return heading;
		}
		public double getRoll()
		{
			return roll;
		}
		public short getThrottle()
		{
			return throttle;
		}

	}

}