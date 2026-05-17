import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class ImageDownloader {
    public static void main(String[] args) throws Exception {
        Files.createDirectories(Paths.get("GroceryClient/resources/images"));

        Map<String, String> files = new HashMap<>();
        files.put("GroceryClient/resources/images/apples.jpg", "https://loremflickr.com/200/200/apple,fruit/all");
        files.put("GroceryClient/resources/images/bananas.jpg", "https://loremflickr.com/200/200/banana,fruit/all");
        files.put("GroceryClient/resources/images/oranges.jpg", "https://loremflickr.com/200/200/orange,fruit/all");
        files.put("GroceryClient/resources/images/tomatoes.jpg", "https://loremflickr.com/200/200/tomato,vegetable/all");
        files.put("GroceryClient/resources/images/potatoes.jpg", "https://loremflickr.com/200/200/potato,vegetable/all");
        files.put("GroceryClient/resources/images/grapes.jpg", "https://loremflickr.com/200/200/grapes,fruit/all");
        files.put("GroceryClient/resources/images/hero.jpg", "https://loremflickr.com/400/300/grocery,market/all");

        for (Map.Entry<String, String> entry : files.entrySet()) {
            System.out.println("Downloading " + entry.getKey() + "...");
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(entry.getValue()).openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setInstanceFollowRedirects(true);
                try (InputStream in = conn.getInputStream()) {
                    Files.copy(in, Paths.get(entry.getKey()), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (Exception e) {
                System.err.println("Failed to download " + entry.getKey() + " from " + entry.getValue());
                e.printStackTrace();
            }
        }
        System.out.println("All images downloaded successfully.");
    }
}
