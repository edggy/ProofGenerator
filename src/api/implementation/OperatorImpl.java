/**
 * 
 */
package api.implementation;

import java.util.List;
import java.util.Set;

import util.Tuple;
import api.TruthValue;

/**
 * @author Ethan
 *
 */
public class OperatorImpl extends AbstractOperator {

	private static final long serialVersionUID = -3340208602844454275L;

	public OperatorImpl(Set<Tuple<TruthValue>> truthSet, List<String> representation) {
		super(truthSet, representation);
	}


}
