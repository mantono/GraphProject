package graphProject.test;

import static org.junit.Assert.*;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import graphProject.Graph;
import graphProject.GraphExplorer;
import graphProject.PathFinder;
import graphProject.concurrent.ConcurrentGraph;

public class ConcurrentGraphTestWithALDATestCases
{

	Graph<String> graph = new ConcurrentGraph<String>();

	private void add(String... nodes) {
		for (String node : nodes) {
			assertTrue(graph.add(node));
		}
	}

	private void connect(String node1, String node2, int cost) {
		assertTrue(graph.connect(node1, node2, cost));
		assertEquals(cost, graph.getWeight(node1, node2));
		assertEquals(cost, graph.getWeight(node2, node1));
	}

	private void addExampleNodes() {
		add("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
	}

	@Test
	public void testAdd() {
		addExampleNodes();
		assertFalse(graph.add("D"));
		assertFalse(graph.add("J"));
		assertTrue(graph.add("K"));
	}

	@Test
	public void testConnect() {
		addExampleNodes();
		assertFalse(graph.isConnected("A", "Z"));
		assertFalse(graph.connect("A", "Z", 5));
		assertEquals(-1, graph.getWeight("A", "Z"));
		assertFalse(graph.connect("X", "B", 5));
		assertEquals(-1, graph.getWeight("X", "B"));
		assertEquals(-1, graph.getWeight("B", "X"));

		assertFalse(graph.isConnected("A", "G"));
		assertFalse(graph.isConnected("G", "A"));
		assertTrue(graph.connect("A", "G", 5));
		assertTrue(graph.isConnected("A", "G"));
		assertTrue(graph.isConnected("G", "A"));
		assertEquals(5, graph.getWeight("A", "G"));
		assertEquals(5, graph.getWeight("G", "A"));
		assertTrue(graph.connect("G", "A", 3));
		assertEquals(3, graph.getWeight("A", "G"));
		assertEquals(3, graph.getWeight("G", "A"));
	}
	
	@Test
	public void testNumberOfNodes()
	{
		addExampleNodes();
		assertEquals(10, graph.size());
	}
	
	@Test
	public void testNumberOfEdges()
	{
		addExampleNodes();
		assertEquals(0, graph.getNumberOfEdges());

		assertTrue(graph.connect("A", "G", 5));
		assertTrue(graph.connect("G", "A", 3));
		assertEquals(1, graph.getNumberOfEdges());
		
		assertTrue(graph.connect("A", "B", 5));
		assertTrue(graph.connect("E", "C", 12));
		assertEquals(3, graph.getNumberOfEdges());
		
	}

	// Nedanst�ende kod �r skriven i ett format f�r att beskriva grafer som
	// heter dot och kan anv�ndas om ni vill ha en bild av den graf som
	// nedanst�ende test anv�nder. Det finns flera program och webbsidor man kan
	// anv�nda f�r att omvandla koden till en bild, bland annat
	// http://sandbox.kidstrythisathome.com/erdos/

	// Observera dock att vi kommer att k�ra testfall p� andra och betydligt
	// st�rre grafer.

	// @formatter:off
//	graph G {
//	A -- A [label=1]; A -- G [label=3]; G -- B [label=28];
//	B -- F [label=5]; F -- F [label=3]; F -- H [label=1];
//	H -- D [label=1]; H -- I [label=3]; D -- I [label=1];
//	B -- D [label=2]; B -- C [label=3]; C -- D [label=5];
//	E -- C [label=2]; E -- D [label=2]; J -- D [label=5];
//	}
	// @formatter:on

	private void createExampleGraph() {
		addExampleNodes();

		connect("A", "A", 1);
		connect("A", "G", 3);
		connect("G", "B", 28);
		connect("B", "F", 5);
		connect("F", "F", 3);
		connect("F", "H", 1);
		connect("H", "D", 1);
		connect("H", "I", 3);
		connect("D", "I", 1);
		connect("B", "D", 2);
		connect("B", "C", 3);
		connect("C", "D", 5);
		connect("E", "C", 2);
		connect("E", "D", 2);
		connect("J", "D", 5);
	}
	
	private void createExampleGraphBig(int i)
	{
		addExampleNodesBig(i);
		SecureRandom rand = new SecureRandom();
		connect("A", "node0", 5);
		connect("B", "node0", 10);
		for(int n = 0; n < i*3; n++)
		{
			int node1 = rand.nextInt(i);
			int node2 = rand.nextInt(i);
			int weight = rand.nextInt(25);
			connect("node" + node1, "node" + node2, weight);
		}
	}

	private void addExampleNodesBig(int numberOfNodes)
	{
		for(int i = 0; i < numberOfNodes; i++)
			add("node" + i);
	}

	private void testPath(String start, String end, List<String> path) {
		assertEquals(start, path.get(0));
		assertEquals(end, path.get(path.size() - 1));

		String previous = start;
		for (int i = 1; i < path.size(); i++) {
			assertTrue(graph.isConnected(previous, path.get(i)));
			previous = path.get(i);
		}

		Set<String> nodesInPath = new HashSet<String>(path);
		assertEquals(path.size(), nodesInPath.size());
	}

	private void testDepthFirstSearch(String start, String end, int minimumPathLength) {
		createExampleGraph();
		GraphExplorer<String> explorer = new PathFinder<String>(graph);
		List<String> path = explorer.depthFirstSearch(start, end);

		assertTrue(path.size() >= minimumPathLength);
		assertTrue(path.size() <= graph.size());

		testPath(start, end, path);
	}

	@Test
	public void testDepthFirstSearchFromAToJ() {
		testDepthFirstSearch("A", "J", 5);
	}

	@Test
	public void testDepthFirstSearchFromJToA() {
		testDepthFirstSearch("J", "A", 5);
	}

	@Test
	public void testDepthFirstSearchFromFToE() {
		testDepthFirstSearch("F", "E", 3);
	}

	@Test
	public void testDepthFirstSearchFromFToF() {
		testDepthFirstSearch("F", "E", 1);
	}

}
