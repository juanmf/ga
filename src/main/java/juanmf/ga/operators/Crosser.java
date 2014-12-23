/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package juanmf.ga.operators;

import juanmf.ga.structure.Individual;
import juanmf.ga.structure.Gen;
import java.util.Collection;
import java.util.List;

/**
 * @param <I> A subtype of Individual
 * @param <I> A subtype of Gen
 *
 * @author juan.fernandez
 */
public interface Crosser <I extends Individual, G extends Gen> {
    List<I> crossOver(I mom, I dad);
    List<I> crossOver(Collection<I> individualsToRecombine, int populationNumber);
}
