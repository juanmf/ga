package juanmf.ga.implementation.operators;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.util.Arrays;
import java.util.Collections;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import juanmf.ga.events.EvolutionFinishedEvent;
import juanmf.ga.structure.Gen;
import juanmf.ga.structure.Individual;
import juanmf.ga.structure.IndividualFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Reduced surrogate Implementation.
 * 
 * Recombines the given parents to generate offsprings, normally two offsprings.
 * 
 * @param <I> A subtype of Individual
 * 
 * @author juan.fernandez
 */
//@Component
public class ConcurrentBasicReducesSurogateCrossOver <I extends Individual, G extends Gen> 
        extends BasicReducesSurogateCrossOver<I, G> implements EventListener {
    
    @Autowired
    private EventBus eb;
            
    private static final int THREADS = Runtime.getRuntime().availableProcessors();
    private final ExecutorService ex;
        
    public ConcurrentBasicReducesSurogateCrossOver(IndividualFactory individualFactory) {
        super(individualFactory);
        ex = Executors.newFixedThreadPool(THREADS);
    }

    @Subscribe
    public void handleEvolutionFinish(EvolutionFinishedEvent e) {
        ex.shutdownNow();
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
        // TODO: limited to even populations. If you don't want the last Individual to be lost.
        Individual[] offsprings;
        offsprings = new Individual[populationNumber];
        Collections.shuffle(individualsToRecombine);
        CopyOnWriteArrayList<I> concurrentList = new CopyOnWriteArrayList(individualsToRecombine); 
        Iterator<I> it = concurrentList.iterator();
        AtomicInteger atomPopulationNumber = new AtomicInteger(populationNumber - 1); 
        CountDownLatch latch = new CountDownLatch(THREADS);
        for (int i = 0; i < THREADS; i++) {
            ex.execute(new CrosserWorker(it, concurrentList, offsprings, atomPopulationNumber, latch));
        }
        try {
            latch.await();
        } catch (InterruptedException ex) {
        }
        // Historical Best lost!!
        //meetStopCondition: Current Best: 2105: Historical Best: 2114
        return (List<I>) Arrays.asList(offsprings);
    }
    
    /**
     * Checks whether or not the individual is elite and, if so, adds it to the
     * list that will be passed to the next generation.
     * 
     * @param offsprings The list of offsprings for the next generation.
     * @param i1 the individual, from current generation that might pass 
     * through.
     * 
     * @return true if it was elite, false otherwise. 
     */
    private void preserveElite(Individual[] offsprings, AtomicInteger index, I i1, I i2) {
        int count = 0;
        if (i1.isElite()) {
            offsprings[index.getAndDecrement()] = i1;
            i1.setElite(false);
        }
        if (i2.isElite()) {
            offsprings[index.getAndDecrement()] = i2;
            i2.setElite(false);
        }
    }
    
    private class CrosserWorker implements Runnable {
        private AtomicInteger index;
                
        private Individual[] offsprings;
        private CopyOnWriteArrayList<I> individualsToRecombine;
        private CountDownLatch latch;
        private Iterator<I> it;

        private CrosserWorker(
                Iterator<I> it, CopyOnWriteArrayList<I> individuals, 
                Individual[] offsprings, AtomicInteger populationNumber, CountDownLatch latch
        ) {
            this.offsprings = offsprings;
            this.individualsToRecombine = individuals;
            this.index = populationNumber;
            this.it = it;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                I mom = it.next(); 
                I dad = it.next();
                while (index.get() > 0) {
                    List<I> crossingResults;
                    try {
                        preserveElite(offsprings, index, mom, dad);
                        crossingResults = crossOver(mom, dad);
                        for (I i : crossingResults) {
                            int ind = index.getAndDecrement();
                            if (0 <= ind) {
                                offsprings[ind] = i;
                            }
                        }
                        mom = null; 
                        dad = null;
                        mom = it.next();
                        dad = it.next();
                    } catch (NoSuchElementException ignored) {
                        it = individualsToRecombine.iterator();
                        if (null == mom) {
                            mom = it.next();
                        }
                        dad = it.next();
                    }
                }
            } finally {
                latch.countDown();
            }
        }
    }
}
