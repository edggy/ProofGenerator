package nicod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import util.Tuple;

import api.Expression;
import api.Operator;
import api.TruthValue;
import api.exception.*;
import api.implementation.AbstractOperator;

/**
 * @author Ethan
 *
 */
public class Nand extends AbstractOperator{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3142528235098634568L;
	private static String[] representation = {"|", "Nand"};
	private static Set<Tuple<TruthValue>> truthSet;
	
	public Nand() {
		truthSet = new HashSet<Tuple<TruthValue>>();
		Tuple<TruthValue> lst = new Tuple<TruthValue>(2);
		for(int i = 0; i < 4; i++) {
			lst.set(0,(i%2==0)?TruthValue.False:TruthValue.True);
			lst.set(1,(i/2%2==0)?TruthValue.False:TruthValue.True);
			if(i != 3) truthSet.add(lst);
		}
	}
	
	/* (non-Javadoc)
	 * @see Operator#operate(Expression, Expression)
	 */
	@Override
	public TruthValue operate(List<Expression> e) throws InvalidExpressionException {
		if(e == null || e.size() != 2) throw new InvalidExpressionException();
		Expression a = e.get(0);
		Expression b = e.get(1);
		if(a == null || b == null) throw new InvalidExpressionException();
		Tuple<TruthValue> lst = new Tuple<TruthValue>(2);
		lst.set(0, a.evaluate());
		lst.set(1, b.evaluate());
		if(truthSet.contains(lst)) return TruthValue.True;
		return TruthValue.False;
		//if(a.evaluate().isTrue() && b.evaluate().isTrue()) return TruthValue.False;
		//return TruthValue.True;
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Nand;
	}
	
	public String toString() {
		return representation[0];
	}

	@Override
	public boolean isRepresentation(String s) {
		for(String r : representation) {
			if(r.equals(s)) return true;
		}
		return false;
	}

	@Override
	public boolean containsRepresentation(String s) {
		for(String r : representation) {
			//if(s.matches(r)) return true;
			//if(s.matches(r+".*")) return true;
			if(s.matches("^.*"+ Pattern.quote(r)+".*$")) return true;
			//if(s.length() == r.length() && s.matches("^.*"+r.replaceAll("|", "\\|")+".*$")) return true;
			//if(s.startsWith(r) || s.endsWith(r)) return true;
		}
		return false;
	}

	/*public Nand clone() {
		return this;
	}

	@Override
	public Operator deepClone() {
		return this;
	}
	
	@Override
	public int hashCode() {
		return representation.hashCode();
	}

	@Override
	public int arity() {
		return 2;
	}

	@Override
	public Set<Tuple<TruthValue>> getTruthSet() {
		return truthSet;
	}*/
}
