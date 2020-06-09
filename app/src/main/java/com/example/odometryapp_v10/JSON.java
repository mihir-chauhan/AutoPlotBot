package com.example.odometryapp_v10;

import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JSON {
    public enum JSONArchitecture {
        Function_Notation, DefaultRobotController_Notation
    }

    public static void writeJSONToTextFile(String fileName, @Nullable String filePath, JSONArray jsonArray, JSONArchitecture fileArchitecture) {
        JSONObject fileJSONObject = new JSONObject();
        if (!fileName.contains(".txt")) {
            fileName += ".txt";
        }
        File file = new File(filePath, fileName);
        if (filePath == null) {
            file = new File(Environment.getExternalStorageDirectory() + "/Innov8rz/", fileName);
        }
        FileOutputStream outputStream;
        try {
            if(fileArchitecture == JSONArchitecture.DefaultRobotController_Notation) {
                fileJSONObject.put("program", jsonArray);
            } else if(fileArchitecture == JSONArchitecture.Function_Notation) {
                fileJSONObject.put("function", jsonArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] data = fileJSONObject.toString().getBytes();
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeJSONToTextFile(String fileName, @Nullable String filePath, JSONObject jsonObject) {
        JSONObject fileJSONObject = jsonObject;
        if (!fileName.contains(".txt")) {
            fileName += ".txt";
        }
        File file = new File(filePath, fileName);
        if (filePath == null) {
            file = new File(Environment.getExternalStorageDirectory() + "/Innov8rz/", fileName);
        }
        FileOutputStream outputStream;
        byte[] data = fileJSONObject.toString().getBytes();
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject readJSONTextFile(String fileName, @Nullable String filePath) {
        return readFile(fileName, filePath);
    }

    @Nullable public static JSONObject readFile(String fileName, @Nullable String filePath) {
        if (!fileName.contains(".txt")) {
            fileName += ".txt";
        }
        System.out.println(fileName);
        StringBuilder sb = new StringBuilder();
        try {
            File textFile = new File(filePath, fileName);
            if (filePath == null) {
                textFile = new File(Environment.getExternalStorageDirectory() + "/Innov8rz/", fileName);
            }
            FileInputStream fis = new FileInputStream(textFile);
            if (fis != null) {
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader buff = new BufferedReader(isr);

                String line;
                while ((line = buff.readLine()) != null) {
                    sb.append(line + "\n");
                }

                fis.close();

                System.out.println("File Contents: " + sb);

                return new JSONObject(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void appendJSONToTextFile(String fileName, @Nullable String filePath, JSONArray jsonArray, JSONArchitecture fileArchitecture) {

        JSONObject fileContents = readJSONTextFile(fileName, filePath);

        if(fileContents == null || fileContents.toString().isEmpty()) {
            writeJSONToTextFile(fileName, filePath, jsonArray, fileArchitecture);
        }

        System.out.println("JSON File Contents: " + fileContents);

        try {
            if(fileArchitecture == JSONArchitecture.DefaultRobotController_Notation) {
                fileContents.getJSONArray("program").put(jsonArray);
            } else if(fileArchitecture == JSONArchitecture.Function_Notation) {
                fileContents.getJSONArray("function").put(jsonArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("FINAL JSON File Contents: " + fileContents);

        writeJSONToTextFile(fileName, filePath, fileContents);
    }
}
