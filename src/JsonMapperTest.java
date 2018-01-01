import java.util.HashMap;

public class JsonMapperTest {

    public static void main(String[] args) {
        JsonMapper parser = new JsonMapper("{\"id\": 12, \"name\": \"Doe, John\", \"points\": 96.5 , \"married\": false}");
        HashMap map = (HashMap) parser.parse();
        System.out.println("map : " + map + ", size : " + map.size());
    }

}
