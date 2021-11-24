package database;

import action.Action;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import video.Movie;
import video.Show;
import video.Video;
import actor.Actor;
import java.util.ArrayList;
import java.util.HashMap;

public final class Database {

  private final ArrayList<Actor> actorsList;
  private final ArrayList<User> usersList;
  private final ArrayList<Movie> moviesList;
  private final ArrayList<Show> showsList;
  private final ArrayList<Action> actionsList;

  public Database(final Input in) {

    this.moviesList = new ArrayList<>();
    this.actorsList = new ArrayList<>();
    this.usersList = new ArrayList<>();
    this.showsList = new ArrayList<>();
    this.actionsList = new ArrayList<>();

    for (MovieInputData mov : in.getMovies()) {
      this.moviesList.add(new Movie(mov));
    }

    for (ActorInputData ac : in.getActors()) {
      this.actorsList.add(new Actor(ac));
    }

    for (UserInputData ui : in.getUsers()) {
      this.usersList.add(new User(ui));
    }

    for (SerialInputData si : in.getSerials()) {
      this.showsList.add(new Show(si));
    }

    for (ActionInputData ai : in.getCommands()) {
      this.actionsList.add(new Action(ai));
    }
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
  public User getUserByName(final String name) {
    for (User us : usersList) {
      if (us.getUsername().compareTo(name) == 0) {
        return us;
      }
    }
    return null;
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
  public Video getVideoByTitle(final String title) {
    for (Movie vid : moviesList) {
      if (vid.getTitle().compareTo(title) == 0) {
        return vid;
      }
    }

    for (Show show : showsList) {
      if (show.getTitle().compareTo(title) == 0) {
        return show;
      }
    }

    return null;
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
  public HashMap<String, Double> getVideosByRating(final ArrayList<Video> vids) {
    HashMap<String, Double> vidMap = new HashMap<>();
    for (Video vid : vids) {
      vidMap.put(vid.getTitle(), vid.getRating());
    }
    return vidMap;
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
  public HashMap<String, Double> getVideosAndRatings(final String type) {
    HashMap<String, Double> map = new HashMap<>();

    if (type.compareTo("movies") == 0) {
      for (Movie mv : moviesList) {
        map.put(mv.getTitle(), mv.getRating());
      }
    }
    if (type.compareTo("shows") == 0) {
      for (Show show : showsList) {
        map.put(show.getTitle(), show.getRating());
      }
    }

    return map;
  }

  public ArrayList<Actor> getActorsList() {
    return actorsList;
  }

  public ArrayList<User> getUsersList() {
    return usersList;
  }

  public ArrayList<Movie> getMoviesList() {
    return moviesList;
  }

  public ArrayList<Show> getShowsList() {
    return showsList;
  }

  public ArrayList<Action> getActionsList() {
    return actionsList;
  }
}
