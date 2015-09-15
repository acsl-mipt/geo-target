package test;

class SubStruct
{
	private short first;
	private U8Enum second;
	private F32Arr arr;
	public SubStruct(short first, U8Enum second, F32Arr arr)
	{
		this.first = first;
		this.second = second;
		this.arr = arr;
	}
	public short getFirst()
	{
		return first;
	}
	public U8Enum getSecond()
	{
		return second;
	}
	public F32Arr getArr()
	{
		return arr;
	}

}