package graphProject;

import java.util.List;

public interface GraphExplorer<T>
{
	List<T> depthFirstSearch(T start, T end);
	boolean hasPath(T start, T end);
	int getTotalWeight(T start, T end);
	List<Edge<T>> getShortestPath(T start, T end);
	List<Edge<T>> getPathAllNodes();
	Graph<T> getMinimumSpanningTree();
}
