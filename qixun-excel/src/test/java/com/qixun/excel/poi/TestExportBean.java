package com.qixun.excel.poi;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

public class TestExportBean {
    public static void main(String[] args) throws IOException {
        
        String[] headers = {"a","b","c"};
        Collection<Object> dataset=new ArrayList<Object>();
        dataset.add(new Model("a1", "b1", "c1"));
        dataset.add(new Model("a2", "b2", "c2"));
        dataset.add(new Model("a3", "b3", "c3"));
        File f=new File("d:\\test2.xls");
        OutputStream out =new FileOutputStream(f);

        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
        fieldMap.put("a","测试1");
        fieldMap.put("b","测试1a2");
        fieldMap.put("c","测试13");

        ExcelUtil.exportExcel(fieldMap,dataset,"测试", out);
        out.close();
    }
}
