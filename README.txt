INSTRUCTIONS...

- All py components need to be in the same dir i believe.
- Create the ScriptExecutor class
- Add params using .addParam(String key, String val)
- On python side -- 
	- If you want to use kwargs in python set a var = json.loads(sys.argv[-1])
	- Any output needs to be printed to the console.
- There are 3 options to run the script depending on the desired Java side output	
	- runScriptGetLinkedListOutput() - Returns output Line - by - Line as it's printed from the console python side
	- runScriptGetSingleMap() - Retrieves a single json string from py out and converts to hashmap<string,string>
	- runScriptGetMultiMaps() - Retrives multiple json strings from py out and converts them to hashmap<string,string> then
		adding them to a linked list