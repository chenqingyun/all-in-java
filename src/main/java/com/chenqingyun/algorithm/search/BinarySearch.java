package com.chenqingyun.algorithm.search;

/**
 * 二分查找法
 * 最坏时间复杂度：O(log n)
 * 最优时间复杂度：O(1)
 * 平均时间复杂度：O(log n)
 * 最坏空间复杂度	循环： O(1)   递归： O(log n)
 *
 * @author chenqingyun
 * @date 2019-04-09 04:13.
 */
public class BinarySearch {
    public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 6, 7, 9, 10};
        System.out.println(binarySearch(array, 9));
        System.out.println(binarySeatchWihtRecursion(array, 0, 0, array.length - 1));
    }

    /**
     * while 循环实现
     *
     * @param array
     * @param target
     * @return
     */
    public static int binarySearch(int[] array, int target) {
        if (array == null || array.length == 0) {
            return -1;
        }
        int lowIndex = 0;
        int highIndex = array.length - 1;
        while (lowIndex <= highIndex) {
            //int midIndex = (lowIndex + highIndex) / 2;
            // 防止算术溢出
            int midIndex = lowIndex + (highIndex - lowIndex) / 2;
            if (array[midIndex] > target) {
                highIndex = midIndex - 1;
            } else if (array[midIndex] < target) {
                lowIndex = midIndex + 1;
            } else {
                return midIndex;
            }
        }
        return -1;
    }

    /**
     * 递归实现
     *
     * @param array
     * @param target
     * @param lowIndex
     * @param highIndex
     * @return
     */
    public static int binarySeatchWihtRecursion(int[] array, int target, int lowIndex, int highIndex) {
        if (lowIndex > highIndex) {
            return -1;
        }
        int midIndex = lowIndex + (highIndex - lowIndex) / 2;
        int value = array[midIndex];
        if (value == target) {
            return midIndex;
        }
        if (value > target) {
            return binarySeatchWihtRecursion(array, target, lowIndex, midIndex - 1);
        } else {
            return binarySeatchWihtRecursion(array, target, midIndex + 1, highIndex);
        }
    }

    /**
     * Arrays.binarySearch 源码
     *
     * @param a
     * @param fromIndex
     * @param toIndex
     * @param key
     * @return
     */
    public static int binarySearch0(int[] a, int fromIndex, int toIndex,
                                    int key) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = a[mid];

            if (midVal < key)
                low = mid + 1;
            else if (midVal > key)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found.
    }

}
