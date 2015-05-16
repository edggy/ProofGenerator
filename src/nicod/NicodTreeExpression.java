package nicod;
import java.util.HashSet;
import java.util.Set;

import api.*;
import api.exception.InvalidExpressionException;
import api.implementation.TreeExpression;


/**
 * @author Ethan
 *
 */
public class NicodTreeExpression extends TreeExpression {

	private static final long serialVersionUID = -3282778081671456614L;

	public NicodTreeExpression() {
		super();
	}

	public NicodTreeExpression(Operator o, Expression a, Expression b) throws InvalidExpressionException {
		super(o,a,b);
	}
	
	public NicodTreeExpression(Expression a, Expression b) throws InvalidExpressionException {
		super(Util.getNand(), a, b);
	}

	public NicodTreeExpression(Expression a) throws InvalidExpressionException {
		super(a);
	}

	public NicodTreeExpression(String s, Notation n) throws InvalidExpressionException {
		if(s == null) throw new InvalidExpressionException();
		
		Set<Operator> opSet = new HashSet<Operator>(1);
		opSet.add(Util.getNand());
		
		Expression a = parse(opSet, s, n);
		if(a == null || !a.isValid()) throw new InvalidExpressionException();
		
		this.setTo(a);
		if(!this.isValid()) throw new InvalidExpressionException();
	}

	public NicodTreeExpression(Set<Operator> oplist, String s, Notation n) throws InvalidExpressionException {
		super(oplist, s, n);
	}
}
