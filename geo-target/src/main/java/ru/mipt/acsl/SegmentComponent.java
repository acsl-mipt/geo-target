package ru.mipt.acsl;
import ifdev.Ber;

public class SegmentComponent
{
	public static class SegmentationAckModeMessage
	{
		private boolean isSegmentationAckModeActive;
		private Ber maxSegmentNumber;
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