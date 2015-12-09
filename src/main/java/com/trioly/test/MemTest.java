package com.trioly.test;

import com.trioly.util.MemcacheUtil;

/**
 * Created by maidou on 15/12/3.
 */
public class MemTest {
    public static void main(String[] args) throws Exception{
        MemcacheUtil.getInstance().add("key","val");
    }
}
