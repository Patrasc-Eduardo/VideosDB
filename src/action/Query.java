package action;

import actor.Actor;
import common.Constants;
import database.Database;
import database.User;
import fileio.Writer;
import org.json.simple.JSONArray;
import video.Video;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clasa de query care are ca subclase "ActorQuery", "VideoQuery" si "UserQuery"
 *
  */
public class Query {
  private Database db;

  public Query(final Database db) {
    this.db = db;
  }
  /** default constructor */
  public Query() { }
  /**
   * Verifica tipul query-ului si
   * executa metoda aferenta.
   *
   * @param mainDatabase Baza de date.
   * @param act Actiunea de unde vom prelua tipul query-uli, id-ul si alte informatii de care
   *            mai avem nevoie
   * @param fileWriter  Obiectul prin care se va face scrierea in JSONObject.
   * @param arrayResult Obiectul in care se stocheaza rezultatul actiunilor.
   * @throws IOException Exceptie generata de scrierea in JSONObject.
   * */
  @SuppressWarnings("unchecked")
  public void init(final Database mainDatabase, final Action act, final Writer fileWriter,
                   final JSONArray arrayResult)
          throws IOException {

    String typeOf = act.getActionInp().getObjectType();
    String typeOfCriteria = act.getActionInp().getCriteria();
    int id = act.getActionInp().getActionId();
    int queryNumber = act.getActionInp().getNumber();
    String sortType = act.getActionInp().getSortType();

    switch (typeOf) {
      case Constants.ACTORS -> {
        ActorQuery aquery = new ActorQuery(mainDatabase);
        ArrayList<Actor> actorsList = mainDatabase.getActorsList();
        if (typeOfCriteria.compareTo(Constants.AVERAGE) == 0) {
          arrayResult.add(aquery.average(id, actorsList, queryNumber, sortType, fileWriter));
        }
        if (typeOfCriteria.compareTo(Constants.AWARDS) == 0) {
          List<String> awards = act.getActionInp().getFilters().
                  get(Constants.AWARDS_POSITION);

          arrayResult.add(aquery.awards(id, actorsList, sortType, fileWriter,
                  awards));

        }
        if (typeOfCriteria.compareTo(Constants.FILTER_DESCRIPTIONS) == 0) {
          List<String> words = act.getActionInp().getFilters().
                  get(Constants.WORDS_POSITION);
          arrayResult.add(aquery.filterDescription(id, actorsList, sortType, fileWriter,
                  words));
        }
      }
      case Constants.MOVIES, Constants.SHOWS  -> {
        VideoQuery vquery = new VideoQuery(mainDatabase);
        ArrayList<Video> videos = new ArrayList<>();
        if (typeOf.compareTo(Constants.MOVIES) == 0) {
          videos.addAll(mainDatabase.getMoviesList());
        } else {
          videos.addAll(mainDatabase.getShowsList());
        }
        List<String> yearList = act.getActionInp().getFilters().
                get(Constants.YEAR_POSITION);
        List<String> genreList = act.getActionInp().getFilters().
                get(Constants.GENRE_POSITION);
        int year = 0;
        if (yearList.get(0) != null) {
          year = Integer.parseInt(yearList.get(0));
        }
        String genre = genreList.get(0);
        if (typeOfCriteria.compareTo(Constants.RATINGS) == 0) {
          arrayResult.add(vquery.rating(id, videos, queryNumber, sortType,
                  year, genre, fileWriter));
        }
        if (typeOfCriteria.compareTo(Constants.FAVORITE) == 0) {
          ArrayList<User> users = mainDatabase.getUsersList();
          arrayResult.add(vquery.favorite(id, videos, users, queryNumber, sortType,
                  year, genre, fileWriter));
        }
        if (typeOfCriteria.compareTo(Constants.LONGEST) == 0) {

          arrayResult.add(vquery.longest(id, videos, queryNumber, sortType,
                  year, genre, fileWriter));

        }
        if (typeOfCriteria.compareTo(Constants.MOST_VIEWED) == 0) {
          ArrayList<User> users = mainDatabase.getUsersList();
          arrayResult.add(vquery.mostViewed(id, videos, users, queryNumber, sortType,
                  year, genre, fileWriter));
        }
      }
      case Constants.USERS -> {
        UserQuery uquery = new UserQuery(mainDatabase);
        if (typeOfCriteria.compareTo(Constants.NUMBER_OF_RATINGS) == 0) {
          ArrayList<User> users = mainDatabase.getUsersList();
          arrayResult.add(uquery.numRatings(id, users, queryNumber, sortType, fileWriter));

        }
      }
      default -> System.out.println("No type found!");
    }
  }
  /** Getter ce returneaza baza de date */
  public Database getDb() {
    return db;
  }
}
