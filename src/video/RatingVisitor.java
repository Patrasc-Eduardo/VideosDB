package video;

import database.User;
import fileio.Writer;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class RatingVisitor implements VideoVisitor {

  private User us;
  private int id;
  private double grade;
  private Writer writer;
  private int seasonNum;

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
  public RatingVisitor(
      final User us, final int id, final double grade, final Writer writer, final int seasonNum) {
    this.us = us;
    this.id = id;
    this.grade = grade;
    this.writer = writer;
    this.seasonNum = seasonNum;
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
  public JSONObject visit(final Movie movie) throws IOException {
    String str = "";
    String title = movie.getTitle();

    if (us.getGivenMovieRatings() != null && us.getGivenMovieRatings().contains(title)) {
      str = "error -> " + title + " has been already rated";
    } else if (us.getHistory().containsKey(title)) {
      us.addMovieRating(title);
      us.setNumberOfRatingsGiven(us.getNumberOfRatingsGiven() + 1);
      movie.setRating(grade, us.getUsername(), 0);
      str = "success -> " + title + " was rated with " + grade + " by " + us.getUsername();
    } else {
      str = "error -> " + title + " is not seen";
      return writer.writeFile(id, null, str);
    }
    return writer.writeFile(id, null, str);
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
  public JSONObject visit(final Show show) throws IOException {
    String str;

    HashMap<String, Show.MyVec> seasonRating = show.getEachSeasonRating();

    if (seasonRating.size() >= 1) {

      for (Map.Entry<String, Show.MyVec> entry : seasonRating.entrySet()) {
        if (entry.getKey().compareTo(us.getUsername()) == 0) {

          if (entry.getValue().getArr() != null && entry.getValue().getArr()[seasonNum - 1] > 0) {

            str = "error -> " + show.getTitle() + " has been already rated";
            return writer.writeFile(id, null, str);
          }
        }
      }
    }
    if (this.us.getHistory().containsKey(show.getTitle())) {

      show.setRating(grade, null, seasonNum);
      show.setUsersShowRating(show.getTitle(), this.us.getUsername(), seasonNum, grade);
      us.setNumberOfRatingsGiven(us.getNumberOfRatingsGiven() + 1);
      us.addMovieRating(show.getTitle());
      str =
          "success -> " + show.getTitle() + " was rated with " + grade + " by " + us.getUsername();
      return writer.writeFile(id, null, str);
    } else {
      str = "error -> " + show.getTitle() + " is not seen";
    }
    return writer.writeFile(id, null, str);
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
  public JSONObject visit(final Video video) {
    return null;
  }

  public User getUs() {
    return us;
  }

  public int getId() {
    return id;
  }

  public double getGrade() {
    return grade;
  }

  public Writer getWriter() {
    return writer;
  }

  public int getSeasonNum() {
    return seasonNum;
  }
}
