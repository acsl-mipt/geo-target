package ru.mipt.acsl;
import decode.Array;
import decode.Ber;

public class SegmentReceiverComponent
{
	public static final String FQN = "ru.mipt.acsl.SegmentReceiver";
	public static class SegmentsReceivedMessage
	{
		private Array<Ber> segmentsReceived;
		public static final String FQN = SegmentReceiverComponent.FQN + ".segmentsReceived";
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