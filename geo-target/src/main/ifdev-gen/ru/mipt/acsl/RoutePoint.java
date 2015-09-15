package ru.mipt.acsl;

class RoutePoint
{
	private float latitude;
	private float longitude;
	private float altitude;
	private float speed;
	private long flags;
	public RoutePoint(float latitude, float longitude, float altitude, float speed, long flags)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.speed = speed;
		this.flags = flags;
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
	public float getSpeed()
	{
		return speed;
	}
	public long getFlags()
	{
		return flags;
	}

}