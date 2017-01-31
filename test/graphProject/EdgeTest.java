package graphProject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.PriorityQueue;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

public class EdgeTest
{
	@Test
	public void testEqualsOfStrings()
	{
		Edge<String> first = new Edge<String>("node1", "node2", 10);
		Edge<String> second = new Edge<String>("node1", "node2", 10);
		assertEquals(first, second);

		Edge<String> third = new Edge<String>("node1", "node2", 12);
		assertFalse(first.equals(third));

		Edge<String> fourth = new Edge<String>("node2", "node1", 10);
		assertFalse(first.equals(fourth));
	}
	
	@Test
	public void testEqualsOfDifferentPrimitives()
	{
		Edge<Integer> edge1 = new Edge<Integer>(20, 3, 15);
		Edge<Integer> edge2 = new Edge<Integer>(20, 3, 15);
		assertEquals(edge1, edge2);

		Edge<Short> edge3 = new Edge<Short>((short) 20, (short) 3,15);
		assertFalse(edge1.equals(edge3));

		Edge<Double> edge4 = new Edge<Double>(20.0, 3.0, 15);
		assertFalse(edge1.equals(edge4));
	}
	
	@Test
	public void testEqualsNull()
	{
		Edge<String> good = new Edge<String>("node1", "node2", 10);
		Edge<String> nullData = null;
		assertFalse(good.equals(nullData));

		nullData = new Edge<String>(null, null, 10);
		assertFalse(good.equals(nullData));
	}

	@Test
	public void testCompareTo()
	{
		final Queue<Edge<Integer>> q = new PriorityQueue<Edge<Integer>>(5);

		final Edge<Integer> first = new Edge<Integer>(1, 2, 0.0);
		final Edge<Integer> second = new Edge<Integer>(2, 2, 0.000000000005);
		final Edge<Integer> third = new Edge<Integer>(3, 2, 0.0000000001);
		final Edge<Integer> fourth = new Edge<Integer>(4, 2, 1);
		final Edge<Integer> fifth = new Edge<Integer>(5, 2, 200);

		q.add(first);
		q.add(fourth);
		q.add(third);
		q.add(fifth);
		q.add(second);

		assertEquals(first, q.poll());
		assertEquals(second, q.poll());
		assertEquals(third, q.poll());
		assertEquals(fourth, q.poll());
		assertEquals(fifth, q.poll());
	}
}
