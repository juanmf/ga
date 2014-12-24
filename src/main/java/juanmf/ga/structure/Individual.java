package juanmf.ga.structure;

import java.util.Collection;
import java.util.Map;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * An individual must be a possible solution to the given problem, an element in 
 * a Population.
 * 
 * Its structure is open, and must be defined after de problem. But it must be able
 * to iterate over its genes, in order to recombine with a neighbour. Though Structure
 * is limited to a multidimensional Map. e.g. <pre> Map<Ob, Map<Ob2, ...>> </pre>
 * 
 * Instances of Individual should be final and inmutable, so they can be freely 
 * shared.
 * 
 * @param <V> The type of object that describes the aptitude.
 * @param <E> The Type of object that describes a valid key in the (multidimensional)
 * Map of Genes.
 * @param <T> A SubType of Type Gen
 * @param <I> A SubType of Individual used by {@link Comparable.CompareTo()}
 * 
 * @author juan.fernandez
 */
public interface Individual <V extends Comparable<? super V>, E, T extends Gen, 
        I extends Individual> extends Comparable<I>, Iterable<T> {
    /**
     * Returns this individual's aptitude or fitness value. which must be a final
     * value initialized in constructor.
     * 
     * @return 
     */
    V getAptitude();
    
    /**
     * Finds out if this individual if "compatible with life". some restrictions 
     * might apply to individuals, other than fitness function that determines if
     * it is, indeed, a possible solution to the problem. Where fitness must tell
     * us how good the solution is.
     * 
     * @return 
     */
    boolean isLegal();
    
    /**
     * In order to preserve the individual through the crossover phase, Selector
     * should mark it as Elite.
     * 
     * @return 
     */
    void setElite(boolean isElite);
    
    /**
     * Returns the Elite value, if it was marked by Selector, Crosser should unmark
     * it when passing it to the next generation.
     * 
     * @return 
     */
    boolean isElite();
    
    /**
     * Returns a specific gen.  
     * 
     * @param genPositionKeys
     * @return 
     */
    Gen getGen(Collection<E> genPositionKeys);
    
    /**
     * Returns the number of Genes in this individual. 
     *
     * @return the number of Genes in this individual
     */
    int size();
}
