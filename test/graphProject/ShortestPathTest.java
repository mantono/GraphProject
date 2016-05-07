package graphProject;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import graphProject.*;
import graphProject.concurrent.ConcurrentGraph;
import graphProject.generator.GraphGenerator;

public class ShortestPathTest
{
	Graph<String> graph;
	GraphExplorer<String> explorer;
	GraphGenerator gg;

	@Before
	public void setup()
	{
		graph = new ConcurrentGraph<String>();
		gg = new GraphGenerator();
		explorer = new PathFinder<String>(graph);
	}
	
	private void createNodes(int amount)
	{
		for(int i = 0; i < amount; i++)
			graph.add("node" + i);
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

		int result = explorer.getTotalWeight("C", "F");
		assertEquals(4, result);
		
		result = explorer.getTotalWeight("A", "G");
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
		graph.connect("node13", "node30", 14);
		graph.connect("node13", "node43", 1);
		graph.connect("node13", "node48", 11);
		graph.connect("node48", "node46", 9);
		
		int sum = explorer.getTotalWeight("node11", "node7");
		assertEquals(189, sum);
	}
	
	@Test
	public void stressTestWithNoAsserts()
	{
		for(int i = 0; i < 50; i++)
		{
			int m = (i/4)+1;
			graph = gg.generateConnectedGraph(1000*m, 3000*m, 100);
			explorer = new PathFinder<String>(graph);
			explorer.getShortestPath("node0", "node800");
			assertTrue(explorer.hasPath("node0", "node500"));
		}
	}
	
	
	@Test
	public void testHugeGraphWithNoAsserts()
	{
		graph = gg.generateConnectedGraph(2_000_000, 2_270_000, 100);
		explorer = new PathFinder<String>(graph);
		List<Edge<String>> result = explorer.getShortestPath("node0", "node400000");
		System.out.println(result);
	}

}
