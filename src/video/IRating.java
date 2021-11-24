package video;

public interface IRating {
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
   void setRating(Double db, String user, int seasonNum);
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
   Double getRating();
}
