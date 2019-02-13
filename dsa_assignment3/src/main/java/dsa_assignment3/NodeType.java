package dsa_assignment3;

/**
 * The NodeType enum provides the possible entries in a Propositional Logic expression.
 * They are immutable so can be passed around and assigned without concern. 
 * 
 * Using an enum like this instead of, say, Strings, makes it much easier to avoid having to put in lots of
 * error checking code: If, using Strings, you try to create a node, you have to make sure every time that
 * it is an allowable String. With these enums, the compiler complains if you give it an invalid enum value.
 * c.f. <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html">https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html</a>
 *
 */
/**
 * @author aps
 *
 */
public enum NodeType
{
	// Creating the enum values with their constructor calls:
	IMPLIES("implies", "\u2192", 2, false), // →
	AND(    "and",     "\u2227", 2, false), // ∧
	OR(     "or",      "\u2228", 2, false), // ∨
	NOT(    "not",     "\u00AC", 1, false), // ¬
	TRUE(   "true",    "\u22A4", 0, false), // ⊤
	FALSE(  "false",   "\u22A5", 0, false), // ⊥
	
	// the remainder are logical variables
	A(      "A",       "A",      0, true),
	B(      "B",       "B",      0, true),
	C(      "C",       "C",      0, true),
	D(      "D",       "D",      0, true),
	E(      "E",       "E",      0, true),
	F(      "F",       "F",      0, true),
	G(      "G",       "G",      0, true),
	H(      "H",       "H",      0, true),
	I(      "I",       "I",      0, true),
	J(      "J",       "J",      0, true),
	K(      "K",       "K",      0, true),
	L(      "L",       "L",      0, true),
	M(      "M",       "M",      0, true),
	N(      "N",       "N",      0, true),
	O(      "O",       "O",      0, true),
	P(      "P",       "P",      0, true),
	Q(      "Q",       "Q",      0, true),
	R(      "R",       "R",      0, true),
	S(      "S",       "S",      0, true),
	T(      "T",       "T",      0, true), // avoid using this: it looks like the "⊤" symbol for logical "true" 
	U(      "U",       "U",      0, true),
	V(      "V",       "V",      0, true),
	W(      "W",       "W",      0, true),
	X(      "X",       "X",      0, true),
	Y(      "Y",       "Y",      0, true),
	Z(      "Z",       "Z",      0, true);
	
	private String prefixName;
	private String infixName;
	private int arity;
	private boolean isVar;
	
	/**
	 * Construct an enum element: this cannot be called by the programmer - it is constructed internally by Java
	 * 
	 * @param prefixName The name that appears when printed in prefix notation format
	 * @param infixName The name that appears when printed in infix notation format
	 * @param arity The number of arguments that this propositional logic term takes
	 * @param isVar True if this element is a variable
	 */
	private NodeType(String prefixName, String infixName, int arity, boolean isVar)
	{
		this.prefixName = prefixName;
		this.infixName = infixName;
		this.arity = arity;
		this.isVar = isVar;
	}

	/**
	 * Getter for the prefix notation name
	 * 
	 * @return the name to be used in prefix notation
	 */
	public String getPrefixName()
	{
		return prefixName;
	}

	/**
	 * Getter for the infix notation name
	 * 
	 * @return the name to be used in infix notation
	 */
	public String getInfixName()
	{
		return infixName;
	}

	/**
	 * Getter for the arity of this term, i.e. the number of arguments, that this propositional logic term takes
	 * 
	 * @return The number of arguments that this propositional logic term takes
	 */
	public int getArity()
	{
		return arity;
	}

	/**
	 * True if this element is a propositional logic variable
	 * @return True if this element is a propositional logic variable
	 */
	public boolean isVar()
	{
		return isVar;
	}
}
