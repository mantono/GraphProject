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
		for(int i = 0; i < nodes; i++)
		{
			graph.add("node" + i);
			oracle.addVertex("node" + i);
		}
		
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
		DijkstraShortestPath<String, Integer> dijkstras = new DijkstraShortestPath<String, Integer>(oracle, start, end);

		System.out.println(start + " --> " + end);
		printGraph();
		assertEquals(dijkstras.getPath(), pathLength);
	}
	
	@Test
	public void testBigRandomGraphWithOracle()
	{
		final String start = "node0";
		final String end = createRandomConnectedGraph(80000, 30000, 20);
		int pathLength = explorer.getTotalWeight(start, end);
		DijkstraShortestPath<String, Integer> dijkstras = new DijkstraShortestPath<String, Integer>(oracle, start, end);

		assertEquals(dijkstras.getPathLength(), pathLength, 0.1);
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
