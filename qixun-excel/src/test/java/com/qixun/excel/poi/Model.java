package com.qixun.excel.poi;



/**
 * The <code>Model</code>
 * 
 * @author SargerasWang Created at 2014年8月7日 下午5:09:29
 */
public class Model {
    private String a;
    private String b;
    private String c;

    public Model() {
    }

    public Model(String a, String b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * @return the a
     */
    public String getA() {
        return a;
    }

    /**
     * @param a
     *            the a to set
     */
    public void setA(String a) {
        this.a = a;
    }

    /**
     * @return the b
     */
    public String getB() {
        return b;
    }

    /**
     * @param b
     *            the b to set
     */
    public void setB(String b) {
        this.b = b;
    }

    /**
     * @return the c
     */
    public String getC() {
        return c;
    }

    /**
     * @param c
     *            the c to set
     */
    public void setC(String c) {
        this.c = c;
    }
}