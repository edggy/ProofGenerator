package api.implementation;

import java.util.Comparator;

import util.Pair;
import api.Expression;
import api.Inference;
import api.Line;
import api.Operator;
import api.exception.InvalidExpressionException;

public final class LineImpl extends TreeExpression implements Line {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2275893147859039910L;
	public class LineUseComparator implements Comparator<LineImpl> {

		double lineWeight = 0;
		double useWeight = 0;
		double lenghtWeight = 1;
		@Override
		public int compare(LineImpl l1, LineImpl l2) {
			return (int) ((Math.pow(2, l1.uses) * useWeight + l1.lineNum * lineWeight + l1.length() * lenghtWeight) - 
					(Math.pow(2, l2.uses) * l2.uses * useWeight + l2.lineNum * lineWeight + l2.length() * lenghtWeight));
		}
		
	}
	//private String r;
	private int lineNum;
	private Inference infer;
	private Pair<Line, Line> prev;
	private int uses = 0;

	private LineImpl() {
	}
	public LineImpl(Expression e, int n) throws InvalidExpressionException {
		super(e);
		lineNum = n;
		infer = null;
		prev = null;
	}
	public LineImpl(Expression e, Inference i, Pair<Line, Line> pair, int n) throws InvalidExpressionException {
		super(e);
		//this.r = r;
		//infer = i.deepClone();
		//prev = pair.clone();
		infer = i;
		prev = pair;
		lineNum = n;
	}

	
	public Expression getExpression() {
		return this;
	}
	public Inference getInference() {
		return infer;
	}
	
	@Override
	public LineImpl clone() {
		LineImpl clone = null;
		if(infer == null) {
			try {
				clone = new LineImpl(super.clone(), lineNum);
			} catch (InvalidExpressionException e) {
				e.printStackTrace();
			}
			clone.uses = uses;
			return clone;
		}
		try {
			clone = new LineImpl(super.clone(), infer, prev.clone(), lineNum);
			clone.uses = uses;
		} catch (InvalidExpressionException e) {
			e.printStackTrace();
		}
		return clone;
	}
	
	@Override
	public Line deepClone() {
		LineImpl clone = new LineImpl();
		clone.oper = (Operator) oper.deepClone();
		clone.left = (Expression) left.deepClone();
		clone.right = (Expression) right.deepClone();
		
		clone.lineNum = this.lineNum;
		if(infer != null) clone.infer = (Inference) this.infer.deepClone();
		if(prev != null) clone.prev = this.prev.deepClone();
		
		return clone;
	}
	
	@Override
	public String toString() {
		if(infer != null) return lineNum + "\t" + infer.toString().replaceAll("<1>", ""+prev.getLeft().getLine()).replaceAll("<2>", ""+prev.getRight().getLine()) + "\t" + this.isTautology() + "\t" + super.infix();
		//if(infer != null) return lineNum + "\t" + infer.toString().replaceAll("<1>", ""+prev.getLeft().getLine()).replaceAll("<2>", ""+prev.getRight().getLine()) + "\t" + super.infix();
		return lineNum + "\t" + "Premise" + "\t" + super.infix();
		//return lineNum + "\t" + r;
		//return lineNum + "";
	}
	@Override
	public int compareTo(Line l) {
		return lineNum - l.getLine();
	}
	public int getUses() {
		return uses;
	}
	
	public void use() {
		uses += 1;
	}
	@Override
	public int getLine() {
		return lineNum;
	}
	@Override
	public Pair<Line, Line> getPreviousLines() {
		return prev.deepClone();
	}
}
