package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import model.Graph;
import model.PlanarEdge;
import model.PlanarFace;
import model.PlanarGraph;
import model.PlanarVertex;

public class PlanarGraphGeneration {

	/**
	 * Creates an Apollonian network, i.e. planar graph constructed by
	 * recursively adding vertices in triangles.
	 * 
	 * @param numVertices
	 *            number of vertices
	 * @param seed
	 *            seed for randomness
	 * @return an Apollonian network
	 */
	public static PlanarGraph createApollonianNetwork(int numVertices, int seed) {
		if (numVertices < 3) {
			throw new IllegalArgumentException("n to small");
		}

		Random random = new Random(seed);
		ArrayList<PlanarVertex> vertices = new ArrayList<PlanarVertex>();
		ArrayList<PlanarEdge> edges = new ArrayList<PlanarEdge>();
		ArrayList<PlanarFace> faces = new ArrayList<PlanarFace>();

		// 1. base triangle
		// a) vertices
		PlanarVertex v0 = new PlanarVertex(0);
		PlanarVertex v1 = new PlanarVertex(1);
		PlanarVertex v2 = new PlanarVertex(2);
		// b) faces
		PlanarFace f0 = new PlanarFace(0);
		PlanarFace f1 = new PlanarFace(1);
		// c) edges
		PlanarEdge e0 = PlanarEdge.edgeFactory(v0, v1, f1, f0, 0);
		PlanarEdge e1 = PlanarEdge.edgeFactory(v1, v2, f1, f0, 1);
		PlanarEdge e2 = PlanarEdge.edgeFactory(v2, v0, f1, f0, 2);
		// d) add dependencies vertex -> edge
		v0.addEdgeAtEnd(e2.getReversedEdge());
		v0.addEdgeAtEnd(e0);
		v1.addEdgeAtEnd(e0.getReversedEdge());
		v1.addEdgeAtEnd(e1);
		v2.addEdgeAtEnd(e1.getReversedEdge());
		v2.addEdgeAtEnd(e2);
		// e) add dependencies face -> edge
		// for f0 outer face: reversed order
		f0.addEdgeAtEnd(e0.getReversedEdge());
		f0.addEdgeAtEnd(e2.getReversedEdge());
		f0.addEdgeAtEnd(e1.getReversedEdge());
		// for f1 inner face: normal order
		f1.addEdgeAtEnd(e0);
		f1.addEdgeAtEnd(e1);
		f1.addEdgeAtEnd(e2);
		// f) add to collections
		vertices.add(v0);
		vertices.add(v1);
		vertices.add(v2);
		edges.add(e0);
		edges.add(e1);
		edges.add(e2);
		faces.add(f0);
		faces.add(f1);

		// 2. add vertices inside randomly chosen face
		while (vertices.size() != numVertices) {
			// a) choose face at random
			f1 = faces.get(random.nextInt(faces.size()));
			e0 = f1.getEdges().get(0);
			v0 = e0.getStart();
			e1 = f1.getEdges().get(1);
			v1 = e1.getStart();
			e2 = f1.getEdges().get(2);
			v2 = e2.getStart();

			// b) add vertex
			PlanarVertex x = new PlanarVertex(vertices.size());
			vertices.add(x);

			// d) add faces
			PlanarFace f2 = new PlanarFace(faces.size());
			faces.add(f2);
			PlanarFace f3 = new PlanarFace(faces.size());
			faces.add(f3);

			// c) add edges
			PlanarEdge e3 = PlanarEdge.edgeFactory(v0, x, f3, f1, edges.size());
			edges.add(e3);
			PlanarEdge e4 = PlanarEdge.edgeFactory(v1, x, f1, f2, edges.size());
			edges.add(e4);
			PlanarEdge e5 = PlanarEdge.edgeFactory(v2, x, f2, f3, edges.size());
			edges.add(e5);

			// e) correct dependencies edge -> face
			e1.setLeftFace(f2);
			e2.setLeftFace(f3);

			// f) add dependencies vertex -> edge
			v0.addEdgeAfter(e3, e2.getReversedEdge());
			v1.addEdgeAfter(e4, e0.getReversedEdge());
			v2.addEdgeAfter(e5, e1.getReversedEdge());
			x.addEdgeAtEnd(e3.getReversedEdge()); // clockwise
			x.addEdgeAtEnd(e5.getReversedEdge());
			x.addEdgeAtEnd(e4.getReversedEdge());

			// g) add & correct dependencies face -> edge
			f1.removeEdge(e2);
			f1.removeEdge(e1);
			f1.addEdgeAtEnd(e4);
			f1.addEdgeAtEnd(e3.getReversedEdge());
			f2.addEdgeAtEnd(e1);
			f2.addEdgeAtEnd(e5);
			f2.addEdgeAtEnd(e4.getReversedEdge());
			f3.addEdgeAtEnd(e2);
			f3.addEdgeAtEnd(e3);
			f3.addEdgeAtEnd(e5.getReversedEdge());
		}

		// 3. create graph
		String name = "ApollionaNetwork(" + numVertices + "," + seed + ")";
		return new PlanarGraph(vertices, edges, faces, name);
	}

