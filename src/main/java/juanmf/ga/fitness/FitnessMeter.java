package juanmf.ga.fitness;

import java.util.Collection;
import juanmf.ga.structure.Individual;

/**
 * Aptitude 
 * @param <I> subType of Individual, that accepts a comparable aptitude score.
 * @param <V> A representation of an Aptitude or fitness value.
 * @param <C> A representation of an average Aptitude with a natural Order.
 * 
 * @author juan.fernandez
 */
public interface FitnessMeter<I extends Individual, V extends Comparable<? super V>, 
        C extends Comparable<? super C>> {
    V evaluate(I i);
    /**
     * If there is a way to determine if the Individual is an acceptable optimal 
     * solution, this method should tell so, otherwise it should return always false.
     * 
     * @param i An individual subtype
     * 
     * @return true if i is an optimal solution. false if not known or rejected.
     */
    boolean meetStopCondition(I i);
    
    /**
     * An alternative to decide by the fitness of an individual itself is to keep
     * track of the best found on each generation, if the fitness does not improve 
     * in a considerable number of generations, this method should cut iteration by
     * returning true.
     * 
     * Be ware of premature convergence, a singles superman could appear many times
     * in the population to come up with a better solution.
     * 
     * @param i An Individual subtype
     * 
     * @param generationNumber
     * @return 
     */
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
    
    /**
     * Updates an Average Aptitude value, given an Individual to be removed from 
     * a population current size and the currentAverage.
     * 
     * @param individual      A new individual that's about to modify a population.
     * @param size            The population current size.
     * @param averageAptitude The population current averageAptitude.
     * 
     * @return The updated Average.
     */
    C updateAverageOnRemove(I individual, int size, C averageAptitude);
    
    /**
     * Computes the average Fitness of a Collection of Individuals.
     * 
     * @param c A Collection containing all the individuals
     * 
     * @return The Average Aptitude
     */
    C getAverageFitness(Collection<? extends I> c);
    
    /**
     * Computes the average Fitness given the total Aptitude and de population
     * size.
     * 
     * @param totalAptitude  The cumulative {@see getSum()} of all fitness.
     * @param populationSize The population size.
     *
     * @return The Average Aptitude
     */
    C getAverageFitness(V totalAptitude, int populationSize);
    
    /**
     * Returns (type-safely) a representation of an average fitness in the type 
     * of fitness.
     * 
     * @param average The average value.
     * 
     * @return the representation in the type of fitness.
     */
    V castAverageToFitness(C average);
    
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
    
    /**
     * Returns the Zero or neutral value in the Fitness type. 
     * 
     * This is the right value if and only if the following is true
     * i1.getFitness().equals(getSum(getZeroFitness(), i1.getFitness())) 
     * 
     * @return 
     */
    V getZeroFitness();
    
    /**
     * An overload of +
     * 
     * @param aptitude1
     * @param aptitude2
     * @return 
     */
    V getSum(V aptitude1, V aptitude2);
}
