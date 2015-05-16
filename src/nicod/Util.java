package nicod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.Tuple;

import api.Operator;
import api.TruthValue;
import api.implementation.OperatorImpl;

final class Util {
	private static Operator nand;
	
	public static Operator getNand() {
		if(nand == null) {
			Set<Tuple<TruthValue>> set = new HashSet<Tuple<TruthValue>>();
			List<String> rep = new ArrayList<String>();
			Tuple<TruthValue> lst;
			for(int i = 0; i < 4; i++) {
				lst = new Tuple<TruthValue>(2);
				lst.set(0,(i%2==0)?TruthValue.False:TruthValue.True);
				lst.set(1,(i/2%2==0)?TruthValue.False:TruthValue.True);
				if(i != 3) set.add(lst);
			}
			rep.add("|");
			rep.add("Nand");
			nand = new OperatorImpl(set, rep);
		}
		return nand;
	}
}
