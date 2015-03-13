package alda.graphProject;

public class Edge<T> implements Comparable<Edge<T>>
{
	private final T destination;
	private int weight;
	
	public Edge(T destination, int weight)
	{
		if(weight < 1)
			throw new IllegalArgumentException("Weight must not be negative or zero");
		this.destination = destination;
		this.weight = weight;
	}
	
	public T getDestination()
	{
		return destination;
	}
	
	public int getWeight()
	{
		return weight;
	}
	
	public void setWeight(int weight)
	{
		this.weight = weight;
	}
	
	@Override
	public boolean equals(Object object)
	{
		if(object == null)
			return false;
		if(!this.getClass().equals(object.getClass()))
			return false;
		Edge<T> other = (Edge<T>) object;
		if(this.destination.equals(other.destination) && this.weight == other.weight)
			return true;
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return destination.hashCode() + weight;
	}
	
	@Override
	public String toString()
	{
		return "--" + weight + "-->" + destination.toString();
	}

	@Override
	public int compareTo(Edge<T> other)
	{
		return this.weight - other.weight;
	}
	
}
