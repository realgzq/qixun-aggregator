package com.qixun.excel.poi;

import com.qixun.excel.jxl.ExcelException;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The <code>ExcelUtil</code>
 *
 * @author albert.guo
 * @version 1.0, Created at 2016年1月23日
 */
public class ExcelUtil {
    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 用来验证excel与Vo中的类型是否一致 <br>
     * Map<栏位类型,只能是哪些Cell类型>
     */
    private static Map<Class<?>, Integer[]> validateMap = new HashMap<Class<?>, Integer[]>();

    static {
        validateMap.put(String[].class, new Integer[]{Cell.CELL_TYPE_STRING});
        validateMap.put(Double[].class, new Integer[]{Cell.CELL_TYPE_NUMERIC});
        validateMap.put(String.class, new Integer[]{Cell.CELL_TYPE_STRING});
        validateMap.put(Double.class, new Integer[]{Cell.CELL_TYPE_NUMERIC});
        validateMap.put(Date.class, new Integer[]{Cell.CELL_TYPE_NUMERIC, Cell.CELL_TYPE_STRING});
        validateMap.put(Integer.class, new Integer[]{Cell.CELL_TYPE_NUMERIC});
        validateMap.put(Float.class, new Integer[]{Cell.CELL_TYPE_NUMERIC});
        validateMap.put(Long.class, new Integer[]{Cell.CELL_TYPE_NUMERIC});
        validateMap.put(Boolean.class, new Integer[]{Cell.CELL_TYPE_BOOLEAN});
    }

    /**
     * 获取cell类型的文字描述
     *
     * @param cellType <pre>
     *                                 Cell.CELL_TYPE_BLANK
     *                                 Cell.CELL_TYPE_BOOLEAN
     *                                 Cell.CELL_TYPE_ERROR
     *                                 Cell.CELL_TYPE_FORMULA
     *                                 Cell.CELL_TYPE_NUMERIC
     *                                 Cell.CELL_TYPE_STRING
     *                                 </pre>
     * @return
     */
    private static String getCellTypeByInt(int cellType) {
        switch (cellType) {
            case Cell.CELL_TYPE_BLANK:
                return "Null type";
            case Cell.CELL_TYPE_BOOLEAN:
                return "Boolean type";
            case Cell.CELL_TYPE_ERROR:
                return "Error type";
            case Cell.CELL_TYPE_FORMULA:
                return "Formula type";
            case Cell.CELL_TYPE_NUMERIC:
                return "Numeric type";
            case Cell.CELL_TYPE_STRING:
                return "String type";
            default:
                return "Unknown type";
        }
    }

