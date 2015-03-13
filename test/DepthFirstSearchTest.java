package alda.graphProject.test;

import static org.junit.Assert.*;

import java.security.SecureRandom;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import alda.graphProject.ConcurrentGraph;
import alda.graphProject.Graph;

public class DepthFirstSearchTest
{
	Graph<String> graph;
	org.jgrapht.graph.AbstractGraph oracle;

	@Before
	public void setup()
	{
		 graph = new ConcurrentGraph<String>();
	}
	
	private void createRandomGraph(int nodes, int edges)
	{
		SecureRandom rand = new SecureRandom();
		for(int i = 0; i < nodes; i++)
		{
			graph.add("node" + i);
		}
		while(edges > 0)
		{
			String randomNode1 = "node" + rand.nextInt(nodes);
			String randomNode2 = "node" + rand.nextInt(nodes);
			graph.connect(randomNode1, randomNode2, rand.nextInt(100));
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

}
