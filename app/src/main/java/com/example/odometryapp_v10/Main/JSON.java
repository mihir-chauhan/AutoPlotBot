package com.example.odometryapp_v10.Main;

import android.os.Environment;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

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

    public static void createFile(String fileName, @Nullable String filePath) {
        if (!fileName.contains(".txt")) {
            fileName += ".txt";
        }
        File file = new File(filePath, fileName);
        if (filePath == null) {
            file = new File(Environment.getExternalStorageDirectory() + "/Innov8rz/", fileName);
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
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

    public static boolean doesFileExist(String fileName, @Nullable String filePath) {
        if (!fileName.contains(".txt")) {
            fileName += ".txt";
        }
        StringBuilder sb = new StringBuilder();
        try {
            File textFile = new File(filePath, fileName);
            if (filePath == null) {
                textFile = new File(Environment.getExternalStorageDirectory() + "/Innov8rz/", fileName);
            }
            return textFile.exists();
        } catch (Exception e) {
            return false;
        }
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
                    fileContents.getJSONArray("program").put(jsonArray);
                } else if (fileArchitecture == JSONArchitecture.Function_Notation) {
                    fileContents.getJSONArray("function").put(jsonArray);
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

    public static void removeInJSONTextFile(String fileName, JSONObject fileContents, int positionOfItemForRemoval, String filePath, JSONArchitecture jsonArchitecture) {
        try {
            JSONArray list = new JSONArray();
            JSONArray jsonArray;
            if(jsonArchitecture == JSONArchitecture.Function_Notation) {
                jsonArray = fileContents.getJSONArray("function");
            } else {
                jsonArray = fileContents.getJSONArray("program");
            }
            int len = jsonArray.length();
            if (jsonArray != null) {
                for (int i=0;i<len;i++) {
                    //Excluding the item at position
                    if (i != positionOfItemForRemoval)
                    {
                        list.put(jsonArray.get(i));
                    }
                }
            }
            JSONObject jObject = new JSONObject();
            if(jsonArchitecture == JSONArchitecture.Function_Notation) {
                jObject.put("function", list);
            } else {
                jObject.put("program", list);
            }
            writeJSONToTextFile(fileName, filePath, jObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void removeFromJSONTextFile(String fileName, @Nullable String filePath, int positionOfItemForRemoval, JSONArchitecture jsonArchitecture) {
        JSONObject fileContents = null;
        try {
            fileContents = readJSONTextFile(fileName, filePath);
        } catch (Exception e) {

        }
        removeInJSONTextFile(fileName, fileContents, positionOfItemForRemoval, filePath, jsonArchitecture);
    }

    public static void replaceInJSONTextFile(String fileName, int positionOfItemForRemoval, @Nullable String filePath, @Nullable JSONObject jsonObject, @Nullable JSONArray jsonArray, JSONArchitecture fileArchitecture) {
        JSONObject fileContents = null;
        try {
            fileContents = readJSONTextFile(fileName, filePath);
        } catch (Exception e) {

        }

        try {
            if (jsonArray == null && jsonObject != null) {
                if (fileArchitecture == JSONArchitecture.DefaultRobotController_Notation) {
                    removeInJSONTextFile(fileName, fileContents, positionOfItemForRemoval, filePath, fileArchitecture);
                    appendJSONToTextFile(fileName, filePath, jsonObject, jsonArray, fileArchitecture);
                } else if (fileArchitecture == JSONArchitecture.Function_Notation) {
                    removeInJSONTextFile(fileName, fileContents, positionOfItemForRemoval, filePath, fileArchitecture);
                    appendJSONToTextFile(fileName, filePath, jsonObject, jsonArray, fileArchitecture);
                }
            } else if (jsonArray != null && jsonObject == null) {
                if (fileArchitecture == JSONArchitecture.DefaultRobotController_Notation) {
                    removeInJSONTextFile(fileName, fileContents, positionOfItemForRemoval, filePath, fileArchitecture);
                    appendJSONToTextFile(fileName, filePath, jsonObject, jsonArray, fileArchitecture);
                } else if (fileArchitecture == JSONArchitecture.Function_Notation) {
                    removeInJSONTextFile(fileName, fileContents, positionOfItemForRemoval, filePath, fileArchitecture);
                    appendJSONToTextFile(fileName, filePath, jsonObject, jsonArray, fileArchitecture);
                }
            } else {
                throw new NullPointerException("Both @Nullable JSON inputs are NULL -- when appending to existing file");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Integer returnFunctionPositionFromJSONArray(JSONArray jsonArray, String name) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                if (jsonArray.getJSONObject(i).getString("functionName").equals(name)) {
                    return i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }


    public static void addFunctionFromProgramToFile(String fileName, String functionName, ArrayList<FunctionReturnFormat> functionParameters, @Nullable String filePath){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("functionName", functionName);
            JSONObject parametersObject = new JSONObject();
            for(int i = 0; i < functionParameters.size(); i++) {
                parametersObject.put(functionParameters.get(i).parameterName, functionParameters.get(i).parameterValue);
            }
            jsonObject.put("parameters", parametersObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(filePath == null) {
            appendJSONToTextFile(fileName, Environment.getExternalStorageDirectory() + "/Innov8rz/AutosavedFiles/", jsonObject, null, JSONArchitecture.DefaultRobotController_Notation);
        } else {
            appendJSONToTextFile(fileName, filePath, jsonObject, null, JSONArchitecture.DefaultRobotController_Notation);
        }
    }


    public static void reorderFunctionsInProgram(int fromPosition, int toPosition, String fileName, @Nullable String filePath) {
        JSONObject fileContents = readJSONTextFile(fileName, filePath);

        try {
            ArrayList<JSONObject> functionJSONObjects = new ArrayList<>();
            JSONArray jsonArray = fileContents.getJSONArray("program");
            for(int i = 0; i < jsonArray.length(); i++) {
                functionJSONObjects.add(jsonArray.getJSONObject(i));
            }
            Collections.swap(functionJSONObjects, fromPosition, toPosition);
            JSONArray newlyCreatedJSONArray = new JSONArray();
            for(int i = 0; i < functionJSONObjects.size(); i++) {
                newlyCreatedJSONArray.put(functionJSONObjects.get(i));
            }
            JSONObject newlyCreatedFileObject = new JSONObject();
            newlyCreatedFileObject.put("program", newlyCreatedJSONArray);
            writeJSONToTextFile(fileName, filePath, newlyCreatedFileObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
