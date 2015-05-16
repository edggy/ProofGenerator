/**
 * 
 */
package util;

import java.io.Serializable;

/**
 * @author Ethan
 *
 */
public interface DeepCloneable extends Cloneable, Serializable {
	
	public Object deepClone();

}
