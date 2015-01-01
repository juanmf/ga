package juanmf.ga.implementation.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import juanmf.ga.fitness.FitnessMeter;
import juanmf.ga.operators.Crosser;
import juanmf.ga.structure.Gen;
import juanmf.ga.structure.Individual;
import juanmf.ga.structure.IndividualFactory;

/**
 * Reduced surrogate Implementation.
 * 
 * Recombines the given parents to generate offsprings, normally two offsprings.
 * 
 * @param <I> A subtype of Individual
 * 
 * @author juan.fernandez
 */
public class BasicReducesSurogateCrossOver <I extends Individual, G extends Gen> 
        implements Crosser<I, G> {
        
    /**
     * Random cross point generator.
     */
    private static final Random RANDY = new Random();
    private final IndividualFactory individualFactory;

    public BasicReducesSurogateCrossOver(IndividualFactory individualFactory) {
        this.individualFactory = individualFactory;
    }
    
    /**
     * Recombines two individuals using variable (random) crosspoint, but trying 
     * to ensure that genes chain differ, a chain too short might be equal in both.
     * 
     * @param mom An individual to recombine.
     * @param dad An individual to recombine.
     * 
     * @return an empty List if both are equal, a list with two offsprings if 
     * everything when fine. a List with mom and dad if they could not recombine 
     * into a viable son.
     */
    @Override
    public List<I> crossOver(I mom, I dad) {
        if (mom.equals(dad)) {
            // take care of convergence, if both are equal return none, chances of 
            // loosing this combination are very low.
            System.out.println("Crossing equal individuals, eliminating both: " + mom.hashCode());
            return Collections.emptyList();
        }
        int size = mom.size();
        Iterator<G> parent1It = mom.iterator();
        Iterator<G> parent2It = dad.iterator();
        Individual son1, son2;
        List<G>  offspring1 = new ArrayList<>(), offspring2 = new ArrayList<>();
        boolean chainsDiffer = false;
        boolean parentsNotSwitched = true;
        int i = 0, tries = 10;
        do {
            int crossPoint = RANDY.nextInt(size);
            for (; i < size; i++) {
                G gen1 = parent1It.next();
                G gen2 = parent2It.next();
                offspring1.add(gen1);
                offspring2.add(gen2);
                if (!chainsDiffer) {
                    chainsDiffer = !gen2.equals(gen1);
                } else if (i >= crossPoint && parentsNotSwitched) {
                    Iterator<G> aux = parent1It;
                    parent1It = parent2It;
                    parent2It = aux;
                    parentsNotSwitched = false;
                }
            }
            son1 = individualFactory.createIndividual(offspring1);
            son2 = individualFactory.createIndividual(offspring2);
        } while ((null == son1 || null == son2) && 0 < --tries);
        // If I couldn't find viable sons return parents in inverse order.
        // TODO: no need to return both sons or both parents, if one son could spawn.
        return (List<I>) ((null == son1 || null == son2) ? Arrays.asList(dad, mom) : Arrays.asList(son1, son2));
    }

    /**
     * Recombines the whole List of individuals.
     * 
     * It shuffles the List before recombining, otherwise convergence happens 
     * too soon. It also takes care of preserving Elite and unmarking them.
     * 
     * @param individualsToRecombine
     * @param populationNumber
     * 
     * @return The new generation of individuals, ready to form a new {@see Population}
     * @see Individual.isElite()
     * @see Selector
     */
    @Override
    public List<I> crossOver(List<I> individualsToRecombine, int populationNumber) {
        // TODO: limited to even populations. If you don't want the las Individual to be lost.
        List<I> offsprings = new ArrayList<>();
        Collections.shuffle(individualsToRecombine);
        Iterator<I> it = individualsToRecombine.iterator();
        while (populationNumber >= 0) {
            List<I> crossingResults = Collections.emptyList();
            I mom = null; 
            I dad = null;
            try {
                mom = it.next();
                dad = it.next();
                if (preserveElite(offsprings, mom)) {
                    populationNumber--;
                }
                if (preserveElite(offsprings, dad)) {
                    populationNumber--;
                }
                offsprings.addAll(crossingResults = crossOver(mom, dad));
            } catch (NoSuchElementException e) {
                it = individualsToRecombine.iterator();
                if (null != mom) {
                    dad = it.next();
                    offsprings.addAll(crossOver(mom, dad));
                }
            }
            populationNumber -= crossingResults.size();
        }
        return offsprings;
    }
    
    /**
     * Checks whether or not the individual is elite and, if so, adds it to the
     * list that will be passed to the next generation.
     * 
     * @param offsprings The list of offsprings for the next generation.
     * @param individual the individual, from current generation that might pass 
     * through.
     * 
     * @return true if it was elite, false otherwise. 
     */
    private boolean preserveElite(List<I> offsprings, I individual) {
        if (individual.isElite()) {
            offsprings.add(individual);
            individual.setElite(false);
            return true;
        }
        return false;
    }
}
