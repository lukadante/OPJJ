package hr.fer.zemris.java.fractals.complex;

import static org.junit.Assert.*;
import hr.fer.zemris.java.fractals.complex.Complex;

import org.junit.Test;

public class ComplexRootedPolynomialTest {

	private final static ComplexRootedPolynomial cpr = new ComplexRootedPolynomial(
			Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG);
	private static final double THETA = 1e-9;
	

	@Test
	public void toComplexPolynomTest() {
		ComplexPolynomial expected = new ComplexPolynomial(
				Complex.ONE, Complex.ZERO, Complex.ZERO, Complex.ZERO,
				Complex.ONE_NEG);
		assertEquals(expected.toString(), cpr.toComplexPolynom().toString());
	}

	@Test
	public void indexOfClosestRootTest() {
		int index = cpr.indexOfClosestRootFor(new Complex(-0.9995, 0), 1e-3);
		assertEquals(2, index);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void createPolynomWithNoRoots() {
		new ComplexRootedPolynomial();
	}
	
	@Test
	public void toStringTest() {
		assertEquals("f(z) = (z-1)(z+1)(z-i1)(z+i1)", cpr.toString());
	}
	
	@Test
	public void applyTest() {
		Complex result = cpr.apply(new Complex(1, 1));
		assertEquals(0, result.sub(new Complex(-5, 0)).module(), THETA);
	}
}
