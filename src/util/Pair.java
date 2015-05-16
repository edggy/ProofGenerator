/**
 * 
 */
package util;

/**
 * @author Ethan
 *
 */
public class Pair<E , T> implements DeepCloneable {

	private static final long serialVersionUID = -7877729320247282612L;
	public E e;
	public T t;
	
	public Pair() {
		t = null;
		e = null;
	}
	
	public Pair(E e, T t) {
		this.t = t;
		this.e = e;
	}
	
	public E getLeft() {
		return e;
	}
	
	public T getRight() {
		return t;
	}

	@Override
	public int hashCode() {
		return e.hashCode()^(t.hashCode()+1);
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Pair<?, ?>)) return false;
		Pair<?, ?> p = (Pair<?, ?>) o;
		if(t.equals(p.t) && e.equals(p.e)) return true;
		return false;
	}
	
	@Override
	public String toString() {
		return "("+t+","+e+")";
	}
	
	@Override
	public Pair<E, T> clone() {
		Pair<E,T> copy = new Pair<E,T>();
		copy.e = e;
		copy.t = t;
		return copy;
		//return new Pair<E, T>((E) ((DeepCloneable<E>)e).clone(), (T) ((DeepCloneable<T>)t).clone());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pair<E, T> deepClone() {
		Pair<E, T> copy = clone();
		if(e instanceof DeepCloneable) copy.e = (E) ((DeepCloneable) e).deepClone();
		if(t instanceof DeepCloneable) copy.t = (T) ((DeepCloneable) t).deepClone();
		return copy;
	}
}

/*public class Pair {
		public Expression e1;
		public Expression e2;

		public Pair(Expression e1, Expression e2) {
			this.e1 = e1;
			this.e2 = e2;
		}
		public int hashCode() {
			return e1.hashCode();
		}
		public boolean equals(Object o) {
			if(!(o instanceof Pair)) return false;
			Pair p = (Pair) o;
			if(e1.equals(p.e1) && e2.equals(p.e2)) return true;
			return false;
		}
	}*/
