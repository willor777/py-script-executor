INSTRUCTIONS...


- All py components need to be in the same dir i believe.
- Create the ScriptExecutor class
- Add params using .addParam(String key, String val)
- On python side -- 
	- I have included a example class that is set up to load the params from Java. Or use sys.argv
	- Any output needs to be printed to the console. - Be careful not to leave random print()s in your code
	
- There are 3 options to run the script depending on the desired Java side output	
- runScriptDisplayOutput() - Displays every line python sends out in the console
- runScriptGetLinkedListOutput() - Returns output Line - by - Line as it's printed from the console python side
- runScriptGetSingleMap() - Retrieves a single json string from py out and converts to hashmap<string,string>
- runScriptGetMultiMaps() - Retrives multiple json strings from py out and converts them to hashmap<string,string> then
adding them to a linked list
