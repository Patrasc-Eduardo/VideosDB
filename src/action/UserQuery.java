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
   Intoarce primii N utilizatori sortați după numărul de ratings pe care le-au dat.
   Pentru fiecare user apelam metoda getNumberOfRatingsGiven() si stocam valoarea intr-un
   map. Sortam map-ul si intoarcem primii N utilizatori.

   @param id ID-ul actiunii.
   @param users Lista cu toti userii.
   @param number Numarul de utilizatori din query-ul rezultat.
   @param sortType Tipul de sortare dorit.
   @param writer Obiectul prin care se face afisare in JSONObject.
   @return Obiectul json care va fi pus in arrayResult din main.
   @throws IOException Exceptie generata de scrierea in JSONObject.
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
