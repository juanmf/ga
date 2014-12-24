package juanmf.ga.operators;

import juanmf.ga.structure.IndividualFactory;
import juanmf.ga.structure.Individual;
import juanmf.ga.fitness.FitnessMeter;
import juanmf.ga.structure.Gen;
import juanmf.ga.structure.Population;

/**
 * Generates successive generations of individuals asking AptitudeMeter for the
 * ending conditions, using Genetic operators to navigate the solution field.
 * 
 * @param <I> A SubType of Individual.
 * @param <A> A Subtype of AptitudeMeter.
 * @param <C> A representation of an average Aptitude with a natural Order.
 * @param <V> A representation of an Aptitude Value.
 * @param <G> A SubType of Gen.
 *
 * @author juan.fernandez
 */
public interface Evolution <I extends Individual, A extends FitnessMeter<I, V, C>, 
        C extends Comparable<? super C>, V extends Comparable<? super V>,
        G extends Gen> {
    
    /**
     * Uses Individual Factory to generate a random population.
     * 
     * The population number might be stored as a constant in the implementation.
     * 
     * @param factory
     * @return The random population.
     */
    Population<I, A, C, V> generatePopulation(IndividualFactory<I, G> factory);
    
    /**
     * Generates successive generations of individuals asking AptitudeMeter for the
     * ending conditions, using Genetic operators to navigate the solution field.
     * 
     * @param population The initial Population.
     * 
     * @return The last population, after AptitudeMeter dictates it's enough
     */
    Population<I, A, C, V> evolve(Population<I, A, C, V> population);
}
