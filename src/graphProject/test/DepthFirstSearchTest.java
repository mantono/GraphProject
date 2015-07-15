package graphProject.test;

import static org.junit.Assert.*;

import java.security.SecureRandom;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import graphProject.*;
import graphProject.concurrent.ConcurrentGraph;

public class DepthFirstSearchTest
{
	Graph<String> graph;
	GraphExplorer<String> explorer;

	@Before
	public void setup()
	{
		graph = new ConcurrentGraph<String>();
		explorer = new PathFinder<String>(graph);
	}

	private void createRandomGraph(int nodes, int edges)
	{
		SecureRandom rand = new SecureRandom();
		for(int i = 0; i < nodes; i++)
			graph.add("node" + i);
		while(edges > 0)
		{
			String randomNode1 = "node" + rand.nextInt(nodes);
			String randomNode2 = "node" + rand.nextInt(nodes);
			int weight = rand.nextInt(100) + 1;
			graph.connect(randomNode1, randomNode2, weight);
			edges--;
		}
	}

	@Test
	public void testSimpleDFSWithOnlyOneWay()
	{
		graph.add("A");
		graph.add("B");
		graph.add("C");
		graph.add("D");
		graph.add("E");
		graph.connect("A", "B", 3);
		graph.connect("B", "C", 3);
		graph.connect("A", "D", 3);
		graph.connect("A", "E", 3);

		List<String> path = explorer.depthFirstSearch("A", "E");
		assertEquals("[A, E]", path.toString());

		path = explorer.depthFirstSearch("A", "D");
		assertEquals("[A, D]", path.toString());

		path = explorer.depthFirstSearch("A", "C");
		assertEquals("[A, B, C]", path.toString());
	}
	
	@Test
	public void testHasPathInSimpleGraph()
	{
		graph.add("node0");
		graph.add("node1");
		graph.add("node2");
		graph.add("node3");
		
		graph.connect("node0", "node1", 3);
		graph.connect("node1", "node2", 3);
		graph.connect("node2", "node3", 3);
		
		assertTrue(explorer.hasPath("node0", "node1"));
		assertTrue(explorer.hasPath("node0", "node2"));
		assertTrue(explorer.hasPath("node0", "node3"));
	}
	
	@Test
	public void testSimpleDFSWithSlightlyLargerGraph()
	{
		graph.add("A");
		graph.add("B");
		graph.add("C");
		graph.add("D");
		graph.add("E");
		graph.add("F");
		graph.add("G");
		graph.add("H");
		graph.add("I");
		graph.add("J");
		
		graph.connect("A", "B", 1);
		graph.connect("A", "E", 1);
		graph.connect("B", "C", 1);
		graph.connect("C", "D", 1);
		graph.connect("C", "G", 1);
		graph.connect("D", "F", 1);
		graph.connect("D", "G", 1);
		graph.connect("G", "H", 1);
		graph.connect("H", "I", 1);
		graph.connect("I", "E", 1);
		graph.connect("E", "A", 1);
		
		assertTrue(explorer.hasPath("A", "H"));
		List<String> dfsPath = explorer.depthFirstSearch("A", "H");
		assertTrue(dfsPath.size() == 4 || dfsPath.size() == 5 || dfsPath.size() == 6);
	}
	
	@Test
	public void testDFSInSmallUnconnectedGraph()
	{
		graph.add("A");
		graph.add("B");
		graph.add("C");
		graph.add("D");
		graph.add("E");
		
		graph.connect("A", "B", 3);
		graph.connect("B", "C", 3);
		graph.connect("C", "D", 3);
		
		List<String> path = explorer.depthFirstSearch("A", "E");
		assertEquals(path.toString(), "[]");
	}

	@Test
	public void testBigRandomGraph()
	{
		createRandomGraph(10000, 45600);
		final String start = "node0";
		final String end = "node1000";
		List<String> dsfPathGraph = explorer.depthFirstSearch(start, end);
		assertTrue(dsfPathGraph.size() <= graph.size());

	}
}
