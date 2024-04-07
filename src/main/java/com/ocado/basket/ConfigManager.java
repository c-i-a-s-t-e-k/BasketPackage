package com.ocado.basket;

import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// parse config.json file and archive information about Products and Shipping method
public class ConfigManager {
    private final List<String> products = new ArrayList<>();
    private final Map<String, Integer> productsMap = new HashMap<>();
    private final List<String> delivery = new ArrayList<>();
    private final Map<Integer, Set<Integer>> config = new HashMap<>();

    public ConfigManager(String pathToConfigFile) {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(pathToConfigFile));
        } catch (FileNotFoundException e) {
            System.err.println("File:" + pathToConfigFile + " not found");
            throw new RuntimeException(e);
        }
        Map<String, Integer> deliveryIds = new HashMap<>();
        Integer productId = -1;
        Integer deliveryId = 0;
        try {
//            reading product
            reader.beginObject();
            while (reader.hasNext()) {
                String product = reader.nextName();
                products.add(product);
                config.put(++productId, new HashSet<>());
                productsMap.put(product, productId);

//                reading product shipping methods
                reader.beginArray();
                while (reader.hasNext()) {
                    String delivery = reader.nextString();
                    if (!deliveryIds.containsKey(delivery)) {
                        deliveryIds.put(delivery, deliveryId++);
                        this.delivery.add(delivery);
                    }
                    config.get(productId).add(deliveryIds.get(delivery));
                }
                reader.endArray();
            }
            reader.endObject();
        } catch (IOException e) {
            System.err.println("Error reading config file");
            throw new RuntimeException(e);
        }
    }

    public String getProduct(int id) {
        return products.get(id);
    }

    public int getProductId(String product) {
        return productsMap.get(product);
    }

    public Collection<Integer> getProductShipping(String product) {
        return getProductShipping(getProductId(product));
    }

    public Collection<Integer> getProductShipping(int id) {
        return List.copyOf(config.get(id));
    }

    public String getDelivery(int id) {
        return delivery.get(id);
    }

    public int getDeliveriesSize() {
        return delivery.size();
    }


}
