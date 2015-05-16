package nicod;
import java.util.Map;
import java.util.Set;

import api.Expression;
import api.Inference;
import api.Notation;
import api.Variable;
import api.exception.InvalidExpressionException;
import api.implementation.VariableImpl;


/**
 * @author Ethan
 *
 */
public class NicodSub implements Inference {

	private static final long serialVersionUID = 929266838143390294L;
	public static int priority = 5;
	//private static Map<Pair<Expression, Expression>, Expression> map = new HashMap<Pair<Expression, Expression>, Expression>();
	//private static Map<Expression, Pair<Expression, Expression>> backMap = new HashMap<Expression, Pair<Expression, Expression>>();
	private static Expression x;
	
	private Map<Expression, Expression> subMap;
	
	public NicodSub() {
		try {
			x = new NicodTreeExpression("|X1|X2X3", Notation.Prefix);
		} catch (InvalidExpressionException e) {
		}
	}

	/* 
	 * line1 = ((A|(B|C))|((D|(D|D))|((D|B)|((A|D)|(A|D)))))
	 * line2 = ((A|(B|C))|((D|(D|D))|((D|B)|((A|D)|(A|D)))))
	 * line3 = (((A|(B|C))|((D|(D|D))|((D|B)|((A|D)|(A|D)))))|((E|(E|E))|((E|(D|(D|D)))|(((A|(B|C))|E)|((A|(B|C))|E)))))
	 */
	
	/* 
	 * line1 = ((A|(B|C))|((D|(D|D))|((D|B)|((A|D)|(A|D)))))
	 * line2 = ((E|(D|(D|D)))|(((A|(B|C))|E)|((A|(B|C))|E)))
	 * line3 = (((E|(D|(D|D)))|(((A|(B|C))|E)|((A|(B|C))|E)))|((F|(F|F))|((F|((A|(B|C))|E))|(((E|(D|(D|D)))|F)|(((E|(D|(D|D)))|F))))))
	 */
	@Override
	public Expression infer(Expression line1, Expression line2) throws InvalidExpressionException {
		Expression top = line1.normalize();
		Expression bottom = line2.normalize();
		
		Expression res = null;
		if(x.canBeReplacedBy(top) && top.getLeft().canBeReplacedBy(bottom)) {
			subMap = top.getLeft().findSubstitution(bottom);
			Set<Variable> vars = top.getVariables();
			for(Variable v : vars) {
				if(!subMap.containsKey(v)) {
					subMap.put(v, new VariableImpl());
				}
			}
			res = top.substitute(subMap).normalize();
			return res;
		}
		return res;
	}
	
	@Override
	public String toString() {
		return "Sub <1>,<2>";
	}

	@Override
	public int compareTo(Inference i) {
		return priority - i.getPriority();
	}

	@Override
	public int getPriority() {
		return priority;
	}
	
	public NicodSub clone() {
		return this;
	}

	@Override
	public Inference deepClone() {
		return this;
	}

	/* 
	 * line1 = ((A|(B|C))|((D|(D|D))|((D|B)|((A|D)|(A|D)))))
	 * line2 = ((E|(D|(D|D)))|(((A|(B|C))|E)|((A|(B|C))|E)))
	 * line3 = (((E|(D|(D|D)))|(((A|(B|C))|E)|((A|(B|C))|E)))|((F|(F|F))|((F|((A|(B|C))|E))|(((E|(D|(D|D)))|F)|(((E|(D|(D|D)))|F))))))
	 */
	
	@Override
	public Expression backInfer1(Expression line2, Expression line3) throws InvalidExpressionException {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * line1 = ((A|(B|C))|((D|(D|D))|((D|B)|((A|D)|(A|D)))))
	 * line2 = ((E|(D|(D|D)))|(((A|(B|C))|E)|((A|(B|C))|E)))
	 * line3 = (((E|(D|(D|D)))|(((A|(B|C))|E)|((A|(B|C))|E)))|((F|(F|F))|((F|((A|(B|C))|E))|(((E|(D|(D|D)))|F)|(((E|(D|(D|D)))|F))))))
	 */
	@Override
	public Expression backInfer2(Expression line1, Expression line3) throws InvalidExpressionException {
		Expression line2 = line3.getLeft();
		if(line1.canBeReplacedBy(line3)) return line2.normalize();
		return null;
	}

	@Override
	public String moreInfo() {
		return subMap.toString();
	}

}
