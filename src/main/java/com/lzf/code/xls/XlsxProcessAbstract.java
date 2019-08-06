//package com.lzf.code.xls;
//
//import org.apache.commons.compress.utils.Lists;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.ooxml.util.SAXHelper;
//import org.apache.poi.openxml4j.opc.OPCPackage;
//import org.apache.poi.ss.usermodel.DataFormatter;
//import org.apache.poi.ss.util.CellAddress;
//import org.apache.poi.ss.util.CellReference;
//import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
//import org.apache.poi.xssf.eventusermodel.XSSFReader;
//import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
//import org.apache.poi.xssf.model.StylesTable;
//import org.apache.poi.xssf.usermodel.XSSFComment;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.xml.sax.ContentHandler;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//import org.xml.sax.XMLReader;
//
//import javax.xml.parsers.ParserConfigurationException;
//import java.beans.IntrospectionException;
//import java.beans.PropertyDescriptor;
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.InvocationTargetException;
//import java.util.ArrayList;
//import java.util.LinkedList;
//
///**
// * Excle xxls 批量读取大文件操作类
// *
// */
//public abstract class XlsxProcessAbstract  {
//
//    private final Logger logger = LoggerFactory.getLogger(XlsxProcessAbstract.class);
//
//    //开始读取行数从第0行开始计算
//    private int rowIndex = -1;
//
//    private final int minColumns = 0;
//    /**
//     * Destination for data
//     */
//
//
//    public <T> LinkedList<T> processAllSheet(Integer index, Class<T> clazz) throws Exception {
//        OPCPackage pkg = OPCPackage.open(this.getInputStream());
//        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
//        XSSFReader xssfReader = new XSSFReader(pkg);
//        StylesTable styles = xssfReader.getStylesTable();
//        SheetToCSV<T> sheetToCSV = new SheetToCSV<T>(clazz);
//        parserSheetXml(styles, strings, sheetToCSV, xssfReader.getSheet("rId"+index));
//        return sheetToCSV.getPojoList();
//    }
//
//
//    /**
//     * 解析excel 转换成xml
//     *
//     * @param styles
//     * @param strings
//     * @param sheetHandler
//     * @param sheetInputStream
//     * @throws IOException
//     * @throws SAXException
//     */
//    public void parserSheetXml(StylesTable styles, ReadOnlySharedStringsTable strings, XSSFSheetXMLHandler.SheetContentsHandler sheetHandler, InputStream sheetInputStream) throws IOException, SAXException {
//        DataFormatter formatter = new DataFormatter();
//        InputSource sheetSource = new InputSource(sheetInputStream);
//        try {
//            XMLReader sheetParser = SAXHelper.newXMLReader();
//            ContentHandler handler = new XSSFSheetXMLHandler(styles, null, strings, sheetHandler, formatter, false);
//            sheetParser.setContentHandler(handler);
//            sheetParser.parse(sheetSource);
//        } catch (ParserConfigurationException e) {
//            throw new RuntimeException("SAX parser appears to be broken - " + e);
//        } catch (SAXException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public abstract InputStream getInputStream() throws IOException;
//
//    /**
//     * 读取excel行、列值
//     *
//     * @author nevin.zhang
//     */
//    private class SheetToCSV<T> implements XSSFSheetXMLHandler.SheetContentsHandler {
//        private boolean firstCellOfRow = false;
//        private T pojo;
//        private Class<T> clazz;
//        private int currentRowNumber = -1;
//        private int currentColNumber = -1;
//        private ArrayList<String> keyList = Lists.newArrayList();
//        private LinkedList<T> pojoList = new LinkedList<>();
//
//        public LinkedList<T> getPojoList() {
//            return pojoList;
//        }
//
//        public SheetToCSV(Class<T> clazz) {
//            this.clazz = clazz;
//        }
//
//        /**
//         * 处理cell中为空值的情况
//         * @param number
//         */
//        private void processCellBlankCells(int number) {
//            for (int i = 0; i < number; i++) {
//                for (int j = 0; j < minColumns; j++) {
//                }
//            }
//        }
//
//
//        @Override
//        public void startRow(int rowNum) {
//            //logger.info(String.valueOf(rowNum));
//            processCellBlankCells(rowNum - currentRowNumber - 1);
//            if(rowNum!=0){
//                try {
//                    pojo = clazz.newInstance();
//                } catch (InstantiationException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            firstCellOfRow = true;
//            currentRowNumber = rowNum;
//            currentColNumber = -1;
//        }
//
//        @Override
//        public void endRow(int rowNum) {
//            if(pojo==null){
//                return;
//            }
//            System.out.println(pojo);
//            if (currentRowNumber!=0){
//                pojoList.add(pojo);
//            }
//
//        }
//
//        @Override
//        public void cell(String cellReference, String cellValue, XSSFComment comment) {
//
//            if (firstCellOfRow) {
//                firstCellOfRow = false;
//            } else {
//            }
//            if (cellReference == null) {
//                cellReference = new CellAddress(currentRowNumber, currentColNumber).formatAsString();
//            }
//            int thisCol = (new CellReference(cellReference)).getCol();
//            int missedCols = thisCol - currentColNumber - 1;
//            for (int i = 0; i < missedCols; i++) {
//                // excel中为空的值设置为“|@|”
//            }
//            currentColNumber = thisCol;
//            logger.info("当前行数:{},当前列数:{},当前值cell:{}",currentRowNumber, currentColNumber, cellValue);
//            if (currentRowNumber ==0){
//                keyList.add(cellValue);
//                return;
//            }
//            if (pojo == null|| StringUtils.isBlank(cellValue)) {
//                return;
//            }
//            try {
//                PropertyDescriptor pd = new PropertyDescriptor(keyList.get(currentColNumber), clazz);
//                try {
//                    pd.getWriteMethod().invoke(pojo, cellValue);
//                    pd=new PropertyDescriptor("createBy",clazz);
//                    pd.getWriteMethod().invoke(pojo, RequestHolder.getCurrentUser().getUsername());
//                    pd=new PropertyDescriptor("updateBy",clazz);
//                    pd.getWriteMethod().invoke(pojo, RequestHolder.getCurrentUser().getUsername());
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            } catch (IntrospectionException e) {
//                e.printStackTrace();
//            } catch (IntrospectionException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        public void headerFooter(String text, boolean isHeader, String tagName) {
//        }
//
//    }
//
//}
//
