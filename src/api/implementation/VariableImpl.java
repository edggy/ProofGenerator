package api.implementation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import api.*;
import api.exception.*;

/**
 * @author Ethan
 *
 */
public class VariableImpl extends AbstractVariable {

	private static final long serialVersionUID = 8952381735986750705L;

	/**
	 * 
	 */
	public VariableImpl() {
	}

	/**
	 * @param s
	 */
	public VariableImpl(String s) {
		super(s);
	}

	/**
	 * @param oplst
	 * @param s
	 * @throws InvalidExpressionException
	 */
	public VariableImpl(Set<Operator> oplst, String s) throws InvalidExpressionException {
		super(oplst, s);
	}

	@Override
	public Variable parse(Set<Operator> o, String s, Notation n) throws InvalidVariableException {
		for(Operator op : o) {
			if(op.containsRepresentation(s)) throw new InvalidVariableException();
		}
		
		return parse(s);
	}
	
	@Override
	public Variable parse(String s) throws InvalidVariableException {
		if(!s.matches("^\\(*.+\\)*$")) throw new InvalidVariableException();
		if(s.matches("^\\(+.+\\)+$")) return parse(s.substring(1, s.length() - 1));
		if(s.matches("^[^()]+$")) return new VariableImpl(s);
		throw new InvalidVariableException();
	}
	
	@Override
	public Variable clone() {
		return new VariableImpl(name);
	}

	@Override
	public Variable deepClone() {
		return new VariableImpl(name);
	}
	
	@Override
	public Map<Expression, Expression> findSubstitution(Expression a) throws InvalidExpressionException {
		if(!a.isValid()) throw new InvalidExpressionException();
		Map<Expression, Expression> map = new HashMap<Expression, Expression>();
		//map.put(this.deepClone(), new ExpressionImpl(a));
		map.put(this, a);
		return map;
	}
}
