ga
==
A Generic implementatin of Genetic Algorithms. So far only one strategy for 
each Genetic operatr is implemented. Feel free to contribute if you need more.

Performance is compromised by the fact of generifing aptitude and average aptitude 
values instead of assuming primitive float.

To solve a given problem, you need to implement: 
```java
  juanmf.ga.structure.Individual // In the example, Team
  juanmf.ga.structure.Gen // In the example, Player
  juanmf.ga.fitness.AptitudeMeter // or subclass abstract juanmf.ga.implementation.BasicFitnessMeter
  juanmf.ga.structure.IndividualFactory // In the example, inner class of Team
  juanmf.ga.structure.PopulationFactory // In the example, inner class of Team
```

Then Initialize the Evolution (BasicEvolution) like this

```java
public class App
{
    public static void main(String[] args)
    {
        TeamAptitudeMeter aptitudeMeter = new TeamAptitudeMeter();
        Selector<Team, TeamAptitudeMeter, Float, Integer> selector = 
                new BasicSusSelector(aptitudeMeter);
        IndividualFactory<Team, Player> individualFactory = new Team.TeamFactory();
        Crosser<Team, Player> crosser = new BasicReducesSurogateCrossOver<>(individualFactory);
        Mutator<Team> mutator = new BasicMutator<>(individualFactory);
        PopulationFactory<Team, TeamAptitudeMeter, Float, Integer> populationFactory =
                new Team.TeamPopulationFactory();
        
        Evolution e = new BasicEvolution(
                selector, crosser, mutator, individualFactory, aptitudeMeter, 
                populationFactory
            );
        System.out.println(
                e.evolve(BasicPopulation.createPopulation(aptitudeMeter)).getBest()
            );
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
