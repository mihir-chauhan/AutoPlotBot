package com.example.odometryapp_v10;

import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

public class JSON {
    public static enum JSONArchitecture {
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
}
