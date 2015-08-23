package juanmf.ga.operators;

import com.google.common.eventbus.EventBus;
import juanmf.ga.structure.Individual;
import juanmf.ga.structure.Gen;
import java.util.Collection;
import java.util.List;

/**
 * Objects implementing this interface should be able to recombine Individuals.
 * 
 * By iterating through individual's genes the chosen strategy should assign the 
 * selected genes to the offspring. Care must be taken to keep the population to
 * its intended number, after Selection might have ripped it to a lesser number of
 * individuals.
 * 
 * If elitist selection is to be performed, consider the {@see Individual.isElite()}
 * to figure out whether or not to pass the individual unchanged to the next 
 * generation. And be careful to unmark it so it wont be passed through indefinitely
 * to successive generations, unless Selector marks it again.
 * 
 * @param <I> A subtype of Individual
 * @param <G> A subtype of Gen
 *
 * @author juan.fernandez
 */
public interface Crosser <I extends Individual, G extends Gen> {
    /**
     * Recombines the given parents to generate offsprings, normally two offsprings.
     * 
     * Take special care here to avoid premature convergence. If mom and dad are 
     * equal, recombining is useless. And maybe returning two identical offsprings
     * ain't the best idea.
     * 
     * @param mom An Individual to recombine.
     * @param dad An Individual to recombine.
     * 
     * @return The newly created offsprings.
     */
    List<I> crossOver(I mom, I dad);
    
    /**
     * Recombines the whole List of individuals.
     * 
     * Care must be taken to keep the population to its intended number. As
     * individualsToRecombine, returned by Selector can be much less than intended 
     * population number.
     * 
     * @param individualsToRecombine
     * @param populationNumber
     * 
     * @return The new generation of individuals, ready to form a new {@see Population}
     */
    List<I> crossOver(List<I> individualsToRecombine, int populationNumber);
}
