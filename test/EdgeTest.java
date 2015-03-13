package alda.graphProject.test;

import static org.junit.Assert.*;

import alda.graphProject.Edge;
import org.junit.Before;
import org.junit.Test;

public class EdgeTest
{

	@Before
	public void setUp() throws Exception
	{
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalConstruction()
	{
		new Edge<Long>(45L, 0);
	}

	@Test
	public void testEqualsOfStrings()
	{
		Edge<String> first = new Edge<String>("node1", 10);
		Edge<String> second = new Edge<String>("node1", 10);
		assertEquals(first, second);
		
		Edge<String> third = new Edge<String>("node1", 12);
		assertFalse(first.equals(third));
		
		Edge<String> fourth = new Edge<String>("node2", 10);
		assertFalse(first.equals(fourth));
	}
	
	@Test
	public void testEqualsOfDifferentPrimitives()
	{
		Edge<Integer> edge1 = new Edge<Integer>(20, 15);
		Edge<Integer> edge2 = new Edge<Integer>(20, 15);
		assertEquals(edge1, edge2);
		
		Edge<Short> edge3 = new Edge<Short>((short)20, 15);
		assertFalse(edge1.equals(edge3));
		
		Edge<Double> edge4 = new Edge<Double>(20.0, 15);
		assertFalse(edge1.equals(edge4));
	}
	
	@Test
	public void testEqualsNull()
	{
		Edge<String> good = new Edge<String>("node1", 10);
		Edge<String> nullData = null;
		assertFalse(good.equals(nullData));
		
		nullData = new Edge<String>(null, 10);
		assertFalse(good.equals(nullData));
	}
	

}
