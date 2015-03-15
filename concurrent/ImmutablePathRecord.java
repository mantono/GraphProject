package alda.graphProject.concurrent;

public class ImmutablePathRecord<T> implements Comparable<ImmutablePathRecord<T>>
{
	private final T node;
	private final int weight;
	private final T reachedThrough;
	
	ImmutablePathRecord(T node, T reachedThrough, int weight)
	{
		this.node = node;
		this.reachedThrough = reachedThrough;
		this.weight = weight;
	}
	
	ImmutablePathRecord(T node)
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
	public int compareTo(ImmutablePathRecord<T> other)
	{
		return this.weight - other.weight;
	}
}
