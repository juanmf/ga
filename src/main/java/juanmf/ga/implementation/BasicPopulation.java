package juanmf.ga.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import juanmf.ga.fitness.FitnessMeter;
import juanmf.ga.structure.Individual;
import juanmf.ga.structure.Population;

/**
 * @param <I> A SubType of Individual.
 * @param <A> A Subtype of AptitudeMeter.
 * @param <C> A representation of an average Aptitude with a natural Order.
 * @param <V> A representation of an Aptitude Value.
 *
 * @author juan.fernandez
 */
public class BasicPopulation <I extends Individual, A extends FitnessMeter<I, V, C>, 
        C extends Comparable<? super C>, V extends Comparable<? super V>> 
        implements Population<I, A, C, V> {

    private final List<I> individuals;
    private final A aptitudMeter;
    private I bestIndividual;
    
    /**
     * TODO: re-calculate on Collection change or on demand.
     * TODO: Lacks update on removal, not used yet, might REMOVE.
     */
    private C averageAptitude;

    public static <I extends Individual, A extends FitnessMeter<I, V, C>, 
            C extends Comparable<? super C>, V extends Comparable<? super V>> 
            Population<I, A, C, V> createPopulation(A aptitudMeter) {
        return new BasicPopulation(aptitudMeter);
    }

    public static <I extends Individual, A extends FitnessMeter<I, V, C>, 
            C extends Comparable<? super C>, V extends Comparable<? super V>> 
            Population<I, A, C, V> createPopulation(A aptitudMeter, List<I> individuals) {
        return new BasicPopulation(aptitudMeter, individuals);
    }
            
    private BasicPopulation(A aptitudMeter) {
        this(aptitudMeter, new ArrayList<>());
    }

    private BasicPopulation(A aptitudMeter, List<I> individuals) {
        this.individuals = individuals;
        this.aptitudMeter = aptitudMeter;
        averageAptitude = individuals.isEmpty()
                ? aptitudMeter.getInitialAverage() 
                : aptitudMeter.getAverageFitness(this);
    }
    
    @Override
    public C getAverageAptitude() {
        return averageAptitude;
    }

    @Override
    public A getAptitudeMeter() {
        return aptitudMeter;
    }
    
    @Override
    public I getBest() {
        if (null == bestIndividual) {
            bestIndividual = getBestOfList(individuals);
        }
        return bestIndividual;
    }
    
    private I getBestOfList(Collection<? extends I> individuals) {
        I best = (I) ((Collection<I>) individuals).stream()
            .max(Comparator.comparing(i -> i.getAptitude())).orElse(null);
        return best;
    }
            
    private void updateBestIndividualOnAdd(I i) {
        averageAptitude = aptitudMeter.updateAverageOnAdd(i, size(), averageAptitude);
        if (null != bestIndividual && 0 < i.compareTo(bestIndividual)) {
            bestIndividual = i;
        }
    }
    
    private void updateBestIndividualOnRemove(I i) {
        averageAptitude = aptitudMeter.updateAverageOnRemove(i, size(), averageAptitude);
        if (i == bestIndividual) {
            bestIndividual = null;
        }
        
    }
    
    private void updateBestIndividualOnRemove(Collection<? extends I> c) {
        averageAptitude = aptitudMeter.getAverageFitness(individuals);
        if (c.contains(bestIndividual)) {
            bestIndividual = null;
        }
    }
    
    private void updateBestIndividualOnRetain(Collection<? extends I> c) {
        averageAptitude = aptitudMeter.getAverageFitness(individuals);
        if (! c.contains(bestIndividual)) {
            bestIndividual = null;
        }
    }

    private void updateBestIndividualOnAdd(Collection<? extends I> c) {
        averageAptitude = aptitudMeter.getAverageFitness(individuals);
        if (null != bestIndividual) {
            I bestOfC = getBestOfList(c);
            if (0 < bestOfC.compareTo(bestIndividual)) {
                bestIndividual = bestOfC;
            }
        }
    }
    
    @Override
    public int size() {
        return individuals.size();
    }

    @Override
    public boolean isEmpty() {
        return individuals.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return individuals.contains(o);
    }

    @Override
    public Iterator<I> iterator() {
        return individuals.iterator();
    }

    @Override
    public Object[] toArray() {
        return individuals.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return individuals.toArray(a);
    }

    @Override
    public boolean add(I e) {
        averageAptitude = aptitudMeter.updateAverageOnAdd(e, size(), averageAptitude);
        updateBestIndividualOnAdd(e);
        return individuals.add(e);
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Individual)) {
            throw new ClassCastException("Population can only remove Individuals");
        }
        updateBestIndividualOnRemove((I) o);
        return individuals.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return individuals.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends I> c) {
        boolean result = individuals.addAll(c);
        if (result) {
            updateBestIndividualOnAdd(c);
        }
        return individuals.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends I> c) {
        boolean result = individuals.addAll(index, c);
        if (result) {
            updateBestIndividualOnAdd(c);
        }
        return individuals.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = individuals.removeAll(c);
        if (result) {
            updateBestIndividualOnRemove((Collection<I>) c);
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = individuals.retainAll(c);
        if (result) {
            updateBestIndividualOnRetain((Collection<I>) c);
        }
        return result;
    }

    @Override
    public void clear() {
        individuals.clear();
        bestIndividual = null;
        averageAptitude = null;
    }

    @Override
    public I get(int index) {
        return individuals.get(index);
    }

    @Override
    public I set(int index, I element) {
        I i = individuals.set(index, element);
        updateBestIndividualOnRemove(i);
        updateBestIndividualOnAdd(element);
        return i;
    }

    @Override
    public void add(int index, I element) {
        updateBestIndividualOnAdd(element);
        individuals.add(index, element);
    }

    @Override
    public I remove(int index) {
        I i = individuals.remove(index);
        updateBestIndividualOnRemove(i);
        return i;
    }

    @Override
    public int indexOf(Object o) {
        return individuals.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return individuals.lastIndexOf(o);
    }

    @Override
    public ListIterator<I> listIterator() {
        return individuals.listIterator();
    }

    @Override
    public ListIterator<I> listIterator(int index) {
        return individuals.listIterator(index);
    }

    @Override
    public List<I> subList(int fromIndex, int toIndex) {
        return individuals.subList(fromIndex, toIndex);
    }
}
