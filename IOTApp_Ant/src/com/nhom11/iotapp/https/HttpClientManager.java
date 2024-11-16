/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.https;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nhom11.iotapp.callback.HttpResponseCallback;
import com.nhom11.iotapp.entities.ModelLogin;
import com.nhom11.iotapp.entities.User;
import java.io.IOException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;


/**
 *
 * @author DELL
 */
public class HttpClientManager {

    User user;

    public User getUser() {
        return user;
    }
    static HttpClientManager instance;
    String BaseUrl = "http://localhost:5000/api";

    public String getBaseUrl() {
        return BaseUrl;
    }

    public void setBaseUrl(String BaseUrl) {
        this.BaseUrl = BaseUrl;
    }

    public static HttpClientManager getInstance() {
        if (instance == null) {
            instance = new HttpClientManager();
        }
        return instance;
    }

    CloseableHttpClient httpClient;
    private HttpPost createPostRequest(String jsonPayload,String apiEndpoint){
        // Create a POST request to authenticate and get JWT
        HttpPost postRequest = new HttpPost(BaseUrl + apiEndpoint);

        // Set credentials as JSON payload
        StringEntity entity = new StringEntity(jsonPayload);
        postRequest.setEntity(entity);
        
        // Set the Content-Type header
        postRequest.setHeader("Content-Type", "application/json");
        if(user != null)
        {
            postRequest.setHeader("Authorization", "Bearer " + user.getAccess_token());
        }
        return postRequest;
    }
    public void login(ModelLogin modelLogin,HttpResponseCallback callback) throws IOException, ParseException{
        CloseableHttpResponse response = httpClient.execute(createPostRequest(modelLogin.toJsonObj(), "/auth/login"));
        String reponseString = EntityUtils.toString(response.getEntity());
        System.out.println(reponseString);
        JsonObject jsonResponse = JsonParser.parseString(reponseString).getAsJsonObject();
        if(jsonResponse.get("success").getAsBoolean()){
            user = new User(jsonResponse.get("data").getAsJsonObject());
            callback.onSuccess();
        }
        else {
            callback.onFailed(jsonResponse.get("message").getAsString());
        }
    }
    private HttpClientManager() {
        httpClient = HttpClients.createDefault();
    }
;
}
