/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package juanmf.ga.implementation;

import java.util.ArrayList;
import java.util.List;
import juanmf.ga.fitness.AptitudeMeter;
import juanmf.ga.operators.Crosser;
import juanmf.ga.operators.Evolution;
import juanmf.ga.structure.Gen;
import juanmf.ga.structure.Individual;
import juanmf.ga.operators.Mutator;
import juanmf.ga.structure.Population;
import juanmf.ga.structure.PopulationFactory;
import juanmf.ga.structure.IndividualFactory;
import juanmf.ga.operators.Selector;

/**
 * @param <I> A SubType of Individual.
 * @param <A> A Subtype of AptitudeMeter.
 * @param <C> A representation of an average Aptitude with a natural Order.
 * @param <V> A representation of an Aptitude Value.
 * @param <G> A Sub Type of Gen.
 *
 * @author juan.fernandez
 */
public class BasicEvolution <I extends Individual, A extends AptitudeMeter<I, V, C>, 
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
        while (!(aptitudeMeter.meetStopCondition(generation.getBest()) 
                || aptitudeMeter.meetStopCondition(generation.getBest(), generationNumber))) {
            selection = selector.makeSelection(generation);
            selection = crosser.crossOver(selection, populationNumber);
            mutator.mutate(selection);
            generation = BasicPopulation.createPopulation(aptitudeMeter, selection);
            generationNumber++;
        }
        return generation;
    }
}
