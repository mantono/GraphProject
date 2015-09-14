package graphProject.concurrent;

import graphProject.Edge;
import graphProject.Graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ConcurrentGraph<T> implements Graph<T>
{
	//private final Map<T, List<Edge<T>>> nodeConnections = new HashMap<T, List<Edge<T>>>();
	private final Set<T> nodes = new HashSet<T>();
	private final Set<T> edges = new HashSet<T>();
	
	public ConcurrentGraph(Collection<T> nodes)
	{
		this.nodes.addAll(nodes);
	}

	public ConcurrentGraph()
	{
	}

	@Override
	public boolean add(T data)
	{
		return nodes.add(data);
	}

	@Override
	public boolean remove(T data)
	{
		if(!nodes.contains(data))
			return false;
		removeConnectionsTo(data);
		return nodes.remove(data);
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
		return nodes.contains(data);
	}

	@Override
	public int getWeight(T start, T end)
	{
		if(!isConnected(start, end))
			return -1;
		return getEdgeBetween(start, end).getWeight();
	}
	
	@Override
	public Edge<T> getEdgeBetween(T node1, T node2)
	{
		List<Edge<T>> edgesNode1 = nodeConnections.get(node1);
		for(Edge<T> edge:edgesNode1)
		{
			if(edge.getDestination().equals(node2))
				return edge;
		}
		return null;
	}

	@Override
	public int size()
	{
		return nodeConnections.size();
	}
	
	@Override
	public void clear()
	{
		nodeConnections.clear();
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
		if(nodeConnections.containsKey(node1) && nodeConnections.containsKey(node2))
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
		List<Edge<T>> edges = nodeConnections.get(source);
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
		for(List<Edge<T>> edges : nodeConnections.values())
			edgeCount += edges.size();
		return edgeCount/2;
	}
	
	@Override
	public List<Edge<T>> getEdgesFor(T node)
	{
		return nodeConnections.get(node);
	}

	@Override
	public int edgeSize(T data)
	{
		return getEdgesFor(data).size();
	}

	@Override
	public Set<T> getAllNodes()
	{
		return nodeConnections.keySet();
	}

	@Override
	public int getNumberOfNodes()
	{
		return nodeConnections.size();
	}

	@Override
	public T getNodeWithLeastEdges()
	{
		int lowestAmountOfEdges = Integer.MAX_VALUE;
		T nodeWitLowestAmountOfEdges = null;
		for(T node : nodeConnections.keySet())
		{
			final int numberOfEdges = nodeConnections.get(node).size();
			if(numberOfEdges < lowestAmountOfEdges)
				nodeWitLowestAmountOfEdges = node;
		}
		
		return nodeWitLowestAmountOfEdges;
	}

	@Override
	public Set<Edge<T>> getAllEdges()
	{
		Set<Edge<T>> allEdges = new HashSet<Edge<T>>();
		for(List<Edge<T>> edges : nodeConnections.values())
			allEdges.addAll(edges);
		return allEdges;
	}
}
