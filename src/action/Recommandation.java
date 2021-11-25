package action;

import database.User;
import entertainment.Genre;
import fileio.Writer;
import org.json.simple.JSONObject;
import utils.Utils;
import video.Video;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Recommandation implements SortMap {

  public Recommandation() { }

  /**
   * Some javadoc. // OK
   */
  public JSONObject standard(
      final int id, final ArrayList<Video> allVids, final User user, final Writer writer)
      throws IOException {
    String finalStr = "Standard";
    if (user.getHistory().size() == allVids.size()) {
      finalStr += "Recommendation cannot be applied!";
      return writer.writeFile(id, null, finalStr);
    }

    for (Video vd : allVids) {
      if (!user.getHistory().containsKey(vd.getTitle())) {
        finalStr += "Recommendation result: " + vd.getTitle();
        return writer.writeFile(id, null, finalStr);
      }
    }
    return null;
  }
  /**
   * Some javadoc. // OK
   *
   * @author Some javadoc. // OK
   * @param Some javadoc. // OK
   * @return Some javadoc. // OK
   * @throws Some javadoc. // OK
   * @exception Some javadoc. // OK
   * @see Some javadoc. // OK
   * @since Some javadoc. // OK
   * @serialData // OK
   */
  public JSONObject bestUnseen(
      final int id, final ArrayList<Video> allVids, final User user, final Writer writer)
      throws IOException {
    String finalStr = "BestRatedUnseen";

    if (user.getHistory().size() == allVids.size()) {
      finalStr += "Recommendation cannot be applied!";
      return writer.writeFile(id, null, finalStr);
    }
    HashMap<String, Double> videoRatings = new LinkedHashMap<>();
    for (Video vid : allVids) {
      if (!user.getHistory().containsKey(vid.getTitle())) {
        videoRatings.put(vid.getTitle(), vid.getRating());
      }
    }
    ArrayList<String> videoNames = new ArrayList<>();
    for (Video v : allVids) {
      videoNames.add(v.getTitle());
    }
    List<String> names = SortMap.sortMap(videoRatings, "desc", "yes", videoNames, 1);
    finalStr += "Recommendation result: " + names.get(0);
    return writer.writeFile(id, null, finalStr);
  }
  /**
   * Some javadoc. // OK
   *
   * @author Some javadoc. // OK
   * @param Some javadoc. // OK
   * @return Some javadoc. // OK
   * @throws Some javadoc. // OK
   * @exception Some javadoc. // OK
   * @see Some javadoc. // OK
   * @since Some javadoc. // OK
   * @serialData // OK
   */
  public JSONObject popular(
      final int id, final ArrayList<Video> allVids, final User user, final Writer writer)
      throws IOException {
    String finalStr = "Popular";

    if (user.getHistory().size() == allVids.size()) {
      finalStr += "Recommendation cannot be applied!";
      return writer.writeFile(id, null, finalStr);
    }

    HashMap<String, Double> popularGenres = new HashMap<>();
    HashMap<String, ArrayList<Video>> moviesByGenres = new HashMap<>();

    for (Genre gen : Genre.values()) {
      for (Video vid : allVids) {
        if (vid.getGenre().contains(Utils.genreToString(gen))) {

          if (!popularGenres.containsKey(Utils.genreToString(gen))) {
            popularGenres.put(Utils.genreToString(gen), 1.0);
          } else {
            popularGenres.put(
                Utils.genreToString(gen), popularGenres.get(Utils.genreToString(gen)) + 1.0);
          }
          ArrayList<Video> videoList;
          videoList = moviesByGenres.get(Utils.genreToString(gen));
          if (videoList == null) {
            videoList = new ArrayList<>();
            videoList.add(vid);
            moviesByGenres.put(Utils.genreToString(gen), videoList);
          } else {
            if (!videoList.contains(vid)) {
              videoList.add(vid);
            }
          }
        }
      }
    }
    ArrayList<String> videoNames = new ArrayList<>();
    for (Video v : allVids) {
      videoNames.add(v.getTitle());
    }
    List<String> popGenres = SortMap.sortMap(popularGenres, "desc", null, videoNames, null);
    for (String genre : popGenres) {
      for (Map.Entry<String, ArrayList<Video>> entry : moviesByGenres.entrySet()) {
        if (entry.getKey().compareTo(genre) == 0) {
          for (Video vid : entry.getValue()) {
            if (!user.getHistory().containsKey(vid.getTitle())) {
              finalStr += "Recommendation result: " + vid.getTitle();
              return writer.writeFile(id, null, finalStr);
            }
          }
        }
      }
    }
    finalStr += "Recommendation cannot be applied!";
    return writer.writeFile(id, null, finalStr);
  }
  /**
   * Some javadoc. // OK
   *
   * @author Some javadoc. // OK
   * @param Some javadoc. // OK
   * @return Some javadoc. // OK
   * @throws Some javadoc. // OK
   * @exception Some javadoc. // OK
   * @see Some javadoc. // OK
   * @since Some javadoc. // OK
   * @serialData // OK
   */
  public JSONObject favorite(
      final int id,
      final ArrayList<Video> allVids,
      final User user,
      final ArrayList<User> allUsers,
      final Writer writer)
      throws IOException {
    String finalStr = "Favorite";

    HashMap<String, Double> vidsByFavorite = new HashMap<>();

    for (Video vid : allVids) {
      for (User us : allUsers) {
        if (us.getFavorite().contains(vid.getTitle())) {
          if (!vidsByFavorite.containsKey(vid.getTitle())) {
            vidsByFavorite.put(vid.getTitle(), 1.0);
          } else {
            vidsByFavorite.put(vid.getTitle(), vidsByFavorite.get(vid.getTitle()) + 1.0);
          }
        }
      }
    }
    ArrayList<String> videoNames = new ArrayList<>();
    for (Video v : allVids) {
      videoNames.add(v.getTitle());
    }
    List<String> vidsSorted = SortMap.sortMap(vidsByFavorite, "desc", "yes", videoNames, null);
    for (String vid : vidsSorted) {
      if (!user.getHistory().containsKey(vid)) {
        finalStr += "Recommendation result: " + vid;
        return writer.writeFile(id, null, finalStr);
      }
    }
    finalStr += "Recommendation cannot be applied!";
    return writer.writeFile(id, null, finalStr);
  }
  /**
   * Some javadoc. // OK
   *
   * @author Some javadoc. // OK
   * @param Some javadoc. // OK
   * @return Some javadoc. // OK
   * @throws Some javadoc. // OK
   * @exception Some javadoc. // OK
   * @see Some javadoc. // OK
   * @since Some javadoc. // OK
   * @serialData // OK
   */
  public JSONObject search(
      final int id,
      final ArrayList<Video> allVids,
      final User user,
      final String genre,
      final Writer writer)
      throws IOException {
    String finalStr = "Search";
    HashMap<String, Double> videoRatings = new HashMap<>();

    if (user.getHistory().size() == allVids.size()) {
      finalStr += "Recommendation cannot be applied!";
      return writer.writeFile(id, null, finalStr);
    }

    for (Video vid : allVids) {
      if (vid.getGenre().contains(genre) && !user.getHistory().containsKey(vid.getTitle())) {
        videoRatings.put(vid.getTitle(), vid.getRating());
      }
    }
    if (videoRatings.size() != 0) {
      List<String> vidsSorted;
      ArrayList<String> videoNames = new ArrayList<>();
      for (Video v : allVids) {
        videoNames.add(v.getTitle());
      }
      vidsSorted = SortMap.sortMap(videoRatings, "asc", null, videoNames, null);
      finalStr += "Recommendation result: " + vidsSorted.toString();
    } else {
      finalStr += "Recommendation cannot be applied!";
    }
    return writer.writeFile(id, null, finalStr);
  }
}
