package ru.mipt.acsl;

/**
 * @author Artem Shein
 */
public class Motion
{
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
        private byte throttle;

        public AllMessage(double latitude, double longitude, double altitude, double accuracy, double speed,
                          double pitch,
                          double heading, double roll, byte throttle)
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

        public void setLatitude(double latitude)
        {
            this.latitude = latitude;
        }

        public double getLongitude()
        {
            return longitude;
        }

        public void setLongitude(double longitude)
        {
            this.longitude = longitude;
        }

        public double getAltitude()
        {
            return altitude;
        }

        public void setAltitude(double altitude)
        {
            this.altitude = altitude;
        }

        public double getAccuracy()
        {
            return accuracy;
        }

        public void setAccuracy(double accuracy)
        {
            this.accuracy = accuracy;
        }

        public double getSpeed()
        {
            return speed;
        }

        public void setSpeed(double speed)
        {
            this.speed = speed;
        }

        public double getPitch()
        {
            return pitch;
        }

        public void setPitch(double pitch)
        {
            this.pitch = pitch;
        }

        public double getHeading()
        {
            return heading;
        }

        public void setHeading(double heading)
        {
            this.heading = heading;
        }

        public double getRoll()
        {
            return roll;
        }

        public void setRoll(double roll)
        {
            this.roll = roll;
        }

        public byte getThrottle()
        {
            return throttle;
        }

        public void setThrottle(byte throttle)
        {
            this.throttle = throttle;
        }
    }
}
