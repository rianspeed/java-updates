/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.bootstrap.app.util;

import com.spring.bootstrap.app.bean.Data;
import com.spring.bootstrap.app.bean.Feeds;
import java.util.ArrayList;

/**
 *
 * @author sandeep.s
 */
public class FeedExecuter {
    public static void main(String[] args) {
        RssFeedLoader feedLoader = RssFeedLoader.getInstance();
        for(String url: Feeds.imageUrls) {
            ArrayList<Data> dataList = feedLoader.loadRSS(url);
            for(Data data : dataList) {
                System.out.println(data);
            }
        }
        
    }
}
