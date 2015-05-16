package api;

import java.util.Map;
import java.util.Set;

import api.exception.*;

import util.*;


/**
 * A logic statement that has a left Expression and a right Expression that has an Operator
 * @author Ethan
 * 
 */
public interface Expression extends DeepCloneable {

	/**
	 * Gets the main operator for this Expression<p>
	 * <b>Note:</b> This will be null if this is a root
	 * @return The main Operator
	 */
	public Operator getOperator();
	
	/**
	 * Gets the left part of the Expression<p>
	 * <b>Note:</b> This will be null if this is a Wff or Variable
	 * @return The left Expression
	 */
	public Expression getLeft();
	
	/**
	 * Gets the right part of the Expression<p>
	 * <b>Note:</b> This will be null if this is a Wff or Variable
	 * @return The right Expression
	 */
	public Expression getRight();

	/**
	 * Sets the left side of the main Operator to e
	 * @param e A new Expression to replace the left side of the main Operator
	 * @return This with the left side changed
	 * @throws InvalidExpressionException If e is not a valid  Expression
	 */
	public Expression setLeft(Expression e) throws InvalidExpressionException;

	/**
	 * Sets the right side of the main Operator to e
	 * @param e A new Expression to replace the right side of the main Operator
	 * @return This with the right side changed
	 * @throws InvalidExpressionException If e is not a valid  Expression
	 */
	public Expression setRight(Expression e) throws InvalidExpressionException;
	
	/**
	 * Sets the main Operator to o
	 * @param o The new Operator to use
	 * @return This with the operator changed
	 */
	public Expression setOperator(Operator o);
	
	/**
	 * Evaluates this Expression and determines it's TruthValue
	 * @return The TruthValue of this Expression
	 */
	public TruthValue evaluate();
	
	/**
	 * Determines if this Expression is valid
	 * @return Whether this Expression is valid
	 */
	public boolean isValid();
	
	/**
	 * Finds a mapping of Expression that can be replaced to substitute a into this
	 * @param a The Expression to be replaced
	 * @return The mapping of substitutions or an empty map if there are none
	 * @throws InvalidExpressionException If e is not a valid  Expression
	 */
	public Map<Expression, Expression> findSubstitution(Expression a) throws InvalidExpressionException;
	
	/**
	 * Substitutes each Expression in the map<p>
	 * @param m A Map of replacements
	 * @return The new Expression with replacements
	 * @throws InvalidExpressionException If any Expressions in the Map is not valid
	 */
	public Expression substitute(Map<Expression, Expression> m) throws InvalidExpressionException;
	
	/**
	 * Determines if the Expression can replace the Wff
	 * @param a The Expression to test
	 * @return If the new Expression can replace the Wff
	 * @throws InvalidExpressionException If a is not a valid  Expression
	 */
	public boolean canBeReplacedBy(Expression a) throws InvalidExpressionException;
	
	/**
	 * Checks whether e is a subExpression
	 * @param e The Expression to search for
	 * @return True if e is a subtree
	 * @throws InvalidExpressionException If e is not a valid  Expression
	 */
	public boolean existsIn(Expression e) throws InvalidExpressionException;
	
	/**
	 * Determines if Object o is equivalent to this Expression
	 * @param o An object to compare to
	 * @return True iff this Expression is equivalent to o
	 */
	public boolean equals(Object o);
	
	/**
	 * Parses the string s into an Expression using the Operators in the List o
	 * @param o A List of the Operators that are used in this Expression
	 * @param s A String to parse into an Expression
	 * @return An Expression that is formed from s and o
	 * @throws InvalidExpressionException If the String does not parse into a valid Expression
	 */
	public Expression parse(Set<Operator> o, String s, Notation n) throws InvalidExpressionException;
	
	/**
	 * Generates a set containing all the Wffs and Variables used
	 * @return A set of all the Wffs and Variables used in this Expression
	 */
	public Set<Variable> getVariables();
	
	/**
	 * Generates a set containing all the Operators used
	 * @return A set of all the Operators used in this Expression
	 */
	public Set<Operator> getOpSet();
	
	/**
	 * Makes a deep copy of this Expression and sets the first Wffs to "A", the second to "B" and so forth
	 * @return A normalized deep copy of this Expression
	 */
	public Expression normalize();
	
	/**
	 * Makes a deep copy of this Expression and sets the first Wffs to "X"+start, the second to "X"+(start+1) and so forth
	 * @return A normalized deep copy of this Expression
	 */
	public Expression normalize(int start);
	
	/**
	 * Gets the size of this Expression
	 * @return The number of Wffs and Variables in this Expression
	 */
	public int length();
	
	/**
	 * Determines if this Expression is a tautology<p>
	 * An Expression is a tautology iff it is always true independent of it's Variable's or Wff's values are
	 * @return Whether this Expression is a tautology or not
	 */
	public boolean isTautology();
	
	/**
	 * Determines if this Expression is a contradiction<p>
	 * An Expression is a contradiction iff it is always false independent of it's variable's or Wff's values are
	 * @return Whether this Expression is a contradiction or not
	 */
	public boolean isContradiction();
	
	public String prefix();
	
	public String infix();
	
	public String postfix();
	
	public String toString(Notation n);
} 
