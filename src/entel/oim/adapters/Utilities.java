package entel.oim.adapters;

import java.text.Normalizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Operations.tcLookupOperationsIntf;
import oracle.iam.platform.Platform;

/**
 * Contains generics adapters share by all connectors
 * 
 * @author Oracle
 *
 */
public class Utilities {

	private final static String className = Utilities.class.getName();
	private static Logger logger = Logger.getLogger(className);
	private static tcLookupOperationsIntf lookupOps = Platform.getService(tcLookupOperationsIntf.class);

	public Utilities() {
		super();
	}

	/**
	 * Return the upper case of a string
	 * 
	 * @param words
	 * @return Upper Case of the words
	 */
	public String toUpperCase(String str) {

		logger.entering(className, "toUpperCase", str);
		String formatText = str != null ? str.toUpperCase() : "";
		formatText = formatText.trim();

		logger.exiting(className, "toUpperCase", formatText);
		return formatText;

	}

	/**
	 * Return the lower case of a string
	 * 
	 * @param words
	 * @return Lower Case of the words
	 */
	public String toLowerCase(String str) {

		logger.entering(className, "toLowerCase", str);
		String formatText = str != null ? str.toLowerCase() : "";
		formatText = formatText.trim();

		logger.exiting(className, "toLowerCase", formatText);
		return formatText;

	}

	/**
	 * Capitalize a String
	 * 
	 * @param str
	 *            String to capitalize
	 * @return Capitalized string
	 */
	public String toCapitalizeString(String str) {
		logger.entering(className, "toCapitalizeString", str);

		logger.finer("Checking null value");
		if (str == null)
			str = "";

		logger.finer("Split the String into words");
		String[] words = str.split("\\s");
		StringBuilder formatText = new StringBuilder();

		logger.finer("Loop over all words of the String");
		for (String word : words) {
			if (word != null && word.length() > 0) {
				formatText.append(
						word.substring(0, 1).toUpperCase().concat(word.substring(1, word.length()).toLowerCase()));
				formatText.append(" ");
				logger.finest("->" + formatText.toString() + "<-");
			}
		}

		logger.finer("Trim spaces to the word");
		String finalText = formatText.toString();
		finalText = finalText.trim();

		logger.finer("Remove Special Characters to the word");
		finalText = finalText.replaceAll("[^a-zA-ZáäàÁÄÀéëèÉËÈíïìÍÏÌóöòÓÖÒúüùÚÜÙñÑ ]","");
		
		logger.exiting(className, "toCapitalizeString", finalText);
		return finalText;
	}

	/**
	 * Replace special characters from string
	 * 
	 * @param s
	 *            String to transform
	 * @return String formated
	 */
	public String toStripAccents(String str) {

		logger.entering(className, "toStripAccents", str);

		// Variable to return
		String newVal = str;

		logger.finer("Checking null value");
		if (newVal == null)
			newVal = "";

		logger.finer("Normalizer for: " + newVal);
		// Normalize the string
		newVal = Normalizer.normalize(newVal, Normalizer.Form.NFD);
		newVal = newVal.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		newVal = newVal.trim();
		logger.finer("Normalized string: " + newVal);

		// Return the format string
		logger.exiting(className, "toStripAccents", newVal);
		return newVal;
	}

	/**
	 * Return all names of an user in a single string
	 * 
	 * @param firstName
	 * @param middleName
	 * @return First and middlename string
	 */
	public String toConcatNames(String firstName, String middleName) {

		logger.entering(className, "toConcatNames", firstName + " " + middleName);
		// variable to return
		String newVal = "";

		if (firstName == null) {
			firstName = "";
		} else {
			firstName = toCapitalizeStripAccentString(firstName);
		}
		if (middleName == null) {
			middleName = "";
		} else {
			middleName = toCapitalizeStripAccentString(middleName);
		}

		if ("".equals(middleName)) {
			newVal = firstName;
		} else {
			newVal = firstName + " " + middleName;
		}

		logger.finer("Trim spaces to the word");
		newVal = newVal.trim();

		logger.exiting(className, "toConcatNames", newVal);
		return newVal;

	}

