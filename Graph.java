package alda.graphProject;

public interface Graph<T>
{
	boolean add(T data);
	boolean remove(T data);
	boolean contains(T data);
	boolean hasPath(T start, T end);
	void connect(T start, T end, int weight);
	boolean disconnect(T start, T end);
	int getWeight(T start, T end);
	Path getShortestPath(T start, T end);
	int size();
}
