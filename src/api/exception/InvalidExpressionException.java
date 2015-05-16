package api.exception;

public class InvalidExpressionException extends Exception {

	private static final long serialVersionUID = -7478784434017529501L;

	public InvalidExpressionException() {
		super();
	}
	
	public InvalidExpressionException(String s) {
		super(s);
	}
}
