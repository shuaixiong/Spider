package us.codecraft.webmagic.tools;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpSamp {
    public static void main(String[] args) throws IOException {
        System.out.println(HttpSamp.acquireIDs());
    }

    public static  List<CompareForm>  acquireIDs() throws IOException {
        List<ProductInfo> productName=new ArrayList<ProductInfo>();
        List<CompareForm> compareForms=new ArrayList<CompareForm>();
        //read name from excel file
        String FILE_NAME="/Users/william/spider/products.xlsx";
        productName.addAll(excelRead.readFile(FILE_NAME));

        //get id from souce code 2917 3065
//        String sks="AN Jungle Juice Micro 23L,GTIN 845268005660 zenhydro";productName.size()
        for(int i=4471;i<productName.size();i++){
            CompareForm compareForm=new CompareForm();
            compareForm.setOrgName(productName.get(i).getName());
            try {
                Thread.sleep( 1000);
                System.out.println("程序调整为减慢1秒"+i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String sk=productName.get(i).getName();


            //method 1
            sk=sk+" zenhydro";
            String sd;
            sd = URLEncoder.encode(sk,"UTF-8");
            String m="1";
            CompareForm compareFormT=new CompareForm();
            System.out.println("尝试获得方法一数据");
            compareFormT=HttpSamp.GetResultFromG(productName.get(i),compareForm,sd,m);
            compareForm.setM1Name(compareFormT.getM1Name());
            compareForm.setM1ProductID(compareFormT.getM1ProductID());

            //method 2
            m="2";
            String sk2=productName.get(i).getName()+" GTIN "+productName.get(i).getGTIN()+" zenhydro";
            sd=URLEncoder.encode(sk2, "UTF-8");
            System.out.println("尝试获得方法二数据");

            compareFormT=HttpSamp.GetResultFromG(productName.get(i),compareForm,sd,m);
            compareForm.setM2Name(compareFormT.getM2Name());
            compareForm.setM2ProductID(compareFormT.getM2ProductID());



            if(compareForm.getM1Name()!=null &&compareForm.getM2Name()!=null) {
                Levenshtein lt = new Levenshtein();
                System.out.println("调整数据精准度");

                //System.out.println("similarityRatio index " + i +" ->"+ lt.getSimilarityRatio(compareForm.getM1Name(), compareForm.getOrgName()) + " " + lt.getSimilarityRatio(compareForm.getM2Name(), compareForm.getOrgName()));
                compareForm.setSimilarityScoreM1(lt.getSimilarityRatio(compareForm.getM1Name(), compareForm.getOrgName()));
                compareForm.setSimilarityScoreM2(lt.getSimilarityRatio(compareForm.getM2Name(), compareForm.getOrgName()));

                if(compareForm.getSimilarityScoreM1()>=compareForm.getSimilarityScoreM2()){
                    compareForm.setFlatM("1");
                }else{
                    compareForm.setFlatM("2");
                }
            }
            writeToCsv(compareForm);
           // compareForms.add(compareForm);
            System.out.println("成功获得ID,写入文件"+i);
        }
       // HttpSamp.writeToCsv(compareForms);
       // System.out.println(compareForms);

        return compareForms;

    }

    private static void writeToCsv(CompareForm compareFormList) throws IOException {


        String csvFileID = "/Users/william/spider/competitorsOfIDS.csv";

        CSVWriter writerID = new CSVWriter(new FileWriter(csvFileID,true));
        List<String[]> idAll=new ArrayList<String[]>();


            String[] itemID=new String[1];
            if(compareFormList.getFlatM().equals("1")){
                compareFormList.getM1Name();
                compareFormList.getM1ProductID();
                compareFormList.getSimilarityScoreM1().toString();
                if( compareFormList.getM1ProductID().toString()!=null&&! compareFormList.getM1ProductID().toString().trim().equals("")&& compareFormList.getSimilarityScoreM1()>0.2){
                    itemID[0]= compareFormList.getM1ProductID();
                }
            }else{
                compareFormList.getM2Name();
                compareFormList.getM2ProductID();
                compareFormList.getSimilarityScoreM2().toString();
                if( compareFormList.getM2ProductID().toString()!=null&&! compareFormList.getM2ProductID().toString().trim().equals("")&& compareFormList.getSimilarityScoreM2()>0.2){
                    itemID[0]= compareFormList.getM2ProductID();
                }
            }
            //list of all info 添加每一行
            String[] itemArr=new String[1];
            itemArr[0]=itemID[0];

             writerID.writeNext(itemArr);
             writerID.close();


    }


    private static void writeToCsv(List<CompareForm> compareFormList) throws IOException {

        String csvFile = "/Users/william/spider/compareFormList.csv";
        String csvFileID = "/Users/william/spider/competitorsOfIDS.csv";

        CSVWriter writer = new CSVWriter(new FileWriter(csvFile));
        CSVWriter writerID = new CSVWriter(new FileWriter(csvFileID));
        List<String[]> dataAll=new ArrayList<String[]>();
        List<String[]> idAll=new ArrayList<String[]>();

        for(int i=0;i<compareFormList.size();i++){
            List<String> itemsNameList=new ArrayList<String>();
            String[] itemID=new String[1];
            if(compareFormList.get(i).getFlatM().equals("1")){
                itemsNameList.add(compareFormList.get(i).getM1Name());
                itemsNameList.add(compareFormList.get(i).getM1ProductID());
                itemsNameList.add(compareFormList.get(i).getSimilarityScoreM1().toString());
                if(compareFormList.get(i).getM1ProductID().toString()!=null&&!compareFormList.get(i).getM1ProductID().toString().trim().equals("")&&compareFormList.get(i).getSimilarityScoreM1()>0.2){
                    itemID[0]=compareFormList.get(i).getM1ProductID();
                }
            }else{
                itemsNameList.add(compareFormList.get(i).getM2Name());
                itemsNameList.add(compareFormList.get(i).getM2ProductID());
                itemsNameList.add(compareFormList.get(i).getSimilarityScoreM2().toString());
                if(compareFormList.get(i).getM2ProductID().toString()!=null&&!compareFormList.get(i).getM2ProductID().toString().trim().equals("")&&compareFormList.get(i).getSimilarityScoreM2()>0.2){
                    itemID[0]=compareFormList.get(i).getM2ProductID();
                }
            }
            //list of all info 添加每一行
            String[] itemArr=new String[itemsNameList.size()];
            itemArr=itemsNameList.toArray(itemArr);
            dataAll.add(itemArr);

            //listOfID id 另外一个表存id
            idAll.add(itemID);

        }
         writer.writeAll(dataAll);
         writerID.writeAll(idAll);
         writer.close();
         writerID.close();


    }




    private static CompareForm GetResultFromG(ProductInfo productInfo,CompareForm compareForm,String key,String m) throws IOException {
        //抓取id google
        try {
            Thread.sleep( 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("开始抓取网页");

        Document doc = Jsoup.connect("https://www.google.com/search?safe=strict&biw=1021&bih=897&tbm=shop&ei=IhEWWp_7LOHf0gKI2bXIDw&btnG=Search&q="+key)
                .userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                .timeout(5000).get();

        try {
            Thread.sleep( 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(doc.select(".pstl")!=null&&doc.select(".pstl").size()>0&&doc.select(".pstl").toString().contains("?safe=")&&doc.select(".pstl").toString().contains("/shopping/product/")) {
                String str = doc.select(".pstl").toString();
            System.out.println("获得网页回复数据，开始分析");
                Integer StartStr = str.indexOf("/shopping/product/");
                Integer EndStr = str.indexOf("?safe=");
                String strName = str.substring(StartStr + 18, EndStr);

                String nameFromGoogle = str.substring(str.indexOf("\"spop.c\">") + 9, str.indexOf("</a>"));

                productInfo.setProductId(strName);
                if (m.equals("1")) {
                    compareForm.setM1Name(nameFromGoogle);
                    compareForm.setM1ProductID(productInfo.getProductId());
                   // System.out.println(key + "success->" + nameFromGoogle + " " + productInfo.getProductId());
                } else {
                    compareForm.setM2Name(nameFromGoogle);
                    compareForm.setM2ProductID(productInfo.getProductId());
                   // System.out.println(key + "success->" + nameFromGoogle + " " + productInfo.getProductId());
                }

        }else{
            if(m.equals("1")){
                compareForm.setM1Name("fail");
                compareForm.setM1ProductID("fail");
            }else{
                compareForm.setM2Name("fail");
                compareForm.setM2ProductID("fail");
            }
        }

        return compareForm;
    }



}
