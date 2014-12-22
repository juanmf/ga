/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package juanmf.geneticoperators;

import java.util.Collection;

/**
 * @param <I> subType of Individual, that accepts a comparable aptitude score.
 * @param <V> A representation of an Aptitude or fitness value.
 * @param <C> A representation of an average Aptitude with a natural Order.
 * 
 * @author juan.fernandez
 */
public interface AptitudeMeter<I extends Individual, V extends Comparable<? super V>, 
        C extends Comparable<? super C>> {
    V evaluate(I i);
    boolean meetStopCondition(I i);
    boolean meetStopCondition(I i, int generationNumber);
    
    /**
     * Updates an Average Aptitude value, given a new Individual, a population 
     * current size and the currentAverage.
     * 
     * @param individual      A new individual that's about to modify a population.
     * @param size            The population current size.
     * @param averageAptitude The population current averageAptitude.
     * 
     * @return The updated Average.
     */
    C updateAverageOnAdd(I individual, int size, C averageAptitude);
    C updateAverageOnRemove(I individual, int size, C averageAptitude);
    C getAverageAptitude(Collection<? extends I> c);
    C getAverageAptitude(V totalAptitude, int populationSize);
    V castAverageToAptitude(C average);
    
    /**
     * Returns a float representing the proportion of i1 compared to the sum of both.
     * 
     * For example, i1.getAptitude() is 10 and i2.getAptitude() is 20, then it'll
     * return the proportion of 10 in 30 (10 + 20) which is 0.333... 
     * 
     * @param i1 The 1st individual 
     * @param i2 The 2nd individual 
     * 
     * @return 
     */
    float getProportionBtwTwoIndividuals(I i1, I i2);
            
    /**
     * Compute the zero Average value for the Type C
     * 
     * @return The initial average for an empty population.
     */
    C getInitialAverage();
    V getZeroAptitude();
    
    /**
     * An overload of +
     * 
     * @param aptitude1
     * @param aptitude2
     * @return 
     */
    V getSum(V aptitude1, V aptitude2);
}
