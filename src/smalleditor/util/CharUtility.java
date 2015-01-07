package smalleditor.util;




/**
 * Utility class to determine if a character is a special Json character.
 *
 * @author Matt Garner
 *
 */
public final class CharUtility {
	public static final char[] javascriptBrackets = {'(', ')', '[', ']', '{', '}'};
	public static final char[] javascriptContentAssistTriggers = {'(', '[', '{', '.'};

	public static final char openCurly = javascriptBrackets[4];
	public static final char closeCurly = javascriptBrackets[5];
	public static final char openSquare = javascriptBrackets[2];
	public static final char closeSquare = javascriptBrackets[3];
	public static final char comma = ',';
	public static final char quote = '"';
	public static final char colon = ':';
	public static final char backslash = '\\';
	public static final char slash = '/';
	public static final char minus = '-';
	public static final char underscore = '_';
	public static final char plus = '+';
	public static final char point = '.';
//	public static final char n = 'n';
//	public static final char u = 'u';
//	public static final char l = 'l';
//	public static final char f = 'f';
//	public static final char a = 'a';
//	public static final char s = 's';
//	public static final char e = 'e';
//	public static final char t = 't';
//	public static final char r = 'r';
//	public static final char E = 'E';
	public static final char eof = (char) -1;
	public static final int space = ' ';
	public static final int eol = '\n';
	public static final int tab = '\t';
	public static final int carriageReturn = '\r';
	
	public static boolean isAlphaNumeric(char ch) {
		return Character.isLetterOrDigit(ch) || ch == underscore; 
	}

	public static boolean isWhiteSpace(char ch) {
		if (ch == space || ch == eol || ch == tab || ch == carriageReturn) {
			return true;
		}

		return false;
	}

	public static boolean isNotWhiteSpace(char ch) {
		if (ch != space && ch != eol && ch != tab && ch != carriageReturn) {
			return true;
		}

		return false;
	}

	public static boolean isClosed(char ch) {
		if (ch == comma || ch == closeCurly || ch == closeSquare) {
			return true;
		}

		return false;
	}

	public static boolean isNotClosed(char ch) {
		if (ch != comma && ch != closeCurly && ch != closeSquare) {
			return true;
		}
		return false;
	}
	
	public static boolean isBlock(char ch) {
		return ch == openCurly || ch == closeCurly ||
				ch == openSquare || ch == closeSquare;
	}
	
	public static boolean isNewLine(char ch) {
		return ch == eol || ch == carriageReturn;
	}
	
	public static boolean isOneLineComment(char ch, char previous) {
		return ch == slash && previous == slash;
	}
}
