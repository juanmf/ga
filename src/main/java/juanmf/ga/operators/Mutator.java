package juanmf.ga.operators;

import juanmf.ga.structure.Individual;
import java.util.List;

/**
 * Implementors of this Interface should be able to mutate genes of given individuals.
 * 
 * You will need access to the genes field in order to get a random one, either 
 * through constructor injection or Individual implementation's accessor.
 * 
 * @author juan.fernandez
 */
public interface Mutator<I extends Individual> {
    /**
     * With a probability, mutate the given Individual.
     * 
     * @param i An individual
     * 
     * @return The individual, possibly mutated.
     */
    I mutate(I i);
    
    /**
     * Apply {@link mutate} to the Collection.
     * 
     * @param i A Collection of Individual.
     * 
     * @return 
     */
    List<I> mutate(List<I> i);
}
