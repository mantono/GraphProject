package graphProject;

public class PathRecord<T> implements Comparable<PathRecord<T>>
{
	private final T node;
	private final double weight;
	private final T reachedThrough;
	
	public PathRecord(T node, T reachedThrough, double newWeightForNode)
	{
		this.node = node;
		this.reachedThrough = reachedThrough;
		if(newWeightForNode < 0)
			throw new IllegalArgumentException("Weight must not be negative!");
		this.weight = newWeightForNode;
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
	
	double getWeight()
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
		return new Double(this.weight - other.weight).intValue();
	}
	
	@Override
	public String toString()
	{
		return reachedThrough + "--" + weight + "-->" + node;
	}
}
