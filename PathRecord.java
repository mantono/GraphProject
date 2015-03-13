package alda.graphProject;

public class PathRecord<T>
{
	private int totalWeight = Integer.MAX_VALUE;
	private T reachedThrough;
	
	PathRecord(T node, int totalWeight)
	{
		this.reachedThrough = node;
		this.totalWeight = totalWeight;
	}
	
	boolean updateRecord(T node, int weight)
	{
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
		return reachedThrough;
	}
}
