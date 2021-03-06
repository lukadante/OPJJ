package hr.fer.zemris.java.fractals.complex;

import static org.junit.Assert.*;
import hr.fer.zemris.java.fractals.complex.Complex;

import org.junit.Test;

/**
 * Unit tests for the Complex class.
 * 
 * @author luka
 *
 */
public class ComplexTest {

	private static final double DELTA = 1E-9;

	@Test
	public void addTest() {
		Complex cn1 = new Complex(2, 3);
		Complex cn2 = new Complex(-4, 6);
		Complex result = cn1.add(cn2);
		assertEquals(result.getImaginary(), 9, DELTA);
		assertEquals(result.getReal(), -2, DELTA);
	}

	@Test
	public void subTest() {
		Complex cn1 = new Complex(2, 3);
		Complex cn2 = new Complex(-4, 6);
		Complex result = cn1.sub(cn2);
		assertEquals(result.getImaginary(), -3, DELTA);
		assertEquals(result.getReal(), 6, DELTA);
	}

	@Test
	public void multiplyTest() {
		Complex cn1 = new Complex(2.3, 3.7);
		Complex cn2 = new Complex(-4, 7);
		Complex result = cn1.multiply(cn2);
		assertEquals(result.getImaginary(), 1.3, DELTA);
		assertEquals(result.getReal(), -35.1, DELTA);
	}

	@Test
	public void divideTest() {
		Complex cn1 = new Complex(2.3, 3.7);
		Complex cn2 = new Complex(-4, 7);
		Complex result = cn1.divide(cn2);
		assertEquals(result.getImaginary(), -0.47538461538, DELTA);
		assertEquals(result.getReal(), 0.2569230769, DELTA);
	}

	@Test(expected=ArithmeticException.class)
	public void divideWithZero() {
		Complex cn1 = new Complex(2, 3);
		Complex cn2 = new Complex(0, 0);
		cn1.divide(cn2);
	}

	@Test
	public void getAsStringFullComplex() {
		Complex cn1 = new Complex(-2.3, 3.7);
		assertEquals(cn1.toString(), "-2.3 + i3.7");
	}
	
	@Test
	public void getAsStringMissingImaginaryPart() {
		Complex cn1 = new Complex(-2.3, 0);
		assertEquals(cn1.toString(), "-2.3");
	}
	
	@Test
	public void getAsStringMissingRealPart() {
		Complex cn1 = new Complex(0, 3.7);
		assertEquals(cn1.toString(), "i3.7");
	}
	
	@Test
	public void getAsStringZero() {
		Complex cn1 = new Complex(0, 0);
		assertEquals(cn1.toString(), "0");
	}
	
	@Test
	public void parseComplexTestIntInt() {
		Complex cn1 = Complex.parseComplex("3 + i2");
		assertTrue(cn1.getImaginary() == 2 && cn1.getReal() == 3);
	}
	
	@Test
	public void parseComplexTestIntDouble() {
		Complex cn1 = Complex.parseComplex("3 + i2.22");
		assertTrue(cn1.getImaginary() == 2.22 && cn1.getReal() == 3);
	}
	
	@Test
	public void parseComplexTestDoubleInt() {
		Complex cn1 = Complex.parseComplex("33.33 + i2");
		assertTrue(cn1.getImaginary() == 2 && cn1.getReal() == 33.33);
	}
	
	@Test
	public void parseComplexTestDoubleDouble() {
		Complex cn1 = Complex.parseComplex("3.76 + i2.14");
		assertTrue(cn1.getImaginary() == 2.14 && cn1.getReal() == 3.76);
	}
	
	@Test
	public void parseComplexTestNoneDouble() {
		Complex cn1 = Complex.parseComplex(" +i2.56");
		assertTrue(cn1.getImaginary() == 2.56 && cn1.getReal() == 0);
	}
	
	@Test
	public void parseComplexTestDoubleNone() {
		Complex cn1 = Complex.parseComplex("-3.66 ");
		assertTrue(cn1.getImaginary() == 0 && cn1.getReal() == -3.66);
	}
	
	@Test
	public void parseComplexTestDoubleNoneImaginaryPartWihoutCoefficient() {
		Complex cn1 = Complex.parseComplex("-3.66 + i");
		assertEquals(cn1.getImaginary(), 1, DELTA); 
		assertEquals(cn1.getReal(), -3.66, DELTA);
	}
	
	@Test
	public void parseComplexTestPlusI() {
		Complex cn1 = Complex.parseComplex("+ i");
		assertTrue(cn1.getImaginary() == 1 && cn1.getReal() == 0);
	}
	@Test
	public void parseComplexTestMinusI() {
		Complex cn1 = Complex.parseComplex("- i");
		assertTrue(cn1.getImaginary() == -1 && cn1.getReal() == 0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void parseComplexTestNoneNone() {
		Complex.parseComplex("   ");
	}
	
	@Test(expected=NumberFormatException.class)
	public void parseComplexTestJustAnOperator() {
		Complex.parseComplex(" -  ");
	}
	
	@Test
	public void equalsTestTrue() {
		assertTrue(new Complex(0, 0).equals(Complex.ZERO));
	}
	
	@Test
	public void equalsTestDifferentObjects() {
		assertFalse(new Complex(0, 0).equals("asdf"));
	}
	
	@Test
	public void equalsTestDifferentRealPart() {
		assertFalse(new Complex(0, 0).equals(Complex.ONE));
	}
	
	@Test
	public void equalsTestDifferentImaginaryPart() {
		assertFalse(new Complex(0, 0).equals(Complex.IM));
	}
	
	@Test
	public void equalsTestDifferentImaginaryandRealPart() {
		assertFalse(new Complex(0, 0).equals(Complex.IM.add(Complex.ONE)));
	}

}
