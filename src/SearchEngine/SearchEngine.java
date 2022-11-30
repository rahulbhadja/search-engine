package SearchEngine;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import crawler.Trie;
import crawler.CrawlerMain;

public class SearchEngine implements Serializable {
	private static Trie<ArrayList<Integer>> trie;
	private final String wordRegex = "[[ ]*|[,]*|[)]*|[(]*|[\"]*|[;]*|[-]*|[:]*|[']*|[�]*|[\\.]*|[:]*|[/]*|[!]*|[?]*|[+]*]+";
	private static HashSet<String> allLinks;

	private static LinkedList<String> suggestions = new LinkedList<>();

	public HashSet<String> createTrie(String urlName) throws IOException {
		Hash hash = new Hash();
		CrawlerMain crawl = new CrawlerMain();

		trie = new Trie<ArrayList<Integer>>();

		HashSet<String> unrequiredWords = hash.savepages("stopwords.txt");

		allLinks = crawl.getPageUrls(urlName, 1);

		HashSet<String> temp = null;
		String txt;
		String word;
		String[] split_Words;

		Iterator<String> linkIterator = null;
		Iterator<String> wordIterator = null;

		linkIterator = allLinks.iterator();

		int i = 0;
		while (linkIterator.hasNext()) {
			String s1 = linkIterator.next();
			txt = crawl.HTMLtoText(s1);

			if (txt.length() == 0) {
				continue;
			}

			txt = txt.toLowerCase();
			split_Words = txt.split(wordRegex);

			for (String s : split_Words) {
				suggestions.add(s);
			}

			suggestions.removeAll(unrequiredWords);

			temp = new HashSet<String>(Arrays.asList(split_Words));
			temp.remove(unrequiredWords);

			wordIterator = temp.iterator();

			while (wordIterator.hasNext()) {
				word = (String) wordIterator.next();
				ArrayList<Integer> arrList = trie.searchWord(word);

				if (arrList == null) {
					trie.put(word, new ArrayList<Integer>(Arrays.asList(i)));
				} else {
					arrList.add(i);
				}
			}

			i++;
		}

		return allLinks;
	}

	public String[] search(String[] index) {

		int[] count = new int[allLinks.size()];
		List<String> links = new ArrayList<String>(allLinks);

		ArrayList<Integer> tmp = null;
		for (int i = 0; i < index.length; ++i) {
			tmp = trie.searchWord(index[i].toLowerCase());

			if (tmp != null) {
				for (int k = 0; k < tmp.size(); k++) {
					count[tmp.get(k)]++;
				}
			} else {
				System.out.println("The word <" + index[i] + "> is not in any file!");
				// suggestWords(index[i]);
				return null;
			}
		}
		/* Answers stores the indexes of the webPages */
		ArrayList<String> webPages = new ArrayList<String>();
		for (int m = 0; m < count.length; ++m) {
			if (count[m] == index.length) {
				webPages.add(links.get(m));
			}
		}
		return webPages.toArray(new String[0]);
	}

	// function to provide suggestions
	public static String suggestWords(String s) {

		int dstnc = 10000;
		String suggest = "No Suggestions!";

		for (String suggest1 : suggestions) {
			int d = EditDistance.editDistanceCal(s, suggest1);
			if (d < dstnc) {
				suggest = suggest1;
				dstnc = d;
			}
		}
		System.out.println("Did you mean " + suggest + "?");
		return suggest;

	}

}
