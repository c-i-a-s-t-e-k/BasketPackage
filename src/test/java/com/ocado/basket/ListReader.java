package com.ocado.basket;

import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ListReader {
    static List<String> readList(File file) {
        List<String> list = new ArrayList<>();

        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                list.add(reader.nextString());
            }
            reader.endArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
