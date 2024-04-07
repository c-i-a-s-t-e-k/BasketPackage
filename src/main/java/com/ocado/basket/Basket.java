package com.ocado.basket;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Basket implements Cloneable{
    private final ConfigManager manager;
    private final Map<Integer, Set<Integer>> productsNodes;
    private final Map<Integer, Set<Integer>> deliveryNodes;
    private final Set<Integer> changes;
    private int[] deliveryStat;


    public Basket(Collection<String> items, ConfigManager manager) {
        productsNodes = new HashMap<>();
        deliveryNodes = new HashMap<>();
        this.manager = manager;
        for (String item : items) {
            int productId = manager.getProductId(item);
            HashSet<Integer> itemEdges = new HashSet<Integer>(manager.getProductShipping(productId));
            productsNodes.put(productId, itemEdges);

            for (int deliveryId : itemEdges) {
                if (!deliveryNodes.containsKey(deliveryId)) {
                    deliveryNodes.put(deliveryId, new HashSet<>(List.of(productId)));
                } else {
                    deliveryNodes.get(deliveryId).add(productId);
                }
            }
        }
        changes = IntStream.rangeClosed(0, manager.getDeliveriesSize() - 1).boxed().collect(Collectors.toSet());
        deliveryStat = new int[manager.getDeliveriesSize()];
        updateStats();
    }

    private Basket(
            Map<Integer, Set<Integer>> deliveryNodes,
            Map<Integer, Set<Integer>> productsNodes,
            ConfigManager manager, Set<Integer> changes,
            int[] deliveryStat
    ){
        this.deliveryNodes = deliveryNodes;
        this.productsNodes = productsNodes;
        this.manager = manager;
        this.changes = changes;
        this.deliveryStat = deliveryStat;
    }

    //    ths should be called when *changes* is empty
    public void updateStats() {
        deliveryStat = new int[manager.getDeliveriesSize()];
        for (int deliveryId : changes) {
            if (!deliveryNodes.containsKey(deliveryId)) {
                deliveryStat[deliveryId] = 0;
            } else {
                deliveryStat[deliveryId] = deliveryNodes.get(deliveryId).size();
            }
        }
        changes.clear();
    }

    public int getDeliveryStat(int deliveryId) {
        return deliveryStat[deliveryId];
    }

    private Collection<Integer> removeProduct(int productId) {
        Collection<Integer> changedDeliveriesIds = productsNodes.remove(productId);
        for (int deliveryId : changedDeliveriesIds) {
            deliveryNodes.get(deliveryId).remove(productId);
        }
        changes.addAll(changedDeliveriesIds);
        return changedDeliveriesIds;
    }

    //    returns list of Products that can be delivery by *deliveryId* and sends information to *changes* about other
//    deliveries which according product was taken. after operation Products and that Delivery are removed
    public List<String> createSplit(int deliveryId) {
        List<String> split = new ArrayList<>();

        if (!deliveryNodes.containsKey(deliveryId)) {
            return split;
        }

        List<Integer> deliveryNode = List.copyOf(deliveryNodes.get(deliveryId));
        for (Integer productId : deliveryNode) {
            split.add(manager.getProduct(productId));
            removeProduct(productId);
        }
        deliveryNodes.remove(deliveryId);
        return split;
    }

    public boolean haveChanges() {
        return !changes.isEmpty();
    }

    public Collection<Integer> getChanges() {
        return new ArrayList<>(this.changes);
    }

    @Override
    public Basket clone() {
        return  new Basket(
                Map.copyOf(productsNodes),
                Map.copyOf(deliveryNodes),
                manager,
                Set.copyOf(changes),
                Arrays.copyOf(deliveryStat, deliveryStat.length));
    }
}
