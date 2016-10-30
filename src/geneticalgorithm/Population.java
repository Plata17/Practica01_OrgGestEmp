package geneticalgorithm;


import flowshop.FlowShop;
import flowshop.MatrixFromFile;
import flowshop.RandomPermutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population
{

    private List<Individual> individuals;
    private Random random;
    private MatrixFromFile matrix;

    private int indSize;

    public Population(int size, MatrixFromFile matrixFromFile)
    {
        random = new Random();

        this.matrix = matrixFromFile;

        this.indSize = matrixFromFile.getOrderNum();

        individuals = new ArrayList<>();

        for (int i = 0; i < size; i++)
        {
            individuals.add(new Individual(new RandomPermutation(matrixFromFile.getOrderNum())));
        }
    }

    /**
     * Method that creates a new generation crossing the previous generation.
     */
    void newGen()
    {
        ArrayList<Individual> newGen = new ArrayList<>();

        individuals = sortIndividuals(individuals);

        newGen.add(getBest());

        while (newGen.size() < individuals.size())
        {
            newGen.addAll(crossoverOX(tournament(individuals), tournament(individuals)));
        }

        individuals = newGen;

        mutateAll();
        updateFitness();
    }

    private List<Individual> sortIndividuals(List<Individual> individuals)
    {
        updateFitness();

        individuals.sort((o1, o2) -> Float.compare(o1.getFitness(), o2.getFitness()));

        List<Individual> resorted = new ArrayList<>();

        for (int i = individuals.size() - 1; i >= 0; i--)
        {
            resorted.add(individuals.get(i));
        }

        individuals = resorted;

        return individuals;
    }

    /**
     * Method that triggers a mutation in all the individuals.
     */
    private void mutateAll()
    {
        individuals.forEach(Individual::mutate);
    }

    /**
     * Method that randomly selects half the individuals for the tournament.
     *
     * @return arraylist with the selected individuals.
     */
    private List<Individual> tournamentSelection(List<Individual> candidates)
    {
        List<Individual> selected = new ArrayList<>();
        List<Integer> alreadySelected = new ArrayList<>();

        candidates = sortIndividuals(candidates);
        int next;

        while (selected.size() < candidates.size() / 10)
        {
            next = random.nextInt(candidates.size());
            selected.add(candidates.get(next));
            alreadySelected.add(next);
        }

        return selected;
    }

    /**
     * Method that selects the better fitted individual from a part of the population.
     *
     * @return the fittest.
     */
    private Individual tournament(List<Individual> candidates)
    {
        List<Individual> participants = tournamentSelection(candidates);

        updateFitness();

        Individual winner = new Individual(new RandomPermutation(matrix.getOrderNum()));
        winner.setFitness(999999999);

        for (Individual ind : participants)
        {
            if (ind.getFitness() < winner.getFitness())
                winner = ind;
        }

        return winner;
    }

    /**
     * Method that crosses the chromosomes of two individuals to create a new one.
     *
     * @param pa1 first parent.
     * @param pa2 second parent.
     * @return the new individuals.
     */
    private List<Individual> crossoverOX(Individual pa1, Individual pa2)
    {
        int firstPoint = random.nextInt(indSize) + 1;
        int secondPoint = 0;
        List<Integer> chrom1 = new ArrayList<>();
        List<Integer> chrom2 = new ArrayList<>();

        while (secondPoint < firstPoint)
        {
            secondPoint = random.nextInt(indSize) + 1;
        }

        for (int i = 0; i < firstPoint; i++)
        {
            chrom1.add(pa1.getChromosomes().get(i));
            chrom2.add(pa2.getChromosomes().get(i));
        }

        for (int i = firstPoint; i < secondPoint; i++)
        {
            chrom1.add(pa2.getChromosomes().get(i));
            chrom2.add(pa1.getChromosomes().get(i));
        }

        for (int i = secondPoint; i < indSize; i++)
        {
            chrom1.add(pa1.getChromosomes().get(i));
            chrom2.add(pa2.getChromosomes().get(i));
        }

        List<Individual> children = new ArrayList<>();
        children.add(new Individual(chrom1));
        children.add(new Individual(chrom2));

        return children;
    }

    /**
     * Method that forces all individuals to calculate their fitness.
     */
    void updateFitness()
    {
        for (Individual ind : individuals)
        {
            ind.setFitness(new FlowShop(matrix, ind.getChromosomes()).fmed());
        }
    }

    Individual getBest()
    {
        return tournament(individuals);
    }

    public List<Individual> getIndividuals()
    {
        return individuals;
    }

    public void print()
    {
        individuals.forEach(Individual::print);
    }

    void printFitness()
    {
        System.out.println("Best: " + getBest().getFitness());
    }
}
