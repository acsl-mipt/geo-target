package ru.mipt.acsl;
import ifdev.Ber;

class ComponentNumberGuidPair
{
	private Ber number;
	private Ber guid;
	public ComponentNumberGuidPair(Ber number, Ber guid)
	{
		this.number = number;
		this.guid = guid;
	}
	public Ber getNumber()
	{
		return number;
	}
	public Ber getGuid()
	{
		return guid;
	}

}