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

public class NotStaticSearchEngine implements Serializable {
	private Trie<ArrayList<Integer>> trie; // trie of words
	private final String wordRegex = "[[ ]*|[,]*|[)]*|[(]*|[\"]*|[;]*|[-]*|[:]*|[']*|[�]*|[\\.]*|[:]*|[/]*|[!]*|[?]*|[+]*]+"; // regex
																																// to
																																// split
																																// words
	private HashSet<String> allLinks; // all links in the web

	private LinkedList<String> suggestions = new LinkedList<>(); // suggestions for the user

	public HashSet<String> createTrie(String urlName) throws IOException {
		Hash hash = new Hash(); // hash of all words
		CrawlerMain crawl = new CrawlerMain(); // crawler

		trie = new Trie<ArrayList<Integer>>(); // trie of words

		HashSet<String> unrequiredWords = hash.savepages("stopwords.txt");

		allLinks = crawl.getPageUrls(urlName, 1); // get all links

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
				suggestions.add(s); // add all words to suggestions
			}

			suggestions.removeAll(unrequiredWords); // remove unrequired words from suggestions

			temp = new HashSet<String>(Arrays.asList(split_Words));
			temp.remove(unrequiredWords); // remove unrequired words from words

			wordIterator = temp.iterator();

			while (wordIterator.hasNext()) {
				word = (String) wordIterator.next();
				ArrayList<Integer> arrList = trie.searchWord(word); // search for the word in the trie

				if (arrList == null) {
					trie.put(word, new ArrayList<Integer>(Arrays.asList(i))); // if the word is not in the trie, add it
				} else {
					arrList.add(i);
				}
			}

			i++;
		}

		return allLinks;
	}

	public String[] search(String[] index) // search method
	{

		int[] count = new int[allLinks.size()]; // array of counts
		List<String> links = new ArrayList<String>(allLinks); // list of links

		ArrayList<Integer> tmp = null;
		for (int i = 0; i < index.length; ++i) {
			tmp = trie.searchWord(index[i].toLowerCase());

			if (tmp != null) {
				for (int k = 0; k < tmp.size(); k++) {
					count[tmp.get(k)]++;
				}
			} else {
				// System.out.println("The word <" + index[i] + "> is not in any file!");
				System.out.println("Oops.. ! The word <" + index[i] + "> is not present in  any file!");
				suggestWords(index[i]); // suggest words
				return null;
			}
		}
		/* Answers stores the indexes of the webPages */
		ArrayList<String> webPages = new ArrayList<String>(); // list of web pages
		for (int m = 0; m < count.length; ++m) {
			if (count[m] == index.length) {
				webPages.add(links.get(m));
			}
		}
		return webPages.toArray(new String[0]);
	}

	// function to provide suggestions
	public String suggestWords(String s) {

		int dstnc = 10000; // distance
		String suggest = "No Suggestions!"; // suggestion

		for (String suggest1 : suggestions) {
			int d = EditDistance.editDistanceCal(s, suggest1); // calculate edit distance
			if (d < dstnc) {
				suggest = suggest1; // update suggestion
				dstnc = d;
			}
		}

		System.out.println("Did you mean " + suggest + "?");
		return suggest;
	}

}
