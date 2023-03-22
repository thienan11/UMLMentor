import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * CSC 309 UML Tutor
 * Class to generate problem for professor using GPT 3.5 Turbo API
 * 
 * @author Mario Vuletic
 */
public class ProblemGenerator {

    /**
     * Generates a problem from ChatGPT API
     * @return The string of the problem generated
     * @throws Exception
     */
    public String generateProblem() throws Exception {
        String prob = "";
        URL url = new URL("https://api.openai.com/v1/chat/completions");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        JSONObject obj = new JSONObject();
        obj.put("model", "gpt-3.5-turbo-0301");
        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", "give me something similar to the following, class names should be any one capital letter in the alphabet, don't include primitive types for variables and give me exactly 1-10 java classess: class J { K kVar; void methodJ() { M mvar; } } class K extends J { L lVar; M mVar; void methodK() { } } class L extends K { void methodL() { } } class M { K kVar; void methodM() { L lvar; } }");
        messages.put(message);

        obj.put("messages", messages);

        String jsonBody = obj.toString();
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(jsonBody);
        wr.close();
        wr.flush();

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        prob = content.toString();
        JSONObject json = new JSONObject(prob);
        Map<String, Object> jsonMap = json.toMap();
        ArrayList<Object> arr = (ArrayList<Object>) jsonMap.get("choices");
        HashMap<String, Object> indexed = (HashMap<String, Object>) arr.get(0);
        HashMap<String, Object> messageMap = (HashMap<String, Object>) indexed.get("message");
        String code = (String) messageMap.get("content");
        code = code.replace("`", "");
        Blackboard.getInstance().setClasses((new Parser()).parse(code));
        return prob;
    }
}
