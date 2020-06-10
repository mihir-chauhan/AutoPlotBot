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
            if (fileArchitecture == JSONArchitecture.DefaultRobotController_Notation) {
                fileJSONObject.put("program", jsonArray);
            } else if (fileArchitecture == JSONArchitecture.Function_Notation) {
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

    @Nullable
    public static JSONObject readJSONTextFile(String fileName, @Nullable String filePath) {
        if (!fileName.contains(".txt")) {
            fileName += ".txt";
        }
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


                return new JSONObject(sb.toString());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static void appendJSONToTextFile(String fileName, @Nullable String filePath, @Nullable JSONObject jsonObject, @Nullable JSONArray jsonArray, JSONArchitecture fileArchitecture) {


        JSONObject fileContents = null;
        try {
            fileContents = readJSONTextFile(fileName, filePath);
        } catch (Exception e) {

        }

        if (fileContents == null || fileContents.length() == 0) {
            if (jsonArray != null) {
                writeJSONToTextFile(fileName, filePath, jsonArray, fileArchitecture);
            } else if (jsonObject != null) {
                JSONArray jArray = new JSONArray();
                jArray.put(jsonObject);
                writeJSONToTextFile(fileName, filePath, jArray, fileArchitecture);
            } else {
                throw new NullPointerException("Both @Nullable JSON inputs are NULL -- when writing to new file");
            }
            return;
        }


        try {
            if (jsonArray == null && jsonObject != null) {
                if (fileArchitecture == JSONArchitecture.DefaultRobotController_Notation) {
                    fileContents.getJSONArray("program").put(jsonObject);
                } else if (fileArchitecture == JSONArchitecture.Function_Notation) {
                    fileContents.getJSONArray("function").put(jsonObject);
                }
            } else if (jsonArray != null && jsonObject == null) {
                if (fileArchitecture == JSONArchitecture.DefaultRobotController_Notation) {
                    fileContents.getJSONArray("program").put(jsonObject);
                } else if (fileArchitecture == JSONArchitecture.Function_Notation) {
                    fileContents.getJSONArray("function").put(jsonObject);
                }
            } else {
                throw new NullPointerException("Both @Nullable JSON inputs are NULL -- when appending to existing file");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        writeJSONToTextFile(fileName, filePath, fileContents);
    }

    public static String splitCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }
}
