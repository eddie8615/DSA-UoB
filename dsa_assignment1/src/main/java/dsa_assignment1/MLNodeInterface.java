package dsa_assignment1;

/**
 * 
 * A Multi-Linked list node class interface. Each object of this type is a node which has two
 * independent pairs of forward and backward pointers and thus can be on two independent
 * lists (list1 and list2) at the same time
 * 
 * @param <E> The type of base element contained in this data structure: normally a String or Integer
 */
public interface MLNodeInterface<E>
{

	/**
	 * Remove this node from list1, list2 is not affected, this nodes next1 and prev1 references are reset to this node 
	 * @return this node
	 */
	MLNodeInterface<E> remove1();

	/**
	 * Remove this node from list2, list1 is not affected, this nodes next2 and prev2 references are reset to this node 
	 * @return this node
	 */
	MLNodeInterface<E> remove2();

	/**
	 * Inserts this node onto list1 immediately after <code>target</code>,
	 * after first removing it from its original position in list1
	 * @param target the node after which this node should be inserted on list1
	 * @return this node
	 */
	MLNodeInterface<E> addAfter1(MLNodeInterface<E> target);

	/**
	 * Inserts this node onto list2 immediately after <code>target</code>,
	 * after first removing it from its original position in list2
	 * @param target the node after which this node should be inserted on list2
	 * @return this node
	 */
	MLNodeInterface<E> addAfter2(MLNodeInterface<E> target);

	/**
	 * Inserts this node onto list1 immediately before <code>target</code>,
	 * after first removing it from its original position in list1
	 * @param target the node before which this node should be inserted on list1
	 * @return this node
	 */
	MLNodeInterface<E> addBefore1(MLNodeInterface<E> target);

	/**
	 * Inserts this node onto list2 immediately before <code>target</code>,
	 * after first removing it from its original position in list2
	 * @param target the node before which this node should be inserted on list2
	 * @return this node
	 */
	MLNodeInterface<E> addBefore2(MLNodeInterface<E> target);

	/**
	 * Gets the element stored in this node
	 * @return the element
	 */
	E getElement();

	/**
	 * Gets the next node on list1 after this node
	 * @return the next node
	 */
	MLNodeInterface<E> getNext1();

	/**
	 * Gets the previous node on list1 before this node
	 * @return the previous node
	 */
	MLNodeInterface<E> getPrev1();

	/**
	 * Gets the next node on list2 after this node
	 * @return the next node
	 */
	MLNodeInterface<E> getNext2();

	/**
	 * Gets the previous node on list2 before this node
	 * @return the previous node
	 */
	MLNodeInterface<E> getPrev2();
	
	/**
	 * Primitive setter to set the next list1 pointer on this node to next1
	 * @param next1 the node to be pointed to
	 */
	void setNext1(MLNodeInterface<E> next1);

	/**
	 * Primitive setter to set the previous list1 pointer on this node to prev1
	 * @param prev1 the node to be pointed to
	 */
	void setPrev1(MLNodeInterface<E> prev1);

	
	/**
	 * Primitive setter to set the next list2 pointer on this node to next2
	 * @param next2 the node to be pointed to
	 */
	void setNext2(MLNodeInterface<E> next2);

	/**
	 * Primitive setter to set the previous list2 pointer on this node to prev2
	 * @param prev2 the node to be pointed to
	 */
	void setPrev2(MLNodeInterface<E> prev2);

}