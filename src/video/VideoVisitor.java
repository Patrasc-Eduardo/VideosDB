package video;

import org.json.simple.JSONObject;
import java.io.IOException;

public interface VideoVisitor {
  /**
   Metoda de visit ajutatoare pentru patternul "visitor".
   Aceasta permite "vizitarea" unui Video.
   */
  JSONObject visit(Video video) throws IOException;
  /**
   Metoda de visit ajutatoare pentru patternul "visitor".
   Aceasta permite "vizitarea" unui serial.
   */
  JSONObject visit(Show show) throws IOException;
  /**
   Metoda de visit ajutatoare pentru patternul "visitor".
   Aceasta permite "vizitarea" unui film.
   */
  JSONObject visit(Movie movie) throws IOException;
}
