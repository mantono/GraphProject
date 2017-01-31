package graphProject;

import static org.junit.Assert.*;
import graphProject.Graph;
import graphProject.GraphExplorer;
import graphProject.PathFinder;
import graphProject.concurrent.ConcurrentGraph;
import graphProject.generator.GraphGenerator;

import org.junit.Before;
import org.junit.Test;

public class MinimumSpanningTreeTest
{

	Graph<String> graph;
	GraphExplorer<String> explorer;
	GraphGenerator gg;

	@Before
	public void setup()
	{
		graph = new ConcurrentGraph<String>();
		explorer = new PathFinder<String>(graph);
	}

	@Test
	public void testJozefsKlassiskaPROG2GrafFörDijkstrasAlgortimMedKruskals()
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
		Graph<String> mst = explorer.getMinimumSpanningTree();
		assertEquals(7, mst.size());
		assertEquals(6, mst.getNumberOfEdges());
	}

	@Test
	public void testJozefsKlassiskaPROG2GrafFörDijkstrasAlgortimMedPrims()
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
		Graph<String> mst = explorer.getMinimumSpanningTreePrims();
		assertEquals(7, mst.size());
		assertEquals(6, mst.getNumberOfEdges());
	}
	
	@Test
	public void testIsConnectedOnSmallConnectedGraph()
	{
		graph.add("A");
		graph.add("B");
		graph.add("C");
		graph.add("D");

		graph.connect("A", "B", 1);
		graph.connect("B", "C", 1);
		graph.connect("C", "D", 1);

		PathFinder<String> explorer = new PathFinder<String>(graph);
		assertTrue(explorer.isConnected());
	}
	
	@Test
	public void testIsConnectedOnSmallNotConnectedGraph()
	{
		graph.add("A");
		graph.add("B");
		graph.add("C");
		graph.add("D");

		graph.connect("A", "B", 1);
		graph.connect("C", "D", 1);

		PathFinder<String> explorer = new PathFinder<String>(graph);
		assertFalse(explorer.isConnected());
	}
	
	@Test
	public void testIsConnectedOnLargeConnectedGraph()
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
		assertTrue(explorer.isConnected());
	}
	
	@Test
	public void testIsConnectedOnLargeNotConnectedGraph()
	{
		graph.add("A");
		graph.add("B");
		graph.add("C");
		graph.add("D");
		graph.add("E");
		graph.add("F"); // Not connected
		graph.add("G");

		graph.connect("A", "B", 3);
		graph.connect("A", "C", 7);
		graph.connect("A", "D", 12);
		graph.connect("B", "C", 11);
		graph.connect("C", "D", 12);
		graph.connect("C", "G", 2);
		graph.connect("D", "E", 1);
		graph.connect("D", "G", 6);
		graph.connect("E", "G", 15);

		PathFinder<String> explorer = new PathFinder<String>(graph);
		assertFalse(explorer.isConnected());
	}
}
