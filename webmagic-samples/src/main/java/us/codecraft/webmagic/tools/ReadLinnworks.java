package us.codecraft.webmagic.tools;

import au.com.bytecode.opencsv.CSVWriter;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import us.codecraft.webmagic.tools.domain.Product;
import us.codecraft.webmagic.tools.domain.ProductLinnworks;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadLinnworks {
    private static final String FILE_NAME = "/Users/william/Downloads/zenhydro/dataImport/Price.xlsx";
    private static final String MagentoProducts = "/Users/william/Downloads/zenhydro/dataImport/ProductsOfMagento.xlsx";
    private static final String ListOfnotExistInMagento = "/Users/william/Downloads/zenhydro/dataImport/ListOfnotExistInMagento.csv";
    private static final String itemsOflinnworks = "/Users/william/Downloads/zenhydro/dataImport/itemsOfLinnworks.xlsx";
    private static final String needTOImportExcel = "/Users/william/Downloads/zenhydro/dataImport/needTOImportExcel.csv";

    public static List<List<String>> getSKUNotExistInMagento(String filename){
        List<ProductLinnworks> productLinnworks=new ArrayList<ProductLinnworks>();
        List<ProductInfo> productInfos=new ArrayList<ProductInfo>();
        List<ProductInfo> productOfprices=new ArrayList<ProductInfo>();
        List<List<String>> lists=new ArrayList<List<String>>();



        try {

            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
            FileInputStream excelFileM = new FileInputStream(new File(MagentoProducts));
            XSSFWorkbook wbprice = new XSSFWorkbook(new BufferedInputStream(excelFile));
            XSSFWorkbook wbM = new XSSFWorkbook(new BufferedInputStream(excelFileM));

            //获取所有的SKU
            XSSFSheet sheet = wbM.getSheetAt(0); //获取第一张表
            int rowNum = sheet.getPhysicalNumberOfRows();//得到数据的行数
            //遍历行
            for (int i = 0; i < rowNum; i++)
            {

                ProductInfo productOfM=new ProductInfo();
                XSSFRow row = sheet.getRow(i);
                int colNum = row.getPhysicalNumberOfCells();//得到当前行中存在数据的列数
                //遍历列
                productOfM.setSKU(excelRead.getXCellValNoFormat(row.getCell(5)));
                System.out.println(i);

                productInfos.add(productOfM);

            }

            //获取要搜索是否在数据库里的sku
            XSSFSheet sheetprice = wbprice.getSheetAt(0); //获取第一张表
            int productSheetRowNum = sheetprice.getPhysicalNumberOfRows();//得到数据的行数
            //遍历行
            for (int j = 0; j < productSheetRowNum; j++)
            {

                ProductInfo productOfPrice=new ProductInfo();
                XSSFRow productRow = sheetprice.getRow(j);
                int productColNum = productRow.getPhysicalNumberOfCells();//得到当前行中存在数据的列数
                //遍历列获得 sku 和
                if(productRow.getCell(0)==null) {
                    continue;
                }
                productOfPrice.setSKU(excelRead.getXCellValNoFormat(productRow.getCell(0)));
                productOfprices.add(productOfPrice);
            }

            //找到有和没有的sku
            List<String> listwithSKU=new ArrayList<String>();
            List<String> listwithoutSKU=new ArrayList<String>();
            for (ProductInfo p:productOfprices
                    ) {
                try {

                    //在总表里面找需要的数据

                    FSearchTool tool = new FSearchTool(productInfos, "SKU");
                    List<Object> productInfoList=new ArrayList<Object>();
                    productInfoList= tool.searchTasks(p.getSKU());
                    System.out.println(productInfoList.size());
                    //不存在
                    if(productInfoList.size()<1){
                        listwithoutSKU.add(p.getSKU());

                    }else{
                        System.out.println(p.getSKU());
                        listwithSKU.add(((ProductInfo)productInfoList.get(0)).getSKU());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println(listwithSKU.size());
            System.out.println(listwithoutSKU.size());
            System.out.println("~~~");
            lists.add(listwithoutSKU);
            lists.add(listwithSKU);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lists;
    }

    private static void writeToCsv() throws IOException {

        String csvFile = ListOfnotExistInMagento;
        List<List<String>> ListOfnotExist=ReadLinnworks.getSKUNotExistInMagento(FILE_NAME);

        CSVWriter writer = new CSVWriter(new FileWriter(csvFile));
        List<String[]> dataAll=new ArrayList<String[]>();

        for (String sku:
        ListOfnotExist.get(0)) {
            String[] itemArr={sku};
            writer.writeNext(itemArr);

        }
        writer.close();

    }

    private static void getDataFromMagentoExcelbyItemNotExistInMagento() throws IOException {
        FileInputStream excelFile = new FileInputStream(new File(itemsOflinnworks));
        XSSFWorkbook wblw = new XSSFWorkbook(new BufferedInputStream(excelFile));
        List<ProductLinnworks> productLWs=new ArrayList<ProductLinnworks>();


        //获取所有的SKU
        XSSFSheet sheet = wblw.getSheetAt(0); //获取第一张表
        int rowNum = sheet.getPhysicalNumberOfRows();//得到数据的行数
        //遍历行
        for (int i = 0; i < rowNum; i++)
        {

            ProductLinnworks productLinnworks=new ProductLinnworks();
            XSSFRow row = sheet.getRow(i);
            int colNum = row.getPhysicalNumberOfCells();//得到当前行中存在数据的列数
            //遍历列
            productLinnworks.setItemTitle(excelRead.getXCellValNoFormat(row.getCell(0)));
            productLinnworks.setSKU(excelRead.getXCellValNoFormat(row.getCell(1)));
            productLinnworks.setItemDescription(excelRead.getXCellValNoFormat(row.getCell(2)));
            productLinnworks.setRetailPrice(excelRead.getXCellValNoFormat(row.getCell(3)));
            productLinnworks.setWeight(excelRead.getXCellValNoFormat(row.getCell(4)));
            productLinnworks.setPurchasePrice(excelRead.getXCellValNoFormat(row.getCell(5)));
            productLinnworks.setBarcodeNumber(excelRead.getXCellValNoFormat(row.getCell(6)));
            productLinnworks.setDimHeight(excelRead.getXCellValNoFormat(row.getCell(7)));
            productLinnworks.setDimWidth(excelRead.getXCellValNoFormat(row.getCell(8)));
            productLinnworks.setDimDepth(excelRead.getXCellValNoFormat(row.getCell(9)));
            productLWs.add(productLinnworks);


        }

        //搜索出SKU
        List<List<String>> productInfos=ReadLinnworks.getSKUNotExistInMagento((FILE_NAME));
        List<ProductLinnworks> linnworks=new ArrayList<ProductLinnworks>();

        for (String SKU:productInfos.get(0)
             ) {
            for (ProductLinnworks p:productLWs
                 ) {
                if(p.getSKU().equals(SKU)){
                    linnworks.add(p);
                }
            }
        }

        String csvFile = needTOImportExcel;

        CSVWriter writer = new CSVWriter(new FileWriter(csvFile));
        List<String[]> dataAll=new ArrayList<String[]>();

        for (ProductLinnworks p:
                linnworks) {
            List<String> plist=new ArrayList<String>();
            plist.add(p.getItemTitle());
            plist.add(p.getSKU());
            plist.add(p.getItemDescription());
            plist.add(p.getRetailPrice());
            plist.add(p.getWeight());
            plist.add(p.getPurchasePrice());
            plist.add(p.getBarcodeNumber());
            plist.add(p.getDimHeight());
            plist.add(p.getDimWidth());
            plist.add(p.getDimDepth());

            //list of all info 添加每一行
            String[] itemArr=new String[plist.size()];
            itemArr=plist.toArray(itemArr);
            dataAll.add(itemArr);

        }
        writer.writeAll(dataAll);
        writer.close();

    }

    //读一列sku 返回string list

    private static List<String> getSkuFromExcel(String filename) throws IOException {
        FileInputStream excelFile = new FileInputStream(new File(filename));
        XSSFWorkbook wblw = new XSSFWorkbook(new BufferedInputStream(excelFile));
        List<String> productLWs=new ArrayList<String>();


        //获取所有的SKU
        XSSFSheet sheet = wblw.getSheetAt(0); //获取第一张表
        int rowNum = sheet.getPhysicalNumberOfRows();//得到数据的行数
        //遍历行
        for (int i = 1; i < rowNum; i++)
        {

            ProductLinnworks productLinnworks=new ProductLinnworks();
            XSSFRow row = sheet.getRow(i);
            int colNum = row.getPhysicalNumberOfCells();//得到当前行中存在数据的列数
            //遍历列
            productLWs.add(excelRead.getXCellValNoFormat(row.getCell(0)));


        }
        return productLWs;
    }

    //读products 信息 返回所有产品
    private static List<Product> getProductsFromExcel(String filename) throws IOException {
        FileInputStream excelFile = new FileInputStream(new File(filename));
        XSSFWorkbook wblw = new XSSFWorkbook(new BufferedInputStream(excelFile));
        List<Product> products=new ArrayList<Product>();


        //获取所有的SKU
        XSSFSheet sheet = wblw.getSheetAt(0); //获取第一张表
        int rowNum = sheet.getPhysicalNumberOfRows();//得到数据的行数
        //遍历行
        for (int i = 1; i < rowNum; i++) {

            Product product=new Product();
            XSSFRow row = sheet.getRow(i);
            int colNum = row.getPhysicalNumberOfCells();//得到当前行中存在数据的列数
            //遍历列
            if(row.getCell(3)!=null) {
                product.setPrice(excelRead.getXCellValNoFormat(row.getCell(3)));
            }else{
                product.setPrice("");
            }
            if(row.getCell(1)!=null) {
                product.setSKU(excelRead.getXCellValNoFormat(row.getCell(1)));
            }else{
                product.setSKU("");
            }
            products.add(product);
            //System.out.println(i);
           // System.out.println(product.getSKU());

        }
        return products;

    }

    //获取文件2中存在与文件1 sku 相同读所有数据
    private static void getRequireData(String file1,String file2,String exportFilepath) throws IOException {
        List<String> skus=ReadLinnworks.getSkuFromExcel(file1);
        List<Product> products=ReadLinnworks.getProductsFromExcel(file2);

        //搜索出SKU
        List<Product> linnworks=new ArrayList<Product>();
        List<String> withoutProduct=new ArrayList<String>();
        List<String> skuofproducts=new ArrayList<String>();

        for (String SKU:skus
                ) {
            for (Product p:products
                    ) {
                if(p.getSKU().trim().equals(SKU.trim())){
                    linnworks.add(p);
                }

            }
        }

/*if need without*/
            for (Product p:linnworks
                    ) {
                skuofproducts.add(p.getSKU());

            }
            for(String s:skus){
                if(!skuofproducts.contains(s.trim())){
                    withoutProduct.add(s);
                    System.out.println(s);
                }
            }


        String csvFile = exportFilepath;

        CSVWriter writer = new CSVWriter(new FileWriter(csvFile));
        List<String[]> dataAll=new ArrayList<String[]>();

        for (Product p:
                linnworks) {
            List<String> plist=new ArrayList<String>();
            plist.add(p.getSKU());
            plist.add(p.getPrice());

            //list of all info 添加每一行
            String[] itemArr=new String[plist.size()];
            itemArr=plist.toArray(itemArr);
            dataAll.add(itemArr);

        }
        writer.writeAll(dataAll);
        writer.close();

    }





    public static void main(String[] args) throws IOException {
        //List<ProductInfo> re=excelRead.readFile(FILE_NAME);
        //List<List<Store>> ls=excelRead.readCompetitorFile(FullCmpe_FILE_NAME);
        //excelRead.writeToLawFormat();
      //  List<List<String>> productInfos=ReadLinnworks.getSKUNotExistInMagento(FILE_NAME);
//        try {
//            ReadLinnworks.writeToCsv();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //ReadLinnworks.getDataFromMagentoExcelbyItemNotExistInMagento();
        String file1="/Users/william/Downloads/zenhydro/readSKU/file1.xlsx";
        String file2="/Users/william/Downloads/zenhydro/readSKU/linnworksexport_20171212.xlsx";
        String exportfile = "/Users/william/Downloads/zenhydro/dataupdate/data.csv";
        ReadLinnworks.getRequireData(file1,file2,exportfile);
        System.out.println("");
    }

}
