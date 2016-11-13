package ru.test.drom.dromtest.mvp.models;

import java.util.List;

public class Repositories {
    public int total_count;
    public boolean incomplete_results;

    public List<Item> items;

    public class Item {
        public int id;
        public String name;
        public String full_name;
        public String description;

        public User owner;

        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public String getDescription() {
            return description;
        }
    }
}
