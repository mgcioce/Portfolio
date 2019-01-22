import java.util.Iterator;
import java.util.Objects;

/**
*   File: Array.java
*   Date: 12/26/18
*   @author Mike Cioce
*   Purpose: This class is used to provide functions for building and maintaining
*       a dynamically sized array.
*/

/**
*   Dynamically sized version of an array of a generic type.
*   @param <T> - a type T which is used to define the type that the array
*       can hold.
*/
public class Array<T> implements Iterable<T>{

    private T[] array;
    private int numElements;


    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator(numElements, array.length);
    }

    private class ArrayIterator implements Iterator<T> {
        private int currentIndex;
        private int numEl;
        private int length;

        public ArrayIterator(int n, int arrayLength) {
            super();
            currentIndex = 0;
            numEl = n;
            length = arrayLength;
        }

        @Override
        public boolean hasNext() {
            for (int i = currentIndex; i < length; i++) {
                T nextLocation = getElement(i);
                if (!Objects.isNull(nextLocation)) {
                    currentIndex = i;
                    return true;
                }
            }
            return false;
        }

        @Override
        public T next() {
            T nextElement = getElement(currentIndex++);
            return nextElement;
        }
    }

    public Array(int initialSize) {
        if (initialSize > 0)
            array = (T[]) new Object[initialSize];
        else
            array = (T[]) new Object[8];
        numElements = 0;
    }

    public void exchange(T a, T b, int i, int j) {
        array[i] = b;
        array[j] = a;
    }

    public void add(T element) {
        int index = numElements++;
        //System.out.println("Index: " + index);
        array[index] = element;
        array = checkSize(array, numElements);
    }

    public void remove() {
        if (numElements > 0) {
            int index = --numElements;
            //System.out.println("Index: " + index);
            array[index] = null;
        }
        array = checkSize(array,numElements);
    }

    public void remove(int index) {
        if (!Objects.isNull(array[index])) {
            array[index] = null;
            numElements--;   
        }

    }

    public T getElement(int i) {
        T returnElement = null;
        if ( (i >= 0) && (i <= (array.length - 1)) )
            returnElement = array[i];
        else {
            System.out.println("index out of bounds");
        }
        return returnElement;
    }

    private T[] resizeLarger(T[] A, int N) {
        int size = A.length;
        T[] array2 = (T[]) new Object[size*2];
        for (int i = 0; i < N; i++) {
            array2[i] = A[i];
        }
        return array2;
    }

    private T[] resizeSmaller(T[] A, int N) {
        int size = A.length;
        T[] array2 = (T[]) new Object[size/2];
        for (int i = 0; i < N; i++) {
            array2[i] = A[i];
        }
        return array2;
    }

    private T[] checkSize(T[] A, int N) {
        double n = (double) N;
        double size = (double) A.length;
        double ratio = n/size;
        if (ratio >= 0.75) //if array is too small
            A = resizeLarger(A,N);
        if (ratio <= 0.25 && N > 0) // if array is too big
            A = resizeSmaller(A,N);
        return A;
    }

    public int numberOfElements() {
        return numElements;
    }
}