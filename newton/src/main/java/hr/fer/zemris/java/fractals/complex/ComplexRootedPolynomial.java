package hr.fer.zemris.java.fractals.complex;

/**
 * ComplexRootedPolynomial represents a polynomial which roots are
 * {@link Complex}. Class instances are immutable.
 * 
 * @author Luka Skugor
 *
 */
public final class ComplexRootedPolynomial {

	/**
	 * Roots of the polynomial.
	 */
	private Complex[] roots;

	/**
	 * Creates a new polynomial from given roots.
	 * @param roots roots from which polynomial is created
	 */
	public ComplexRootedPolynomial(Complex... roots) {
		if (roots.length == 0) {
			throw new IllegalArgumentException();
		}
		this.roots = roots;
	}

	/**
	 * Computes polynomial value at given point z.
	 * @param z complex point on which polynomial is applied.
	 * @return result of polynomial at point z
	 */
	public Complex apply(Complex z) {
		Complex result = new Complex(1, 0);
		for (Complex complex : roots) {
			result = result.multiply(z.sub(complex));
		}

		return result;
	}

	/**
	 * Converts this representation to ComplexPolynomial type.
	 * @return converted polynomial
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial sol = new ComplexPolynomial(Complex.ONE);
		for (Complex root : roots) {
			sol = sol
					.multiply(new ComplexPolynomial(Complex.ONE, root.negate()));
		}
		return sol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("f(z) = ");
		for (Complex complex : roots) {
			stringBuilder.append("(z");
			String rootAsString = complex.negate().toString();
			if (!rootAsString.startsWith("-")) {
				stringBuilder.append("+");
			}
			stringBuilder.append(rootAsString).append(")");
		}

		return stringBuilder.toString();
	}

	/**
	 * Finds index of closest root for given complex number z that is within treshold. If there is no such root, returns -1.
	 * @param z given point z for which closest root is calculated
	 * @param treshold treshold within which root is searched
	 * @return index + 1 of the root's index
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		double closestTreshold = treshold;
		int index = -1;
		for (int i = 0; i < roots.length; i++) {
			double currentTreshold = roots[i].sub(z).module();
			if (currentTreshold < closestTreshold) {
				closestTreshold = currentTreshold;
				index = i;
			}
		}

		return index + 1;
	}

}
