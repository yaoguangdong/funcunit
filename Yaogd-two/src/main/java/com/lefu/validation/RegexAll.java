package com.lefu.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;


public class RegexAll {
	
	/**0.00-999999999.99（9亿），支持整数、1位小数、2位小数。*/
	static final String AMOUNT_REGEX = "^(([1-9]{1}\\d{0,8})|0)(\\.(\\d){1,2})?$";
	/**中国的手机号*/
	static final String PHONE_REGEX = "^(\\+?\\d{2}-?)?(1[0-9])\\d{9}$";
	/**数字*/
	static final boolean isDigits = TextUtils.isDigitsOnly("333"); 
	/**Email*/
	static final String EMAIL_REGEX =
            "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
                    "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+" +
                    "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
	/**主机名*/
	static final String HOST_REGEX = "^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,65}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$";
	/**URL*/
	static final String URL_REGEX =
            "^(https?:\\/\\/)?[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?$";
	/**IPV4*/
	static final String IPV4_REGEX = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
	
	static final boolean notBlank = ! TextUtils.isEmpty(" s") && ! match("^\\s*$", " s") ;
	/**信用卡号*/
	public boolean CreditCardTest(CharSequence inputValue) {
		// accept only spaces, digits and dashes
		if (!match("[\\d -]*", inputValue)) {
			return false;
		}

		String value = String.valueOf(inputValue).replaceAll("\\D", "");

		// Basing min and max length on
		// http://developer.ean.com/general_info/Valid_Credit_Card_Types
		int length = value.length();
		if (length < 13 || length > 19) {
			return false;
		}

		char cDigit;
		int nCheck = 0, nDigit;
		boolean bEven = false;
		for (int n = length - 1; n >= 0; n--) {
			cDigit = value.charAt(n);
			nDigit = Integer.parseInt(String.valueOf(cDigit), 10);
			if (bEven) {
				if ((nDigit *= 2) > 9) {
					nDigit -= 9;
				}
			}
			nCheck += nDigit;
			bEven = !bEven;
		}

		return (nCheck % 10) == 0;
	}
	
	private static boolean match(String regex, CharSequence value) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	 
}
