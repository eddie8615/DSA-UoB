package dsa_assignment1;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

@Ignore
public class OrderedMruListTest
{
	@Rule
	public Timeout globalTimeout = new Timeout(20, TimeUnit.MILLISECONDS);

	@Test
	public void testInitializeList()
	{
		OrderedMruListInterface<String> oml = new OrderedMruList<>();

		assertEquals("Intialization of OrderedMruList failed", Boolean.TRUE, oml.isEmptyOrdered());
		assertEquals("Intialization of OrderedMruList failed", Boolean.TRUE, oml.isEmptyMru());
		assertEquals("After initialization, getFirstOrdered", null, oml.getFirstOrdered());
		assertEquals("After initialization, getFirstMru", null, oml.getFirstMru());
	}
	
	

	@Test
	public void testAddOrdered()
	{
		OrderedMruListInterface<String> oml = new OrderedMruList<>();
		oml.add("E").add("A").add("C").add("B").add("D");
		MLNodeInterface<String> e1 = oml.getFirstOrdered() ;   // A
		MLNodeInterface<String> e2 = oml.getNextOrdered(e1); // B
		MLNodeInterface<String> e3 = oml.getNextOrdered(e2); // C
		MLNodeInterface<String> e4 = oml.getNextOrdered(e3); // D
		MLNodeInterface<String> e5 = oml.getNextOrdered(e4); // E
		MLNodeInterface<String> e6 = oml.getNextOrdered(e5); // null
		
		String ordered = String.format("%s %s %s %s %s %s",
				e1.getElement(), e2.getElement(), e3.getElement(),
				e4.getElement(), e5.getElement(), (e6==null)?"null":e6.getElement());
		
		assertEquals("Adding to OrderedMruList, ordered part", "A B C D E null", ordered);

		assertEquals("Adding to OrderedMruList, Not isEmptyOrdered", Boolean.FALSE, oml.isEmptyOrdered());
	}

	@Test
	public void testAddMru()
	{
		OrderedMruListInterface<String> oml = new OrderedMruList<>();
		oml.add("E").add("A").add("C").add("B").add("D");
		MLNodeInterface<String> e1 = oml.getFirstMru() ; // D
		MLNodeInterface<String> e2 = oml.getNextMru(e1); // B
		MLNodeInterface<String> e3 = oml.getNextMru(e2); // C
		MLNodeInterface<String> e4 = oml.getNextMru(e3); // A
		MLNodeInterface<String> e5 = oml.getNextMru(e4); // E
		MLNodeInterface<String> e6 = oml.getNextMru(e5); // null
		
		String ordered = String.format("%s %s %s %s %s %s",
				e1.getElement(), e2.getElement(), e3.getElement(),
				e4.getElement(), e5.getElement(), (e6==null)?"null":e6.getElement());
		
		assertEquals("Adding to OrderedMruList, MRU part", "D B C A E null", ordered);

		assertEquals("Adding to OrderedMruList, Not isEmptyMru", Boolean.FALSE, oml.isEmptyMru());
	}
	
	@Test
	public void testTouch()
	{
		OrderedMruListInterface<String> oml = new OrderedMruList<>();
		oml.add("E").add("D").add("C").add("B").add("A");
		MLNodeInterface<String> e1 = oml.getFirstMru() ; // A
		MLNodeInterface<String> e2 = oml.getNextMru(e1); // B
		MLNodeInterface<String> e3 = oml.getNextMru(e2); // C
		MLNodeInterface<String> e4 = oml.getNextMru(e3); // D
		MLNodeInterface<String> e5 = oml.getNextMru(e4); // E
		MLNodeInterface<String> e6 = oml.getNextMru(e5); // null
		
		oml.touch(e1); // make sure touching the first on the MRU list also works
		oml.touch(e2);
		oml.touch(e4);
		oml.touch(e2);
		
		e1 = oml.getFirstMru() ; // B
		e2 = oml.getNextMru(e1); // D
		e3 = oml.getNextMru(e2); // A
		e4 = oml.getNextMru(e3); // C
		e5 = oml.getNextMru(e4); // E
		e6 = oml.getNextMru(e5); // null

		String mru = String.format("%s %s %s %s %s %s",
				e1.getElement(), e2.getElement(), e3.getElement(),
				e4.getElement(), e5.getElement(), (e6==null)?"null":e6.getElement());
		
		assertEquals("Touching elements, MRU part", "B D A C E null", mru);

		e1 = oml.getFirstOrdered() ; // A
		e2 = oml.getNextOrdered(e1); // B
		e3 = oml.getNextOrdered(e2); // C
		e4 = oml.getNextOrdered(e3); // D
		e5 = oml.getNextOrdered(e4); // E
		e6 = oml.getNextOrdered(e5); // null

		String ordered = String.format("%s %s %s %s %s %s",
				e1.getElement(), e2.getElement(), e3.getElement(),
				e4.getElement(), e5.getElement(), (e6==null)?"null":e6.getElement());

		assertEquals("Touching elements, Ordered part", "A B C D E null", ordered);
		
	}
	
	@Test
	public void testRemove()
	{
		OrderedMruListInterface<String> oml = new OrderedMruList<>();
		oml.add("E").add("D").add("C").add("B").add("A");
		MLNodeInterface<String> e1 = oml.getFirstOrdered() ; // A
		MLNodeInterface<String> e2 = oml.getNextOrdered(e1); // B
		MLNodeInterface<String> e3 = oml.getNextOrdered(e2); // C
		MLNodeInterface<String> e4 = oml.getNextOrdered(e3); // D
		MLNodeInterface<String> e5 = oml.getNextOrdered(e4); // E
		
		oml.remove(e3); // remove C
		oml.remove(e1); // remove A
		oml.remove(e5); // remove E
		
		MLNodeInterface<String> x1 = oml.getFirstOrdered() ; // B
		MLNodeInterface<String> x2 = oml.getNextOrdered(x1); // D
		MLNodeInterface<String> x3 = oml.getNextOrdered(x2); // Null
		
		String ordered = String.format("%s %s %s",
				x1.getElement(), x2.getElement(), (x3==null)?"null":x3.getElement());
		
		assertEquals("Removing from OrderedMruList, Ordered part", "B D null", ordered);
		
		x1 = oml.getFirstMru() ; // B
		x2 = oml.getNextMru(x1); // D
		x3 = oml.getNextMru(x2); // Null

		String mru = String.format("%s %s %s",
				x1.getElement(), x2.getElement(), (x3==null)?"null":x3.getElement());
		
		assertEquals("Removing from OrderedMruList, MRU part", "B D null", ordered);

		oml.remove(e4);
		oml.remove(e2);
		
		assertEquals("Removing all from OrderedMruList, Ordered part", Boolean.TRUE, oml.isEmptyOrdered());
		assertEquals("Removing all from OrderedMruList, Mru part", Boolean.TRUE, oml.isEmptyMru());
	}
}
