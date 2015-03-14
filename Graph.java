package alda.graphProject;

import java.util.List;
import java.util.Set;

public interface Graph<T>
{
	boolean add(T data);
	boolean remove(T data);
	boolean contains(T data);
	int edgeSize(T data);
	boolean isConnected(T start, T end);
	boolean connect(T start, T end, int weight);
	boolean disconnect(T start, T end);
	int getWeight(T start, T end);
	Set<T> getAllNodes();
	List<Edge<T>> getEdgesFor(T node);
	Edge<T> getEdgeBetween(T node1, T node2);
	int size();
	int getNumberOfEdges();
	void clear();
}
