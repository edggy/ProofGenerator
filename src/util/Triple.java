/**
 * 
 */
package util;

/**
 * @author Ethan
 *
 */
public class Triple<E, A, T> extends Pair<E, Pair<A, T>>{
//implements DeepCloneable<Triple<?, ?, ?>>{

	//public E e;
	//public A a;
	//public T t;
	
	private static final long serialVersionUID = 2363099002795870103L;

	public Triple() {
		super();
		//e = null;
		//a = null;
		//t = null;
	}

	
	public Triple(E e, A a, T t) {
		super(e, new Pair<A,T>(a,t));
		//this.e = e;
		//this.a = a;
		//this.t = t;
	}
	

	public E getFirst() {
		return e;
	}
	
	public A getSecond() {
		return t.e;
	}
	
	public T getThird() {
		return t.t;
	}
	
	/*@Override
	public int hashCode() {
		return e.hashCode()^a.hashCode()^t.hashCode();
	}
	*/
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Triple<?, ?, ?>)) return false;
		Triple<?, ?, ?> p = (Triple<?, ?, ?>) o;
		if(e.equals(p.e) && t.e.equals(p.t.e) && t.t.equals(p.t.t)) return true;
		return false;
	}
	
	@Override
	public String toString() {
		return "("+e+","+t.e+","+t.t+")";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Triple<E, A, T> clone() {
		//Triple<E, A, T> copy = new Triple<E, A, T>();
		//if(e instanceof DeepCloneable<?>) copy.e = (E) e.clone();
		//if(a instanceof DeepCloneable<?>) copy.a = (A) a.clone();
		//if(t instanceof DeepCloneable<?>) copy.t = (T) t.clone();
		return (Triple<E, A, T>) super.clone();
	}
	/*
	@SuppressWarnings("unchecked")
	@Override
	public Triple<E, A, T> deepClone() {
		Triple<E, A, T> copy = new Triple<E, A, T>();
		if(e instanceof DeepCloneable<?>) copy.e = (E) e.deepClone();
		if(a instanceof DeepCloneable<?>) copy.a = (A) a.deepClone();
		if(t instanceof DeepCloneable<?>) copy.t = (T) t.deepClone();
		return copy;
	}*/

}
