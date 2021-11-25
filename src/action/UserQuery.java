package action;

import database.Database;
import database.User;
import fileio.Writer;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserQuery extends Query implements SortMap {
  public UserQuery(final Database db) {
    super(db);
  }
  /**
   * Some javadoc. // OK
   */
  public JSONObject numRatings(
      final int id,
      final ArrayList<User> users,
      final int number,
      final String sortType,
      final Writer writer)
      throws IOException {

    String finalStr = "Query result: ";
    HashMap<String, Double> usersNrRat = new HashMap<>();

    for (User us : users) {
      double rating = us.getNumberOfRatingsGiven();
      if (Double.compare(rating, 0.0) != 0) {
        usersNrRat.put(us.getUsername(), rating);
      }
    }

    List<String> usersSorted;
    usersSorted = SortMap.sortMap(usersNrRat, sortType, null, null, number);
    finalStr += usersSorted.toString();
    return writer.writeFile(id, null, finalStr);
  }
}
