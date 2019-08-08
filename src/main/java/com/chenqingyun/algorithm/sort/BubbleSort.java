package com.chenqingyun.algorithm.sort;

import java.util.Arrays;

/**
 * @author chenqingyun
 * @date 2019-08-08 14:15.
 *
 * 冒泡排序
 */
public class BubbleSort {

    public static void main(String[] args) {
        int[] a = {2,4,7,3,1,9,0};
        sort(a);
        System.out.println(Arrays.toString(a));
    }

    public static void sort(int[] arr){
        int length = arr.length;

        for(int i=0;i<length;i++){
            for(int j=1;j<length-i;j++){
                if(arr[j-1] > arr[j]){
                    int temp = arr[j-1];
                    arr[j-1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }
}
