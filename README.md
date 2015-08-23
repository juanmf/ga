ga
==
A Generic implementation of Genetic Algorithms. So far only one strategy for 
each Genetic operator is implemented.

To solve a given problem, you need to implement: 
```java
  juanmf.ga.structure.Individual // In the example, Team
  juanmf.ga.structure.Gen // In the example, Player
  juanmf.ga.fitness.FitnessMeter // or subclass abstract juanmf.ga.implementation.BasicFitnessMeter
  juanmf.ga.structure.IndividualFactory // In the example, inner class of Team
  juanmf.ga.structure.PopulationFactory // In the example, inner class of Team
```

Then expose individualFactory, populationFactory and fitnessMeter beans as follows.

```java
    @Configuration
    public class Config {

        @Bean(name="individualFactory")
        public IndividualFactory getIndividualFactory() {
            return new Team.TeamFactory();
        }

        @Bean(name="populationFactory")
        public PopulationFactory getPopulationFactory() {
            return new Team.TeamPopulationFactory();
        }

        @Bean(name="fitnessMeter")
        public FitnessMeter getFitnessMeter() {
            System.out.println("getFitnessMeter");
            return new TeamAptitudeMeter();
        }
    }
```

Then run the project and whatch the evolution process in console.
```
meetStopCondition: Current Best: 1310: Historical Best: 0
Selection individuals: 5806
meetStopCondition: Current Best: 1310: Historical Best: 1310
Selection individuals: 5931
...
Mutating at gen:14. individual:/*individual toString output*/
...
// X generations after last time the best changed.
meetStopCondition: Current Best: 2116: Historical Best: 2116
Selection individuals: 9921
meetStopCondition: Current Best: 2116: Historical Best: 2116
/*individual toString output of the best Individual in the last generation*/ 
```

Crossing Strategies
===================

Currently only Reduces Surrogate method is used for Crossing, in two flavors,
single thread and non-blocking multi-thread(@Primary). Multi-thread is preferred 
because it's marked with @Primary annotation, feel free to change it if you want 
to try the single threaded.

Feel free to contribute other strategies you might find of interest.

BasicFitnessMeter
=================

It implements a default stop condition, if the best didn't improve in last 100 
generations, it's meetStopCondition() returns true.

Selection Strategy
==================

Stochastic universal sampling method is used.
