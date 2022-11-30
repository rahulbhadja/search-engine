package HtmlParser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLtoText {

	public static void HTMLtoText(File file, String Filename) throws IOException {
		
		Document d = Jsoup.parse(file, "utf-8");
		
		String doc = d.text();
		
		String path ="D://projectjava" + Filename + ".txt";
		PrintWriter p = new PrintWriter(path);
		
		p.println(doc);
		
		p.close();

	}

}
