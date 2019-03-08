package com.yld.jsoup.controller;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.poifs.filesystem.EntryUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yld
 * @Date: 2019-03-08 09:54
 * @Version 1.0
 */
@Controller
@RequestMapping("/excel")
public class ExcelController {

    @PostMapping("/upload")
    public void uploadExcel(HttpServletResponse response, @RequestParam("file") MultipartFile file){
        /**
         * 1. 获取上传的url列表
         * 2. 遍历获取url对应页面的HTML源码
         * 3. 提取对应的商品信息字段
         * 4. 输出的excel
         */
        try {
            //读取excel文件
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            //读取excel工作表
            XSSFSheet sheet = workbook.getSheetAt(0);

            //创建输出excel
            XSSFWorkbook newWorkbook  = new XSSFWorkbook();
            //创建sheet
            XSSFSheet newSheet = newWorkbook.createSheet();
            //创建标题行
            XSSFRow titleRow = newSheet.createRow(0);
            //设置标题行
            XSSFCell cell1 = titleRow.createCell(0, STCellType.INT_STR);
            cell1.setCellValue("商品编码");
            XSSFCell cell2 = titleRow.createCell(1, STCellType.INT_STR);
            cell2.setCellValue("商品名称");
            XSSFCell cell3 = titleRow.createCell(2, STCellType.INT_STR);
            cell3.setCellValue("商品分类");
            //设置宽度
            newSheet.setColumnWidth(0,2560);
            newSheet.setColumnWidth(1,25600);
            newSheet.setColumnWidth(2,5210);

            //遍历获取html代码,提取信息
            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                //获取行
                XSSFRow row = sheet.getRow(i);
                //获取列
                XSSFCell cell = row.getCell(0);
                String url = cell.getStringCellValue();

                //输出excel创建行
                XSSFRow newRow = newSheet.createRow(i + 1);
                //判断url不为空并且包含http
                if (!url.isEmpty() && url.contains("http")){
                    //获取商品信息集合
                    Map<String, String> data = getProductInfo(url);
                    //输出商品信息到excel表
                    if (data != null) {
                        XSSFCell cellOne = newRow.createCell(0, STCellType.INT_STR);
                        cellOne.setCellValue(data.get("sku"));
                        XSSFCell cellTwo = newRow.createCell(1, STCellType.INT_STR);
                        cellTwo.setCellValue(data.get("name"));
                        XSSFCell cellThree = newRow.createCell(2, STCellType.INT_STR);
                        cellThree.setCellValue(data.get("cat"));
                    }
                }
                //调试打印
                System.out.println("\n内容是：" + url);
            }
            // 下载excel
            response.setContentType("application/octet-stream");
            // 以时间戳命名
            String fileName = String.valueOf(System.currentTimeMillis()) + ".xlsx";
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            response.flushBuffer();

            // 输出excel
            newWorkbook.write(response.getOutputStream());

        }catch (Exception e){


        }
    };

    /**
     * 提取商品信息
     */
    public Map<String,String> getProductInfo(String url) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        //模拟浏览器浏览
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:60.0) Gecko/20100101 Firefox/60.0");
        CloseableHttpResponse response1 = httpClient.execute(httpGet);

        //定义结果集合
        Map<String,String> result = null;
        //获取响应的状态码
        int statusCode = response1.getStatusLine().getStatusCode();
        try {
            HttpEntity entity1 = response1.getEntity();
            //如果状态吗是200，则获取html实体内容或者json文件
            if (statusCode == 200){
                String html = EntityUtils.toString(entity1, Consts.UTF_8);
                //提取html获取商品信息
                result = getData(html);
                // 消耗掉实体
                EntityUtils.consume(response1.getEntity());
            }else {
                //否则，消耗掉实体
                EntityUtils.consume(response1.getEntity());
            }
        }catch (Exception e){

        }finally {
            response1.close();
        }
        return result;
    }
    //提取html获取商品的信息
    private static Map<String,String>getData(String html) throws  Exception{
        //获取的数据，放到集合中
        HashMap<String, String> data = new HashMap<>();

        //采用jsoup解析
        Document doc = Jsoup.parse(html);

        //获取html标签中的内容
        String name = doc.select("div[class=sku-name]").text();
        if (name != null){
            data.put("name",name);
        }
        //sku
        String sku = "";
        Elements elements = doc.select("a[data-sku]");
        for (Element element :elements){
            if (element.hasAttr("data-sku")){
                element.attr("data-sku");
                break;
            }
        }
        if (sku != null) {
            data.put("sku", sku);
        }

        String cat = doc.select("a[clstag=shangpin|keycount|product|mbNav-1]").text();

        if (cat != null) {
            data.put("cat", cat);
        }
        System.out.print(sku + "---------" + cat + "---------" + name);
        //返回数据
        return data;
    }
}
