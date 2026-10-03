package com.universal.engineers.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CatalogService {
    private final LinkedHashMap<String,List<String>> catalog = new LinkedHashMap<>();
    public CatalogService(){
        add("Raw Material","Metal Materials"); add("Consumables","Abrasive Consumables");
        add("Welding","Welding Consumables"); add("Fasteners","Mechanical Fasteners");
        add("Gas","Industrial Gases"); add("Machining Items","Cutting & Machining Tools");
        add("Electrical","Electrical Components"); add("Tools","Hand Tools");
        add("Safety / PPE","Personal Protective Equipment"); add("Stationery","Office Stationery");
        add("Hardware","General Hardware"); add("Paint & Chemical","Surface Treatment Chemicals");
        add("FRP Materials","Fiberglass Materials"); add("Furnishing","Interior & Upholstery");
        add("Packaging","Packing Materials"); add("Assets","IT & Office Assets");
        add("Office Equipment","Computer Peripherals & Network");
    }
    private void add(String c,String s){catalog.computeIfAbsent(c,k->new ArrayList<>()).add(s);}
    public List<String> getCategories(){return new ArrayList<>(catalog.keySet());}
    public List<String> getSubCategories(String category){return catalog.getOrDefault(category,List.of());}
    public boolean valid(String category,String sub){return catalog.containsKey(category)&&catalog.get(category).contains(sub);}
    public Map<String,List<String>> getCatalog(){return Collections.unmodifiableMap(catalog);}
}
