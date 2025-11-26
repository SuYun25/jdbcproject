package stock.util;

import java.util.Scanner;

public class InputUtil {

	// 안전한 정수 입력
	public static int getInt(Scanner sc, String message) {
		while (true) {
			try {
				System.out.print(message);
				return Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println(" 숫자만 입력 가능합니다. 다시 입력해주세요.");
			}
		}
	}

	// 공백 방지 문자열 입력
	public static String getString(Scanner sc, String message) {
		while (true) {
			System.out.print(message);
			String input = sc.nextLine().trim();
			if (!input.isEmpty())
				return input;

			System.out.println(" 값이 비어있습니다. 다시 입력해주세요.");
		}
	}
}
