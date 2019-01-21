package dsa_assignment1;

/**
 * A multi-linked list interface of a class that implements an ordered list and a most
 * recently used (MRU) list, based on the <code>MLNodeInterface</code>, where list1 is used
 * for the ordered list and list2 is used for the most recently used list.
 * 
 *  
 *  @param <E> The class type of base element contained in this data structure: normally a String or Integer.
 *  This class must implement the <code>Comparable</code> interface to represent the order used in the
 *  ordered list
 */
public interface OrderedMruListInterface<E extends Comparable<E>>
{
	/**
	 * Check whether the ordered list is empty
	 * @return <code>Boolean.TRUE</code> if the ordered list is empty, else <code>Boolean.FALSE</code>
	 */
	boolean isEmptyOrdered();

	/**
	 * Check whether the MRU list is empty
	 * @return <code>Boolean.TRUE</code> if the MRU list is empty, else <code>Boolean.FALSE</code>
	 */
	boolean isEmptyMru();

	/**
	 * Removes the target node from its current position in the MRU list and puts it at the front
	 * of the MRU list. The ordered list is unaffected.
	 * @param target the node to move
	 * @return this <code>OrderedMruListInterface</code> object, so that further calls on it can be chained
	 */
	OrderedMruListInterface<E> touch(MLNodeInterface<E> target);

	/**
	 * Get the first node on the MRU list
	 * @return the first node of the MRU list, or <code>null</code> if the list is empty 
	 */
	MLNodeInterface<E> getFirstMru();

	/**
	 * Get the first node on the ordered list
	 * @return the first node of the ordered list, or <code>null</code> if the list is empty 
	 */
	MLNodeInterface<E> getFirstOrdered();

	/**
	 * Get the next node in order after the <code>current</code> node on the ordered list
	 * @param current the node on the ordered list preceeding the one to return
	 * @return the next ordered node, or <code>null</code> if there is none 
	 */
	MLNodeInterface<E> getNextOrdered(MLNodeInterface<E> current);

	/**
	 * Get the next node on the MRU list after the <code>current</code> node
	 * @param current the node on the MRU list preceeding the one to return
	 * @return the next MRU node, or <code>null</code> if there is none 
	 */
	MLNodeInterface<E> getNextMru(MLNodeInterface<E> current);

	/**
	 * Remove the <code>target</code> node from both ordered and MRU lists
	 * @param target the node to remove
	 * @return this <code>OrderedMruListInterface</code> object, so that further calls on it can be chained
	 */
	OrderedMruListInterface<E> remove(MLNodeInterface<E> target);

	/**
	 * Add an element of the base element class type in a new node to the list.
	 * The new node is added to the ordered list in the correct order as defined
	 * by <code>E</code>'s implemented <code>Comparable</code>interface
	 * and is added to the front of the MRU list
	 * @param element The element to be wrapped in a new <code>MLNodeInterface</code> node and added to the list
	 * @return this <code>OrderedMruListInterface</code> object, so that further calls on it can be chained
	 */
	OrderedMruListInterface<E> add(E element);

}