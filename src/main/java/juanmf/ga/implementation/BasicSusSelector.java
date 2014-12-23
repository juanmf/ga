/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package juanmf.ga.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import juanmf.ga.fitness.AptitudeMeter;
import juanmf.ga.structure.Individual;
import juanmf.ga.structure.Population;
import juanmf.ga.operators.Selector;

/**
 * Implements the "Stochastic universal sampling" Selection method.
 * 
 * @param <I> A SubType of Individual.
 * @param <A> A Subtype of AptitudeMeter.
 * @param <C> A representation of an average Aptitude with a natural Order.
 * @param <V> A representation of an Aptitude Value.
 *
 * @author juan.fernandez
 */
public class BasicSusSelector <I extends Individual, A extends AptitudeMeter<I, V, C>, 
        C extends Comparable<? super C>, V extends Comparable<? super V>> 
        implements Selector<I, A, C, V> {

    private final AptitudeMeter<I, V, C> aptitudeMeter;
    private static final float ELITE_PROPORTION = 0.05f;

    public BasicSusSelector(AptitudeMeter<I, V, C> aptitudeMeter) {
        this.aptitudeMeter = aptitudeMeter;
    }
    
    @Override
    public List<I> makeSelection(Population<I, A, C, V> population) {
        List<I> selection = new ArrayList<>();
        NavigableMap<V, I> susRoullete = makeSusRoullete(population);
        Iterator<Map.Entry<V, I>> susIterator = susRoullete.entrySet().iterator();
        V offspringAptDelta = (V) markElite(population);
        V deltaAcum = offspringAptDelta;
        while (susIterator.hasNext()) {
            Map.Entry<V, I> susEntry = susIterator.next();
            if (0 <= susEntry.getKey().compareTo(deltaAcum)) {
                deltaAcum = aptitudeMeter.getSum(deltaAcum, offspringAptDelta);
                selection.add(susEntry.getValue());
            }
        }
        System.out.println("Selection individuals: " + selection.size());
        return selection;
    }
    
    /**
     * Sorts the population & Marks the ELITE_PROPORTION Individuals as Elite.
     * 
     * Note that the population remains sorted.
     * 
     * @param population
     * @return 
     */
    private V markElite(Population<I, A, C, V> population) {
        List<I> individuals = new ArrayList();
        individuals.addAll(population);
        Collections.sort(individuals);
        Collections.reverse(individuals);
        int elite = (int) (individuals.size() * ELITE_PROPORTION);
        Iterator<I> eliteIt = individuals.iterator();
        for (int i = 0; i < elite; i++) {
            eliteIt.next().setElite(true);
        }
        return (V) eliteIt.next().getAptitude();
    }
    
    private NavigableMap<V, I> makeSusRoullete(Population<I, A, C, V> population) {
        V sum = aptitudeMeter.getZeroAptitude();
        NavigableMap<V, I> susRoullete = new TreeMap<>();
        for (I i : population) {
            sum = aptitudeMeter.getSum(sum, (V) i.getAptitude());
            susRoullete.put(sum, i);
        }
        return susRoullete;
    }
}
