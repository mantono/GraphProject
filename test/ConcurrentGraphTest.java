package alda.graphProject.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import alda.graphProject.ConcurrentGraph;
import alda.graphProject.Graph;

public class ConcurrentGraphTest
{
	private Graph<String> graph;
	
	@Before
	public void setup()
	{
		graph = new ConcurrentGraph<String>();
		for(int i = 0; i < 10; i++)
			graph.add("node" + i);
	}
	
	@Test
	public void testConstructorWithOtherGraph()
	{
		//Graph<String> secondGraph = new ConcurrentGraph<String>(graph);
	}

	
	@Test
	public void testAdd()
	{
		assertTrue(graph.add("test1"));
		assertTrue(graph.add("test2"));
		assertTrue(graph.add("test3"));
	}
	
	@Test
	public void testContains()
	{
		assertTrue(graph.contains("node0"));
		assertTrue(graph.contains("node1"));
		assertTrue(graph.contains("node2"));
	}
	
	@Test
	public void testRemove()
	{
		assertTrue(graph.remove("node0"));
		assertTrue(graph.remove("node1"));
		assertTrue(graph.remove("node2"));
		
		assertFalse(graph.contains("node0"));
		assertFalse(graph.contains("node1"));
		assertFalse(graph.contains("node2"));
	}
	
	@Test
	public void testConnect()
	{
		graph.connect("node0", "node1", 14);
		assertEquals(14, graph.getWeight("node0", "node1"));
		assertEquals(14, graph.getWeight("node1", "node0"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalConnectWithZeroWeight()
	{
		graph.connect("node0", "node1", 0);
	}
	
	@Test
	public void testHasPathInSimpleGraph()
	{
		graph.connect("node0", "node1", 3);
		graph.connect("node1", "node2", 3);
		graph.connect("node2", "node3", 3);
		assertTrue(graph.hasPath("node0", "node1"));
	}
}
