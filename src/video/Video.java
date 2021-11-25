package video;

import fileio.ShowInput;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Video implements VideoVisitable {

  private String title;
  private int year;
  private ArrayList<String> genre;
  private ArrayList<String> cast;
  private List<Double> ratings;

  public Video() { }

  public Video(final ShowInput in) {
    this.title = in.getTitle();
    this.year = in.getYear();
    this.genre = in.getCast();
    this.cast = in.getCast();
  }

  public Video(
      final String title,
      final int year,
      final ArrayList<String> cast,
      final ArrayList<String> genres) {
    this.title = title;
    this.year = year;
    this.cast = cast;
    this.genre = genres;
  }
  /**
   * Some javadoc. // OK
   */
  public Double getRating() {
    return 0.0;
  }
  /**
   * Some javadoc. // OK
   */
  public int getDuration() {
    return 0;
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
  public List<Double> getRatings() {
    return ratings;
  }
  /**
   * Some javadoc. // OK
   */
  public void setRatings(final List<Double> ratings) {
    this.ratings = ratings;
  }

  /**
   * Some javadoc. // OK
   K
   */
  public String getTitle() {
    return title;
  }
  /**
   * Some javadoc. // OK
   */
  public void setTitle(final String title) {
    this.title = title;
  }
  /**
   * Some javadoc. // OK
   K
   */
  public int getYear() {
    return year;
  }
  /**
   * Some javadoc. // OK
   */
  public void setYear(final int year) {
    this.year = year;
  }
  /**
   * Some javadoc. // OK
   K
   */
  public ArrayList<String> getGenre() {
    return genre;
  }
  /**
   * Some javadoc. // OK
   */
  public void setGenre(final ArrayList<String> genre) {
    this.genre = genre;
  }
  /**
   * Some javadoc. // OK
   */
  public ArrayList<String> getCast() {
    return cast;
  }
  /**
   * Some javadoc. // OK
   */
  public void setCast(final ArrayList<String> cast) {
    this.cast = cast;
  }
}
