package graphProject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import graphProject.concurrent.ConcurrentGraph;

public class PathFinder<T> implements GraphExplorer<T>
{
	private final Graph<T> graph;
	private Set<T> visitedNodes;
	private Queue<PathRecord<T>> nodes;
	private Map<T, T> nodePath;
	private Map<T, Double> nodeWeight;
	
	public PathFinder(Graph<T> graph)
	{
		this.graph = graph;
	}
	
	
	private void setupDataStructures()
	{
		nodePath = new HashMap<T, T>(graph.size());
		nodeWeight = new HashMap<T, Double>(graph.size());
		visitedNodes = new HashSet<T>(graph.size());
		nodes = new PriorityQueue<PathRecord<T>>();
	}
	

	@Override
	public List<Edge<T>> getShortestPath(T start, T end)
	{
		checkIfValidGraph(start, end);
		setupDataStructures();
		createWeightRecords(start);
		PathRecord<T> currentRecord = new PathRecord<T>(start, start, 0);
		nodes.add(currentRecord);
		while(!nodes.isEmpty())
		{
			currentRecord = getNextNode();
			updateEdges(currentRecord);
			visitedNodes.add(currentRecord.getNode());
		}
		assert visitedNodes.size() == graph.size() : "Only visited " + visitedNodes.size() + " out of " + graph.size();
		return buildPath(start, end);
	}

	private void checkIfValidGraph(T start, T end)
	{
		if(graph.size() == 0)
			throw new IllegalStateException("Graph does not have any nodes!");
		if(graph.getNumberOfEdges() == 0)
			throw new IllegalStateException("Graph does not have any edges!");
		if(!graph.getAllNodes().contains(start))
			throw new IllegalArgumentException("Start node " + start + " is not in the graph.");
		if(!graph.getAllNodes().contains(end))
			throw new IllegalArgumentException("Start node " + end + " is not in the graph.");
		if(!hasPath(start, end))
			throw new IllegalArgumentException("There is no path between " + start + " and " + end);
	}