	/**
	 * Format Sentence string and strip accents
	 * 
	 * @param str
	 * @return
	 */
	public String toCapitalizeStripAccentString(String str) {

		logger.entering(className, "toCapitalizeStripAccentString", str);

		logger.finer("Checking null value");
		if (str == null)
			str = "";

		logger.finer("Split the String into words");
		String[] words = str.split("\\s");
		StringBuilder formatText = new StringBuilder();

		logger.finer("Loop over all words of the String");
		for (String word : words) {
			if (word != null && word.length() > 0) {
				formatText.append(
						word.substring(0, 1).toUpperCase().concat(word.substring(1, word.length()).toLowerCase()));
				formatText.append(" ");
			}
		}

		String value = formatText.toString();
		logger.finer("Formated text: " + value);

		value = toStripAccents(value);
		logger.finer("Striped Accent text: " + value);

		logger.finer("Trim spaces to the word");
		value = value.trim();

		logger.finer("Remove Special Characters to the word");
		value = value.replaceAll("[^a-zA-ZáäàÁÄÀéëèÉËÈíïìÍÏÌóöòÓÖÒúüùÚÜÙñÑ ]","");
		

		logger.exiting(className, "toCapitalizeStripAccentString", value);
		return value;
	}

	/**
	 * Format RUT
	 * 
	 * @param rut
	 *            Rut
	 * @param sepRut
	 *            Include separator for characters of the RUT
	 * @param sepDigVer
	 *            Include separator for verify digit
	 * @return The rut in format XXXXXXX-D or XXXXXXXD
	 */
	public String toFormatRut(String rut, int indSepRut, int indSepDigVer) {

		logger.entering(className, "toFormatRut ", rut);
		String value = rut;

		logger.finer("Remove all characters (.) (-)");
		value = value.replace(".", "").replace("-", "");
		logger.finer("Formated text: " + value);

		logger.finer("Remove all leading zeros");
		value = value.replaceFirst("^0*", "");
		logger.finer("Formated text: " + value);

		logger.finest("indSepRut: " + indSepRut);
		if (indSepRut == 1) {
			logger.finer("Add the . separator");
			StringBuilder str = new StringBuilder(value);
			int idx = (str.length() - 1) - 3;

			while (idx > 0) {
				str.insert(idx, ".");
				idx = idx - 3;
			}

			value = str.toString();
			logger.finer("Formated text: " + value);
		}

		logger.finest("indSepDigVer: " + indSepDigVer);
		if (indSepDigVer == 1) {
			logger.finer("Add the - to the verify digit");
			value = value.substring(0, value.length() - 1) + "-" + value.charAt(value.length() - 1);
			logger.finer("Formated text: " + value);
		}

		logger.exiting(className, "toFormatRut", value);
		return value;
	}

	/**
	 * Full name of an user. Format: Names + Last names
	 * 
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param mmName
	 * @return fullName
	 */
	public String fullName(String firstName, String middleName, String lastName, String mmName) {

		logger.entering(className, "fullName", new Object[] { firstName, middleName, lastName, mmName });

		String names = toConcatNames(firstName, middleName);
		String lastNames = toConcatNames(lastName, mmName);
		String fullName = toConcatNames(names, lastNames);

		logger.exiting(className, "fullName", fullName);
		return fullName;

	}

	/**
	 * Full name of an user. Format: Last names + Names
	 * 
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param mmName
	 * @return fullName
	 */
	public String fullNameInverted(String firstName, String middleName, String lastName, String mmName) {

		logger.entering(className, "fullNameInverted", new Object[] { firstName, middleName, lastName, mmName });

		String names = toConcatNames(firstName, middleName);
		String lastNames = toConcatNames(lastName, mmName);
		String fullName = toConcatNames(lastNames, names);

		logger.exiting(className, "fullNameInverted", fullName);
		return fullName;

	}

	/**
	 * Get the decode value for a code in a Lookup
	 * 
	 * @param code
	 *            Code to find
	 * @return Decode Value
	 */
	public String getDecodeValueFromLookup(String lookup, String code) {

		logger.entering(className, "getDecodeValueFromLookup", code);
		String decode = null;

		try {
			logger.finer("Calling to Lookup Service");
			decode = lookupOps.getDecodedValueForEncodedValue(lookup, code);
		} catch (tcAPIException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "getDecodeValueFromLookup - Unexpected error", e);
		}

		logger.exiting(className, "getDecodeValueFromLookup", decode);
		return decode;
	}
	
}
