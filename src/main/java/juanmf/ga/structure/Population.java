package juanmf.ga.structure;

import juanmf.ga.fitness.FitnessMeter;
import java.util.List;

/**
 * A population is a list of Individuals, that should keep traking of the best one,
 * and the average aptitude, that might be needed by operators and profilers. 
 * 
 * @param <I> A SubType of Individual.
 * @param <A> A Subtype of AptitudeMeter.
 * @param <C> A representation of an average Aptitude with a natural Order.
 * @param <V> A representation of an Aptitude Value.
 *
 * @author juan.fernandez
 */
public interface Population <I extends Individual, A extends FitnessMeter<I, V, C>, 
        C extends Comparable<? super C>, V extends Comparable<? super V>> extends List<I> {
    /**
     * Returns the population's average aptitude.
     * 
     * @return the population's average aptitude.
     */
    C getAverageAptitude();
    
    /**
     * Returns the aptitudeMeter.
     * 
     * Population needs a reference to AptitudeMeter in order to calculate Average 
     * aptitudes.
     * 
     * @return 
     */
    A getAptitudeMeter();
    
    /**
     * Returns the best valued Individual for this population.
     * 
     * @return 
     */
    I getBest();
}
