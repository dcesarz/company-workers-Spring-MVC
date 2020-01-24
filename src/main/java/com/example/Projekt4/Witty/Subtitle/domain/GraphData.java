package com.example.Projekt4.Witty.Subtitle.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GraphData {

    private String name;
    private List<Map<Object, Object>> workers;

    public GraphData() {
    }

}
