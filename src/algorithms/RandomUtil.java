package algorithms;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Contains utility methods for random number generation.
 * 
 * @author Matthias Wolf, Jonathan Klawitter
 *
 */
public final class RandomUtil {

	private RandomUtil() {
		throw new AssertionError("RandomUtil should not be instantiated");
	}

	/**
	 * Returns a random integer in the range {@code [least, bound)} that is not
	 * equal to {@code not}. Uses ThreadLocalRandom.current() as random number
	 * generator.
	 * 
	 * @param least
	 *            the least value returned
	 * @param bound
	 *            the upper bound (exclusive)
	 * @param not
	 *            the value that is not returned
	 * @return a random value in the given range that is not equal to
	 *         {@code not}.
	 */
	public static int randomIntUnequalTo(int least, int bound, int not) {
		return RandomUtil.randomIntUnequalTo(least, bound, not, ThreadLocalRandom.current());
	}

	/**
	 * Returns a random integer in the range {@code [least, bound)} that is not
	 * equal to {@code not}.
	 * 
	 * @param least
	 *            the least value returned
	 * @param bound
	 *            the upper bound (exclusive)
	 * @param not
	 *            the value that is not returned
	 * @param the
	 *            random number generator
	 * @return a random value in the given range that is not equal to
	 *         {@code not}.
	 */
	public static int randomIntUnequalTo(int least, int bound, int not, Random random) {
		if (least > not || bound <= not) {
			return least + random.nextInt(bound - least);
		} else {
			int value = least + random.nextInt(bound - least - 1);
			return value >= not ? value + 1 : value;
		}

	}

	/**
	 * Returns a permutation of [0..k-1] using the Fisher-Yates shuffle.
	 * 
	 * @param k
	 *            length of the permutation, [0..k-1]
	 * @param random
	 *            a random
	 * @return a permutation of [0..k-1]
	 */
	public static int[] randomPermutation(int k, Random random) {
		// Using Fisher-Yates algorithm
		int[] permutation = new int[k];
		for (int j = 0; j < permutation.length; j++) {
			permutation[j] = j;
		}

		for (int j = permutation.length - 1; j >= 0; j--) {
			int target = random.nextInt(j + 1);
			int tmp = permutation[j];
			permutation[j] = permutation[target];
			permutation[target] = tmp;
		}

		return permutation;
	}

	/**
	 * Returns a permutation of [0..k-1] using the Fisher-Yates shuffle.
	 * 
	 * @param k
	 *            length of the permutation, [0..k-1]
	 * @return a permutation of [0..k-1]
	 */
	public static int[] randomPermutation(int k) {
		return randomPermutation(k, ThreadLocalRandom.current());
	}

	/**
	 * Returns a new Random.
	 * 
	 * @return a new Random
	 */
	public static Random getRandom() {
		return new Random(System.currentTimeMillis());
	}

}
