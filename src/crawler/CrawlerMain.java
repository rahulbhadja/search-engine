package crawler;

import java.io.*;
import java.net.URL;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerMain {

	private final int MaxDepth = 5; // maximum depth limit for crawler to search in webpages
	private final int MaxPage = 100; // maximum pages crawler is allowed to fetched
	private HashSet<String> urls;

	public CrawlerMain() {
		urls = new HashSet<String>();
	}

	public String HTMLtoText(String ConstUrl) {
		try {
			Document doc = Jsoup.connect(ConstUrl).get();
			String text = doc.body().text();
			return text;
		} catch (Exception e) {

			return "Couldn't convert to Text";

		}
	}

	public Elements getUrlsFromPage(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		Elements urlsFromPage = doc.select("a[href]");

		return urlsFromPage;
	}

	public String savePages(String url) throws IOException {
		String location = "C:\\Users\\admin\\eclipse-workspace\\Search Engine\\W3WebPages\\Text" + System.nanoTime()
				+ ".txt";
		PrintWriter pw = new PrintWriter(location);
		pw.println(HTMLtoText(url));
		pw.close();

		return HTMLtoText(url);
	}

	public HashSet<String> getPageUrls(String url, int dep) {
		// to check if url is present and the depth is within limit(50)
		if ((!urls.contains(url) && (dep <= MaxDepth))) {
			try {
				urls.add(url);

				if (urls.size() >= MaxPage) // to check that the number of pages are within limit(100)
					return urls;

				Elements urlsFromPage = getUrlsFromPage(url);

				dep++;

				for (Element e : urlsFromPage) {
					getPageUrls(e.attr("abs:href"), dep);
				}
			} catch (IOException IOex) {
			}
		}
		return urls;
	}
}
