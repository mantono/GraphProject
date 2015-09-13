package graphProject.test;

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

	@Before
	public void setUp() throws Exception
	{
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
		Graph<String> mst = explorer.getMinimumSpanningTree();
		assertEquals(6, mst.getNumberOfEdges());
	}

}
