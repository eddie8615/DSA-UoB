package dsa_assignment2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class DroneTest
{
	private static final Logger logger        = Logger.getLogger(DroneTest.class);

	private static int			numChambers		= 4 ;
	private static double		avgDoorsPerChamber = 2.3;
	
	private static Maze         baseMaze      = new Maze(numChambers, avgDoorsPerChamber, 999);


	@Rule
	public Timeout              globalTimeout = new Timeout(2000, TimeUnit.MILLISECONDS);

	/**
	 * Not a test. This is just to log the maze used
	 * To disable, turn the "BeforeClass" into "Ignore"
	 */
	@BeforeClass
	public static void logMazeAndVisitsAndBackPath()
	{
		Maze maze = new Maze(baseMaze, true);
		logger.debug("Maze in toString format:\n" + maze);
		logger.debug("Maze in dot format:\n" + maze.toDotFormat());
	}
	
	

	/**
	 * Not a test. This is just to log the visits and back paths at each step in the execution
	 * of a search.
	 * To disable, turn the "BeforeClass" into "Ignore"
	 */
	@BeforeClass
	public static void logDroneVisitsAndBackPath()
	{
		Maze maze = new Maze(baseMaze, true);
		DroneInterface drone = new Drone(maze);

		Portal[] visitOrder = null;
		Portal[] pathBack = null;
		logger.trace("Drone search steps log Begin:");

		while (drone.searchStep() != null)
		{
			visitOrder = drone.getVisitOrder();
			pathBack = drone.findPathBack();
			logger.trace("Current chamber: " + maze.getCurrentChamber() + //
					"\nVisit order: " + Arrays.toString(visitOrder) + //
					"\nPath back:   " + Arrays.toString(pathBack));
		}
		logger.trace("Drone search steps log End");
		//assertSame("Drone: Finished search", null, portal);
	}

	@Test
	public void testCheckStudentIdentification()
	{
		Maze maze = new Maze(baseMaze, true);
		Drone drone = new Drone(maze);
		assertNotEquals("Please update the studentID field in Drone.java with your student id number", //
				"MY STUDENT ID", drone.getStudentID());
		assertNotEquals("Please update the studentName field in Drone.java with your name", //
				"MY NAME", drone.getStudentID());
	}

	@Test
	public void testDroneConstruction()
	{
		Maze maze = new Maze(baseMaze, true);

		DroneInterface drone = new Drone(maze);
		assertNotNull("Drone created successfully", drone);
	}

	@Test
	public void testDroneOneStep()
	{
		Maze maze = new Maze(baseMaze, true);
		DroneInterface drone = new Drone(maze);

		Portal portal = drone.searchStep();
		assertNotNull("Result of single drone search step", portal);
		assertEquals("Result chamber of single drone search step", maze.getCurrentChamber(), portal.getChamber());

		portal = maze.traverse(portal.getDoor());
		assertNotNull("Result of reversing single drone search step", portal);
		assertEquals("Result chamber of reversing single drone search step", 0, maze.getCurrentChamber());
	}
	
	@Test
	public void testDroneSearch()
	{
		Maze maze = new Maze(baseMaze, true);

		DroneInterface drone = new Drone(maze);
		Portal portal = null;
		int prevChamber = 0; // for checking that the drone actual moves each step
		int numSteps = 0;
		
		// This is so that we can tick off each chamber in the maze that we have to visit
		Set<Integer> chambersToVisit = new HashSet<>(numChambers);
		// Add all the chambers in the maze to the set of chambers that must be visited
		for (int i = 0 ; i < numChambers ; i++)
			chambersToVisit.add(i);
		
		// To record the set of all Portals that we enter a chamber through
		// Since we pass through each portal once to enter each chamber (and once to leave it)
		// this is sufficient to record every portal in the maze so long as we enter every
		// chamber in maze
		Set<Portal> portalSet = new HashSet<>();
		
		// for counting the total number of portals in the maze
		int portalCount = 0;
		
		// Note: chamber 0 is covered (with respect to checking visits and portals), not on
		// the first iteration, but at least on the last, if not before
		
		while ((portal = drone.searchStep()) != null)
		{
			numSteps++;
			assertEquals("Drone search current chamber", maze.getCurrentChamber(), portal.getChamber());
			assertNotEquals("Drone search chamber hasn't changed", prevChamber, maze.getCurrentChamber());
			prevChamber = maze.getCurrentChamber();

			// Have now visited this chamber: remove it from the set of chambers to visit and,
			// if its the first time we visit this chamber, add in the number of doors
			// of this chamber to the total portal count.
			if (chambersToVisit.remove(prevChamber))
				portalCount += maze.getNumDoors() ;
			
			// add the portal we arrived through to the set of all portals. Since we pass
			// through each passage in both directions, that will get all the portals
			portalSet.add(portal);
		}
		assertEquals("A completed search must visit every chamber: number of chambers unvisited", 0, chambersToVisit.size());
		assertEquals("A completed search must pass through every portal: number of portals used", portalCount, portalSet.size());
		assertEquals("A completed search must terminate in chamber 0", 0, maze.getCurrentChamber());
		assertNotEquals("A search of a Maze with 2 or more chambers must take more than 0 steps", 0, numSteps);
		assertSame("Drone: Finished search", null, portal);
	}

	@Test
	public void testDroneFinalVisitOrder()
	{
		Maze maze = new Maze(baseMaze, true);
		Maze mazeCopy;

		DroneInterface drone = new Drone(maze);

		Portal portal = null;
		while ((portal = drone.searchStep()) != null)
		{
		}
		assertSame("Drone: Finished search", null, portal);

		Portal[] visitOrder = drone.getVisitOrder();
		
		// New maze copy, but reset the current chamber to 0:
		mazeCopy = new Maze(maze, true);

		assertNotNull("The visit order must not be null", visitOrder);
		assertNotEquals("The visit order must not be empty", 0, visitOrder.length);
		assertTrue("The visit order sequence must have a length that is a multiple of 2", visitOrder.length % 2 == 0);
		assertEquals("The visit order must start in chamber 0", 0, visitOrder[0].getChamber());

		for (int i = 0; i < visitOrder.length; i += 2)
		{
			Portal srcPortal = visitOrder[i];
			Portal dstPortal = visitOrder[i + 1];
			portal = mazeCopy.traverse(srcPortal.getDoor());
			assertEquals("Error in visitOrder: wrong arrival portal when traversing from " + srcPortal, dstPortal, portal);
		}
		assertEquals("The visit order must end in chamber 0", 0, visitOrder[visitOrder.length - 1].getChamber());
		assertEquals("Following the full visit order must end in chamber 0", 0, mazeCopy.getCurrentChamber());
	}

	
	@Test
	public void testDroneAllVisitOrders()
	{
		Maze maze = new Maze(baseMaze, true);
		Maze mazeCopy;

		DroneInterface drone = new Drone(maze);

		Portal portal = null;
		Portal[] visitOrder = null;
		while ((portal = drone.searchStep()) != null)
		{
			visitOrder = drone.getVisitOrder();
			
			// New maze copy, but reset the current chamber to 0:
			mazeCopy = new Maze(maze, true);

			assertNotNull("The visit order must not be null", visitOrder);
			assertNotEquals("The visit order must not be empty", 0, visitOrder.length);
			assertTrue("The visit order sequence must have a length that is a multiple of 2", visitOrder.length % 2 == 0);
			assertEquals("The visit order must start in chamber 0", 0, visitOrder[0].getChamber());

			for (int i = 0; i < visitOrder.length; i += 2)
			{
				Portal srcPortal = visitOrder[i];
				Portal dstPortal = visitOrder[i + 1];
				portal = mazeCopy.traverse(srcPortal.getDoor());
				assertEquals("Error in visitOrder: wrong arrival portal when traversing from " + srcPortal, dstPortal, portal);
			}
		}
		assertSame("Drone: Finished search", null, portal);

		visitOrder = drone.getVisitOrder();
		assertNotNull("The visit order must not be null", visitOrder);
		assertNotEquals("The visit order must not be empty", 0, visitOrder.length);
		assertTrue("The visit order sequence must have a length that is a multiple of 2", visitOrder.length % 2 == 0);
		assertEquals("The visit order must start in chamber 0", 0, visitOrder[0].getChamber());
		assertEquals("The visit order must end in chamber 0", 0, visitOrder[visitOrder.length - 1].getChamber());
	}

	
	@Test
	public void testDroneSimpleBackPath()
	{
		Maze maze = new Maze(baseMaze, true);
		Maze mazeCopy;
		DroneInterface drone = new Drone(maze);
		Portal portal = null;
		Portal[] pathBack = null;

		int numSteps = 0;
		while ((portal = drone.searchStep()) != null)
		{
			numSteps++;
			pathBack = drone.findPathBack();
			mazeCopy = new Maze(maze, false); // Don't reset the current chamber
			for (int i = 0; i < pathBack.length; i++)
			{
				Portal srcPortal = pathBack[i];
				assertEquals("Pathback: not in correct chamber", mazeCopy.getCurrentChamber(), srcPortal.getChamber());
				assertNotNull("Result of a traverse step in pathBack", mazeCopy.traverse(srcPortal.getDoor()));
			}
			assertEquals("Following the full path back must end in chamber 0", 0, mazeCopy.getCurrentChamber());
		}
		assertNotEquals("A search of a Maze with 2 or more chambers must take more than 0 steps", 0, numSteps);
		assertSame("Drone: Finished search", null, portal);
	}

	@Test
	public void testDroneBackPathNoLoops()
	{
		Maze maze = new Maze(baseMaze, true);
		Maze mazeCopy;
		DroneInterface drone = new Drone(maze);
		Portal portal = null;
		Portal[] pathBack = null;
		Set<Integer> chamberSet = new HashSet<>();

		int numSteps = 0 ;
		while ((portal = drone.searchStep()) != null)
		{
			numSteps++ ;
			pathBack = drone.findPathBack();
			mazeCopy = new Maze(maze, false); // Don't reset the current chamber
			chamberSet.clear();
			for (int i = 0; i < pathBack.length; i++)
			{
				Portal srcPortal = pathBack[i];
				assertEquals("Pathback: not in correct chamber", mazeCopy.getCurrentChamber(), srcPortal.getChamber());
				assertNotNull("Result of a traverse step in pathBack", mazeCopy.traverse(srcPortal.getDoor()));

				assertTrue("Second time visiting this chamber while following path back", chamberSet.add(srcPortal.getChamber()));
			}
			assertTrue("Already visited chamber 0 before finished following back path", chamberSet.add(0));
			
			assertEquals("Following the full path back must end in chamber 0", 0, mazeCopy.getCurrentChamber());
		}
		assertNotEquals("A search of a Maze with 2 or more chambers must take more than 0 steps", 0, numSteps);
		assertSame("Drone: Finished search", null, portal);
	}

}
