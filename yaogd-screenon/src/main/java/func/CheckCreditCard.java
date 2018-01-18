package func;

/**
 * Checks for valid credit card number using Luhn algorithm
 */
public class CheckCreditCard {

	public static void main(String[] args) {
		// String cardNumber = "4408 0412 3456 7890";
		String cardNumber = "377155021456378";

		boolean valid = isValid(cardNumber);
		System.out.println(cardNumber + ": " + valid);
	}

	private static String getDigitsOnly(String s) {
		StringBuffer digitsOnly = new StringBuffer();
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isDigit(c)) {
				digitsOnly.append(c);
			}
		}
		return digitsOnly.toString();
	}

	public static boolean isValid(String cardNumber) {
		String digitsOnly = getDigitsOnly(cardNumber);
		int sum = 0;
		int digit = 0;
		int addend = 0;
		boolean timesTwo = false;
		for (int i = digitsOnly.length() - 1; i >= 0; i--) {
			digit = Integer.parseInt(digitsOnly.substring(i, i + 1));
			if (timesTwo) {
				addend = digit * 2;
				if (addend > 9) {
					addend -= 9;
				}
			} else {
				addend = digit;
			}
			sum += addend;

			timesTwo = !timesTwo;
		}
		int modulus = sum % 10;
		return modulus == 0;
	}

}
