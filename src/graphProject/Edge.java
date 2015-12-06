package graphProject;

public class Edge<T> implements Comparable<Edge<T>>
{
	private final T destiantion;
	private int weight;

	public Edge(final T node, int weight)
	{
		if(weight < 1)
			throw new IllegalArgumentException("Weight must not be negative or zero");
		this.destiantion = node;
		this.weight = weight;
	}

	public T getSource()
	{
		return destiantion;
	}

	public T getDestination()
	{
		return destiantion;
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
		final boolean sameWeight = this.weight == other.weight;
		final boolean sameDestination = this.destiantion.equals(other.destiantion);
		return sameDestination && sameWeight;
	}

	@Override
	public int hashCode()
	{
		final int prime = 17;
		int code = prime*weight;
		code = code*prime + destiantion.hashCode();
		return code;
	}

	@Override
	public String toString()
	{
		return weight + "-->" + destiantion.toString();
	}

	@Override
	public int compareTo(Edge<T> other)
	{
		return this.weight - other.weight;
	}

}
