package com.example.diningroom.httpRequests;

import android.content.Context;

import com.example.diningroom.config;
import com.example.diningroom.report.ReportParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class getPassData {
    Context context;
    ReportParams reportParams;
    public getPassData(Context context) {
        this.context = context;
        reportParams = new ReportParams(context);
    }

    Document HTMLDoc;
    String jsonStr;
    JSONObject json;
    JSONArray passData;

    //Get JSON from server
    public void getJSON() {
        try {
            String getURL = config.getDataPath + reportParams.getImei();
            //Get JSON data
            //HTMLDoc = Jsoup.connect(getURL).timeout(300000).get()
            jsonStr = Jsoup.connect(getURL).timeout(3000000).maxBodySize(150000000).get().text();
            json = new JSONObject(jsonStr);
            passData = json.getJSONArray("PassData");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    //Get number of incoming pass data
    public int getLength() {
        int dataLength = 0;
        try {
            dataLength = passData.length();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataLength;
    }

    //Get name from input JSON
    public String getName(int number) {
        String jsonName = null;
        try {
            JSONObject jsonStr = new JSONObject(passData.getString(number));
            jsonName = jsonStr.getString("Name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonName;
    }

    public String getSurname(int number) {
        String jsonSurname = null;
        try {
            JSONObject jsonStr = new JSONObject(passData.getString(number));
            jsonSurname = jsonStr.getString("Surname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonSurname;
    }

    //Get patronymic from input JSON
    public String getPatronymic(int number) {
        String jsonPatronymic = null;
        try {
            JSONObject jsonStr = new JSONObject(passData.getString(number));
            jsonPatronymic = jsonStr.getString("Patronymic");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonPatronymic;
    }

    //Get PassNumber from input JSON
    public String getPassNumber(int number) {
        String jsonPassNumber = null;
        try {
            JSONObject jsonStr = new JSONObject(passData.getString(number));
            jsonPassNumber = jsonStr.getString("PassNumber");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonPassNumber;
    }

    //Get Organization from input JSON
    public  String getOrganization(int number) {
        String jsonOrganization = null;
        try {
            JSONObject jsonStr = new JSONObject(passData.getString(number));
            jsonOrganization = jsonStr.getString("Organization");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonOrganization;
    }

    //Get Active from input JSON
    public  String getActive(int number) {
        String jsonActive = null;
        try {
            JSONObject jsonStr = new JSONObject(passData.getString(number));
            jsonActive = jsonStr.getString("Active");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonActive;
    }

    //Get ValidUntil from input JSON
    public  String getValidUntil(int number) {
        String jsonValidUntil = null;
        try {
            JSONObject jsonStr = new JSONObject(passData.getString(number));
            jsonValidUntil = jsonStr.getString("ValidUntil");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonValidUntil;
    }

    //Get PhotoPath from input JSON
    public  String getPhotoPath(int number) {
        String jsonPhotoPath = null;
        try {
            JSONObject jsonStr = new JSONObject(passData.getString(number));
            jsonPhotoPath = jsonStr.getString("PhotoPath");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonPhotoPath;
    }

    //Get PropuskGuid from input JSON
    public  String getPropuskGuid(int number) {
        String jsonPropuskGuid = null;
        try {
            JSONObject jsonStr = new JSONObject(passData.getString(number));
            jsonPropuskGuid = jsonStr.getString("PropuskGuid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonPropuskGuid;
    }
}
