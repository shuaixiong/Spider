package us.codecraft.webmagic.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class CSVReader {

    public static List<String> ReadProductId(String path){

        String csvFile = "/Users/william/spider/competitorsOfIDS.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        List<String> ids=new ArrayList<String>();
        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);
                if(country[0]!=null&&!country[0].trim().toString().equals("")){
                    if(country[0].indexOf("\"")==0) country[0] = country[0].substring(1,country[0].length());   //去掉第一个 "
                    if(country[0].lastIndexOf("\"")==(country[0].length()-1)) country[0] = country[0].substring(0,country[0].length()-1);  //去掉最后一个 "
                }
                ids.add(country[0]);
                //System.out.println(country[0]);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        HashSet h = new HashSet(ids);
        ids.clear();
        ids.addAll(h);
        System.out.println(ids.size());
        return ids;

    }

    public static void main(String[] args) throws FileNotFoundException {

        // List<String> dfs=CSVReader.ReadProductId("/Users/william/spider/abc.csv");
 //      au.com.bytecode.opencsv.CSVReader reader = new CSVReader(new FileReader("competitorsOfIDS.csv"), ',', '"', 1);
//
//        //Read CSV line by line and use the string array as you want
//        String[] nextLine;
//        while ((nextLine = reader.readNext()) != null) {
//            if (nextLine != null) {
//                //Verifying the read data here
//                System.out.println(Arrays.toString(nextLine));
//            }
//        }
    }
}
