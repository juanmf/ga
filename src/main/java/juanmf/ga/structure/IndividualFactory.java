package juanmf.ga.structure;

import java.util.Collection;

/**
 * This Factory should know how to translate a plain List<Gen> into a new Individual.
 * 
 * Receiving a List<Gen> of mixed Genes to build a new Individual, check for its 
 * validity, and return the valid Individual (actual possible solution) or null, 
 * if the genes sequence is not viable. 
 * 
 * Even though the structure is limited to a multidimensional Map. Crosser, by 
 * iterating individuals provides a mixed gen list which is the prime matter to 
 * form new Individuals. So the mapping from a plain list to the Map responsability 
 * is encapsulated here.
 * 
 * @param <I> A SubType of Individual
 * @param <G> A SubType of Gen
 *
 * @author juan.fernandez
 */
public interface IndividualFactory <I extends Individual, G extends Gen> {
    /**
     * Generates a random individual.
     * 
     * Should be able to use/generate all possible genes.
     * 
     * @return 
     */
    I createRandomIndividual();
     
    /**
     * Generates an Individual using the Collection of Genes provided.
     * 
     * @param genList
     * 
     * @return The new Individual.
     */
    I createIndividual(Collection<G> genList);
     
    /**
     * Generates a new individual with the Gen referenced by the linear idx 
     * representation changed randomly.
     * 
     * @param i      The individual to mutate.
     * @param genIdx The selected Gen index to be changed.
     * 
     * @return The new Individual.
     */
    I createIndividual(I i, int genIdx);
}
