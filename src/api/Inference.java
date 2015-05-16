package api;

import api.exception.*;
import util.*;

/**
 * 
 */

/**
 * @author Ethan
 *
 */
public interface Inference extends Comparable<Inference>, DeepCloneable {
	public Expression infer(Expression line1, Expression line2) throws InvalidExpressionException;
	public Expression backInfer1(Expression line2, Expression line3) throws InvalidExpressionException;
	public Expression backInfer2(Expression line1, Expression line3) throws InvalidExpressionException;
	public int getPriority();
	public String moreInfo();
}
