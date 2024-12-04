package ru.otus;

import com.google.common.collect.Lists;
import java.util.List;

@SuppressWarnings("java:S106")
public class HelloOtus {
    public static void main(String[] args) {
        List<String> list = Lists.newArrayList("One", "Two", "Three");
        list.forEach(System.out::println);
    }
}
