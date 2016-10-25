package geneticalgorithm;


import flowshop.MatrixFromFile;
import flowshop.RandomPermutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population
{
    private List<Individual> individuals;
    private Random random = new Random();
    private MatrixFromFile matrix;
    private int indSize;

    public Population(int size, MatrixFromFile matrixFromFile)
    {
        this.matrix = matrixFromFile;

        this.indSize = matrixFromFile.getOrderNum();

        individuals = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
        {
            individuals.add(new Individual(new RandomPermutation(matrixFromFile.getOrderNum())));
        }
    }

    public List<Individual> getIndividuals()
    {
        return individuals;
    }

    /**
     * Method that prints out the population.
     */
    public void print()
    {
        for (Individual ind : individuals)
        {
            System.out.print("X1:");
            ind.print();
        }

        System.out.println();
    }

    public Individual selectFitest()
    {
        Individual winner = new Individual(new RandomPermutation(matrix.getOrderNum()));
        winner.setFitness(999999999);

        for (Individual ind : individuals)
        {
            if (ind.getFitness() < winner.getFitness())
                winner = ind;
        }

        return winner;
    }

    /**
     * Metod that checks if all individuals have the same values.
     *
     * @return
     */
    public boolean allEqual()
    {

        return false;
    }

    /**
     * Method that creates a new generation crossing the previous generation.
     */
    public void newGenByTournament()
    {
        ArrayList<Individual> newGen = new ArrayList<>();
        while (newGen.size() < individuals.size())
        {
            newGen.addAll(crossoverOX(tournament(individuals), tournament(individuals)));
        }

        individuals = newGen;

        mutateAll();

        updateFitness();
    }

    /**
     * Method that triggers a mutation in all the individuals.
     */
    public void mutateAll()
    {
        for (Individual ind : individuals)
        {
            ind.mutate();
        }
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
        int next;

        while (selected.size() < candidates.size() / 2 + 1)
        {
            next = random.nextInt(candidates.size());
            if (!alreadySelected.contains(next))
                selected.add(candidates.get(next));
            alreadySelected.add(next);
        }

        return selected;
    }

    /**
     * Method that selects the better fitted individual from a part of the population.
     *
     * @return
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
     * @param pa1
     * @param pa2
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

        for(int i = 0; i < firstPoint; i++)
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
    public void updateFitness()
    {

    }
}
