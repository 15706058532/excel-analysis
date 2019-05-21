# excel-analysis

1. **目的**

   解决java在解析大数据量excel时出现OOM的问题

2. **使用方式**

   1. 将LzfExcelUrils类和LzfExcelException类引入到项目

   2. 单独解析.xls后缀的文件，返回Workbook

      ```java
       LzfExcelUrils lzfExcelUrils = new LzfExcelUrils();
              //小文件
              String filePath = "E:\\ideaProjects\\excel-analysis\\src\\main\\resources\\file\\测试-老版本-副本.xls";
              Workbook wb = lzfExcelUrils.xls(filePath);
              
      ```

   3. 单独解析.xlsx后缀的文件，返回Workbook

      ```java
      LzfExcelUrils lzfExcelUrils = new LzfExcelUrils();
              //大文件100+M
              String filePath = "E:\\ideaProjects\\excel-analysis\\src\\main\\resources\\file\\测试-新版本.xlsx";
              Workbook wb = lzfExcelUrils.xlsx(filePath);
      ```

   4. 自动检测文件类型解析，返回Workbook

      ```java
      LzfExcelUrils lzfExcelUrils = new LzfExcelUrils();
      String filePath = "E:\\ideaProjects\\excel-analysis\\src\\main\\resources\\file\\测试-老版本-副本.xls";
              Workbook wb = lzfExcelUrils.getWorkbook(filePath);
       filePath = "E:\\ideaProjects\\excel-analysis\\src\\main\\resources\\file\\测试-新版本.xlsx";
              start = System.currentTimeMillis();
              wb = lzfExcelUrils.getWorkbook(filePath);
      ```

   **注**：后续的操作就和之前的PIO操作相同，我这里就不累述了。

