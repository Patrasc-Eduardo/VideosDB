package video;

public interface IRating {
    /**
     * Some javadoc. // OK
     */
   void setRating(Double db, String user, int seasonNum);
    /**
     * Some javadoc. // OK
     */
   Double getRating();
}
