package us.codecraft.webmagic.tools;


import au.com.bytecode.opencsv.CSVWriter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class excelRead {

    private static final String FILE_NAME = "/Users/william/spider/products.xlsx";
    private static final String FullCmpe_FILE_NAME = "/Users/william/spider/FullCmpe.xlsx";
    private static final String Ben_FILE_NAME = "/Users/william/spider/Ben.xlsx";
    private static final String ProductInfo_FILE_NAME = "/Users/william/spider/productInfo.xlsx";




    public static List<ProductInfo> readFile(String filename){

        List<ProductInfo> returnList=new ArrayList<ProductInfo>();
        try {

            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
            XSSFWorkbook wb = new XSSFWorkbook(new BufferedInputStream(excelFile));
            XSSFSheet sheet = wb.getSheetAt(0); //获取第一张表

            int rowNum = sheet.getPhysicalNumberOfRows();//得到数据的行数
            System.out.println("行数：" + rowNum);

            //遍历行
            for (int i = 0; i < rowNum; i++)
            {
                ProductInfo productInfo=new ProductInfo();
                List<String> strList = new ArrayList<String>();
                XSSFRow row = sheet.getRow(i);
                int colNum = row.getPhysicalNumberOfCells();//得到当前行中存在数据的列数
                //遍历列
                productInfo.setName(getXCellVal(row.getCell(0)));
                if(row.getCell(1)!=null&&!row.getCell(1).toString().equals("")) {
                    productInfo.setGTIN(getXCellVal(row.getCell(1)));
                }else{
                    productInfo.setGTIN("0");
                }
                returnList.add(productInfo);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnList;
    }
public static List<List<Store>> readCompetitorFile(String filename){

        List<List<Store>> returnList=new ArrayList<List<Store>>();
        try {

            FileInputStream excelFile = new FileInputStream(new File(FullCmpe_FILE_NAME));
            XSSFWorkbook wb = new XSSFWorkbook(new BufferedInputStream(excelFile));
            XSSFSheet sheet = wb.getSheetAt(0); //获取第一张表

            int rowNum = sheet.getPhysicalNumberOfRows();//得到数据的行数
            System.out.println("行数：" + rowNum);

            //遍历行
            for (int i = 0; i < rowNum; i++)
            {

                ProductInfo productInfo=new ProductInfo();
                List<Store> stores=new ArrayList<Store>();
                List<String> strList = new ArrayList<String>();
                XSSFRow row = sheet.getRow(i);
                int colNum = row.getPhysicalNumberOfCells();//得到当前行中存在数据的列数
                //遍历列
                //productInfo.setName(getXCellVal(row.getCell(0)));
                System.out.println("colNum="+colNum);
                System.out.println("i="+i);
                for(int j=2;j<colNum;){
                    System.out.println("j="+j);
                    Store store=new Store();
                    store.setProductName(getXCellValNoFormat(row.getCell(0)));
                    store.setProductUPC(getXCellValNoFormat(row.getCell(1)));
                    store.setStoreName(getXCellValNoFormat(row.getCell(j)));
                    if(row.getCell(j+3)==null||getXCellValNoFormat(row.getCell(j+3)).equals("")){
                        store.setPriceTotal("0");
                    }else{
                        store.setPriceTotal(getXCellValNoFormat(row.getCell(j+1)));
                    }

                    if(row.getCell(j+3)==null||getXCellValNoFormat(row.getCell(j+3)).equals("Free shipping")||getXCellValNoFormat(row.getCell(j+3)).equals("")){
                        store.setShippingOfDetails("0");
                    }else{
                        store.setShippingOfDetails(getXCellValNoFormat(row.getCell(j+3)));
                    }

                    if(row.getCell(j+3)==null||getXCellValNoFormat(row.getCell(j+5)).equals("No tax")||getXCellValNoFormat(row.getCell(j+3)).equals("")){
                        store.setTax("0");
                    }else{
                        store.setTax(getXCellValNoFormat(row.getCell(j+5)));
                    }

                    store.setProductPrice(getXCellValNoFormat(row.getCell(j+7)));
                    BigDecimal priceWithOutTax=new BigDecimal(store.getShippingOfDetails()).add(new BigDecimal(store.getProductPrice()));
                    store.setTotalWithoutTax(priceWithOutTax.toString());
                    stores.add(store);
                    j=j+8;
                }
                returnList.add(stores);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnList;
    }


    public static String getXCellVal(XSSFCell cell) {
         String           val = null;
         SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); //日期格式yyyy-mm-dd
         DecimalFormat    df = new DecimalFormat("0");

        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    val = fmt.format(cell.getDateCellValue()); //日期型
                } else {
                    val = df.format(cell.getNumericCellValue()); //数字型
                }
                break;
            case XSSFCell.CELL_TYPE_STRING: //文本类型
                val = cell.getStringCellValue();
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN: //布尔型
                val = String.valueOf(cell.getBooleanCellValue());
                break;
            case XSSFCell.CELL_TYPE_BLANK: //空白
                val = cell.getStringCellValue();
                break;
            case XSSFCell.CELL_TYPE_ERROR: //错误
                val = "错误";
                break;
            case XSSFCell.CELL_TYPE_FORMULA: //公式
                try {
                    val = String.valueOf(cell.getStringCellValue());
                } catch (IllegalStateException e) {
                    val = String.valueOf(cell.getNumericCellValue());
                }
                break;
            default:
                val = cell.getRichStringCellValue() == null ? null : cell.getRichStringCellValue().toString();
        }
        return val;
    }

    public static String getXCellValNoFormat(XSSFCell cell) {
        String           val = null;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); //日期格式yyyy-mm-dd
        DecimalFormat    df = new DecimalFormat(".##");

        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    val = fmt.format(cell.getDateCellValue()); //日期型
                } else {
                    val = df.format(cell.getNumericCellValue()); //数字型
                }
                break;
            case XSSFCell.CELL_TYPE_STRING: //文本类型
                val = cell.getStringCellValue();
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN: //布尔型
                val = String.valueOf(cell.getBooleanCellValue());
                break;
            case XSSFCell.CELL_TYPE_BLANK: //空白
                val = cell.getStringCellValue();
                break;
            case XSSFCell.CELL_TYPE_ERROR: //错误
                val = "错误";
                break;
            case XSSFCell.CELL_TYPE_FORMULA: //公式
                try {
                    val = String.valueOf(cell.getStringCellValue());
                } catch (IllegalStateException e) {
                    val = String.valueOf(cell.getNumericCellValue());
                }
                break;
            default:
                val = cell.getRichStringCellValue() == null ? null : cell.getRichStringCellValue().toString();
        }
        return val;
    }
    public static void writeToLawFormat(){
        List<List<Store>> listsStores=excelRead.readCompetitorFile(FullCmpe_FILE_NAME);

        String csvFile = "/Users/william/spider/competitorLAW.csv";
        FileWriter writer = null;
        //title
        List<String> title=new ArrayList<String>();
        title.add("productName");
        title.add("UPC");
        title.add("zenhydro-Total");
        title.add("zenhydro-TotalWithoutTax");
        title.add("zenhydro-Shipping");
        title.add("zenhydro-ProductPrice");
        title.add("zenhydro-Tax");
        for(int p=1;p<=3;p++){
            title.add("Competitor"+p+"-Store Name");
            title.add("Competitor"+p+"-Total");
            title.add("Competitor"+p+"-TotalWithoutTax");
            title.add("Competitor"+p+"-Shipping");
            title.add("Competitor"+p+"-ProductPrice");
            title.add("Competitor"+p+"-Tax");
        }
        String[] titleArr=new String[title.size()];
        titleArr=title.toArray(titleArr);
        try {
            CSVWriter writerCSV = new CSVWriter(new FileWriter(csvFile));
            writerCSV.writeNext(titleArr);
            writerCSV.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("listsStores.size()"+listsStores.size());
        List<Double> priceComp=new ArrayList<Double>();
        for(int i=0;i<listsStores.size();i++){

            List<String> storeNameList=new ArrayList<String>();
            List<Store> zen=new ArrayList<Store>();

            //find out zenhydro
            for(int q=0;q<listsStores.get(i).size();q++){
                if(listsStores.get(i).get(q).getStoreName().trim().equals("Total - Zenhydro Hydroponics")){
                    zen.add(listsStores.get(i).get(q));
                    listsStores.get(i).remove(q);

                }
            }


            //listsStores.get(i);
            //添加名字 添加zenhydro
            if(zen.size()>0) {
                storeNameList.add(zen.get(0).getProductName());
                storeNameList.add(zen.get(0).getProductUPC());
                storeNameList.add(zen.get(0).getPriceTotal());
                storeNameList.add(zen.get(0).getTotalWithoutTax());
                storeNameList.add(zen.get(0).getShippingOfDetails());
                storeNameList.add(zen.get(0).getProductPrice());
                storeNameList.add(zen.get(0).getTax());
            }else{
                continue;
            }




            //从小到大排序 totalWithoutTax
            Collections.sort(listsStores.get(i), new Comparator<Store>() {
                public int compare(Store s1, Store s2) {
                    return Double.compare(Double.parseDouble(s1.getTotalWithoutTax()),Double.parseDouble(s2.getTotalWithoutTax()));
                }
            });

            //最少三个
            if(listsStores.get(i).size()>=3) {
                for (int k = 0; k < 3; k++) {
                    storeNameList.add(listsStores.get(i).get(k).getStoreName().substring(7,listsStores.get(i).get(k).getStoreName().length()));
                    storeNameList.add(listsStores.get(i).get(k).getPriceTotal());
                    storeNameList.add(listsStores.get(i).get(k).getTotalWithoutTax());
                    storeNameList.add(listsStores.get(i).get(k).getShippingOfDetails());
                    storeNameList.add(listsStores.get(i).get(k).getProductPrice());
                    storeNameList.add(listsStores.get(i).get(k).getTax());
                }
            }else{
                for(int j=0;j<listsStores.get(i).size();j++){
                    storeNameList.add(listsStores.get(i).get(j).getStoreName().substring(7,listsStores.get(i).get(j).getStoreName().length()));
                    storeNameList.add(listsStores.get(i).get(j).getPriceTotal());
                    storeNameList.add(listsStores.get(i).get(j).getTotalWithoutTax());
                    storeNameList.add(listsStores.get(i).get(j).getShippingOfDetails());
                    storeNameList.add(listsStores.get(i).get(j).getProductPrice());
                    storeNameList.add(listsStores.get(i).get(j).getTax());
                }
            }
            String[] itemArr=new String[storeNameList.size()];
            itemArr=storeNameList.toArray(itemArr);
            try {
                CSVWriter writerCSV = new CSVWriter(new FileWriter(csvFile,true));
                writerCSV.writeNext(itemArr);
                writerCSV.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("listsStores.size()222"+listsStores.size());

    }

    public static List<ProductInfo> readLAWFileToWriteBenFile(String filename){
        List<ProductInfo> productInfos=new ArrayList<ProductInfo>();
        List<ProductInfo> productOfOurs=new ArrayList<ProductInfo>();
        List<ProductInfo> productOfResult=new ArrayList<ProductInfo>();


        try {

            FileInputStream excelFile = new FileInputStream(new File(FullCmpe_FILE_NAME));
            FileInputStream productInfo = new FileInputStream(new File(ProductInfo_FILE_NAME));
            XSSFWorkbook wb = new XSSFWorkbook(new BufferedInputStream(excelFile));
            XSSFWorkbook productwb = new XSSFWorkbook(new BufferedInputStream(productInfo));

            //获取所有的upc name
            XSSFSheet sheet = wb.getSheetAt(0); //获取第一张表
            int rowNum = sheet.getPhysicalNumberOfRows();//得到数据的行数
            //遍历行
            for (int i = 0; i < rowNum; i++)
            {

                ProductInfo productOfGoogle=new ProductInfo();
                XSSFRow row = sheet.getRow(i);
                int colNum = row.getPhysicalNumberOfCells();//得到当前行中存在数据的列数
                //遍历列
                productOfGoogle.setName(getXCellVal(row.getCell(0)));
                productOfGoogle.setGTIN(getXCellVal(row.getCell(1)));

                productInfos.add(productOfGoogle);

            }

            //获取我们公司所有的upc name sku
            XSSFSheet productsheet = productwb.getSheetAt(0); //获取第一张表
            int productSheetRowNum = productsheet.getPhysicalNumberOfRows();//得到数据的行数
            //遍历行
            for (int j = 0; j < productSheetRowNum; j++)
            {

                ProductInfo productOfOur=new ProductInfo();
                XSSFRow productRow = productsheet.getRow(j);
                int productColNum = productRow.getPhysicalNumberOfCells();//得到当前行中存在数据的列数
                //遍历列获得 sku 和
                if(productRow.getCell(5)==null || productRow.getCell(22)==null) {
                    continue;
                }
                productOfOur.setSKU(getXCellVal(productRow.getCell(5)));
                productOfOur.setGTIN(getXCellVal(productRow.getCell(22)));


                productOfOurs.add(productOfOur);

            }
            int j=0;
            for (ProductInfo p:productInfos
                 ) {
                try {
                    //在总表里面找需要的数据
                    FSearchTool tool = new FSearchTool(productOfOurs, "SKU", "GTIN");
                    List<Object> productInfoList=new ArrayList<Object>();
                    productInfoList= tool.searchTasks(p.getGTIN());

                   // System.out.println(productInfoList.size());
                    if(productInfoList.size()>1){

                        System.out.println(p.getGTIN());
                        System.out.println(p.getSKU());
                        System.out.println(j++);

                    }else if(productInfoList.size()==1){
                        ProductInfo productInfo1 = new ProductInfo();
                        productInfo1.setName(((ProductInfo) productInfoList.get(0)).getName());
                        productInfo1.setSKU(((ProductInfo) productInfoList.get(0)).getSKU());
                        productInfo1.setGTIN(((ProductInfo) productInfoList.get(0)).getGTIN());
                        productOfResult.add(productInfo1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println(productOfResult.size());

            productOfOurs.size();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return productInfos;
    }


    public static void main(String[] args) {
        //List<ProductInfo> re=excelRead.readFile(FILE_NAME);
        //List<List<Store>> ls=excelRead.readCompetitorFile(FullCmpe_FILE_NAME);
        //excelRead.writeToLawFormat();
        List<ProductInfo> productInfos=excelRead.readLAWFileToWriteBenFile(FullCmpe_FILE_NAME);
        System.out.println("");
    }
}