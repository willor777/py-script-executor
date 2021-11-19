package tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ScriptExector {

    public String path;
    public JsonObject jsonParams = new JsonObject();

    public ScriptExector(String path){
        this.path = path;
    }

    /** <h3>Adds String type key-word-arguments to JSON Object to be sent to py script</h3> */
    public void addParam(String key, String val){

        jsonParams.addProperty("\"" + key + "\"", "\"" + val + "\"");
    }


    /** <h4>
     * Runs a python script at the given path. Will return output line-by-line as LinkedList
     * * </h4>*/
    public LinkedList<String> runScriptGetLinkedListOutput(){
        ProcessBuilder pBuilder = new ProcessBuilder("python",
                path, this.jsonParams.toString());

        pBuilder.redirectErrorStream(true);
        try{
            Process pyProcess = pBuilder.start();
            BufferedReader pyOut = new BufferedReader(new InputStreamReader(pyProcess.getInputStream()));
            BufferedReader pyErr = new BufferedReader(new InputStreamReader(pyProcess.getErrorStream()));

            String out;
            LinkedList<String> pyResults = new LinkedList<>();

            while((out = pyOut.readLine()) != null){
                pyResults.add(out);
            }

            if(pyResults.size() == 0){
                while((out = pyErr.readLine()) != null){
                    pyResults.add(out);
                }
            }

            return pyResults;

        }catch(Exception e){
            System.out.println("\n[ CRITICAL - Exception Caught ]");
            e.printStackTrace();
        }

    return null;
    }

    /** <h4>
     * Runs a python script at the given path. Will return output as a Single HashMap
     * </h4> <h2>Intended for use with scripts that return a single JSON String</h2>*/
    public HashMap<String, String> runScriptGetSingleMap(){
        ProcessBuilder pBuilder = new ProcessBuilder("python",
                path, this.jsonParams.toString());

//        pBuilder.redirectErrorStream(true);
        try{
            Process pyProcess = pBuilder.start();
            BufferedReader pyOut = new BufferedReader(new InputStreamReader(pyProcess.getInputStream()));
            BufferedReader pyErr = new BufferedReader(new InputStreamReader(pyProcess.getErrorStream()));
            String out;
            LinkedList<String> pyResults = new LinkedList<>();

            while((out = pyOut.readLine()) != null){
                pyResults.add(out);
            }
            boolean errFlag = false;
            if(pyResults.size() == 0){
                errFlag = true;

                while((out = pyErr.readLine()) != null){
                    pyResults.add(out);
                }
                System.out.println("[ Py ERROR MSG... ]");
                for(String s: pyResults){
                    System.out.println(s);
                }
                return null;
            }

            // Go thru results and find json strings
            for(String s: pyResults){
                if((s.contains("{")) && (s.contains("}"))){
                    try{
                        Gson gsonObj = new Gson();
                        HashMap jsonMap = gsonObj.fromJson(s, HashMap.class);
                        return jsonMap;
                    }catch(com.google.gson.JsonSyntaxException e){
                        System.out.println("[ CRITICAL - JsonSyntaxException Caught! ]");
                        System.out.println("Causes - > :    " + s);
                        e.printStackTrace();
                    }
                    }
            }


        }catch(Exception e){
            System.out.println("\n[ CRITICAL - Exception Caught ]");
            e.printStackTrace();
        }

        return null;
    }

    /** <h4>
     * Runs a python script at the given path. Will return output as a Single HashMap
     * </h4> <h2>Intended for use on scripts with multiple return JSON strings</h2>*/
    public LinkedList<HashMap<String, String>> runScriptGetMultipleMaps(){
        ProcessBuilder pBuilder = new ProcessBuilder("python",
                path, this.jsonParams.toString());

        pBuilder.redirectErrorStream(true);
        try{
            Process pyProcess = pBuilder.start();
            BufferedReader pyOut = new BufferedReader(new InputStreamReader(pyProcess.getInputStream()));
            BufferedReader pyErr = new BufferedReader(new InputStreamReader(pyProcess.getErrorStream()));

            String out;
            LinkedList<String> pyResults = new LinkedList<>();

            while((out = pyOut.readLine()) != null){
                pyResults.add(out);
            }

            if(pyResults.size() == 0){
                while((out = pyErr.readLine()) != null){
                    pyResults.add(out);
                }
            }

            LinkedList<HashMap<String, String>> scriptReturnJson = new LinkedList<>();

            // Go thru results and find json strings
            for(String s: pyResults){
                if((s.contains("{")) && (s.contains("}"))){

                    try{
                        Gson gsonObj = new Gson();
                        HashMap jsonMap;
                        jsonMap = gsonObj.fromJson(s, HashMap.class);
                        scriptReturnJson.add(jsonMap);
                    }catch(com.google.gson.JsonSyntaxException e){
                        System.out.println("[ JsonSyntaxException Caught!! ]");
                        System.out.println("Cause - > :    " + s);
                        e.printStackTrace();
                        }
                }

            }
            return scriptReturnJson;


        }catch(Exception e){
            System.out.println("\n[ CRITICAL - Exception Caught ]");
            e.printStackTrace();
        }

        return null;
    }
}
