package action;

import common.Constants;
import database.Database;
import database.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import video.RatingVisitor;
import video.Video;
import fileio.Writer;
import video.VideoVisitor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public final class Commands {

  public Commands() { }
  /**
   * Verifica tipul comenzii si
   * executa metoda aferenta.
   *
   * @param mainDatabase Baza de date.
   * @param act Actiunea de unde vom prelua tipul comenzii, id-ul si alte informatii de care
   *            mai avem nevoie
   * @param fileWriter  Obiectul prin care se va face scrierea in JSONObject.
   * @param arrayResult Obiectul in care se stocheaza rezultatul actiunilor.
   * @throws IOException Exceptie generata de scrierea in JSONObject.
   * */
  @SuppressWarnings("unchecked")
  public void init(
      final Database mainDatabase,
      final Action act,
      final Writer fileWriter,
      final JSONArray arrayResult)
      throws IOException {
    String typeOfCommand = act.getActionInp().getType();

    Video vid = mainDatabase.getVideoByTitle(act.getActionInp().getTitle());
    User us = mainDatabase.getUserByName(act.getActionInp().getUsername());

    int id = act.getActionInp().getActionId();
    double grade = act.getActionInp().getGrade();
    int seasonNum = act.getActionInp().getSeasonNumber();

    if (typeOfCommand.compareTo(Constants.VIEW) == 0) {
      assert us != null;
      assert vid != null;
      arrayResult.add(this.view(vid, us, id, fileWriter));
    }
    if (typeOfCommand.compareTo(Constants.FAVORITE) == 0) {
      assert vid != null;
      assert us != null;
      arrayResult.add(this.favorite(vid, us, id, fileWriter));
    }
    if (typeOfCommand.compareTo(Constants.RATING) == 0) {
      assert vid != null;
      arrayResult.add(this.rating(vid, us, id, grade, seasonNum, fileWriter));
    }
  }

  /**
   * Adauga un video in lista de favorite a userului
   *
   * @param vid Videoul pe care vrem sa-l adaugam.
   * @param user Userul in lista caruia vrem sa adaugam.
   * @param id ID-ul actiunii
   * @param writer  Obiectul prin care se va face scrierea in JSONObject.
   * @return Obiectul json care va fi pus in arrayResult din main.
   * @throws IOException Exceptie generata de scrierea in JSONObject. */
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
   * Adauga un video in istoricul de vizionari a userului
   *
   * @param vid Videoul pe care vrem sa-l adaugam.
   * @param user Userul in lista caruia vrem sa adaugam.
   * @param id ID-ul actiunii
   * @param writer  Obiectul prin care se va face scrierea in JSONObject.
   * @return Obiectul json care va fi pus in arrayResult din main.
   * @throws IOException Exceptie generata de scrierea in JSONObject. */
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
   * Dam rate la un video. Deoarece ratingul se face diferit la filme si seriale
   * si deoarece nu stim tipul video-ului la run-time , folosim un "visitor" pattern.
   *
   * @param vid Videoul caruia vrem sa-i dam un rating.
   * @param us Userul care da ratingul.
   * @param id ID-ul actiunii
   * @param grade Ratingul.
   * @param seasonNumb Sezonul la care dam rating daca este cazul unui serial.
   * @param writer  Obiectul prin care se va face scrierea in JSONObject.
   * @return Obiectul json care va fi pus in arrayResult din main.
   * @throws IOException Exceptie generata de scrierea in JSONObject. */
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
