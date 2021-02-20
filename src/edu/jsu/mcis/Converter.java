/*
  Jayden Duncan
  CS 310-002
  Project #2A
  Dr. Snellen
*/

package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results;
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            // INSERT YOUR CODE HERE
            String[] csvColHeaders = iterator.next();
            String[] headings = {"rowHeaders", "data", "colHeaders"};
            
            JSONArray records = new JSONArray();
            LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<>();
            String[] record;
            ArrayList<String> rowHeaders = new ArrayList<>();
            ArrayList<ArrayList> data = new ArrayList<>();
            ArrayList<String> columnHeaders = new ArrayList<>();
            
            for(String c : csvColHeaders){
                columnHeaders.add(c);
            }
            
            while(iterator.hasNext()){
                ArrayList<Integer> scores = new ArrayList<>();
                record = iterator.next();
                rowHeaders.add(record[0]);
                scores.add(Integer.parseInt(record[1]));
                scores.add(Integer.parseInt(record[2]));
                scores.add(Integer.parseInt(record[3]));
                scores.add(Integer.parseInt(record[4]));
                data.add(scores);
            }
            
            jsonObject.put(headings[0], rowHeaders);
            jsonObject.put(headings[1], data);
            jsonObject.put(headings[2], columnHeaders);
            
            results = JSONValue.toJSONString(jsonObject);
        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            // INSERT YOUR CODE HERE
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
            JSONArray colHeaderArray = (JSONArray)jsonObject.get("colHeaders"); //contains list of column headers
            JSONArray rowHeaderArray = (JSONArray)jsonObject.get("rowHeaders"); //contains list of row headers
            JSONArray dataArray = (JSONArray)jsonObject.get("data"); // contains the list of data arrays
            
            //Store column headers and add to CSVWriter
            String[] colHeaders = new String[colHeaderArray.size()];
            for(int i=0; i<colHeaderArray.size(); i++){
                colHeaders[i] = colHeaderArray.get(i).toString();
            }
            csvWriter.writeNext(colHeaders);
            
            //Store row headers
            String[] rowHeaders = new String[rowHeaderArray.size()];
            for(int i=0; i<rowHeaderArray.size(); i++){
                rowHeaders[i] = rowHeaderArray.get(i).toString();
            }
            
            //Store row data and write each line to CSVWriter
            ArrayList<Object> dataAL = new ArrayList<>(); // contains each data array (as an Object)
            for(Object d : dataArray){
                dataAL.add(d);
            }
            
            //loop through each row and add respective data to the csv writer
            for(int i=0; i<dataAL.size(); i++){
                String[] rowData = new String[5]; // contains row data for csv writer
                JSONArray grades = (JSONArray)dataAL.get(i); // converts each array of grades into a json array
                ArrayList<String> line = new ArrayList<>(); // holds each data field for the row
                line.add(rowHeaders[i]); // adds the subsequent row header
                
                // add each grade to the ArrayList as a string value
                for(int j=0; j<grades.size(); j++){
                    String grade = grades.get(j).toString();
                    
                    line.add(grade);
                }
                
                // add each ArrayList element to the corresponding spot in the string array
                for(int k=0; k<line.size(); k++){
                    rowData[k] = line.get(k);
                }
                
                csvWriter.writeNext(rowData);
                
            }
            
            String csvString = writer.toString();
            results = csvString;
            
        }
        
        catch(Exception e) { return e.toString(); } 
        
        return results.trim();
        
    }

}