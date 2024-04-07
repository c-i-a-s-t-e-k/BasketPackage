package com.ocado.basket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

public class BasketSplitterTest {
    private final String pathToConfig
            = Thread.currentThread().getContextClassLoader().getResource("config.json").getFile();
    private final List<String> items1;
    private final List<String> items2;

    private BasketSplitterTest(){
        String pathToBasket1 = Thread.currentThread().getContextClassLoader().getResource("basket-1.json").getFile();
        items1 = ListReader.readList(new File(pathToBasket1));
        String pathToBasket2 = Thread.currentThread().getContextClassLoader().getResource("basket-2.json").getFile();
        items2 = ListReader.readList(new File(pathToBasket2));
    }

    @Test public void splitTest() {
        BasketSplitter splitter = new BasketSplitter(pathToConfig);
        Assertions.assertFalse(splitter.split(items1).isEmpty());
        Assertions.assertFalse(splitter.split(items2).isEmpty());
    }
    


    private void checkNecessaryCondition(Collection<String> items){
        ConfigManager manager = new ConfigManager(pathToConfig);
        Basket basket;
        Set<String> actualItems = new HashSet<>(items);
        Set<String> usedDeliveries = new HashSet<>();
        BasketSplitter splitter = new BasketSplitter(pathToConfig);
        Map<String, List<String>> map = splitter.split((List<String>) items);
        SortedMap<List<String>, String> reversedSplit = new TreeMap<>(Comparator.comparingInt(List::size));
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            reversedSplit.put(entry.getValue(), entry.getKey());
        }
        while (!reversedSplit.isEmpty()) {
            List<String> biggestSplit = reversedSplit.lastKey();
            String biggestDelivery = reversedSplit.remove(biggestSplit);
            usedDeliveries.add(biggestDelivery);

            for(int deliveryId = 0; deliveryId < manager.getDeliveriesSize()-1; deliveryId++) {
                if(usedDeliveries.contains(manager.getDelivery(deliveryId))) continue;
                basket = new Basket(actualItems, manager);
                List<String> someSplit = basket.createSplit(deliveryId);
                Assertions.assertTrue(biggestSplit.size() >= someSplit.size());
            }
            actualItems.removeAll(biggestSplit);

        }
    }
    @Test public void splitCorrectnessTest1() {
        checkNecessaryCondition(items1);}
    @Test public void splitCorrectnessTest2() {
        checkNecessaryCondition(items2);}

}
