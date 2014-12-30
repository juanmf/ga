/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package juanmf.ga.implementation.fitness;

import juanmf.ga.fitness.FitnessMeter;
import juanmf.ga.structure.Individual;

/**
 * You can subclass this AptitudeMeter and get a basic implementation of the
 * meetStopCondition(I, int) for free.
 * 
 * It implements a public inner class, FirstGenerationWithBestFitness, that defer
 * to concrete implementation to set the zero Fitness value.
 * 
 * @param <I> subType of Individual, that accepts a comparable aptitude score.
 * @param <V> A representation of an Aptitude or fitness value.
 * @param <C> A representation of an average Aptitude with a natural Order.
 * 
 * @author juan.fernandez
 */
public abstract class BasicFitnessMeter <I extends Individual, V extends Comparable<? super V>, 
        C extends Comparable<? super C>> implements FitnessMeter<I, V, C> {
    
    /**
     * This is a marker that shows the last best fitness fount and the generation
     * in which it appeared. needed by {@see meetStopCondition(I, int)}
     */
    protected FirstGenerationWithBestFitness lastBestAptitude;

    /**
     * This is the number of generations we tolerate without improvements in
     * the best aptitude in a generation.
     */
    protected int maxGenerationsWithoutImprovement = 100; 

    /**
     * No way to determine how good a Team is.
     * 
     * @param i
     * 
     * @return false
     */
    @Override
    public boolean meetStopCondition(I i) {
        return false;
    }

    /**
     * An alternative to decide by the fitness of an individual itself is to keep
     * track of the best found on each generation, if the fitness does not improve 
     * in a considerable number of generations, this method should cut iteration by
     * returning true.
     * 
     * @param i
     * @param generationNumber
     * @return 
     */
    @Override
    public boolean meetStopCondition(I i, int generationNumber) {
        if (null == lastBestAptitude) {
            throw new NullPointerException("lastBestAptitude not initialized.");
        }
        System.out.println(
                "meetStopCondition: Current Best: " + i.getAptitude() 
              + ": Historical Best: " + lastBestAptitude.aptitude
        );
        if (0 < i.getAptitude().compareTo(lastBestAptitude.aptitude)) {
            lastBestAptitude.aptitude = (V) i.getAptitude();
            lastBestAptitude.generationNumber = generationNumber;
            return false;
        }
        return maxGenerationsWithoutImprovement 
                < (generationNumber - lastBestAptitude.generationNumber);
    }

    /**
     * Utility class that must be instantiated at concrete FitnessMeter construction 
     * time in order to keep track of the best aptitude found and in which generation
     * it was found.
     */
    public class FirstGenerationWithBestFitness {
        V aptitude;
        int generationNumber;

        public FirstGenerationWithBestFitness(V aptitude, int generationNumber) {
            this.aptitude = aptitude;
            this.generationNumber = generationNumber;
        }
    }
}
