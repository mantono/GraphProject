package alda.graphProject;

public class PathRecord<T> implements Comparable<PathRecord<T>>
{
	private final T node;
	private final int weight;
	private final T reachedThrough;
	
	PathRecord(T node, T reachedThrough, int weight)
	{
		this.node = node;
		this.reachedThrough = reachedThrough;
		this.weight = weight;
	}
	
	PathRecord(T node)
	{
		this.reachedThrough = this.node = node;
		this.weight = Integer.MAX_VALUE;
	}
	
	T getNode()
	{
		return node;
	}
	
	int getWeight()
	{
		return weight;
	}
	
	T getNodeReachedThrough()
	{
		return reachedThrough;
	}

	@Override
	public int compareTo(PathRecord<T> other)
	{
		return this.weight - other.weight;
	}
}
