package video;

import entertainment.Season;
import fileio.SerialInputData;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class Show extends Video implements IRating {

  private final int numberOfSeasons;
  private final ArrayList<Season> seasons;
  private final HashMap<String, MyVec> seasonRating;

  /**
   * Some javadoc. // OK
   */
  public Show(final SerialInputData sh) {
    super(sh.getTitle(), sh.getYear(), sh.getCast(), sh.getGenres());
    this.numberOfSeasons = sh.getNumberSeason();
    this.seasons = sh.getSeasons();
    seasonRating = new HashMap<>();
  }

  /**
   * Some javadoc. // OK
   */
  public JSONObject accept(final VideoVisitor v) throws IOException {
    return v.visit(this);
  }

  /**
   * Some javadoc. // OK
   */
  public Double getSeasonRating(final Season season) {
    Double sum = 0.0;
    Double num = 0.0;
    for (Double d : season.getRatings()) {

      sum += d;
      if (Double.compare(d, 0.0) != 0) {
        num++;
      }
    }

    if (Double.compare(sum, 0.0) != 0) {
      return sum / num;
    } else {
      return 0.0;
    }
  }

  /**
   * Some javadoc. // OK
   */
  @Override
  public void setRating(final Double db, final String user, final int seasonNum) {
    int it = 1;

    for (Season s : this.getSeasons()) {

      if (seasonNum == it) {

        s.getRatings().add(db);

      } else {
        s.getRatings().add(0.0);
      }
      it++;
    }
  }

  /**
   * Some javadoc. // OK
   */
  @Override
  public Double getRating() {
    double sum = 0.0;

    for (Season s : this.seasons) {
      sum += getSeasonRating(s);
    }
    if (Double.compare(sum, 0.0) != 0) {
      return sum / this.numberOfSeasons;
    } else {
      return 0.0;
    }
  }

  /**
   * Some javadoc. // OK
   */
  public void setUsersShowRating(
      final String username, final int seasonRated, final double grade) {

    if (this.seasonRating.containsKey(username)) {
      for (Map.Entry<String, MyVec> entry : seasonRating.entrySet()) {
        if (entry.getKey().compareTo(username) == 0) {
          entry.getValue().arr[seasonRated - 1] = grade;
        }
      }
    } else {
      seasonRating.put(username, new MyVec(this.numberOfSeasons, seasonRated, grade));
    }
  }

  /**
   * Some javadoc. // OK
   */
  public int getDuration() {
    int finalDur = 0;
    for (Season s : seasons) {
      finalDur += s.getDuration();
    }
    return finalDur;
  }

  public HashMap<String, MyVec> getEachSeasonRating() {
    return seasonRating;
  }

  public ArrayList<Season> getSeasons() {
    return seasons;
  }

  @Override
  public String toString() {
    return "SerialInputData{"
        + " title= "
        + super.getTitle()
        + " "
        + " year= "
        + super.getYear()
        + " cast {"
        + super.getCast()
        + " }\n"
        + " genres {"
        + super.getGenre()
        + " }\n "
        + " numberSeason= "
        + numberOfSeasons
        + ", seasons="
        + seasons
        + "\n\n"
        + '}';
  }

  static final class MyVec {
    private final Double[] arr;
    private final Double grade;

      MyVec(final int size, final int numOfSeason, final double grade) {

      arr = new Double[size];
      this.grade = grade;
      Arrays.fill(arr, 0.0);
      if (numOfSeason != 0) {
        arr[numOfSeason - 1] = grade;
      }
    }

    public Double[] getArr() {
      return arr;
    }

    public Double getGrade() {
      return grade;
    }

  }
}
