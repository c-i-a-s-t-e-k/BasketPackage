package com.ocado.basket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class BasketTest {
    private final ConfigManager manager;
    List<String> items1;
    List<String> items2;

    private void compereStats(int[] expectedStats, Basket basket) {
        int[] actualStats = new int[manager.getDeliveriesSize()];
        for (int deliveryId = 0; deliveryId < manager.getDeliveriesSize(); deliveryId++) {
            actualStats[deliveryId] = basket.getDeliveryStat(deliveryId);
        }
        Assertions.assertArrayEquals(expectedStats, actualStats);
    }

    private BasketTest() {
        String pathToConfig = Thread.currentThread().getContextClassLoader().getResource("config.json").getFile();
        manager = new ConfigManager(pathToConfig);
        String pathToBasket1 = Thread.currentThread().getContextClassLoader().getResource("basket-1.json").getFile();
        items1 = ListReader.readList(new File(pathToBasket1));
        String pathToBasket2 = Thread.currentThread().getContextClassLoader().getResource("basket-2.json").getFile();
        items2 = ListReader.readList(new File(pathToBasket2));
    }

    private int[] calculateStats(List<String> items) {
        int[] result = new int[manager.getDeliveriesSize()];
        for (String product : items) {
            for (int deliveryId : manager.getProductShipping(product))
                result[deliveryId]++;
        }
        return result;
    }

    private void checkCreation(List<String> items) {
        Basket basket = new Basket(items, manager);
        int[] expectedStats = calculateStats(items);

        for (int deliveryId = 0; deliveryId < manager.getDeliveriesSize(); deliveryId++) {
            if (expectedStats[deliveryId] == 0) {
                int finalDeliveryId = deliveryId;
                Basket finalBasket = basket;
                Assertions.assertThrowsExactly(IndexOutOfBoundsException.class, () -> finalBasket.createSplit(finalDeliveryId));
            } else {
                List<String> split = basket.createSplit(deliveryId);
                for (String item : split) {
                    Assertions.assertTrue(items.contains(item));
                    Assertions.assertTrue(manager.getProductShipping(item).contains(deliveryId));
                    basket = new Basket(items, manager);
                }
            }
        }
    }

    @Test
    public void loadingBasket() {
        Basket basket = new Basket(items1, manager);

        int[] expectedStats = calculateStats(items1);
        compereStats(expectedStats, basket);
    }

    @Test
    public void loadingBasket2() {
        Basket basket = new Basket(items2, manager);

        int[] expectedStats = calculateStats(items2);
        compereStats(expectedStats, basket);
    }

    @Test
    public void creatingSplit1() {
        checkCreation(items1);
    }

    @Test
    public void creatingSplit2() {
        checkCreation(items2);
    }

    @Test
    public void tryingToGetEmptySplit() {
        Basket basket = new Basket(items2, manager);
        int[] expectedStats = calculateStats(items1);

        basket.createSplit(0);
        Assertions.assertTrue(basket.createSplit(0).isEmpty());
    }


    @Test
    public void updateTest() {
        Basket basket = new Basket(items2, manager);

        int[] expectedStats = calculateStats(items2);
        List<String> split = basket.createSplit(0);
        compereStats(expectedStats, basket);
        for (String item : split) {
            for (int deliveryId : manager.getProductShipping(item))
                expectedStats[deliveryId]--;
        }
        basket.updateStats();
        compereStats(expectedStats, basket);

    }
}
