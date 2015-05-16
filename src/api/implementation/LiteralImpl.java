package api.implementation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import api.*;
import api.exception.*;

/**
 * @author Ethan
 *
 */
public class LiteralImpl extends VariableImpl implements Literal {
	private static final long serialVersionUID = 4412313428658280944L;
	TruthValue TV;
	
	public LiteralImpl() {
		TV = TruthValue.False;
		name = TV.toString();
	}
	
	public LiteralImpl(boolean value) {
		if(value) {
			TV = TruthValue.True;
		}
		else {
			TV = TruthValue.False;
		}
		name = TV.toString();
	}
	
	public LiteralImpl(TruthValue value) {
		TV = value;
		name = TV.toString();
	}
	
	public LiteralImpl(Literal l) {
		if(l.isTrue()) TV = TruthValue.True;
		else if(l.isFalse()) TV = TruthValue.False;
		else TV = TruthValue.Unknown;
		name = TV.toString();
	}

	@Override
	public boolean isTrue() {
		return TV.isTrue();
	}
	
	@Override
	public boolean isFalse() {
		return TV.isFalse();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Literal) {
			if(TV.isTrue() && ((Literal) o).isTrue()) return true;
			if(TV.isFalse() && ((Literal) o).isFalse()) return true;
			if(TV.isUnknown()) return true;
		}
		else if(o instanceof TruthValue) {
			if(TV.isTrue() && ((TruthValue) o).isTrue()) return true;
			if(TV.isFalse() && ((TruthValue) o).isFalse()) return true;
			if(TV.isUnknown()) return true;
		}
		else if(o instanceof Boolean) {
			if(TV.isTrue() && ((Boolean) o)) return true;
			if(TV.isFalse() && (!(Boolean) o)) return true;
			if(TV.isUnknown()) return true;
		}
		return false;
	}

	@Override
	public TruthValue evaluate() {
		return TV;
	}
	
	@Override
	public Literal clone() {
		return new LiteralImpl(this);
	}
	
	@Override
	public Literal deepClone() {
		return new LiteralImpl(this);
	}
	
	@Override
	public String getName() {
		return TV.name();
	}
	
	@Override
	public Literal setValue(boolean value) {
		if(value) TV = TruthValue.True;
		else TV = TruthValue.False;
		return this;
	}
	
	public Literal setValue(TruthValue value) {
		TV = value;
		return this;
	}

	@Override
	public Literal setValue(Literal l) {
		if(l.isTrue()) TV = TruthValue.True;
		else if(l.isFalse()) TV = TruthValue.False;
		else TV = TruthValue.Unknown;
		return this;
	}

	@Override
	public Map<Expression, Expression> findSubstitution(Expression a) throws InvalidExpressionException {
		if(!a.isValid()) throw new InvalidExpressionException();
		Map<Expression, Expression> m = new HashMap<Expression, Expression>();
		return m;
	}

	@Override
	public Literal parse(Set<Operator> o, String s, Notation n) throws InvalidLiteralException {
		return parse(o, s);
	}
	
	public Literal parse(Set<Operator> o, String s) throws InvalidLiteralException {
		try {
			Variable var = super.parse(o, s, null);
			for(TruthValue t : TruthValue.values()) {
				if(var.getName().equals(t)) return new LiteralImpl(t);
			}
		} catch (InvalidVariableException e) {
			throw new InvalidLiteralException();
		}
		throw new InvalidLiteralException();
	}
	
	@Override
	public Literal parse(String s) throws InvalidLiteralException {
		return parse(new HashSet<Operator>(), s);
	}

	@Override
	public void setName(String s) {
	}

	@Override
	public Literal normalize() {
		return this.deepClone();
	}

	@Override
	public Literal normalize(int start) {
		return this.deepClone();
	}

}
