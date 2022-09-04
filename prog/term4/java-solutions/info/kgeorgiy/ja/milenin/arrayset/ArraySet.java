package info.kgeorgiy.ja.milenin.arrayset;

import java.util.*;

public class ArraySet<N> extends AbstractSet<N> implements SortedSet<N> {
    private final List<N> elements;
    private final Comparator<? super N> comparator;

    public ArraySet() {
        elements = List.of();
        comparator = null;
    }

    public ArraySet(final Collection<? extends N> collection) {
        elements = List.copyOf(new TreeSet<>(collection));
        comparator = null;
    }

    public ArraySet(final Comparator<? super N> comparator) {
        this.comparator = comparator;
        elements = Collections.emptyList();
    }

    private ArraySet(final List<N> elements, final Comparator<? super N> comparator) {
        this.elements = elements;
        this.comparator = comparator;
    }

    public ArraySet(final Collection<? extends N> collection, final Comparator<? super N> comparator) {
        this.comparator = comparator;
        final TreeSet<N> filteredSet = new TreeSet<>(comparator);
        filteredSet.addAll(collection);
        elements = List.copyOf(filteredSet);
    }


    @Override
    public Iterator<N> iterator() {
        return elements.iterator();
    }

    @Override
    public int size() {
        return elements.size();
    }


    @Override
    public Comparator<? super N> comparator() {
        return comparator;
    }

    @Override
    public SortedSet<N> subSet(final N fromElement, final N toElement) {
        if (comparator.compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException(String.format(
                    "It is impossible to get values from the segment [_%s_ ; _%s_], " +
                            "because the first element is after the second in the ArraySet.",
                    fromElement.toString(), toElement.toString()
            ));
        }


        if (isEmpty()) {
            return new ArraySet<>(Collections.emptyList());
        }

        return sortedSubSet(fromElement, toElement, false);
    }

    @Override
    public SortedSet<N> headSet(final N toElement) {
        if (isEmpty()) return new info.kgeorgiy.ja.milenin.arrayset.ArraySet<>(comparator);

        return sortedSubSet(first(), toElement, false);
    }

    @Override
    public SortedSet<N> tailSet(final N fromElement) {
        if (isEmpty()) return new info.kgeorgiy.ja.milenin.arrayset.ArraySet<>(comparator);

        return sortedSubSet(fromElement, last(), true);
    }

    private SortedSet<N> sortedSubSet(final N fromElementObject, final N toElementObject, final boolean isTailSet) {
        final int fromElementIndex = find(fromElementObject);
        int toElementIndex = find(toElementObject);

        if (isTailSet) {
            toElementIndex++;
        }
        // :NOTE: Долго
        return new ArraySet<>(elements.subList(fromElementIndex, toElementIndex), comparator);
    }

    private int find(final N toElementObject) {
        final int toElementIndex = Collections.binarySearch(elements, toElementObject, comparator);
        return toElementIndex < 0 ? -toElementIndex - 1 : toElementIndex;
    }

    @Override
    public N first() {
        if (isEmpty()) throw new NoSuchElementException();

        else return elements.get(0);
    }

    @Override
    public N last() {
        if (isEmpty()) throw new NoSuchElementException();

        return elements.get(size() - 1);
    }


    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(final Object element) {
        return Collections.binarySearch(elements, (N) element, comparator) >= 0;
    }

}
