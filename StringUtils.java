import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Stringのユーティリティ.
 * 変換等。
 */
public final class StringUtils {
	/** コンストラクタ(全てstaticメソッドのクラスなのでインスタンス化なし) */
	private StringUtils() {
	}
	//--------------------------------------------------------------------------
	// 汎用ユーティリティ
	//--------------------------------------------------------------------------
	/**
	 * 右Trim.
	 * (1)nullの場合は空文字を返す。
	 * (2)null以外は右半角スペース及び全角スペースを削除する。
	 * @param in 入力文字列
	 * @return 戻り
	 */
	public static String rTrim(String s) {
		if (s == null) {
			return "";
		}
		int rightOffset = s.length();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(rightOffset - 1);
			if (!isTrimChar(c)) {
				break;
			}
			rightOffset--;
		}
		return s.substring(0, rightOffset);
	}
	/**
	 * 両側Trim.
	 * (1)nullの場合は空文字を返す。
	 * (2)null以外は左右半角スペース及び全角スペースを削除する。
	 * @param s 入力文字列
	 * @return 戻り
	 */
	public static String trim(String s) {
		if (s == null) {
			return "";
		}
		String w = rTrim(s);
		int leftOffset = 0;
		for (int i = 0; i < w.length(); i++) {
			char c = w.charAt(leftOffset);
			if (!isTrimChar(c)) {
				break;
			}
			leftOffset++;
		}
		return w.substring(leftOffset);
	}
	/**
	 * 右Padding(文字数).(注)バイト数でPaddingする場合は@see rPadByte(String s, int byteLength, char c)
	 * charLengthの文字数になるまで、cを付加して返す
	 * sの文字数＞＝最終文字数のときは、sをそのまま返す
	 * @param s 入力文字列(nullの場合はException)
	 * @param charLength 最終文字数
	 * @param c Padding文字
	 * @return Padding後文字列
	 */
	public static String rPad(String s, int charLength, char c) {
		if (s == null)
			s = "";
		assertNotNull(s);
		//もう既に文字数＞＝最終文字数ならば、Paddingなし
		if (s.length() >= charLength) {
			return s;
		}
		//Padding処理
		StringBuffer buf = new StringBuffer(s);
		for (int i = s.length(); i < charLength; i++) {
			buf.append(c);
		}
		return buf.toString();
	}
	/**
	 * 右半角スペースPadding(文字数).(注)バイト数でPaddingする場合は@see rPadByte(String s, int byteLength)
	 * charLengthの文字数になるまで、半角スペースを付加して返す
	 * sの文字数＞＝最終文字数のときは、sをそのまま返す
	 * @param s 入力文字列(nullの場合はException)
	 * @param charLength 最終文字数
	 * @return Padding後文字列
	 */
	public static String rPad(String s, int charLength) {
		return rPad(s, charLength, ' ');
	}
	/**
	 * 左Padding(文字数).(注)バイト数でPaddingする場合は@see lPadByte(String s, int byteLength, char c)
	 * charLengthの文字数になるまで、cを付加して返す
	 * sの文字数＞＝最終文字数のときは、sをそのまま返す
	 * @param s 入力文字列(nullの場合はException)
	 * @param charLength 最終文字数
	 * @param c Padding文字
	 * @return Padding後文字列
	 */
	public static String lPad(String s, int charLength, char c) {
		assertNotNull(s);
		//もう既に文字数＞＝最終文字数ならば、Paddingなし
		if (s.length() >= charLength) {
			return s;
		}
		//Padding処理
		StringBuffer buf = new StringBuffer(s);
		for (int i = s.length(); i < charLength; i++) {
			buf.insert(0, c);
		}
		return buf.toString();
	}
	/**
	 * 左半角スペースPadding(文字数).(注)バイト数でPaddingする場合は@see lPadByte(String s, int byteLength)
	 * charLengthの文字数になるまで、半角スペースを付加して返す
	 * sの文字数＞＝最終文字数のときは、sをそのまま返す
	 * @param s 入力文字列(nullの場合はException)
	 * @param charLength 最終文字数
	 * @return Padding後文字列
	 */
	public static String lPad(String s, int charLength) {
		return lPad(s, charLength, ' ');
	}
	/**
	 * 左ゼロPadding(文字数)。負の場合は先頭文字が"-"となる.
	 * charLength＜最終文字数のときは、例外発生
	 * @param i 数値
	 * @param charLength 最終文字数
	 * @return Padding後文字列
	 */
	public static String zeroPad(int i, int charLength) {
		String rtn;
		if (i >= 0) {
			rtn = lPad(Integer.toString(i), charLength, '0');
		} else {
			rtn = "-" + lPad(Integer.toString(-i), charLength - 1, '0');
		}
		//エラーチェック
		if (rtn.length() != charLength) {
			throw new RuntimeException(
				"指定したcharLengthではZEROパディングできません。数値="
					+ i
					+ ",charLength="
					+ charLength);
		}
		return rtn;
	}
	/**
	 * 指定文字数で文字列をカット.
	 * sの文字数＜＝指定文字数のときは、sをそのまま返す
	 * @param s 入力文字列(nullの場合はException)
	 * @param length 文字数
	 * @return カット後文字列
	 */
	public static String cutString(String s, int length) {
		assertNotNull(s);
		if (s.length() <= length) {
			return s;
		}
		return s.substring(0, length);
	}
	/**
	 * バイト長取得.
	 * @param s 入力文字列(nullの場合はException)
	 * @return バイト長
	 */
	public static int byteLength(String s) {
		assertNotNull(s);
		int rtn = 0;
		for (int i = 0; i < s.length(); i++) {
			rtn += byteLength(s.charAt(i));
		}
		return rtn;
	}
	/**
	 * １文字のバイト長取得.
	 * @param c 入力文字
	 * @return バイト長
	 */
	public static int byteLength(char c) {
		//ASCIIチェック
		if (c <= '\u007E') {
			return 1;
		}
		//\(YEN SIGN)----u005C(REVERSE SOLIDUS)とは異なる
		if (c == '\u00A5') {
			return 1;
		}
		//~(OVERLINE)----u007E(TILDE)とは異なる
		if (c == '\u203E') {
			return 1;
		}
		//半角カタカナ
		if (c >= '\uFF61' && c <= '\uFF9F') {
			return 1;
		}
		//以外は全角文字
		return 2;
	}
	/**
	 * 右Padding.
	 * byteLengthのバイト数になるまで、cを付加して返す
	 * sのバイト長＞＝最終バイト長のときは、sをそのまま返す
	 * バイト長境界が文字境界でない場合(多バイト文字の場合)直前の文字境界までPaddingして返す。
	 * @param s 入力文字列(nullの場合はException)
	 * @param byteLength 最終バイト長
	 * @param c Padding文字
	 * @return Padding後文字列
	 */
	public static String rPadByte(String s, int byteLength, char c) {
		if (s == null)
			s = "";
		assertNotNull(s);
		//もう既に文字長＞＝最終バイト長ならば、Paddingなし
		if (s.length() >= byteLength) {
			return s;
		}
		//文字バイト長取得
		int sByteLen = byteLength(s);
		//もう既に文字バイト長＞＝最終バイト長ならば、Paddingなし
		if (sByteLen >= byteLength) {
			return s;
		}
		//Padding文字バイト長取得
		int cByteLen = byteLength(c);
		//Padding処理
		StringBuffer buf = new StringBuffer(s);
		for (int i = sByteLen + cByteLen; i <= byteLength; i += cByteLen) {
			buf.append(c);
		}
		return buf.toString();
	}
	/**
	 * 右半角スペースPadding.
	 * byteLengthのバイト数になるまで、半角スペースを付加して返す
	 * sのバイト長＞＝最終バイト長のときは、sをそのまま返す
	 * バイト長境界が文字境界でない場合(多バイト文字の場合)直前の文字境界までPaddingして返す。
	 * @param s 入力文字列(nullの場合はException)
	 * @param byteLength 最終バイト長
	 * @return Padding後文字列
	 */
	public static String rPadByte(String s, int byteLength) {
		return rPadByte(s, byteLength, ' ');
	}
	/**
	 * 左Padding.
	 * byteLengthのバイト数になるまで、cを付加して返す
	 * sのバイト長＞＝最終バイト長のときは、sをそのまま返す
	 * バイト長境界が文字境界でない場合(多バイト文字の場合)直前の文字境界までPaddingして返す。
	 * @param s 入力文字列(nullの場合はException)
	 * @param byteLength 最終バイト長
	 * @param c Padding文字
	 * @return Padding後文字列
	 */
	public static String lPadByte(String s, int byteLength, char c) {
		assertNotNull(s);
		//もう既に文字長＞＝最終バイト長ならば、Paddingなし
		if (s.length() >= byteLength) {
			return s;
		}
		//文字バイト長取得
		int sByteLen = byteLength(s);
		//もう既に文字バイト長＞＝最終バイト長ならば、Paddingなし
		if (sByteLen >= byteLength) {
			return s;
		}
		//Padding文字バイト長取得
		int cByteLen = byteLength(c);
		//Padding処理
		StringBuffer buf = new StringBuffer(s);
		for (int i = sByteLen + cByteLen; i <= byteLength; i += cByteLen) {
			buf.insert(0, c);
		}
		return buf.toString();
	}
	/**
	 * 左半角スペースPadding.
	 * byteLengthのバイト数になるまで、半角スペースを付加して返す
	 * sのバイト長＞＝最終バイト長のときは、sをそのまま返す
	 * バイト長境界が文字境界でない場合(多バイト文字の場合)直前の文字境界までPaddingして返す。
	 * @param s 入力文字列(nullの場合はException)
	 * @param byteLength 最終バイト長
	 * @return Padding後文字列
	 */
	public static String lPadByte(String s, int byteLength) {
		return lPadByte(s, byteLength, ' ');
	}
	/**
	 * バイトオフセットによる部分文字列取得.
	 * (注)指定オフセットが文字境界でない場合、例外発生
	 * @param s 入力文字列(nullの場合はException)
	 * @param beginByteIndex 開始インデックス (この値を含む)
	 * @param endByteIndex 終了インデックス (この値を含まない)
	 * @return
	 */
	public static String substringByte(
		String s,
		int beginByteIndex,
		int endByteIndex) {
		assertNotNull(s);
		//１文字単位でLOOP
		int wBeginIndex = -1;
		int wEndIndex = -1;
		int wByteLength = 0;
		for (int i = 0; i < s.length(); i++) {
			if (wByteLength == beginByteIndex) {
				wBeginIndex = i;
			}
			if (wByteLength == endByteIndex) {
				wEndIndex = i;
			}
			wByteLength += byteLength(s.charAt(i));
		}
		
		if (wByteLength == beginByteIndex) {
			wBeginIndex = s.length();
		}
		if (wByteLength == endByteIndex) {
			wEndIndex = s.length();
		}
		//エラーチェック
		if (wBeginIndex < 0 || wEndIndex < 0) {
			throw new RuntimeException(
				"指定INDEXは文字境界ではありません。"
					+ "beginByteIndex="
					+ beginByteIndex
					+ ",endByteIndex="
					+ endByteIndex
					+ ",s=["
					+ s
					+ "]");
		}
		//return
		return s.substring(wBeginIndex, wEndIndex);
	}
	/**
	 * 指定バイト長で文字列をカット.
	 * バイト長境界が文字境界でない場合(多バイト文字の場合)、直前の文字境界でカット。
	 * @param s 入力文字列(nullの場合はException)
	 * @param byteLength バイト長
	 * @return カット後文字列
	 */
	public static String cutStringByte(String s, int byteLength) {
		assertNotNull(s);
		//１文字単位でLOOP
		int wEndIndex = s.length();
		int wByteLength = 0;
		for (int i = 0; i < s.length(); i++) {
			wByteLength += byteLength(s.charAt(i));
			if (wByteLength > byteLength) {
				wEndIndex = i;
				break;
			}
		}
		//return
		return s.substring(0, wEndIndex);
	}
	//--------------------------------------------------------------------------
	// char文字判断
	//--------------------------------------------------------------------------
	/**
	 * trim対象文字(半角スペース／制御文字／全角スペース)判断.
	 * @param c char入力文字
	 * @return true:trim対象文字
	 */
	private static boolean isTrimChar(char c) {
		//半角スペース又は制御文字
		if (c <= '\u0020') {
			return true;
		}
		//全角スペース
		if (c == '　') {
			return true;
		}
		return false;
	}

	private static void assertNotNull(Object in) {
		assertNotNull("", in);
	}
	 private static void assertNotNull(String message, Object in) {
		if (in == null) {
			throw new RuntimeException("assertNotNull()エラー:" + message);
		}
	}
	/**
	 * 必須チェック.
	 *   空文字がNG。
	 *   必ず、setterはtrim()/rTrim()をしているはずなのでオールスペースはOKとする
	 * @param s 入力文字列(nullの場合はException)
	 * @return true:チェックOK
	 */
	public static boolean isRequired(String s) {
		//null時Exception
		assertNotNull(s);
		//必須チェック
		if (s.length() == 0) {
			return false;
		}
		//OK
		return true;
	}

	/**
	 * 全て半角数字(0-9)チェック.
	 *   空文字はOK。スペースはNG
	 * @param s 入力文字列(nullの場合はException)
	 * @return true:チェックOK
	 */
	public static boolean isHalfSizeInt(String s) {
		//null時Exception
		assertNotNull(s);
		//全て半角数字チェック
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!isHalfSizeIntChar(c)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 半角数字(0-9)判断.
	 * @param c char入力文字
	 * @return true:半角数字(0-9)
	 */
	private static boolean isHalfSizeIntChar(char c) {
		if (c >= '0' && c <= '9') {
			return true;
		}
		return false;
	}
	/**
	 * 全て半角数字(0-9)＆MIN桁数＆MAX桁数チェック.
	 *   空文字はOK。スペースはNG
	 * @param s 入力文字列(nullの場合はException)
	 * @param inMinLength MIN桁数(文字数)
	 * @param inMaxLength MAX桁数(文字数)
	 * @return true:チェックOK
	 */
	public static boolean isHalfSizeIntAndMinMaxLength(
		String s,
		int inMinLength,
		int inMaxLength) {
		//null時Exception
		assertNotNull(s);
		//全て半角数字チェック
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!isHalfSizeIntChar(c)) {
				return false;
			}
		}
		//MIN桁数/MAX桁数チェック
		return isMinMaxLength(s, inMinLength, inMaxLength);
	}
	/**
	 * MIN桁数(文字数)＆MAX桁数(文字数)チェック.
	 * @param s 入力文字列(nullの場合はException)
	 * @param inMinLength MIN桁数(文字数)
	 * @param inMaxLength MAX桁数(文字数)
	 * @return true:チェックOK
	 */
	public static boolean isMinMaxLength(
		String s,
		int inMinLength,
		int inMaxLength) {
		//null時Exception
		assertNotNull(s);
		//MIN桁数(文字数)＆MAX桁数(文字数)チェック
		int wLength = s.length();
		return (wLength >= inMinLength && wLength <= inMaxLength);
	}
	//--------------------------------------------------------------------------
	// 日付関連
	//--------------------------------------------------------------------------
	/** DateFormatインスタンス */
	private static final DateFormat dateFmt = DateFormat.getDateInstance();

	/**
	 * Date型->"yyyyMMdd"変換.
	 * @param date Date型の日付(nullの場合はException)
	 * @return yyyyMMdd形式の文字列
	 */
	private static String formatYYYYMMDD(Date date) {
		//null時Exception
		assertNotNull(date);
		//変換
		synchronized (dateFmt) {
			((SimpleDateFormat)dateFmt).applyPattern("yyyyMMdd");
			dateFmt.setLenient(false);
			return dateFmt.format(date);
		}
	}

	/**
	 * 日付チェック(YYYYMMDDHHMMSS).
	 *   空文字はNG。スペースはNG
	 * @param in yyyyMMddHHmmss形式の文字列(nullの場合はException)
	 * @return true:チェックOK
	 */
	public static boolean isYYYYMMDDHHMMSS(String in) {
		//null時Exception
		assertNotNull(in);
		if (parseYYYYMMDDHHMMSS(in) == null) {
			return false;
		}
		//OK
		return true;
	}
	/**
	 * 日付チェック(YYYYMMDD).
	 *   空文字はNG。スペースはNG
	 * @param in yyyyMMdd形式の文字列(nullの場合はException)
	 * @return true:チェックOK
	 */
	public static boolean isYYYYMMDD(String in) {
		//null時Exception
		assertNotNull(in);
		if (parseYYYYMMDD(in) == null) {
			return false;
		}
		//OK
		return true;
	}
	/**
	 * 時刻チェック(HHMMSS)---"000000"～"235959".
	 *   空文字はNG。スペースはNG。"900"はNG。
	 * @param in HHmm形式の文字列(nullの場合はException)
	 * @return true:チェックOK
	 */
	public static boolean isHHMMSS(String in) {
		//null時Exception
		assertNotNull(in);
		if (parseHHMMSS(in) == null) {
			return false;
		}
		//OK
		return true;
	}
	/**
	 * 時刻チェック(HHMM)---"0000"～"2359"(2400を許すか場合によって変更の可能性あり).
	 *   空文字はNG。スペースはNG。"900"はNG。
	 * @param in HHmm形式の文字列(nullの場合はException)
	 * @return true:チェックOK
	 */
	public static boolean isHHMM(String in) {
		//null時Exception
		assertNotNull(in);
		//Date変換できるか？
		if (parseHHMM(in) == null) {
			return false;
		}
		//OK
		return true;
	}
	/**
	 * "yyyyMMddHHmmss"->Date型変換.
	 * @param in yyyyMMddHHmmss形式の文字列(nullの場合はException)
	 * @return Date型の日付(null時、変換エラー)
	 */
	public static Date parseYYYYMMDDHHMMSS(String in) {
		//null時Exception
		assertNotNull(in);
		//半角数字１４桁チェック
		if (!isHalfSizeIntAndMinMaxLength(in, 14, 14)) {
			return null;
		}
		//変換
		synchronized (dateFmt) {
			((SimpleDateFormat) dateFmt).applyPattern("yyyyMMddHHmmss");
			dateFmt.setLenient(false);
			Date date;
			try {
				date = dateFmt.parse(in);
			} catch (ParseException e) {
				date = null;
			}
			return date;
		}
	}

	/**
	 * "yyyyMMdd"->Date型変換.
	 * @param in yyyyMMdd形式の文字列(nullの場合はException)
	 * @return Date型の日付(null時、変換エラー)
	 */
	public static Date parseYYYYMMDD(String in) {
		//null時Exception
		assertNotNull(in);
		//半角数字８桁チェック
		if (!isHalfSizeIntAndMinMaxLength(in, 8, 8)) {
			return null;
		}
		//変換
		synchronized (dateFmt) {
			((SimpleDateFormat) dateFmt).applyPattern("yyyyMMdd");
			dateFmt.setLenient(false);
			Date date;
			try {
				date = dateFmt.parse(in);
			} catch (ParseException e) {
				date = null;
			}
			return date;
		}
	}
	/**
	 * "HHmmss"->Date型変換.
	 * @param in HHmmss形式の文字列(nullの場合はException)
	 * @return Date型の日付(null時、変換エラー)
	 */
	public static Date parseHHMMSS(String in) {
		//null時Exception
		assertNotNull(in);
		//半角数字６桁チェック
		if (!isHalfSizeIntAndMinMaxLength(in, 6, 6)) {
			return null;
		}
		//変換
		synchronized (dateFmt) {
			((SimpleDateFormat) dateFmt).applyPattern("HHmmss");
			dateFmt.setLenient(false);
			Date date;
			try {
				date = dateFmt.parse(in);
			} catch (ParseException e) {
				date = null;
			}
			return date;
		}
	}

	/**
	 * "HHmm"->Date型変換.
	 * @param in HHmm形式の文字列(nullの場合はException)
	 * @return Date型の日付(null時、変換エラー)
	 */
	public static Date parseHHMM(String in) {
		//null時Exception
		assertNotNull(in);
		//半角数字４桁チェック
		if (!isHalfSizeIntAndMinMaxLength(in, 4, 4)) {
			return null;
		}
		//変換
		synchronized (dateFmt) {
			((SimpleDateFormat) dateFmt).applyPattern("HHmm");
			dateFmt.setLenient(false);
			Date date;
			try {
				date = dateFmt.parse(in);
			} catch (ParseException e) {
				date = null;
			}
			return date;
		}
	}

	/**
	 * 指定日付から指定日数加えた(引いた)日付を取得する
	 * @param in yyyyMMdd形式の文字列(null、日付変換エラーの場合はException)
	 * @param inDateLength 加算日数(負の場合は減算日数)
	 * @return  求められたyyyyMMdd形式の文字列
	 */
	public static String addDate(String in, int inDateLength) {
		//Date変換
		Date date = parseYYYYMMDD(in);
		assertNotNull(date);
		//Calendar変換
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		//指定日数加算
		cal.add(Calendar.DATE, inDateLength);
		//String変換
		return formatYYYYMMDD(cal.getTime());
	}
}
