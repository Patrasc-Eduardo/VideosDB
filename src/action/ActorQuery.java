package action;

import actor.Actor;
import actor.ActorsAwards;
import database.Database;
import org.json.simple.JSONObject;
import fileio.Writer;
import utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ActorQuery extends Query implements SortMap {
  /**
   * Some javadoc. // OK
   */
  public ActorQuery(final Database db) {
    super(db);
  }

  /**
   * Some javadoc. // OK
   */
  public JSONObject average(
      final int id,
      final ArrayList<Actor> actors,
      final Integer number,
      final String sortType,
      final Writer writer)
      throws IOException {
    String finalStr = "Query result: ";

    HashMap<String, Double> actorsRatings = new HashMap<>();

    for (Actor act : actors) {
      double bigRating = 0.0;
      int count = 0;

      for (String str : act.getFilmography()) {

        Double rating = 0.0;

        if (this.getDb().getVideoByTitle(str) != null) {
          ///// rating = this.getDb().getVideoByTitle(str).getRating();
          try {
            rating = Objects.requireNonNull(this.getDb().getVideoByTitle(str)).getRating();
          } catch (NullPointerException e) {
            System.out.println("Rating is null");
          }
          if (Double.compare(rating, 0.0) != 0) {
            count++;
          }
        }

        bigRating += rating;
      }

      if (count != 0) {
        bigRating /= count;
      }
      if (bigRating >= 1) {
        actorsRatings.put(act.getName(), bigRating);
      }
    }

    List<String> actorsSorted;
    actorsSorted = SortMap.sortMap(actorsRatings, sortType, null, null, number);
    finalStr += actorsSorted.toString();
    return writer.writeFile(id, null, finalStr);
  }
  /**
   * Some javadoc. // OK
   */
  public JSONObject awards(
      final int id,
      final ArrayList<Actor> actors,
      final String sortType,
      final Writer writer,
      final List<String> awards)
      throws IOException {

    String finalStr = "Query result: ";

    ArrayList<ActorsAwards> awardsList = new ArrayList<>();
    HashMap<String, Double> actorNumAwards = new HashMap<>();

    for (String strAwards : awards) {
      awardsList.add(Utils.stringToAwards(strAwards));
    }

    int count;
    Double sumAwards;
    for (Actor act : actors) {
      count = 0;
      sumAwards = 0.0;

      for (Map.Entry<ActorsAwards, Integer> entry : act.getAwards().entrySet()) {
        if (awardsList.contains(entry.getKey())) {

          sumAwards += entry.getValue();
          count++;
        } else {
          sumAwards += entry.getValue();
        }
      }
      if (count == awardsList.size()) {
        actorNumAwards.put(act.getName(), sumAwards);
      }
    }

    List<String> actorsSorted;

    actorsSorted = SortMap.sortMap(actorNumAwards, sortType, null, null, null);

    finalStr += actorsSorted.toString();
    return writer.writeFile(id, null, finalStr);
  }
  /**
   * Some javadoc. // OK
   *
   * @author Some javadoc. // OK
   * @param id javadoc. // OK
   * @return Some javadoc. // OK
   * @throws IOException // OK
   */
  public JSONObject filterDescription(
      final int id,
      final ArrayList<Actor> actors,
      final String sortType,
      final Writer writer,
      final List<String> words)
      throws IOException {

    String finalStr = "Query result: ";
    ArrayList<String> finalActors = new ArrayList<>();
    int count;

    for (Actor act : actors) {
      String description = act.getCareerDescription();
      count = 0;
      for (String word : words) {

        String patternString = "\\b(" + word + ")\\b";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(description.toLowerCase(Locale.ROOT));
        if (matcher.find()) {
          count++;
        }
      }

      if (count == words.size()) {
        finalActors.add(act.getName());
      }
    }
    Collections.sort(finalActors);
    if (sortType.compareTo("desc") == 0) {
      Collections.reverse(finalActors);
    }

    finalStr += finalActors.toString();

    return writer.writeFile(id, null, finalStr);
  }
}
