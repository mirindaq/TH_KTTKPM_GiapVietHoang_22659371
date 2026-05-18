package com.payment.main.adapter;

public class Main {
    public static void main(String[] args) {
        XmlSystem xmlSystem = new XmlSystem();
        XmlJsonAdapter adapter = new XmlJsonAdapter(xmlSystem);

        String json = "{\"name\":\"Hoang\"}";
        String xml = "<name>Hoang</name>";

        System.out.println("JSON -> XML:");
        System.out.println(adapter.jsonToXml(json));

        System.out.println("\nXML -> JSON:");
        System.out.println(adapter.xmlToJson(xml));

        System.out.println("\nGui JSON qua he thong XML:");
        adapter.sendJsonAsXml(json);
    }
}