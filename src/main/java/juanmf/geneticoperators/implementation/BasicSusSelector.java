/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package juanmf.geneticoperators.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import juanmf.geneticoperators.AptitudeMeter;
import juanmf.geneticoperators.Individual;
import juanmf.geneticoperators.Population;
import juanmf.geneticoperators.Selector;

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

    public BasicSusSelector(int offsprings, AptitudeMeter<I, V, C> aptitudeMeter) {
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
     * 
     * @param population
     * @return 
     */
    private V markElite(Population<I, A, C, V> population) {
        Collections.sort(population);
        Collections.reverse(population);
        int elite = (int) (population.size() * ELITE_PROPORTION);
        Iterator<I> eliteIt = population.iterator();
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