	/**
	 * Creates for given number of vertices and edge flips a planar graph. The
	 * resulting graph is based on an Apollonian network which underwent edge
	 * flips.
	 * 
	 * @param numVertices
	 *            number of vertices
	 * @param numFlips
	 *            number of flips
	 * @param seed
	 * @return a planar graph
	 */
	public static PlanarGraph createMaxPlanarGraph(int numVertices, long numFlips, int seed) {
		PlanarGraph g = PlanarGraphGeneration.createApollonianNetwork(numVertices, seed);
		PlanarGraphGeneration.flipEdges(g, numFlips, seed + 1);
		String name = "PlanarGraph(" + numVertices + "/flps" + numFlips + "/sd" + seed + ")";
		g.setName(name);

		return g;
	}

	/**
	 * Flips for given graph edges (specified by given parameter).
	 * 
	 * @param g
	 *            graph which undergoes edge flips
	 * @param numEdgesToFlip
	 *            number of edges to flip
	 * @param seed
	 */
	public static void flipEdges(PlanarGraph g, long numEdgesToFlip, int seed) {
		if (g.getN() < 5) {
			throw new IllegalArgumentException("n to small");
		}
		Random random = new Random(seed);
		ArrayList<PlanarEdge> edges = g.getEdges();

		// let edgeToFlip connect v0 and v2 in cycle v0-v1-v2-v3-v0
		// with f1 left of it and f2 right of it
		// cycle edges: e0-e1-e2-e3
		// we flip edge to v1-v3 and update all dependencies

		// ! edge can only be flipped if result is not already in graph

		for (int i = 0; i < numEdgesToFlip; i++) {
			// a) get random edge and neighborhood
			PlanarEdge edgeToFlip = edges.get(random.nextInt(edges.size()));
			PlanarVertex v0 = edgeToFlip.getStart();
			PlanarVertex v1 = edgeToFlip.getNextEdgeAtStart().getTarget();
			PlanarVertex v2 = edgeToFlip.getTarget();
			PlanarVertex v3 = edgeToFlip.getNextEdge().getTarget();
			// a2) if flip of edge already exists, retry
			if (v1.hasNeighbor(v3)) {
				i--;
				continue;
			}

			PlanarFace f1 = edgeToFlip.getLeftFace();
			PlanarFace f2 = edgeToFlip.getRightFace();
			PlanarEdge e0 = edgeToFlip.getNextEdgeAtStart();
			PlanarEdge e1 = e0.getNextEdge();
			PlanarEdge e2 = edgeToFlip.getNextEdge();
			PlanarEdge e3 = e2.getNextEdge();

			// b) flip edge
			edgeToFlip.setStart(v1);
			edgeToFlip.setTarget(v3);

			// c) correct dependencies vertex -> edge
			v0.removeEdge(edgeToFlip);
			v2.removeEdge(edgeToFlip.getReversedEdge());
			v1.addEdgeAfter(edgeToFlip, e0.getReversedEdge());
			v3.addEdgeAfter(edgeToFlip.getReversedEdge(), e2.getReversedEdge());

			// d) correct dependencies edge -> face
			// new face f1 = v0-v1-v3 and f2 = v1-v3-v2
			// -> stays the same for edgeToFlip
			e0.setLeftFace(f1);
			e2.setLeftFace(f2);

			// e) correct dependencies face -> edge
			f1.removeEdge(e2);
			f1.addEdgeAfter(e0, e3);
			f2.removeEdge(e0);
			f2.addEdgeAfter(e2, e1);
		}
	}

