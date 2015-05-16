package nicod;
import java.util.HashMap;
import java.util.Map;

import api.Expression;
import api.InvalidExpressionException;
import api.Literal;
import api.TruthValue;

/**
 * 
 */

/**
 * @author Ethan
 *
 */
public class NicodLiteral extends NicodExpression implements Literal {
	TruthValue TV;
	
	public NicodLiteral() {
		TV = TruthValue.False;
	}
	
	public NicodLiteral(boolean value) {
		if(value) {
			TV = TruthValue.True;
		}
		else {
			TV = TruthValue.False;
		}
	}
	
	public NicodLiteral(TruthValue value) {
		TV = value;
	}
	
	public NicodLiteral(Literal l) {
		if(l.isTrue()) TV = TruthValue.True;
		else if(l.isFalse()) TV = TruthValue.False;
		else TV = TruthValue.Unknown;
	}
	
	public boolean isTrue() {
		return TV.isTrue();
	}
	
	public boolean isFalse() {
		return TV.isFalse();
	}
	
	@Override
	public TruthValue evaluate() {
		return TV;
	}
	
	@Override
	public Expression clone() {
		return new NicodLiteral(this);
	}

	@Override
	public String getName() {
		return TV.name();
	}

	@Override
	public Literal setTruthValue(boolean value) {
		if(value) TV = TruthValue.True;
		else TV = TruthValue.False;
		return this;
	}
	
	@Override
	public Literal setTruthValue(TruthValue value) {
		TV = value;
		return this;
	}
	
	@Override
	public Literal setTruthValue(Literal l) {
		if(l.isTrue()) TV = TruthValue.True;
		else if(l.isFalse()) TV = TruthValue.False;
		else TV = TruthValue.Unknown;
		return this;
	}
	
	@Override
	public Expression normalize() {
		return this;
	}
	
	@Override
	public Map<Expression, Expression> findSubstitution(Expression a) throws InvalidExpressionException {
		if(!a.isValid()) throw new InvalidExpressionException();
		Map<Expression, Expression> m = new HashMap<Expression, Expression>();
		return m;
	}
}

