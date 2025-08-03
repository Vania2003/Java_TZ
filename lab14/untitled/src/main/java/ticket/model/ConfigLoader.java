package ticket.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ConfigLoader {
    public static void loadConfig(String filename) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File(filename));

            TicketQueueManager manager = TicketQueueManager.getInstance();

            JsonNode categories = root.get("categories");
            if (categories != null && categories.isArray()) {
                for (JsonNode cat : categories) {
                    String name = cat.get("name").asText();
                    int priority = cat.get("priority").asInt();
                    manager.addCategory(name, priority);
                }
            }

            JsonNode desks = root.get("desks");
            if (desks != null && desks.isObject()) {
                Iterator<String> fieldNames = desks.fieldNames();
                while (fieldNames.hasNext()) {
                    String deskIdStr = fieldNames.next();
                    int deskId = Integer.parseInt(deskIdStr);
                    JsonNode catArray = desks.get(deskIdStr);
                    for (JsonNode catName : catArray) {
                        manager.assignCategoryToDesk(deskId, catName.asText());
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Błąd wczytywania config.json: " + e.getMessage());
        }
    }
}
