package dsa_assignment5;

/**
 * A class to represent a connection in an adjacency list representation of a
 * weighted undirected graph. Each node X in the graph has a list of connections,
 * where the node Y of the connection is connected to node X with weight (or distance)
 * given by the distance of this connection.
 *
 */
public class Connection
{
	private int node;     // node of this connection
	private int distance; // distance to this node

	public Connection(int node, int distance)
	{
		this.node = node;
		this.distance = distance;
	}

	public int getNode()
	{
		return node;
	}

	public int getDistance()
	{
		return distance;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + distance;
		result = prime * result + node;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Connection other = (Connection) obj;
		if (distance != other.distance)
			return false;
		if (node != other.node)
			return false;
		return true;
	}

}
