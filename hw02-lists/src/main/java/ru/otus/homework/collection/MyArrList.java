package ru.otus.homework.collection;

import org.jetbrains.annotations.NotNull;
import ru.otus.homework.entries.Dog;

import java.util.*;

public class MyArrList<T> implements List<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elementData;
    private int size;
    private static final Object[] DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA = new Object[DEFAULT_CAPACITY];

    public MyArrList() {
        this.elementData = DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] array) {
        if (array.length < size)
            return (T[]) Arrays.copyOf(elementData, size, array.getClass());

        System.arraycopy(elementData, 0, array, 0, size);
        if (array.length > size)
            array[size] = null;

        return array;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(T t) {
        ensureCapacityInternal(size + 1);
        elementData[size++] = t;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        if (index >= size)
            throw new IndexOutOfBoundsException("Индекс: " + index + ", Размер: " + size);
        T oldValue = (T) elementData[index];
        elementData[index] = element;
        return oldValue;
    }

    private void ensureCapacityInternal(int minCapacity) {
        if (elementData == DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA)
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);

        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    private void grow(int minCapacity) {
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void sort(Comparator<? super T> c) {
        Object[] array = this.toArray();
        Arrays.sort(array, (Comparator) c);
        ListIterator<T> iterator = this.listIterator();
        for (Object obj : array) {
            iterator.next();
            iterator.set((T) obj);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListItr(0);
    }

    private class Itr implements Iterator<T> {

        int cursor;
        int lastRet = -1;

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public T next() {
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = MyArrList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (T) elementData[lastRet = i];
        }
    }

    private class ListItr extends Itr implements ListIterator<T> {

        ListItr(int index) {
            super();
            cursor = index;
        }

        @Override
        public void set(T t) {
            if (lastRet < 0)
                throw new IllegalStateException();
            try {
                MyArrList.this.set(lastRet, t);
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

        public T previous() {
            throw new UnsupportedOperationException("Метод previous() не реализован");
        }

        public void add(T t) {
            throw new UnsupportedOperationException("Метод add(T t) не реализован");
        }
    }
////////////////////////////////////////////////////////////////////////////////////////

    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("Метод listIterator(int index) не реализован");
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Метод remove(Object o) не реализован");
    }

    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Метод containsAll(Collection<?> c) не реализован");
    }

    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException("Метод addAll(Collection<? extends T> c) не реализован");
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException("Метод addAll(int index, Collection<? extends T> c) не реализован");
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Метод removeAll(Collection<?> c) не реализован");
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Метод retainAll(Collection<?> c) не реализован");
    }

    public void clear() {
        throw new UnsupportedOperationException("Метод clear() не реализован");
    }

    public T get(int index) {
        throw new UnsupportedOperationException("Метод get(int index) не реализован");
    }

    public void add(int index, T element) {
        throw new UnsupportedOperationException("Метод add(int index, T element) не реализован");
    }

    public T remove(int index) {
        throw new UnsupportedOperationException("Метод remove(int index) не реализован");
    }

    public int indexOf(Object o) {
        throw new UnsupportedOperationException("Метод indexOf(Object o) не реализован");
    }

    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Метод lastIndexOf(Object o) не реализован");
    }

    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Метод subList(int fromIndex, int toIndex) не реализован");
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("Метод isEmpty() не реализован");
    }

    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Метод contains(Object o) не реализован");
    }
}
