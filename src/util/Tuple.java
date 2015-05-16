/**
 * 
 */
package util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ethan
 *
 */
public class Tuple<T> implements DeepCloneable {

	private static final long serialVersionUID = 3484313982198936052L;
	int size;
	ArrayList<T> list;
	
	public Tuple(int n) {
		size = n;
		list = new ArrayList<T>(size);
		while(list.size() < size) {
			list.add(null);
		}
	}
	
	public Tuple(Tuple<T> l) {
		size = l.size();
		list = new ArrayList<T>(size);
		for(int i = 0; i < l.size(); i++) {
			list.set(i, l.get(i));
		}
	}
	
	public Tuple(List<T> l) {
		size = l.size();
		list = new ArrayList<T>(size);
		for(int i = 0; i < l.size(); i++) {
			list.set(i, l.get(i));
		}
	}
	
	public Tuple(T[] l) {
		size = l.length;
		list = new ArrayList<T>(size);
		for(int i = 0; i < l.length; i++) {
			list.set(i, l[i]);
		}
	}

	public T get(int n) {
		return list.get(n);
	}
	
	public void set(int n, T t) {
		list.set(n, t);
	}
	
	public int size() {
		return size;
	}

	public Tuple<T> clone() {
		return new Tuple<T>(this);
	}
	
	@SuppressWarnings("unchecked")
	public Tuple<T> deepClone() {
		Tuple<T> copy = new Tuple<T>(size);
		for(int i = 0; i < size; i++) {
			T cur = list.get(i);
			if(cur instanceof DeepCloneable) {
				DeepCloneable d = (DeepCloneable) cur;
				copy.set(i, (T) d.deepClone());
			} 
			else {
				copy.set(i, cur);
			}
		}
		return copy;
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Tuple<?>)) return false;
		Tuple<?> p = (Tuple<?>) o;
		for(int i = 0; i < size; i++) {
			if(!p.list.get(i).equals(this.list.get(i))) return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		String s = "<";
		for(T t : list) {
			s += t + ",";
		}
		return s.substring(0, s.length()-1) + ">";
	}
	
	@Override
	public int hashCode() {
		int hash = list.hashCode();
		for(int i = 0; i < list.size(); i++) {
			hash = hash^(list.get(i).hashCode() + i*100);
		}
		return hash;
	}
}