	private void createWeightRecords(T start)
	{
		nodeWeight.put(start, 0.0);
		for(T node:graph.getAllNodes())
			if(!node.equals(start))
				nodeWeight.put(node, Double.MAX_VALUE);
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

	private void updateEdges(PathRecord<T> currentRecord)
	{
		double currentWeight = nodeWeight.get(currentRecord.getNode());
		List<Edge<T>> connectingEdges = graph.getEdgesFor(currentRecord.getNode());
		for(Edge<T> edge:connectingEdges)
		{
			double newWeightForNode = currentWeight + edge.getWeight();
			if(updatePathRecord(currentRecord.getNode(), edge.getDestination(), newWeightForNode) && !visitedNodes.contains(edge.getDestination())) 
				nodes.add(new PathRecord<T>(edge.getDestination(), currentRecord.getNode(), newWeightForNode));
		}		
	}

	private boolean updatePathRecord(T node, T destination, double newWeightForNode)
	{
		double currentWeightForNode = nodeWeight.get(destination);
		if(newWeightForNode < currentWeightForNode)
		{
			nodePath.put(destination, node);
			nodeWeight.put(destination, newWeightForNode);
			return true;
		}
		return false;
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
			assert !node.equals(cameFrom) : node + " has a reference to itself. This should not be possible!";
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

	private PathRecord<T> getNextNode()
	{
		PathRecord<T> next;
		do
		{
			next = nodes.poll();
		}while(!nodes.isEmpty() && visitedNodes.contains(next.getNode()));
		return next;
	}

	@Override
	/**
	 * Using Kruskal's algorithm.
	 */
	public Graph<T> getMinimumSpanningTree()
	{
		setupDataStructures();
		Graph<T> mst = new ConcurrentGraph<T>(graph.getAllNodes());
		PathFinder<T> mstPath = new PathFinder<T>(mst);
		final Set<Edge<T>> allEdges = getAllEdges(); 
		Queue<Edge<T>> edges = new PriorityQueue<Edge<T>>(allEdges);
		final int nodesInCompleteGraph = graph.size();
		
		while(mst.getNumberOfEdges() + 1 < nodesInCompleteGraph && !edges.isEmpty())
		{
			Edge<T> edge = edges.poll();
			final T source = edge.getSource();
			final T destination = edge.getDestination();
			if(!mstPath.hasPath(source, destination))
				mst.connect(source, destination, edge.getWeight());
		}

		return mst;
	}

	public Graph<T> getMinimumSpanningTreePrims()
	{
		Graph<T> mst = new ConcurrentGraph<T>(graph.getAllNodes());
		List<Edge<T>> edgesToAdd = graph.getEdgesFor(mst.getRandomElement());
		PriorityQueue<Edge<T>> edgeQueue = new PriorityQueue<Edge<T>>(edgesToAdd);
		final int nodesInCompleteGraph = graph.size();

		while(mst.getNumberOfEdges() + 1 < nodesInCompleteGraph && !edgeQueue.isEmpty())
		{
			Edge<T> edge = edgeQueue.poll();
			final T source = edge.getDestination();
			final T destination = edge.getDestination();
			if(!mst.isConnected(source) || !mst.isConnected(destination))
			{
				mst.connect(source, destination, edge.getWeight());
				edgesToAdd = graph.getEdgesFor(destination);
				edgeQueue.addAll(edgesToAdd);
			}
		}

		return mst;
	}

	private Set<Edge<T>> getAllEdges()
	{
		final Map<T, List<Edge<T>>> edges = graph.getAllEdges();
		final Set<Edge<T>> allEdges = new HashSet<Edge<T>>(edges.size()*2);
		Iterator<Entry<T, List<Edge<T>>>> iter = edges.entrySet().iterator();
		while(iter.hasNext())
		{
			List<Edge<T>> nodeEdges = iter.next().getValue();
			allEdges.addAll(nodeEdges);
		}

		return allEdges;		
	}


	@Override
	public List<Edge<T>> getPathAllNodes()
	{
		setupDataStructures();
		List<Edge<T>> path = new ArrayList<Edge<T>>(visitedNodes.size());
		Graph<T> mst = getMinimumSpanningTree();
		T currentNode = mst.getNodeWithLeastEdges();
		while(visitedNodes.size() != mst.size())
		{
			visitedNodes.add(currentNode);
			Edge<T> nextEdge = getNextEdge(currentNode, visitedNodes, mst);
			path.add(nextEdge);
			currentNode = nextEdge.getDestination();
		}
		
		return path;
	}

	private Edge<T> getNextEdge(final T currentNode, Set<T> visitedNodes, Graph<T> mst)
	{
		List<Edge<T>> possibleEdges = mst.getEdgesFor(currentNode);
		if(possibleEdges.size() == 1)
			return possibleEdges.get(0);
		for(Edge<T> edge : possibleEdges)
			if(!visitedNodes.contains(edge.getDestination()))
				return edge;
		return null;
	}


	@Override
	public List<T> breadthFirstSearch(T start, T end)
	{
		final Queue<T> nodesToCheck = new ArrayDeque<T>(16);
		final Map<T, T> path = new HashMap<T, T>(32);

		nodesToCheck.add(start);
		path.put(start, null);
		while(!nodesToCheck.isEmpty())
		{
			T currentNode = nodesToCheck.poll();
			List<Edge<T>> possibleNodes = graph.getEdgesFor(currentNode);
			for(Edge<T> edge : possibleNodes)
			{
				final T node = edge.getDestination();
				if(!path.containsKey(node))
				{
					nodesToCheck.add(node);
					path.put(node, currentNode);
				}
			}
			if(currentNode.equals(end))
				return buildPath(path, start, end);
		}
		return null;
	}

	private List<T> buildPath(Map<T, T> visitedNodes, T start, T end)
	{
		final LinkedList<T> path = new LinkedList<T>();
		T node = end;
		while(node != start)
		{
			path.push(node);
			final T comingFrom = visitedNodes.get(node);
			node = comingFrom;
		}

		return path;
	}

	@Override
	public boolean isConnected()
	{
		Map<T, List<Edge<T>>> edges = graph.getAllEdges();
		for(T node : graph)
			if(!edges.containsKey(node) || edges.get(node).isEmpty())
				return false;
		
		Graph<T> mst = getMinimumSpanningTreePrims();
		final int mstNodes = mst.getNumberOfEdges() + 1;
		
		return mstNodes == graph.size();
	}
}
