/**
 * 
 */
package api.implementation;

import java.util.Set;

import api.Expression;
import api.Notation;
import api.Operator;
import api.exception.InvalidExpressionException;

/**
 * @author Ethan
 *
 */
public class PrefixExpression extends AbstractPrefixExpression {

	private static final long serialVersionUID = 1377157948405660594L;

	/**
	 * 
	 */
	public PrefixExpression() {
		super();
	}

	/**
	 * @param o
	 * @param a
	 * @param b
	 * @throws InvalidExpressionException
	 */
	public PrefixExpression(Operator o, Expression a, Expression b) throws InvalidExpressionException {
		super(o, a, b);
	}

	/**
	 * @param a
	 * @throws InvalidExpressionException
	 */
	public PrefixExpression(Expression a) throws InvalidExpressionException {
		super(a);
	}

	/**
	 * @param oplist
	 * @param s
	 * @throws InvalidExpressionException
	 */
	public PrefixExpression(Set<Operator> oplist, String s, Notation n) throws InvalidExpressionException {
		super(oplist, s, n);
	}

	/* (non-Javadoc)
	 * @see api.implementation.AbstractPrefixExpression#clone()
	 */
	@Override
	public Expression clone() {
		PrefixExpression copy = new PrefixExpression();
		copy.oper = oper;
		copy.left = left;
		copy.right = right;
		copy.opSet = this.opSet;
		return copy;
	}

	/* (non-Javadoc)
	 * @see api.implementation.AbstractPrefixExpression#deepClone()
	 */
	@Override
	public Expression deepClone() {
		PrefixExpression copy = new PrefixExpression();
		copy.oper = oper;
		copy.left = left;
		copy.right = right;
		copy.opSet.addAll(opSet);
		return copy;
	}
	
	@Override
	public PrefixExpression parse(Set<Operator> oplist, String s, Notation n) throws InvalidExpressionException {
		if(Notation.Prefix.equals(n)) {
			return parseInfix(oplist, s);
		}
		else if(Notation.Infix.equals(n)) {
			//TODO
		}
		else if(Notation.Postfix.equals(n)) {
			//TODO
		}
		return null;
	}
	
	public PrefixExpression parseInfix(Set<Operator> oplist, String s) throws InvalidExpressionException {
		if(s == null || oplist == null) throw new InvalidExpressionException();
		Operator operator = null;
		String parser = "";
		for(int i = 0; i < s.length(); i++) {
			parser += s.charAt(i);
			for(Operator o : oplist) {
				if(o.containsRepresentation(parser)) {
					if(operator == null) operator = o;
					parser = "";
					break;
				}
			}
		}
		
		int depth = 1;
		parser = "";
		int i;
		for(i = 1; i < s.length() && depth > 0; i++) {
			parser += s.charAt(i);
			
			for(Operator o : oplist) {
				if(o.containsRepresentation(parser)) {
					depth += o.arity() - 1;
					parser = "";
					break;
				}
			}
			if(parser.matches(".*X.*")) {
				depth--;
				parser = "";
			}
		}
		
		i++;
		while('0' > s.charAt(i) && s.charAt(i) < '9') {
			i++;
		}
		String left = s.substring(1, i);
		String right = s.substring(i, s.length());
		
		PrefixExpression ret = new PrefixExpression();
		ret.opSet = oplist;
		ret.oper = operator.toString();
		ret.left = left;
		ret.right = right;
		return ret;
	}
}
