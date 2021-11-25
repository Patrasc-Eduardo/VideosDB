package database;

import fileio.UserInputData;
import java.util.ArrayList;
import java.util.Map;


public final class User {
  private final String username;
  private final String subscriptionType;
  private final Map<String, Integer> history;
  private final ArrayList<String> favorite;
  private final ArrayList<String> givenMovieRatings;
  private int numberOfRatingsGiven;

  public User(final UserInputData usIn) {
    this.username = usIn.getUsername();
    this.subscriptionType = usIn.getSubscriptionType();
    this.favorite = usIn.getFavoriteMovies();
    this.history = usIn.getHistory();
    this.givenMovieRatings = new ArrayList<>();
    this.numberOfRatingsGiven = 0;
  }
  /**
   * Some javadoc. // OK
   */
  public int getNumberOfViews(final String title) {
    for (Map.Entry<String, Integer> entry : history.entrySet()) {
      if (entry.getKey().compareTo(title) == 0) {
        return entry.getValue();
      }
    }
    return 0;
  }
  /**
   * Some javadoc. // OK
   */
  public void addMovieRating(final String title) {
    this.givenMovieRatings.add(title);
  }


  public String getUsername() {
    return username;
  }

  public String getSubscriptionType() {
    return subscriptionType;
  }

  public Map<String, Integer> getHistory() {
    return history;
  }

  public ArrayList<String> getFavorite() {
    return favorite;
  }

  public ArrayList<String> getGivenMovieRatings() {
    return givenMovieRatings;
  }

  public int getNumberOfRatingsGiven() {
    return numberOfRatingsGiven;
  }

  public void setNumberOfRatingsGiven(final int numberOfRatingsGiven) {
    this.numberOfRatingsGiven = numberOfRatingsGiven;
  }

  @Override
  public String toString() {
    return "UserInputData{"
        + "username='"
        + username
        + '\''
        + ", subscriptionType='"
        + subscriptionType
        + '\''
        + ", history="
        + history
        + ", favorite="
        + favorite
        + '}';
  }


}
