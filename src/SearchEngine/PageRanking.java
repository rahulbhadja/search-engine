package SearchEngine;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PageRanking {

	private static String regexp = "[[ ]*|[,]*|[)]*|[(]*|[\"]*|[;]*|[-]*|[:]*|[']*|[�]*|[\\.]*|[:]*|[/]*|[!]*|[?]*|[+]*]+"; // regex
																															// to
																															// split
																															// the
																															// words

	// This method is used to get the page rank of a page if it exists in the index
	// and if not it will return 0
	public static int WordOcuurence(String URL, String WORD) throws IOException {
		Document webpages = Jsoup.connect(URL).get(); // get the webpage
		String content = webpages.body().text(); // get the content of the page
		Map<String, WordElement> map_content = new HashMap<String, WordElement>(); // map to store the words and their
																					// frequency

		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)))); // Read the
																											// content
																											// of the
																											// page

		String string; // string to read the file

		while ((string = reader.readLine()) != null) {
			String word[] = string.split(regexp);
			for (String allwords : word) {
				if ("".equals(allwords)) {
					continue;
				}

				WordElement woel = map_content.get(allwords); // get the word from the map

				if (allwords.equalsIgnoreCase(WORD)) // if the word is the word we are looking for
				{
					if (woel == null) {
						woel = new WordElement();
						woel.word = allwords;
						woel.count = 0;
						map_content.put(allwords, woel);
					}
					woel.count++;
				}
			}
		}
		reader.close(); // close the file
		SortedSet<WordElement> sort = new TreeSet<WordElement>(map_content.values()); // sort the map
		int page = 0; // variable to store the page rank
		int max = 1000;

		LinkedList<String> unusedWords = new LinkedList<>(); // list to store the unused words
		try {
			BufferedReader buReader = new BufferedReader(new FileReader("stopwords.txt"));
			String w;
			while ((w = buReader.readLine()) != null) {
				unusedWords.add(w);
			}
			buReader.close();
		} catch (FileNotFoundException e) {
			// System.out.println("Oops!! Sorry..The desired word not found");
			System.out.println("Oops!! Sorry.. Looks like the word you are looking for is not found");
		}

		for (WordElement words : sort) {
			if (page >= max) // if the page rank is greater than 1000 then break
			{

				break;
			}
			if (unusedWords.contains(words.word)) {
				page++;
				max++;
			} else {
				page++;
				return words.count;
			}
		}
		return 0;
	}

	public static class WordElement implements Comparable<WordElement> // class to store the words and their frequency
	{
		String word;
		int count;

		@Override
		public int hashCode() //
		{
			return word.hashCode();
		}

		@Override
		public boolean equals(Object object) {
			return word.equals(((WordElement) object).word);
		}

		@Override
		public int compareTo(WordElement we) {
			return we.count - count;
		}
	}
}
