package graphProject;

public class PathRecord<T> implements Comparable<PathRecord<T>>
{
	private final T node;
	private final int weight;
	private final T reachedThrough;
	
	public PathRecord(T node, T reachedThrough, int weight)
	{
		this.node = node;
		this.reachedThrough = reachedThrough;
		if(weight < 0)
			throw new IllegalArgumentException("Weight must not be negative!");
		this.weight = weight;
	}
	
	public PathRecord(T node)
	{
		this.reachedThrough = this.node = node;
		this.weight = Integer.MAX_VALUE;
	}
	
	public T getNode()
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
	
	@Override
	public String toString()
	{
		return reachedThrough + "--" + weight + "-->" + node;
	}
}
