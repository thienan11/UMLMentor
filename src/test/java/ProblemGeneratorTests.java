import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.util.Map;
import java.util.ArrayList;

public class ProblemGeneratorTests {

    @Test
    public void testgenerateProblem() {
        ProblemGenerator pg = new ProblemGenerator();
        String prob = "";
        try{
            prob = pg.generateProblem();
        } catch (Exception e) { throw new AssertionError(); }
        JSONObject json = new JSONObject(prob);
        Map<String, Object> jsonMap = json.toMap();
        ArrayList<Object> arr = (ArrayList<Object>) jsonMap.get("choices");
        HashMap<String, Object> indexed = (HashMap<String, Object>) arr.get(0);
        HashMap<String, Object> message = (HashMap<String, Object>) indexed.get("message");
        String role = (String) message.get("role");
        String code = (String) message.get("content");
        Assertions.assertEquals("assistant", role);
    }
}