package ru.cpb9.geotarget.exchange;

/**
 * @author Artem Shein
 */
public class ExchangeError extends RuntimeException
{
    public ExchangeError(Throwable cause)
    {
        super(cause);
    }
}
