import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class InstituteDBOldVersion {
    public static void main(String[] args) {
        try {
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            System.out.println("✅ Connected to MongoDB server.");

            MongoDatabase database = mongoClient.getDatabase("InstituteDB");
            System.out.println("✅ Database ready: " + database.getName());

            MongoCollection<Document> collection = database.getCollection("Students");
            System.out.println("✅ Collection ready: " + collection.getNamespace());

            Document student = new Document("roll_no", 1)
                    .append("name", "Roshan")
                    .append("course", "Computer Engineering")
                    .append("year", "Second Year");

            collection.insertOne(student);
            System.out.println("✅ Document inserted!");

            System.out.println("\n📘 Student Records:");
            for (Document doc : collection.find()) {
                System.out.println(doc.toJson());
            }

            collection.updateOne(
                    new Document("roll_no", 1),
                    new Document("$set", new Document("year", "Third Year"))
            );
            System.out.println("\n✅ Document updated!");

            collection.deleteOne(new Document("roll_no", 1));
            System.out.println("\n🗑️ Document deleted!");

            mongoClient.close();
            System.out.println("\n🔒 Connection closed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

