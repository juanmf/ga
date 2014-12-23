/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package juanmf.ga.operators;

import juanmf.ga.structure.Individual;
import java.util.Collection;
import java.util.List;

/**
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
