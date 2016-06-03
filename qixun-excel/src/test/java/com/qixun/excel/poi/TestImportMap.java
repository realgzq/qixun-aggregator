/**
 * @author SargerasWang
 */
package com.qixun.excel.poi;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;

/**
 * The <code>TestImportMap</code>	
 * 
 * @author SargerasWang
 * Created at 2014年9月21日 下午5:06:17
 */
public class TestImportMap {
  @SuppressWarnings("rawtypes")
  public static void main(String[] args) throws FileNotFoundException {
    File f=new File("d:/test2.xls");
    InputStream inputStream= new FileInputStream(f);
    
    ExcelLogs logs =new ExcelLogs();
    Collection<Model> importExcel = ExcelUtil.importExcel(Model.class, inputStream, "yyyy/MM/dd HH:mm:ss", logs , 0);
    
    for(Model m : importExcel){
      System.out.println(m.getA()+m.getB()+m.getC());
    }
  }
}
