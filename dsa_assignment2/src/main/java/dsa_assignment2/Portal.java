package dsa_assignment2;

/**
 * Portal is an immutable class to uniquely identify a doorway in a chamber 
 *
 */
public class Portal
{
	private int chamber ;
	private int door;
	
	public Portal(int chamber, int door)
	{
		this.chamber = chamber;
		this.door = door;
	}
	
	// This default constructor is intentionally made private.
	// The idea is that a Portal object should never be created
	// without setting its chamber and door fields. By making 
	// the default constructor private, we can guarantee that no
	// other class can create such a Portal object and then we
	// only have to make sure that the Portal class itself does not
	// do so. We do this by using the assert(false) statement, which 
	// will throw and exception if it is ever called.
	
	@SuppressWarnings("unused")
	private Portal()
	{			
		assert(false);
	}

	public int getChamber()
	{
		return chamber;
	}

	public int getDoor()
	{
		return door;
	}

	@Override
	public String toString()
	{
		return chamber + ":" + door;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + chamber;
		result = prime * result + door;
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
		Portal other = (Portal) obj;
		if (chamber != other.chamber)
			return false;
		if (door != other.door)
			return false;
		return true;
	}

}
