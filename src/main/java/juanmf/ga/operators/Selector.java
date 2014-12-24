package juanmf.ga.operators;

import juanmf.ga.structure.Individual;
import juanmf.ga.fitness.FitnessMeter;
import juanmf.ga.structure.Population;
import java.util.List;

/**
 * Selector implementors must preserve the individuals that should be recombined 
 * in the attempt to generate a better population.
 * 
 * If elitist selection is to be performed, consider the {@see Individual.setElite()}
 * 
 * @param <I> A SubType of Individual.
 * @param <A> A Subtype of AptitudeMeter.
 * @param <C> A representation of an average Aptitude with a natural Order.
 * @param <V> A representation of an Aptitude Value.
 *
 * @author juan.fernandez
 */
public interface Selector <I extends Individual, A extends FitnessMeter<I, V, C>, 
        C extends Comparable<? super C>, V extends Comparable<? super V>> {
    
    List<I> makeSelection(Population<I, A, C, V> population);
}
