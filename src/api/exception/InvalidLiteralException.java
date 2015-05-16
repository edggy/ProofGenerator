/**
 * 
 */
package api.exception;


/**
 * @author Ethan
 *
 */
public class InvalidLiteralException extends InvalidVariableException {
	private static final long serialVersionUID = -9106029748490859004L;

	public InvalidLiteralException() {
		super();
	}
	
	public InvalidLiteralException(String s) {
		super(s);
	}
}
