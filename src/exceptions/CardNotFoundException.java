package exceptions;

/**
 * Exception thrown when a card is not found
 * 
 * @author group17
 */
public class CardNotFoundException extends Exception {
	private static final long	serialVersionUID	= 6883143284907049675L;

	public CardNotFoundException(String msg) {
		super(msg);
	}

	public CardNotFoundException(String msg, Exception cause) {
		super(msg, cause);
	}
}
