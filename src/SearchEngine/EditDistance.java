package SearchEngine;

public class EditDistance {

	public static int editDistanceCal(String str1, String str2) {
		int stringLen1 = str1.length(); // length of str1
		int stringLen2 = str2.length(); // length of str2

		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] distance = new int[stringLen1 + 1][stringLen2 + 1]; // create a matrix to store the distance

		for (int i = 0; i <= stringLen1; i++)
			distance[i][0] = i; // the distance of any first string to an empty second string

		for (int j = 0; j <= stringLen2; j++)
			distance[0][j] = j; // the distance of any second string to an empty first string

		// iterate though, and check last char
		for (int i = 0; i < stringLen1; i++) {
			char c1 = str1.charAt(i); // get the char of the first string
			for (int j = 0; j < stringLen2; j++) {
				char c2 = str2.charAt(j); // get the char of the second string

				// if last two chars equal
				if (c1 == c2) {
					// update dp value for +1 length
					distance[i + 1][j + 1] = distance[i][j];
				} else {
					int replace = distance[i][j] + 1;
					int insert = distance[i][j + 1] + 1;
					int delete = distance[i + 1][j] + 1;
					int min = 0;
					if (replace > insert)
						min = insert;
					else
						min = replace;

					if (delete < min)
						min = delete;

					distance[i + 1][j + 1] = min;
				}
			}
		}

		return distance[stringLen1][stringLen2];
	}

}
