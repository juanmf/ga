/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package juanmf.ga.operators;

import juanmf.ga.structure.Individual;
import juanmf.ga.fitness.AptitudeMeter;
import juanmf.ga.structure.Population;
import java.util.List;

/**
 * @param <I> A SubType of Individual.
 * @param <A> A Subtype of AptitudeMeter.
 * @param <C> A representation of an average Aptitude with a natural Order.
 * @param <V> A representation of an Aptitude Value.
 *
 * @author juan.fernandez
 */
public interface Selector <I extends Individual, A extends AptitudeMeter<I, V, C>, 
        C extends Comparable<? super C>, V extends Comparable<? super V>> {
    
    List<I> makeSelection(Population<I, A, C, V> population);
}
