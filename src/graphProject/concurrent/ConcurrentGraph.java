package graphProject.concurrent;

import graphProject.Edge;
import graphProject.Graph;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class ConcurrentGraph<T> extends HashSet<T> implements Graph<T>, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2473878382917498580L;
	private final ConcurrentHashMap<T, List<Edge<T>>> edges;
	
	public ConcurrentGraph(Collection<T> nodes)
	{
		addAll(nodes);
		edges = new ConcurrentHashMap<T, List<Edge<T>>>(nodes.size(), 0.8f, 3);
	}

	public ConcurrentGraph()
	{
		edges = new ConcurrentHashMap<T, List<Edge<T>>>(100, 0.8f, 3);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object data)
	{
		if(!contains(data))
			return false;
		removeConnectionsTo((T) data);
		synchronized(this)
		{
			return super.remove(data);
		}
	}
	
	@Override
	public synchronized boolean add(T data)
	{
		return super.add(data);
	}
	
	private void removeConnectionsTo(T data)
	{
		final List<Edge<T>> edges = new ArrayList<Edge<T>>(getEdgesFor(data));
		for(Edge<T> edge:edges)
			disconnect(data, edge.getDestination());
	}

	@Override
	public double getWeight(T start, T end)
	{
		if(!isConnected(start, end))
			return -1;
		return getEdgeBetween(start, end).getWeight();
	}
	
	@Override
	public Edge<T> getEdgeBetween(T node1, T node2)
	{
		List<Edge<T>> edgeList = edges.get(node1);
		for(Edge<T> edge : edgeList)
			if(edge.getDestination().equals(node2))
				return edge;
		return null;
	}

	@Override
	public boolean connect(T start, T end, double weight)
	{
		if(!nodesExist(start, end))
			throw new NoSuchElementException();
		if(isConnected(start, end))
			changeWeight(start, end, weight);
		else
			createEdge(start, end, weight);
		return true;
	}
	
	private boolean nodesExist(T node1, T node2)
	{
		return contains(node1) && contains(node2);
	}
	
	private void changeWeight(T source, T destination, double weight)
	{
		Edge<T> edgeFromSource = getEdgeBetween(source, destination);
		edgeFromSource.setWeight(weight);
		Edge<T> edgeFromDestination = getEdgeBetween(destination, source);
		edgeFromDestination.setWeight(weight);
	}

	private void createEdge(T source, T destination, double weight)
	{
		edges.putIfAbsent(source, new ArrayList<Edge<T>>(3));
		List<Edge<T>> edgeList = edges.get(source);
		Edge<T> edge = new Edge<T>(destination, weight);
		edgeList.add(edge);
		
		edges.putIfAbsent(destination, new ArrayList<Edge<T>>(3));
		edgeList = edges.get(destination);
		edge = new Edge<T>(source, weight);
		edgeList.add(edge);
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
			throw new NoSuchElementException();
		List<Edge<T>> edges = getEdgesFor(start);
		for(Edge<T> edge:edges)
			if(edge.getDestination().equals(end))
				return true;
		return false;
	}
	
	@Override
	public int getNumberOfEdges()
	{
		int size = 0;
		for(List<Edge<T>> edgeList : edges.values())
			size += edgeList.size();
		return size/2;
	}
	
	@Override
	public List<Edge<T>> getEdgesFor(T node)
	{
		final List<Edge<T>> edgeList = edges.get(node);
		if(edgeList == null)
			return new ArrayList<Edge<T>>(1);
		return edgeList;
	}

	@Override
	public int edgeSize(T data)
	{
		return getEdgesFor(data).size();
	}

	@Override
	public Set<T> getAllNodes()
	{
		return (Set<T>) this;
	}
	
	@Override
	public T getNodeWithLeastEdges()
	{
		int lowestAmountOfEdges = Integer.MAX_VALUE;
		T nodeWitLowestAmountOfEdges = null;
		for(T node : this)
		{
			final int numberOfEdges = getEdgesFor(node).size();
			if(numberOfEdges < lowestAmountOfEdges)
				nodeWitLowestAmountOfEdges = node;
		}
		
		return nodeWitLowestAmountOfEdges;
	}

	@Override
	public Map<T, List<Edge<T>>> getAllEdges()
	{
		return edges;
	}
}
