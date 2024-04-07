package com.ocado.basket;

import java.util.*;


public class BasketSplitter {
    private final ConfigManager manager;

    public BasketSplitter(String absolutePathToConfigFile) {
        manager = new ConfigManager(absolutePathToConfigFile);
    }

//    this function could return wrong result - when in *queue* we have items with same priority
//    then we do not check both, just take random (heuristics)
    public Map<String, List<String>> split(List<String> items) {
        Map<String, List<String>> splits = new HashMap<>();
        Basket basket = new Basket(items, manager);
        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt((o) -> {
            return -basket.getDeliveryStat(o);
        }));

//        init queue
        for (int deliveryId = 0; deliveryId < manager.getDeliveriesSize(); deliveryId++) {
            if (basket.getDeliveryStat(deliveryId) > 0) queue.add(deliveryId);
        }

        while (!queue.isEmpty()) {
            int currentDeliveryId = queue.poll();
            if (basket.getDeliveryStat(currentDeliveryId) == 0) break;
//            creating splits
            splits.put(manager.getDelivery(currentDeliveryId), basket.createSplit(currentDeliveryId));

//            updating queue
            if (basket.haveChanges()) {
                Collection<Integer> changes = basket.getChanges();
                for (int changedDeliveryId : changes) {
                    queue.remove(changedDeliveryId);
                }
                basket.updateStats();
                changes.remove(currentDeliveryId);
                queue.addAll(changes);
            }
        }
        return splits;
    }
}