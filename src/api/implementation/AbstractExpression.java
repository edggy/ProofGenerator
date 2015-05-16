package api.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;

import util.Pair;
import util.Triple;

import api.*;
import api.exception.*;

/**
 * @author Ethan
 *
 */
public abstract class AbstractExpression implements Expression {


	private static final long serialVersionUID = 3989766754317076728L;
	
	public abstract Expression clone();
	
	public abstract Expression deepClone();
	
	public abstract Operator getOperator();

	public abstract Expression getLeft();

	public abstract Expression getRight();

	public abstract Expression setLeft(Expression e) throws InvalidExpressionException;

	public abstract Expression setRight(Expression e) throws InvalidExpressionException;

	public abstract Expression setOperator(Operator o);
	
	public abstract Expression parse(Set<Operator> opSet, String s, Notation n) throws InvalidExpressionException;

	/* (non-Javadoc)
	 * @see Expression#length()
	 */
	@Override
	public int length() {
		return getLeft().length() + getRight().length() + 1;
	}

	/* (non-Javadoc)
	 * @see Expression#evaluate()
	 */
	@Override
	public TruthValue evaluate() {
		try {
			List<Expression> eList = new ArrayList<Expression>();
			eList.add(getLeft());
			eList.add(getRight());
			return getOperator().operate(eList);
		} catch (InvalidExpressionException e) {
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see Expression#isValid()
	 */
	@Override
	public boolean isValid() {
		if(this.getLeft() == null || this.getRight() == null || this.getOperator() == null) return false;
		if(this.getLeft().isValid() && this.getRight().isValid()) return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see Expression#substitute(java.util.Map)
	 */
	@Override
	public Expression substitute(Map<Expression, Expression> m) throws InvalidExpressionException {
		//return this.clone().setLeft(this.getLeft().substitute(m)).setRight(this.getRight().substitute(m));

		HashSet<Expression> toRemove = new HashSet<Expression>();
		for(Expression e : m.keySet()) {
			if(m.get(e).equals(e)) toRemove.add(e);
		}
		
		for(Expression e : toRemove) {
			m.remove(e);
		}
		
		Expression tmp = this.deepClone();
		if(m.isEmpty()) return tmp;
		
		tmp.setLeft(tmp.getLeft().substitute(m));
		tmp.setRight(tmp.getRight().substitute(m));

		return tmp;
	}

	/* (non-Javadoc)
	 * @see Expression#canBeReplacedBy(Expression)
	 */
	@Override
	public boolean canBeReplacedBy(Expression a) throws InvalidExpressionException {
		if(a != null && !a.isValid()) throw new InvalidExpressionException();

		/*	Example:
		 * 	this = (A|(B|C)) can be replaced by 
		 * 	a = ((A|(B|C))|((D|(D|D))|((D|B)|((A|D)|(A|D)))))
		 *  A => (A|(B|C))
		 *  B => (D|(D|D))
		 *  C => ((D|B)|((A|D)|(A|D)))
		 *  D => E
		 *  We will get:
		 *  (((A|(B|C))|((D|(D|D))|((D|B)|((A|D)|(A|D)))))|((E|(E|E))|((E|(D|(D|D)))|(((A|(B|C))|E)|((A|(B|C))|E)))))
		 */
		Stack<Pair<Expression, Expression>> s = new Stack<Pair<Expression, Expression>>();
		s.add(new Pair<Expression, Expression>(this, a));
		while(!s.isEmpty()) {
			Pair<Expression, Expression> p = s.pop();
			Expression top = p.getLeft();
			Expression sub = p.getRight();
			if(top instanceof Variable) {
				if(sub instanceof Variable) {
					if(!((Variable) top).getName().equals(((Variable) sub).getName())) return false;
				}
				continue;
			}
			if(sub instanceof Variable) return false;
			if(!top.getOperator().equals(sub.getOperator())) return false;
			s.add(new Pair<Expression, Expression>(top.getRight(), sub.getRight()));
			s.add(new Pair<Expression, Expression>(top.getLeft(), sub.getLeft()));
		}
		return true;
		//An Expression can't be replaced by a variable
		//if(a instanceof Variable) return false;

		//Operator must be the same
		//if(!this.getOperator().equals(a.getOperator())) return false;
		
		//If left can be replaced by a's left and same for the right then this can be replaced
		//if(this.getLeft().canBeReplacedBy(a.getLeft()) && this.getRight().canBeReplacedBy(a.getRight())) return true;

		//Otherwise it can't
		//return false;
	}

	/* (non-Javadoc)
	 * @see Expression#existsIn(Expression)
	 */
	@Override
	public boolean existsIn(Expression e) throws InvalidExpressionException {
		if(!e.isValid()) throw new InvalidExpressionException();

		//If e equals this then it is a subtree
		if(this.equals(e)) return true;

		//If e exists in the left or right Expression then it is in this Expression
		if(this.getLeft().existsIn(e)) return true;
		if(this.getRight().existsIn(e)) return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see Expression#getVariables()
	 */
	@Override
	public Set<Variable> getVariables() {
		Set<Variable> set = this.getLeft().getVariables();
		set.addAll(this.getRight().getVariables());
		return set;
	}
	
	@Override
	public Set<Operator> getOpSet() {
		Set<Operator> set = this.getLeft().getOpSet();
		set.addAll(this.getRight().getOpSet());
		set.add(this.getOperator());
		return set;
	}

	@Override
	public boolean equals(Object o) {
		//If o is not an Expression it can't be equal
		if(!(o instanceof Expression)) return false;
		Expression e = ((Expression) o);

		//If e is not valid it can't be equal
		if(!e.isValid()) return false;

		try {
			if(this.canBeReplacedBy(e) && e.canBeReplacedBy(this)) return true;
		} catch (InvalidExpressionException e1) {
		}
		
		return false;
		//Check if left is the same
		//if(!getLeft().equals(e.getLeft())) return false;

		//Check if operator is the same
		//if(!getOperator().equals(e.getOperator())) return false;

		//Check if right is the same
		//if(!getRight().equals(e.getRight())) return false;

		//return true;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		return prefix();
	}
	
	@Override
	public String toString(Notation n) {
		if(Notation.Prefix.equals(n)) {
			return prefix();
		}
		else if(Notation.Infix.equals(n)) {
			return prefix();
		}
		else if(Notation.Postfix.equals(n)) {
			return prefix();
		}
		return null;
	}
	
	@Override
	public String prefix() {
		return "" + this.getOperator() + this.getLeft().prefix() + this.getRight().prefix();
	}
	
	@Override
	public String infix() {
		return "(" + this.getLeft().infix() + this.getOperator() + this.getRight().infix() + ")";
	}
	
	@Override
	public String postfix() {
		return "" + this.getLeft().postfix() + this.getRight().postfix() + this.getOperator();
	}
	
	public boolean isTautology() {
		List<Map<Expression, Expression>> listOfMaps = generatePossibleValues();
		for(Map<Expression, Expression> m : listOfMaps) {
			try {
				if(this.substitute(m).evaluate().isFalse()) {
					return false;
				}
			} catch (InvalidExpressionException e) {
			}
		}
		return true;
	}
	
	public boolean isContradiction() {
		List<Map<Expression, Expression>> listOfMaps = generatePossibleValues();
		for(Map<Expression, Expression> m : listOfMaps) {
			try {
				if(this.substitute(m).evaluate().isTrue()) return false;
			} catch (InvalidExpressionException e) {
			}
		}
		return true;
	}
	
	private List<Map<Expression, Expression>> generatePossibleValues() {
		return generatePossibleValues(getVariables());
	}
	
	private List<Map<Expression, Expression>> generatePossibleValues(Set<Variable> variables) {
		if(variables.size() == 0) return new ArrayList<Map<Expression, Expression>>();
		Expression curExpression = variables.iterator().next();
		variables.remove(curExpression);
		List<Map<Expression, Expression>> listOfMaps = generatePossibleValues(variables);
		if(listOfMaps.size() == 0) listOfMaps.add(new HashMap<Expression, Expression>());
		for(Map<Expression, Expression> m : listOfMaps) {
			m.put(curExpression, new LiteralImpl(TruthValue.True));
			m.put(curExpression, new LiteralImpl(TruthValue.False));
		}
		return listOfMaps;
	}
	
	@Override
	public Expression normalize() {
		return normalize(1);
		/*
		Map<Expression, Expression> map = new HashMap<Expression, Expression>();
		int count = 1;
		Stack<Expression> s = new Stack<Expression>();
		Expression norm = this.deepClone();
		
		s.push(norm);
		while(!s.isEmpty()) {
			
			Expression e = s.pop();
			if(e instanceof Variable) {
				Expression tmp = null;
				if(count <= 26) tmp = new VariableImpl(""+(char)(count + 'A' - 1));
				else tmp = new VariableImpl("X" + (count - 26));
				if(map.putIfAbsent(e, tmp) == null) {
					count++;
				}
			}
			else {
				s.push(e.getRight());
				s.push(e.getLeft());
			}
		}

		try {
			this.setTo(this.substitute(map));
		} catch (InvalidExpressionException e1) {
		}
		return norm;*/
	}
	
	/* (non-Javadoc)
	 * @see Expression#normalize(int)
	 */
	public Expression normalize(int start) {
		Map<Expression, Expression> map = new HashMap<Expression, Expression>();
		int count = 0;
		Stack<Expression> s = new Stack<Expression>();
		Expression norm = this.deepClone();
		
		s.push(norm);
		//s.push(this);
		while(!s.isEmpty()) {
			Expression e = s.pop();
			if(e instanceof Variable) {
				VariableImpl tmp = new VariableImpl();
				tmp.setName("X"+(count + start));
				if(map.putIfAbsent(e, tmp) == null) {
					count++;
				}
			}
			else {
				s.push(e.getRight());
				s.push(e.getLeft());
			}
		}

		try {
			this.setTo(this.substitute(map));
		} catch (InvalidExpressionException e1) {
		}
		return norm;
	}
	
	/* (non-Javadoc)
	 * @see api.AbstractExpression#findSubstitution(api.Expression)
	 */
	@Override
	public Map<Expression, Expression> findSubstitution(Expression a) throws InvalidExpressionException {
		if(a == null || !a.isValid()) throw new InvalidExpressionException();
		
		Map<Expression, Expression> leftMap = new HashMap<Expression, Expression>();
		Map<Expression, Expression> rightMap = new HashMap<Expression, Expression>();
		
		if((a instanceof Variable) || !this.canBeReplacedBy(a)) return leftMap;

		if(this.getLeft() instanceof Variable) {
			leftMap.put(getLeft(), a.getLeft());
		}
		else {
			leftMap.putAll(getLeft().findSubstitution(a.getLeft()));
		}

		if(this.getRight() instanceof Variable) {
			rightMap.put(getRight(), a.getRight());
		}
		else {
			rightMap.putAll(getRight().findSubstitution(a.getRight()));
		}
		Map<Expression, Expression> map = mapMerge(leftMap, rightMap);
		return map;
	}
	
	private Map<Expression, Expression> mapMerge(Map<Expression, Expression> left, Map<Expression, Expression> right) throws InvalidExpressionException {
		Map<Expression, Expression> map = new HashMap<Expression, Expression>();
		for(Entry<Expression, Expression> leftset  : left.entrySet()) {
			Expression var = leftset.getKey();
			if(right.containsKey(var)) {
				Expression leftSub = leftset.getValue();
				Expression rightSub = right.get(var);
				if(leftSub.canBeReplacedBy(rightSub)) {
					map.put(var, rightSub);
				}
				else if(rightSub.canBeReplacedBy(leftSub)) {
					map.put(var, leftSub);
				}
			}
			else {
				map.put(var, leftset.getValue());
			}
		}
		for(Entry<Expression, Expression> rightset  : right.entrySet()) {
			map.putIfAbsent(rightset.getKey(), rightset.getValue());
		}
		return map;
	}

	protected Expression setTo(Expression e) throws InvalidExpressionException {
		this.setOperator(e.getOperator());
		this.setLeft(e.getLeft());
		this.setRight(e.getRight());
		return this;
	}
	
	public static List<Object> tokenize(Set<Operator> o, String s) {
		List<Object> ret = new ArrayList<Object>();
		String symbol = "";
		for(int i = 0; i < s.length(); i++) {
			symbol += s.charAt(i);
			
			if(symbol.endsWith("(")){
				String tmp = symbol.substring(0, symbol.length() - 1);
				if(tmp.length() > 0) ret.add(tmp);
				ret.add("(");
				symbol = "";
			}
			else if(symbol.endsWith(")")) {
				String tmp = symbol.substring(0, symbol.length() - 1);
				if(tmp.length() > 0) ret.add(tmp);
				ret.add(")");
				symbol = "";
			}
			else if(symbol.endsWith("X")) {
				String tmp = symbol.substring(0, symbol.length() - 1);
				if(tmp.length() > 0) ret.add(tmp);
				symbol = "X";
			}
			else {
				for(Operator op : o) {
					if(op.containsRepresentation(symbol)) {
						String tmp = symbol.substring(0, symbol.length() - 1);
						if(tmp.length() > 0) ret.add(tmp);
						ret.add(op);
						symbol = "";
					}
				}
			}
		}
		ret.add(symbol);
		return ret;
	}
	
	public static Triple<Operator, List<Object>, List<Object>> parseToTriple(Set<Operator> o, String s, Notation n) throws InvalidExpressionException {
		List<Object> tok = tokenize(o, s);
		return parseToTriple(o, tok, n);
	}
	
	public static Triple<Operator, List<Object>, List<Object>> parseToTriple(Set<Operator> o, List<Object> tok, Notation n) throws InvalidExpressionException {
		if(tok.size() == 1 && tok.get(0) instanceof String) {
			List<Object> ret = new ArrayList<Object>();
			ret.add(tok.get(0));
			return new Triple<Operator, List<Object>, List<Object>>(null, ret, null);
		}
		if(tok.size() < 3) {
			throw new InvalidExpressionException();
		}
		if(Notation.Prefix.equals(n)) {
			return parseToTriplePrefix(tok);
		}
		if(Notation.Infix.equals(n)) {
			return parseToTripleInfix(tok);
		}
		if(Notation.Postfix.equals(n)) {
			return parseToTriplePostfix(tok);
		}
		throw new InvalidExpressionException();
		
	}
	
	public static Triple<Operator, List<Object>, List<Object>> parseToTriplePrefix(List<Object> tok) throws InvalidExpressionException {
		if(!(tok.get(0) instanceof Operator)) throw new InvalidExpressionException();
		int depth = 1;
		int i;
		//Find begnning of right expression	||ABC vs 	|A|BC
		//									1210		10
		for(i = 1; depth > 0; i++) {
			if(tok.get(i) instanceof Operator) {
				depth++;
			}
			else if(tok.get(i) instanceof String) {
				depth--;
			}
		}
		
		return new Triple<Operator, List<Object>, List<Object>>((Operator) tok.get(0), tok.subList(1, i), tok.subList(i, tok.size()));
	}
	
	public static Triple<Operator, List<Object>, List<Object>> parseToTripleInfix(List<Object> tok) throws InvalidExpressionException{
		if(!tok.get(0).equals("(") || !tok.get(tok.size() - 1).equals(")")) throw new InvalidExpressionException();
		List<Object> lst = tok.subList(1, tok.size() - 1);
		int depth = 0;
		int i = 0;
		//Find main Operator	(...)|(...)
		for(i = 0; depth > 0; i++) {
			if(lst.get(i).equals("(")) {
				depth++;
			}
			else if(lst.get(i).equals(")")) {
				depth--;
			}
		}
		i++;
		return new Triple<Operator, List<Object>, List<Object>>((Operator) tok.get(i), tok.subList(0, i), tok.subList(i+1, tok.size()));
	}
	
	public static Triple<Operator, List<Object>, List<Object>> parseToTriplePostfix(List<Object> tok) throws InvalidExpressionException{
		if(!(tok.get(tok.size()) instanceof Operator)) throw new InvalidExpressionException();
		int depth = 1;
		int i;
		//Find begnning of right expression	ABC|| vs 	AB|C|
		//									?0121		???01
		for(i = tok.size() - 2; depth > 0; i--) {
			if(tok.get(i) instanceof Operator) {
				depth++;
			}
			else if(tok.get(i) instanceof String) {
				depth--;
			}
		}
		return new Triple<Operator, List<Object>, List<Object>>((Operator) tok.get(tok.size()), tok.subList(0, i), tok.subList(i, tok.size() - 1));
	}
	
	/*private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeUTF(this.prefix());
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		try {
			this.setTo(this.parse(this.getOpSet(), in.readUTF()));
		} catch (InvalidExpressionException e) {
			throw new IOException();
		}
	}*/

}
