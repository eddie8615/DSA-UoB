package dsa_assignment1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

@Ignore
public class MLNodeTest
{
	@Rule
	public Timeout globalTimeout = new Timeout(20, TimeUnit.MILLISECONDS);
	
	@Test
	public void testInitializeNode()
	{
		MLNodeInterface<String> n = new MLNode<>("abc");
		assertEquals("Initialization of MLNode failed on item", "abc", n.getElement());
		assertSame("Initialization of MLNode failed on prev1", n, n.getPrev1());
		assertSame("Initialization of MLNode failed on next1", n, n.getNext1());
		assertSame("Initialization of MLNode failed on prev2", n, n.getPrev2());
		assertSame("Initialization of MLNode failed on next2", n, n.getNext2());
	}
	
	
	@Test
	public void testAddElement()
	{
		MLNodeInterface<Integer> n0 = new MLNode<>(0);
		MLNodeInterface<Integer> n1 = new MLNode<>(1);
		MLNodeInterface<Integer> n2 = new MLNode<>(2);
		MLNodeInterface<Integer> n3 = new MLNode<>(3);
		
		n3.addAfter1(n0);  // 0-3, 1, 2
		n2.addBefore1(n3); // 0-2-3. 1
		n1.addAfter1(n0);  // 0-1-2-3
		
		n3.addBefore2(n0);  // 3-0, 1, 2
		n2.addAfter2(n3); // 3-2-0. 1
		n1.addBefore2(n0);  // 3-2-1-0
		
		assertSame("adding1", n3, n0.getPrev1());
		assertSame("adding1", n0, n1.getPrev1());
		assertSame("adding1", n1, n2.getPrev1());
		assertSame("adding1", n2, n3.getPrev1());
		assertSame("adding1", n1, n0.getNext1());
		assertSame("adding1", n2, n1.getNext1());
		assertSame("adding1", n3, n2.getNext1());
		assertSame("adding1", n0, n3.getNext1());
		
		
		assertSame("adding2", n1, n0.getPrev2());
		assertSame("adding2", n2, n1.getPrev2());
		assertSame("adding2", n3, n2.getPrev2());
		assertSame("adding2", n0, n3.getPrev2());
		assertSame("adding2", n3, n0.getNext2());
		assertSame("adding2", n0, n1.getNext2());
		assertSame("adding2", n1, n2.getNext2());
		assertSame("adding2", n2, n3.getNext2());		
	}
	
	@Test
	public void testAddElementWithoutRemoving1()
	{
		MLNodeInterface<Integer> n0 = new MLNode<>(0);
		MLNodeInterface<Integer> n1 = new MLNode<>(1);
		MLNodeInterface<Integer> n2 = new MLNode<>(2);
		
		n1.addAfter1(n0);  
		n2.addAfter1(n1);  // 0-1-2

		n1.addAfter1(n2);  // 0-2-1
		
		assertEquals("adding After Without Removing 1", n2.getElement(), n0.getNext1().getElement());
		assertEquals("adding After Without Removing 1", n1.getElement(), n2.getNext1().getElement());
		assertEquals("adding After Without Removing 1", n0.getElement(), n1.getNext1().getElement());

		n1.addBefore1(n2); // 0-1-2

		assertEquals("adding Before Without Removing 1", n1.getElement(), n0.getNext1().getElement());
		assertEquals("adding Before Without Removing 1", n2.getElement(), n1.getNext1().getElement());
		assertEquals("adding Before Without Removing 1", n0.getElement(), n2.getNext1().getElement());
	}

	@Test
	public void testAddElementWithoutRemoving2()
	{
		MLNodeInterface<Integer> n0 = new MLNode<>(0);
		MLNodeInterface<Integer> n1 = new MLNode<>(1);
		MLNodeInterface<Integer> n2 = new MLNode<>(2);
		
		n1.addAfter2(n0);  
		n2.addAfter2(n1);  // 0-1-2
		
		n1.addAfter2(n2);  // 0-2-1
		
		assertEquals("adding After Without Removing 2", n2.getElement(), n0.getNext2().getElement());
		assertEquals("adding After Without Removing 2", n1.getElement(), n2.getNext2().getElement());
		assertEquals("adding After Without Removing 2", n0.getElement(), n1.getNext2().getElement());

		n1.addBefore2(n2); // 0-1-2

		assertEquals("adding Before Without Removing 2", n1.getElement(), n0.getNext2().getElement());
		assertEquals("adding Before Without Removing 2", n2.getElement(), n1.getNext2().getElement());
		assertEquals("adding Before Without Removing 2", n0.getElement(), n2.getNext2().getElement());
	}
	
	@Test
	public void testRemoveElement()
	{
		MLNodeInterface<Integer> n0 = new MLNode<>(0);
		MLNodeInterface<Integer> n1 = new MLNode<>(1);
		MLNodeInterface<Integer> n2 = new MLNode<>(2);
		
		n1.addAfter1(n0);
		n2.addAfter1(n1);
		
		n1.addAfter2(n0);
		n2.addAfter2(n1);
		
		MLNodeInterface<Integer>x1 = n1.remove1();  //list 1: 0-2, 1
		MLNodeInterface<Integer>x2 = n2.remove2();  //list 2: 0-1, 2

		assertSame("Remove1 returning argument", n1, x1);
		assertSame("Remove1 returning argument", n1, x1.getNext1());
		assertSame("Remove1 returning argument", n1, x1.getPrev1());
		
		assertSame("Remove2 returning argument", n2, x2);
		assertSame("Remove2 returning argument", n2, x2.getNext2());
		assertSame("Remove2 returning argument", n2, x2.getPrev2());
		
		assertSame("Remove1 remaining list", n2, n0.getNext1());
		assertSame("Remove1 remaining list", n0, n2.getNext1());
		assertSame("Remove1 remaining list", n2, n0.getPrev1());
		assertSame("Remove1 remaining list", n0, n2.getPrev1());

		assertSame("Remove2 remaining list", n1, n0.getNext2());
		assertSame("Remove2 remaining list", n0, n1.getNext2());
		assertSame("Remove2 remaining list", n1, n0.getPrev2());
		assertSame("Remove2 remaining list", n0, n1.getPrev2());

		//assertSame("")
	}
}
