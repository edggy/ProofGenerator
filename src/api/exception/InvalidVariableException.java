/**
 * 
 */
package api.exception;

/**
 * @author Ethan
 *
 */
public class InvalidVariableException extends InvalidExpressionException {

	private static final long serialVersionUID = 8576155827833415153L;

	public InvalidVariableException() {
		super();
	}

	public InvalidVariableException(String arg0) {
		super(arg0);
	}
}
