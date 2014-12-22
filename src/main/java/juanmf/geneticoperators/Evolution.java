/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package juanmf.geneticoperators;

/**
 * @param <I> A SubType of Individual.
 * @param <A> A Subtype of AptitudeMeter.
 * @param <C> A representation of an average Aptitude with a natural Order.
 * @param <V> A representation of an Aptitude Value.
 * @param <G> A SubType of Gen.
 *
 * @author juan.fernandez
 */
public interface Evolution <I extends Individual, A extends AptitudeMeter<I, V, C>, 
        C extends Comparable<? super C>, V extends Comparable<? super V>,
        G extends Gen> {
    
    Population<I, A, C, V> generatePopulation(IndividualFactory<I, G> factory);
    Population<I, A, C, V> evolve(Population<I, A, C, V> population);
}
