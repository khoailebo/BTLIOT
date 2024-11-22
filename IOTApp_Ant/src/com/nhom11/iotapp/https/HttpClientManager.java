/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.https;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nhom11.iotapp.bluetooth.BluetoothManager;
import com.nhom11.iotapp.callback.HttpResponseCallback;
import com.nhom11.iotapp.entities.Fine;
import com.nhom11.iotapp.entities.Mesurement;
import com.nhom11.iotapp.entities.ModelDevice;
import com.nhom11.iotapp.entities.ModelLogin;
import com.nhom11.iotapp.entities.ModelSignUp;
import com.nhom11.iotapp.entities.User;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;

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

    public void setBaseUrl(String fineName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/config/baseurl.txt"));
            String line = reader.readLine();
            if(line.contains(":")){
                BaseUrl = line;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HttpClientManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HttpClientManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static HttpClientManager getInstance() {
        if (instance == null) {
            instance = new HttpClientManager();
            instance.setBaseUrl("baseurl.txt");
        }
        return instance;
    }

    CloseableHttpClient httpClient;

    private HttpPost createPostRequest(String jsonPayload, String apiEndpoint) {
        // Create a POST request to authenticate and get JWT
        HttpPost postRequest = new HttpPost(BaseUrl + apiEndpoint);

        // Set credentials as JSON payload
        StringEntity entity = new StringEntity(jsonPayload);
        postRequest.setEntity(entity);

        // Set the Content-Type header
        postRequest.setHeader("Content-Type", "application/json");
        if (user != null) {
            postRequest.setHeader("Authorization", "Bearer " + user.getAccess_token());
        }
        return postRequest;
    }

    public void signout() {
        user = null;
    }

    public void idendifyDevice(ModelDevice modelDevice,HttpResponseCallback callback) throws IOException, ParseException{
        if(!BluetoothManager.getInstance().getVirtualDevice().isSystemDevice()){
            callback.onFailed("Device is not recognize");
            return;
        }
        CloseableHttpResponse response = httpClient.execute(createPostRequest(modelDevice.toJsonObj(), "/devices/register"));
        String responseString = EntityUtils.toString(response.getEntity());
        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
        String msg = jsonResponse.get("message").getAsString();
        int statusCode = response.getCode();
        System.out.println(statusCode);
        if(statusCode == 201){
            callback.onSuccess(msg);
        }
        else {
            callback.onFailed(msg);
        }
    }
    public boolean checkDeviceIdentity(String DeviceId) throws URISyntaxException, IOException, ParseException {
        HashMap<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("device_id", DeviceId);
        HttpGet getRequest = createGetRequest("/devices/check", null,DeviceId);
        CloseableHttpResponse response = httpClient.execute(getRequest);
        String responseString = EntityUtils.toString(response.getEntity());
        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
        System.out.println(jsonResponse.get("exists").getAsBoolean());
        return jsonResponse.get("exists").getAsBoolean();
    }

    public HttpGet createGetRequest(String apiEndpoint, HashMap<String, ? extends Object> queryParameters, Object pathParamenter) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(BaseUrl + apiEndpoint + (pathParamenter != null ? "/" + pathParamenter.toString() : ""));
//                .addParameter("userId", "1") // Add parameters here
//                .build();
        if (queryParameters != null) {
            for (String key : queryParameters.keySet()) {
                builder.addParameter(key, queryParameters.get(key).toString());
            }
        }
        URI uri = builder.build();
        // Create a GET request with the URI

        HttpGet request = new HttpGet(uri);
        request.setHeader("Content-Type", "application/json");
        if (user != null) {
            request.setHeader("Authorization", "Bearer " + user.getAccess_token());
        }
        return request;
    }

    
    public void submitFineInfo(Fine fine,HttpResponseCallback callback) throws IOException{
        CloseableHttpResponse response = httpClient.execute(createPostRequest(fine.toJsonObj(), "/measurements"));
        if(response.getCode() == 201){
            callback.onSuccess("Submit Success");
        }
        else{callback.onFailed("Submit Failed");}
    }
    public HttpDelete createDeleteRequest(String apiEndPoint,String pathParameter){
        HttpDelete deleteRequest = new HttpDelete(BaseUrl + apiEndPoint + (pathParameter != null ? "/" + pathParameter.toString() : ""));
        deleteRequest.setHeader("Content-Type", "application/json");
        if (user != null) {
            deleteRequest.setHeader("Authorization", "Bearer " + user.getAccess_token());
        }
        return  deleteRequest;
    }
    public void deleteDevice(String deviceId,HttpResponseCallback callback) throws IOException, ParseException{
        CloseableHttpResponse response = httpClient.execute(
                createDeleteRequest("/devices", deviceId)
        );
        String responseString = EntityUtils.toString(response.getEntity());
        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
        boolean success = jsonResponse.get("success").getAsBoolean();
        String msg = jsonResponse.get("message").getAsString();
        if(success){
            callback.onSuccess(msg,deviceId);
        }
        else {
            callback.onFailed(msg);
        }
    }
    public void getDeviceList(HttpResponseCallback callback) throws URISyntaxException, IOException, ParseException{
        HashMap<String,String> queryParameters = new HashMap<>();
        queryParameters.put("status", "active");       
//        queryParameters.put("search", "test");
        queryParameters.put("page", "1");
        queryParameters.put("per_page", "10");

        
        CloseableHttpResponse response = httpClient.execute(
                createGetRequest("/devices", queryParameters, null)
        );
        String responseString = EntityUtils.toString(response.getEntity());
        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
        boolean success = jsonResponse.get("success").getAsBoolean();
        if(success){
            JsonObject data = jsonResponse.getAsJsonObject("data");
            JsonArray devices = data.getAsJsonArray("devices");
            List<ModelDevice> devicesList = new ArrayList<>();
            for(JsonElement device : devices){
                devicesList.add(new Gson().fromJson(device, ModelDevice.class));
            }
            if(!devicesList.isEmpty() && devicesList.get(0) instanceof ModelDevice device)
            callback.onSuccess(devicesList.toArray());
        }
        else{
            callback.onFailed();
        }
    }
    public void getAllMesurements(HttpResponseCallback callback) throws URISyntaxException, IOException, ParseException{
        CloseableHttpResponse response = httpClient.execute(
                createGetRequest("/measurements", null, null)
        );
        String responseString = EntityUtils.toString(response.getEntity());
        JsonArray mesurementArray = JsonParser.parseString(responseString).getAsJsonArray();
        List<Mesurement>mesurementList = new ArrayList<>();
        for(JsonElement mesurement : mesurementArray){
            mesurementList.add(new Gson().fromJson(mesurement, Mesurement.class));
        }
        System.out.println(responseString);
        if(response.getCode() == 200){
            callback.onSuccess(mesurementList.toArray());
        }
        else {
            callback.onFailed("Cannot get data");
        }
    }
    public void signup(ModelSignUp signUp, HttpResponseCallback callback) throws IOException, ParseException {
        CloseableHttpResponse response = httpClient.execute(createPostRequest(signUp.toJsonObj(), "/auth/register"));
        String responseString = EntityUtils.toString(response.getEntity());
        System.out.println(responseString);
        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
        boolean success = jsonResponse.get("success").getAsBoolean();
        if (success) {
            callback.onSuccess(jsonResponse.get("message").getAsString());
        } else {
            callback.onFailed("Sign up failed");
        }

    }

    public void login(ModelLogin modelLogin, HttpResponseCallback callback) throws IOException, ParseException {
        CloseableHttpResponse response = httpClient.execute(createPostRequest(modelLogin.toJsonObj(), "/auth/login"));
        String reponseString = EntityUtils.toString(response.getEntity());
        System.out.println(reponseString);
        JsonObject jsonResponse = JsonParser.parseString(reponseString).getAsJsonObject();
        if (jsonResponse.get("success").getAsBoolean()) {
            user = new User(jsonResponse.get("data").getAsJsonObject());
            callback.onSuccess();
        } else {
            callback.onFailed(jsonResponse.get("message").getAsString());
        }
    }

    private HttpClientManager() {
        httpClient = HttpClients.createDefault();
    }
;
}