	/**
	 * Creates for given plane triangulation a 1-planar graph. The resulting
	 * graph is based on a planar graph, which in turn is based on an Apollonian
	 * network.
	 * 
	 * @param n
	 *            number of vertices
	 * @param flips
	 *            edge flips used for underlying planar graph
	 * @param seed
	 *            seed for randomness
	 * @return a 1-planar graph
	 */
	public static PlanarGraph create1PlanarGraph(PlanarGraph planarGraph, int seed) {
		final int m = planarGraph.getM();
		int newEdges = 0;
		Random random = new Random(seed + m);

		ArrayList<PlanarVertex> verticesToConsider = new ArrayList<PlanarVertex>(
				planarGraph.getN());
		verticesToConsider.addAll(planarGraph.getVertices());
		Collections.shuffle(verticesToConsider, random);

		// 0. pick random vertex x
		// check if neighbor y of neighbors u,v can be connected to x

		// u u
		// / | \ / | \
		// x | y -> x--|--y
		// \ | / \ | /
		// v v

		// if so: add edge with index >= 3n -6
		// -> can identify these edges in further search

		// how to check:
		// 1. take two consecutive neighbors u and v
		// 2. consider third vertex y on other side of u-v
		// 3.1 check if u has edge between u-x and u-v or u-v and u-y
		// 3.2 check if v has edge between v-x and v-u or v-u and v-y
		// if not
		// 4. check if x-y already exists
		// if not
		// 5. found new edge to add: x-y

		// 6. continue with x-v and x-w

		while (verticesToConsider.size() > 2) {
			PlanarVertex x = verticesToConsider.remove(verticesToConsider.size() - 1);

			ArrayList<PlanarEdge> neighbours = x.getEdges();
			for (int i = 0; i < neighbours.size(); i++) {
				// 1.
				int j = (i == 0) ? (neighbours.size() - 1) : (i - 1);
				PlanarEdge xu = neighbours.get(j);
				if (xu.getIndex() >= m) {
					continue;
				}
				PlanarEdge xv = neighbours.get(i);
				if (xv.getIndex() >= m) {
					i++; // v can also not function as u
					continue;
				}
				PlanarVertex u = xu.getTarget();
				PlanarVertex v = xv.getTarget();

				// 3.1 & 2.
				PlanarEdge uv = xu.getReversedEdge().getPreviousEdgeAtStart();
				if (uv.getIndex() >= m) {
					i++;
					continue;
				} else if (uv.getTarget() != v) {
					throw new IllegalStateException();
				}
				PlanarEdge uy = uv.getPreviousEdgeAtStart();
				if (uy.getIndex() >= m) {
					continue;
				}
				PlanarVertex y = uy.getTarget();

				// 3.2
				PlanarEdge vu = xv.getReversedEdge().getNextEdgeAtStart();
				if (vu.getIndex() >= m) {
					continue;
				} else if ((vu.getTarget() != u) || (vu.getReversedEdge() != uv)) {
					System.out.println("x " + x);
					System.out.println("xv " + xv);

					System.out.println("u " + u);
					System.out.println("v " + v);
					System.out.println("uv " + uv);
					System.out.println("vu " + vu);
					throw new IllegalStateException();
				}
				PlanarEdge vy = vu.getNextEdgeAtStart();
				if (vy.getIndex() >= m) {
					continue;
				} else if (vy.getTarget() != y) {
					throw new IllegalStateException();
				}

				// 4.
				if (x.hasNeighbor(y)) {
					i++; // y would again be fourth vertex
					continue;
				}

				// 5.
				// a) create edge(s)
				PlanarEdge xy = PlanarEdge.edgeFactory(x, y, xv.getLeftFace(), vy.getLeftFace(),
						m + newEdges);
				newEdges++;
				// b) add edges to vertices
				x.addEdgeAfter(xy, xu);
				y.addEdgeAfter(xy.getReversedEdge(), vy.getReversedEdge());
				// c) faces are broken - nothing to do
				// d) add edge to graph
				planarGraph.addEdge(xy);
			}
		}

		planarGraph.setName("1-" + planarGraph.getName());
		return planarGraph;
	}

	/**
	 * Creates for given number of vertices and edge flips a 1-planar graph. The
	 * resulting graph is based on a planar graph, which in turn is based on an
	 * Apollonian network.
	 * 
	 * @param numVertices
	 *            number of vertices
	 * @param numFlips
	 *            edge flips used for underlying planar graph
	 * @param seed
	 * @return a 1-planar graph
	 */
	public static PlanarGraph create1PlanarGraph(int numVertices, int numFlips, int seed) {
		PlanarGraph g = createMaxPlanarGraph(numVertices, numFlips, seed);
		return create1PlanarGraph(g, seed + 1);
	}

