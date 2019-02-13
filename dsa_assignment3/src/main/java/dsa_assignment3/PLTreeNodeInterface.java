package dsa_assignment3;

import java.util.Map;

public interface PLTreeNodeInterface
{

	/**
	 * Produce the list of <code>NodeType</code> entries which, if provide to
	 * <code>reversePolishBuilder</code>, would construct the current tree
	 * 
	 * @return A <code>NodeType</code> array of the reverse polish notation
	 *         specification of this tree
	 */
	public NodeType[] getReversePolish();

	/**
	 * Prints out the string in prefix notation. There should be no white space
	 * in the representation. Binary operators should use parentheses around
	 * their sub-expression. Unary operators should not.
	 * 
	 * The <code>getInfixName()</code> method from <code>NodeType</code> should
	 * be used to get the name use for an <code>NodeType</code> operator, and
	 * the <code>getArity()</code> method to get the number of arguments that a
	 * <code>NodeType</code> operator takes.
	 * 
	 * The following are examples of infix notations:
	 * 
	 * <pre>
	 * ((R∨P)→(⊤∧¬Q))
	 * (¬(⊥∨⊤)∨(⊤∧¬Q))
	 * ((¬⊥∨⊤)∧((¬⊥∨¬Q)∧((¬⊤∨⊤)∧(¬⊤∨¬Q))))
	 * </pre>
	 * 
	 * @return the string representation of the tree rooted at this node in
	 *         infix notation
	 */
	public String toString();

	/**
	 * Prints out the string in prefix notation. There should be no white space
	 * in the representation. Both unary and binary operators should use
	 * parentheses around their arguments:
	 * 
	 * The <code>getPrefixName()</code> method from <code>NodeType</code> should
	 * be used to get the name use for an <code>NodeType</code> operator, and
	 * the <code>getArity()</code> method to get the number of arguments that a
	 * <code>NodeType</code> operator takes.
	 * 
	 * The following are examples of prefix notation:
	 * 
	 * <pre>
	 * implies(or(R,P),and(true,not(Q)))
	 * or(not(or(false,true)),and(true,not(Q)))
	 * and(or(not(false),true),and(or(not(false),not(Q)),and(or(not(true),true),or(not(true),not(Q)))))
	 * </pre>
	 * 
	 * @return the string representation of the tree rooted at this node in
	 *         prefix notation
	 */
	public String toStringPrefix();

	/**
	 * Prints out the string in infix notation. There should be no white space
	 * in the representation. Binary operators should use parentheses around
	 * their sub-expression. Unary operators should not.
	 * 
	 * The <code>getInfixName()</code> method from <code>NodeType</code> should
	 * be used to get the name use for an <code>NodeType</code> operator, and
	 * the <code>getArity()</code> method to get the number of arguments that a
	 * <code>NodeType</code> operator takes.
	 * 
	 * The following are examples of infix notations:
	 * 
	 * <pre>
	 * ((R∨P)→(⊤∧¬Q))
	 * (¬(⊥∨⊤)∨(⊤∧¬Q))
	 * ((¬⊥∨⊤)∧((¬⊥∨¬Q)∧((¬⊤∨⊤)∧(¬⊤∨¬Q))))
	 * </pre>
	 * 
	 * @return the string representation of the tree rooted at this node in
	 *         infix notation
	 */
	public String toStringInfix();

	/**
	 * Applies a set of variable bindings recursively to the propositional logic
	 * expression represented by the current <code>PLTreeNode</code>
	 * 
	 * <p>
	 * This should <strong>update</strong> the tree rooted at the current node
	 * <strong>in place</strong> by replacing any variable in the tree
	 * (<code>NodeType.A</code> to <code>NodeType.A</code>) that also appears in
	 * <code>bindings</code> with either <code>NodeType.TRUE</code> or
	 * <code>NodeType.FALSE</code> depending on whether that variable is mapped
	 * to <code>true</code> or <code>false</code> respectively in
	 * <code>bindings</code>
	 * </p>
	 * 
	 * @param bindings
	 *            A map that maps <code>NodeType</code> objects to boolean
	 *            values. Any variable in <code>bindings</code> that does not
	 *            appear in the tree will be ignored. Any <code>NodeType</code>
	 *            in <code>bindings</code> that is <strong>not</strong> a
	 *            variable will be ignored
	 */
	public void applyVarBindings(Map<NodeType, Boolean> bindings);

