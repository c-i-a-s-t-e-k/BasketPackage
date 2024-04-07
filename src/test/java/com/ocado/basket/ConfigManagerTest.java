package com.ocado.basket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigManagerTest {
    private String path = Thread.currentThread().getContextClassLoader().getResource("config1.json").getFile();

    @Test
    public void configTest() {
        ConfigManager manager = new ConfigManager(path);
        String product = manager.getProduct(0);
        assertArrayEquals("Cookies Oatmeal Raisin".toCharArray(), product.toCharArray());
    }

    @Test
    public void config1Test() {
        ConfigManager manager = new ConfigManager(path);
        List<String> expectedProducts = Arrays.asList("Cookies Oatmeal Raisin", "Cheese Cloth");
        List<String> expectedDeliveries = Arrays.asList(
                "Pick-up point",
                "Parcel locker",
                "Courier",
                "Same day delivery",
                "Next day shipping"
        );

        List<String> products = Arrays.asList(manager.getProduct(0), manager.getProduct(1));
        List<String> deliveries = Arrays.asList(
                manager.getDelivery(0),
                manager.getDelivery(1),
                manager.getDelivery(2),
                manager.getDelivery(3),
                manager.getDelivery(4)
        );

        Assertions.assertArrayEquals(expectedDeliveries.toArray(),
                deliveries.toArray());
        Assertions.assertArrayEquals(expectedProducts.toArray(), products.toArray());
    }

    @Test
    public void outOfBoundsTest() {
        ConfigManager manager = new ConfigManager(path);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> manager.getDelivery(10));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> manager.getProduct(10));
    }

    @Test
    public void productIdTest() {
        ConfigManager manager = new ConfigManager(path);
        Assertions.assertEquals(0, manager.getProductId("Cookies Oatmeal Raisin"));
    }

    @Test
    public void productShippingTest() {
        ConfigManager manager = new ConfigManager(path);

        Assertions.assertArrayEquals(new Integer[]{0, 1}, manager.getProductShipping(0).toArray());
    }

}
