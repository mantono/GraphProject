package alda.graphProject;

import java.util.List;
import java.util.Set;

public interface Graph<T>
{
	boolean add(T data);
	boolean remove(T data);
	boolean contains(T data);
	int edgeSize(T data);
	boolean hasPath(T start, T end);
	List<T> depthFirstSearch(T start, T end);
	boolean isConnected(T start, T end);
	boolean connect(T start, T end, int weight);
	boolean disconnect(T start, T end);
	int getWeight(T start, T end);
	List<Edge<T>> getShortestPath(T start, T end);
	List<Edge<T>> getEdgesFor(T node);
	int size();
	int getNumberOfEdges();
	void clear();
}
