package api.implementation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import api.*;
import api.exception.*;

/**
 * @author Ethan
 *
 */
public abstract class AbstractVariable extends AbstractExpression implements Variable {

	private static final long serialVersionUID = -5244591713137857642L;
	protected static long index = 1;
	protected String name;
	//private Expression left;
	//private Expression right;
	//private Operator oper;
	
	/**
	 * 
	 */
	public AbstractVariable() {
		name = "X"+index++;
	}

	/**
	 * @param s
	 */
	public AbstractVariable(String s) {
		name = s;
	}

	/**
	 * @param oplst
	 * @param s
	 * @throws InvalidExpressionException
	 */
	public AbstractVariable(Set<Operator> oplst, String s) throws InvalidExpressionException {
		name = this.parse(oplst, s, null).getName();
	}
	
	@Override
	public abstract Variable parse(Set<Operator> o, String s, Notation n) throws InvalidVariableException;
	
	@Override
	public abstract Variable clone();
	
	@Override
	public abstract Variable deepClone();
	
	@Override
	public Operator getOperator() { return null; }

	@Override
	public Expression getLeft() { return null; }

	@Override
	public Expression getRight() { return null; }

	public Expression setLeft(Expression e) { return this; }

	public Expression setRight(Expression e) { return this; }
	
	public Expression setOperator(Operator o) { return this; }

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String s) {
		name = s;

	}
	
	@Override
	public Expression substitute(Map<Expression, Expression> map) throws InvalidExpressionException {
		if(map.containsKey(this)) return map.get(this);
		return this.deepClone();
	}
	
	@Override
	public boolean canBeReplacedBy(Expression a) throws InvalidExpressionException {
		//A variable can be replaced by anything
		return true;
	}
	
	@Override
	public Set<Variable> getVariables() {
		Set<Variable> set = new HashSet<Variable>();
		//set.add(this.deepClone());
		set.add(this);
		return set;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!super.equals(o)) return false;
		if(!(o instanceof Variable)) return false;
		if(!name.equals(((Variable)o).getName())) return false;
		return true;
	}
	
	@Override
	public boolean isValid() {
		if(this.getLeft() != null || this.getRight() != null || this.getOperator() != null || name == null || name == "") return false;
		return true;
	}
	
	@Override
	public Variable normalize() {
		Variable norm = this.deepClone();
		norm.setName("A");
		//name = "A";
		return norm;
	}
	
	@Override
	public Expression normalize(int start) {
		Variable norm = this.deepClone();
		norm.setName("X" + start);
		return norm;
		//name = "X" + start;
		//return this;
	}
	
	@Override
	public int length() {
		return 1;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public String prefix() {
		return name;
	}
	
	@Override
	public String infix() {
		return name;
	}
	
	@Override
	public String postfix() {
		return name;
	}
	
	@Override
	public TruthValue evaluate() {
		//try {
		//	return getOperator().operate(this.getLeft(), this.getRight());
		//} catch (InvalidExpressionException e) {
		//}
		return TruthValue.Unknown;
	}
	
	public boolean isTautology() {
		return false;
	}
	
	public boolean isContradiction() {
		return false;
	}
	
	@Override
	public boolean existsIn(Expression e) throws InvalidExpressionException {
		return equals(e);
	}

}
