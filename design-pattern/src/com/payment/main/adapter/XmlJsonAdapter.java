package com.payment.main.adapter;

public class XmlJsonAdapter implements DataConverter {
    private XmlSystem xmlSystem;

    public XmlJsonAdapter(XmlSystem xmlSystem) {
        this.xmlSystem = xmlSystem;
    }

    @Override
    public String xmlToJson(String xmlData) {
        return "{ \"data\": \"" + xmlData + "\" }";
    }

    @Override
    public String jsonToXml(String jsonData) {
        return "<data>" + jsonData + "</data>";
    }

    public void sendJsonAsXml(String jsonData) {
        String xmlData = jsonToXml(jsonData);
        xmlSystem.sendXml(xmlData);
    }
}
