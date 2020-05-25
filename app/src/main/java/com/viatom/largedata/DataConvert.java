package com.viatom.largedata;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;

public class DataConvert {

    public static final byte COM_MAX_VAL = 127;
    public static final byte COM_MIN_VAL = -127;
    public static final short COM_EXTEND_MAX_VAL = 382;
    public static final short COM_EXTEND_MIN_VAL = -382;

    public static final byte COM_RET_ORIGINAL = -128;
    public static final byte COM_RET_POSITIVE = 127;
    public static final byte COM_RET_NEGATIVE = -127;

    public static final short UNCOM_RET_INVALI = -32768;

    public byte unCompressNum;
    public int lastCompressData;

    public DataConvert() {
        this.unCompressNum = 0;
        this.lastCompressData = 0;
    }

    public int unCompressAlgECG(byte compressData) {
        int ecgData = 0;
        //标志位
        switch (unCompressNum) {
            case 0:
                if (compressData == COM_RET_ORIGINAL) {
                    unCompressNum = 1;
                    ecgData = UNCOM_RET_INVALI;
                } else if (compressData == COM_RET_POSITIVE){		//正
                    unCompressNum = 3;
                    ecgData = UNCOM_RET_INVALI;
                } else if (compressData == COM_RET_NEGATIVE) {		//负
                    unCompressNum = 4;
                    ecgData = UNCOM_RET_INVALI;
                } else {
                    ecgData = lastCompressData + compressData;
                    lastCompressData = ecgData;
                }
                break;
            case 1:			// 原始数据字节低位
//                lastCompressData = compressData & 0xFFFF;
                lastCompressData = compressData & 0xFF;
                unCompressNum = 2;
                ecgData = UNCOM_RET_INVALI;
                break;
            case 2:			//原始数据字节高位
                ecgData = lastCompressData + (compressData << 8);
                lastCompressData = ecgData;
                unCompressNum = 0;
                break;
            case 3:
                ecgData = COM_MAX_VAL + (lastCompressData + (compressData & 0xFF));
                lastCompressData = ecgData;
                unCompressNum = 0;
                break;
            case 4:
                ecgData = COM_MIN_VAL + (lastCompressData - (compressData & 0xFF));
                lastCompressData = ecgData;
                unCompressNum = 0;
                break;
            default:
                break;
        }
        return ecgData;
    }

    public static int[] unCompressAlgECG(byte[] tmpDataArray) {
        int[] tmpInt = new int[tmpDataArray.length];
        DataConvert convert = new DataConvert();
        for(int i = 0; i < tmpDataArray.length; i++) {
            int tmp = convert.unCompressAlgECG(tmpDataArray[i]);
            tmpInt[i] = tmp;
        }

        tmpInt = Arrays.stream(tmpInt)
                .filter(x -> x != -32768 )
                .map(operand -> {
                    if(operand == 32767) {
                        return 0;
                    } else {
                        return operand;
                    }
                }).toArray();

        return tmpInt;
    }
}
