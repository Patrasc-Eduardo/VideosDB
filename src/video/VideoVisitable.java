package video;

import org.json.simple.JSONObject;
import java.io.IOException;

public interface VideoVisitable {
    /**
     * Some javadoc. // OK
     *
     * @author Some javadoc. // OK
     * @serialData // OK
     */
    JSONObject accept(VideoVisitor v) throws IOException;
}
