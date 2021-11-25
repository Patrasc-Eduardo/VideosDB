package action;

import database.User;
import org.json.simple.JSONObject;
import video.RatingVisitor;
import video.Video;
import fileio.Writer;
import video.VideoVisitor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public final class Commands extends Action {

  public Commands() { }
  /**
   * Some javadoc. // OK
   */
  public JSONObject favorite(final Video vid, final User user, final int id, final Writer writer)
      throws IOException {

    ArrayList<String> favList = user.getFavorite();
    String str = "";
    String vidTitle = vid.getTitle();

    for (String fav : favList) {
      if (fav.compareTo(vidTitle) == 0) {
        str += "error -> " + vidTitle + " is already in favourite list";
        return writer.writeFile(id, null, str);
      }
    }

    if (user.getNumberOfViews(vidTitle) < 1) {
      str += "error -> " + vidTitle + " is not seen";
      return writer.writeFile(id, null, str);
    }

    user.getFavorite().add(vidTitle);
    str += "success -> " + vidTitle + " was added as favourite";
    return writer.writeFile(id, null, str);
  }

  /**
   * Some javadoc. // OK
   */
  public JSONObject view(final Video vid, final User user, final int id, final Writer writer)
      throws IOException {

    Map<String, Integer> map = user.getHistory();
    String str;

    if (map.containsKey(vid.getTitle())) {
      map.put(vid.getTitle(), map.get(vid.getTitle()) + 1);
    } else {
      map.put(vid.getTitle(), 1);
    }

    str =
        "success -> "
            + vid.getTitle()
            + " was viewed with total views of "
            + user.getNumberOfViews(vid.getTitle());

    return writer.writeFile(id, null, str);
  }
  /**
   * Some javadoc. // OK
   */
  public JSONObject rating(
      final Video vid,
      final User us,
      final int id,
      final double grade,
      final int seasonNumb,
      final Writer writer)
      throws IOException {

    JSONObject obj;

    VideoVisitor v = new RatingVisitor(us, id, grade, writer, seasonNumb);
    obj = vid.accept(v);
    return obj;
  }
}
