package graphProject;

import java.io.Serializable;

public class Edge<T> implements Comparable<Edge<T>>, Serializable
{
	private final static long serialVersionUID = 0L;
	private final T source, destiantion;
	private double weight;

	public Edge(final T source, final T destination, double weight)
	{
		if(weight < 0)
			throw new IllegalArgumentException("Weight must not be negative");
		this.source = source;
		this.destiantion = destination;
		this.weight = weight;
	}
	
	public T getSource()
	{
		return source;
	}

	public T getDestination()
	{
		return destiantion;
	}

	public double getWeight()
	{
		return weight;
	}

	/**
	 * Changes the weight of this edge.
	 * @param newWeight the new weight for the Edge.
	 * @return true if the weight was changed, else false.
	 */
	public boolean setWeight(double newWeight)
	{
		final double previousWeight = this.weight;
		this.weight = newWeight;
		return previousWeight != newWeight;
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
		final boolean sameSource = this.source.equals(other.source);
		final boolean sameDestination = this.destiantion.equals(other.destiantion);
		return sameSource && sameDestination && sameWeight;
	}

	@Override
	public int hashCode()
	{
		final int prime = 17;
		double code = prime*weight;
		code = code*prime + source.hashCode();
		code = code*prime + destiantion.hashCode();
		return new Double(code).intValue();
	}

	@Override
	public String toString()
	{
		return source + "--" + weight + "-->" + destiantion.toString();
	}

	@Override
	public int compareTo(Edge<T> other)
	{
		return Double.compare(this.weight, other.weight);
	}

}
