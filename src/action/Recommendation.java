package action;

import common.Constants;
import database.Database;
import database.User;
import entertainment.Genre;
import fileio.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.Utils;
import video.Video;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Recommendation implements SortMap {

  public Recommendation() { }
  /**
   * Verifica tipul recomandarii si
   * executa metoda aferenta.
   *
   * @param mainDatabase Baza de date.
   * @param act Actiunea de unde vom prelua tipul comenzii, id-ul si ce alte informatii mai avem
   *            nevoie
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
    User us = mainDatabase.getUserByName(act.getActionInp().getUsername());
    int id = act.getActionInp().getActionId();
    ArrayList<Video> allVids = new ArrayList<>();
    allVids.addAll(mainDatabase.getMoviesList());
    allVids.addAll(mainDatabase.getShowsList());
    String type = act.getActionInp().getType();

    if (type.compareTo(Constants.STANDARD) == 0) {
      assert us != null;
      arrayResult.add(this.standard(id, allVids, us, fileWriter));
      return;
    }

    if (type.compareTo(Constants.BEST_UNSEEN) == 0) {

      assert us != null;
      arrayResult.add(this.bestUnseen(id, allVids, us, fileWriter));
      return;
    }

    assert us != null;
    if (us.getSubscriptionType().compareTo(Constants.PREMIUM) == 0) {

      ArrayList<User> allUsers = mainDatabase.getUsersList();

      if (type.compareTo(Constants.POPULAR) == 0) {
        arrayResult.add(this.popular(id, allVids, us, fileWriter));
        return;
      }
      if (type.compareTo(Constants.FAVORITE) == 0) {
        arrayResult.add(this.favorite(id, allVids, us, allUsers, fileWriter));
        return;
      }
      if (type.compareTo(Constants.SEARCH) == 0) {
        String genre = act.getActionInp().getGenre();
        arrayResult.add(this.search(id, allVids, us, genre, fileWriter));
        return;
      }
    }

    arrayResult.add(fileWriter.writeFile(id, null, "PopularRecommendation cannot be applied!"));
  }
  /** Recomandarea care intoarce primul video din baza de date
   *  nevizionat de user. Se verifica mai intai daca userul are videoclipuri
   *  nevizionate, apoi luam videoruile pe rand si daca nu exista in istoricul
   *  utilizatorului, il afisam ca rezultat al recomandarii.
   *
   *  @param id ID-ul actiunii.
   *  @param allVids Toate videoclipurile din baza de date (filme si seriale in ordinea lor naturala
   *                  de aparitie).
   * @param user Userul pentru care se face recomandarea.
   * @param writer Obiectul prin care se face afisare in JSONObject.
   * @throws IOException Exceptie generata de scrierea in JSONObject.
   * */
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
  /** Recomandarea care intoarce primul video din baza de date
   *  nevizionat de user(cel mai bun, dupa rating). Se verifica mai intai
   *  daca userul are videoclipuri nevizionate, apoi luam videoruile pe rand
   *  si daca nu exista in istoricul userului, le punem intr-un map drept cheie
   *  si ratingul lor drept valoare. Sortam dictionarul descrescator si intoarcem
   *  primul videoclip care va fi cel mai bun.
   *
   *  @param id ID-ul actiunii.
   *  @param allVids Toate videoclipurile din baza de date (filme si seriale in ordinea lor naturala
   *                  de aparitie).
   * @param user Userul pentru care se face recomandarea.
   * @param writer Obiectul prin care se face afisare in JSONObject.
   * @throws IOException Exceptie generata de scrierea in JSONObject.
   * */
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
  /** Recomandarea care intoarce primul video din baza de date
   *  nevizionat de user(din cel mai popular gen).
   *  Parcurgem lista de genuri, iar pentru fiecare gen parcurgem
   *  de fiecare data lista de videouri.
   *  La fiecare iteratie, daca videoul contine respectivul gen, incrementam
   *  intr-un map numarul de aparatii al genului (popularitatea acestuia) si
   *  adaugam intr-un alt map video-ul drept valoare (cheia fiind genul).
   *
   *  Sortam Map-ul de genuri descrescator si il parcurgem treptat. Daca pentru un gen
   *  gasim un videoclip care nu este vizualizat (aici intervine al doilea map) , atunci
   *  acesta va fi rezultatul recomandarii.
   *
   *  @param id ID-ul actiunii.
   *  @param allVids Toate videoclipurile din baza de date (filme si seriale in ordinea lor naturala
   *                  de aparitie).
   * @param user Userul pentru care se face recomandarea.
   * @param writer Obiectul prin care se face afisare in JSONObject.
   * @return Obiectul json care va fi pus in arrayResult din main.
   * @throws IOException Exceptie generata de scrierea in JSONObject.
   * */
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
  /** Recomandarea care intoarce videclipul cel mai des intalnit in listele de favorite ale
   * utilizatorilor.
   * Parcurgem toate videoclipurile din database si le cautam in listele de favorite ale
   * tuturor utilizatorilor. Daca le gasim, le adadaugam intr-un map cu numele lor drept cheie
   * si numarul de aparitii drept valoare.
   * Sortam map-ul drescrescator si intoarcem cel mai bun video.
   *
   *  @param id ID-ul actiunii.
   *  @param allVids Toate videoclipurile din baza de date (filme si seriale in ordinea lor naturala
   *                  de aparitie).
   * @param user Userul pentru care se face recomandarea.
   * @param allUsers Toti utilizatorii din baza de date.
   * @param writer Obiectul prin care se face afisare in JSONObject.
   * @return Obiectul json care va fi pus in arrayResult din main.
   * @throws IOException Exceptie generata de scrierea in JSONObject.
   * */
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
  /** Recomandarea care intoarce toate videoclipurile dintr-un anumit gen, nevazute de user.
   *  Parcugem lista totala de videoclipuri si , pentru fiecare video, daca contine genul cerute si
   *  daca nu se afla in istoricul de vizionari ale userului, punem numele videoului intr-un map
   *  avand drept valoare ratingul.
   *  Sortam descrescator map-ul si afisam lista de videouri.
   *
   *  @param id ID-ul actiunii.
   *  @param allVids Toate videoclipurile din baza de date (filme si seriale in ordinea lor naturala
   *                  de aparitie).
   * @param user Userul pentru care se face recomandarea.
   * @param genre Genul de film cerut.
   * @param writer Obiectul prin care se face afisare in JSONObject.
   * @return Obiectul json care va fi pus in arrayResult din main.
   * @throws IOException Exceptie generata de scrierea in JSONObject.
   * */
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
