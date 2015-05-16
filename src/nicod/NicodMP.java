package nicod;
import api.Expression;
import api.Inference;
import api.Notation;
import api.implementation.TreeExpression;
import api.implementation.VariableImpl;
import api.exception.InvalidExpressionException;

/**
 * @author Ethan
 *
 */
public class NicodMP implements Inference {
	
	private static final long serialVersionUID = 5807114174595828883L;

	public static int priority = 1;
	
	private static Expression x;

	public NicodMP() {
		try {
			x = new NicodTreeExpression("|X1|X2X3", Notation.Prefix);
		} catch (InvalidExpressionException e) {
		}
	}
	/* 
	 * line1 = (A)
	 * line2 = (A|(B|C))
	 * line3 = (C)
	 */
	
	/* (non-Javadoc)
	 * @see Expression#Ponens(Expression, Expression)
	 */
	@Override
	public Expression infer(Expression line1, Expression line2) throws InvalidExpressionException {
		if(line1 == null || line2 == null) throw  new InvalidExpressionException();

		Expression top = line1.normalize();
		Expression sub = line2.normalize();
		
		if(!x.canBeReplacedBy(sub)) {
			return null;
		}
		
		Expression partA = sub.getLeft();
		Expression partBC = sub.getRight();
		if(partA == null || partBC == null) return null;
		Expression partB = partBC.getLeft();
		Expression partC = partBC.getRight();
		if(partB == null || partC == null) return null;

		if(top.equals(partA)) {
			Expression res = partC.normalize();
			return res;
		}
		return null;
	}

	@Override
	public String toString() {
		return "MP <1>,<2>";
	}

	@Override
	public int compareTo(Inference i) {
		return priority - i.getPriority();
	}

	@Override
	public int getPriority() {
		return priority;
	}
	
	public NicodMP clone() {
		return this;
	}

	@Override
	public Inference deepClone() {
		return this;
	}

	/* 
	 * line1 = (A)
	 * line2 = (A|(B|C))
	 * line3 = (C)
	 */
	
	/**
	 * Not used
	 */
	@Override
	public Expression backInfer1(Expression line2, Expression line3) throws InvalidExpressionException {
		if(line2 == null || line3 == null) throw  new InvalidExpressionException();
		
		//Expression infer = line3.deepClone();
		//Pair<Expression, Expression> p = new Pair<Expression, Expression>(line1.deepClone().normalize(),line2.deepClone().normalize());
		//if(backMap.containsKey(infer)) {
		//	return backMap.get(infer).getLeft().deepClone().normalize();
		//}
		
		Expression line1 = (Expression) line2.getLeft();
		if(line2.getRight().getRight().equals(line3)) {
			//Pair<Expression, Expression> p = new Pair<Expression, Expression>(line1.deepClone().normalize(),line2.deepClone().normalize());
			//Expression res = line2.deepClone().normalize();
			//map.put(p, res);
			//backMap.put(res, p);
			return line1.normalize();
		}
		return null;
	}

	/* 
	 * line1 = (A)
	 * line2 = (A|(B|C))
	 * line3 = (C)
	 */
	
	/**
	 * Not used
	 */
	@Override
	public Expression backInfer2(Expression line1, Expression line3) throws InvalidExpressionException {
		if(line1 == null || line3 == null) throw  new InvalidExpressionException();
		return null;
		/*
		//Expression infer = line3.deepClone().normalize();
		
		//if(backMap.containsKey(infer)) {
		//	return backMap.get(infer).getRight().deepClone().normalize();
		//}
		
		Expression a = line1.deepClone();
		Expression b = new VariableImpl();
		Expression c = line3.deepClone();
		Expression left = a;
		Expression right = a.deepClone();
		right.setOperator(Util.getNand());
		right.setLeft(b);
		right.setRight(c);
		//new TreeExpression(Util.getNand(), b, c);
		
		Expression line2 = 
				new TreeExpression(Util.getNand(), left, right);
		//line2.setOperator(new Nand());
		//line2.setLeft(left);
		//line2.setRight(right);
		//line2.setName("");
		//b = line2.getNewVariable();
		//Pair<Expression, Expression> p = new Pair<Expression, Expression>(line1.deepClone().normalize(),line2.deepClone().normalize());
		
		//Expression res = line3.deepClone().normalize();
		//map.put(p, res);
		//backMap.put(res, p);
		return line2.deepClone().normalize();
		*/
	}

	@Override
	public String moreInfo() {
		return "";
	}

}
