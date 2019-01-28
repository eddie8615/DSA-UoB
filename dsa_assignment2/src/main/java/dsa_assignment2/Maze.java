package dsa_assignment2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

/**
 * A Maze class to simulate being in an underground maze
 * 
 */
public class Maze
{

	/**
	 * Records the connectivity of the chambers: which doorway in which chamber
	 * connects to which doorway in which other chamber. Note that if there is
	 * an entry in this map for portal A to be connected to portal B, then there
	 * is also a second entry for B to be connected to A.
	 */
	private Map<Portal, Portal> mazeMap            = null;

	/**
	 * Records the number of doors in each chamber
	 */
	private int[]               numDoorsPerChamber = null;

	/**
	 * The number of the chamber that the drone is currently in
	 */
	private int                 currentChamber     = 0;

	/**
	 * Simulates following a move from a chamber through a doorway. When the
	 * maze is created, you are located in chamber 0.
	 * 
	 * @param door
	 *            the number of the door that is used to exit the current
	 *            chamber
	 * @return the portal you come out through at the other end of the passage
	 *         (consisting of the chamber and the doorway in that chamber that
	 *         you came through), or null if there is no such door in the
	 *         current chamber
	 */
	public Portal traverse(int door)
	{

		Portal newPortal = mazeMap.get(new Portal(currentChamber, door));
		if (newPortal == null)
			return null;
		currentChamber = newPortal.getChamber();
		return newPortal;
	}

	/**
	 * Gets the number of the chamber that you are currently in.
	 * 
	 * @return The number of the chamber that you are currently in
	 */
	public int getCurrentChamber()
	{
		return currentChamber;
	}

	/**
	 * Simulates looking around to see the number of doors in the current
	 * chamber.
	 * 
	 * @return the number of doors in the current chamber. The doors of the
	 *         current chamber are numbered from 0 to this number.
	 */
	public int getNumDoors()
	{
		return numDoorsPerChamber[currentChamber];
	}

	/**
	 * Construct a new maze as a copy of an existing one
	 * 
	 * <p>
	 * Mostly for use in debugging and testing. The new maze is an exact copy of
	 * the argument maze except that the current chamber is reset to 0 if
	 * resetCurrentChamber is true, otherwise the current chamber remains what
	 * it was.
	 * </p>
	 * <p>
	 * In fact, the other class variables are shared with the old maze (so this
	 * copy is very cheap) but that does not cause a problem because after
	 * creation, these internal class variables are never modified (other than
	 * the currentChamber)
	 * </p>
	 * @param maze
	 *            The maze that is being copied
	 * @param resetCurrentChamber
	 *            A boolean that, if true, causes the current chamber in the new maze
	 *            to be reset to 0. If false, its value is copied from the parameter maze
	 */
	public Maze(Maze maze, Boolean resetCurrentChamber)
	{
		if (maze == null)
			throw new IllegalArgumentException("Error: tried to construct a copy of a null Maze");
		this.mazeMap = maze.mazeMap;
		this.numDoorsPerChamber = maze.numDoorsPerChamber;
		if (resetCurrentChamber)
			this.currentChamber = 0;
		else
			this.currentChamber = maze.currentChamber;
	}

	/**
	 * The default Maze constructor that creates a random maze with 10 chambers
	 * and an average of 2.5 doors per chamber
	 */
	public Maze()
	{
		Random random = new Random();
		setRandom(10, 2.5, random);
	}

	/**
	 * A constructor to create a random maze
	 * 
	 * @param numChambers
	 *            The number of chambers to make in the maze
	 * @param avgDoorsPerChamber
	 *            The average number of doors per chamber
	 */
	public Maze(int numChambers, double avgDoorsPerChamber)
	{
		Random random = new Random();
		setRandom(numChambers, avgDoorsPerChamber, random);
	}

	/**
	 * 
	 * A constructor to create a random maze using a specific random number
	 * generator seed
	 * 
	 * @param numChambers
	 *            The number of chambers to make in the maze
	 * @param avgDoorsPerChamber
	 *            The average number of doors per chamber
	 * @param randomSeed
	 *            An integer that seeds the random number generator used: if two
	 *            mazes are generated with the same parameters and the same
	 *            random seed, they should be the same
	 */
	public Maze(int numChambers, double avgDoorsPerChamber, int randomSeed)
	{
		Random random = new Random(randomSeed);
		setRandom(numChambers, avgDoorsPerChamber, random);
	}

