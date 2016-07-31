package org.bukkitai.advancedsweardetection.utils;

/**
 * Simple way to pair two objects
 * 
 * @author Arsen
 *
 * @param <T1>
 *            Type of element #1
 * @param <T2>
 *            Type of element #2
 */
public class Pair<T1, T2> {
	private T1 element1;
	private T2 element2;

	/**
	 * Gets element #1
	 * 
	 * @return Element #1
	 */
	public T1 getElementOne() {
		return element1;
	}

	/**
	 * Changes element #1
	 * 
	 * @param element1
	 *            New element #1
	 */
	public void setElementOne(T1 element1) {
		this.element1 = element1;
	}

	/**
	 * Gets element #2
	 * 
	 * @return Element #2
	 */
	public T2 getElementTwo() {
		return element2;
	}

	/**
	 * Changes element #2
	 * 
	 * @param element2
	 *            New element #2
	 */
	public void setElementTwo(T2 element2) {
		this.element2 = element2;
	}

	/**
	 * Initiates a new Pair
	 * 
	 * @param element1
	 *            Element #1
	 * @param element2
	 *            Element #2
	 */
	public Pair(T1 element1, T2 element2) {
		this.element1 = element1;
		this.element2 = element2;
	}

	@Override
	public boolean equals(Object anotherObject) {
		if (!(anotherObject instanceof Pair))
			return false;
		@SuppressWarnings("rawtypes")
		Pair anotherPair = (Pair) anotherObject;
		return anotherPair.element1.equals(element1) && anotherPair.element2.equals(element2);
	}
}
