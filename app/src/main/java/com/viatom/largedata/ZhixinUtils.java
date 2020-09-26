package com.viatom.largedata;

public class ZhixinUtils {

    public void unzipEcgFile() {

    }


    public static short[] byte2Short(byte[] oriBytes) {
        /**
         * 3字节转为2short
         */
        int len = oriBytes.length / 3;
        short[] outArr = new short[len * 2];

        for (int i = 0; i < len; i ++) {
            short s1 = (short) ((oriBytes[3*i] & 0xff) | ((oriBytes[3*i+1] & 0x0f) << 8));
            short s2 = (short) ((oriBytes[3*i+2] & 0xff) | (((oriBytes[3*i+1] & 0xff)>> 4) << 8));

            outArr[2*i] = s1;
            outArr[2*i+1] = s2;
        }

        return outArr;
    }

    public static float[] byte2Float(byte[] oriBytes) {
        /**
         * 3字节转为2 double
         */
        int len = oriBytes.length / 3;
        float[] outArr = new float[len * 2];

        for (int i = 0; i < len; i ++) {
            short s1 = (short) ((oriBytes[3*i] & 0xff) | ((oriBytes[3*i+1] & 0x0f) << 8));
            short s2 = (short) ((oriBytes[3*i+2] & 0xff) | (((oriBytes[3*i+1] & 0xff)>> 4) << 8));

            outArr[2*i] = (float) s1 * 3.3f / 4095;
            outArr[2*i+1] = (float) s2 * 3.3f / 4095;
        }

        return outArr;
    }
}
