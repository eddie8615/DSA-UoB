package dsa_assignment1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;


public class OrderedMruListMarkTest
{
	private static final Logger logger = Logger.getLogger(OrderedMruListMarkTest.class);

	@Rule
	public Timeout globalTimeout = new Timeout(2000, TimeUnit.MILLISECONDS);

	@Rule
	public TestName name = new TestName();

	@Test
	public void testInitializeList()
	{
		OrderedMruListInterface<String> oml = new OrderedMruList<>();

		assertNotNull("Intialization of OrderedMruList failed", oml);
		assertTrue("New OrderedMruList reports it is NOT empty in the Ordered part", oml.isEmptyOrdered());
		assertTrue("New OrderedMruList reports it is NOT empty in the Mru part", oml.isEmptyMru());
		assertNull("After initialization, getFirstOrdered returns a non-null MLNode", oml.getFirstOrdered());
		assertNull("After initialization, getFirstMru returns a non-null MLNode", oml.getFirstMru());
	}
	
	

	@Test
	public void testAddOrdered()
	{
		OrderedMruListInterface<String> oml = new OrderedMruList<>();

		assertNotNull("Intialization of OrderedMruList failed", oml);
		oml.add("E");
		oml.add("A");
		oml.add("C");
		oml.add("B");
		oml.add("D");

		MLNodeInterface<String> e1 = oml.getFirstOrdered() ; // A
		assertNotNull("GetFirstOrdered after add of OrderedMruList failed", e1);
		MLNodeInterface<String> e2 = oml.getNextOrdered(e1); // B
		assertNotNull("GetNextOrdered after add of OrderedMruList failed", e2);
		MLNodeInterface<String> e3 = oml.getNextOrdered(e2); // C
		assertNotNull("GetNextOrdered after add of OrderedMruList failed", e3);
		MLNodeInterface<String> e4 = oml.getNextOrdered(e3); // D
		assertNotNull("GetNextOrdered after add of OrderedMruList failed", e4);
		MLNodeInterface<String> e5 = oml.getNextOrdered(e4); // E
		assertNotNull("GetNextOrdered after add of OrderedMruList failed", e5);
		MLNodeInterface<String> e6 = oml.getNextOrdered(e5); // null
		assertNull("GetNextOrdered after add of OrderedMruList failed", e6);
		
		String ordered = String.format("%s %s %s %s %s %s",
				e1.getElement(), e2.getElement(), e3.getElement(),
				e4.getElement(), e5.getElement(), (e6==null)?"null":e6.getElement());
		
		assertEquals("After adding to OrderedMruList, ordered part", "A B C D E null", ordered);

		assertFalse("After adding to OrderedMruList, isEmptyOrdered() reports true when it should be false", oml.isEmptyOrdered());
	}

	@Test
	public void testAddMru()
	{
		OrderedMruListInterface<String> oml = new OrderedMruList<>();
		assertNotNull("Intialization of OrderedMruList failed", oml);

		oml.add("E");
		oml.add("A");
		oml.add("C");
		oml.add("B");
		oml.add("D");

		MLNodeInterface<String> e1 = oml.getFirstMru() ; // D
		assertNotNull("GetFirstMru after add to OrderedMruList failed", e1);
		MLNodeInterface<String> e2 = oml.getNextMru(e1); // B
		assertNotNull("GetNextMru after add to OrderedMruList failed", e2);
		MLNodeInterface<String> e3 = oml.getNextMru(e2); // C
		assertNotNull("GetNextMru after add to OrderedMruList failed", e3);
		MLNodeInterface<String> e4 = oml.getNextMru(e3); // A
		assertNotNull("GetNextMru after add to OrderedMruList failed", e4);
		MLNodeInterface<String> e5 = oml.getNextMru(e4); // E
		assertNotNull("GetNextMru after add to OrderedMruList failed", e5);
		MLNodeInterface<String> e6 = oml.getNextMru(e5); // null
		assertNull("GetNextMru after add to OrderedMruList failed", e6);
		
		String ordered = String.format("%s %s %s %s %s %s",
				e1.getElement(), e2.getElement(), e3.getElement(),
				e4.getElement(), e5.getElement(), (e6==null)?"null":e6.getElement());
		
		assertEquals("After adding to OrderedMruList, MRU part", "D B C A E null", ordered);

		assertFalse("After adding to OrderedMruList, isEmptyMru() reports true when it should be false", oml.isEmptyMru());
	}
	
