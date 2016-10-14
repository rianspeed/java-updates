/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.bootstrap.app.util;

import com.spring.bootstrap.app.bean.Data;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.jsoup.select.Elements;

/**
 *
 * @author sandeep.s
 */
public class RssFeedLoader {

    private final String dateFormatPatterns[] = new String[]{"EEE, d MMM yyyy HH:mm:ss Z",
        "yyyy-MM-dd'T'HH:mm:ss", "d MMM yyyy, HH:mm", "yyyy-MM-dd'T'HH:mm:ssz",
        "dd MMM yyyy, HH:mm", "yyyy.MM.dd G 'at' HH:mm:ss z", "yyMMddHHmmssZ",
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"};

    private RssFeedLoader() {

    }
    private static RssFeedLoader feedLoader;

    public static RssFeedLoader getInstance() {
        if (feedLoader == null) {
            feedLoader = new RssFeedLoader();
        }
        return feedLoader;
    }

    public ArrayList<Data> loadRSS(String urlString) {
        ArrayList<Data> results = new ArrayList<>();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            results.addAll(processXML(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<Data> processXML(InputStream inputStream) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory
                .newDocumentBuilder();
        Document xmlDocument = documentBuilder.parse(inputStream);
        Element rootElement = xmlDocument.getDocumentElement();

        ArrayList<Data> results = new ArrayList<>();
        results = processItem(rootElement);
        if (results.isEmpty()) {
            results = processEntry(rootElement);
        }
        return results;
    }

    public ArrayList<Data> processItem(Element rootElement) {
        NodeList itemsList = rootElement.getElementsByTagName("item");
        if (itemsList.getLength() <= 0) {
            return new ArrayList<>();
        }
        NodeList itemChildren = null;
        Node currentItem = null;
        Node currentChild = null;
        int count = 0;
        ArrayList<Data> results = new ArrayList<>();
        for (int i = 0; i < itemsList.getLength(); i++) {
            currentItem = itemsList.item(i);
            itemChildren = currentItem.getChildNodes();

            Data data = new Data();
            for (int j = 0; j < itemChildren.getLength(); j++) {
                currentChild = itemChildren.item(j);
                if (currentChild.getNodeName().equalsIgnoreCase("title")) {
                    data.setTitle(currentChild.getTextContent().trim());
                    continue;
                }
                if (currentChild.getNodeName().equalsIgnoreCase("pubDate")) {
                    data.setPublishedDate(parseDate(currentChild.getTextContent().trim()));
                    continue;
                }
                if (currentChild.getNodeName().equalsIgnoreCase("description")) {
                    Elements elements = Jsoup.parse(currentChild.getTextContent()).getElementsByTag("img");
                    if (elements != null) {
                        org.jsoup.nodes.Element element = elements.first();
                        if (element != null) {
                            data.setImgUrl(element.attr("src"));
                        }
                    }
                    data.setDesc(currentChild.getTextContent().trim());
                    continue;
                }
                if (currentChild.getNodeName().equalsIgnoreCase("media:thumbnail")) {
                    count++;
                    if (count == 2) {
                        System.out.println("have media thumbnail...");
                        data.setImgUrl(currentChild.getAttributes().item(0).getTextContent());
                        continue;
                    }
                }
                if (currentChild.getNodeName().equalsIgnoreCase("thumbnail")) {
                    System.out.println("have thumbnail...");
                    data.setImgUrl(currentChild.getTextContent().trim());
                    continue;
                }
                if (currentChild.getNodeName().equalsIgnoreCase("link")) {
                    data.setUrl(currentChild.getTextContent().trim());
                    continue;
                }
                if (currentChild.getNodeName().equalsIgnoreCase("content:encoded")) {
                    data.setContent(currentChild.getTextContent().trim());
                    continue;
                }
                if (currentChild.getNodeName().equalsIgnoreCase("content")) {
                    data.setContent(currentChild.getTextContent().trim());
                    continue;
                }

            }
            processImage(data);
            processDescription(data);
            if (data != null) {
                results.add(data);
            }
            count = 0;
        }
        return results;
    }

    public ArrayList<Data> processEntry(Element rootElement) {
        NodeList itemsList = rootElement.getElementsByTagName("entry");
        NodeList itemChildren = null;
        Node currentItem = null;
        Node currentChild = null;
        int count = 0;
        ArrayList<Data> results = new ArrayList<>();
        for (int i = 0; i < itemsList.getLength(); i++) {
            currentItem = itemsList.item(i);
            itemChildren = currentItem.getChildNodes();

            Data data = new Data();
            for (int j = 0; j < itemChildren.getLength(); j++) {
                currentChild = itemChildren.item(j);
                if (currentChild.getNodeName().equalsIgnoreCase("title")) {
                    data.setTitle(currentChild.getTextContent().trim());
                    continue;
                }
                if (currentChild.getNodeName().equalsIgnoreCase("published")) {
                    data.setPublishedDate(parseDate(currentChild.getTextContent()));
                    continue;
                }
                if (currentChild.getNodeName().equalsIgnoreCase("updated")) {
                    data.setPublishedDate(parseDate(currentChild.getTextContent()));
                    continue;
                }
                if (currentChild.getNodeName().equalsIgnoreCase("content")) {
                    data.setDesc(currentChild.getTextContent().trim());
                    continue;
                }
                if (currentChild.getNodeName().equalsIgnoreCase("summary")) {
                    data.setDesc(currentChild.getTextContent().trim());
                    continue;
                }
                if (currentChild.getNodeName().equalsIgnoreCase("media:thumbnail")) {
                    count++;
                    if (count == 2) {
                        System.out.println("have media thumbnail...");
                        data.setImgUrl(currentChild.getAttributes().item(0).getTextContent());
                        continue;
                    }
                }
                if (currentChild.getNodeName().equalsIgnoreCase("thumbnail")) {
                    System.out.println("have thumbnail...");
                    data.setImgUrl(currentChild.getTextContent().trim());
                    continue;
                }
                if (currentChild.getNodeName().equalsIgnoreCase("link")) {
                    data.setUrl(currentChild.getAttributes().getNamedItem("href").getNodeValue());
                    continue;
                }
                if (currentChild.getNodeName().equalsIgnoreCase("id")) {
                    data.setUrl(currentChild.getTextContent().trim());
                    continue;
                }
                if (currentChild.getNodeName().equalsIgnoreCase("content:encoded")) {
                    data.setContent(currentChild.getTextContent().trim());
                    continue;
                }
                if (currentChild.getNodeName().equalsIgnoreCase("content")) {
                    data.setContent(currentChild.getTextContent().trim());
                    continue;
                }

            }
            processImage(data);
            processDescription(data);
            if (data != null) {
                results.add(data);
            }
            count = 0;
        }
        return results;
    }

    private Date parseDate(String dateString) {
        //12 Oct 2015, 09:15
        SimpleDateFormat simpleDateFormat;
        for (String format : dateFormatPatterns) {
            try {
                simpleDateFormat = new SimpleDateFormat(format);
                return simpleDateFormat.parse(dateString);
            } catch (Exception ex) {

            }
        }
        return null;
    }

    private void processImage(Data data) {
        String imgUrl = null;
        if (data.getImgUrl() != null) {
            return;
        }

        if (imgUrl == null) {
            String desc = data.getDesc();
            if (desc != null) {
                org.jsoup.nodes.Document document = Jsoup.parse(desc);
                org.jsoup.nodes.Node node = document.select("img").first();
                if (node != null) {
                    imgUrl = node.attr("src");
                }
            }
        }
        if (imgUrl == null) {
            String desc = data.getContent();
            if (desc != null) {
                org.jsoup.nodes.Document document = Jsoup.parse(desc);
                org.jsoup.nodes.Node node = document.select("img").first();
                if (node != null) {
                    imgUrl = node.attr("src");
                }
            }
        }

        data.setImgUrl(imgUrl);
    }

    private void processDescription(Data data) {
        String desc = data.getDesc();
        String content = data.getContent();
        if (content != null && content.length() > desc.length()) {
            desc = content;
        }
        String text = "";
        if (desc != null) {
            org.jsoup.nodes.Document document = Jsoup.parse(desc);
            document.select("figure").remove();
            org.jsoup.select.Elements elements = document.select("p");
            if (elements == null || elements.isEmpty()) {
                text = document.text().trim();
            } else {
                for (org.jsoup.nodes.Element element : elements) {
                    String elemText = element.text().trim();
                    if (elemText.startsWith("See also:") || elemText.isEmpty()) {
                        continue;
                    }
                    text = text + "\n" + elemText;
                }
            }
        }
        data.setDescText(Jsoup.parse(text).text());
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("md5");
            byte[] hash = messageDigest.digest(data.getTitle().getBytes());
            String hashData = toHexString(hash);
            data.setDataHash(hashData);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
