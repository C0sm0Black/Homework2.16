package com.example.homework216.domain;

import com.example.homework216.exception.NotContainsIntegerException;
import com.example.homework216.exception.NullException;
import com.example.homework216.exception.WrongIndexException;

import java.util.Arrays;
import java.util.stream.IntStream;

public class IntegerListImpl implements IntegerList{

    private Integer[] arr;
    private int size;

    public IntegerListImpl(int size) {

        this.arr = new Integer[size];
        this.size = 0;

    }

    public IntegerListImpl(Integer[] arr, int size) {

        this.arr = arr;
        this.size = size;

    }

    @Override
    public Integer add(Integer item) {

        checkNullException(item);
        checkForSize();

        arr[size] = item;
        size++;

        return item;

    }

    @Override
    public Integer add(int index, Integer item) {

        checkNullException(item);
        checkIndexException(index);
        addWithDisplacement(index, item);

        return item;

    }

    @Override
    public Integer set(int index, Integer item) {

        checkNullException(item);
        checkIndexException(index);

        arr[index] = item;

        return item;

    }

    @Override
    public Integer remove(Integer item) {

        if (!contains(item)) {
            throw new NotContainsIntegerException("NotContainsIntegerException");
        }

        removeWithDisplacement(indexOf(item));

        return item;

    }

    @Override
    public Integer remove(int index) {

        checkIndexException(index);
        removeWithDisplacement(index);

        return arr[index];

    }

    @Override
    public boolean contains(Integer item) {

        checkNullException(item);
        return binarySearch(item);

    }

    @Override
    public int indexOf(Integer item) {

        checkNullException(item);

        return IntStream.range(0, size)
                .filter(i -> arr[i].equals(item))
                .findFirst()
                .orElse(-1);

    }

    @Override
    public int lastIndexOf(Integer item) {

        checkNullException(item);

        return IntStream.iterate(size - 1, i -> i >= 0, i -> i + 1)
                .filter(i -> arr[i].equals(item))
                .findFirst()
                .orElse(-1);

    }

    @Override
    public Integer get(int index) {

        checkIndexException(index);

        return arr[index];

    }

    @Override
    public boolean equals(IntegerList otherList) {

        if (size != otherList.size()) {
            return false;
        }

        for (int i = 0; i < size + 1; i++) {

            if (!arr[i].equals(otherList.get(i))) {
                return false;
            }

        }

        return true;

    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        arr = new Integer[5];
    }

    @Override
    public Integer[] toArray() {

        Integer[] newArr = new Integer[arr.length];
        System.arraycopy(arr, 0, newArr, 0, arr.length);

        return newArr;
    }

    public IntegerListImpl copy() {
        return new IntegerListImpl(Arrays.copyOf(this.arr, size), size);
    }

    private void checkNullException(Integer item) {

        if (item == null) {
            throw new NullException("NullException");
        }

    }

    private void checkForSize() {

        if (arr.length - 1 == size) {

            Integer[] newArr = new Integer[arr.length * 2 + 1];
            System.arraycopy(arr, 0, newArr, 0, size);
            arr = newArr;

        }

    }

    private void checkIndexException(int index) {

        if (index > size) {
            throw new WrongIndexException("WrongIndexException");
        }

    }

    private void addWithDisplacement(int index, Integer item) {

        checkForSize();

        Integer[] newArr = new Integer[arr.length];

        if (index == 0) {

            System.arraycopy(arr, 0, newArr, 1, size + 1);
            arr = newArr;
            arr[0] = item;
            size++;

        } else if (index == size) {

            arr[index] = item;
            size++;

        } else {

            System.arraycopy(arr, 0, newArr, 0, index);
            System.arraycopy(arr, index, newArr, index + 1, size + 1);
            newArr[index] = item;
            arr = newArr;
            size++;

        }

    }

    private void removeWithDisplacement(int index) {

        Integer[] newArr = new Integer[arr.length];

        if (index == 0) {
            System.arraycopy(arr, 1, newArr, 0, size -1);
        } else {

            System.arraycopy(arr, 0, newArr, 0, index);
            System.arraycopy(arr, index + 1, newArr, index, size + 1);

        }

        arr = newArr;
        size--;

    }

    private void sortBubble() {

        for (int i = 0; i < size - 1; i++) {

            for (int j = 0; j < size - 1 - i; j++) {

                if (arr[j] > arr[j + 1] && j != size - 1) {
                    swapElements(j, j + 1);
                }

            }
        }

    }

    private void sortSelection() {

        for (int i = 0; i < size - 1; i++) {

            int minElementIndex = i;

            for (int j = i + 1; j < size; j++) {

                if (arr[j] < arr[minElementIndex]) {
                    minElementIndex = j;
                }

            }

            if (i != minElementIndex) {
                swapElements(i, minElementIndex);
            }

        }

    }

    private void sortInsertion() {

        for (int i = 1; i < size; i++) {

            int temp = arr[i];
            int j = i;

            while (j > 0 && arr[j - 1] >= temp) {

                arr[j] = arr[j - 1];
                j--;

            }

            arr[j] = temp;

        }

    }

    public void quickSort(int begin, int end) {

        if (begin < end) {

            int partitionIndex = partition(begin, end);

            quickSort(begin, partitionIndex - 1);
            quickSort(partitionIndex + 1, end);

        }

    }

    private int partition(int begin, int end) {

        int pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {

            if (arr[j] <= pivot) {

                i++;
                swapElements(i, j);

            }

        }

        swapElements(i + 1, end);
        return i + 1;

    }

    private void swapElements(int i, int j) {

        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;

    }

    private boolean binarySearch(Integer element) {

        IntegerListImpl otherList = copy();
        otherList.quickSort(0, size - 1);

        Integer[] newArr = otherList.toArray();

        int min = 0;
        int max = size - 1;

        while (min <= max) {

            int mid = (min + max) / 2;

            if (element == newArr[mid]) {
                return true;
            }

            if (element < newArr[mid]) {
                max = mid - 1;
            } else {
                min = mid + 1;
            }

        }

        return false;

    }

    @Override
    public String toString() {

        StringBuilder b = new StringBuilder();
        int iMax = size;
        b.append('[');

        for (int i = 0; i < iMax; i++) {
            b.append(arr[i]);

            if (!(i == iMax - 1)) {
                b.append(", ");
            }

        }

        b.append(']');

        return "IntegerListImpl {" +
                "arr = " + b.toString() +
                ", size = " + size +
                '}';

    }

}
