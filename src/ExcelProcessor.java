import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sen on 2016/10/14.
 * 主要实现读取单元格数据（这个是选作，成绩以word为准）
 * 设置单元格模式
 * 设计单元格模式
 * 设置单元格类型
 * 公式
 */

public class ExcelProcessor {

    public static void main(String[] args) throws IOException {

        String filePath="data/example.xls";
        HSSFWorkbook workbook =new HSSFWorkbook();
        //设置格式
        setFormat(workbook);
        //合并单元格
        mergeCell(workbook);
        //改变对齐方式
        changeAlignment(workbook);
        //设计算法
        setFormule(workbook);

        FileOutputStream out = new FileOutputStream(filePath);
        workbook.write(out);
        out.close();
        readCells(filePath);
    }
    public static void readCells(String filePath)throws IOException{

        FileInputStream stream=new FileInputStream(filePath);
        HSSFWorkbook workbook = new HSSFWorkbook(stream);
        HSSFSheet sheet= workbook.getSheet("Test4");                    //读取Test4
        for(Row row:sheet){
            for(Cell cell:row){
                System.out.print(cell+"\t");

            }
            System.out.println();
        }
    }
    public static void setFormat(HSSFWorkbook workbook){
        //显示日期的格式
        HSSFSheet sheet=workbook.createSheet("Test1");
        HSSFRow row=sheet.createRow(0);
        HSSFCell cell=row.createCell(0);
        cell.setCellValue(new Date());
        HSSFCellStyle style=workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        cell.setCellStyle(style);
        //设置保留两位有效数字
        cell=row.createCell(1);
        cell.setCellValue(12.3456789);
        style=workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        cell.setCellStyle(style);
        //设置货币格式
        cell=row.createCell(2);
        cell.setCellValue(12345.6789);
        style=workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("￥#,##0"));
        cell.setCellStyle(style);
        //设置百分比的格式
        cell=row.createCell(3);
        cell.setCellValue(0.123456789);
        style=workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
        cell.setCellStyle(style);
        //设置中文大写格式
        cell=row.createCell(4);
        cell.setCellValue(12345);
        style=workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("[DbNum2][$-804]0"));
        cell.setCellStyle(style);
        //设置科学计数法格式
        cell=row.createCell(5);
        cell.setCellValue(12345);
        style=workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("0.00E+00"));
        cell.setCellStyle(style);

    }
    public static void mergeCell(HSSFWorkbook workbook){
        HSSFSheet sheet =workbook.createSheet("Test2");
        HSSFRow row =sheet.createRow(0);
        //合并列
        HSSFCell cell=row.createCell(0);
        cell.setCellValue("合并列");
        CellRangeAddress region=new CellRangeAddress(0,0,0,5);
        sheet.addMergedRegion(region);
        //合并行
        cell=row.createCell(6);
        cell.setCellValue("合并行");
        region=new CellRangeAddress(0,5,6,6);
        sheet.addMergedRegion(region);

    }
    public static void changeAlignment(HSSFWorkbook workbook){
        HSSFSheet sheet=workbook.createSheet("Test3");
        HSSFRow row=sheet.createRow(0);
        HSSFCell cell =row.createCell(0);
        cell.setCellValue("单元格对齐");
        CellStyle style=workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        cell.setCellStyle(style);
        style.setVerticalAlignment((CellStyle.VERTICAL_CENTER));            //垂直居中
        style.setWrapText(true);                                            //自动换行
        style.setIndention((short)5);                                       //缩进
        style.setRotation((short)60);                                       //文本旋转，
        cell.setCellStyle(style);
    }
    public static void setFormule(HSSFWorkbook workbook){
        //基本运算
        HSSFSheet sheet=workbook.createSheet("Test4");
        HSSFRow row=sheet.createRow(0);
        HSSFCell cell=row.createCell(0);
        cell.setCellFormula("2+3*4");                                        //设置公式
        cell=row.createCell(1);
        cell.setCellValue(10);
        cell=row.createCell(2);
        cell.setCellFormula("A1*B1");
        //求和函数
        row=sheet.createRow(1);
        row.createCell(0).setCellValue(1);
        row.createCell(1).setCellValue(2);
        row.createCell(2).setCellValue(3);
        row.createCell(3).setCellValue(4);
        row.createCell(4).setCellValue(5);
        row=sheet.createRow(2);
        row.createCell(0).setCellFormula("sum(A2,C2)");
        row.createCell(1).setCellFormula("sum(A2:E2)");
        //日期函数
        HSSFCellStyle style=workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("yyyy-mm-dd"));
        row=sheet.createRow(3);
        Calendar date=Calendar.getInstance();
        cell=row.createCell(0);
        date.set(2016,10,15);
        cell.setCellValue(date.getTime());
        cell.setCellStyle(style);
        cell=row.createCell(1);
        date.set(2017,1,22);
        cell.setCellValue(date.getTime());
        cell.setCellStyle(style);
        cell=row.createCell(3);
        cell.setCellFormula("CONCATENATE(DATEDIF(A4,B4,\"y\"),\"年\")");
        cell=row.createCell(4);
        cell.setCellFormula("CONCATENATE(DATEDIF(A4,B4,\"m\"),\"月\")");
        cell=row.createCell(5);
        cell.setCellFormula("CONCATENATE(DATEDIF(A4,B4,\"d\"),\"日\")");

        //字符串相关函数
        row=sheet.createRow(4);
        row.createCell(0).setCellValue("abcdefg");
        row.createCell(1).setCellValue("aa bb cc dd ee fF GG");
        row.createCell(3).setCellFormula("UPPER(A5)");
        row.createCell(4).setCellFormula("PROPER(B5)");
        //IF 函数
        row=sheet.createRow(5);
        row.createCell(0).setCellValue(12);
        row.createCell(1).setCellValue(13);
        row.createCell(3).setCellFormula("IF(A6>B6,\"A6大于B6\",\"A6小于等于B6\")");

        //Lookup函数
        row=sheet.createRow(6);
        row.createCell(0).setCellValue(0);
        row.createCell(1).setCellValue(59);
        row.createCell(2).setCellValue("不及格");
        row=sheet.createRow(7);
        row.createCell(0).setCellValue(60);
        row.createCell(1).setCellValue(69);
        row.createCell(2).setCellValue("及格");
        row=sheet.createRow(8);
        row.createCell(0).setCellValue(70);
        row.createCell(1).setCellValue(79);
        row.createCell(2).setCellValue("良好");
        row=sheet.createRow(9);
        row.createCell(0).setCellValue(80);
        row.createCell(1).setCellValue(100);
        row.createCell(2).setCellValue("优秀");
        row = sheet.createRow(10);
        row.createCell(0).setCellValue(75);
        row.createCell(1).setCellFormula("LOOKUP(A11,$A$7:$A$10,$C$7:$C$10)");
        row.createCell(2).setCellFormula("VLOOKUP(A11,$A$7:$C$10,3,true)");

        //随机数函数
        row=sheet.createRow(11);
        row.createCell(0).setCellFormula("RAND()");                    //取0-1之间的随机数
        row.createCell(1).setCellFormula("int(RAND()*100)");           //取0-100之间的随机整数
        row.createCell(2).setCellFormula("rand()*10+10");              //取10-20之间的随机实数
        row.createCell(3).setCellFormula("CHAR(INT (RAND()*26)+97)");  //随机小写字母
        row.createCell(4).setCellFormula("CHAR(INT(RAND()*26)+65)");   //随机大写字母
        row.createCell(5).setCellFormula("CHAR(INT(RAND()*26)+IF(INT(RAND()*2)=0,97,65))");



    }
}
