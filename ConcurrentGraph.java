package alda.graphProject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.PriorityBlockingQueue;


public class ConcurrentGraph<T> implements Graph<T>
{
	private final Map<T, List<Edge<T>>> nodes = new HashMap<T, List<Edge<T>>>();

	@Override
	public boolean add(T data)
	{
		if(nodes.containsKey(data))
			return false;
		nodes.put(data, new ArrayList<Edge<T>>());
		return true;
	}

	@Override
	public boolean remove(T data)
	{
		if(!nodes.containsKey(data))
			return false;
		removeConnectionsTo(data);
		nodes.remove(data);
		return true;
	}
	
	private void removeConnectionsTo(T data)
	{
		final List<Edge<T>> edges = new ArrayList<Edge<T>>(getEdgesFor(data));
		for(Edge<T> edge:edges)
			disconnect(data, edge.getDestination());
	}

	@Override
	public boolean contains(T data)
	{
		return nodes.containsKey(data);
	}

	@Override
	public boolean hasPath(T start, T end)
	{
		List<T> visitedNodes = depthFirstSearch(start, end);
		return visitedNodes.contains(end);
	}

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
		List<Edge<T>> edges = getEdgesFor(path.peek());
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

	@Override
	public int getWeight(T start, T end)
	{
		if(!isConnected(start, end))
			return -1;
		return getEdgeBetween(start, end).getWeight();
	}
	
	private Edge<T> getEdgeBetween(T node1, T node2)
	{
		List<Edge<T>> edgesNode1 = nodes.get(node1);
		for(Edge<T> edge:edgesNode1)
		{
			if(edge.getDestination().equals(node2))
				return edge;
		}
		return null;
	}

	@Override
	public List<Edge<T>> getShortestPath(T start, T end)
	{
		Queue<Edge<T>> edgesToWalk = new PriorityBlockingQueue<Edge<T>>();
		Map<T, PathRecord<T>> pathTable = new HashMap<T, PathRecord<T>>();
		pathTable.put(start, new PathRecord<T>(start, 0));
		
		edgesToWalk.addAll(getEdgesFor(start));
		T currentNode = start;
		while(!edgesToWalk.isEmpty())
		{
			
		}
		return null;
	}

	@Override
	public int size()
	{
		return nodes.size();
	}
	
	@Override
	public void clear()
	{
		nodes.clear();
	}	

	@Override
	public boolean connect(T start, T end, int weight)
	{
		if(!nodesExist(start, end))
			return false;
		if(isConnected(start, end))
			changeWeight(start, end, weight);
		else
		{
			createEdge(start, end, weight);
			createEdge(end, start, weight);
		}
		return true;
	}
	
	private boolean nodesExist(T node1, T node2)
	{
		if(nodes.containsKey(node1) && nodes.containsKey(node2))
			return true;
		return false;
	}
	
	private void changeWeight(T source, T destination, int weight)
	{
		Edge<T> edgeFromSource = getEdgeBetween(source, destination);
		edgeFromSource.setWeight(weight);
		Edge<T> edgeFromDestination = getEdgeBetween(destination, source);
		edgeFromDestination.setWeight(weight);
	}

	private void createEdge(T source, T destination, int weight)
	{
		List<Edge<T>> edges = nodes.get(source);
		if(edges == null)
			edges = new ArrayList<Edge<T>>();
		Edge<T> edge = new Edge<T>(destination, weight);
		if(!edges.contains(edge))
			edges.add(edge);
	}

	@Override
	public boolean disconnect(T start, T end)
	{
		if(!isConnected(start, end))
			return false;
		removeEdge(start, end);
		removeEdge(end, start);
		return true;
	}
	
	private void removeEdge(T source, T destination)
	{
		Iterator<Edge<T>> edgeIterator = getEdgesFor(source).iterator();
		while(edgeIterator.hasNext())
		{
			if(edgeIterator.next().getDestination().equals(destination))
			{
				edgeIterator.remove();
				return;
			}
		}
	}

	@Override
	public boolean isConnected(T start, T end)
	{
		if(!nodesExist(start, end))
			return false;
		List<Edge<T>> edges = getEdgesFor(start);
		for(Edge<T> edge:edges)
		{
			if(edge.getDestination().equals(end))
				return true;
		}
		return false;
	}
	
	@Override
	public int getNumberOfEdges()
	{
		int edgeCount = 0;
		for(List<Edge<T>> edges : nodes.values())
			edgeCount += edges.size();
		return edgeCount/2;
	}
	
	@Override
	public List<Edge<T>> getEdgesFor(T node)
	{
		return nodes.get(node);
	}

	@Override
	public int edgeSize(T data)
	{
		return getEdgesFor(data).size();
	}
}
