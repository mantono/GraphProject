package alda.graphProject;

public class PathRecord<T> implements Comparable<PathRecord<T>>
{
	private final T node;
	private int totalWeight = Integer.MAX_VALUE;
	private T reachedThrough;
	private final int connectingNodes;
	private int checkedNodes = 0;
	
	PathRecord(T node, int connectingNodes)
	{
		this.node = node;
		this.connectingNodes = connectingNodes;
	}
	
	PathRecord(T node, T reachedThrough, int weight, int connectingNodes)
	{
		this.node = node;
		this.reachedThrough = reachedThrough;
		this.totalWeight = weight;
		this.connectingNodes = connectingNodes;
		if(weight == 0)
			this.checkedNodes = connectingNodes;
	}
	
	synchronized boolean updateRecord(T node, int weight)
	{
		checkedNodes++;
		if(weight == 0)
			checkedNodes = connectingNodes;
		if(this.totalWeight > weight)
		{
			this.reachedThrough = node;
			this.totalWeight = weight;
			return true;
		}
		return false;
	}
	
	T getNode()
	{
		return node;
	}
	
	int getCurrentWeight()
	{
		return totalWeight;
	}
	
	T getNodeReachedThrough()
	{
		return reachedThrough;
	}

	@Override
	public int compareTo(PathRecord<T> other)
	{
		return this.totalWeight - other.totalWeight;
	}
}
