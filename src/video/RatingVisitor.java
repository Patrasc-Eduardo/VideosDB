package video;

import database.User;
import fileio.Writer;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class RatingVisitor implements VideoVisitor {

  private final User us;
  private final int id;
  private final double grade;
  private final Writer writer;
  private final int seasonNum;

  /**
   Constructorul pentru clasa de visitor.
   Aici sunt setate campurile cu informatiile de care avem nevoie pentru implementarea ratingului.
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
   Metoda ofera un rating pentru filme.
   Se verifica daca filmul nu este deja rated de acel user sau daca filmul exista in istoricul
   userului (si se afiseaza eroarea corespunzatoare).
   Se adauga titlul filmului in lista de videouri rated ale userului.
   Se incrementeaza numarul de ratings date ale userului.
   Se actualizeaza lista cu ratinguri primite ale filmului.
   Se afiseaza successul in JSONObject.

   @param movie Filmul care primeste rating.
   @return JSONObjectul in care se face afisarea.
   @throws IOException Exceptie generata de scrierea in JSONObject.
   */
  public JSONObject visit(final Movie movie) throws IOException {
    String str;
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
   Metoda ofera un rating pentru sezonul unui serial.
   Se verifica daca sezonul serialului nu este deja rated de acel user sau daca filmul exista in
   istoricul userului (si se afiseaza eroarea corespunzatoare).

   Clasa Show contine un map care are drept cheie numele unui user si drept valoare o clasa "MyVec"
   care retine un array ce reprezinta un vector de frecventa pentru sezonele serialului.(Daca un
   user ofera un rating la sezonul 2 -> al doilea element din acest vector este actualizat cu
   valoarea ratingului).

   Daca nu ne aflam in conditiile de eroare, sunt actualizate campurile de rating ale serialului
   (inclusiv mapu-ul mentionat mai sus) si este incrementat numarul de ratinguri oferite ale
   userului.

   @param show Serialul care primeste rating.
   @return JSONObjectul in care se face afisarea.
   @throws IOException Exceptie generata de scrierea in JSONObject.
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
      show.setUsersShowRating(this.us.getUsername(), seasonNum, grade);
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
   Metoda de visit si pentru video care nu are o anumita functionalitate.
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
}
