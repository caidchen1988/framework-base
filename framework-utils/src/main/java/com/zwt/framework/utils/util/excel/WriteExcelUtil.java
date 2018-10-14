package com.zwt.framework.utils.util.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

/**
 * @author zwt
 * @detail
 * @date 2018/9/13
 * @since 1.0
 */
public class WriteExcelUtil {
    public static void writeExecl(List<String> titleList,List<List<Long>> list,String path) throws Exception{
        Workbook workbook=null;
        if(path.endsWith(".xls")){
            workbook=new HSSFWorkbook();
        }else if(path.endsWith(".xlsx")){
            workbook=new XSSFWorkbook();
        }

        Sheet s = workbook.createSheet();

        //把第一行当做标题
        Row r1 = s.createRow(0);
        for(int i=0;i<titleList.size();i++){
            Cell c = r1.createCell(i);
            c.setCellValue(titleList.get(i));
        }

        //创建数据表
        for(int rownum = 1; rownum <= list.size(); rownum++) {
            Row r = s.createRow(rownum);
            List<Long> longList=list.get(rownum-1);
            for(int cellnum = 0; cellnum < longList.size(); cellnum ++) {
               Cell c = r.createCell(cellnum);
                c.setCellValue(longList.get(cellnum));
            }
        }
        FileOutputStream out = new FileOutputStream(path);
        workbook.write(out);
        out.close();

    }
}
