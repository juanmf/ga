/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package juanmf.ga.structure;

import java.util.Collection;

/**
 * @param <I> A SubType of Individual
 * @param <G> A SubType of Gen
 *
 * @author juan.fernandez
 */
public interface IndividualFactory <I extends Individual, G extends Gen> {
     I createRandomIndividual();
     I createIndividual(Collection<G> genList);
     
     /**
      * Generates a new individual with the Gen referenced by the linear idx 
      * representation changed randomly.
      * 
      * @param i
      * @param genIdx
      * @return 
      */
     I createIndividual(I i, int genIdx);
}
