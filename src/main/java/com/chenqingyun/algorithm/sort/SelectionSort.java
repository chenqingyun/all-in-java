package com.chenqingyun.algorithm.sort;

import java.util.Arrays;

/**
 * 选择排序
 *
 * @author chenqingyun
 * @date 2019-04-10 23:13.
 */
public class SelectionSort {
    public static void main(String[] args) {
        int[] a = {2,4,7,3,1,9,0};
        System.out.println(Arrays.toString(sort(a)));
    }

    /**
     * 从小到大排序
     * 每一次从待排序的数据元素中选出最小(或最大)的一个元素，存放待排序序列的起始位置(或末尾位置），交换两个位置上的元素
     * @param a
     * @return
     */
    private static int[] sort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int smallest = a[i];
            for (int j = i + 1; j < a.length; j++) {
               if(smallest > a[j]){
                   int temp = smallest;
                   smallest = a[j];
                   a[j] = temp;
               }
            }
            a[i] = smallest;
        }
        return a;
    }
}
