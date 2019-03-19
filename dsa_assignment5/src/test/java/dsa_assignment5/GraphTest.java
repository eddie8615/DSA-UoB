package dsa_assignment5;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;

/**
 * You need to have the JUnitParams-1.1.1 library installed to make this work.
 * <p>
 * You can download that library for your own machines from <a href="https://www.cs.bham.ac.uk/~aps/javalibs">https://www.cs.bham.ac.uk/~aps/javalibs</a>
 * </p>
 * <p>
 * On the lab machines these are already installed at <pre>/bham/pd/packages/java/1.8.0/lib</pre>
 * </p>
 *
 */
@RunWith(JUnitParamsRunner.class)
public class GraphTest
{
	private static final Logger logger        = Logger.getLogger(GraphTest.class);

	@Rule
	public Timeout              globalTimeout = new Timeout(2000, TimeUnit.MILLISECONDS);

	@Test
	public void testCheckStudentIdentification()
	{
		assertNotEquals("Please update the studentID field in CsvUtils.java with your student id number", //
				"MY STUDENT ID", Graph.getStudentID());
		assertNotEquals("Please update the studentName field in CsvUtils.java with your name", //
				"MY NAME", Graph.getStudentName());
	}

	/**
	 * A simple sorter that sorts edges (arrays of node1, node2, distance) to make more readable log messages
	 *
	 */
	private static class ConnectionSorter implements Comparator<int[]>
	{
		public int compare(int[] a, int[] b)
		{
			for (int i = 0; i < 3; i++)
			{
				if (a[i] < b[i])
					return -1;
				if (a[i] > b[i])
					return 1;
			}
			return 0;
		}
	}

	/**
	 * A prebuilt connectionSorter that can be re-used without requiring the need
	 * to recreate it multiple times
	 */
	ConnectionSorter connectionSorter = new ConnectionSorter();

	/**
	 * Sorts, in place, an array of edges, where each edge is an array of node1, node2, distance.
	 * <p>
	 * This is for display purposes only
	 * </p>
	 * @param edges an array of edges
	 */
	private void sort(int[][] edges)
	{
		Arrays.sort(edges, connectionSorter);
	}

	/**
	 * When we create an graph from an input array of edges, and then get the connections
	 * back from the graph, the graph, during construction, will have added the reverse
	 * reverse connections. For testing purposes we need to be able to add the reverse
	 * connections to the input array of edges to check that this corresponds to the
	 * edges returned from the graph. This method does that.
	 * @param connections the input array of edges
	 * @return an array of edges that has the input array of edges plus their reverse edges
	 */
	private int[][] addReverseConnections(int[][] connections)
	{
		ArrayList<int[]> cList = new ArrayList<>(connections.length * 2);
		Collections.addAll(cList, connections);

		for (int[] connection : connections)
			cList.add(new int[] { connection[1], connection[0], connection[2] });

		return cList.toArray(new int[0][0]);
	}

	@Test
	public void testConstruction()
		throws GraphException
	{
		int[][] input = new int[][] { //
				{ 0, 1, 5 }, //
				{ 1, 2, 7 }, //
				{ 2, 3, 11 }, //
				{ 0, 2, 9 }, //
				{ 0, 3, 6 } };

		Graph g = new Graph(input);

		int[][] expectedOutput = addReverseConnections(input);
		int[][] output = g.getConnections();

		assertThat(expectedOutput, arrayContainingInAnyOrder(output));

	}

	@Test
	public void testContraction()
		throws GraphException
	{
		int[][] input = new int[][] { //
				{ 0, 1, 5 }, //
				{ 1, 2, 7 }, //
				{ 2, 3, 11 }, //
				{ 0, 2, 9 }, //
				{ 0, 3, 6 } };

		Graph g = new Graph(input);

		g.contractNodeWithTwoEdges(3);

		int[][] expectedOutput = new int[][] { //
				{ 0, 1, 5 }, //
				{ 1, 2, 7 }, //
				{ 0, 2, 9 }, //
				{ 0, 2, 17 } //
		};
		expectedOutput = addReverseConnections(expectedOutput);
		int[][] output = g.getConnections();

		// Uncomment the following if you would like the lists in the test failure
		// messages to be ordered for readability and debugging reasons 
		// the actual assert does not need them to be ordered to work correctly
		//		sort(expectedOutput);
		//		sort(output);

		assertThat(output, arrayContainingInAnyOrder(expectedOutput));
	}

	@Test(expected = dsa_assignment5.GraphException.class)
	public void testBadContraction()
		throws GraphException
	{
		int[][] input = new int[][] { //
				{ 0, 1, 5 }, //
				{ 1, 2, 7 }, //
				{ 2, 3, 11 }, //
				{ 0, 2, 9 }, //
				{ 0, 3, 6 } };

		Graph g = new Graph(input);

		// Should throw GraphException because node has 3 edges
		g.contractNodeWithTwoEdges(0);

	}

	/**
	 * THis method is used to feed parameters into multiple test calls of {@code testDijkstra(...)}.
	 * It returns an array of arrays of parameters. The junitParam framework uses each array element of the
	 * whole array as the set of parameters of testDijkstra(...) to use in each test. The annotations of the
	 * {code @GraphTest} class and of @code testDijkstra(...)} wire this together.
	 * @return An array of one array of parameters for each test
	 */
	private Object[] dijkstraParams()
	{
		Object[][] typeListList = { //
				{ 0, 1, 3 }, //
				{ 0, 2, 8 }, //
				{ 0, 3, 2 }, //
				{ 0, 4, 9 }, //
				{ 0, 5, 12 } //
		};
		return typeListList;
	}

	@Test
	@Parameters(method = "dijkstraParams")
	@TestCaseName("{method}({params}) [{index}]") //can use {0} {1}... for params from current param set
	public void testDijkstra(int node1, int node2, int expectedDistance)
		throws GraphException
	{
		int[][] input = new int[][] { //
				{ 0, 1, 4 }, //
				{ 0, 3, 2 }, //
				{ 1, 2, 5 }, //
				{ 1, 3, 1 }, //
				{ 2, 3, 8 }, //
				{ 2, 4, 1 }, //
				{ 2, 5, 6 }, //
				{ 3, 4, 9 }, //
				{ 4, 5, 3 } //
		};

		int[][] inputGraph = addReverseConnections(input);

		Graph g = new Graph(input);

		int distance = g.dijkstra(node1, node2);

		assertThat(distance, is(expectedDistance));

		int[][] output = g.getConnections();

		// Uncomment the following if you would like the lists in the test failure
		// messages to be ordered for readability and debugging reasons 
		// the actual assert does not need them to be ordered to work correctly
		//		sort(expectedOutput);
		//		sort(output);

		// check that the call dijkstra(...) has not modified the graph
		assertThat(output, arrayContainingInAnyOrder(inputGraph));
	}

	@Test(expected = dsa_assignment5.GraphException.class)
	public void testDijkstraException()
		throws GraphException
	{
		int[][] input = new int[][] { //
				{ 0, 1, 4 } //
		};

		Graph g = new Graph(input);

		// no such node in the graph so should throw exception
		g.dijkstra(0, 3);

	}

}
