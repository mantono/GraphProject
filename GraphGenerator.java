package alda.graphProject;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GraphGenerator
{
	private Graph<String> graph;
	
	public Graph<String> generateConnectedGraph(int nodes, int edges, int maxWeight)
	{	
		SecureRandom rand = new SecureRandom();
		graph = new ConcurrentGraph<String>();
		createNodes(nodes);
		
		List<String> connectedNodes = new ArrayList<String>(nodes/2);
		connectedNodes.add("node0");
		
		while(connectedNodes.size() < nodes)
		{
			String randomNode1 = getRandomConnctedNode(connectedNodes, rand.nextInt(connectedNodes.size()));
			String randomNode2 = "node" + connectedNodes.size();
			int weight = rand.nextInt(maxWeight) + 1;
			if(!randomNode1.equals(randomNode2))
			{
				graph.connect(randomNode1, randomNode2, weight);
				connectedNodes.add(randomNode2);
			}
		}
		edges -= connectedNodes.size();
		while(edges > 0)
		{
			String randomNode1 = getRandomConnctedNode(connectedNodes, rand.nextInt(connectedNodes.size()));
			String randomNode2 = getRandomConnctedNode(connectedNodes, rand.nextInt(connectedNodes.size()));
			int weight = rand.nextInt(maxWeight) + 1;
			if(!randomNode1.equals(randomNode2))
			{
				graph.connect(randomNode1, randomNode2, weight);
				connectedNodes.add(randomNode2);
				edges--;
			}
		}
			
		System.out.println("Graph created with " + graph.size() + " nodes and " + graph.getNumberOfEdges() + " edges ");
		return graph;
	}
	
	public void printGraph()
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
	
	private void createNodes(int amount)
	{
		for(int i = 0; i < amount; i++)
			graph.add("node" + i);
	}

	private String getRandomConnctedNode(List<String> connectedNodes, int nextInt)
	{
		if(nextInt < connectedNodes.size()/2)
			nextInt += connectedNodes.size()/2;
		return connectedNodes.get(nextInt % connectedNodes.size());
	}
}
