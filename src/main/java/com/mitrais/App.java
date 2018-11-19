package com.mitrais;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class App {

    private static HttpURLConnection con;
    private static URL url;
    private static BufferedReader in;
    private static StringBuffer content = new StringBuffer();
    private static JSONObject jsonObject;
    private static JSONArray jsonArray;
    private static String title, inputLine;
    private static int totalPage;
    private static List<JSONObject> jsonObjectList = new ArrayList<>();
    private static List<String> result = new ArrayList<>();

    public static JSONObject httpGet(String title, int page) throws IOException {
        // HTTP Request.
        url = new URL("https://jsonmock.hackerrank.com/api/movies/search/?Title="+ title +"&page="+ page);
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // HTTP Response.
        in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        content.delete(0, content.length());
        while((inputLine = in.readLine()) != null)
            content.append(inputLine);
        in.close();
        jsonObject = new JSONObject(content.toString());
        totalPage = jsonObject.getInt("total_pages");
        return jsonObject;
    }

    public static void getAllTitles(List<JSONObject> jsonObjectList){
        for(int i = 0; i < totalPage; i++){
            jsonArray = jsonObjectList.get(i).getJSONArray("data");
            for(int j = 0; j < jsonArray.length(); j++){
                jsonObject = jsonArray.getJSONObject(j);
                result.add(jsonObject.optString("Title"));
            }
        }

        // Sorting ascending.
        Collections.sort(result);

        // Print titles.
        for(String title : result){
            System.out.println(title);
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.print("Input Title: ");
        title = in.next();

        // Processing the result.
        jsonObject = httpGet(title, 1);
        jsonObjectList.add(jsonObject);
        if(totalPage > 1){
            for(int i = 2; i <= totalPage; i++){
                jsonObject = httpGet(title, i);
                jsonObjectList.add(jsonObject);
            }
        }
        getAllTitles(jsonObjectList);
    }
}
