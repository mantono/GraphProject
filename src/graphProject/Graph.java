package graphProject;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mantono.RandomAccess;

public interface Graph<T> extends Collection<T>, RandomAccess<T>
{
	boolean add(T data);
	boolean remove(Object data);
	boolean contains(Object data);
	int edgeSize(T data);
	boolean isDirected();
	boolean isConnected(T node);
	boolean isConnected(T start, T end);
	boolean connect(T start, T end, double weight);
	boolean disconnect(T start, T end);
	double getWeight(T start, T end);
	Set<T> getAllNodes();
	List<Edge<T>> getEdgesFor(T node);
	Edge<T> getEdgeBetween(T node1, T node2);
	int size();
	int getNumberOfEdges();
	void clear();
	T getNodeWithLeastEdges();
	Map<T, List<Edge<T>>> getAllEdges();
}
