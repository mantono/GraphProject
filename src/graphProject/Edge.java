package graphProject;

public class Edge<T> implements Comparable<Edge<T>>
{
	private final T node1;
	private final T node2;
	private int weight;

	public Edge(final T node1, final T node2, int weight)
	{
		if(weight < 1)
			throw new IllegalArgumentException("Weight must not be negative or zero");
		this.node1 = node1;
		this.node2 = node2;
		this.weight = weight;
	}

	public T getSource()
	{
		return node1;
	}

	public T getDestination()
	{
		return node2;
	}

	public T getDestination(final T source)
	{
		return (node1.equals(source)) ? node2 : node1;
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
		final boolean sameNode1Node1 = this.node1.equals(other.node1);
		final boolean sameNode2Node2 = this.node2.equals(other.node2);
		final boolean sameNode1Node2 = this.node1.equals(other.node2);
		final boolean sameNode2Node1 = this.node2.equals(other.node1);
		return (sameNode1Node1 && sameNode2Node2 || sameNode1Node2 && sameNode2Node1) && sameWeight;
	}

	@Override
	public int hashCode()
	{
		final int prime = 17;
		int code = prime*weight;
		code = code*prime + node1.hashCode();
		code = code*prime + node2.hashCode();
		return code;
	}

	@Override
	public String toString()
	{
		return node1.toString() + "<--" + weight + "-->" + node2.toString();
	}

	@Override
	public int compareTo(Edge<T> other)
	{
		return this.weight - other.weight;
	}

}
