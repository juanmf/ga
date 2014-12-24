package juanmf.ga.structure;

import juanmf.ga.fitness.FitnessMeter;
import java.util.List;

/**
 * This Factory should create suitable populations. It will be used to create a 
 * new population for each generation of individuals, after selection and crossover.
 * 
 * @param <I> A SubType of Individual.
 * @param <A> A Subtype of AptitudeMeter.
 * @param <C> A representation of an average Aptitude with a natural Order.
 * @param <V> A representation of an Aptitude value.
 *
 * @author juan.fernandez
 */
public interface PopulationFactory <I extends Individual, A extends FitnessMeter<I, V, C>, 
        C extends Comparable<? super C>, V extends Comparable<? super V>> {
    /**
     * Creates an empty population.
     * 
     * @return An empty population
     */ 
    Population<I, A, C, V> createEmptyPopulation();
    
    /**
     * Creates a population based on the given Individuals.
     * 
     * @param aptitudMeter The aptitudeMeter capable of evaluating these individuals.
     * @param individuals  The individuals that will populate the population.
     * 
     * @return a new Population instance.
     */
    Population<I, A, C, V> createPopulation(A aptitudMeter, List<I> individuals);
}