	/**
	 * Evaluate the logical expression tree recursively, <strong>updating it in
	 * place</strong> to reduce constant sub-trees (i.e. those containing no
	 * variables) and make simplifications that do not require deep comparisons.
	 * <p>
	 * Thus <code>⊥∧A</code> should be reduced to <code>⊥</code>, as it has to
	 * be <code>⊥</code> no matter what value <code>A</code> takes.
	 * </p>
	 * Similarly <code>⊥∧(A∨B)</code> should be reduced to <code>⊥</code>, as it
	 * has to be <code>⊥</code> no matter what value the right sub-expression
	 * <code>(A∨B)</code> takes.
	 * <p>
	 * A further example: A tree that is the logical AND of one expression and
	 * the logical NOT of the same expression must be false. However, that can
	 * only be discovered by doing a deep comparison of the two sub-trees and
	 * therefore should not be done. A simple example of this is
	 * <code>A∧¬A</code>. In this case, although both sub-expressions are the
	 * same variable and are easy to compare, we should not make the evaluation
	 * to <code>⊥</code> because we should not make comparisons that two
	 * sub-expressions are equal
	 * </p>
	 * 
	 * @return <code>true</code> if this tree evaluates to <code>true</code>,
	 *         <code>false</code> if this tree evaluates to <code>false</code>
	 *         and <code>null</code> if this tree cannot be fully evaluated to
	 *         either <code>true</code> or <code>false</code> because of
	 */
	public Boolean evaluateConstantSubtrees();

	/**
	 * This takes the tree and executes all steps in
	 * the correct order to reduce it to Conjunctive Normal Form (CNF)
	 * 
	 * Note that it does NOT call evaluateConstantSubtrees(). This does not change the fact that
	 * the result is in CNF, it just means that it is perhaps larger than it need be. Feel free to call
	 * it at any time to simplify the results.
	 * 
	 * Note that if you call the contained methods below in a different order, you are not guaranteed
	 * to end up with an expression in CNF
	 */
	public void reduceToCNF();

	/**
	 * Recursively replace <strong>in place</strong> every occurrence of the
	 * pattern <code>x→y</code> with <code>¬x∨y</code>, for sub-trees
	 * <code>x</code> and <code>y</code>
	 */
	public void eliminateImplies();

	/**
	 * Recursively replace <strong>in place</strong>, for sub-trees
	 * <code>x</code> and <code>y</code>, every occurrence of:
	 * <ul>
	 * <li><code>¬¬x</code> with <code>x</code></li>
	 * <li><code>¬(x∨y)</code> with <code>(¬x∧¬y)</code></li>
	 * <li><code>¬(x∧y)</code> with <code>(¬x∨¬y)</code></li>
	 * </ul>
	 * Be careful of handling cases such as <code>¬¬¬¬¬x</code> correctly (it
	 * should be reduced to <code>¬x</code>)
	 */
	public void pushNotDown();

	/**
	 * Recursively replace <strong>in place</strong>, for sub-trees
	 * <code>x</code>, <code>y</code> and <code>z</code>, every occurrence of:
	 * <ul>
	 * <li><code>x∨(y∧z)</code> with <code>(x∨y)∧(x∨z)</code></li>
	 * <li><code>(x∧y)∨z</code> with <code>(x∨z)∧(y∨z)</code></li>
	 * </ul>
	 * This step is also knows as "distributing OR over AND"
	 * <p>
	 * Warning: since this requires replacing a pattern with one copy of a
	 * sub-tree with a different pattern with two copies of that sub-tree, and
	 * since these trees get modified in place, a <strong>deep copy</strong> of
	 * the sub-tree must be made so that the same actual sub-tree objects are
	 * not linked into from different places in the same tree
	 * </p>
	 */
	public void pushOrBelowAnd();

	/**
	 * Recursively replace <strong>in place</strong>, for sub-trees
	 * <code>W</code>, <code>X</code>, <code>Y</code> and <code>Z</code>, every
	 * occurrence of:
	 * <ul>
	 * <li><code>(X∨Y)∨Z</code> with <code>X∨(Y∨Z)</code></li>
	 * <li><code>(X∧Y)∧Z</code> with <code>X∧(Y∧Z)</code></li>
	 * </ul>
	 * This is the closest we can come in a binary tree representation to
	 * "flattening" the conjunctions and disjunctions as the last step in
	 * producing Conjunctive Normal Form. The result is to turn complex trees of
	 * nested conjunctions into a simple right deep tree of conjunctions and
	 * similarly for disjunctions Thus this will change:
	 * <ul>
	 * <li><code>(W∨X)∨(Y∨Z)</code> into <code>W∨(X∨(Y∨Z))</code></li>
	 * <li><code>((W∨X)∨Y)∨Z</code> into <code>W∨(X∨(Y∨Z))</code></li>
	 * </ul>
	 * 
	 */
	public void makeAndOrRightDeep();

}