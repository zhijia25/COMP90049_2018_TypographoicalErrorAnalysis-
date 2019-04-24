import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class KnoeledgeTechnology1 {

	public static void main(String args[]) {
		Scanner mispell = null;
		Scanner correct = null;
		Scanner dic = null;
		ArrayList<String> respones = new ArrayList<String>();
		int totalGuess = 0;
		try {
			mispell = new Scanner(new FileInputStream("wiki_misspell.txt"));
			correct = new Scanner(new FileInputStream("wiki_correct.txt"));
			int count = 0;
			while (mispell.hasNextLine()) {

				dic = new Scanner(new FileInputStream("dict.txt"));
				int minDis = 999999999;
				String mis = mispell.nextLine();
				String cor = correct.nextLine();

				while (dic.hasNextLine()) {

					String dicSample = dic.nextLine();
					int distance = LevenshteindDistance(mis, dicSample);
					// int distance1 = nGram(mis, dicSample, 2);
					// int distance1 = soundex_distance(mis, dicSample);
					if (distance < 3) {
						// if (distance < 4) {

						respones.add(dicSample);
					}

				}
				System.out.print(mis + " ");
				System.out.print(minDis + " " + "(" + " ");
				for (int i = 0; i < respones.size(); i++) {
					System.out.print(respones.get(i) + " ");
				}
				System.out.println(")");
				if (respones.contains(cor)) {
					count++;
				}

				totalGuess = respones.size() + totalGuess;
				respones.clear();
			}

			mispell.close();
			correct.close();
			dic.close();
			float recall = (float) count / (float) 4453;
			float precision = (float) count / (float) totalGuess;

			System.out.println("precision: " + precision);
			System.out.println("recall: " + recall);
		}

		catch (FileNotFoundException e) {
			System.out.println("File morestuff.txt was not found");
			System.out.println("or could not be opened.");
			System.exit(0);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static int LevenshteindDistance(String word1, String word2) {
		int len1 = word1.length(), len2 = word2.length();
		int[][] dp = new int[len2 + 1][len1 + 1];
		int m = 0, i = 1, d = 1, r = 1;
		// int m = 0, i = 1, d = 3, r = 3;
		// int m = 0, i = 3, d = 1, r = 3£»
		// int m = 0, i = 3, d = 3, r = 1;
		// int m = 0, i = 1, d = 4, r = 4;
		// int m = 0, i = 4, d = 1, r = 4£»
		// int m = 0, i = 4, d = 4, r = 1;

		for (int k = 0; k <= len1; k++) {
			dp[0][k] = k * d;
		}

		for (int j = 0; j <= len2; j++) {
			dp[j][0] = j * i;
		}

		for (int j = 1; j <= len2; j++) {
			for (int k = 1; k <= len1; k++) {
				if (word1.charAt(k - 1) == word2.charAt(j - 1)) {
					dp[j][k] = Math.min(dp[j][k - 1] + d, Math.min(dp[j - 1][k] + i, dp[j - 1][k - 1] + m));
				} else {
					dp[j][k] = Math.min(dp[j][k - 1] + d, Math.min(dp[j - 1][k] + i, dp[j - 1][k - 1] + r));
				}
			}
		}

		return dp[len2][len1];
	}

	public static int nGram(String word1, String word2, int size) {
		ArrayList<String> tokenList1 = new ArrayList<String>();
		ArrayList<String> tokenList2 = new ArrayList<String>();
		String terminal = "#";
		word1 = terminal + word1 + terminal;
		word2 = terminal + word2 + terminal;
		for (int i = 0; i < word1.length() - 1; i++) {
			tokenList1.add(word1.substring(i, i + size));
		}

		for (int i = 0; i < word2.length() - 1; i++) {
			tokenList2.add(word2.substring(i, i + size));
		}

		int numOfToken1 = tokenList1.size();
		int numOfToken2 = tokenList2.size();
		int numOfToken = numOfToken1 + numOfToken2;

		removeDuplicate(tokenList1);
		removeDuplicate(tokenList2);
		tokenList1.retainAll(tokenList2);

		int numOfSame = tokenList1.size();
		int sim = numOfToken - (size * numOfSame);

		return sim;
	}

	private static void removeDuplicate(ArrayList<String> list) {
		ArrayList<String> result = new ArrayList<String>(list.size());
		for (String str : list) {
			if (!result.contains(str)) {
				result.add(str);
			}
		}
		list.clear();
		list.addAll(result);
	}

	public static char[] removeDuplicates(char[] list) {
		if (list.length <= 1)
			return list;

		for (int i = 1; i < list.length; i++) {
			if (list[i] == list[i - 1]) {
				for (int j = i; j < list.length; j++) {
					list[j - 1] = list[j];
				}
				list = Arrays.copyOf(list, list.length - 1);
			}
		}
		return list;
	}

	public static String SoundexCode(String word) {
		word.toLowerCase();
		char[] wordList = word.toCharArray();
		for (int i = 1; i < wordList.length; i++) {

			String list0 = "aehiouwy";
			String list1 = "bpfv";
			String list2 = "cgjkqsxz";
			String list3 = "dt";
			String list4 = "l";
			String list5 = "mn";
			String list6 = "r";

			if (list0.contains(Character.toString(wordList[i])))
				wordList[i] = '0';
			else if (list1.contains(Character.toString(wordList[i])))
				wordList[i] = '1';
			else if (list2.contains(Character.toString(wordList[i])))
				wordList[i] = '2';
			else if (list3.contains(Character.toString(wordList[i])))
				wordList[i] = '3';
			else if (list4.contains(Character.toString(wordList[i])))
				wordList[i] = '4';
			else if (list5.contains(Character.toString(wordList[i])))
				wordList[i] = '5';
			else if (list6.contains(Character.toString(wordList[i])))
				wordList[i] = '6';
		}
		wordList = removeDuplicates(wordList);

		if (wordList.length > 4)
			wordList = Arrays.copyOf(wordList, 4);

		return String.valueOf(wordList);
	}

	public static int soundex_distance(String word1, String word2) {
		String sounfdexCode1 = SoundexCode(word1);
		String sounfdexCode2 = SoundexCode(word2);
		return LevenshteindDistance(sounfdexCode1, sounfdexCode2);

	}

}
