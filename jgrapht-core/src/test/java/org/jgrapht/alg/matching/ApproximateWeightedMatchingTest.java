/*
 * (C) Copyright 2016-2017, by Dimitrios Michail and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */
package org.jgrapht.alg.matching;

import java.util.*;
import java.util.stream.*;

import org.jgrapht.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.interfaces.MatchingAlgorithm.*;
import org.jgrapht.graph.*;

import junit.framework.*;

/**
 * Unit tests for the approximate weighted matching algorithms.
 * 
 * @author Dimitrios Michail
 */
public abstract class ApproximateWeightedMatchingTest
    extends TestCase
{

    public abstract MatchingAlgorithm<Integer, DefaultWeightedEdge> getApproximationAlgorithm(
        Graph<Integer, DefaultWeightedEdge> graph);

    public void testPath1()
    {
        WeightedPseudograph<Integer, DefaultWeightedEdge> g =
            new WeightedPseudograph<>(DefaultWeightedEdge.class);

        Graphs.addAllVertices(g, Arrays.asList(0, 1, 2, 3, 4, 5));
        Graphs.addEdge(g, 0, 1, 1.0);
        Graphs.addEdge(g, 1, 2, 1.0);
        Graphs.addEdge(g, 2, 3, 1.0);
        Graphs.addEdge(g, 3, 4, 1.0);
        Graphs.addEdge(g, 4, 5, 1.0);

        MatchingAlgorithm<Integer, DefaultWeightedEdge> mm = getApproximationAlgorithm(g);

        Matching<DefaultWeightedEdge> m = mm.getMatching();
        assertEquals(3, m.getEdges().size());
        assertEquals(3.0, m.getWeight(), MatchingAlgorithm.DEFAULT_EPSILON);
        assertTrue(m.getEdges().contains(g.getEdge(0, 1)));
        assertTrue(m.getEdges().contains(g.getEdge(2, 3)));
        assertTrue(m.getEdges().contains(g.getEdge(4, 5)));
        assertTrue(isMatching(g, m));
    }

    public void testPath2()
    {
        WeightedPseudograph<Integer, DefaultWeightedEdge> g =
            new WeightedPseudograph<>(DefaultWeightedEdge.class);

        Graphs.addAllVertices(g, Arrays.asList(0, 1, 2, 3, 4, 5));
        Graphs.addEdge(g, 0, 1, 1.0);
        Graphs.addEdge(g, 1, 2, 5.0);
        Graphs.addEdge(g, 2, 3, 1.0);
        Graphs.addEdge(g, 3, 4, 5.0);
        Graphs.addEdge(g, 4, 5, 1.0);

        MatchingAlgorithm<Integer, DefaultWeightedEdge> mm = getApproximationAlgorithm(g);
        Matching<DefaultWeightedEdge> m = mm.getMatching();

        assertEquals(2, m.getEdges().size());
        assertEquals(10.0, m.getWeight(), MatchingAlgorithm.DEFAULT_EPSILON);
        assertTrue(m.getEdges().contains(g.getEdge(1, 2)));
        assertTrue(m.getEdges().contains(g.getEdge(3, 4)));
        assertTrue(isMatching(g, m));
    }

    public void testNegativeAndZeroEdges()
    {
        WeightedPseudograph<Integer, DefaultWeightedEdge> g =
            new WeightedPseudograph<>(DefaultWeightedEdge.class);

        Graphs.addAllVertices(g, Arrays.asList(0, 1, 2, 3));
        Graphs.addEdge(g, 0, 1, -1.0);
        Graphs.addEdge(g, 1, 2, -5.0);
        Graphs.addEdge(g, 2, 3, -1.0);
        Graphs.addEdge(g, 3, 0, -1.0);
        Graphs.addEdge(g, 3, 1, 0d);
        Graphs.addEdge(g, 0, 2, 0d);

        MatchingAlgorithm<Integer, DefaultWeightedEdge> mm = getApproximationAlgorithm(g);
        Matching<DefaultWeightedEdge> m = mm.getMatching();

        assertEquals(0, m.getEdges().size());
        assertEquals(0d, m.getWeight(), MatchingAlgorithm.DEFAULT_EPSILON);
        assertTrue(isMatching(g, m));
    }

    public void testNegativeAndZeroEdges1()
    {
        WeightedPseudograph<Integer, DefaultWeightedEdge> g =
            new WeightedPseudograph<>(DefaultWeightedEdge.class);

        Graphs.addAllVertices(g, Arrays.asList(0, 1, 2, 3));
        Graphs.addEdge(g, 0, 1, -1.0);
        Graphs.addEdge(g, 1, 2, -5.0);
        Graphs.addEdge(g, 2, 3, -1.0);
        Graphs.addEdge(g, 3, 0, -1.0);
        Graphs.addEdge(g, 3, 1, -1.0d);
        Graphs.addEdge(g, 0, 2, -1.0d);

        MatchingAlgorithm<Integer, DefaultWeightedEdge> mm = getApproximationAlgorithm(g);
        Matching<DefaultWeightedEdge> m = mm.getMatching();

        assertEquals(0, m.getEdges().size());
        assertEquals(0d, m.getWeight(), MatchingAlgorithm.DEFAULT_EPSILON);
        assertTrue(isMatching(g, m));
    }

    public void testNegativeAndZeroEdges2()
    {
        WeightedPseudograph<Integer, DefaultWeightedEdge> g =
            new WeightedPseudograph<>(DefaultWeightedEdge.class);

        Graphs.addAllVertices(g, Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        Graphs.addEdge(g, 0, 1, 1.0);
        Graphs.addEdge(g, 1, 3, 1.0);
        Graphs.addEdge(g, 2, 4, -1.0);
        Graphs.addEdge(g, 3, 5, -1.0);
        Graphs.addEdge(g, 4, 6, -1.0);
        Graphs.addEdge(g, 5, 6, -1.0);
        Graphs.addEdge(g, 6, 7, -1.0);
        Graphs.addEdge(g, 6, 8, -1.0);
        Graphs.addEdge(g, 7, 9, -1.0);
        Graphs.addEdge(g, 8, 10, -1.0);
        Graphs.addEdge(g, 9, 11, 1.0);
        Graphs.addEdge(g, 10, 12, 1.0);

        MatchingAlgorithm<Integer, DefaultWeightedEdge> mm = getApproximationAlgorithm(g);
        Matching<DefaultWeightedEdge> m = mm.getMatching();

        assertTrue(isMatching(g, m));
        assertTrue(m.getWeight() >= 2d);
        assertTrue(m.getEdges().size() >= 2);
    }

    public void testGraph1()
    {
        WeightedPseudograph<Integer, DefaultWeightedEdge> g =
            new WeightedPseudograph<>(DefaultWeightedEdge.class);

        Graphs.addAllVertices(g, IntStream.range(0, 15).boxed().collect(Collectors.toList()));
        Graphs.addEdge(g, 0, 1, 5.0);
        Graphs.addEdge(g, 1, 2, 2.5);
        Graphs.addEdge(g, 2, 3, 5.0);
        Graphs.addEdge(g, 3, 4, 2.5);
        Graphs.addEdge(g, 4, 0, 2.5);
        Graphs.addEdge(g, 0, 13, 2.5);
        Graphs.addEdge(g, 13, 14, 5.0);
        Graphs.addEdge(g, 1, 11, 2.5);
        Graphs.addEdge(g, 11, 12, 5.0);
        Graphs.addEdge(g, 2, 9, 2.5);
        Graphs.addEdge(g, 9, 10, 5.0);
        Graphs.addEdge(g, 3, 7, 2.5);
        Graphs.addEdge(g, 7, 8, 5.0);
        Graphs.addEdge(g, 4, 5, 2.5);
        Graphs.addEdge(g, 5, 6, 5.0);

        MatchingAlgorithm<Integer, DefaultWeightedEdge> mm = getApproximationAlgorithm(g);
        Matching<DefaultWeightedEdge> m = mm.getMatching();

        assertEquals(7, m.getEdges().size());
        assertEquals(35.0, m.getWeight(), MatchingAlgorithm.DEFAULT_EPSILON);
        assertTrue(isMatching(g, m));
    }

    public void test3over4Approximation()
    {
        WeightedPseudograph<Integer, DefaultWeightedEdge> g =
            new WeightedPseudograph<>(DefaultWeightedEdge.class);

        Graphs.addAllVertices(g, Arrays.asList(0, 1, 2, 3));
        Graphs.addEdge(g, 0, 1, 1.0);
        Graphs.addEdge(g, 1, 2, 1.0);
        Graphs.addEdge(g, 2, 3, 1.0);
        Graphs.addEdge(g, 3, 0, 1.0);
        Graphs.addAllVertices(g, Arrays.asList(4, 5, 6, 7));
        Graphs.addEdge(g, 4, 5, 1.0);
        Graphs.addEdge(g, 5, 6, 1.0);
        Graphs.addEdge(g, 6, 7, 1.0);
        Graphs.addEdge(g, 7, 4, 1.0);

        MatchingAlgorithm<Integer, DefaultWeightedEdge> mm = getApproximationAlgorithm(g);
        Matching<DefaultWeightedEdge> m = mm.getMatching();

        assertEquals(4, m.getEdges().size());
        assertEquals(4.0, m.getWeight(), MatchingAlgorithm.DEFAULT_EPSILON);
        assertTrue(isMatching(g, m));
    }

    public void testSelfLoops()
    {
        WeightedPseudograph<Integer, DefaultWeightedEdge> g =
            new WeightedPseudograph<>(DefaultWeightedEdge.class);

        Graphs.addAllVertices(g, Arrays.asList(0, 1, 2, 3));
        Graphs.addEdge(g, 0, 1, 1.0);
        Graphs.addEdge(g, 1, 2, 1.0);
        Graphs.addEdge(g, 2, 3, 1.0);
        Graphs.addEdge(g, 3, 0, 1.0);
        Graphs.addAllVertices(g, Arrays.asList(4, 5, 6, 7));
        Graphs.addEdge(g, 4, 5, 1.0);
        Graphs.addEdge(g, 5, 6, 1.0);
        Graphs.addEdge(g, 6, 7, 1.0);
        Graphs.addEdge(g, 7, 4, 1.0);

        // add self loops
        Graphs.addEdge(g, 0, 0, 100.0);
        Graphs.addEdge(g, 1, 1, 200.0);
        Graphs.addEdge(g, 2, 2, -200.0);
        Graphs.addEdge(g, 3, 3, -100.0);
        Graphs.addEdge(g, 4, 4, 0.0);

        MatchingAlgorithm<Integer, DefaultWeightedEdge> mm = getApproximationAlgorithm(g);
        Matching<DefaultWeightedEdge> m = mm.getMatching();

        assertEquals(4, m.getEdges().size());
        assertEquals(4.0, m.getWeight(), MatchingAlgorithm.DEFAULT_EPSILON);
        assertTrue(isMatching(g, m));
    }

    public void testMultiGraph()
    {
        WeightedPseudograph<Integer, DefaultWeightedEdge> g =
            new WeightedPseudograph<>(DefaultWeightedEdge.class);

        Graphs.addAllVertices(g, Arrays.asList(0, 1, 2, 3));
        Graphs.addEdge(g, 0, 1, 1.0);
        Graphs.addEdge(g, 1, 2, 1.0);
        Graphs.addEdge(g, 2, 3, 1.0);
        Graphs.addEdge(g, 3, 0, 1.0);
        Graphs.addAllVertices(g, Arrays.asList(4, 5, 6, 7));
        Graphs.addEdge(g, 4, 5, 1.0);
        Graphs.addEdge(g, 5, 6, 1.0);
        Graphs.addEdge(g, 6, 7, 1.0);
        Graphs.addEdge(g, 7, 4, 1.0);

        // add multiple edges
        Graphs.addEdge(g, 0, 1, 2.0);
        Graphs.addEdge(g, 1, 2, 2.0);
        Graphs.addEdge(g, 2, 3, 2.0);
        Graphs.addEdge(g, 3, 0, 2.0);
        Graphs.addEdge(g, 4, 5, 2.0);
        Graphs.addEdge(g, 5, 6, 2.0);
        Graphs.addEdge(g, 6, 7, 2.0);
        Graphs.addEdge(g, 7, 4, 2.0);

        MatchingAlgorithm<Integer, DefaultWeightedEdge> mm = getApproximationAlgorithm(g);
        Matching<DefaultWeightedEdge> m = mm.getMatching();

        // greedy finds maximum here 8.0
        assertEquals(4, m.getEdges().size());
        assertEquals(8.0, m.getWeight(), MatchingAlgorithm.DEFAULT_EPSILON);
        assertTrue(isMatching(g, m));
    }

    public void testDirected()
    {
        DirectedWeightedPseudograph<Integer, DefaultWeightedEdge> g =
            new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);

        Graphs.addAllVertices(g, Arrays.asList(0, 1, 2, 3));
        Graphs.addEdge(g, 0, 1, 1.0);
        Graphs.addEdge(g, 1, 2, 1.0);
        Graphs.addEdge(g, 2, 3, 1.0);
        Graphs.addEdge(g, 3, 0, 1.0);
        Graphs.addAllVertices(g, Arrays.asList(4, 5, 6, 7));
        Graphs.addEdge(g, 4, 5, 1.0);
        Graphs.addEdge(g, 5, 6, 1.0);
        Graphs.addEdge(g, 6, 7, 1.0);
        Graphs.addEdge(g, 7, 4, 1.0);

        // add multiple edges
        Graphs.addEdge(g, 0, 1, 2.0);
        Graphs.addEdge(g, 1, 2, 2.0);
        Graphs.addEdge(g, 2, 3, 2.0);
        Graphs.addEdge(g, 3, 0, 2.0);
        Graphs.addEdge(g, 4, 5, 2.0);
        Graphs.addEdge(g, 5, 6, 2.0);
        Graphs.addEdge(g, 6, 7, 2.0);
        Graphs.addEdge(g, 7, 4, 2.0);

        MatchingAlgorithm<Integer, DefaultWeightedEdge> mm = getApproximationAlgorithm(g);
        Matching<DefaultWeightedEdge> m = mm.getMatching();

        assertEquals(4, m.getEdges().size());
        assertEquals(8.0, m.getWeight(), MatchingAlgorithm.DEFAULT_EPSILON);
        assertTrue(isMatching(g, m));
    }

    public void testDisconnectedAndIsolatedVertices()
    {
        WeightedPseudograph<Integer, DefaultWeightedEdge> g =
            new WeightedPseudograph<>(DefaultWeightedEdge.class);

        Graphs.addAllVertices(g, Arrays.asList(0, 1, 2, 3));
        Graphs.addEdge(g, 0, 1, 1.0);
        Graphs.addEdge(g, 1, 2, 1.0);
        Graphs.addEdge(g, 2, 3, 1.0);
        Graphs.addEdge(g, 3, 0, 1.0);
        Graphs.addAllVertices(g, Arrays.asList(4, 5, 6, 7));
        Graphs.addEdge(g, 4, 5, 1.0);
        Graphs.addEdge(g, 5, 6, 1.0);
        Graphs.addEdge(g, 6, 7, 1.0);
        Graphs.addEdge(g, 7, 4, 1.0);
        Graphs.addAllVertices(g, Arrays.asList(8, 9, 10, 11));

        MatchingAlgorithm<Integer, DefaultWeightedEdge> mm = getApproximationAlgorithm(g);
        Matching<DefaultWeightedEdge> m = mm.getMatching();

        assertTrue(m.getWeight() >= 2.0);
        assertTrue(isMatching(g, m));
    }

    public void testBnGraph()
    {
        // create graphs which have a perfect matching
        for (int size = 1; size < 100; size++) {

            SimpleGraph<Integer, DefaultWeightedEdge> graph =
                new SimpleGraph<>(DefaultWeightedEdge.class);

            for (int i = 0; i < size; i++) {
                graph.addVertex(i);
            }

            for (int i = 0; i < size; i++) {
                graph.addVertex(i + size);
                graph.addEdge(i, i + size);
            }

            for (int i = 0; i < size; i++) {
                for (int j = i + 1; j < size; j++) {
                    graph.addEdge(i, j);
                }
            }

            MatchingAlgorithm<Integer, DefaultWeightedEdge> maxAlg =
                getApproximationAlgorithm(graph);
            Matching<DefaultWeightedEdge> matching = maxAlg.getMatching();
            double weight = matching.getWeight();

            assertTrue(isMatching(graph, matching));
            assertTrue(weight >= size / 2.0);
        }
    }

    protected <V, E> boolean isMatching(Graph<V, E> g, Matching<E> m)
    {
        Set<V> matched = new HashSet<>();
        for (E e : m.getEdges()) {
            V source = g.getEdgeSource(e);
            V target = g.getEdgeTarget(e);
            if (matched.contains(source)) {
                return false;
            }
            matched.add(source);
            if (matched.contains(target)) {
                return false;
            }
            matched.add(target);
        }
        return true;
    }

}

// End PathGrowingWeightedMatchingTest.java
