package juanmf.ga.events;

import juanmf.ga.fitness.FitnessMeter;
import juanmf.ga.structure.Individual;
import juanmf.ga.structure.Population;

/**
 *
 * @author juan.fernandez
 */
public class EvolutionFinishedEvent <I extends Individual, A extends FitnessMeter<I, V, C>, 
        C extends Comparable<? super C>, V extends Comparable<? super V>> {

    private final Population<I, A, C, V> generation;
    
    public EvolutionFinishedEvent(Population<I, A, C, V> generation) {
        this.generation = generation;
    }

    public Population<I, A, C, V> getGeneration() {
        return generation;
    }
}
