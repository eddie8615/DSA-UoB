package dsa_assignment5;

/**
 * Rather than always throwing {@code Exception}, as we have done in previous exercises,
 * in this assignment we will throw our own specialised Exception. It is very easy to create,
 * just make a new class that extends Exception and let Eclipse generate all the constructors
 *
 */
public class GraphException extends Exception
{
	private static final long serialVersionUID = 1L;

	// All these constructors were generated automatically from Eclipse using:
	// Source|Generate Constructors from Superclass...
	public GraphException()
	{
		super();
	}

	public GraphException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GraphException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public GraphException(String message)
	{
		super(message);
	}

	public GraphException(Throwable cause)
	{
		super(cause);
	}

}
