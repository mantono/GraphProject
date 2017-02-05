package graphProject.concurrent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.mantono.RandomHashSet;

import graphProject.Edge;
import graphProject.Graph;

public class ConcurrentGraph<T> implements Graph<T>, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2473878382917498580L;
	private final RandomHashSet<T> nodes;
	private final Map<T, List<Edge<T>>> edges;
	private final boolean directed, allowNegativeWeights;
	private final ReentrantReadWriteLock dataLock = new ReentrantReadWriteLock(true);
	private final Lock writeLock = dataLock.writeLock();
	private final Lock readLock = dataLock.readLock();

	public ConcurrentGraph(Graph<T> graph)
	{
		this.nodes = new RandomHashSet<T>(graph.getAllNodes());
		this.edges = graph.getAllEdges();
		this.directed = graph.isDirected();
		this.allowNegativeWeights = graph.allowsNegativeWeights();
	}

	public ConcurrentGraph(Collection<T> nodes, boolean directed, boolean allowNegativeWeights)
	{
		this.nodes = new RandomHashSet<T>(nodes);
		this.edges = new HashMap<T, List<Edge<T>>>(nodes.size() * 2, 0.8f);
		this.directed = directed;
		this.allowNegativeWeights = allowNegativeWeights;
	}

	public ConcurrentGraph(Collection<T> nodes)
	{
		this(nodes, false, false);
	}

	public ConcurrentGraph(int initialCapacity)
	{
		this(initialCapacity, false, false);
	}
	
	public ConcurrentGraph(int initialCapacity, boolean directed)
	{
		this(initialCapacity, directed, false);
	}

	public ConcurrentGraph(int initialCapacity, boolean directed, boolean allowNegativeWeights)
	{
		this.nodes = new RandomHashSet<T>(initialCapacity);
		this.edges = new HashMap<T, List<Edge<T>>>(initialCapacity*2, 0.8f);
		this.directed = directed;
		this.allowNegativeWeights = allowNegativeWeights;
	}

	public ConcurrentGraph(boolean directed, boolean allowNegativeWeights)
	{
		this(50, directed, allowNegativeWeights);
	}

	public ConcurrentGraph(boolean directed)
	{
		this(50, directed, false);
	}

	public ConcurrentGraph()
	{
		this(50, false, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object data)
	{
		try
		{
			writeLock.lock();
			if(!contains(data))
				return false;
			removeConnectionsTo((T) data);
			return nodes.remove(data);
		}
		finally
		{
			writeLock.unlock();
		}
	}

	private void removeConnectionsTo(T data)
	{
		final List<Edge<T>> edges = new ArrayList<Edge<T>>(getEdgesFor(data));
		for(Edge<T> edge : edges)
			disconnect(data, edge.getDestination());
	}

	@Override
	public double getWeight(T source, T destination)
	{

		return getEdgeBetween(source, destination).getWeight();
	}

	@Override
	public Edge<T> getEdgeBetween(T source, T destination)
	{
		try
		{
			readLock.lock();
			List<Edge<T>> edgeList = edges.get(source);
			for(Edge<T> edge : edgeList)
				if(edge.getDestination().equals(destination))
					return edge;
			throw new IllegalArgumentException("There is no edge between nodes " + source + " and " + destination);
		}
		finally
		{
			readLock.unlock();
		}
	}

	@Override
	public boolean connect(T source, T destination, double weight)
	{
		try
		{
			writeLock.lock();
			if(!nodesExist(source, destination))
				throw new NoSuchElementException();
			if(isConnected(source, destination))
				return false;
			return createEdge(source, destination, weight);
		}
		finally
		{
			writeLock.unlock();
		}
	}

	private boolean nodesExist(T source, T destination)
	{
		try
		{
			readLock.lock();
			return contains(source) && contains(destination);
		}
		finally
		{
			readLock.unlock();
		}
	}

	public boolean changeWeight(T source, T destination, double weight)
	{
		if(!allowsNegativeWeights() && weight < 0)
			throw new IllegalArgumentException("Weight must not be negative");

		try
		{
			writeLock.lock();

			Edge<T> edgeFromSource = getEdgeBetween(source, destination);
			final boolean changed = edgeFromSource.setWeight(weight);

			if(isDirected())
				return changed;

			Edge<T> edgeFromDestination = getEdgeBetween(destination, source);
			return changed && edgeFromDestination.setWeight(weight);
		}
		finally
		{
			writeLock.unlock();
		}
	}

	private boolean createEdge(T source, T destination, double weight)
	{
		if(!allowsNegativeWeights() && weight < 0)
			throw new IllegalArgumentException("Weight must not be negative");

		try
		{
			writeLock.lock();
			edges.putIfAbsent(source, new ArrayList<Edge<T>>(3));
			List<Edge<T>> edgeList = edges.get(source);
			Edge<T> edge = new Edge<T>(source, destination, weight);
			final boolean added = edgeList.add(edge);

			if(isDirected())
				return added;

			edges.putIfAbsent(destination, new ArrayList<Edge<T>>(3));
			edgeList = edges.get(destination);
			edge = new Edge<T>(destination, source, weight);
			return added && edgeList.add(edge);
		}
		finally
		{
			writeLock.unlock();
		}
	}

	@Override
	public boolean disconnect(T start, T end)
	{
		try
		{
			writeLock.lock();
			if(!isConnected(start, end))
				return false;
			removeEdge(start, end);
			if(!isDirected())
				removeEdge(end, start);
			return true;
		}
		finally
		{
			writeLock.unlock();
		}
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
	public boolean isConnected(T node)
	{
		try
		{
			readLock.lock();
			return contains(node) && edges.get(node) != null && !edges.get(node).isEmpty();
		}
		finally
		{
			readLock.unlock();
		}
	}

	@Override
	public boolean isConnected(T start, T end)
	{
		try
		{
			readLock.lock();
			if(!nodesExist(start, end))
				throw new NoSuchElementException();
			List<Edge<T>> edges = getEdgesFor(start);
			for(Edge<T> edge : edges)
				if(edge.getDestination().equals(end))
					return true;
			return false;
		}
		finally
		{
			readLock.unlock();
		}
	}

	@Override
	public int getNumberOfEdges()
	{
		try
		{
			readLock.lock();
			int size = 0;
			for(List<Edge<T>> edgeList : edges.values())
				size += edgeList.size();
			if(!isDirected())
				size /= 2;
			return size;
		}
		finally
		{
			readLock.unlock();
		}
	}

	@Override
	public List<Edge<T>> getEdgesFor(T node)
	{
		try
		{
			readLock.lock();
			final List<Edge<T>> edgeList = edges.get(node);
			if(edgeList == null)
				return new ArrayList<Edge<T>>(1);
			return edgeList;
		}
		finally
		{
			readLock.unlock();
		}
	}

	@Override
	public int edgeSize(T data)
	{
		return getEdgesFor(data).size();
	}

	@Override
	public Set<T> getAllNodes()
	{
		return new RandomHashSet<T>(nodes);
	}

	@Override
	public T getNodeWithLeastEdges()
	{
		try
		{
			readLock.lock();
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
		finally
		{
			readLock.unlock();
		}
	}

	@Override
	public Map<T, List<Edge<T>>> getAllEdges()
	{
		try
		{
			readLock.lock();
			return new HashMap<T, List<Edge<T>>>(edges);
		}
		finally
		{
			readLock.unlock();
		}
	}

	@Override
	public boolean isDirected()
	{
		return directed;
	}

	@Override
	public boolean allowsNegativeWeights()
	{
		return allowNegativeWeights;
	}

	@Override
	public boolean addAll(Collection<? extends T> c)
	{
		return nodes.addAll(c);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{

		return nodes.containsAll(c);
	}

	@Override
	public boolean isEmpty()
	{
		return nodes.isEmpty();
	}

	@Override
	public Iterator<T> iterator()
	{
		return nodes.iterator();
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return nodes.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return nodes.retainAll(c);
	}

	@Override
	public Object[] toArray()
	{
		return nodes.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return nodes.toArray(a);
	}

	@Override
	public T getRandomElement()
	{
		return nodes.getRandomElement();
	}

	@Override
	public boolean add(T data)
	{
		return nodes.add(data);
	}

	@Override
	public boolean contains(Object data)
	{
		return nodes.contains(data);
	}

	@Override
	public int size()
	{
		return nodes.size();
	}

	@Override
	public void clear()
	{
		try
		{
			writeLock.lock();
			nodes.clear();
			edges.clear();
		}
		finally
		{
			writeLock.unlock();
		}
	}
}
