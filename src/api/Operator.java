package api;

import java.util.List;
import java.util.Set;

import api.exception.*;
import util.*;

/**
 * @author Ethan
 *
 */
public interface Operator extends DeepCloneable {
	/*public static class Null implements Operator {
		public Null() {}
		
		@Override
		public TruthValue operate(Expression a, Expression b)
				throws InvalidExpressionException {
			return null;
		}

		@Override
		public boolean isRepresentation(String s) {
			if(s.equals("")) return true;
			return false;
		}

		@Override
		public boolean containsRepresentation(String s) {
			return true;
		}
		
		@Override
		public Operator clone() {
			return this;
		}
		
		@Override
		public String toString() {
			return "";
		}
		
		@Override
		public boolean equals(Object o) {
			if(o instanceof Null) return true;
			if(o instanceof Operator && ((Operator) o).isRepresentation("")) return true;
			return false;
		}

		@Override
		public Operator deepClone() {
			return this;
		}
	}*/
	
	public TruthValue operate(List<Expression> e) throws InvalidExpressionException;
	public boolean isRepresentation(String s);
	public boolean containsRepresentation(String s);
	public boolean equals(Object o);
	public Set<Tuple<TruthValue>> getTruthSet();
	public int arity();
	public Set<String> getRepresentations();
}
