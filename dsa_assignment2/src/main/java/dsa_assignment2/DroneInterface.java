package dsa_assignment2;

public interface DroneInterface
{

	/**
	 * Executes one step in a maze search, i.e. gets the drone to choose a
	 * portal out of the current chamber and traverse, via that passageway, into
	 * the next chamber, returning the portal by which the drone enters the new
	 * chamber. When the drone has successfully searched the entire maze, it
	 * should have returned to the start chamber (chamber 0), after which any
	 * further calls to this method should return null. 
	 * <p>
	 * This should be one step in an organised search of the maze. By executing
	 * a sequence of calls to searchStep(), until it returns null, the drone
	 * should traverse through the entire maze, always trying a portal that it
	 * has not traversed before until there are no such unused portals left in
	 * the current chamber, upon which it backtracks through the portal it
	 * originally entered that chamber by.
	 * </p>
	 * @return The portal by which the drone enters the chamber it ends up at
	 *         the end of this step, or null, if it has already returned to
	 *         chamber 0 after fully exploring the maze and is asked to take
	 *         further steps.
	 */
	Portal searchStep();

	/**
	 * Ask for an array of the full sequence of Portal objects that represent
	 * the doorways that the Drone has passed through in the order they were
	 * taken, either in entering or exiting a chamber. Note that this list shows
	 * the full path that the drone has taken since starting at chamber 0 up to
	 * where it is now.
	 * <p>
	 * The array returned should NOT be a reference to an internal class
	 * variable of Drone: doing so would allow other classes to modify the
	 * internal class variables of Drone and must not be allowed. Instead, it
	 * should return a new array with a copy of all elements on the appropriate
	 * internal class variable.
	 * </p>
	 * @return A new array with a copy of the sequence of Portals passed through
	 *         by the Drone from the first doorway in chamber 0 to the last
	 *         doorway it passed through to enter the current chamber.
	 */
	Portal[] getVisitOrder();

	/**
	 * Gets the sequence of portals to pass through to traverse through the
	 * necessary chambers to get back to the starting chamber (chamber 0) from
	 * the current position in the maze. Only the portals taken to exit each
	 * chamber should be included. The portals by which a chamber is entered
	 * should NOT be included. The sequence should be taken as the instructions
	 * on which portals to take in the correct order to return to the start
	 * chamber.
	 * <p>
	 * No chamber should be visited more than once on the path back: no loops
	 * and no taking wrong turns or entering dead ends that require back
	 * tracking.
	 * </p>
	 * <p>
	 * Please note: there is no need to implement a "Shortest Path" algorithm
	 * such as Dijkstra's algorithm which you may have heard about in your
	 * Artificial Intelligence module (if you are taking it). Simply remove any
	 * sub-sequences of moves that start and end in the same chamber and ensure
	 * that, if your sequence passes through chamber 0, it stops there.
	 * </p>
	 * @return An array of portals such that element 0 in the array is the first
	 *         portal, to take on the way out from the current location in the
	 *         maze, element 1 is the second portal to take etc. If the Drones
	 *         current position is already in chamber 0 then this array should
	 *         have length 0.
	 */
	Portal[] findPathBack();

}