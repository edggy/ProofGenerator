package api.implementation;

import java.util.Set;
import api.*;
import api.exception.*;

/**
 * @author Ethan
 *
 */
public abstract class AbstractTreeExpression extends AbstractExpression {

	private static final long serialVersionUID = -3661292200351361464L;
	protected Expression left;
	protected Expression right;
	protected Operator oper;

	protected AbstractTreeExpression() {
		//index++;
		//name = "X"+index;
		//left = new Expression.Null();
		//right = new Expression.Null();
		//oper = new Operator.Null();
	}

	public AbstractTreeExpression(Operator o, Expression a, Expression b) throws InvalidExpressionException {
		if(a == null || b == null || o == null || !a.isValid() || !b.isValid()) throw new InvalidExpressionException();
		//left = a.deepClone();
		//right = b.deepClone();
		//oper = o.deepClone();
		left = a;
		right = b;
		oper = o;
	}

	public AbstractTreeExpression(Expression a) throws InvalidExpressionException {
		if(a == null || !a.isValid()) throw new InvalidExpressionException();

		//left = a.getLeft().deepClone();
		//right = a.getRight().deepClone();
		//oper = a.getOperator().deepClone();
		
		left = a.getLeft();
		right = a.getRight();
		oper = a.getOperator();
	}

	//public AbstractExpression(String s) {
	//	name = s;
	//	left = new Expression.Null();
	//	right = new Expression.Null();
	//	oper = new Operator.Null();
	//}

	public AbstractTreeExpression(Set<Operator> oplst, String s, Notation n) throws InvalidExpressionException {
		Expression a = parse(oplst, s, n);

		if(a.getLeft() == null || a.getRight() == null || a.getOperator() == null) throw new InvalidExpressionException();
		a.normalize();
		left = a.getLeft();
		right = a.getRight();
		oper = a.getOperator();
	}
	
	public abstract Expression clone();
	
	public abstract Expression deepClone();
	
	/* (non-Javadoc)
	 * @see Expression#getOperator()
	 */
	@Override
	public Operator getOperator() {
		return oper;
	}

	/* (non-Javadoc)
	 * @see Expression#getLeft()
	 */
	@Override
	public Expression getLeft() {
		return left;
	}

	/* (non-Javadoc)
	 * @see Expression#getRight()
	 */
	@Override
	public Expression getRight() {
		return right;
	}

	/* (non-Javadoc)
	 * @see Expression#setLeft(Expression)
	 */
	@Override
	public Expression setLeft(Expression e) throws InvalidExpressionException {
		if(e != null && !e.isValid()) throw new InvalidExpressionException();
		left = e;
		return this;
	}

	/* (non-Javadoc)
	 * @see Expression#setRight(Expression)
	 */
	@Override
	public Expression setRight(Expression e) throws InvalidExpressionException {
		if(e != null && !e.isValid()) throw new InvalidExpressionException();
		right = e;
		return this;
	}

	/* (non-Javadoc)
	 * @see Expression#setOperator(Operator)
	 */
	@Override
	public Expression setOperator(Operator o) {
		oper = o;
		return this;
	}

}
