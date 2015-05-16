package api;

import api.exception.InvalidVariableException;

/**
 * 
 */

/**
 * @author Ethan
 *
 */
public interface Variable extends Expression {
	public String getName();
	
	public void setName(String s);

	Variable parse(String s) throws InvalidVariableException;
}
