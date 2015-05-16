/**
 * 
 */
package api.implementation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import util.Tuple;
import api.Expression;
import api.Operator;
import api.TruthValue;
import api.exception.InvalidExpressionException;

/**
 * @author Ethan
 *
 */
public abstract class AbstractOperator implements Operator {

	private static final long serialVersionUID = 233380873619351953L;
	protected int arity;
	protected List<String> representation;
	protected Set<Tuple<TruthValue>> truthSet;
	
	/**
	 * 
	 */
	public AbstractOperator(Set<Tuple<TruthValue>> truthSet, List<String> representation) {
		this.truthSet = new HashSet<Tuple<TruthValue>>();
		for(Tuple<TruthValue> t : truthSet) {
			this.truthSet.add(t);
			arity = t.size();
		}
		this.representation = new LinkedList<String>();
		for(String r : representation) {
			this.representation.add(r);
		};
	}

	public Operator clone() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see util.DeepCloneable#deepClone()
	 */
	@Override
	public Operator deepClone() {
		return this;
	}

	/* (non-Javadoc)
	 * @see api.Operator#operate(java.util.List)
	 */
	@Override
	public TruthValue operate(List<Expression> e) throws InvalidExpressionException {
		Tuple<TruthValue> lst = new Tuple<TruthValue>(arity);
		for(int i = 0; i < e.size(); i++) {
			lst.set(i, e.get(i).evaluate());
		}
		return truthSet.contains(lst)?TruthValue.True:TruthValue.False;
	}

	/* (non-Javadoc)
	 * @see api.Operator#isRepresentation(java.lang.String)
	 */
	@Override
	public boolean isRepresentation(String s) {
		for(String r : representation) {
			if(r.equals(s)) return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see api.Operator#containsRepresentation(java.lang.String)
	 */
	@Override
	public boolean containsRepresentation(String s) {
		for(String r : representation) {
			if(s.matches("^.*"+ Pattern.quote(r)+".*$")) return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see api.Operator#getTruthSet()
	 */
	@Override
	public Set<Tuple<TruthValue>> getTruthSet() {
		return truthSet;
	}

	/* (non-Javadoc)
	 * @see api.Operator#arity()
	 */
	@Override
	public int arity() {
		return arity;
	}
	
	@Override
	public int hashCode() {
		return representation.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Operator)) return false;
		return this.truthSet.equals(((Operator) o).getTruthSet());
	}
	
	public String toString() {
		return representation.get(0);
	}
	
	public Set<String> getRepresentations() {
		Set<String> s = new HashSet<String>();
		s.addAll(representation);
		return s;
	}

}
