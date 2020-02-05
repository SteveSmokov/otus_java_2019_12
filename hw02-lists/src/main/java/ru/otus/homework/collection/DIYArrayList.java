package ru.otus.homework.collection;



import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

//http://developer.alexanderklimov.ru/android/java/generic.php
//https://docs.oracle.com/javase/6/docs/api/java/util/Collections.html#addAll(java.util.Collection,%20T...)
//https://docs.oracle.com/javase/6/docs/api/java/util/Collection.html#addAll(java.util.Collection)
public class DIYArrayList<T> implements List<T> {
    private int arraySize = 0;
    private Object[] array = new Object[arraySize];

    public DIYArrayList(List<T> list) {
    }

    @Override
    public int size() {
        return this.arraySize;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean add(T t) {
        Boolean result = false;
        resizeArray(this.arraySize+1);
        this.array[this.arraySize] = t;
        this.arraySize++;
        result = true;
        return result;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public T get(int index) {
        return (T) this.array[index];
    }

    @Override
    public T set(int index, T element) {
        if (index <= this.arraySize) {
            Object replacedElement = this.array[index];
            this.array[index] = element;
            return (T) replacedElement;
        } else {
            return null;
        }
    }

    @Override
    public void add(int index, T element) {

    }

    @Override
    public T remove(int index) {
        if (index < this.arraySize) {
            Object deletedElement = this.array[index];
            for (int i=index; i<this.arraySize-1; i++){
                this.array[i] = this.array[i+1];
            }
            this.arraySize--;
            resizeArray(this.arraySize);
            return (T) deletedElement;
        } else {
            return null;
        }
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }

    private void resizeArray(int length){
       Object[] newArray = new Object[length];
       if (this.arraySize>0) {
           System.arraycopy(this.array, 0, newArray, 0, this.arraySize);
       }
       this.array = newArray;
    }

    @Override
    public void sort(Comparator<? super T> c) {
//        Object[] a = this.array;
//        Arrays.sort(a, (Comparator) c);
//        ListIterator<T> i = this.listIterator();
//        for (Object e : a) {
//            i.next();
//            i.set((T) e);
//        }
    }
}
