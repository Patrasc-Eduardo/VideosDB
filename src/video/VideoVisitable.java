package video;

import org.json.simple.JSONObject;
import java.io.IOException;

public interface VideoVisitable {
    /**
     * Some javadoc. // OK
     *
     * @author Some javadoc. // OK
     * @param Some javadoc. // OK
     * @return Some javadoc. // OK
     * @throws Some javadoc. // OK
     * @exception Some javadoc. // OK
     * @see Some javadoc. // OK
     * @since Some javadoc. // OK
     * @serialData // OK
     */
    JSONObject accept(VideoVisitor v) throws IOException;
}
