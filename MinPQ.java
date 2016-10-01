/*
 * Copyright (C) 2016 Michael <GrubenM@GMail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 *
 * @author Michael <GrubenM@GMail.com>
 * @param <Key>
 */
public class MinPQ<Key extends Comparable<Key>> {
    private Key[] pq;
    private int size;
    
    
    /**
     * Create an empty priority queue.
     * Passing capacity here is for convenience, but in practice pq should
     * almost certainly be a resizing array or a linked list.
     */
    public MinPQ(int capacity) {
        // Recall that, to ease indexing maths, we use a 1-indexed array.
        pq = (Key[]) new Comparable[capacity + 1];
    }
    
    // Create a priority queue with the given keys
    public MinPQ(Key[] k) {}
    
    
    // Swap item in array pq[] at index i with the one at index j
    private void exch(int i, int j) {
        Key tmp = pq[i];
        pq[i] = pq[j];
        pq[j] = tmp;
    }
    
    private boolean greater(int i, int j) {
        return pq[i].compareTo(pq[j]) > 0;
    }
    
    /**
     * Given a key at index k, swaps the key with its ancestors
     * until the heap order is restored.
     * @param k 
     */
    public void swim(int k) {
        /**
         * Because of cleverness with integer division, each key's parent
         * can be found be dividing the key's index by two.
         */
        while (k > 1 && greater(k / 2, k)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }
    
    /**
     * Insert a key into the priority queue.
     * Specifically, the key is inserted at the end of the priority queue,
     * which almost certainly violates the heap ordering.
     * Then, the key "swims" up to its proper location, through
     * successive swaps.
     */
    public void insert(Key k) {
        pq[++size] = k;
        swim(size);
    }
    
    /**
     * Given a key at index k, swaps the key with its smallest descendants
     * until the heap order is restored.
     * @param k 
     */
    private void sink(int k) {
        while (2 * k < size) {
            int j = 2 * k;
            if (j < size && greater(j, j + 1)) j++; // Choose smaller child
            if (!greater(k, j)) break; // Check if further sink needed
            exch(k, j); // swap this key with its smaller child
            k = j; // reorient to new location
        }
    }
    
    // Return and remove the largest key
    public Key delMin() {
        Key min = pq[1];
        exch(1, size--);
        sink(1);
        pq[size + 1] = null;
        return min;
    }
    
    // Is the priority queue empty?
    public boolean isEmpty() {
        return size == 0;
    }
    
    // Return the smallest key
    public Key min() {
        return pq[1];
    }
    
    // Number of entries in the priority queue
    public int size() {
        return size;
    }
}
