package ru.mipt.acsl.geotarget.exchange;

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