	/**
	 * Generate a string representation of this maze. Useful for debug purposes
	 * when included in log messages.
	 * 
	 * @return a String showing the connections in the maze
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		for (Entry<Portal, Portal> connection : mazeMap.entrySet())
		{
			Portal src = connection.getKey();
			Portal dst = connection.getValue();

			if (src.getChamber() < dst.getChamber())
				sb.append(src).append(" -- ").append(dst).append("\n");
		}
		return sb.toString();
	}

	/**
	 * Generate a "dot" format representation of this maze. If the contents of
	 * this string are saved as a ".gv" file, then the graphvis package can be
	 * used to generate a graphical image of the maze: use the command:
	 * <pre>
	 * <code>neato -Tpng -ox.png x.gv</code>
	 * </pre>
	 * to generate x.png from x.gv if you have the graphvis package installed.
	 * It is already installed on the school lab machines, available as a
	 * package in Ubuntu linux, otherwise available for Macs and Windows from:
	 * <a href="https://www.graphviz.org">https://www.graphviz.org</a>
	 * 
	 * @return the "dot" format representation of this maze
	 */
	public String toDotFormat()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("// Use the graphvis package to generate an image. Put this output in x.gv and run:\n" + //
				"// neato -Tpng -ox.png x.gv\n" + //
				"// to generate the x.png with the image of the connections. Alternatively, use\n" + //
				"// \"fdp\" or \"circo\" with the same arguments to get a different layout\n" + //
				"strict graph G {\n" + //
				"  size=\"6,6!\" dpi=100 splines=true ratio=fill margin=0\n" + //
				"  edge[penwidth=0.2 color=blue forcelabels=true labelangle=0]\n" + //
				"  node[shape=circle margin=0.02 fixedsize=false\n" + //
				"       width=0.25 style=filled fontsize=12 color=lightblue]\n");

		for (Entry<Portal, Portal> connection : mazeMap.entrySet())
		{
			Portal src = connection.getKey();
			Portal dst = connection.getValue();

			if (src.getChamber() < dst.getChamber())
				sb.append(String.format("  %d -- %d [taillabel=%d headlabel=%s]\n", src.getChamber(), dst.getChamber(), src.getDoor(),
						dst.getDoor()));
		}
		sb.append("}\n");
		return sb.toString();
	}

	/**
	 * Setup a random maze with the given parameters.
	 * <p>
	 * This method is private as maze layouts should be immutable. It is only called from
	 * the constructors.
	 * </p>
	 * @param numChambers
	 *            The number of chambers to make in the maze
	 * @param avgDoorsPerChamber
	 *            The average number of doors per chamber
	 * @param random
	 *            The Random number generator to use in generating the maze
	 */
	private void setRandom(int numChambers, double avgDoorsPerChamber, Random random)
	{

		Map<Portal, Portal> pmap = new HashMap<>();

		// Start by generating a random spanning tree

		List<Integer> usedChambers = new ArrayList<>(numChambers);
		List<Integer> unusedChambers = new ArrayList<>(numChambers);
		for (int i = 0; i < numChambers; i++)
			unusedChambers.add(i);

		usedChambers.add(unusedChambers.remove(0));

		int[] nDoorsPerChamber = new int[numChambers];
		for (int i = 0; i < numChambers; i++)
			nDoorsPerChamber[i] = 0;

		while (unusedChambers.size() > 0)
		{
			// Choose a random used chamber as the source of a new passage
			int srcChamberIdx = random.nextInt(usedChambers.size());
			int srcChamber = usedChambers.get(srcChamberIdx);
			int srcDoor = nDoorsPerChamber[srcChamber]++;
			Portal src = new Portal(srcChamber, srcDoor);

			// Choose a random unused chamber as the destination of a new passage
			int dstChamber = unusedChambers.remove(random.nextInt(unusedChambers.size()));
			int dstDoor = nDoorsPerChamber[dstChamber]++;
			Portal dst = new Portal(dstChamber, dstDoor);

			usedChambers.add(dstChamber);

			pmap.put(src, dst);
			pmap.put(dst, src);
		}

		// now add remaining connections while avoiding duplicates
		// there are numChamber chambers, therefore numChamber*numChamber possible
		// connections between chambers that do not connect to themselves.
		// Thus a single number 0 <= n < (numChambers * numChambers) can represent a connection:
		// n means a connection from chamber (n/numChambers) to chamber (n%numChamber)
		// Although we want a set of this connections, we use a list as we want to be able to 
		// select random elements and Set<> does not give us that option
		// Rather than adding both the a->b and the b->a connections, we just add the one from
		// the lower numbered chamber to the higher numbered one to represent both.
		Set<Integer> connectionSet = new HashSet<Integer>();
		for (int src = 0; src < numChambers - 1; src++)
		{
			for (int dst = src + 1; dst < numChambers; dst++)
				connectionSet.add(src * numChambers + dst);
		}
		for (Entry<Portal, Portal> connection : pmap.entrySet())
		{
			int src = connection.getKey().getChamber();
			int dst = connection.getValue().getChamber();

			if (src < dst)
				connectionSet.remove(src * numChambers + dst);

		}
		ArrayList<Integer> connects = new ArrayList<>(connectionSet);

		while (connects.size() > 0 && pmap.size() < (int) (numChambers * avgDoorsPerChamber))
		{
			int con = connects.remove(random.nextInt(connects.size()));
			int srcChamber = con / numChambers;
			int dstChamber = con % numChambers;
			int srcDoor = nDoorsPerChamber[srcChamber]++;
			int dstDoor = nDoorsPerChamber[dstChamber]++;

			Portal src = new Portal(srcChamber, srcDoor);
			Portal dst = new Portal(dstChamber, dstDoor);
			pmap.put(src, dst);
			pmap.put(dst, src);
		}

		this.mazeMap = pmap;
		this.numDoorsPerChamber = nDoorsPerChamber;
		return;
	}

}
