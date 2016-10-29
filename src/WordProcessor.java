
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlException;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sen on 2016/10/17.
 * 对word进行操作
 * 主要实现统计字数
 * 统计段落
 * 列出标题
 * 替换数据
 * 主要根据助教提供的视频以及网上教程
 */


public class WordProcessor {

    public static void main(String[] args) throws Exception{

        InputStream in=new  FileInputStream("data/poi-input.doc");
        HWPFDocument doc=new HWPFDocument(in);
        WordExtractor extractor =new WordExtractor(doc);
        //统计字数
        printCharacterNum(extractor.getSummaryInformation());
        //统计段落
        Range range=doc.getRange();
        printParagraphNum(range);
        //统计标题和副标题
        printTitleAndSubtitle("data/poi-input.docx");
        //替换数据并写入新的文件中
        Map<String,String>map= new HashMap<>();
        map.put("故事","事件");
        HWPFDocument output=replaceStr("data/poi-input.doc",map);
        OutputStream out=new FileOutputStream("data/poi-output.doc");
        output.write(out);

    }//end main


    public static void  printCharacterNum(SummaryInformation info){
        System.out.println("一共有 "+info.getCharCount()+" 个字");
    }

    public static void printParagraphNum(Range range){
        System.out.println("一共有 "+ range.numParagraphs()+" 段");
    }

    public static void printTitleAndSubtitle(String path) throws IOException, OpenXML4JException, XmlException {

        InputStream in=new FileInputStream(path);
        XWPFDocument doc=new XWPFDocument(in);
        List<XWPFParagraph> paragraphs=doc.getParagraphs();                           //遍历段落来获取标题的类型
        for(XWPFParagraph paras:paragraphs){
            String text=paras.getParagraphText();
            String style=paras.getStyle();
            if("1".equals(style)){
                System.out.println(text+"--["+style+"]--");
            }else if("2".equals(style)){
                System.out.println(text+"--["+style+"]--");
            }else if("3".equals(style)){
                System.out.println(text+"--["+style+"]--");
            }else{
                continue;
            }//end else
        }//end for

    }//end printTileAndSubtitle
    public static HWPFDocument replaceStr(String path,Map<String,String> map) throws FileNotFoundException {
        try {
            FileInputStream file = new FileInputStream(new File(path));
            HWPFDocument doc=new HWPFDocument(file);
            Range range=doc.getRange();
            for(Map.Entry<String,String> entry:map.entrySet()){
                range.replaceText(entry.getKey(),entry.getValue());               //替换文本
            }
            return doc;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }//end catch


    }//end replaceStr
}//end WordProcessor
