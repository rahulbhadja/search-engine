package GUI;

import io.javalin.http.staticfiles.Location;
import io.javalin.Javalin;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;

import SearchEngine.NotStaticSearchEngine;
import SearchEngine.SearchEngine;
import console.Console;

public class index {
  public static void main(String[] args) {
    Javalin app = Javalin.create(config -> {
      config.addStaticFiles("/", Location.CLASSPATH);
      Console.startProject();
    }).start(7003);

    app.get("/main", ct -> {
      ct.render("/GUI/index.html");
    });

    // API for search
    app.post("/search", ct -> {
      System.out.println(ct.formParam("Search_Query"));
      LinkedHashMap<String, Integer> results = Console.search(ct.formParam("Search_Query"));
      System.out.println(results);
      if (results != null) {

        ct.json(results);
      } else
        ct.json("{}");

    });
    // API for suggest
    app.post("/suggest", ct -> {
      String suggestion = Console.searchEngineMain.suggestWords(ct.formParam("Suggest"));

      ObjectMapper mapper = new ObjectMapper();
      ObjectNode JSON = mapper.createObjectNode();
      JSON.put("suggestion", suggestion);

      ct.json(JSON);
    });
  }
}
