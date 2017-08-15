package unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import algorithms.GraphGeneration;
import algorithms.GraphOperators;
import algorithms.RandomUtil;

public class GraphOperatorsTest {

	@Test
	public void testThinOutGraph() {
		int n = 20;
		assertEquals(0, GraphOperators
				.thinOutGraph(GraphGeneration.createStar(n), 0, RandomUtil.getRandom()).getM());
		assertEquals(n - 1, GraphOperators
				.thinOutGraph(GraphGeneration.createStar(n), 1, RandomUtil.getRandom()).getM());
	}

	@Test
	public void testCreateCartesianGraphProductGraph() {
		// method is actually tested a lot indirectly via graph generation
		// one may add more tests later
		fail("Not yet implemented");
	}

}
