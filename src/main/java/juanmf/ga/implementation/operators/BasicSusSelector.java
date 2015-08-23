package juanmf.ga.implementation.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import juanmf.ga.fitness.FitnessMeter;
import juanmf.ga.structure.Individual;
import juanmf.ga.structure.Population;
import juanmf.ga.operators.Selector;
import org.apache.commons.collections.set.ListOrderedSet;

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
public class BasicSusSelector <I extends Individual, A extends FitnessMeter<I, V, C>, 
        C extends Comparable<? super C>, V extends Comparable<? super V>> 
        implements Selector<I, A, C, V> {

    private final FitnessMeter<I, V, C> aptitudeMeter;
    private static final float ELITE_PROPORTION = 0.005f;

    public BasicSusSelector(FitnessMeter<I, V, C> aptitudeMeter) {
        this.aptitudeMeter = aptitudeMeter;
    }
    
    @Override
    public List<I> makeSelection(Population<I, A, C, V> population) {
        List<I> selection = new ArrayList<>();
        Map<V, I> susRoullete = makeSusRoullete(population);
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
        Collections.sort(population);
        Collections.reverse(population);
        Set<I> uniq = ListOrderedSet.decorate(population);
        int elite = (int) (population.size() * ELITE_PROPORTION);
        Iterator<I> eliteIt = uniq.iterator();
        for (int i = 0; i < elite; i++) {
            eliteIt.next().setElite(true);
        }
        return (V) eliteIt.next().getAptitude();
    }
    
    /**
     * Arranges the population in a map, which keys are the accumulated sum of 
     * all already allocated individual's fitness value.
     * 
     * @param population The current population to select individuals among.
     * 
     * @return a Map implementation that must keep input order of its keys, containing
     * the population indexed by the fitness accumulated sum.
     */
    private Map<V, I> makeSusRoullete(Population<I, A, C, V> population) {
        V sum = aptitudeMeter.getZeroFitness();
        NavigableMap<V, I> susRoullete = new TreeMap<>();
        for (I i : population) {
            sum = aptitudeMeter.getSum(sum, (V) i.getAptitude());
            susRoullete.put(sum, i);
        }
        return susRoullete;
    }
}
