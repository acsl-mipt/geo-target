package ru.mipt.acsl;
import ifdev.Ber;

public class SegmentComponent
{
	public static final String FQN = "ru.mipt.acsl.Segment";
	public static class SegmentationAckModeMessage
	{
		private boolean isSegmentationAckModeActive;
		private Ber maxSegmentNumber;
		public static final String FQN = SegmentComponent.FQN + ".segmentationAckMode";
		public SegmentationAckModeMessage(boolean isSegmentationAckModeActive, Ber maxSegmentNumber)
		{
			this.isSegmentationAckModeActive = isSegmentationAckModeActive;
			this.maxSegmentNumber = maxSegmentNumber;
		}
		public boolean getIsSegmentationAckModeActive()
		{
			return isSegmentationAckModeActive;
		}
		public Ber getMaxSegmentNumber()
		{
			return maxSegmentNumber;
		}

	}
	public static class AckOnEverySegmentModeMessage
	{
		private boolean isAckOnEverySegmentModeActive;
		public static final String FQN = SegmentComponent.FQN + ".ackOnEverySegmentMode";
		public AckOnEverySegmentModeMessage(boolean isAckOnEverySegmentModeActive)
		{
			this.isAckOnEverySegmentModeActive = isAckOnEverySegmentModeActive;
		}
		public boolean getIsAckOnEverySegmentModeActive()
		{
			return isAckOnEverySegmentModeActive;
		}

	}

}