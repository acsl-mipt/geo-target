package ru.mipt.acsl;

public class DeviceComponent
{
	public static final String FQN = "ru.mipt.acsl.mcc.Device";
	public static class AllMessage
	{
		private short batteryLevel;
		private short signalLevel;
		private int crc;
		private short kind;
		public static final String FQN = DeviceComponent.FQN + ".all";
		public AllMessage(short batteryLevel, short signalLevel, int crc, short kind)
		{
			this.batteryLevel = batteryLevel;
			this.signalLevel = signalLevel;
			this.crc = crc;
			this.kind = kind;
		}
		public short getBatteryLevel()
		{
			return batteryLevel;
		}
		public short getSignalLevel()
		{
			return signalLevel;
		}
		public int getCrc()
		{
			return crc;
		}
		public short getKind()
		{
			return kind;
		}

	}

}