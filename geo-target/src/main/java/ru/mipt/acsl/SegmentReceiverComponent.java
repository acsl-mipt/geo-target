package ru.mipt.acsl;
import ifdev.Array;
import ifdev.Ber;

public class SegmentReceiverComponent
{
	public static class SegmentsReceivedMessage
	{
		private Array<Ber> segmentsReceived;
		public SegmentsReceivedMessage(Array<Ber> segmentsReceived)
		{
			this.segmentsReceived = segmentsReceived;
		}
		public Array<Ber> getSegmentsReceived()
		{
			return segmentsReceived;
		}

	}

}