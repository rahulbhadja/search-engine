package console;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import SearchEngine.PageRanking;
import SearchEngine.SearchEngine;
import SearchEngine.NotStaticSearchEngine;

public class Console {

	private static final String URL = "https://www.w3.org/";
	private static final String Regex = "[[ ]*|[,]*|[)]*|[(]*|[\"]*|[;]*|[-]*|[:]*|[']*|[�]*|[\\.]*|[:]*|[/]*|[!]*|[?]*|[+]*]+";
	public static NotStaticSearchEngine searchEngineMain;

	public static void startProject() {
		try {

			File file = new File(
					// create path in the project folde
					"src/console/SearchEngine.txt");

			if (file.exists() && !file.isDirectory()) {

				FileInputStream fileIn = new FileInputStream(
						"src/console/SearchEngine.txt");

				ObjectInputStream IN = new ObjectInputStream(fileIn);
				searchEngineMain = (NotStaticSearchEngine) IN.readObject();
				System.out.println(search("testing") + " this are the results");

				IN.close();
				fileIn.close();
			} else {

				searchEngineMain = new NotStaticSearchEngine();
				System.out.println("serachengine started");

				HashSet<String> ht = searchEngineMain.createTrie(URL);// create hash table using trie

				FileOutputStream fileOut = new FileOutputStream(
						"src/console/SearchEngine.txt");

				ObjectOutputStream OUT = new ObjectOutputStream(fileOut);
				OUT.writeObject(searchEngineMain);
				OUT.close();
				fileOut.close();

			}

		} catch (Exception e) {
			System.out.print(e);
		}

	}

	public static LinkedHashMap<String, Integer> search(String word) {
		try {
			boolean bool = true;
			Scanner sc = new Scanner(System.in);

			while (bool) {
				bool = false;
				System.out.println("\t enter the word to be searched");

				if (!word.equals(null)) {
					String[] Sp = word.split(Regex);// spilt the regular expression word and the entered // word
					String[] allpages = searchEngineMain.search(Sp);

					try {
						if (allpages == null) {

						} else {

							Map<String, Integer> Ul = null;// for unsorted Links
							Ul = new HashMap<>();

							for (String Url : allpages) {
								// if word is not present return other page

								Ul.put(Url, PageRanking.WordOcuurence(Url, word));
							}

							LinkedHashMap<String, Integer> reverse = new LinkedHashMap<>();
							Ul.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
									.forEachOrdered(x -> reverse.put(x.getKey(), x.getValue()));
							return reverse;
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}

				} else {

					// retunt all pages if word is null

					// System.out.println("please enter a valid word, " + word + " is invalid.");
					// continue;
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	public static void main(String[] args) throws IOException {

		SearchEngine Se = new SearchEngine();
		System.out.println("search engine started");

		HashSet<String> Ht = Se.createTrie(URL);// create hash table using trie

		boolean bool = true;
		Scanner sc = new Scanner(System.in);
		String word;

		while (bool) {
			System.out.println("enter your word");
			word = sc.next();

			if (!word.equals(null)) {
				String[] sp = word.split(Regex);// spilt the regular expression word and the entered word
				String[] searchpages = Se.search(sp);

				try {
					if (searchpages == null) {

					} else {
						Map<String, Integer> Ul = null;
						Ul = new HashMap<>();

						for (String url : searchpages) {

							Ul.put(url, PageRanking.WordOcuurence(url, word));
						}
						LinkedHashMap<String, Integer> reverse = new LinkedHashMap<>();
						Ul.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
								.forEachOrdered(x -> reverse.put(x.getKey(), x.getValue()));

						System.out.println(" Page Rank  Search ");

						int no = 1;

						for (Map.Entry<String, Integer> put : reverse.entrySet()) {
							if (no > 10)
								break;
							System.out.println("-| " + put.getValue() + " | --> |" + put.getKey() + " ");
							no++;
						}
						System.out.println("want to search again? -- Press 's' \n Exit? -------- Press 'e' ");

						while (true) {
							String str = sc.next();
							if (str.equals("s")) {
								break;
							} else if (str.equals("e")) {
								bool = false;
								System.out.println("thank you.");
								System.exit(1);
								sc.close();
							} else {
								System.out.println(
										"input is wrong. Please input valid characters. \n More words to search? -------- Press 's' \\n Exit? -------- Press 'e' ");
							}
						}
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} else {
				System.out.println("please enter a valid word, " + word + " is invalid.");
				continue;
			}
		}
	}
}
