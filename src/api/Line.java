/**
 * 
 */
package api;

import util.Pair;

/**
 * @author Ethan
 *
 */
public interface Line extends Expression, Comparable<Line> {
	public int getLine();
	public Inference getInference();
	public Pair<Line, Line> getPreviousLines();
	public Line clone();
	public Line deepClone();
}
