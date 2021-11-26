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
   Aceasta metoda este necesara pentru overriding din subclasele de Movie si Show.
   Chiar daca metoda nu are o functionalitate aparte, ea este implementata deoarece
   de-a lungul programului nu stim daca videoul pe care vrem sa facem prelucrare este
   Movie sau Show.
   */
  public Double getRating() {
    return 0.0;
  }
  /**
   Aceasta metoda este necesara pentru overriding din subclasele de Movie si Show.
   Chiar daca metoda nu are o functionalitate aparte, ea este implementata deoarece
   de-a lungul programului nu stim daca videoul pe care vrem sa facem prelucrare este
   Movie sau Show.
   */
  public int getDuration() {
    return 0;
  }

  /**
   Metoda de accept pentru patternul "visitor"
   @param v Clasa de visitor.
   @return JSONObjectul in care facem afisarea.
   */
  public JSONObject accept(final VideoVisitor v) throws IOException {
    return v.visit(this);
  }
  /**
   Intoarce lista de ratings ale videoului. Aceasta nu va fi apelata niciodata la run-time
   ci doar la compile-time. La run-time getRatings() se apeleaza doar pentru clasa Movie sau Show,
   in functie de caz.
   */
  public List<Double> getRatings() {
    return ratings;
  }
  /**
   Seteaza lista de ratings ale videoului.
   */
  public void setRatings(final List<Double> ratings) {
    this.ratings = ratings;
  }

  /**
   Intoarce numele videoului.
   */
  public String getTitle() {
    return title;
  }
  /**
   Seteaza numele videoului.
   */
  public void setTitle(final String title) {
    this.title = title;
  }
  /**
   Intoarce anul videoului.
   */
  public int getYear() {
    return year;
  }
  /**
   Seteaza anului videoului.
   */
  public void setYear(final int year) {
    this.year = year;
  }
  /**
   Intoarce lista de genuri ale videoului.
   */
  public ArrayList<String> getGenre() {
    return genre;
  }
   /**
   Seteaza lista de genuri ale videoului.
   */
  public void setGenre(final ArrayList<String> genre) {
    this.genre = genre;
  }
  /**
   Intoarce castul unui video.
   */
  public ArrayList<String> getCast() {
    return cast;
  }
  /**
   Seteaza castul unui video.
   */
  public void setCast(final ArrayList<String> cast) {
    this.cast = cast;
  }
}
