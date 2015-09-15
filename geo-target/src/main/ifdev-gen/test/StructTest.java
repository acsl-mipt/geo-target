package test;

class StructTest
{
	private short u8;
	private float f32;
	private U8DynArr dyn_arr;
	private F32Arr arr;
	private long int_64;
	private Array2_3<SubStruct> sub_structs;
	public StructTest(short u8, float f32, U8DynArr dyn_arr, F32Arr arr, long int_64, Array2_3<SubStruct> sub_structs)
	{
		this.u8 = u8;
		this.f32 = f32;
		this.dyn_arr = dyn_arr;
		this.arr = arr;
		this.int_64 = int_64;
		this.sub_structs = sub_structs;
	}
	public short getU8()
	{
		return u8;
	}
	public float getF32()
	{
		return f32;
	}
	public U8DynArr getDyn_arr()
	{
		return dyn_arr;
	}
	public F32Arr getArr()
	{
		return arr;
	}
	public long getInt_64()
	{
		return int_64;
	}
	public Array2_3<SubStruct> getSub_structs()
	{
		return sub_structs;
	}

}