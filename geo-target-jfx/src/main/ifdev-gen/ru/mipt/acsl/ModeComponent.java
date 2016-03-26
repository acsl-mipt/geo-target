package ru.mipt.acsl;

public class ModeComponent
{
	public static final String FQN = "ru.mipt.acsl.mcc.Mode";
	public static class AllMessage
	{
		private short name;
		public static final String FQN = ModeComponent.FQN + ".all";
		public AllMessage(short name)
		{
			this.name = name;
		}
		public short getName()
		{
			return name;
		}

	}

}