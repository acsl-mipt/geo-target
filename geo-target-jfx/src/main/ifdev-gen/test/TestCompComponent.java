package test;

public class TestCompComponent
{
	public static final String FQN = "test.TestComp";
	public static class MsgZeroMessage
	{
		private short u8;
		public static final String FQN = TestCompComponent.FQN + ".msgZero";
		public MsgZeroMessage(short u8)
		{
			this.u8 = u8;
		}
		public short getU8()
		{
			return u8;
		}

	}
	public static class MsgOneMessage
	{
		private short u8;
		private float sub_structs_1__arr_1_;
		public static final String FQN = TestCompComponent.FQN + ".msgOne";
		public MsgOneMessage(short u8, float sub_structs_1__arr_1_)
		{
			this.u8 = u8;
			this.sub_structs_1__arr_1_ = sub_structs_1__arr_1_;
		}
		public short getU8()
		{
			return u8;
		}
		public float getSub_structs_1__arr_1_()
		{
			return sub_structs_1__arr_1_;
		}

	}

}