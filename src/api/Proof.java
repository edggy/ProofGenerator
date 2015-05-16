package api;

import java.io.BufferedWriter;
import java.util.List;

import api.exception.*;
import util.*;

/**
 * @author Ethan
 *
 */
public interface Proof extends DeepCloneable {
	public List<? extends Expression> prove(Expression goal) throws InvalidExpressionException;
	
	public List<? extends Expression> prove(Expression goal, boolean verbose) throws InvalidExpressionException;
	
	public List<? extends Expression> prove(Expression goal, BufferedWriter writer) throws InvalidExpressionException;
	
	public boolean addRule(Inference rule);
	
	public boolean removeRule(Inference rule);
	
	public boolean addPremise(Expression premise) throws InvalidExpressionException;
	
	public boolean removePremise(Expression premise);
	
	
	public int length();
	
	public Line getLine(int n);
}
