package io.github.zekerzhayard.cme_soundmanager;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.BiMap;

public class ConcurrentBiHashMap<K, V> implements BiMap<K, V> {
    public static <K, V> Map<K, V> create() {
        return new ConcurrentBiHashMap<>();
    }

    private Map<K, V> map = new ConcurrentHashMap<>();

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return this.map.get(key);
    }

    @Override
    public V put(K key, V value) {
        return this.map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return this.map.remove(key);
    }

    @Override
    public V forcePut(K key, V value) {
        return this.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        this.map.putAll(map);
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Set<K> keySet() {
        return this.map.keySet();
    }

    @Override
    public Set<V> values() {
        return new ForwardingCollectionSet<>(this.map.values());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }

    @Override
    public BiMap<V, K> inverse() {
        return new Inverse<>(this);
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.map.equals(obj);
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

    private static class Inverse<V, K> implements BiMap<V, K> {
        private BiMap<K, V> map;

        public Inverse(BiMap<K, V> map) {
            this.map = map;
        }

        @Override
        public int size() {
            return this.map.size();
        }

        @Override
        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return this.map.containsValue(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return this.map.containsKey(value);
        }

        @Override
        public K get(Object key) {
            for (Map.Entry<K, V> entry : this.map.entrySet()) {
                if (Objects.equals(entry.getValue(), key)) {
                    return entry.getKey();
                }
            }
            return null;
        }

        @Override
        public K put(V key, K value) {
            K key0 = this.get(key);
            this.map.put(value, key);
            return key0;
        }

        @Override
        public K remove(Object key) {
            for (Map.Entry<K, V> entry : this.map.entrySet()) {
                if (Objects.equals(entry.getValue(), key)) {
                    this.map.remove(entry.getKey());
                    return entry.getKey();
                }
            }
            return null;
        }

        @Override
        public K forcePut(V key, K value) {
            return this.put(key, value);
        }

        @Override
        public void putAll(Map<? extends V, ? extends K> map) {
            for (Map.Entry<? extends V, ? extends K> entry : map.entrySet()) {
                this.map.put(entry.getValue(), entry.getKey());
            }
        }

        @Override
        public void clear() {
            this.map.clear();
        }

        @Override
        public Set<V> keySet() {
            return new ForwardingCollectionSet<>(this.map.values());
        }

        @Override
        public Set<K> values() {
            return this.map.keySet();
        }

        @Override
        public Set<Entry<V, K>> entrySet() {
            return new ForwardingMapEntrySet<>(this.map.entrySet());
        }

        @Override
        public BiMap<K, V> inverse() {
            return this.map;
        }

        @Override
        public int hashCode() {
            return this.map.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return this.map.equals(obj);
        }

        @Override
        public String toString() {
            return this.map.toString();
        }
    }

    private static class ForwardingCollectionSet<E> implements Set<E> {
        private Collection<E> collection;

        public ForwardingCollectionSet(Collection<E> collection) {
            this.collection = collection;
        }

        @Override
        public int size() {
            return this.collection.size();
        }

        @Override
        public boolean isEmpty() {
            return this.collection.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return this.collection.contains(o);
        }

        @Override
        public Iterator<E> iterator() {
            return this.collection.iterator();
        }

        @Override
        public Object[] toArray() {
            return this.collection.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return this.collection.toArray(a);
        }

        @Override
        public boolean add(E e) {
            return this.collection.add(e);
        }

        @Override
        public boolean remove(Object o) {
            return this.collection.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return this.collection.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            return this.collection.addAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return this.collection.retainAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return this.collection.removeAll(c);
        }

        @Override
        public void clear() {
            this.collection.clear();
        }

        @Override
        public int hashCode() {
            return this.collection.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return this.collection.equals(obj);
        }

        @Override
        public String toString() {
            return this.collection.toString();
        }
    }

    private static class ForwardingMapEntrySet<V, K> implements Set<Map.Entry<V, K>> {
        private Set<Map.Entry<K, V>> set;

        public ForwardingMapEntrySet(Set<Map.Entry<K, V>> set) {
            this.set = set;
        }

        @Override
        public int size() {
            return this.set.size();
        }

        @Override
        public boolean isEmpty() {
            return this.set.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> oEntry = (Map.Entry<?, ?>) o;
                for (Map.Entry<K, V> entry : this.set) {
                    if (Objects.equals(entry.getKey(), oEntry.getValue()) && Objects.equals(entry.getValue(), oEntry.getKey())) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public Iterator<Map.Entry<V, K>> iterator() {
            return new ForwardingMapEntryIterator<>(this.set, this.set.iterator());
        }

        @Override
        public Object[] toArray() {
            Set<Map.Entry<V, K>> set = ConcurrentHashMap.newKeySet();
            for (Map.Entry<K, V> entry : this.set) {
                set.add(new AbstractMap.SimpleEntry<>(entry.getValue(), entry.getKey()));
            }
            return set.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            Set<Map.Entry<V, K>> set = ConcurrentHashMap.newKeySet();
            for (Map.Entry<K, V> entry : this.set) {
                set.add(new AbstractMap.SimpleEntry<>(entry.getValue(), entry.getKey()));
            }
            return set.toArray(a);
        }

        @Override
        public boolean add(Entry<V, K> vkEntry) {
            return this.set.add(new AbstractMap.SimpleEntry<>(vkEntry.getValue(), vkEntry.getKey()));
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> oEntry = (Map.Entry<?, ?>) o;
                for (Map.Entry<K, V> entry : this.set) {
                    if (Objects.equals(entry.getKey(), oEntry.getValue()) && Objects.equals(entry.getValue(), oEntry.getKey())) {
                        return this.set.remove(entry);
                    }
                }
            }
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            for (Map.Entry<K, V> entry : this.set) {
                for (Object o : c) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    Map.Entry<?, ?> oEntry = (Map.Entry<?, ?>) o;
                    if (!Objects.equals(entry.getKey(), oEntry.getValue()) || !Objects.equals(entry.getValue(), oEntry.getKey())) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends Map.Entry<V, K>> c) {
            boolean added = false;
            for (Map.Entry<V, K> entry : c) {
                added = this.set.add(new AbstractMap.SimpleEntry<>(entry.getValue(), entry.getKey())) || added;
            }
            return added;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            boolean modified = false;
            for (Map.Entry<K, V> entry : this.set) {
                boolean retained = false;
                for (Object o : c) {
                    if (o instanceof Map.Entry) {
                        Map.Entry<?, ?> oEntry = (Map.Entry<?, ?>) o;
                        if (Objects.equals(entry.getKey(), oEntry.getValue()) && Objects.equals(entry.getValue(), oEntry.getKey())) {
                            retained = true;
                        }
                    }
                }
                if (!retained) {
                    modified = this.set.remove(entry) || modified;
                }
            }
            return modified;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            boolean removed = false;
            for (Map.Entry<K, V> entry : this.set) {
                for (Object o : c) {
                    if (o instanceof Map.Entry) {
                        Map.Entry<?, ?> oEntry = (Map.Entry<?, ?>) o;
                        if (Objects.equals(entry.getKey(), oEntry.getValue()) && Objects.equals(entry.getValue(), oEntry.getKey())) {
                            removed = this.set.remove(entry) || removed;
                        }
                    }
                }
            }
            return removed;
        }

        @Override
        public void clear() {
            this.set.clear();
        }

        @Override
        public int hashCode() {
            return this.set.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return this.set.equals(obj);
        }

        @Override
        public String toString() {
            return this.set.toString();
        }
    }

    private static class ForwardingMapEntryIterator<V, K> implements Iterator<Map.Entry<V, K>> {
        private Set<Map.Entry<K, V>> set;
        private Iterator<Map.Entry<K, V>> iterator;

        public ForwardingMapEntryIterator(Set<Map.Entry<K, V>> set, Iterator<Map.Entry<K, V>> iterator) {
            this.set = set;
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public Map.Entry<V, K> next() {
            return new ForwardingMapEntry<>(this.set, this.iterator.next());
        }

        @Override
        public void remove() {
            this.iterator.remove();
        }
    }

    private static class ForwardingMapEntry<V, K> implements Map.Entry<V, K> {
        private Set<Map.Entry<K, V>> set;
        private Map.Entry<K, V> entry;

        public ForwardingMapEntry(Set<Map.Entry<K, V>> set, Map.Entry<K, V> entry) {
            this.set = set;
            this.entry = entry;
        }

        @Override
        public V getKey() {
            return this.entry.getValue();
        }

        @Override
        public K getValue() {
            return this.entry.getKey();
        }

        @Override
        public K setValue(K value) {
            this.set.remove(this.entry);
            this.set.add(new AbstractMap.SimpleEntry<>(value, this.entry.getValue()));
            return this.entry.getKey();
        }
    }
}
