package cn.edu.jxau.jwc.util.poi.impl;

import cn.edu.jxau.jwc.util.poi.POIUtil;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * 读取Word文件
 * 操作Excel的工具类
 */

@Component
public class POIUtilImpl implements POIUtil {


    private static volatile POIUtil poiUtil;

    public static POIUtil getInstance() {
        if (poiUtil == null) {
            synchronized (POIUtil.class) {
                if (poiUtil == null) {
                    poiUtil = new POIUtilImpl();
                }
            }
        }
        return poiUtil;
    }


    @Override
    public String readNormalFile(File targetFile) throws IOException {
        if (!targetFile.exists()) {
            throw new RuntimeException("数据文件不存在");
        }
        String fileName = targetFile.getName();
        if (fileName.endsWith(".docx") || fileName.endsWith(".doc")) {
            return readWord(targetFile);
        }
        StringBuilder result = new StringBuilder();
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(targetFile))) {
            byte[] bytes = new byte[1024 * 100];
            int len;
            while ((len = bufferedInputStream.read(bytes)) > 0) {
                result.append(new String(bytes, 0, len));
            }
        }
        return new String(result);
    }

    @Override
    public String readWord(File wordFile) throws IOException {
        if (!wordFile.exists()) {
            throw new RuntimeException("文件不存在：" + wordFile.getPath());
        }
        String result = "";
        if (wordFile.getName().endsWith(".doc")) {
            InputStream is = new FileInputStream(wordFile);
            WordExtractor ex = new WordExtractor(is);
            result = ex.getText();
            is.close();
        } else if (wordFile.getName().endsWith(".docx")) {
            OPCPackage opcPackage = POIXMLDocument.openPackage(wordFile.getPath());
            POIXMLTextExtractor extractor;
            try {
                extractor = new XWPFWordExtractor(opcPackage);
                result = extractor.getText();
                opcPackage.close();
            } catch (XmlException | OpenXML4JException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("文件格式不正确");
        }
        result = result.replaceAll("(\\r\\n){2,}", "\r\n");
        result = result.replaceAll("(\\n){2,}", "\n");
        return result;
    }


    @Override
    public File writeWord(InputStream inputStream, File targetFile, Boolean append) throws IOException, InvalidFormatException {
        if (append) {
            if (!targetFile.exists()) {
                throw new RuntimeException("文件不存在，无法追加");
            }
        }

        byte[] bytes = new byte[1024 * 100];
        int len;
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            if (targetFile.getName().endsWith(".doc")) {
                HWPFDocument hwpfDocument = new HWPFDocument(new FileInputStream(targetFile));
                Range range = hwpfDocument.getRange();
                if (append) {
                    range.replaceText("{{start}}", false);
                }
                while ((len = bufferedInputStream.read(bytes)) > 0) {
                    String str = new String(bytes, 0, len);
                    range.insertAfter(str);
                }
                range.replaceText("{{start}}", "");
                hwpfDocument.write(new FileOutputStream(targetFile));
            } else if (targetFile.getName().endsWith(".docx")) {
                XWPFDocument xwpfDocument;
                xwpfDocument = findIndex(targetFile, append);
                List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();
                XWPFParagraph paragraph = xwpfDocument.createParagraph();
                XWPFRun run = paragraph.insertNewRun(0);
                int index = 0;
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String str;
                    while ((str = bufferedReader.readLine()) != null) {
                        run.setText(str, index++);
                        run.addBreak();
                    }
                    xwpfDocument.setParagraph(paragraph, paragraphs.size() - 1);
                    xwpfDocument.write(new FileOutputStream(targetFile));
                }
            }
        }
        return targetFile;
    }

    @Override
    public File writeWord(String data, File targetFile, boolean append) throws IOException, InvalidFormatException {
        if (append) {
            if (!targetFile.exists()) {
                throw new RuntimeException("文件不存在，无法追加");
            }
        }
        if (targetFile.getName().endsWith(".doc")) {
            HWPFDocument hwpfDocument = new HWPFDocument(new FileInputStream(targetFile));
            Range range = hwpfDocument.getRange();
            if (append) {
                range.replaceText("{{start}}", false);
            }
            range.insertAfter(data);
            range.replaceText("{{start}}", "");
            hwpfDocument.write(new FileOutputStream(targetFile));
        } else if (targetFile.getName().endsWith(".docx")) {
            XWPFDocument xwpfDocument;
            xwpfDocument = findIndex(targetFile, append);
            List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();
            XWPFParagraph paragraph = xwpfDocument.createParagraph();
            XWPFRun run = paragraph.insertNewRun(0);
            run.setText(data, 0);
            run.addBreak();
            xwpfDocument.setParagraph(paragraph, paragraphs.size() - 1);
            xwpfDocument.write(new FileOutputStream(targetFile));
        }
        return targetFile;
    }

    @Override
    public File writeWord(File wordDataFile, File targetFile, boolean append) throws IOException, InvalidFormatException {
        if (!wordDataFile.exists()) {
            throw new RuntimeException("数据文件不存在");
        }
        if (append) {
            if (!targetFile.exists()) {
                throw new RuntimeException("文件不存在，无法追加");
            }
        }
        return writeWord(readWord(wordDataFile), targetFile, append);
    }


    /**
     * 根据append确定文档的定位位置
     *
     * @param targetFile 目标文件
     * @param append     是否追加
     * @return 文档对象
     * @throws IOException            io异常
     * @throws InvalidFormatException 异常
     */
    private XWPFDocument findIndex(File targetFile, boolean append) throws IOException, InvalidFormatException {
        XWPFDocument xwpfDocument = new XWPFDocument(OPCPackage.open(new FileInputStream(targetFile)));
        if (!append) {
            List<IBodyElement> elements = xwpfDocument.getBodyElements();
            for (int i = 0; i < elements.size(); i++) {
                xwpfDocument.removeBodyElement(i);
            }
        }
        return xwpfDocument;
    }


    @Override
    public List<List<Object>> readExcel(File excelFile, String sheetName) throws IOException, InvalidFormatException {
        if (!excelFile.exists()) {
            throw new RuntimeException("需要读取文件不存在");
        }
        Workbook workbook;
        Sheet sheet;
        int rowLength;
        if (excelFile.getName().endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(OPCPackage.open(new FileInputStream(excelFile)));
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new RuntimeException("sheet不存在");
            rowLength = sheet.getLastRowNum();
            if (rowLength == 0) throw new RuntimeException("此sheet无数据");
        } else if (excelFile.getName().endsWith(".xls")) {
            workbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(excelFile)));
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new RuntimeException("指定的单元格不存在");
            rowLength = sheet.getLastRowNum();
            if (rowLength == -1 || rowLength == 1) throw new RuntimeException("指定的sheet无数据(不包括标题)");
        } else {
            throw new RuntimeException("文件格式不支持");
        }
        return readExcel(sheet, rowLength);
    }

    @Override
    public List<List<Object>> readXLSXExcel(InputStream inputStream, String sheetName) throws IOException, InvalidFormatException {
        Workbook workbook = new XSSFWorkbook(OPCPackage.open(inputStream));
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) throw new RuntimeException("sheet不存在");
        int rowLength = sheet.getLastRowNum();
        if (rowLength == 0) throw new RuntimeException("此sheet无数据");
        return readExcel(sheet, rowLength);
    }

    @Override
    public List<List<Object>> readXLSExcel(InputStream inputStream, String sheetName) throws IOException {
        Workbook workbook = new HSSFWorkbook(new POIFSFileSystem(inputStream));
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) throw new RuntimeException("指定的单元格不存在");
        int rowLength = sheet.getLastRowNum();
        if (rowLength == -1 || rowLength == 1) throw new RuntimeException("指定的sheet无数据(不包括标题)");
        return readExcel(sheet, rowLength);
    }


    private List<List<Object>> readExcel(Sheet sheet, int rowLength) {
        List<List<Object>> resultList = new ArrayList<>();
        for (int rowIndex = 1; rowIndex <= rowLength; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            int cellLength = row.getLastCellNum();
            if (cellLength == 0) continue;
            List<Object> list = new ArrayList<>();
            for (int cellIndex = 0; cellIndex < cellLength; cellIndex++) {
                list.add(this.getCellValue(row.getCell(cellIndex)));
            }
            resultList.add(list);
        }
        return resultList;
    }


    @Override
    public File writeExcel(List<List<Object>> dataList, File targetFile, String sheetName, boolean append) throws IOException, InvalidFormatException {
        if (append) {
            if (!targetFile.exists()) {
                throw new RuntimeException("文件不存在无法追加");
            }
        }
        Workbook workbook;
        Sheet sheet;
        if (targetFile.getName().endsWith(".xlsx")) {
            if (append) {
                //确定起始的行位置
                workbook = new XSSFWorkbook(OPCPackage.open(new FileInputStream(targetFile)));
                sheet = workbook.getSheet(sheetName);
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet(sheetName);
            }

        } else if (targetFile.getName().endsWith(".xls")) {
            if (append) {
                workbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(targetFile)));
                sheet = workbook.getSheet(sheetName);
            } else {
                workbook = new HSSFWorkbook();
                sheet = workbook.createSheet(sheetName);
            }
        } else {
            throw new RuntimeException("文件格式不支持");
        }
        //创建sheet
        int rowIndex = append ?
                (sheet.getLastRowNum() == 0 ? 0 : sheet.getLastRowNum() + 1)
                : sheet.getFirstRowNum();

        for (List<Object> list : dataList) {
            Row row = sheet.createRow(rowIndex++);
//                设置单元的值
            for (int cellLength = 0; cellLength < list.size(); cellLength++) {
                this.setCellValue(list, cellLength, row);
//                    列自适应宽度
                sheet.autoSizeColumn(cellLength, true);
            }
        }
        workbook.write(new FileOutputStream(targetFile));
        return targetFile;
    }


    @Override
    public List<Map<String, Object>> readExcel(String sheetName, File excelFile) throws IOException, InvalidFormatException {
        if (!excelFile.exists()) {
            throw new RuntimeException("文件不存在无法读取");
        }
        List<String> head = this.getExcelTitle(excelFile, sheetName);
        List<List<Object>> dataList = this.readExcel(excelFile, sheetName);
        List<Map<String, Object>> dataMap = new ArrayList<>();
        if (dataList.size() <= 0) return dataMap;
        if (dataList.get(0).size() != head.size()) throw new RuntimeException("标题与数据列数不对应");
        for (List<Object> list : dataList) {
            Map<String, Object> map = new HashMap<>();
            int index = 0;
            for (Object object : list) {
                map.put(head.get(index++), object);
            }
            dataMap.add(map);
        }
        return dataMap;
    }


    @Override
    public Map<String, List<Map<String, Object>>> readExcel(File excelFile, String... sheetNames) throws IOException, InvalidFormatException {
        if (!excelFile.exists()) {
            throw new RuntimeException("文件不存在无法读取");
        }
        Map<String, List<Map<String, Object>>> dataMap = new HashMap<>();
        Workbook workbook;
        if (excelFile.getName().endsWith(".xlsx")) {
            //确定起始的行位置
            workbook = new XSSFWorkbook(OPCPackage.open(new FileInputStream(excelFile)));
        } else if (excelFile.getName().endsWith(".xls")) {
            workbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(excelFile)));
        } else {
            throw new RuntimeException("文件不支持解析");
        }
        for (String sheetName : sheetNames) {
            Sheet sheet = workbook.getSheet(sheetName);
            List<String> head = this.getExcelHead(sheet);
            int rowLength = sheet.getLastRowNum();
            if (rowLength <= 1) continue;
            List<Map<String, Object>> dataList = new ArrayList<>();
            for (int rowIndex = 0; rowIndex < rowLength; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (rowIndex > 0) {
                    dataList.add(this.getRowValueMap(head, row));
                }
            }
            dataMap.put(sheetName, dataList);
        }
        return dataMap;
    }

    @Override
    public List<String> getExcelTitle(File excelFile, String sheetName) throws IOException, InvalidFormatException {
        if (!excelFile.exists()) {
            throw new RuntimeException("文件不存在无法读取");
        }
        if (sheetName == null || "".equals(sheetName.trim())) {
            throw new RuntimeException("sheetName can't null");
        }
        Workbook workbook;
        Sheet sheet;
        if (excelFile.getName().endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(OPCPackage.open(new FileInputStream(excelFile)));
        } else if (excelFile.getName().endsWith(".xls")) {
            workbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(excelFile)));
        } else {
            throw new RuntimeException("文件格式不支持");
        }
        sheet = workbook.getSheet(sheetName);
        return this.getExcelHead(sheet);
    }

    @Override
    public List<Map<String, Object>> getExcelLineData(File excelFile, String sheetName, Integer lineNumber) throws IOException, InvalidFormatException {
        if (!excelFile.exists()) {
            throw new RuntimeException("文件不存在");
        }
        if (sheetName == null || "".equals(sheetName.trim())) {
            throw new RuntimeException("sheetName can't null");
        }
        if (lineNumber < 1) {
            throw new RuntimeException("lineNumber不合法");
        }
        List<Map<String, Object>> dataList = new ArrayList<>();
        Sheet sheet = getSheet(excelFile, sheetName);
        List<String> head = this.getExcelHead(sheet);
        Row row = sheet.getRow(lineNumber);
        if (row == null) throw new RuntimeException("row is null");
        if (head.size() <= 0) throw new RuntimeException("标题不存在");
        dataList.add(this.getRowValueMap(head, row));
        return dataList;
    }

    @Override
    public List<Map<String, Object>> getExcelLinePartData(File excelFile, String sheetName, Integer startLine, Integer endLine) throws IOException, InvalidFormatException {
        if (!excelFile.exists()) {
            throw new RuntimeException("文件不存在");
        }
        if (sheetName == null || "".equals(sheetName.trim())) {
            throw new RuntimeException("sheetName can't null");
        }
        if (startLine < 1 || endLine <= startLine) {
            throw new RuntimeException("startLine或endLine不合法");
        }
        List<Map<String, Object>> dataList = new ArrayList<>();
        Sheet sheet = this.getSheet(excelFile, sheetName);
        List<String> head = this.getExcelHead(sheet);
        if (head.size() <= 0) throw new RuntimeException("标题不存在");
        for (int startIndex = startLine; startIndex < endLine; startIndex++) {
            Map<String, Object> map = this.getRowValueMap(head, sheet.getRow(startIndex));
            dataList.add(map);
        }
        return dataList;
    }

    @Override
    public List<Map<String, Object>> getExcelColData(File excelFile, String sheetName, String... colNames) throws IOException, InvalidFormatException {
        if (!excelFile.exists()) {
            throw new RuntimeException("文件不存在");
        }
        for (String colName : colNames)
            if (colName == null || "".equals(colName.trim())) throw new RuntimeException("参数名不可为空");
        List<Map<String, Object>> dataList = new ArrayList<>();
        Sheet sheet = this.getSheet(excelFile, sheetName);
        List<String> head = this.getExcelHead(sheet);
        List<Integer> colIndexList = this.getColIndex(head, colNames);
        int rowLength = sheet.getLastRowNum();
        //以列数据进行存储
        List<List<Object>> data = new ArrayList<>();
        for (Integer colIndex : colIndexList) {
            List<Object> list = new ArrayList<>();
            for (int rowIndex = sheet.getFirstRowNum() + 1; rowIndex <= rowLength; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                list.add(this.getCellValue(row.getCell(colIndex)));
            }
            data.add(list);
        }
        //转换数据
        for (int index = sheet.getFirstRowNum() + 1; index <= rowLength; index++) {
            Map<String, Object> map = new HashMap<>();
            int i = 0;
            for (List<Object> li : data) {
                map.put(head.get(colIndexList.get(i++)), li.get(index - 1));
            }
            dataList.add(map);
        }
        return dataList;
    }

    /**
     * 获得要查询列id
     *
     * @param head     标题信息
     * @param colNames 需要的列的名字
     * @return List<Integer>
     */
    private List<Integer> getColIndex(List<String> head, String... colNames) {
        List<Integer> list = new ArrayList<>();
        for (int index = 0; index < head.size(); index++) {
            for (String colName : colNames) {
                if (colName.equals(head.get(index))) {
                    list.add(index);
                    break;
                }
            }
            if (list.size() == colNames.length) {
                break;
            }
        }
        return list;
    }

    /**
     * 获得一个单元对象
     *
     * @param excelFile Excel文件
     * @param sheetName 单元的名称
     * @return Sheet
     * @throws IOException            异常
     * @throws InvalidFormatException 异常
     */
    private Sheet getSheet(File excelFile, String sheetName) throws IOException, InvalidFormatException {
        Workbook workbook;
        Sheet sheet;
        if (excelFile.getName().endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(OPCPackage.open(new FileInputStream(excelFile)));
            sheet = workbook.getSheet(sheetName);
        } else if (excelFile.getName().endsWith(".xls")) {
            workbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(excelFile)));
            sheet = workbook.getSheet(sheetName);
        } else {
            throw new RuntimeException("文件格式不支持");
        }
        return sheet;
    }


    /**
     * 获得单元格的第一行数据 标题信息
     *
     * @param sheet 单元对象
     * @return List<String>
     */
    private List<String> getExcelHead(Sheet sheet) {
        int rowLength = sheet.getLastRowNum();
        if (rowLength <= 0) throw new RuntimeException("不存在标题");
        Row row = sheet.getRow(sheet.getFirstRowNum());
        return this.getExcelHead(row);
    }

    /**
     * 获得标题行的数据
     *
     * @param row 行对象
     * @return List<String>
     */
    private List<String> getExcelHead(Row row) {
        List<String> head = new ArrayList<>();
        int cellLength = row.getLastCellNum();
        for (int cellIndex = 0; cellIndex < cellLength; cellIndex++) {
            head.add(this.getCellValue(row.getCell(cellIndex)) + "");
        }
        return head;
    }

    /**
     * 获得一行的数据
     *
     * @param head 标题信息
     * @param row  行对象
     * @return Map<String, Object>
     */
    private Map<String, Object> getRowValueMap(List<String> head, Row row) {
        if (head.size() != row.getLastCellNum()) throw new RuntimeException("标题数量和的数据的列数不一致");
        int cellLength = row.getLastCellNum();
        Map<String, Object> lineMap = new HashMap<>();
        for (int cellIndex = 0; cellIndex < cellLength; cellIndex++) {
            lineMap.put(head.get(cellIndex), this.getCellValue(row.getCell(cellIndex)));
        }
        return lineMap;
    }

    /**
     * 设置单元格的值
     *
     * @param list      行数据信息
     * @param cellIndex 选中的单元格下标
     * @param row       行对象
     */
    private void setCellValue(List<Object> list, int cellIndex, Row row) {
        Object object = list.get(cellIndex);
        if (object instanceof Date) {
            row.createCell(cellIndex).setCellValue((Date) object);
        } else if (object instanceof Integer) {
            row.createCell(cellIndex).setCellValue((Integer) object);
        } else if (object instanceof Boolean) {
            row.createCell(cellIndex).setCellValue((Boolean) object);
        } else if (object instanceof Long) {
            row.createCell(cellIndex).setCellValue((Long) object);
        } else if (object instanceof Double) {
            row.createCell(cellIndex).setCellValue((Double) object);
        } else {
            row.createCell(cellIndex).setCellValue((String) object);
        }
    }


    /**
     * 组合一行的数据
     *
     * @param cell 单元格对象
     * @return Object
     */
    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                return null;
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_ERROR:
                return "非法字符";
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            default:
                return "未知数据";
        }
    }
}
