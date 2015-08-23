package juanmf.ga.implementation;

import com.google.common.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import juanmf.ga.fitness.FitnessMeter;
import juanmf.ga.operators.Crosser;
import juanmf.ga.Evolution;
import juanmf.ga.events.EvolutionFinishedEvent;
import juanmf.ga.structure.Gen;
import juanmf.ga.structure.Individual;
import juanmf.ga.operators.Mutator;
import juanmf.ga.structure.Population;
import juanmf.ga.structure.PopulationFactory;
import juanmf.ga.structure.IndividualFactory;
import juanmf.ga.operators.Selector;
import juanmf.grandt.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Generates successive generations of individuals asking AptitudeMeter for the
 * ending conditions, using Genetic operators to navigate the solution field.
 * 
 * @param <I> A SubType of Individual.
 * @param <A> A Subtype of AptitudeMeter.
 * @param <C> A representation of an average Aptitude with a natural Order.
 * @param <V> A representation of an Aptitude Value.
 * @param <G> A Sub Type of Gen.
 *
 * @author juan.fernandez
 */
public class BasicEvolution <I extends Individual, A extends FitnessMeter<I, V, C>, 
        C extends Comparable<? super C>, V extends Comparable<? super V>,
        G extends Gen> implements Evolution<I, A, C, V, G> {
    private static final int POPULATION_DEFAULT_NUMBER = 10000;
    private final int populationNumber;
    private final Selector selector;
    private final Mutator mutator;
    private final Crosser crosser;
    private final PopulationFactory<I, A, C, V> populationFactory;
    private final IndividualFactory<I, G> individualFactory;
    private final A aptitudeMeter;
    
    @Autowired
    EventBus eb;
    
    public BasicEvolution(Selector selector, Crosser crosser, Mutator mutator,
            IndividualFactory<I, G> individualFactory, A aptitudeMeter,
            PopulationFactory<I, A, C, V> populationFactory) {
        this(selector, crosser, mutator, individualFactory, aptitudeMeter, 
                populationFactory, POPULATION_DEFAULT_NUMBER);
    }
    
    public BasicEvolution(Selector selector, Crosser crosser, Mutator mutator, 
            IndividualFactory<I, G> individualFactory, A aptitudeMeter, 
            PopulationFactory<I, A, C, V> populationFactory, int populationNumber) {
        this.populationNumber = populationNumber;
        this.selector = selector;
        this.crosser = crosser;
        this.mutator = mutator;
        this.populationFactory = populationFactory;
        this.individualFactory = individualFactory;
        this.aptitudeMeter = aptitudeMeter;
    }

    @Override
    public Population<I, A, C, V> generatePopulation(
            IndividualFactory<I, G> individualFactory) {
        List<I> individuals = new ArrayList<>();
        for (int i = 0; i < populationNumber; i++) {
            individuals.add(individualFactory.createRandomIndividual());
        }
        return populationFactory.createPopulation(aptitudeMeter, individuals);
    }

    @Override
    public Population<I, A, C, V> evolve(Population<I, A, C, V> population) {
        List<I> selection;
        Population<I, A, C, V> generation = population;
        if (generation.isEmpty()) {
            generation = generatePopulation(individualFactory);
        }
        int generationNumber = 1;
        long time, selectionTime = 0, crosserTime = 0, mutationTime = 0;
        while (!(aptitudeMeter.meetStopCondition(generation.getBest()) 
                || aptitudeMeter.meetStopCondition(generation.getBest(), generationNumber))) {
            time = System.currentTimeMillis();
            selection = selector.makeSelection(generation);
            time = System.currentTimeMillis() - time;
            selectionTime += time;
            
            time = System.currentTimeMillis();
            selection = crosser.crossOver(selection, populationNumber);
            time = System.currentTimeMillis() - time;
            crosserTime += time;
            
            time = System.currentTimeMillis();
            mutator.mutate(selection);
            time = System.currentTimeMillis() - time;
            mutationTime += time;
            
            generation = BasicPopulation.createPopulation(aptitudeMeter, selection);
            generationNumber++;
        }
        eb.post(new EvolutionFinishedEvent(generation));
        System.out.println("Selection duration: " + selectionTime);
        System.out.println("Mutation duration: " + mutationTime);
        System.out.println("Crossing duration: " + crosserTime);
        return generation;
    }
}
