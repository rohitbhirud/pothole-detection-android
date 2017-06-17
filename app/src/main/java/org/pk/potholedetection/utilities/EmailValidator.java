package org.pk.potholedetection.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to validate emailId. Uses RegEx pattern for validation.
 * 
 */
public class EmailValidator {

	/**
	 * This method is used to validate input emailId
	 * 
	 * @param inputEmail
	 *            the emailId to validate
	 * @return true if emailId is valid (in proper format), false otherwise
	 */
	public static boolean isEmailValid(String inputEmail) {
		String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
			+ "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		CharSequence inputStr = inputEmail;

		Object pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		Object matcher = ((Pattern) pattern).matcher(inputStr);

		if (((Matcher) matcher).matches())
			return true;
		else
			return false;
	}

}
