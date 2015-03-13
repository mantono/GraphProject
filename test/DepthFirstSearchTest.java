package alda.graphProject.test;

import static org.junit.Assert.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;
import org.junit.Before;
import org.junit.Test;

import alda.graphProject.ConcurrentGraph;
import alda.graphProject.Graph;

public class DepthFirstSearchTest
{
	Graph<String> graph;

	@Before
	public void setup()
	{
		graph = new ConcurrentGraph<String>();
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

		List<String> path = graph.depthFirstSearch("A", "E");
		assertEquals(path.toString(), "[A, E]");

		path = graph.depthFirstSearch("A", "D");
		assertEquals(path.toString(), "[A, D]");

		path = graph.depthFirstSearch("A", "C");
		assertEquals(path.toString(), "[A, B, C]");
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
		
		assertTrue(graph.hasPath("A", "H"));
		List<String> dfsPath = graph.depthFirstSearch("A", "H");
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
		
		List<String> path = graph.depthFirstSearch("A", "E");
		assertEquals(path.toString(), "[]");
	}

	@Test
	public void testBigRandomGraph()
	{
		createRandomGraph(10000, 45600);
		final String start = "node0";
		final String end = "node1000";
		List<String> dsfPathGraph = graph.depthFirstSearch(start, end);
		assertTrue(dsfPathGraph.size() <= graph.size());

	}

}
