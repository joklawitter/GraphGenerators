package unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import algorithms.GraphChecks;
import algorithms.GraphGeneration;

public class GraphChecksTest {

	@Test
	public void testIsGraphConnected() {
		assertFalse(GraphChecks.isConnected(GraphGeneration.createEdgelessGraph(9)));
		assertTrue(GraphChecks.isConnected(GraphGeneration.createCycle(10)));
		assertTrue(GraphChecks.isConnected(GraphGeneration.createPrueferTree(20, 2)));
	}

	@Test
	public void testIsBipartite() {
		assertTrue(GraphChecks.isBipartite(GraphGeneration.createEdgelessGraph(5)));
		assertTrue(GraphChecks.isBipartite(GraphGeneration.createCycle(10)));
		assertFalse(GraphChecks.isBipartite(GraphGeneration.createCycle(9)));
		assertTrue(GraphChecks.isBipartite(GraphGeneration.createHypercube(5)));
	}

	@Test
	public void testIsTree() {
		assertFalse(GraphChecks.isTree(GraphGeneration.createEdgelessGraph(9)));
		assertFalse(GraphChecks.isTree(GraphGeneration.createCycle(10)));
		assertTrue(GraphChecks.isTree(GraphGeneration.createPrueferTree(20, 2)));
		assertTrue(GraphChecks.isTree(GraphGeneration.createStar(20)));
	}

}
