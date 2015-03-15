package alda.graphProject.test;

import static org.junit.Assert.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import alda.graphProject.ConcurrentGraph;
import alda.graphProject.ConcurrentPathFinder;
import alda.graphProject.Edge;
import alda.graphProject.Graph;
import alda.graphProject.GraphExplorer;
import alda.graphProject.PathFinder;

public class ShortestPathTestConcurrent
{
	private Graph<String> graph;
	private GraphExplorer<String> oracle;
	private GraphExplorer<String> concurrent;
	
	@Before
	public void setup()
	{
		graph = new ConcurrentGraph<String>();
		oracle = new PathFinder<String>(graph);
		concurrent = new ConcurrentPathFinder<String>(graph);
	}
	
	private void printGraph()
	{
		Set<String> nodes = graph.getAllNodes();
		System.out.println("graph data\n{");
		for(String node:nodes)
		{
			List<Edge<String>> edges = graph.getEdgesFor(node);
			for(Edge<String> edge:edges)
			{
				if(edge.getDestination().compareTo(node) < 0)
					System.out.println(node + " -- " + edge.getDestination() + "[label=" + edge.getWeight() + "];");
			}
		}
		System.out.println("}");
	}
	
	private String createRandomConnectedGraph(int nodes, int edges, int maxWeight)
	{
		SecureRandom rand = new SecureRandom();
		createNodes(nodes);
		
		List<String> connectedNodes = new ArrayList<String>(nodes/2);
		connectedNodes.add("node0");
		
		while(edges > 0)
		{
			String randomNode1 = getRandomConnctedNode(connectedNodes, rand.nextInt(nodes));
			String randomNode2 = "node" + rand.nextInt(nodes);
			int weight = rand.nextInt(maxWeight) + 1;
			if(!randomNode1.equals(randomNode2))
			{
				graph.connect(randomNode1, randomNode2, weight);
				connectedNodes.add(randomNode2);
				edges--;
			}
		}
		System.out.println("Graph created with " + nodes + " nodes");
		return getRandomConnctedNode(connectedNodes, rand.nextInt(nodes));
	}
	
	private void createNodes(int amount)
	{
		for(int i = 0; i < amount; i++)
			graph.add("node" + i);
	}

	private String getRandomConnctedNode(List<String> connectedNodes, int nextInt)
	{
		
		return connectedNodes.get(nextInt % connectedNodes.size());
	}

	@Test
	public void testSimpleOneWayGraph()
	{
		graph.add("A");
		graph.add("B");
		graph.add("C");
		graph.add("D");
		graph.add("E");
		graph.connect("A", "B", 2);
		graph.connect("B", "C", 4);
		graph.connect("C", "D", 2);
		graph.connect("D", "E", 2);
		
		List<Edge<String>> oraclePath = oracle.getShortestPath("A", "E");
		List<Edge<String>> concurrentPath = concurrent.getShortestPath("A", "E");
		assertEquals(oraclePath, concurrentPath);
	}
	
	@Test
	public void testJozefsKlassiskaPROG2GrafFörDijkstrasAlgortim()
	{
		graph.add("A");
		graph.add("B");
		graph.add("C");
		graph.add("D");
		graph.add("E");
		graph.add("F");
		graph.add("G");
		
		graph.connect("A", "B", 3);
		graph.connect("A", "C", 7);
		graph.connect("A", "D", 12);
		graph.connect("B", "C", 11);
		graph.connect("B", "F", 5);
		graph.connect("C", "D", 12);
		graph.connect("C", "G", 2);
		graph.connect("D", "E", 1);
		graph.connect("D", "G", 6);
		graph.connect("E", "G", 15);
		graph.connect("F", "G", 2);
		
		List<Edge<String>> oraclePath = oracle.getShortestPath("B", "E");
		List<Edge<String>> concurrentPath = concurrent.getShortestPath("B", "E");
		int result = concurrent.getTotalWeight("B", "E");
		assertEquals(oraclePath, concurrentPath);
		assertEquals(14, result);
		
		oraclePath = oracle.getShortestPath("C", "F");
		concurrentPath = concurrent.getShortestPath("C", "F");
		result = concurrent.getTotalWeight("C", "F");
		assertEquals(oraclePath, concurrentPath);
		assertEquals(4, result);
		
		oraclePath = oracle.getShortestPath("A", "G");
		concurrentPath = concurrent.getShortestPath("A", "G");
		result = concurrent.getTotalWeight("A", "G");
		assertEquals(oraclePath, concurrentPath);
		assertEquals(9, result);
	}
	
