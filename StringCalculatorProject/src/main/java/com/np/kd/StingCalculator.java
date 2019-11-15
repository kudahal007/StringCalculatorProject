package com.np.kd;

import java.util.Arrays;

/**
 * String calculator Application
 *
 */
public class StingCalculator {
	private static final String COMMA = ",";
	private static final String NEW_LINE = "\n";
	private boolean enableIgnoreLimit = false;
	private int maxLimit = 1000;

	public void setEnableIgnoreLimit(boolean enableIgnoreLimit, int max) {
		this.enableIgnoreLimit = enableIgnoreLimit;
		this.maxLimit = max;
	}

	public static void main(String[] args) {
		StingCalculator calc = new StingCalculator();
		System.out.println(calc.add("///***\n1***2***3"));
		System.out.println(Arrays.toString(calc.getIntNumbers("1$2$3", "$")));
	}

	private boolean checkEmpty(String inputData) {
		return inputData == null || inputData.length() == 0;
	}

	private String replaceCustomDelimiter(String inputData, String customDelimiters, String replaceBy) {
		for (String d : customDelimiters.split(replaceBy)) {
			inputData = inputData.replace(d, replaceBy);
		}
		return inputData;
	}

	private int[] getNegativeNumbers(int[] numbers) {
		return Arrays.stream(numbers).filter(x -> x < 0).toArray();
	}
	
	private String trimCustomDelimiterPrefix(String inputData, String delimiter) {
		return !delimiter.isEmpty() ? inputData.substring(inputData.indexOf(NEW_LINE) + 1) : inputData;
	}
	
	private void assertNegagiveNumbers(int[] numbers) {
		int[] negativeNumbers = getNegativeNumbers(numbers);
		if (negativeNumbers != null && negativeNumbers.length > 0) {
			throw new NegativeNumException("Negative not allowed: " + Arrays.toString(negativeNumbers));
		}
	}
	
	private boolean predicateMaxLimit(int x) {
		return !enableIgnoreLimit || x <= maxLimit;
	}

	/**
	 * Takes input as string separated by comma
	 * 
	 * @param inputData
	 * @return
	 */
	public int add(String inputData) {
		if (!checkEmpty(inputData)) {
			String customDelimiter = getPrefixDelimiter(inputData);
			inputData = trimCustomDelimiterPrefix(inputData, customDelimiter);
			customDelimiter = customDelimiter.isEmpty() ? COMMA : customDelimiter;
			inputData = replaceCustomDelimiter(inputData, customDelimiter, COMMA);
			int[] numbers = getIntNumbers(inputData, customDelimiter);
			assertNegagiveNumbers(numbers);
			return Arrays.stream(numbers)
					.filter(x -> predicateMaxLimit(x))
					.reduce(Integer::sum)
					.orElse(0);
		}
		return 0;  
	}

	/**
	 * Converts string input data and returns integer array of input data
	 * 
	 * @param inputData
	 * @return
	 */
	int[] getIntNumbers(String inputData, String delimiter) {
		String[] inputArray = inputData.replace(NEW_LINE, "").replace(delimiter, COMMA)
				.split(COMMA);
		return Arrays.stream(inputArray).mapToInt(Integer::valueOf).toArray();
	}

	/**
	 * Method to get custom delimiter from input string
	 * 
	 * @param input
	 * @return
	 */
	String getPrefixDelimiter(String input) {
		if (input.startsWith("//") && input.indexOf(NEW_LINE) > 1)
			return input.substring("//".length(), input.indexOf(NEW_LINE));
		return "";
	}

}

class InvalidNumberException extends RuntimeException {
	public InvalidNumberException(String errMsg) {
		super(errMsg);
	}
}

class NegativeNumException extends InvalidNumberException{
	private static final long serialVersionUID = 1L;

	public NegativeNumException(String errMsg) {
    	super(errMsg);
    }
}

