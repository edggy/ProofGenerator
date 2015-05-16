package api;

/**
 * @author Ethan
 *
 */
public interface Predicate extends Function, Expression {
	//TODO
	public TruthValue result(Object a, Object b);
}
