package geneticalgorithm;


import flowshop.MatrixFromFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GeneticAlgorithm
{
    private Scanner scanner;
    private Population population;
    private float itNumber;

    public GeneticAlgorithm(MatrixFromFile inputmatrix)
    {
        scanner = new Scanner(System.in);

        this.population = new Population(50, inputmatrix);
        population.updateFitness();

        this.itNumber = 500;

    }

    public List<Integer> run()
    {
        boolean solutionFound = false;
        Individual ind;
        float iterations = 0;
        int lastPercent = 0;
        while (!solutionFound)
        {
            iterations++;

            population.newGenByTournament();


            if((int)((iterations/itNumber)*100) > lastPercent) {
                System.out.print((int) ((iterations / itNumber) * 100) + "%...");
                lastPercent = (int) ((iterations / itNumber) * 100);
                if (lastPercent % 10 == 0) System.out.print("\n");
            }

            if(iterations > itNumber)
            {
                solutionFound = true;
            }
        }

        return population.getBest().getChromosomes();
    }
}