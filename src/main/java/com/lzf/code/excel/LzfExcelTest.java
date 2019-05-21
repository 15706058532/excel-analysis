package com.lzf.code.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 写点注释
 * <br/>
 * Created in 2019-05-20 23:16
 *
 * @author Zhenfeng Li
 */
@RunWith(JUnit4.class)
public class LzfExcelTest {
    private Logger logger = LoggerFactory.getLogger(LzfExcelTest.class);

    @Test
    public void testOne() throws IOException, LzfExcelException {
        LzfExcelUrils lzfExcelUrils = new LzfExcelUrils();
        //小文件
        String filePath = "E:\\ideaProjects\\excel-analysis\\src\\main\\resources\\file\\测试-老版本-副本.xls";
        long start = System.currentTimeMillis();
        Workbook wb = lzfExcelUrils.getWorkbook(filePath);
        forEach(lzfExcelUrils, wb);
        long end = System.currentTimeMillis();
        logger.debug(">>>>>>>>>>>>>耗时：{}秒", (end - start) / 1000);
        logger.debug("-------------------------------");
        //大文件100+M
        filePath = "E:\\ideaProjects\\excel-analysis\\src\\main\\resources\\file\\测试-老版本.xls";
        start = System.currentTimeMillis();
        wb = lzfExcelUrils.getWorkbook(filePath);
        forEach(lzfExcelUrils, wb);
        end = System.currentTimeMillis();
        logger.debug(">>>>>>>>>>>>>耗时：{}秒", (end - start) / 1000);
        logger.debug("-------------------------------");
        //大文件100+M
        filePath = "E:\\ideaProjects\\excel-analysis\\src\\main\\resources\\file\\测试-新版本.xlsx";
        start = System.currentTimeMillis();
        wb = lzfExcelUrils.getWorkbook(filePath);
        forEach(lzfExcelUrils, wb);
        end = System.currentTimeMillis();
        logger.debug(">>>>>>>>>>>>>耗时：{}秒", (end - start) / 1000);
        logger.debug("-------------------------------");
    }

    @Test
    public void testXls() throws IOException, LzfExcelException {
        LzfExcelUrils lzfExcelUrils = new LzfExcelUrils();
        //小文件
        String filePath = "E:\\ideaProjects\\excel-analysis\\src\\main\\resources\\file\\测试-老版本-副本.xls";
        long start = System.currentTimeMillis();
        Workbook wb = lzfExcelUrils.xls(filePath);
        forEach(lzfExcelUrils, wb);
        long end = System.currentTimeMillis();
        logger.debug(">>>>>>>>>>>>>耗时:{}秒", (end - start) / 1000);
        logger.debug("-------------------------------");
        //大文件100+M
        filePath = "E:\\ideaProjects\\excel-analysis\\src\\main\\resources\\file\\测试-老版本.xls";
        start = System.currentTimeMillis();
        wb = lzfExcelUrils.xls(new FileInputStream(filePath));
        forEach(lzfExcelUrils, wb);
        end = System.currentTimeMillis();
        logger.debug(">>>>>>>>>>>>>耗时:{}秒", (end - start) / 1000);
        logger.debug("-------------------------------");
    }

    @Test
    public void testXlsx() throws IOException, LzfExcelException {
        LzfExcelUrils lzfExcelUrils = new LzfExcelUrils();
        //大文件100+M
        String filePath = "E:\\ideaProjects\\excel-analysis\\src\\main\\resources\\file\\测试-新版本.xlsx";
        long start = System.currentTimeMillis();
        Workbook wb = lzfExcelUrils.xlsx(filePath);
        forEach(lzfExcelUrils, wb);
        long end = System.currentTimeMillis();
        logger.debug(">>>>>>>>>>>>>耗时:{}秒", (end - start) / 1000);
        logger.debug("-------------------------------");
        //大文件100+M
        filePath = "E:\\ideaProjects\\excel-analysis\\src\\main\\resources\\file\\测试-新版本.xlsx";
        start = System.currentTimeMillis();
        wb = lzfExcelUrils.xlsx(new FileInputStream(filePath));
        forEach(lzfExcelUrils, wb);
        end = System.currentTimeMillis();
        logger.debug(">>>>>>>>>>>>>耗时: {}秒", (end - start) / 1000);
        logger.debug("-------------------------------");
    }

    private void forEach(LzfExcelUrils lzfExcelUrils, Workbook wb) throws LzfExcelException {
        for (Sheet sheet : wb) {
            for (Row row : sheet) {
                for (Cell cell : row) {
                    logger.debug("{}行{}列 值：{}", row.getRowNum(), cell.getColumnIndex(), lzfExcelUrils.getCellValue(cell, cell.getCellType()));
                }
                logger.debug("....................行结束..........................");
            }
            logger.debug("===================Sheet结束===================");
            logger.debug("Sheet[{}],总行数{}", sheet.getSheetName(), sheet.getLastRowNum());
        }
    }

}
