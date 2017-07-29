# JsonParser

JsonObject.java - This contains the class for JsonObjects that we'll get from the JSON file after parsing. It has an instance variable, jsonMap, which contains the JSON objects in key:value pair.

JsonParser.java - This files contains the JsonParser class. It has the parse() function which takes a JSON file as input, parse it and returns it as a JsonObject. This function can also be used to validate a JSON file. It returns null if the input file is invalid..

TestJsonParser.java - This contains the tester class TestJsonParser, which contains main(). I am using "testschema.json" as a test input file and calling parse() function from JsonParser.
