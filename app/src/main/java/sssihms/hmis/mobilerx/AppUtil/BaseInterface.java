package sssihms.hmis.mobilerx.AppUtil;

import org.json.simple.JSONObject;
/**
 * Created by mca2 on 8/2/16.
 */
public interface BaseInterface {

    /**
     * This method validates the data need to be sent. If the data is valid then it returns true else false.
     * @return boolean (True if valid, False if not)
     * @author Murali krishna
     */
    boolean validate(Object... objects);

    /**
     * This is overridden  by the child class to create an JsonObject from the data to be send to the server.
     * @return JsonObject
     * @author Murali krishna
     */
    JSONObject makeJsonObject(Object... objects);

    /**
     * This Method takes Object as an parameter, Parses the Object according to the data required by the UI element.
     * Assigns the data to the appropriate UI elements.
     *
     * @param object
     * @retun void
     * @author Murali krishna
     */
    void setParameterValues(JSONObject object, Object... objects);

    /**
     * It impletements an DialogBox to view an error accured.
     */
    void errorReport(String error, String message);

}
