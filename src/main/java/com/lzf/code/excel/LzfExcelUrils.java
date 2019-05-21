package com.lzf.code.excel;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 超大excel处理工具类
 * <br/>
 * Created in 2019-05-20  23:13
 *
 * @author Zhenfeng Li
 */
public class LzfExcelUrils {
    private Logger logger = LoggerFactory.getLogger(LzfExcelUrils.class);

    /**
     * 自动识别Excel版本
     *
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public Workbook getWorkbook(String filePath) throws IOException {
        File file = new File(filePath);
        try {
            Workbook xls = xls(new FileInputStream(file));
            logger.debug("文件[{}]的真实后缀为.xls,大小{}KB", file.getName(), file.length() / 1024d);
            return xls;
        } catch (OfficeXmlFileException e) {
            Workbook xlsx = xlsx(new FileInputStream(file));
            logger.debug("文件[{}]的真实后缀为.xlsx,大小{}KB", file.getName(), file.length() / 1024d);
            return xlsx;
        }
    }

    /**
     * .xlsx后缀的excel
     *
     * @param filePath .xlsx文件路径
     * @return
     * @throws FileNotFoundException
     */
    public Workbook xlsx(String filePath) throws FileNotFoundException {
        return StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(new FileInputStream(filePath));
    }

    /**
     * .xlsx后缀的excel
     *
     * @param inputStream 输入流
     * @return
     */
    public Workbook xlsx(InputStream inputStream) {
        return StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(inputStream);
    }

    /**
     * .xls后缀的excel
     *
     * @param filePath .xls文件路径
     * @return
     * @throws IOException
     */
    public Workbook xls(String filePath) throws IOException {
        return new HSSFWorkbook(new FileInputStream(filePath));
    }

    /**
     * .xls后缀的excel
     *
     * @param inputStream 输入流
     * @return
     * @throws IOException
     */
    public Workbook xls(InputStream inputStream) throws IOException {
        return new HSSFWorkbook(inputStream);
    }

    /**
     * 按照excel真实后缀获取内容
     *
     * @param cell        单元格
     * @param rowCellType 单元格类型
     * @return
     * @throws LzfExcelException
     */
    public String getCellValue(Cell cell, CellType rowCellType) throws LzfExcelException {
        String value;
        switch (rowCellType) {
            // 数字
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    if (date != null) {
                        //有 年/月/日
                        if (HSSFDateUtil.isCellInternalDateFormatted(cell)) {
                            //默认的日期格式
                            value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        } else {
                            //默认的时间格式
                            value = new SimpleDateFormat("HH:mm:ss").format(date);
                        }
                    } else {
                        value = null;
                    }
                } else {
                    NumberFormat numberFormat = NumberFormat.getNumberInstance();
                    numberFormat.setGroupingUsed(false);
                    //默认保留8位小数
                    numberFormat.setMaximumFractionDigits(8);
                    value = numberFormat.format(cell.getNumericCellValue());
                }
                break;
            // 字符串
            case STRING:
                value = cell.getStringCellValue();
                break;
            // Boolean
            case BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            //是否存在 嵌套 公式类型
            case FORMULA:
                value = getCellValue(cell, cell.getCachedFormulaResultType());
                break;
            // 空值
            case BLANK:
                value = null;
                break;
            // 故障
            case ERROR:
                value = ErrorEval.getText(cell.getErrorCellValue());
                throw new LzfExcelException("故障");
            default:
                value = "未知类型";
                throw new LzfExcelException("未知类型");
        }
        return value;
    }
}
