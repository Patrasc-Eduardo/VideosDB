package video;

public interface IRating {
    /**
     Metoda comuna pentru filme si seriale prin care este setat un rating in lista de ratinguri
     @param db Nota de rating.
     @param user Userul care ofera rating.
     @param seasonNum Numarul sezonului pe care se da rating (daca este serial);
     */
   void setRating(Double db, String user, int seasonNum);
    /**
     Intorace ratingul unui video.
     Metoda este implementat diferit la filme fata de seriale.
     */
   Double getRating();
}