	/**
	 * Creates the dual graph of a given maximal planar graph.
	 * 
	 * @param primal
	 *            maximal planar graph for which dual is requested
	 * @return dual graph of given primal
	 */
	public static PlanarGraph createDualPlanarGraph(PlanarGraph primal) {
		if (!primal.isTriangulated()) {
			throw new IllegalArgumentException("Planar graph not triangulated.");
		}

		ArrayList<PlanarVertex> dualVertices = new ArrayList<PlanarVertex>(primal.getF());
		ArrayList<PlanarEdge> dualEdges = new ArrayList<PlanarEdge>(primal.getM());
		ArrayList<PlanarFace> dualFaces = new ArrayList<PlanarFace>(primal.getF());

		// create vertices and faces
		for (PlanarFace primalFace : primal.getFaces()) {
			dualVertices.add(new PlanarVertex(primalFace.getIndex()));
		}
		for (PlanarVertex primalVertex : primal.getVertices()) {
			dualFaces.add(new PlanarFace(primalVertex.getId()));
		}

		// create edges
		for (PlanarEdge primalEdge : primal.getEdges()) {
			PlanarVertex start = dualVertices.get(primalEdge.getLeftFace().getIndex());
			PlanarVertex target = dualVertices.get(primalEdge.getRightFace().getIndex());
			PlanarFace leftFace = dualFaces.get(primalEdge.getTarget().getId());
			PlanarFace rightFace = dualFaces.get(primalEdge.getStart().getId());
			dualEdges.add(PlanarEdge.edgeFactory(start, target, leftFace, rightFace,
					primalEdge.getIndex()));
		}

		// add dependencies vertex -> edges
		for (PlanarFace primalFace : primal.getFaces()) {
			PlanarVertex dualVertex = dualVertices.get(primalFace.getIndex());
			for (PlanarEdge edgeAroundPrimalFace : primalFace.getEdges()) {
				PlanarEdge dualEdge = dualEdges.get(edgeAroundPrimalFace.getIndex());
				if (dualEdge.getStart() != dualVertex) {
					dualEdge = dualEdge.getReversedEdge();
				}
				if (dualEdge.getStart() != dualVertex) {
					throw new IllegalStateException();
				}

				dualVertex.addEdgeAtEnd(dualEdge);
			}
			// because faces are ccw and vertices are cw
			Collections.reverse(dualVertex.getEdges());
		}

		// add dependencies face -> edges
		for (PlanarVertex primalVertex : primal.getVertices()) {
			PlanarFace dualFace = dualFaces.get(primalVertex.getId());
			for (PlanarEdge edgeAroundPrimalFace : primalVertex.getEdges()) {
				PlanarEdge dualEdge = dualEdges.get(edgeAroundPrimalFace.getIndex());
				if (dualEdge.getLeftFace() != dualFace) {
					dualEdge = dualEdge.getReversedEdge();
				}
				if (dualEdge.getLeftFace() != dualFace) {
					throw new IllegalStateException();
				}

				dualFace.addEdgeAtEnd(dualEdge);
			}
			// because faces are ccw and vertices are cw
			Collections.reverse(dualFace.getEdges());
		}

		return new PlanarGraph(dualVertices, dualEdges, dualFaces,
				"PlanarDual(" + dualVertices.size() + ")");
	}

	/**
	 * Converts a {@link PlanarGraph} to a normal {@link Graph}. Graph does not
	 * have to planar from a graph theoretical point of view. This model just
	 * converts to the other model.
	 * 
	 * @param planarGraph
	 *            graph to be converted
	 * @return graph based on given {@link PlanarGraph}
	 */
	public static Graph convertPlanarGraphToGraph(PlanarGraph planarGraph) {
		if (planarGraph == null) {
			throw new IllegalArgumentException(
					"Can't convert non-existing graph, but parameter was null.");
		}

		Graph convertedGraph = new Graph(planarGraph.getN(), planarGraph.getM(), Graph.UNDIRECTED);
		convertedGraph.setName(planarGraph.getName());
		for (PlanarEdge e : planarGraph.getEdges()) {
			convertedGraph.addEdge(e.getStart().getId(), e.getTarget().getId());
		}

		return convertedGraph;
	}

}
