package com.example.futbolapp.ui.home;

public enum ApiMap {
    LA_LIGA("La Liga", 140),
    PREMIER("Premier League", 39);

    private String name;
    private int value;

    private ApiMap(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static int getValueByName(String name) {
        for (ApiMap apiMap : ApiMap.values()) {
            if (apiMap.getName().equals(name)) {
                return apiMap.getValue();
            }
        }
        throw new IllegalArgumentException("Invalid name: " + name);
    }
}
