package dsa_assignment1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.Timeout;


public class MLNodeMarkTest
{
	private static final Logger logger = Logger.getLogger(MLNodeMarkTest.class);

	@Rule
	public Timeout globalTimeout = new Timeout(2000, TimeUnit.MILLISECONDS);
	
	@Rule
	public TestName name = new TestName();

	@Test
	public void testInitializeNode()
	{
		MLNodeInterface<String> n = new MLNode("abc");
		
		assertNotNull("Construction of MLNode<String> failed", n);
		assertEquals("After construction MLNode's item is not correct", "abc", n.getElement());
		assertSame("Immediately after construction, MLNodes prev1 should point to the same MLNode:", n, n.getPrev1());
		assertSame("Immediately after construction, MLNodes next1 should point to the same MLNode:", n, n.getNext1());
		assertSame("Immediately after construction, MLNodes prev2 should point to the same MLNode:", n, n.getPrev2());
		assertSame("Immediately after construction, MLNodes next2 should point to the same MLNode:", n, n.getNext2());
	}
	
	
	@Test
	public void testAddElement()
	{
		MLNodeInterface<Integer> n0 = new MLNode(0);
		MLNodeInterface<Integer> n1 = new MLNode(1);
		MLNodeInterface<Integer> n2 = new MLNode(2);
		MLNodeInterface<Integer> n3 = new MLNode(3);


		assertNotNull("Construction of MLNode<Integer> failed", n0);
		assertNotNull("Construction of MLNode<Integer> failed", n1);
		assertNotNull("Construction of MLNode<Integer> failed", n2);
		assertNotNull("Construction of MLNode<Integer> failed", n3);
		
		n3.addAfter1(n0);  // 0-3, 1, 2
		n2.addBefore1(n3); // 0-2-3. 1
		n1.addAfter1(n0);  // 0-1-2-3
		
		n3.addBefore2(n0);  // 3-0, 1, 2
		n2.addAfter2(n3); // 3-2-0. 1
		n1.addBefore2(n0);  // 3-2-1-0
		
		assertSame("After a sequence of addAfter1 and addBefore 1: ", n3, n0.getPrev1());
		assertSame("After a sequence of addAfter1 and addBefore 1: ", n0, n1.getPrev1());
		assertSame("After a sequence of addAfter1 and addBefore 1: ", n1, n2.getPrev1());
		assertSame("After a sequence of addAfter1 and addBefore 1: ", n2, n3.getPrev1());
		assertSame("After a sequence of addAfter1 and addBefore 1: ", n1, n0.getNext1());
		assertSame("After a sequence of addAfter1 and addBefore 1: ", n2, n1.getNext1());
		assertSame("After a sequence of addAfter1 and addBefore 1: ", n3, n2.getNext1());
		assertSame("After a sequence of addAfter1 and addBefore 1: ", n0, n3.getNext1());
		
		
		assertSame("After a sequence of addAfter2 and addBefore 2:", n1, n0.getPrev2());
		assertSame("After a sequence of addAfter2 and addBefore 2:", n2, n1.getPrev2());
		assertSame("After a sequence of addAfter2 and addBefore 2:", n3, n2.getPrev2());
		assertSame("After a sequence of addAfter2 and addBefore 2:", n0, n3.getPrev2());
		assertSame("After a sequence of addAfter2 and addBefore 2:", n3, n0.getNext2());
		assertSame("After a sequence of addAfter2 and addBefore 2:", n0, n1.getNext2());
		assertSame("After a sequence of addAfter2 and addBefore 2:", n1, n2.getNext2());
		assertSame("After a sequence of addAfter2 and addBefore 2:", n2, n3.getNext2());		
	}
	
	@Test
	public void testAddElementWithoutRemoving1()
	{
		MLNodeInterface<Integer> n0 = new MLNode<>(0);
		MLNodeInterface<Integer> n1 = new MLNode<>(1);
		MLNodeInterface<Integer> n2 = new MLNode<>(2);
		
		assertNotNull("Construction of MLNode<Integer> failed", n0);
		assertNotNull("Construction of MLNode<Integer> failed", n1);
		assertNotNull("Construction of MLNode<Integer> failed", n2);

		n1.addAfter1(n0);  
		n2.addAfter1(n1);  // 0-1-2

		n1.addAfter1(n2);  // 0-2-1
		
		assertEquals("adding After1 Without Removing1:", n2.getElement(), n0.getNext1().getElement());
		assertEquals("adding After1 Without Removing1:", n1.getElement(), n2.getNext1().getElement());
		assertEquals("adding After1 Without Removing1:", n0.getElement(), n1.getNext1().getElement());

		n1.addBefore1(n2); // 0-1-2

		assertEquals("adding Before1 Without Removing1:", n1.getElement(), n0.getNext1().getElement());
		assertEquals("adding Before1 Without Removing1:", n2.getElement(), n1.getNext1().getElement());
		assertEquals("adding Before1 Without Removing1:", n0.getElement(), n2.getNext1().getElement());
	}