	@Test
	public void testGraphWithCircularEnd()
	{
		graph.add("A");
		graph.add("B");
		graph.add("C");
		graph.add("D");
		graph.add("E");
		graph.add("F");
		graph.add("G");
		graph.add("H");
		
		graph.connect("A", "B", 2);
		graph.connect("B", "C", 2);
		graph.connect("C", "D", 3);
		graph.connect("D", "E", 4);
		graph.connect("E", "F", 7);
		graph.connect("F", "H", 1);
		graph.connect("H", "G", 2);
		graph.connect("G", "D", 16);
		
		List<Edge<String>> oraclePath = oracle.getShortestPath("A", "G");
		List<Edge<String>> concurrentPath = concurrent.getShortestPath("A", "G");
		assertEquals(oraclePath, concurrentPath);	
	}
	
	@Test
	public void testSlightlyMoreComplexGraph()
	{
		createNodes(50);
		graph.connect("node0", "node1", 5);
		graph.connect("node1", "node4", 9);
		graph.connect("node2", "node5", 5);
		graph.connect("node3", "node6", 5);
		graph.connect("node5", "node7", 11);
		graph.connect("node6", "node8", 5);
		graph.connect("node8", "node9", 5);
		graph.connect("node22", "node10", 5);
		graph.connect("node12", "node11", 21);
		graph.connect("node21", "node12", 5);
		graph.connect("node11", "node13", 5);
		graph.connect("node0", "node14", 5);
		graph.connect("node0", "node15", 8);
		graph.connect("node3", "node16", 5);
		graph.connect("node34", "node17", 15);
		graph.connect("node45", "node18", 5);
		graph.connect("node44", "node19", 25);
		graph.connect("node23", "node20", 5);
		graph.connect("node21", "node21", 5);
		graph.connect("node33", "node22", 5);
		graph.connect("node44", "node23", 35);
		graph.connect("node15", "node24", 5);
		graph.connect("node9", "node25", 5);
		graph.connect("node3", "node26", 5);
		graph.connect("node2", "node27", 5);
		graph.connect("node45", "node28", 5);
		graph.connect("node5", "node29", 25);
		graph.connect("node3", "node31", 5);
		graph.connect("node4", "node31", 5);
		graph.connect("node45", "node32", 5);
		graph.connect("node31", "node33", 11);
		graph.connect("node32", "node34", 5);
		graph.connect("node33", "node35", 5);
		graph.connect("node11", "node36", 4);
		graph.connect("node12", "node37", 5);
		graph.connect("node20", "node38", 5);
		graph.connect("node29", "node39", 5);
		graph.connect("node28", "node40", 3);
		graph.connect("node34", "node41", 5);
		graph.connect("node26", "node47", 5);
		graph.connect("node42", "node49", 5);
		graph.connect("node29", "node32", 17);
		graph.connect("node41", "node19", 3);
		graph.connect("node23", "node9", 8);
		graph.connect("node0", "node49", 8);
		graph.connect("node11", "node42", 3);
		
		List<Edge<String>> oraclePath = oracle.getShortestPath("node11", "node7");
		List<Edge<String>> concurrentPath = concurrent.getShortestPath("node11", "node7");
		assertEquals(oraclePath, concurrentPath);	
	}
	
	@Test
	public void testBigGraphAndMeasurePerformance()
	{
		createRandomConnectedGraph(10000, 20000, 30);
		final long oracleTimeStart = System.nanoTime();
		List<Edge<String>> oraclePath = oracle.getShortestPath("node11", "node500");
		final long oracleTimeFinished = System.nanoTime();
		final long concurrentTimeStart = System.nanoTime();
		List<Edge<String>> concurrentPath = concurrent.getShortestPath("node11", "node500");
		final long concurrentTimeFinished = System.nanoTime();
		final long oracleTime = oracleTimeFinished - oracleTimeStart;
		final long concurrentTime = concurrentTimeFinished - concurrentTimeStart;
		
		System.out.println("Elapsed time oracle: " + oracleTime);
		System.out.println("Elapsed time concurrent: " + concurrentTime);
		System.out.println(100*(concurrentTime/(double)oracleTime) + "% compared to oracle time");
		
		assertEquals(oraclePath, concurrentPath);
		assertTrue(oracleTime > concurrentTime);
		assertTrue(oracleTime > concurrentTime*1.2);
		assertTrue(oracleTime > concurrentTime*1.4);
	}
	
	@Test
	public void stressTestWithNoAsserts()
	{
		for(int i = 0; i < 50; i++)
		{
			int m = (i/4)+1;
			createRandomConnectedGraph(1000*m, 2000*m, 100);
			concurrent.getShortestPath("node0", "node800");
		}
	}

}
