package com.spring.bootstrap.app.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Welcome on 02-10-2015.
 */
public class Data implements Comparable<Data>,Serializable{
    private Date publishedDate;
    private String title="";
    private String desc="";
    private String descText="";
    private String content="";
    private String imgUrl="";
    private String url="";
    private int fallbackImg;
    private String author="";
    private String catergories="";
    private String dataHash="";

    public Data() {
    }

    public Data(Date publishedDate, String title, String desc, String descText, String imgUrl,
                String url, String catergories, String dataHash, int fallbackImg) {
        this.publishedDate = publishedDate;
        this.title = title;
        this.desc = desc;
        this.descText = descText;
        this.imgUrl = imgUrl;
        this.url = url;
        this.catergories = catergories;
        this.dataHash = dataHash;
        this.fallbackImg=fallbackImg;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getFallbackImg() {
        return fallbackImg;
    }

    public void setFallbackImg(int fallbackImg) {
        this.fallbackImg = fallbackImg;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescText() {
        return descText;
    }

    public void setDescText(String descText) {
        this.descText = descText;
    }

    public String getCatergories() {
        return catergories;
    }

    public void setCatergories(String catergories) {
        this.catergories = catergories;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    @Override
    public int compareTo(Data data) {
        if (getPublishedDate() == null || data.getPublishedDate() == null)
            return 0;
        return getPublishedDate().compareTo(data.getPublishedDate());
    }

    @Override
    public String toString() {
        return "\n Date:"+publishedDate +"\nTitle:"+ title +"\nImg:"+ imgUrl+"\nURL:"+ url+"\nDesc:"+descText+"\n\n"; //To change body of generated methods, choose Tools | Templates.
    }

    public String getSQLFormattedDate() {
        String pubDate = null;
        if(publishedDate!=null) {
            pubDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(publishedDate);
        } else {
            pubDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(0));
        }
        return pubDate;
    }

    public String getInserSQLPart() {
        String pubDate = null;
        if(publishedDate!=null) {
            pubDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(publishedDate);
        } else {
            pubDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(0));
        }
        return "('"
                +pubDate + "','"
                +title + "','"
                +desc + "','"
                +descText + "','"
                +url + "','"
                +imgUrl + "','"
                +catergories + "','"
                +dataHash + "','"
                +0+ ""
                +"');";
    }


}
