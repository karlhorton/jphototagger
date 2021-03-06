package org.jphototagger.program.module.thumbnails.cache;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Map containing SoftReferences for dropping mapped-to stuff in case of
 * memory shortage.
 *
 * @param <C>
 * @author Martin Pohlack
 */
public class SoftCacheMap<C extends CacheIndirection> {

    private final HashMap<File, SoftReference<C>> _map = new HashMap<>();
    private final int MAX_ENTRIES;
    final WorkQueue<C> w;

    public SoftCacheMap(int maxEntries, WorkQueue<C> _w) {
        if (_w == null) {
            throw new NullPointerException("_w == null");
        }

        MAX_ENTRIES = maxEntries;
        w = _w;
    }

    public C get(File k) {
        if (k == null) {
            throw new NullPointerException("k == null");
        }

        SoftReference<C> sr = _map.get(k);

        if (sr == null) {
            return null;
        }

        return sr.get();
    }

    public C put(File k, C v) {
        if (k == null) {
            throw new NullPointerException("k == null");
        }

        if (v == null) {
            throw new NullPointerException("v == null");
        }

        SoftReference<C> sr = _map.put(k, new SoftReference<>(v));

        if (sr == null) {
            return null;
        }

        return sr.get();
    }

    public C remove(File k) {
        if (k == null) {
            throw new NullPointerException("k == null");
        }

        SoftReference<C> sr = _map.remove(k);

        if (sr == null) {
            return null;
        }

        return sr.get();
    }

    public void clear() {
        _map.clear();
    }

    public int size() {
        return _map.size();
    }

    public boolean containsKey(File k) {
        if (k == null) {
            throw new NullPointerException("k == null");
        }

        if (!_map.containsKey(k)) {
            return false;
        }

        return _map.get(k).get() != null;
    }

    public Set<File> keySet() {
        return _map.keySet();
    }

    public void maybeCleanupCache() {

        /*
         *  1. get EntrySet
         * 2. sort entries according to age in softref, empty softrefs
         *    first, using a sorted set
         * 3. iterate over first n elements and remove them from original
         *    cache
         */
        if (size() <= MAX_ENTRIES) {
            return;
        }

        NavigableSet<Entry<File, SoftReference<C>>> removes =
                new TreeSet<>(new CacheIndirectionAgeComparator<C>());

        removes.addAll(_map.entrySet());

        Iterator<Entry<File, SoftReference<C>>> it = removes.iterator();
        Entry<File, SoftReference<C>> e;
        C ci;

        for (int index = 0; (index < MAX_ENTRIES / 10) && it.hasNext(); index++) {
            e = it.next();

            if (e.getValue() == null) {
                _map.remove(e.getKey());

                continue;
            }

            ci = e.getValue().get();

            if (ci == null) {
                _map.remove(e.getKey());

                continue;
            }

            synchronized (ci) {

                // check if this image is probably in a prefetch queue and remove it
                if (ci.isEmpty() && (w != null)) {
                    w.remove(ci);
                }

                _map.remove(ci.file);
            }
        }
    }
}
