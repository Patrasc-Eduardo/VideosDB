package video;

import org.json.simple.JSONObject;
import java.io.IOException;

public interface VideoVisitable {
    /**
     Metoda de accept ajutatoare pentru patternul "visitor".
     Aceasta este apelata din fiecare subclasa pe care vrem sa o prelucram
     prin clasa de visitor "RatingVisitor"
     */
    JSONObject accept(VideoVisitor v) throws IOException;
}