	@Test
	public void testTouch()
	{
		OrderedMruListInterface<String> oml = new OrderedMruList<>();
		assertNotNull("Intialization of OrderedMruList failed", oml);

		oml.add("E");
		oml.add("D");
		oml.add("C");
		oml.add("B");
		oml.add("A");

		MLNodeInterface<String> e1 = oml.getFirstMru() ; // A
		assertNotNull("GetFirstMru after add to OrderedMruList failed", e1);
		MLNodeInterface<String> e2 = oml.getNextMru(e1); // B
		assertNotNull("GetNextMru after add to OrderedMruList failed", e2);
		MLNodeInterface<String> e3 = oml.getNextMru(e2); // C
		assertNotNull("GetNextMru after add to OrderedMruList failed", e3);
		MLNodeInterface<String> e4 = oml.getNextMru(e3); // D
		assertNotNull("GetNextMru after add to OrderedMruList failed", e4);
		MLNodeInterface<String> e5 = oml.getNextMru(e4); // E
		assertNotNull("GetNextMru after add to OrderedMruList failed", e5);
		MLNodeInterface<String> e6 = oml.getNextMru(e5); // null
		assertNull("GetNextMru after add to OrderedMruList failed", e6);
		
		oml.touch(e1); // make sure touching the first on the MRU list also works
		oml.touch(e2);
		oml.touch(e4);
		oml.touch(e2);
		
		e1 = oml.getFirstMru() ; // B
		assertNotNull("GetFirstMru after touch to OrderedMruList failed", e1);
		e2 = oml.getNextMru(e1); // D
		assertNotNull("GetNextMru after add to OrderedMruList failed", e2);
		e3 = oml.getNextMru(e2); // A
		assertNotNull("GetNextMru after add to OrderedMruList failed", e3);
		e4 = oml.getNextMru(e3); // C
		assertNotNull("GetNextMru after add to OrderedMruList failed", e4);
		e5 = oml.getNextMru(e4); // E
		assertNotNull("GetNextMru after add to OrderedMruList failed", e5);
		e6 = oml.getNextMru(e5); // null
		assertNull("GetNextMru after add to OrderedMruList failed", e6);

		String mru = String.format("%s %s %s %s %s %s",
				e1.getElement(), e2.getElement(), e3.getElement(),
				e4.getElement(), e5.getElement(), (e6==null)?"null":e6.getElement());
		
		assertEquals("Touching elements, MRU part", "B D A C E null", mru);

		e1 = oml.getFirstOrdered() ; // A
		assertNotNull("GetFirstOrdered after touch to OrderedMruList failed", e1);
		e2 = oml.getNextOrdered(e1); // B
		assertNotNull("GetNextOrdered after touch to OrderedMruList failed", e2);
		e3 = oml.getNextOrdered(e2); // C
		assertNotNull("GetNextOrdered after touch to OrderedMruList failed", e3);
		e4 = oml.getNextOrdered(e3); // D
		assertNotNull("GetNextOrdered after touch to OrderedMruList failed", e4);
		e5 = oml.getNextOrdered(e4); // E
		assertNotNull("GetNextOrdered after touch to OrderedMruList failed", e5);
		e6 = oml.getNextOrdered(e5); // null
		assertNull("GetNextOrdered after touch to OrderedMruList failed", e6);

		String ordered = String.format("%s %s %s %s %s %s",
				e1.getElement(), e2.getElement(), e3.getElement(),
				e4.getElement(), e5.getElement(), (e6==null)?"null":e6.getElement());

		assertEquals("Touching elements, Ordered part", "A B C D E null", ordered);
		
	}
	
	@Test
	public void testRemove()
	{
		OrderedMruListInterface<String> oml = new OrderedMruList<>();
		assertNotNull("Intialization of OrderedMruList failed", oml);

		oml.add("E");
		oml.add("D");
		oml.add("C");
		oml.add("B");
		oml.add("A");

		MLNodeInterface<String> e1 = oml.getFirstOrdered() ; // A
		assertNotNull("Adding to and getting from OrderedMruList before removing failed", e1);
		MLNodeInterface<String> e2 = oml.getNextOrdered(e1); // B
		assertNotNull("Adding to and getting from OrderedMruList before removing failed", e2);
		MLNodeInterface<String> e3 = oml.getNextOrdered(e2); // C
		assertNotNull("Adding to and getting from OrderedMruList before removing failed", e3);
		MLNodeInterface<String> e4 = oml.getNextOrdered(e3); // D
		assertNotNull("Adding to and getting from OrderedMruList before removing failed", e3);
		MLNodeInterface<String> e5 = oml.getNextOrdered(e4); // E
		assertNotNull("Adding to and getting from OrderedMruList before removing failed", e5);
		
		oml.remove(e3); // remove C
		oml.remove(e1); // remove A
		oml.remove(e5); // remove E
		
		MLNodeInterface<String> x1 = oml.getFirstOrdered() ; // B
		assertNotNull("Getting from OrderedMruList after removing from Ordered part failed", x1);
		MLNodeInterface<String> x2 = oml.getNextOrdered(x1); // D
		assertNotNull("Getting from OrderedMruList after removing Ordered part failed", x2);
		MLNodeInterface<String> x3 = oml.getNextOrdered(x2); // Null
		assertNull("Getting from OrderedMruList after removing Ordered part failed", x3);
		
		String ordered = String.format("%s %s %s",
				x1.getElement(), x2.getElement(), (x3==null)?"null":x3.getElement());
		
		assertEquals("Removing from OrderedMruList, Ordered part", "B D null", ordered);
		
		x1 = oml.getFirstMru() ; // B
		assertNotNull("Getting from OrderedMruList after removing from Mru part failed", x1);
		x2 = oml.getNextMru(x1); // D
		assertNotNull("Getting from OrderedMruList after removing from Mru part failed", x2);
		x3 = oml.getNextMru(x2); // Null
		assertNull("Getting from OrderedMruList after removing from Mru part failed", x3);

		String mru = String.format("%s %s %s",
				x1.getElement(), x2.getElement(), (x3==null)?"null":x3.getElement());
		
		assertEquals("Removing from OrderedMruList, MRU part", "B D null", ordered);

		oml.remove(e4);
		oml.remove(e2);
		
		assertTrue("After removing all from OrderedMruList, Ordered part, isEmptyOrdered() should be true", oml.isEmptyOrdered());
		assertTrue("After emoving all from OrderedMruList, Mru part, isEmptyMru() should be true", oml.isEmptyMru());
	}
}
