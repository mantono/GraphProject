package alda.graphProject.test;

import static org.junit.Assert.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

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

public class ShortestPathTest
{
	Graph<String> graph;
	org.jgrapht.graph.AbstractGraph<String, Integer> oracle;

	@Before
	public void setup()
	{
		graph = new ConcurrentGraph<String>();
		oracle = new SimpleWeightedGraph<String, Integer>(Integer.class);
	}

	private void createRandomGraph(int nodes, int edges)
	{
		SecureRandom rand = new SecureRandom();
		for(int i = 0; i < nodes; i++)
		{
			graph.add("node" + i);
			oracle.addVertex("node" + i);
		}
		while(edges > 0)
		{
			String randomNode1 = "node" + rand.nextInt(nodes);
			String randomNode2 = "node" + rand.nextInt(nodes);
			int weight = rand.nextInt(100) + 1;
			if(!randomNode1.equals(randomNode2))
			{
				graph.connect(randomNode1, randomNode2, weight);
				oracle.addEdge(randomNode1, randomNode2, weight);
				oracle.addEdge(randomNode2, randomNode1, weight);
				edges--;
			}
		}
	}

	@Test
	public void testSmallRandomGraphWithOracle()
	{
		createRandomGraph(5, 7);
		final String start = "node0";
		final String end = "node3";
		List<Edge<String>> shortestPathGraph = graph.getShortestPath(start, end);
		DijkstraShortestPath<String, Integer> dijkstras = new DijkstraShortestPath<String, Integer>(oracle, start, end);
		List<Integer> shortestPathOracle = dijkstras.getPathEdgeList();
		assertEquals(shortestPathOracle.size(), shortestPathGraph.size());
	}

}
