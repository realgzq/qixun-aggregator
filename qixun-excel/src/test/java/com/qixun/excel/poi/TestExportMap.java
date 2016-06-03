/**
 * @author SargerasWang
 */
package com.qixun.excel.poi;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * The <code>TestExportMap</code>
 *
 * @author SargerasWang
 *         Created at 2014年9月21日 下午4:38:42
 */
public class TestExportMap {
    public static void main(String[] args) throws IOException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "王刚");
        map.put("age", 24);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("name", "sargeras");
        map2.put("age", 100);
        list.add(map);
        list.add(map2);

        File f = new File("D:/test.xls");
        OutputStream out = new FileOutputStream(f);
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
        fieldMap.put("name","姓名");
        fieldMap.put("age","年龄");
        //Collection<T> dataset;
        //String sheetName;int startRow, int startColumn,
        //OutputStream out

        ExcelUtil.exportExcel(fieldMap, list, "test", 1, 1, out, "YYYYMMDD");
        out.close();
    }
}
