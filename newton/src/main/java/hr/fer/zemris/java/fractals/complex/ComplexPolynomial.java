package hr.fer.zemris.java.fractals.complex;

import java.util.Arrays;
import java.util.Collections;

/**
 * ComplexPolynomial represents a polynomial which factors are {@link Complex}.
 * Class instances are immutable.
 * 
 * @author Luka Skugor
 *
 */
public final class ComplexPolynomial {

	/**
	 * Polynomial's factors from those who stand next to variable with greater
	 * power to the one with power zero.
	 */
	private Complex[] factors;

	/**
	 * Creates a new polynomial with given factors. Factors are given from those
	 * which stand to variables with higher power. For example for polynomial
	 * 2*x^2 + 3*x + 1 given factors should be 2, 3, 1.
	 * 
	 * @param factors polynomial's factors
	 */
	public ComplexPolynomial(Complex... factors) {
		Collections.reverse(Arrays.asList(factors));
		factors = normalizeFactors(factors);
		if (factors.length == 0) {
			throw new IllegalArgumentException();
		}
		this.factors = factors;
	}

	/**
	 * Returns order of this polynomial; e.g. For (7+2i)z^3+2z^2+5z+1 returns 3
	 * @return order of the polynomial
	 */
	public short order() {
		return (short) (factors.length - 1);
	}

	/**
	 * Returns a new polynomial which is a result of this polynomial multiplied with given one.
	 * @param p polynomial multiplier
	 * @return resulting polynomial
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] factors = new Complex[this.order() + p.order() + 1];
		Arrays.fill(factors, Complex.ZERO);
		for (int i = 0; i < p.factors.length; i++) {
			for (int j = 0; j < this.factors.length; j++) {
				factors[i + j] = factors[i + j].add(p.factors[i]
						.multiply(this.factors[j]));
			}
		}
		Collections.reverse(Arrays.asList(factors));
		return new ComplexPolynomial(factors);
	}

	/**
	 * Computes first derivative of this polynomial; for example, for (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
	 * @return derivative of this polynomial
	 */
	public ComplexPolynomial derive() {
		Complex[] derivedFactors = new Complex[factors.length - 1];
		for (int i = 1; i < factors.length; i++) {
			derivedFactors[i - 1] = factors[i].multiply(new Complex(i, 0));
		}
		Collections.reverse(Arrays.asList(derivedFactors));
		return new ComplexPolynomial(derivedFactors);
	}

	/**
	 * Computes polynomial value at given point z.
	 * @param z complex point at which polynomial is calculated
	 * @return result for the applied point z
	 */
	public Complex apply(Complex z) {
		Complex result = new Complex(0, 0);
		for (int i = 0; i < factors.length; i++) {

			Complex powedFactor = new Complex(1, 0);
			for (int j = 0; j < i; j++) {
				powedFactor = powedFactor.multiply(z);
			}
			result = result.add(factors[i].multiply(powedFactor));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = factors.length - 1; i >= 0; i--) {
			if (factors[i].equals(Complex.ZERO)) {
				continue;
			}

			String rootString = factors[i].toString();
			if (rootString.trim().startsWith("-")) {
				rootString = factors[i].negate().toString();
				stringBuilder.append("-");
			} else if (stringBuilder.length() != 0) {
				stringBuilder.append("+");
			}

			if (rootString.split("[\\+\\-]").length >= 2) {
				rootString = "(" + rootString + ")";
			}
			stringBuilder.append(rootString);
			if (i != 0) {
				stringBuilder.append("*z^").append(i);
			}
		}
		return stringBuilder.toString();
	}

	/**
	 * Normalizes factors by removing first zeros, e.g. for factors 0, 0, 1, 0, 5 removes first 2 zeros.
	 * @param factors polynomial's factors
	 * @return normalized factors
	 */
	private static Complex[] normalizeFactors(Complex[] factors) {
		int order = factors.length;
		for (int i = factors.length - 1; i >= 0; i--) {
			if (!factors[i].equals(Complex.ZERO)) {
				break;
			}
			order--;
		}

		return Arrays.copyOf(factors, order);
	}
}
