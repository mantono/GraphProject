package alda.graphProject.concurrent;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.PriorityBlockingQueue;

import alda.graphProject.Edge;
import alda.graphProject.Graph;
import alda.graphProject.GraphExplorer;
import alda.graphProject.concurrent.ImmutablePathRecord;

public class ConcurrentPathFinder<T> implements GraphExplorer<T>
{
	private final Graph<T> graph;
	private Set<T> visitedNodes; //TODO Kolla om volatile behövs eller inte. Data kan bli osynkrioniserad mellan trådar, men skadar det algoritmen?
	private Queue<ImmutablePathRecord<T>> nodes;
	private ConcurrentMap<T, T> nodePath;
	
	public ConcurrentPathFinder(Graph<T> graph)
	{
		this.graph = graph;
	}

	@Override
	public List<Edge<T>> getShortestPath(T start, T end)
	{
		final int numberOfNodes = graph.size();
		setupDataStructures();
		createPathRecords(start);
		ImmutablePathRecord<T> currentRecord = new ImmutablePathRecord<T>(start, start, 0);
		while(visitedNodes.size() < numberOfNodes)
		{
			visitedNodes.add(currentRecord.getNode());
			updateEdges(currentRecord);
			writePathRecord(currentRecord);
			currentRecord = getNextNode();
		}
		return buildPath(start, end);
	}
	
	private void writePathRecord(ImmutablePathRecord<T> currentRecord)
	{
		nodePath.put(currentRecord.getNode(), currentRecord.getNodeReachedThrough());
	}

	private void createPathRecords(T start)
	{
		for(T node:graph.getAllNodes())
			if(node != start)
				nodes.add(new ImmutablePathRecord<T>(node));
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
		nodePath = new ConcurrentHashMap<T, T>(graph.size());
		visitedNodes = new HashSet<T>(graph.size());
		nodes = new PriorityBlockingQueue<ImmutablePathRecord<T>>();
	}
	
	private void updateEdges(ImmutablePathRecord<T> currentRecord)
	{
		int currentWeight = currentRecord.getWeight();
		List<Edge<T>> connectingEdges = graph.getEdgesFor(currentRecord.getNode());
		for(Edge<T> edge:connectingEdges)
		{
			int newWeightForNode = currentWeight + edge.getWeight();
			nodes.add(new ImmutablePathRecord<T>(edge.getDestination(), currentRecord.getNode(), newWeightForNode));
		}
		
	}

	private List<Edge<T>> buildPath(T start, T end)
	{
		if(!nodePath.containsKey(end))
			throw new IllegalStateException("Graph does not have a path from " + start + " to " + end);
		List<Edge<T>> path = new LinkedList<Edge<T>>();
		T node = end;
		while(!node.equals(start))
		{
			T cameFrom = nodePath.get(node);
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

	private ImmutablePathRecord<T> getNextNode()
	{
		ImmutablePathRecord<T> next;
		do
		{
			next = nodes.poll();
		}while(visitedNodes.contains(next.getNode()) && !nodes.isEmpty());
		return next;
	}
}
