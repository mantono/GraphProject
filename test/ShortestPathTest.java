package alda.graphProject.test;

import static org.junit.Assert.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;
import org.junit.Before;
import org.junit.Test;

import alda.graphProject.ConcurrentGraph;
import alda.graphProject.Edge;
import alda.graphProject.Graph;
import alda.graphProject.GraphExplorer;
import alda.graphProject.PathFinder;

public class ShortestPathTest
{
	Graph<String> graph;
	GraphExplorer<String> explorer;
	org.jgrapht.graph.AbstractGraph<String, Integer> oracle;

	@Before
	public void setup()
	{
		graph = new ConcurrentGraph<String>();
		explorer = new PathFinder<String>(graph);
		oracle = new SimpleWeightedGraph<String, Integer>(Integer.class);
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
				oracle.addEdge(randomNode1, randomNode2, weight);
				oracle.addEdge(randomNode2, randomNode1, weight);
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
		{
			graph.add("node" + i);
			oracle.addVertex("node" + i);
		}
	}

	private String getRandomConnctedNode(List<String> connectedNodes, int nextInt)
	{
		
		return connectedNodes.get(nextInt % connectedNodes.size());
	}

	@Test
	public void testSmallRandomGraphWithOracle()
	{
		final String start = "node0";
		final String end = createRandomConnectedGraph(15, 25, 10);
		int pathLength = explorer.getTotalWeight(start, end);

		System.out.println(start + " --> " + end);
		printGraph();
	}
	
	@Test
	public void testBigRandomGraphWithOracle()
	{
		// 80 000 noder med 30 000 edges tar ca 280 sekunder för att hitta snabbaste vägen (med JGraphT-orakel) - 275 sekunder utan?!
		final String start = "node0";
		final String end = createRandomConnectedGraph(5000, 2000, 20);
		int pathLength = explorer.getTotalWeight(start, end);
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
		
		PathFinder<String> explorer = new PathFinder<String>(graph);
		assertEquals(14, explorer.getTotalWeight("B", "E"));
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
		
		int length = explorer.getTotalWeight("A", "G");
		assertEquals(21, length);		
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
		
		int sum = explorer.getTotalWeight("node11", "node7");
		assertEquals(189, sum);
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

}
