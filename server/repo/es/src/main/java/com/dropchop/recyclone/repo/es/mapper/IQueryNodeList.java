package com.dropchop.recyclone.repo.es.mapper;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 01. 20.
 */
@SuppressWarnings("unused")
public interface IQueryNodeList extends IQueryNode, List<Object> {
}

@SuppressWarnings("unused")
class QueryNodeList extends QueryNode implements IQueryNodeList {

  private final List<Object> delegate = new ArrayList<>();

  public QueryNodeList() {
  }

  public QueryNodeList(IQueryNode parent) {
    super(parent);
  }

  @Override
  public int size() {
    return delegate.size();
  }

  @Override
  public boolean isEmpty() {
    return delegate.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return delegate.contains(o);
  }

  @Override
  public Iterator<Object> iterator() {
    return delegate.iterator();
  }

  @Override
  public Object[] toArray() {
    return delegate.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return delegate.toArray(a);
  }

  @Override
  public boolean add(Object o) {
    return delegate.add(o);
  }

  @Override
  public boolean remove(Object o) {
    return delegate.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return delegate.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<?> c) {
    return delegate.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<?> c) {
    return delegate.addAll(index, c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return delegate.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return delegate.retainAll(c);
  }

  @Override
  public void replaceAll(UnaryOperator<Object> operator) {
    delegate.replaceAll(operator);
  }

  @Override
  public void sort(Comparator<? super Object> c) {
    delegate.sort(c);
  }

  @Override
  public void clear() {
    delegate.clear();
  }

  @Override
  public boolean equals(Object o) {
    return delegate.equals(o);
  }

  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  @Override
  public Object get(int index) {
    return delegate.get(index);
  }

  @Override
  public Object set(int index, Object element) {
    return delegate.set(index, element);
  }

  @Override
  public void add(int index, Object element) {
    delegate.add(index, element);
  }

  @Override
  public Object remove(int index) {
    return delegate.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return delegate.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return delegate.lastIndexOf(o);
  }

  @Override
  public ListIterator<Object> listIterator() {
    return delegate.listIterator();
  }

  @Override
  public ListIterator<Object> listIterator(int index) {
    return delegate.listIterator(index);
  }

  @Override
  public List<Object> subList(int fromIndex, int toIndex) {
    return delegate.subList(fromIndex, toIndex);
  }

  @Override
  public Spliterator<Object> spliterator() {
    return delegate.spliterator();
  }

  @Override
  public boolean removeIf(Predicate<? super Object> filter) {
    return delegate.removeIf(filter);
  }

  @Override
  public Stream<Object> stream() {
    return delegate.stream();
  }

  @Override
  public Stream<Object> parallelStream() {
    return delegate.parallelStream();
  }

  @Override
  public void forEach(Consumer<? super Object> action) {
    delegate.forEach(action);
  }
}
