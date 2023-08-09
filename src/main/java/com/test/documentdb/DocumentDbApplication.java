package com.test.documentdb;

import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DocumentDbApplication {

    @Value("${username}")
    private String usernameV;

    @Value("${password}")
    private String passwordV;

    @Value("${truststorePassword}")
    private String truststorePasswordV;

    public static void main(String[] args) {
        // SSLContextHelper.setSslProperties();
        String template = "mongodb://%s:%s@%s/Chatting?tls=true&replicaSet=rs0&readpreference=%s";
        String username = usernameV;
        String password = passwordV;
        String clusterEndpoint = "chatting.cluster-caftzpxwupgi.ap-northeast-2.docdb.amazonaws.com:27017";
        String readPreference = "secondaryPreferred";
        String connectionString = String.format(template, username, password, clusterEndpoint, readPreference);

        String truststore = "/home/ubuntu/documentDB/src/main/resources/rds-truststore.jks";
        String truststorePassword = truststorePasswordV;

        // FileInputStream jksInputStream = new FileInputStream(truststore);
        // KeyStore jksKeyStore = KeyStore.getInstance("JKS");
        // jksKeyStore.load(jksInputStream, jksPassword);
        // jksInputStream.close();

        // TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        // trustManagerFactory.init(jksKeyStore);

        // KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

        // SSLContext sslContext = SSLContext.getInstance("TLS");
        // sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        // MongoClientSettings settings = MongoClientSettings.builder()
        //         .applyConnectionString(new ConnectionString(connectionString))
        //         .applyToSslSettings(builder ->
        //                 builder.enabled(true)
        //                         .context(sslContext))
        //         .build();

        // MongoClient mongoClient = MongoClients.create(settings);
        System.setProperty("javax.net.ssl.trustStore", truststore);
        System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);

        MongoClient mongoClient = MongoClients.create(connectionString);

        MongoDatabase testDB = mongoClient.getDatabase("Chatting");
        MongoCollection<Document> numbersCollection = testDB.getCollection("Chatroom");

        Document doc = new Document("name", "pi").append("value", 3.14159);
        numbersCollection.insertOne(doc);

        MongoCursor<Document> cursor = numbersCollection.find().iterator();
        while (cursor.hasNext()) {
            System.out.println(cursor.next().toJson());
        }

        // SpringApplication.run(DocumentDbApplication.class, args);
    }
}
