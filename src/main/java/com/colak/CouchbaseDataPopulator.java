package com.colak;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.manager.collection.CollectionManager;
import com.couchbase.client.java.manager.collection.CollectionSpec;

public class CouchbaseDataPopulator {

    private static final int ITEM_COUNT = 1_000;

    String clusterAddress = "localhost"; // Use the actual address if not running locally
    String username = "admin";
    String password = "password";

    String bucketName = "mybucket";

    public static void main(String[] args) {

        CouchbaseDataPopulator couchbaseDataPopulator = new CouchbaseDataPopulator();
        couchbaseDataPopulator.insertDocuments("items-1", ITEM_COUNT);
        couchbaseDataPopulator.insertDocuments("items-2", ITEM_COUNT);
        couchbaseDataPopulator.insertDocuments("items-3", 101);
    }

    private void insertDocuments(String collectionName, int itemCount) {
        // Connect to the Couchbase cluster
        try (Cluster cluster = Cluster.connect(clusterAddress, username, password)) {
            Bucket bucket = cluster.bucket(bucketName);

            CollectionManager collectionMgr = bucket.collections();
            CollectionSpec spec = CollectionSpec.create(collectionName);
            collectionMgr.createCollection(spec);
            Collection collection = bucket.collection(collectionName);
            for (int i = 0; i < itemCount; i++) {
                String id = collectionName + "-id-" + i;
                JsonObject jsonObject = JsonObject.create().put("value", collectionName + "-value-" + i);
                collection.insert(id, jsonObject);
            }
        }
    }
}
