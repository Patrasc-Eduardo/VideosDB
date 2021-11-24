package action;

import database.Database;
import database.User;
import fileio.Writer;
import org.json.simple.JSONObject;
import video.Video;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoQuery extends Query implements SortMap {
  public VideoQuery(final Database db) {
    super(db);
  }

  /**
   * Some javadoc. // OK
   *
   * @author Some javadoc. // OK
   * @version Some javadoc. // OK
   * @param Some javadoc. // OK
   * @return Some javadoc. // OK
   * @throws Some javadoc. // OK
   * @exception Some javadoc. // OK
   * @see Some javadoc. // OK
   * @since Some javadoc. // OK
   * @serial Some javadoc. // OK
   * @serialField // OK
   * @serialData // OK
   * @deprecated Some javadoc. // OK
   */
  public JSONObject rating(
      final int id,
      final ArrayList<Video> videos,
      final int number,
      final String sortType,
      final int year,
      final String genre,
      final Writer writer)
      throws IOException {

    String finalStr = "Query result: ";
    HashMap<String, Double> videoRatings = new HashMap<>();
    Double rating = 0.0;

    for (Video vid : videos) {
      rating = vid.getRating();
      if (year == 0 && genre == null && Double.compare(rating, 0.0) != 0) {
        videoRatings.put(vid.getTitle(), rating);
      } else if (vid.getYear() == year && genre == null && Double.compare(rating, 0.0) != 0) {
        videoRatings.put(vid.getTitle(), rating);
      } else if (year == 0 && vid.getGenre().contains(genre) && Double.compare(rating, 0.0) != 0) {
        videoRatings.put(vid.getTitle(), rating);
      } else if (vid.getYear() == year
          && vid.getGenre().contains(genre)
          && Double.compare(rating, 0.0) != 0) {
        videoRatings.put(vid.getTitle(), rating);
      }
    }
    List<String> vidsSorted;
    vidsSorted = SortMap.sortMap(videoRatings, sortType, null, null, number);
    finalStr += vidsSorted.toString();
    return writer.writeFile(id, null, finalStr);
  }
  /**
   * Some javadoc. // OK
   *
   * @author Some javadoc. // OK
   * @version Some javadoc. // OK
   * @param Some javadoc. // OK
   * @return Some javadoc. // OK
   * @throws Some javadoc. // OK
   * @exception Some javadoc. // OK
   * @see Some javadoc. // OK
   * @since Some javadoc. // OK
   * @serial Some javadoc. // OK
   * @serialField // OK
   * @serialData // OK
   * @deprecated Some javadoc. // OK
   */
  public JSONObject favorite(
      final int id,
      final ArrayList<Video> videos,
      final ArrayList<User> users,
      final int number,
      final String sortType,
      final int year,
      final String genre,
      final Writer writer)
      throws IOException {

    String finalStr = "Query result: ";
    HashMap<String, Double> videosOcc = new HashMap<>();
    Double occ;

    for (Video vid : videos) {
      occ = 0.0;
      for (User us : users) {
        if (us.getFavorite().contains(vid.getTitle())) {
          occ += 1.0;
        }
      }
      if (year == 0 && genre == null) {
        videosOcc.put(vid.getTitle(), occ);
      } else if (vid.getYear() == year && genre == null) {
        videosOcc.put(vid.getTitle(), occ);
      } else if (year == 0 && vid.getGenre().contains(genre)) {
        videosOcc.put(vid.getTitle(), occ);
      } else if (vid.getYear() == year
          && vid.getGenre().contains(genre)
          && Double.compare(occ, 0.0) != 0.0) {
        videosOcc.put(vid.getTitle(), occ);
      }
    }

    List<String> names = SortMap.sortMap(videosOcc, sortType, null, null, number);
    finalStr += names.toString();
    return writer.writeFile(id, null, finalStr);
  }
  /**
   * Some javadoc. // OK
   *
   * @author Some javadoc. // OK
   * @version Some javadoc. // OK
   * @param Some javadoc. // OK
   * @return Some javadoc. // OK
   * @throws Some javadoc. // OK
   * @exception Some javadoc. // OK
   * @see Some javadoc. // OK
   * @since Some javadoc. // OK
   * @serial Some javadoc. // OK
   * @serialField // OK
   * @serialData // OK
   * @deprecated Some javadoc. // OK
   */
  public JSONObject longest(
      final int id,
      final ArrayList<Video> videos,
      final int number,
      final String sortType,
      final int year,
      final String genre,
      final Writer writer)
      throws IOException {

    String finalStr = "Query result: ";
    HashMap<String, Double> videosDuration = new HashMap<>();
    Double dur;

    for (Video v : videos) {
      dur = 0.0;
      dur += v.getDuration();
      if (year == 0 && genre == null) {
        videosDuration.put(v.getTitle(), dur);
      } else if (v.getYear() == year && genre == null) {
        videosDuration.put(v.getTitle(), dur);
      } else if (year == 0 && v.getGenre().contains(genre)) {
        videosDuration.put(v.getTitle(), dur);
      } else if (v.getYear() == year && v.getGenre().contains(genre)) {
        videosDuration.put(v.getTitle(), dur);
      }
    }

    List<String> sortedVids;
    sortedVids = SortMap.sortMap(videosDuration, sortType, null, null, number);
    finalStr += sortedVids.toString();
    return writer.writeFile(id, null, finalStr);
  }
  /**
   * Some javadoc. // OK
   *
   * @author Some javadoc. // OK
   * @version Some javadoc. // OK
   * @param Some javadoc. // OK
   * @return Some javadoc. // OK
   * @throws Some javadoc. // OK
   * @exception Some javadoc. // OK
   * @see Some javadoc. // OK
   * @since Some javadoc. // OK
   * @serial Some javadoc. // OK
   * @serialField // OK
   * @serialData // OK
   * @deprecated Some javadoc. // OK
   */
  public JSONObject mostViewed(
      final int id,
      final ArrayList<Video> videos,
      final ArrayList<User> users,
      final int number,
      final String sortType,
      final int year,
      final String genre,
      final Writer writer)
      throws IOException {
    String finalStr = "Query result: ";
    HashMap<String, Double> viewed = new HashMap<>();

    String title;
    Double count = 0.0;

    for (User us : users) {
      for (Map.Entry<String, Integer> entry : us.getHistory().entrySet()) {
        title = entry.getKey();
        Video vid = getVideo(videos, title);
        count = viewed.get(title);
        Double views = Double.valueOf(entry.getValue());
        if (vid != null) {
          if (count == null) {
            if (year == 0 && genre == null) {
              viewed.put(title, views);
            } else if (vid.getYear() == year && genre == null) {
              viewed.put(title, views);
            } else if (year == 0 && vid.getGenre().contains(genre)) {
              viewed.put(title, views);
            } else if (vid.getYear() == year && vid.getGenre().contains(genre)) {
              viewed.put(title, views);
            }
          } else {
            if (year == 0 && genre == null) {
              viewed.put(title, count + entry.getValue());
            } else if (vid.getYear() == year && genre == null) {
              viewed.put(title, count + entry.getValue());
            } else if (year == 0 && vid.getGenre().contains(genre)) {
              viewed.put(title, count + entry.getValue());
            } else if (vid.getYear() == year && vid.getGenre().contains(genre)) {
              viewed.put(title, count + entry.getValue());
            }
          }
        }
      }
    }
    List<String> vidsSorted;
    vidsSorted = SortMap.sortMap(viewed, sortType, null, null, number);
    finalStr += vidsSorted.toString();
    return writer.writeFile(id, null, finalStr);
  }
  /**
   * Some javadoc. // OK
   *
   * @author Some javadoc. // OK
   * @version Some javadoc. // OK
   * @param Some javadoc. // OK
   * @return Some javadoc. // OK
   * @throws Some javadoc. // OK
   * @exception Some javadoc. // OK
   * @see Some javadoc. // OK
   * @since Some javadoc. // OK
   * @serial Some javadoc. // OK
   * @serialField // OK
   * @serialData // OK
   * @deprecated Some javadoc. // OK
   */
  public Video getVideo(final ArrayList<Video> videos, final String title) {
    for (Video vid : videos) {
      if (vid.getTitle().compareTo(title) == 0) {
        return vid;
      }
    }
    return null;
  }
}
