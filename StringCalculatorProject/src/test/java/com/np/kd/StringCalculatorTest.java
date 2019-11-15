package com.np.kd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for String Calculator
 */
public class StringCalculatorTest {
	
	private StingCalculator calculator;
	
	@BeforeEach
	public void setUp()
	{
		calculator = new StingCalculator();
	}
	
	@Test
	public void testGetNumbers() {
		String[] input = {"1,0","10,20","\n2,3\n"};
		int[][] expected = {{1,0},{10,20},{2,3}};
		
		for(int i=0;i<input.length;i++) {
			Assertions.assertArrayEquals(expected[i], calculator.getIntNumbers(input[i],","));
		}
		
	}
	
	@Test
	public void testAdd() {
		String[] input = {"1,2,3","","\n5,5\n","1,2,5"};
		int[] expected= {6,0,10,8};
		for(int i=0;i<input.length;i++) {
			Assertions.assertEquals(expected[i], calculator.add(input[i]));
		}
 	}
	
	@Test
	public void testGetDemiliter() {
		String[] input = {"//;\n1;2;3","//,\n2,5,7","//$\n1$2$3","//@\n2@3@8"};
		String[] expected= {";",",","$","@"};
		for(int i=0;i<input.length;i++) {
			Assertions.assertEquals(expected[i], calculator.getPrefixDelimiter(input[i]));
		}
	}
	
	@Test
	public void testAddCustomDelim() {
		String[] input = {"//$\n1$2$3","//@\n2@3@8"};
		int[] expected= {6,13};
		for(int i=0;i<input.length;i++) {
			Assertions.assertEquals(expected[i], calculator.add(input[i]));
		}
	}
	
	@Test
	public void testNegativeNumException() {
		String [] input = {"-1,2,3","-10","2,3,-10,-20","//$\n1$-2$3"};
		
		for(int i=0;i<input.length;i++) {
			final int j=i;
			Assertions.assertThrows(NegativeNumException.class,()-> calculator.add(input[j]));
		}
	}
	
	@Test
	public void testLargerDataWithoutIgnore() {
		String[] input = {"5,10,15","1000,2000,3000,4000"};
		int[] expected = {30,10000};
		for(int i=0;i<input.length;i++) {
			Assertions.assertEquals(expected[i], calculator.add(input[i]));
		}
	}
	
	@Test
	public void testLargerDatasetWithIgnore() {
		String[] input = {"5,10,15","1000,2000,3000,4000","1,2000,30","2,1001"};
		calculator.setEnableIgnoreLimit(true, 1000);
		int[] expected = {30,1000,31,2};
		for(int i=0;i<input.length;i++) {
			Assertions.assertEquals(expected[i], calculator.add(input[i]));
		}
	}
	
	@Test
	public void testArbitaryLengthDelimiter() {
		String[] input = {"","20,30","//@\n1@2@3","//***\n1***2***3"};
		int[] expected = {0,50,6,6};
		for(int i=0;i<input.length;i++) {
			Assertions.assertEquals(expected[i], calculator.add(input[i]));
		}
	}
	
	@Test
	public void testMultipleDelimiter() {
		String[] input = {"10,20,30","//$\n10$20","//@,$\n10@20$30"};
		int[] expected = {60,30,60};
		for(int i=0;i<input.length;i++) {
			Assertions.assertEquals(expected[i], calculator.add(input[i]));
		}
	}
	
	@Test
    public void testMultipleDelimiterArbitraryLength() {
        String[] input = {"3,2,3","//@@,##\n2@@3##4", "//$$$,@@\n100$$$200@@300", "//##,$$$\n2##200$$$300"};
        int[] expected = {8, 9, 600, 502};
        for (int i = 0; i < input.length; i++) {
            Assertions.assertEquals(expected[i], calculator.add(input[i]));
        }
	}
}
