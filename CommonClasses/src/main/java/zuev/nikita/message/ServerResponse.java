package zuev.nikita.message;

import zuev.nikita.structure.Organization;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

/**
 * Client receives an object of this class from the server
 */
public class ServerResponse implements Serializable {
    private final String response;
    private final int statusCode;
    private final Map<String, Organization> collection;
    public static final int OK = 0;
    public static final int NEW_USER = 1;
    public static final int WRONG_PASSWORD=2;
    public static final int WRONG_COMMAND = 3;

    public static final int OLD_USER = 4;






    public ServerResponse(String response, int statusCode, Map<String, Organization> collection) {
        this.response = response;
        this.statusCode = statusCode;
        this.collection = collection;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponse() {
        return response;
    }
    public Map<String, Organization> getCollection() {
        return collection;
    }
}
