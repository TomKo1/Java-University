package com.company.tomek.utilities;

public class BinaryUtilities {

    /**
     *
     * @param binaryArr array containing binary representation of double
     * @param downBound index in array frow which we start converting
     * @param upperBound index to which (excluding) we convert
     * @return binary value as double
     */
    public static double convertToDecimal(int[] binaryArr, int downBound, int upperBound) {
        double base = 1, valueToReturn = 0;
        for(int i = downBound; i < upperBound; i++) {
            if(binaryArr[i] == 1)
                valueToReturn += base;

            base = base*2;
        }
        valueToReturn = (valueToReturn / 1024) * 10.1;

        return valueToReturn;
    }
}
