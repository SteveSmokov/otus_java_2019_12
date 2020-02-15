package ru.otus.collections;

import java.util.*;
import java.util.function.Consumer;

public class DIYArrayList<E> implements List<E> {
    private int arraySize = 0;
    private static final int DEFAULT_ARRAY_SIZE = 10;
    transient Object[] array;
    private static final Object[] EMPTY_ELEMENTDATA = {};

    protected transient int modCount = 0;


    public DIYArrayList(@org.jetbrains.annotations.NotNull Collection<? extends E> c) {
        array = c.toArray();
        if ((arraySize = array.length) != 0) {
            if (array.getClass() != Object[].class)
                array = Arrays.copyOf(array, arraySize, Object[].class);
        } else {
            this.array = EMPTY_ELEMENTDATA;
        }
    }

    public DIYArrayList() {
        this.arraySize = DEFAULT_ARRAY_SIZE;
        this.array = new Object[DEFAULT_ARRAY_SIZE];
    }

    public DIYArrayList(int size) {
        if (size > 0) {
            this.arraySize = size;
            this.array = new Object[size];
        } else if (size == 0) {
            this.array = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Не правильное значение для размера массива");
        }
    }

    @Override
    public int size() {
        return this.arraySize;
    }

    @Override
    public boolean isEmpty() {
        return (this.arraySize == 0);
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Метод contains(Object o) не реализован");
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, arraySize);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < this.arraySize)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(this.array, this.arraySize, a.getClass());
        System.arraycopy(this.array, 0, a, 0, this.arraySize);
        if (a.length > this.arraySize)
            a[this.arraySize] = null;
        return a;
    }

    @Override
    public boolean add(E e) {
        Boolean result = false;
        resizeArray(this.arraySize+1);
        this.array[this.arraySize] = e;
        this.arraySize++;
        result = true;
        return result;
    }

    private void rangeCheckForAdd(int index) {
        if (index > this.arraySize || index < 0)
            throw new IndexOutOfBoundsException("Index: "+index+", Size: "+this.arraySize);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Метод remove(Object o) не реализован");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Метод containsAll(Collection<?> c) не реализован");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Метод  addAll(Collection<? extends E> c) не реализован");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Метод addAll(int index, Collection<? extends E> c) не реализован");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Метод removeAll(Collection<?> c) не реализован");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Метод retainAll(Collection<?> c) не реализован");
    }

    @Override
    public void clear() {
        modCount++;
        final Object[] es = this.array;
        for (int to = this.arraySize, i = this.arraySize = 0; i < to; i++)
            es[i] = null;
    }

    @Override
    public E get(int index) {
        return (E) this.array[index];
    }

    @Override
    public E set(int index, E element) {
        if (index <= this.arraySize) {
            Object replacedElement = this.array[index];
            this.array[index] = element;
            return (E) replacedElement;
        } else {
            return null;
        }
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        if (index < this.arraySize) {
            Object deletedElement = this.array[index];
            for (int i=index; i<this.arraySize-1; i++){
                this.array[i] = this.array[i+1];
            }
            this.arraySize--;
            resizeArray(this.arraySize);
            return (E) deletedElement;
        } else {
            return null;
        }
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException("Метод  не реализован");
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Метод  не реализован");
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        rangeCheckForAdd(index);
        return new ListItr(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    private void resizeArray(int length){
        Object[] newArray = new Object[length];
        if (this.arraySize>0) {
            System.arraycopy(this.array, 0, newArray, 0, this.arraySize);
        }
        this.array = newArray;
    }

    @Override
    public void sort(Comparator<? super E> c) {
        Object[] array = this.toArray();
        Arrays.sort(array, (Comparator) c);
        ListIterator<E> iterator = this.listIterator();
        for (Object obj : array) {
            iterator.next();
            iterator.set((E) obj);
        }
    }

    @Override
    public String toString() {
        String printArray = "[]";
        if (this.arraySize > 0) {
            printArray = '[' + this.array[0].toString();
            for (int i=1; i<this.arraySize; i++) {
                printArray += ", " + this.array[i].toString();
            }
            printArray += ']';
        }
        return printArray;
    }

    private class Itr implements Iterator<E> {

        int cursor;
        int lastRet = -1;

        public boolean hasNext() {
            return cursor != arraySize;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            int i = cursor;
            if (i >= arraySize)
                throw new NoSuchElementException();
            Object[] elementData = DIYArrayList.this.array;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }
    }

    private class ListItr extends Itr implements ListIterator<E> {

        ListItr(int index) {
            super();
            cursor = index;
        }

        @Override
        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            try {
                DIYArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Метод remove() не реализован");
        }

        public boolean hasPrevious() {
            throw new UnsupportedOperationException("Метод hasPrevious() не реализован");
        }

        public int nextIndex() {
            throw new UnsupportedOperationException("Метод nextIndex() не реализован");
        }

        public int previousIndex() {
            throw new UnsupportedOperationException("Метод previousIndex() не реализован");
        }

        public E previous() {
            throw new UnsupportedOperationException("Метод previous() не реализован");
        }

        public void add(E e) {
            throw new UnsupportedOperationException("Метод add(E e) не реализован");
        }
    }
}