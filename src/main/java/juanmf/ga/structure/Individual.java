/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package juanmf.ga.structure;

import java.util.Collection;
import java.util.Map;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Instances of Individual should be final and inmutable.
 * 
 * @param <V> The type of object that describes the aptitude.
 * @param <E> The Type of object that describes a valid key in the (multidimensional)
 * Map of Genes.
 * @param <J> The Type of object that's used by a Gen's value<J>
 * @param <T> A SubType of Type Gen
 * @param <I> A SubType of Individual used by {@link Comparable.CompareTo()}
 * 
 * @author juan.fernandez
 */
public interface Individual <V extends Comparable<? super V>, E, J, T extends Gen<J>, 
        I extends Individual> extends Comparable<I>, Iterable<T> {
    
    /**
     * Retuns this individual's aptitude or fitness value. which must be a final
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
    boolean isElite();
    void setElite(boolean isElite);
    
    /**
     * Returns a specific gen.  
     * 
     * @param genPositionKeys
     * @return 
     */
    Gen<J> getGen(Collection<E> genPositionKeys);
    
    /**
     * Returns the number of Genes in this individual. 
     *
     * @return the number of Genes in this individual
     */
    int size();
}
