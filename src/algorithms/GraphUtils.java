package algorithms;

import model.Vertex;

public class GraphUtils {

	/**
	 * Initializes a {@link Vertex} array with vertices.
	 * 
	 * @param vertices
	 *            array to be initialized
	 * @return vertex array with set indices
	 */
	public static Vertex[] initVerticesArray(Vertex[] vertices) {
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = new Vertex(i);
		}
		return vertices;
	}

	/**
	 * Returns an array of given length of {@link Vertex} objects initialized
	 * with increasing id.
	 * 
	 * @param n
	 *            length of array
	 * @return vertex array
	 */
	public static Vertex[] getInitialVertexArray(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Can't create array of negative length.");
		}
		Vertex[] vertices = new Vertex[n];
		return initVerticesArray(vertices);
	}

}
