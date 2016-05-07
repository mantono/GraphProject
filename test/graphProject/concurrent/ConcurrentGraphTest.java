package graphProject.concurrent;

import static org.junit.Assert.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import graphProject.*;
import graphProject.concurrent.ConcurrentGraph;

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
	public void testRemoveOnNonExistingNode()
	{
		assertFalse(graph.remove("not here"));
	}
	
	@Test
	public void testConnect()
	{
		graph.connect("node0", "node1", 14);
		assertEquals(14, graph.getWeight("node0", "node1"));
		assertEquals(14, graph.getWeight("node1", "node0"));
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testConnectionOfNonExistendNode()
	{
		graph.connect("node0", "node15", 9);
	}
	
	@Test
	public void testConnectDuplicateEdgesAndDisconnect()
	{
		graph.connect("node0", "node1", 5);
		graph.connect("node0", "node1", 5);
		graph.connect("node1", "node0", 5);
		
		assertTrue(graph.isConnected("node0", "node1"));
		assertTrue(graph.isConnected("node1", "node0"));
		
		assertTrue(graph.disconnect("node0", "node1"));
		
		assertFalse(graph.isConnected("node1", "node0"));
		assertFalse(graph.isConnected("node0", "node1"));
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testIsConnectedWithNonExistingNode()
	{
		assertFalse(graph.isConnected("node0", "node13"));
		assertFalse(graph.isConnected("node13", "node0"));
	}
	
	@Test
	public void testConnectEdgesAndRemoveNode()
	{
		graph.connect("node0", "node1", 4);
		assertTrue(graph.remove("node0"));
		assertEquals(0, graph.edgeSize("node1"));
	}
	
	@Test
	public void testEdgeSize()
	{
		assertEquals(0, graph.edgeSize("node0"));
		
		graph.connect("node0", "node1", 10);
		graph.connect("node0", "node2", 10);
		graph.connect("node0", "node3", 10);
		assertEquals(3, graph.edgeSize("node0"));		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalConnectWithZeroWeight()
	{
		graph.connect("node0", "node1", 0);
	}
	

	@Test
	public void testClear()
	{
		graph.clear();
		assertEquals(0, graph.size());
	}
	
	@Test
	public void testGetEdgesForNode()
	{
		graph.connect("node0", "node1", 3);
		graph.connect("node0", "node2", 4);
		graph.connect("node0", "node3", 5);
		List<Edge<String>> edges = graph.getEdgesFor("node0");
		assertEquals(3, edges.size());
	}

}
