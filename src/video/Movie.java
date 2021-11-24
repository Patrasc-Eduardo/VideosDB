package video;

import fileio.MovieInputData;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Movie extends Video implements IRating {
  private final int duration;
  private ArrayList<Double> ratings;
  private Map<String, Double> userRatings;

  public Movie(final MovieInputData mv) {
    super(mv.getTitle(), mv.getYear(), mv.getCast(), mv.getGenres());
    this.duration = mv.getDuration();
    this.ratings = new ArrayList<>();
    this.userRatings = new HashMap<>();
  }

  public int getDuration() {
    return duration;
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
  public JSONObject accept(final VideoVisitor v) throws IOException {
    return v.visit(this);
  }

  @Override
  public Double getRating() {
    Double sum = 0.0;
    Double num = 0.0;
    for (Double d : this.ratings) {
      if (this.ratings.size() == 1) {
        return d;
      }
      sum += d;
      num++;
    }
    if (Double.compare(sum, 0.0) == 0) {
      return 0.0;
    }
    return sum / num;
  }

  @Override
  public void setRating(final Double db, final String user, final int seasonNum) {
    System.out.println("DAT rate si user -> " + user + " si movie -> " + this.getTitle());
    this.ratings.add(db);
    this.userRatings.put(user, db);
  }

  @Override
  public String toString() {
    return "title= "
        + super.getTitle()
        + "year= "
        + super.getYear()
        + "duration= "
        + duration
        + "cast {"
        + super.getCast()
        + " }\n"
        + "genres {"
        + super.getGenre()
        + " }\n ";
  }
}
