ga
==
A Generic implementatin of Genetic Algorithms. So far only one strategy for 
each Genetic operatr is implemented. Feel fre to contribute if you need more.

You need to implement: 
```java
  Individual // In the example, Team
  Gen // In the example, Player
  AptitudeMeter 
  IndividualFactory
  PopulationFactory
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
