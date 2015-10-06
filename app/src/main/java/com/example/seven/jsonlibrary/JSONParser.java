package com.example.seven.jsonlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Seven on 10/5/2015.
 */
public class JSONParser {
    String json, firstKey;

    public JSONParser(String json, String firstKey) {
    this.json = json;
    this.firstKey = firstKey;
    }

    public int getSize(){
        int jsonSize = 0;
        try {
          Object JSON = new JSONTokener(json).nextValue();
            if(JSON instanceof JSONObject){
                JSONObject jObj = new JSONObject(json);
                jsonSize = jObj.length();

            }else if(JSON instanceof JSONArray){
                JSONArray jArray = new JSONArray(json);
                for (int i = 0; i<jArray.length();i++){
                    JSONObject jObj = jArray.getJSONObject(i);
                }
                jsonSize = jArray.length();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonSize;
    }

    public boolean isObject(JSONObject jsonObject,String key){
        JSONObject object;
        JSONArray objects;
        object = jsonObject.optJSONObject(key);
        if(object == null) {
            objects = jsonObject.optJSONArray(key);
            return objects == null?true:false;
        }else{
            return true;
        }
    }

    public List<Map<String,String>> getKeys(){
        List<Map<String,String>> jsonKeys = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json).getJSONObject(firstKey);
            Iterator<String> keys = jsonObject.keys();
            while(keys.hasNext()){
                Map<String,String> map = new HashMap<String,String>();
                String key = keys.next();
                map.put("header", key);
                if(jsonObject.getJSONObject(key).length()>1){
                    JSONObject innerObject = jsonObject.getJSONObject(key);
                    Iterator<String> innerKeys = innerObject.keys();
                    while(innerKeys.hasNext()){
                        String innerKey = innerKeys.next();
                        map.put(key,innerKey);
                    }
                }
                jsonKeys.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonKeys;
    }

    //function to get values on json without arrays
    public List<Map<String,String>> getValues(){
        List<Map<String,String>> values = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(json).getJSONObject(firstKey);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                Map<String,String> map = new HashMap<>();
                String key = keys.next();
                map.put(key,jsonObject.getString(key));
                values.add(map);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        return values;
    }

    public List<Map<String,List<Map<String,String>>>> get2Values(){
        List<Map<String,List<Map<String,String>>>> values = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(json).getJSONObject(firstKey);
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                Map<String,String> map = new HashMap<>();
                String key = keys.next();
                if (jsonObject.getJSONObject(key).length() > 1) {
                    List<Map<String,String>> innerValues = new ArrayList<>();
                    JSONObject innerObject = jsonObject.getJSONObject(key);
                    Iterator<String> innerKeys = innerObject.keys();
                    String innerKey = innerKeys.next();
                    while (innerKeys.hasNext()){
                        Map<String,String> innerMap = new HashMap<>();
                        if(isObject(innerObject,innerKey)){
                            String value = innerObject.getString(innerKey);
                            innerMap.put(innerKey,value);
                        }else{
                            JSONArray array = innerObject.getJSONArray(innerKey);
                            for(int i = 0;i<array.length();i++){
                                JSONObject jObj = array.getJSONObject(i);
                                Iterator<String> innerIteratorKeys = jObj.keys();
                                while (innerIteratorKeys.hasNext()){
                                    String iterKey = innerIteratorKeys.next();
                                    innerMap.put(iterKey,jObj.getString(iterKey));
                                }
                            }
                        }
                        innerValues.add(innerMap);
                    }
                }
            }

        }catch (JSONException e){
            e.printStackTrace();
        }


        return values;
    }


}
