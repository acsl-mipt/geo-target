package ru.mipt.acsl;
import ifdev.Ber;

public class IdentificationComponent
{
	public static class ShortIdMessage
	{
		private Ber deviceGuid;
		private Ber rootComponentGuid;
		public ShortIdMessage(Ber deviceGuid, Ber rootComponentGuid)
		{
			this.deviceGuid = deviceGuid;
			this.rootComponentGuid = rootComponentGuid;
		}
		public Ber getDeviceGuid()
		{
			return deviceGuid;
		}
		public Ber getRootComponentGuid()
		{
			return rootComponentGuid;
		}

	}
	public static class FullIdMessage
	{
		private Ber deviceGuid;
		private Ber rootComponentGuid;
		private Array<ComponentNumberGuidPair> deviceComponentGuidPairs;
		public FullIdMessage(Ber deviceGuid, Ber rootComponentGuid, Array<ComponentNumberGuidPair> deviceComponentGuidPairs)
		{
			this.deviceGuid = deviceGuid;
			this.rootComponentGuid = rootComponentGuid;
			this.deviceComponentGuidPairs = deviceComponentGuidPairs;
		}
		public Ber getDeviceGuid()
		{
			return deviceGuid;
		}
		public Ber getRootComponentGuid()
		{
			return rootComponentGuid;
		}
		public Array<ComponentNumberGuidPair> getDeviceComponentGuidPairs()
		{
			return deviceComponentGuidPairs;
		}

	}
	public static class ComponentGuidMessage
	{
		private ComponentNumberGuidPair deviceComponentGuidPairs_1_;
		public ComponentGuidMessage(ComponentNumberGuidPair deviceComponentGuidPairs_1_)
		{
			this.deviceComponentGuidPairs_1_ = deviceComponentGuidPairs_1_;
		}
		public ComponentNumberGuidPair getDeviceComponentGuidPairs_1_()
		{
			return deviceComponentGuidPairs_1_;
		}

	}

}