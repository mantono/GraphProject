package alda.graphProject;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

public class PathFinder<T> implements GraphExplorer<T>
{
	private final Graph<T> graph;
	private Map<T, PathRecord<T>> pathTable;
	private Set<T> visitedNodes;
	
	public PathFinder(Graph<T> graph)
	{
		this.graph = graph;
	}
	
	@Override
	public List<Edge<T>> getShortestPath(T start, T end)
	{
		final int numberOfNodes = graph.size();
		setupDataStructures();
		createPathRecords(start);
		T currentNode = start;
		while(visitedNodes.size() < numberOfNodes)
		{
			visitedNodes.add(currentNode);
			updateEdges(currentNode);
			currentNode = getNextNode();
		}
		return buildPath(pathTable, start, end);
	}
	
	private void createPathRecords(T start)
	{
		for(T node:graph.getAllNodes())
			pathTable.put(node, new PathRecord<T>(node, graph.getEdgesFor(node).size()));
		pathTable.get(start).updateRecord(start, 0);
	}

	@Override
	public int getTotalWeight(T start, T end)
	{
		List<Edge<T>> path = getShortestPath(start, end);
		int weight = 0;
		for(Edge<T> edge:path)
			weight += edge.getWeight();
		return weight;
	}
	
	private void setupDataStructures()
	{
		pathTable = new HashMap<T, PathRecord<T>>();
		visitedNodes = new HashSet<T>(graph.size());
	}
	
	private void updateEdges(T currentNode)
	{
		int currentWeight = pathTable.get(currentNode).getCurrentWeight();
		List<Edge<T>> connectingEdges = graph.getEdgesFor(currentNode);
		for(Edge<T> edge:connectingEdges)
		{
			int newWeightForNode = currentWeight + edge.getWeight();
			pathTable.get(edge.getDestination()).updateRecord(currentNode, newWeightForNode);
		}
	}

	private List<Edge<T>> buildPath(Map<T, PathRecord<T>> pathTable, T start, T end)
	{
		if(!pathTable.containsKey(end))
			throw new IllegalStateException("Graph does not have a path from " + start + " to " + end);
		List<Edge<T>> path = new LinkedList<Edge<T>>();
		T node = end;
		while(!node.equals(start))
		{
			T cameFrom = pathTable.get(node).getNodeReachedThrough();
			path.add(graph.getEdgeBetween(node, cameFrom));
			node = cameFrom;
		}
		Collections.reverse(path);
		return path;
	}
	
	@Override
	public boolean hasPath(T start, T end)
	{
		List<T> visitedNodes = depthFirstSearch(start, end);
		return visitedNodes.contains(end);
	}

	@Override
	public List<T> depthFirstSearch(T start, T end)
	{
		Set<T> visitedNodes = new HashSet<T>();
		Stack<T> path = new Stack<T>();
		path.push(start);
		T currentNode;
		while(!path.isEmpty())
		{
			currentNode = path.peek();
			if(currentNode.equals(end))
				return path;
			visitedNodes.add(currentNode);
			if(!pushNextUnvisitedNode(path, visitedNodes))
				path.pop();
		}			
		return path;
	}

	private boolean pushNextUnvisitedNode(Stack<T> path, Set<T> visitedNodes)
	{
		List<Edge<T>> edges = graph.getEdgesFor(path.peek());
		for(Edge<T> edge:edges)
		{
			if(!visitedNodes.contains(edge.getDestination()))
			{
				path.push(edge.getDestination());
				return true;
			}
		}
		return false;
	}

	private T getNextNode()
	{
		PathRecord<T> currentLowest = new PathRecord<T>(null, 0);
		for(PathRecord<T> record :pathTable.values())
 			if(record.compareTo(currentLowest) <= 0 && !visitedNodes.contains(record.getNode()))
				currentLowest = record;
		return currentLowest.getNode();
	}
}
