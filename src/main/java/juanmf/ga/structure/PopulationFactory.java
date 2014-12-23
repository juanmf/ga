/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package juanmf.ga.structure;

import juanmf.ga.fitness.AptitudeMeter;
import java.util.List;

/**
 * @param <I> A SubType of Individual.
 * @param <A> A Subtype of AptitudeMeter.
 * @param <C> A representation of an average Aptitude with a natural Order.
 * @param <V> A representation of an Aptitude value.
 *
 * @author juan.fernandez
 */
public interface PopulationFactory <I extends Individual, A extends AptitudeMeter<I, V, C>, 
        C extends Comparable<? super C>, V extends Comparable<? super V>> {
    
     Population<I, A, C, V> createEmptyPopulation();
     Population<I, A, C, V> createPopulation(A aptitudMeter, List<I> individuals);
}
