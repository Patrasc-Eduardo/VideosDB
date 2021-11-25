package video;

import org.json.simple.JSONObject;
import java.io.IOException;

public interface VideoVisitor {
  /**
   * Some javadoc. // OK
   *
   * @author Some javadoc. // OK
   * @serialData // OK
   */
  JSONObject visit(Video video) throws IOException;
  /**
   * Some javadoc. // OK
   *
   * @author Some javadoc. // OK
   * @serialData // OK
   */
  JSONObject visit(Show show) throws IOException;
  /**
   * Some javadoc. // OK
   *
   * @author Some javadoc. // OK
   * @serialData // OK
   */
  JSONObject visit(Movie movie) throws IOException;
}
