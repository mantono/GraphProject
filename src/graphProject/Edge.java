package graphProject;

import java.io.Serializable;

public class Edge<T> implements Comparable<Edge<T>>, Serializable
{
	private final static long serialVersionUID = 0L;
	private final T destiantion;
	private double weight;

	public Edge(final T node, double weight)
	{
		if(weight < 0)
			throw new IllegalArgumentException("Weight must not be negative");
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

	public double getWeight()
	{
		return weight;
	}

	public void setWeight(double weight)
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
		double code = prime*weight;
		code = code*prime + destiantion.hashCode();
		return new Double(code).intValue();
	}

	@Override
	public String toString()
	{
		return weight + "-->" + destiantion.toString();
	}

	@Override
	public int compareTo(Edge<T> other)
	{
		return new Double(this.weight - other.weight).intValue();
	}

}
