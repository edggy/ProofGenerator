package api.implementation;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import api.*;
import api.exception.*;

/**
 * @author Ethan
 *
 */
public abstract class AbstractPrefixExpression extends AbstractExpression {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2270781011291799856L;
	protected String left;
	protected String right;
	protected String oper;
	protected Set<Operator> opSet;

	protected AbstractPrefixExpression() {
		opSet = new HashSet<Operator>();
	}

	public AbstractPrefixExpression(Operator o, Expression a, Expression b) throws InvalidExpressionException {
		if(a == null || b == null || o == null || !a.isValid() || !b.isValid()) throw new InvalidExpressionException();
		oper = o.toString();
		left = a.prefix();
		right = b.prefix();
		opSet = new HashSet<Operator>();
		opSet.add(o);
		Stack<Expression> s = new Stack<Expression>();
		s.push(a);
		s.push(b);
		while(!s.isEmpty()) {
			Expression cur = s.pop();
			if(cur == null || cur instanceof Variable) continue;
			opSet.add(cur.getOperator());
			s.add(cur.getLeft());
			s.add(cur.getRight());
		}
		if(!this.isValid()) throw new InvalidExpressionException();
	}

	public AbstractPrefixExpression(Expression a) throws InvalidExpressionException {
		if(a == null || !a.isValid()) throw new InvalidExpressionException();
		this.setTo(a);
		if(!this.isValid()) throw new InvalidExpressionException();
	}

	public AbstractPrefixExpression(Set<Operator> oplist, String s, Notation n) throws InvalidExpressionException {
		if(s == null) throw new InvalidExpressionException();
		Expression a = parse(oplist, s, n);
		if(a == null || !a.isValid()) throw new InvalidExpressionException();
		
		this.setTo(a);
		this.opSet = oplist;
		if(!this.isValid()) throw new InvalidExpressionException();
	}
	
	public abstract Expression clone();
	
	public abstract Expression deepClone();
	
	/* (non-Javadoc)
	 * @see Expression#getOperator()
	 */
	@Override
	public Operator getOperator() {
		for(Operator o : opSet) {
			if(o.isRepresentation(oper)) return o;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see Expression#getLeft()
	 */
	@Override
	public Expression getLeft() {
		try {
			return this.parse(opSet, left, Notation.Prefix);
		} catch (InvalidExpressionException|java.lang.StringIndexOutOfBoundsException e) {
		}
		return new VariableImpl(left);
	}

	/* (non-Javadoc)
	 * @see Expression#getRight()
	 */
	@Override
	public Expression getRight() {
		try {
			return this.parse(opSet, right, Notation.Prefix);
		} catch (InvalidExpressionException|java.lang.StringIndexOutOfBoundsException e) {
		}
		return new VariableImpl(right);
	}

	/* (non-Javadoc)
	 * @see Expression#setLeft(Expression)
	 */
	@Override
	public Expression setLeft(Expression e) throws InvalidExpressionException {
		if(e != null && !e.isValid()) throw new InvalidExpressionException();
		left = e.prefix();
		return this;
	}

	/* (non-Javadoc)
	 * @see Expression#setRight(Expression)
	 */
	@Override
	public Expression setRight(Expression e) throws InvalidExpressionException {
		if(e != null && !e.isValid()) throw new InvalidExpressionException();
		right = e.prefix();
		return this;
	}

	/* (non-Javadoc)
	 * @see Expression#setOperator(Operator)
	 */
	@Override
	public Expression setOperator(Operator o) {
		oper = o.toString();
		opSet.add(o);
		return this;
	}

	protected Expression setTo(Expression e) throws InvalidExpressionException {
		if(e == null || !e.isValid()) throw new InvalidExpressionException();
		this.oper = e.getOperator().toString();
		this.left = e.getLeft().prefix();
		this.right = e.getRight().prefix();
		opSet = new HashSet<Operator>();
		Stack<Expression> s = new Stack<Expression>();
		s.push(e);
		while(!s.isEmpty()) {
			Expression cur = s.pop();
			if(cur == null || cur instanceof Variable) continue;
			opSet.add(cur.getOperator());
			s.add(cur.getLeft());
			s.add(cur.getRight());
		}
		if(!this.isValid()) throw new InvalidExpressionException();
		return this;
	}
	
	@Override
	public String prefix() {
		return "" + oper + left + right;
	}
}
