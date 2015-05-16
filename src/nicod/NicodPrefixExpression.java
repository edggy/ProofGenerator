package nicod;
import java.util.HashSet;
import java.util.Set;

import api.Expression;
import api.Notation;
import api.Operator;
import api.exception.InvalidExpressionException;
import api.implementation.PrefixExpression;


/**
 * @author Ethan
 *
 */
public class NicodPrefixExpression extends PrefixExpression {
	private static final long serialVersionUID = -2270378308323980092L;

	public NicodPrefixExpression() {
		super();
	}

	public NicodPrefixExpression(Operator o, Expression a, Expression b) throws InvalidExpressionException {
		super(o,a,b);
	}
	
	public NicodPrefixExpression(Expression a, Expression b) throws InvalidExpressionException {
		super(Util.getNand(), a, b);
	}

	public NicodPrefixExpression(Expression a) throws InvalidExpressionException {
		super(a);
	}

	public NicodPrefixExpression(String s, Notation n) throws InvalidExpressionException {
		if(s == null) throw new InvalidExpressionException();
		
		opSet = new HashSet<Operator>(1);
		opSet.add(Util.getNand());
		
		Expression a = parse(opSet, s, n);
		if(a == null || !a.isValid()) throw new InvalidExpressionException();
		
		this.setTo(a);
		if(!this.isValid()) throw new InvalidExpressionException();
		/*
		Expression a = parse(oplst, s);
		if(a.getLeft() == null || a.getRight() == null || a.getOperator() == null) throw new InvalidExpressionException();
		left = a.getLeft();
		right = a.getRight();
		oper = a.getOperator();*/
	}

	public NicodPrefixExpression(Set<Operator> oplist, String s, Notation n) throws InvalidExpressionException {
		super(oplist,s, n);
		/*if(oplst == null) oplst = new HashSet<Operator>(1);
		oplst.add(new Nand());
		Expression a = parse(oplst, s);
		if(a.getLeft() == null || a.getRight() == null || a.getOperator() == null) throw new InvalidExpressionException();
		left = a.getLeft();
		right = a.getRight();
		oper = a.getOperator();*/
	}

	/* (non-Javadoc)
	 * @see Expression#canSubstitute(Wff, Expression)
	 */
	/*@Override
	public Map<Expression, Expression> findSubstitution(Expression a) throws InvalidExpressionException {
		if(a == null || !a.isValid()) throw new InvalidExpressionException();
		
		//TODO
		/*	Example:
		 * 	this = (A|(B|C)) can be replaced by 
		 * 	a = ((A|(B|C))|((D|(D|D))|((D|B)|((A|D)|(A|D)))))
		 *  A => (A|(B|C))
		 *  B => (D|(D|D))
		 *  C => ((D|B)|((A|D)|(A|D)))
		 *  D => E
		 *  We will get:
		 *  (((A|(B|C))|((D|(D|D))|((D|B)|((A|D)|(A|D)))))|((E|(E|E))|((E|(D|(D|D)))|(((A|(B|C))|E)|((A|(B|C))|E)))))
		 */

		/*
		 * (A|BC)
		 * (ABC|DDDDBADAD)
		 */
		/*Map<Expression, Expression> leftMap = new HashMap<Expression, Expression>();
		Map<Expression, Expression> rightMap = new HashMap<Expression, Expression>();

		if(!this.canBeReplacedBy(a)) return leftMap;
		//System.out.println("a = " + a + "\tthis = " + this);
		if(this instanceof Variable) {
			leftMap.put(this, a);
			return leftMap;
		}
		
		if(a instanceof Variable) {
			return leftMap;
		}

		if(this.getLeft() instanceof Variable) {
			leftMap.put(getLeft(), a.getLeft());
		}
		else {
			leftMap.putAll(getLeft().findSubstitution(a.getLeft()));
		}

		if(this.getRight() instanceof Variable) {
			rightMap.put(getRight(), a.getRight());
		}
		else {
			rightMap.putAll(getRight().findSubstitution(a.getRight()));
		}

		//System.out.println("a = " + a + "\tthis = " + this);
		//System.out.println(leftMap + "\t" + rightMap);
		Map<Expression, Expression> map = mapMerge(leftMap, rightMap);
		//Set<Expression> set = a.getVariables();
		//set.removeAll(map.keySet());
		//for(Expression e : set) {
		//	if(map.get(e) == null) {
		//		map.put(e, new NicodExpression());
		//	}
			//System.out.println(e + "=>" + map.get(e).getName());
		//}

		//System.out.println("a = " + a + "\tthis = " + this);
		//System.out.println(leftMap + "\t" + rightMap);
		//System.out.println(map);
		return map;
	}

	private Map<Expression, Expression> mapMerge(Map<Expression, Expression> left, Map<Expression, Expression> right) throws InvalidExpressionException {
		Map<Expression, Expression> map = new HashMap<Expression, Expression>();
		for(Entry<Expression, Expression> leftset  : left.entrySet()) {
			Expression var = leftset.getKey();
			if(right.containsKey(var)) {
				Expression leftSub = leftset.getValue();
				Expression rightSub = right.get(var);
				if(leftSub.canBeReplacedBy(rightSub)) {
					map.put(var, rightSub);
				}
				else if(rightSub.canBeReplacedBy(leftSub)) {
					map.put(var, leftSub);
				}
			}
			else {
				map.put(var, leftset.getValue());
			}
		}
		for(Entry<Expression, Expression> rightset  : right.entrySet()) {
			map.putIfAbsent(rightset.getKey(), rightset.getValue());
		}
		return map;
	}*/
}
