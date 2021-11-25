import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

public class PyScriptExecutor {

    public String path;

    public LinkedList<String> params = new LinkedList<>();

    public PyScriptExecutor(String path){
        this.path = path;
    }


    /**
     * <h4>Creates params in string format of "KEY:VALUE". They will be joined with "," later </h4>
     */
    public void addParam(String key, String val){

        String joinedParam = "$" + key + ":" + val + "$";
        this.params.add(joinedParam);
    }


    public String getParamsForPy() {

        // Params will start end and be split with $$
        String formattedParams = "$";

        if (!(this.params.size() == 0)) {
            for (String s : params) {
                formattedParams = formattedParams + s;
            }
        } else {
            return "$$NoParams:None$$";
        }
        formattedParams = formattedParams + "$";
        return formattedParams;
    }


    /** <h4>
     * Runs a python script at the given path. Will return output line-by-line as LinkedList
     * * </h4>*/
    public LinkedList<String> runScriptGetLinkedListOutput(){
        ProcessBuilder pBuilder = new ProcessBuilder("python",
                path, getParamsForPy());

        pBuilder.redirectErrorStream(true);
        try{
            Process pyProcess = pBuilder.start();
            BufferedReader pyOut = new BufferedReader(new InputStreamReader(pyProcess.getInputStream()));
            BufferedReader pyErr = new BufferedReader(new InputStreamReader(pyProcess.getErrorStream()));

            String out;
            LinkedList<String> pyResults = new LinkedList<>();

            while((out = pyOut.readLine()) != null){
                System.out.println(out);
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
                path, getParamsForPy());

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
                path, getParamsForPy());

        pBuilder.redirectErrorStream(true);
        try{
            Process pyProcess = pBuilder.start();
            BufferedReader pyOut = new BufferedReader(new InputStreamReader(pyProcess.getInputStream()));
            BufferedReader pyErr = new BufferedReader(new InputStreamReader(pyProcess.getErrorStream()));

            String out;
            LinkedList<String> pyResults = new LinkedList<>();

            while((out = pyOut.readLine()) != null){
                pyResults.add(out);
                System.out.println(out);

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




    public void runScriptDisplayOutput(){
        ProcessBuilder pBuilder = new ProcessBuilder("python",
                path, getParamsForPy());

        pBuilder.redirectErrorStream(true);
        try{
            Process pyProcess = pBuilder.start();
            BufferedReader pyOut = new BufferedReader(new InputStreamReader(pyProcess.getInputStream()));
            BufferedReader pyErr = new BufferedReader(new InputStreamReader(pyProcess.getErrorStream()));

            String out;

            while((out = pyOut.readLine()) != null){
                System.out.println(">>>\t" + out);
            }

        }catch(Exception e){
            System.out.println("\n[ CRITICAL - Exception Caught ]");
            e.printStackTrace();
        }

    }



}