    /**
     * 获取单元格值
     *
     * @param cell
     * @return
     */
    private static Object getCellValue(Cell cell) {
        if (cell == null
                || (cell.getCellType() == Cell.CELL_TYPE_STRING && StringUtils.isBlank(cell
                .getStringCellValue()))) {
            return null;
        }
        int cellType = cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_BLANK:
                return null;
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_ERROR:
                return cell.getErrorCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            default:
                return null;
        }
    }


    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于单个sheet
     *
     * @param <T>
     * @param fieldMap 表格属性名, 标题行映射
     *                 List LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
     *                 fieldMap.put("name","姓名");
     *                 fieldMap.put("age","年龄");
     * @param dataset  需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                 javabean属性的数据类型有基本数据类型及String,Date,String[],Double[]
     * @param out      与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     */
    public static <T> void exportExcel(LinkedHashMap<String, String> fieldMap,
                                       Collection<T> dataset,
                                       String sheetName,
                                       OutputStream out) {
        exportExcel(fieldMap, dataset, sheetName, 0, 0, out, null);
    }


    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于单个sheet
     *
     * @param <T>
     * @param fieldMap 表格属性名, 标题行映射
     *                 List LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
     *                 fieldMap.put("name","姓名");
     *                 fieldMap.put("age","年龄");
     * @param dataset  需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                 javabean属性的数据类型有基本数据类型及String,Date,String[],Double[]
     * @param startRow  开始的行
     * @param startColumn 开始的列
     * @param out      与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中*/

    public static <T> void exportExcel(LinkedHashMap<String, String> fieldMap,
                                       Collection<T> dataset,
                                       String sheetName,
                                       int startRow,
                                       int startColumn,
                                       String datePattern,
                                       OutputStream out) {
        exportExcel(fieldMap, dataset, sheetName, startRow, startColumn, out, datePattern);
    }

    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于单个sheet
     *
     * @param <T>
     * @param fieldMap 表格属性名, 标题行映射
     *                 List LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
     *                 fieldMap.put("name","姓名");
     *                 fieldMap.put("age","年龄");
     * @param dataset  需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                 javabean属性的数据类型有基本数据类型及String,Date,String[],Double[]
     * @param startRow  开始的行
     * @param startColumn 开始的列
     * @param out      与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中*/
    public static <T> void exportExcel(LinkedHashMap<String, String> fieldMap,
                                       Collection<T> dataset,
                                       String sheetName,
                                       int startRow,
                                       int startColumn,
                                       OutputStream out,
                                       String pattern) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            // 生成一个表格
            sheet = workbook.createSheet(sheetName);
        }

        write2Sheet(sheet, fieldMap, dataset, startRow, startColumn, pattern);
        try {
            workbook.write(out);
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
    }


    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于单个sheet
     *
     * @param fieldMap 表格属性名, 标题行映射
     *                 List LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
     *                 fieldMap.put("name","姓名");
     *                 fieldMap.put("age","年龄");
     * @param dataset  要显示数据的map的集合, 命名列需要与标题列的命名对应
     * @param out      与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中*/
    public static void exportExcel(LinkedHashMap<String, String> fieldMap,
                                      List<Map<String,Object>> dataset,
                                      String sheetName,
                                      OutputStream out) {
        exportExcel(fieldMap, dataset, sheetName, 0, 0, out, null);
    }


    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于单个sheet
     *
     * @param fieldMap 表格属性名, 标题行映射
     *                 List LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
     *                 fieldMap.put("name","姓名");
     *                 fieldMap.put("age","年龄");
     * @param dataset  要显示数据的map的集合, 命名列需要与标题列的命名对应
     * @param startRow  开始的行
     * @param startColumn 开始的列
     * @param pattern  日期类型的方式
     * @param out      与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中*/
    public static void exportExcel(LinkedHashMap<String, String> fieldMap,
                                   List<Map<String,Object>> dataset,
                                   String sheetName,
                                   int startRow,
                                   int startColumn,
                                   OutputStream out,
                                   String pattern) {

        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();


        HSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            // 生成一个表格
            sheet = workbook.createSheet(sheetName);
        }

        write2Sheet(sheet, fieldMap, dataset, startRow, startColumn,  out,pattern);
        try {
            workbook.write(out);
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
    }

    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于单个sheet
     *
     * @param fieldMap 表格属性名, 标题行映射
     *                 List LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
     *                 fieldMap.put("name","姓名");
     *                 fieldMap.put("age","年龄");
     * @param dataMapList  要显示数据的map的集合, 命名列需要与标题列的命名对应
     * @param startRow  开始的行
     * @param startColumn 开始的列
     * @param pattern  日期类型的方式
     * @param out      与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中*/
    public static void write2Sheet(HSSFSheet sheet,
                                   LinkedHashMap<String, String> fieldMap,
                                   List<Map<String,Object>> dataMapList,
                                   int startRow,
                                   int startColumn,
                                   OutputStream out,
                                   String pattern) {

        //定义存放英文字段名和中文字段名的数组
        String[] enFields = new String[fieldMap.size()];
        String[] cnFields = new String[fieldMap.size()];

        //填充数组
        int count = 0;
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            enFields[count] = entry.getKey();
            cnFields[count] = entry.getValue();
            count++;
        }

        //填充表头
        // 产生表格标题行
        HSSFRow row = sheet.createRow(startRow);

        CellStyle  titleStyle = buildTitleStyle(sheet.getWorkbook());

        for (int i = 0; i < cnFields.length; i++) {
            HSSFCell cell = row.createCell(startColumn+i);
            cell.setCellStyle(titleStyle);
            HSSFRichTextString text = new HSSFRichTextString(cnFields[i]);
            cell.setCellValue(text);
        }


        CellStyle  bodyStyle = buildBodyStyle(sheet.getWorkbook());
        // 遍历集合数据，产生数据行
        Iterator<Map<String,Object>> it = dataMapList.iterator();
        int index = startRow;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            Map<String,Object> t = it.next();
            try {

                //List<FieldForSortting> fields = sortFieldByAnno(t.getClass());
                int cellNum = startColumn;
                //for (int i = 0; i < fields.size(); i++) {
                for (int i = 0; i < enFields.length; i++) {
                    Object value = t.get(enFields[i]);

                    HSSFCell cell = row.createCell(cellNum);
                    cell.setCellStyle(bodyStyle);
                    //Field field = fields.get(i).getField();
                    //field.setAccessible(true);
                    //Object value = field.get(t);
                    String textValue = null;
                    if (value instanceof Integer) {
                        int intValue = (Integer) value;
                        cell.setCellValue(intValue);
                    } else if (value instanceof Float) {
                        float fValue = (Float) value;
                        cell.setCellValue(fValue);
                    } else if (value instanceof Double) {
                        double dValue = (Double) value;
                        cell.setCellValue(dValue);
                    } else if (value instanceof Long) {
                        long longValue = (Long) value;
                        cell.setCellValue(longValue);
                    } else if (value instanceof Boolean) {
                        boolean bValue = (Boolean) value;
                        cell.setCellValue(bValue);
                    } else if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                    } else if (value instanceof String[]) {
                        String[] strArr = (String[]) value;
                        for (int j = 0; j < strArr.length; j++) {
                            String str = strArr[j];
                            cell.setCellValue(str);
                            if (j != strArr.length - 1) {
                                cellNum++;
                                cell = row.createCell(cellNum);
                            }
                        }
                    } else if (value instanceof Double[]) {
                        Double[] douArr = (Double[]) value;
                        for (int j = 0; j < douArr.length; j++) {
                            Double val = douArr[j];
                            // 资料不为空则set Value
                            if (val != null) {
                                cell.setCellValue(val);
                            }

                            if (j != douArr.length - 1) {
                                cellNum++;
                                cell = row.createCell(cellNum);
                            }
                        }
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        String empty = "";
                        textValue = value == null ? empty : value.toString();
                    }
                    if (textValue != null) {
                        HSSFRichTextString richString = new HSSFRichTextString(textValue);
                        cell.setCellValue(richString);
                    }

                    cellNum++;
                }
            } catch (Exception e) {
                logger.error(e.toString(), e);
            }
        }
        // 设定自动宽度
        for (int i = 0; i < cnFields.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * 每个sheet的写入
     *
     * @param sheet    页签
     * @param fieldMap 表头
     * @param dataset  数据集合
     * @param datePattern  日期格式
     */
    private static <T> void write2Sheet(HSSFSheet sheet,
                                        LinkedHashMap<String, String> fieldMap,
                                        Collection<T> dataset,
                                        int startRow,
                                        int startColumn,
                                        String datePattern) {


        //定义存放英文字段名和中文字段名的数组
        String[] enFields = new String[fieldMap.size()];
        String[] cnFields = new String[fieldMap.size()];

        //填充数组
        int count = 0;
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            enFields[count] = entry.getKey();
            cnFields[count] = entry.getValue();
            count++;
        }

        CellStyle  titleStyle = buildTitleStyle(sheet.getWorkbook());
        //填充表头
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < cnFields.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(cnFields[i]);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(text);
        }

        CellStyle bodyStyle = buildBodyStyle(sheet.getWorkbook());

        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = startRow;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            try {

                //List<FieldForSortting> fields = sortFieldByAnno(t.getClass());
                int cellNum = startColumn;
                //for (int i = 0; i < fields.size(); i++) {
                for (int i = 0; i < enFields.length; i++) {
                    Object value = getFieldValueByNameSequence(enFields[i], t );

                    HSSFCell cell = row.createCell(cellNum);
                    cell.setCellStyle(bodyStyle);
                    //Field field = fields.get(i).getField();
                    //field.setAccessible(true);
                    //Object value = field.get(t);
                    String textValue = null;
                    if (value instanceof Integer) {
                        int intValue = (Integer) value;
                        cell.setCellValue(intValue);
                    } else if (value instanceof Float) {
                        float fValue = (Float) value;
                        cell.setCellValue(fValue);
                    } else if (value instanceof Double) {
                        double dValue = (Double) value;
                        cell.setCellValue(dValue);
                    } else if (value instanceof Long) {
                        long longValue = (Long) value;
                        cell.setCellValue(longValue);
                    } else if (value instanceof Boolean) {
                        boolean bValue = (Boolean) value;
                        cell.setCellValue(bValue);
                    } else if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
                        textValue = sdf.format(date);
                    } else if (value instanceof String[]) {
                        String[] strArr = (String[]) value;
                        for (int j = 0; j < strArr.length; j++) {
                            String str = strArr[j];
                            cell.setCellValue(str);
                            if (j != strArr.length - 1) {
                                cellNum++;
                                cell = row.createCell(cellNum);
                            }
                        }
                    } else if (value instanceof Double[]) {
                        Double[] douArr = (Double[]) value;
                        for (int j = 0; j < douArr.length; j++) {
                            Double val = douArr[j];
                            // 资料不为空则set Value
                            if (val != null) {
                                cell.setCellValue(val);
                            }

                            if (j != douArr.length - 1) {
                                cellNum++;
                                cell = row.createCell(cellNum);
                            }
                        }
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        String empty = "";
                        textValue = value == null ? empty : value.toString();
                    }
                    if (textValue != null) {
                        HSSFRichTextString richString = new HSSFRichTextString(textValue);
                        cell.setCellValue(richString);
                    }

                    cellNum++;
                }
            } catch (Exception e) {
                logger.error(e.toString(), e);
            }
        }
        // 设定自动宽度
        for (int i = 0; i < cnFields.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }


    /*<-------------------------辅助的私有方法----------------------------------------------->*/


    /**
     * 构建标题行的格式
     * @param wb
     * @return
     */
    public static CellStyle buildTitleStyle(HSSFWorkbook wb){
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;
    }

    /**
     * 构建标题行的格式
     * @param wb
     * @return
     */
    public static CellStyle buildBodyStyle(HSSFWorkbook wb){
        CellStyle style = wb.createCellStyle();
        HSSFCellStyle style2 =  wb.createCellStyle();
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style2;
    }










    /**
     * @param fieldName 字段名
     * @param o         对象
     * @return 字段值
     * @MethodName : getFieldValueByName
     * @Description : 根据字段名获取字段值
     */
    private static Object getFieldValueByName(String fieldName, Object o) throws Exception {

        Object value = null;
        Field field = getFieldByName(fieldName, o.getClass());

        if (field != null) {
            field.setAccessible(true);
            value = field.get(o);
        } else {
            throw new ExcelException(o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
        }

        return value;
    }

    /**
     * @param fieldName 字段名
     * @param clazz     包含该字段的类
     * @return 字段
     * @MethodName : getFieldByName
     * @Description : 根据字段名获取字段
     */
    private static Field getFieldByName(String fieldName, Class<?> clazz) {
        //拿到本类的所有字段
        Field[] selfFields = clazz.getDeclaredFields();

        //如果本类中存在该字段，则返回
        for (Field field : selfFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        //否则，查看父类中是否存在此字段，如果有则返回
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            return getFieldByName(fieldName, superClazz);
        }

        //如果本类和父类都没有，则返回空
        return null;
    }


    /**
     * @param fieldNameSequence 带路径的属性名或简单属性名
     * @param o                 对象
     * @return 属性值
     * @throws Exception
     * @MethodName : getFieldValueByNameSequence
     * @Description :
     * 根据带路径或不带路径的属性名获取属性值
     * 即接受简单属性名，如userName等，又接受带路径的属性名，如student.department.name等
     */
    private static Object getFieldValueByNameSequence(String fieldNameSequence, Object o) throws Exception {

        Object value = null;

        //将fieldNameSequence进行拆分
        String[] attributes = fieldNameSequence.split("\\.");
        if (attributes.length == 1) {
            value = getFieldValueByName(fieldNameSequence, o);
        } else {
            //根据属性名获取属性对象
            Object fieldObj = getFieldValueByName(attributes[0], o);
            String subFieldNameSequence = fieldNameSequence.substring(fieldNameSequence.indexOf(".") + 1);
            value = getFieldValueByNameSequence(subFieldNameSequence, fieldObj);
        }
        return value;

    }

    /**
     * 把Excel的数据封装成voList
     *
     * @param clazz       vo的Class
     * @param inputStream excel输入流
     * @param pattern     如果有时间数据，设定输入格式。默认为"yyy-MM-dd"
     * @param logs        错误log集合
     * @param arrayCount  如果vo中有数组类型,那就按照index顺序,把数组应该有几个值写上.
     * @return voList
     * @throws RuntimeException
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> importExcel(Class<T> clazz,
                                                InputStream inputStream,
                                                String pattern,
                                                ExcelLogs logs,
                                                Integer... arrayCount) {
        HSSFWorkbook workBook = null;
        try {
            workBook = new HSSFWorkbook(inputStream);
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
        List<T> list = new ArrayList<T>();
        HSSFSheet sheet = workBook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();
        try {
            List<ExcelLog> logList = new ArrayList<ExcelLog>();
            // Map<title,index>
            Map<String, Integer> titleMap = new HashMap<String, Integer>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0) {
                    if (clazz == Map.class) {
                        // 解析map用的key,就是excel标题行
                        Iterator<Cell> cellIterator = row.cellIterator();
                        Integer index = 0;
                        while (cellIterator.hasNext()) {
                            String value = cellIterator.next().getStringCellValue();
                            titleMap.put(value, index);
                            index++;
                        }
                    }
                    continue;
                }
                // 整行都空，就跳过
                boolean allRowIsNull = true;
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Object cellValue = getCellValue(cellIterator.next());
                    if (cellValue != null) {
                        allRowIsNull = false;
                        break;
                    }
                }
                if (allRowIsNull) {
                    logger.warn("Excel row " + row.getRowNum() + " all row value is null!");
                    continue;
                }
                T t = null;
                StringBuilder log = new StringBuilder();
                if (clazz == Map.class) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    for (String k : titleMap.keySet()) {
                        Integer index = titleMap.get(k);
                        String value = row.getCell(index).getStringCellValue();
                        map.put(k, value);
                    }
                    list.add((T) map);

                } else {
                    t = clazz.newInstance();
                    int arrayIndex = 0;// 标识当前第几个数组了
                    int cellIndex = 0;// 标识当前读到这一行的第几个cell了
                    List<FieldForSortting> fields = sortFieldByAnno(clazz);
                    for (FieldForSortting ffs : fields) {
                        Field field = ffs.getField();
                        field.setAccessible(true);
                        if (field.getType().isArray()) {
                            Integer count = arrayCount[arrayIndex];
                            Object[] value = null;
                            if (field.getType().equals(String[].class)) {
                                value = new String[count];
                            } else {
                                // 目前只支持String[]和Double[]
                                value = new Double[count];
                            }
                            for (int i = 0; i < count; i++) {
                                Cell cell = row.getCell(cellIndex);
                                String errMsg = validateCell(cell, field, cellIndex);
                                if (StringUtils.isBlank(errMsg)) {
                                    value[i] = getCellValue(cell);
                                } else {
                                    log.append(errMsg);
                                    log.append(";");
                                    logs.setHasError(true);
                                }
                                cellIndex++;
                            }
                            field.set(t, value);
                            arrayIndex++;
                        } else {
                            Cell cell = row.getCell(cellIndex);
                            String errMsg = validateCell(cell, field, cellIndex);
                            if (StringUtils.isBlank(errMsg)) {
                                Object value = null;
                                // 处理特殊情况,Excel中的String,转换成Bean的Date
                                if (field.getType().equals(Date.class)
                                        && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                                    Object strDate = getCellValue(cell);
                                    try {
                                        value = new SimpleDateFormat(pattern).parse(strDate.toString());
                                    } catch (ParseException e) {

                                        errMsg =
                                                MessageFormat.format("the cell [{0}] can not be converted to a date ",
                                                        CellReference.convertNumToColString(cell.getColumnIndex()));
                                    }
                                } else {
                                    value = getCellValue(cell);
                                    // 处理特殊情况,excel的value为String,且bean中为其他,且defaultValue不为空,那就=defaultValue
                                    ExcelCell annoCell = field.getAnnotation(ExcelCell.class);
                                    if (value instanceof String && !field.getType().equals(String.class)
                                            && StringUtils.isNotBlank(annoCell.defaultValue())) {
                                        value = annoCell.defaultValue();
                                    }
                                }
                                field.set(t, value);
                            }
                            if (StringUtils.isNotBlank(errMsg)) {
                                log.append(errMsg);
                                log.append(";");
                                logs.setHasError(true);
                            }
                            cellIndex++;
                        }
                    }
                    list.add(t);
                    logList.add(new ExcelLog(t, log.toString(), row.getRowNum() + 1));
                }
            }
            logs.setLogList(logList);
        } catch (InstantiationException e) {
            throw new RuntimeException(MessageFormat.format("can not instance class:{0}",
                    clazz.getSimpleName()), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(MessageFormat.format("can not instance class:{0}",
                    clazz.getSimpleName()), e);
        }
        return list;
    }



    /**
     * 驗證Cell類型是否正確
     *
     * @param cell    cell單元格
     * @param field   欄位
     * @param cellNum 第幾個欄位,用於errMsg
     * @return
     */
    private static String validateCell(Cell cell, Field field, int cellNum) {
        String columnName = CellReference.convertNumToColString(cellNum);
        String result = null;
        Integer[] integers = validateMap.get(field.getType());
        if (integers == null) {
            result = MessageFormat.format("Unsupported type [{0}]", field.getType().getSimpleName());
            return result;
        }
        ExcelCell annoCell = field.getAnnotation(ExcelCell.class);
        if (cell == null
                || (cell.getCellType() == Cell.CELL_TYPE_STRING && StringUtils.isBlank(cell
                .getStringCellValue()))) {
            if (annoCell != null && annoCell.valid().allowNull() == false) {
                result = MessageFormat.format("the cell [{0}] can not null", columnName);
            }
            ;
        } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK && annoCell.valid().allowNull()) {
            return result;
        } else {
            List<Integer> cellTypes = Arrays.asList(integers);

            // 如果類型不在指定範圍內,並且沒有默認值
            if (!(cellTypes.contains(cell.getCellType()))
                    || StringUtils.isNotBlank(annoCell.defaultValue())
                    && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                StringBuilder strType = new StringBuilder();
                for (int i = 0; i < cellTypes.size(); i++) {
                    Integer intType = cellTypes.get(i);
                    strType.append(getCellTypeByInt(intType));
                    if (i != cellTypes.size() - 1) {
                        strType.append(",");
                    }
                }
                result =
                        MessageFormat.format("the cell [{0}] type must [{1}]", columnName, strType.toString());
            } else {
                // 类型符合验证,但值不在要求范围内的
                // String in
                if (annoCell.valid().in().length != 0 && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    String[] in = annoCell.valid().in();
                    String cellValue = cell.getStringCellValue();
                    boolean isIn = false;
                    for (String str : in) {
                        if (str.equals(cellValue)) {
                            isIn = true;
                        }
                    }
                    if (!isIn) {
                        result = MessageFormat.format("the cell [{0}] value must in {1}", columnName, in);
                    }
                }
                // 数字型
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    double cellValue = cell.getNumericCellValue();
                    // 小于
                    if (!Double.isNaN(annoCell.valid().lt())) {
                        if (!(cellValue < annoCell.valid().lt())) {
                            result =
                                    MessageFormat.format("the cell [{0}] value must less than [{1}]", columnName,
                                            annoCell.valid().lt());
                        }
                    }
                    // 大于
                    if (!Double.isNaN(annoCell.valid().gt())) {
                        if (!(cellValue > annoCell.valid().gt())) {
                            result =
                                    MessageFormat.format("the cell [{0}] value must greater than [{1}]", columnName,
                                            annoCell.valid().gt());
                        }
                    }
                    // 小于等于
                    if (!Double.isNaN(annoCell.valid().le())) {
                        if (!(cellValue <= annoCell.valid().le())) {
                            result =
                                    MessageFormat.format("the cell [{0}] value must less than or equal [{1}]",
                                            columnName, annoCell.valid().le());
                        }
                    }
                    // 大于等于
                    if (!Double.isNaN(annoCell.valid().ge())) {
                        if (!(cellValue >= annoCell.valid().ge())) {
                            result =
                                    MessageFormat.format("the cell [{0}] value must greater than or equal [{1}]",
                                            columnName, annoCell.valid().ge());
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 根据annotation的seq排序后的栏位
     *
     * @param clazz
     * @return
     */
    private static List<FieldForSortting> sortFieldByAnno(Class<?> clazz) {
        Field[] fieldsArr = clazz.getDeclaredFields();
        List<FieldForSortting> fields = new ArrayList<FieldForSortting>();
        List<FieldForSortting> annoNullFields = new ArrayList<FieldForSortting>();
        for (Field field : fieldsArr) {
            ExcelCell ec = field.getAnnotation(ExcelCell.class);
            if (ec == null) {
                // 没有ExcelCell Annotation 视为不汇入
                continue;
            }
            int id = ec.index();
            fields.add(new FieldForSortting(field, id));
        }
        fields.addAll(annoNullFields);
        sortByProperties(fields, true, false, "index");
        return fields;
    }

    @SuppressWarnings("unchecked")
    private static void sortByProperties(List<? extends Object> list, boolean isNullHigh,
                                         boolean isReversed, String... props) {
        if (CollectionUtils.isNotEmpty(list)) {
            Comparator<?> typeComp = ComparableComparator.getInstance();
            if (isNullHigh == true) {
                typeComp = ComparatorUtils.nullHighComparator(typeComp);
            } else {
                typeComp = ComparatorUtils.nullLowComparator(typeComp);
            }
            if (isReversed) {
                typeComp = ComparatorUtils.reversedComparator(typeComp);
            }

            List<Object> sortCols = new ArrayList<Object>();

            if (props != null) {
                for (String prop : props) {
                    sortCols.add(new BeanComparator(prop, typeComp));
                }
            }
            if (sortCols.size() > 0) {
                Comparator<Object> sortChain = new ComparatorChain(sortCols);
                Collections.sort(list, sortChain);
            }
        }
    }





}