	@Test
	public void testAddElementWithoutRemoving2()
	{
		MLNodeInterface<Integer> n0 = new MLNode<>(0);
		MLNodeInterface<Integer> n1 = new MLNode<>(1);
		MLNodeInterface<Integer> n2 = new MLNode<>(2);

		assertNotNull("Construction of MLNode<Integer> failed", n0);
		assertNotNull("Construction of MLNode<Integer> failed", n1);
		assertNotNull("Construction of MLNode<Integer> failed", n2);

		n1.addAfter2(n0);  
		n2.addAfter2(n1);  // 0-1-2
		
		n1.addAfter2(n2);  // 0-2-1
		
		assertEquals("adding After2 Without Removing2:", n2.getElement(), n0.getNext2().getElement());
		assertEquals("adding After2 Without Removing2:", n1.getElement(), n2.getNext2().getElement());
		assertEquals("adding After2 Without Removing2:", n0.getElement(), n1.getNext2().getElement());

		n1.addBefore2(n2); // 0-1-2

		assertEquals("adding Before2 Without Removing2:", n1.getElement(), n0.getNext2().getElement());
		assertEquals("adding Before2 Without Removing2:", n2.getElement(), n1.getNext2().getElement());
		assertEquals("adding Before2 Without Removing2:", n0.getElement(), n2.getNext2().getElement());
	}
	
	@Test
	public void testRemoveElement()
	{
		MLNodeInterface<Integer> n0 = new MLNode<>(0);
		MLNodeInterface<Integer> n1 = new MLNode<>(1);
		MLNodeInterface<Integer> n2 = new MLNode<>(2);

		assertNotNull("Construction of MLNode<Integer> failed", n0);
		assertNotNull("Construction of MLNode<Integer> failed", n1);
		assertNotNull("Construction of MLNode<Integer> failed", n2);

		n1.addAfter1(n0);
		n2.addAfter1(n1);
		
		n1.addAfter2(n0);
		n2.addAfter2(n1);
		
		MLNodeInterface<Integer>x1 = n1.remove1();  //list 1: 0-2, 1
		MLNodeInterface<Integer>x2 = n2.remove2();  //list 2: 0-1, 2
		assertNotNull("Remove1 on an MLNode should not return null", x1);
		assertNotNull("Remove2 on an MLNode should not return null", x2);
		
		assertSame("Remove1 on an MLNode should return the same MLNode:"                       , n1, x1);
		assertSame("Remove1 should leave the node's next1 pointer pointing to the node itself:", n1, x1.getNext1());
		assertSame("Remove1 should leave the node's prev1 pointer pointing to the node itself:", n1, x1.getPrev1());
		
		assertSame("Remove2 on an MLNode should return the same MLNode:"                       , n2, x2);
		assertSame("Remove2 should leave the node's next2 pointer pointing to the node itself:", n2, x2.getNext2());
		assertSame("Remove2 should leave the node's prev2 pointer pointing to the node itself:", n2, x2.getPrev2());
		
		assertSame("Remove1 should remove the node from list1 but leave the rest of the list intact:", n2, n0.getNext1());
		assertSame("Remove1 should remove the node from list1 but leave the rest of the list intact:", n0, n2.getNext1());
		assertSame("Remove1 should remove the node from list1 but leave the rest of the list intact:", n2, n0.getPrev1());
		assertSame("Remove1 should remove the node from list1 but leave the rest of the list intact:", n0, n2.getPrev1());

		assertSame("Remove2 should remove the node from list2 but leave the rest of the list intact:", n1, n0.getNext2());
		assertSame("Remove2 should remove the node from list2 but leave the rest of the list intact:", n0, n1.getNext2());
		assertSame("Remove2 should remove the node from list2 but leave the rest of the list intact:", n1, n0.getPrev2());
		assertSame("Remove2 should remove the node from list2 but leave the rest of the list intact:", n0, n1.getPrev2());

	}
}
