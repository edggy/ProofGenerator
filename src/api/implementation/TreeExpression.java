package api.implementation;

import java.util.List;
import java.util.Set;

import util.Triple;
import api.*;
import api.exception.*;

/**
 * @author Ethan
 *
 */
public class TreeExpression extends AbstractTreeExpression {

	private static final long serialVersionUID = -5480057303339490080L;

	protected TreeExpression() {
		super();
	}
	
	/**
	 * @param o
	 * @param a
	 * @param b
	 * @throws InvalidExpressionException
	 */
	public TreeExpression(Operator o, Expression a, Expression b) throws InvalidExpressionException {
		super(o, a, b);
	}

	/**
	 * @param a
	 * @throws InvalidExpressionException
	 */
	public TreeExpression(Expression a) throws InvalidExpressionException {
		super(a);
	}

	/**
	 * @param oplst
	 * @param s
	 * @throws InvalidExpressionException
	 */
	public TreeExpression(Set<Operator> oplst, String s, Notation n) throws InvalidExpressionException {
		super(oplst, s, n);
	}

	@Override
	public Expression parse(Set<Operator> oplist, String s, Notation n) throws InvalidExpressionException {
		Triple<Operator, List<Object>, List<Object>> t = AbstractExpression.parseToTriple(oplist, s, n);
		return new TreeExpression(t.getFirst(), parse(oplist, t.getSecond(), n), parse(oplist, t.getThird(), n));
	}

	
	
	private Expression parse(Set<Operator> oplist, List<Object> tok, Notation n) throws InvalidExpressionException {
		Triple<Operator, List<Object>, List<Object>> t = AbstractExpression.parseToTriple(oplist, tok, n);
		if(t.getFirst() == null) return new VariableImpl((String) t.getSecond().get(0));
		return new TreeExpression(t.getFirst(), parse(oplist, t.getSecond(), n), parse(oplist, t.getThird(), n));
	}

	@Override
	public Expression clone() {
		TreeExpression copy = new TreeExpression();
		copy.oper = oper;
		copy.left = left;
		copy.right = right;
		return copy;
	}

	@Override
	public Expression deepClone() {
		TreeExpression copy = new TreeExpression();
		copy.oper = (Operator) oper.deepClone();
		copy.left = (Expression) left.deepClone();
		copy.right = (Expression) right.deepClone();
		return copy;
	}
	
	

	
	
}
