package cn.edu.jxau.jwc.util.poi;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface POIUtil {


    /**
     * 读取普通文件的数据
     * @param targetFile 需要读取的文件的数据
     * @return String
     */
    String readNormalFile(File targetFile) throws IOException;


    /**
     * 读取出Word文件中的数据
     * @param wordFile 需要读取的Word文件
     * @return String
     */
    String readWord(File wordFile) throws IOException;

    /**
     * 将数据写出到Word文件中
     * @param inputStream 数据输入流
     * @param targetFile 目标文件
     * @return File
     */
    File writeWord(InputStream inputStream, File targetFile, Boolean append) throws IOException, InvalidFormatException;


    /**
     * 将字符串添加进Word文档中
     * @param data 被添加的数据
     * @param targetFile Word温文档
     * @param append 是否追加
     * @return File
     * @throws IOException
     */
    File writeWord(String data, File targetFile, boolean append) throws IOException, InvalidFormatException;


    /**
     * 将Word文件内的数据导入到另外一个Word文件
     * @param wordDataFile 目标数据文件
     * @param targetFile  导出目的文件
     * @param append 是否追加
     * @return File
     * @throws IOException
     * @throws InvalidFormatException
     */
    File writeWord(File wordDataFile, File targetFile, boolean append)  throws IOException, InvalidFormatException;


    /**
     * 读取Excel内的数据
     * @param excelFile Excel文件
     * @return List<List<Object>>
     */
    List<List<Object>> readExcel(File excelFile, String sheetName) throws IOException, InvalidFormatException;


    /**
     * 读取Excel文件内的数据
     * @param inputStream 输入流
     * @param sheetName 单元薄的名称
     * @return  List<List<Object>>
     */
    List<List<Object>> readXLSXExcel(InputStream inputStream, String sheetName) throws IOException, InvalidFormatException;


    /**
     * 读取Excel文件内的数据
     * @param inputStream 输入流
     * @param sheetName 单元薄的名称
     * @return  List<List<Object>>
     */
    List<List<Object>> readXLSExcel(InputStream inputStream, String sheetName) throws IOException;

    /**
     * 将数据写出到Excel
     * @param dataList  需要写出的数据
     * @param targetFile 目标文件
     * @param SheetName 单元薄的名字
     * @return File
     */
    File writeExcel(List<List<Object>> dataList, File targetFile, String SheetName, boolean append) throws IOException, InvalidFormatException;


    /**
     * 读取Excel的数据
     * @param sheetName 单元薄的名称
     * @param excelFile Excel文件
     * @return List<Map<String,Object>>
     */
    List<Map<String,Object>> readExcel(String sheetName, File excelFile) throws IOException, InvalidFormatException;


    /***
     * 读取Excel的数据 多个sheet单元的
     * @param excelFile Excel文件
     * @param sheetNames 单元名称
     * @return Map<String,List<Map<String,Integer>>>
     */
    Map<String,List<Map<String,Object>>> readExcel(File excelFile, String... sheetNames) throws IOException, InvalidFormatException;


    /**
     * 获得Excel的标题信息
     * @param excelFile excel 文件
     * @param sheetName 单元的名称
     * @return List<String>
     */
    List<String> getExcelTitle(File excelFile, String sheetName) throws IOException, InvalidFormatException;

    /**
     * 获得Excel某一行的数据
     * @param excelFile Excel文件
     * @param sheetName 单元的名称
     * @param lineNumber 获取记录的行数
     * @return List<Map<String,Object>>
     */
    List<Map<String,Object>> getExcelLineData(File excelFile, String sheetName, Integer lineNumber) throws IOException, InvalidFormatException;


    /**
     * 获取指定行之间的数据信息
     * @param excelFile Excel文件
     * @param sheetName 单元名称
     * @param startLine 起始行
     * @param endLine 结束行
     * @return List<Map<String,Object>>
     */
    List<Map<String,Object>> getExcelLinePartData(File excelFile, String sheetName, Integer startLine, Integer endLine) throws IOException, InvalidFormatException;


    /**
     * 获得指定列的值
     * @param excelFile Excel文件
     * @param sheetName 单元名称
     * @param colNames 列名称
     * @return List<Map<String,Object>>
     */
    List<Map<String,Object>> getExcelColData(File excelFile, String sheetName, String... colNames) throws IOException, InvalidFormatException;



}
