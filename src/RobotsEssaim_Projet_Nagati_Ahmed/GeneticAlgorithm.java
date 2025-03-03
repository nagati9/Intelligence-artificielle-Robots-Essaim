package RobotsEssaim_Projet_Nagati_Ahmed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;



public   class GeneticAlgorithm {

	public SimulationVariables runGeneticAlgorithmWithoutGUI(List<SimulationVariables> maliste) {
	  // maliste est composée de l'échantillon initial 
	    if (maliste == null || maliste.isEmpty()) {
	        System.out.println("La liste de SimulationVariables est null ou vide.");
	        return null;
	    }

	   
//selection
	    SimulationVariables selectedSolution1 = selectSolution(maliste);
	    SimulationVariables selectedSolution2 = selectSolution(maliste);

//Crossover
	    SimulationVariables crossedSolution = crossover(selectedSolution1, selectedSolution2);
//Mutation	    
	    crossedSolution = mutate(crossedSolution);
//CHOIX ALEATOIRE D'UN DES ENFANTS 
	    if (crossedSolution == null) {
	        System.out.println("La solution croisée est null. Commencez les simulations.");
	        return null;
	    } else {
	        System.out.println("Variables suggérées : " + crossedSolution.toString());
	        return crossedSolution;
	    }
	}

  
    
	private SimulationVariables selectSolution(List<SimulationVariables> solutions) {
	    System.out.println("Selection based on roulette wheel");
	    // Calculate the total fitness (sum of extinguishedFires) of all solutions
	    double totalFitness = solutions.stream().mapToDouble(SimulationVariables::getExtinguishedFires).sum();

	    // Generate a random value between 0 and 1
	    Random random = new Random();
	    double randomValue = random.nextDouble();

	    // Scale the random value to represent a portion of totalFitness
	    randomValue *= totalFitness;

	    // Select a solution using roulette wheel selection
	    double runningSum = 0;
	    for (SimulationVariables solution : solutions) {
	        runningSum += solution.getExtinguishedFires();
	        if (runningSum >= randomValue) {
	            return solution;
	        }
	    }
	    // This should not happen under normal circumstances
	    return null;
	}


    private SimulationVariables crossover(SimulationVariables solution1, SimulationVariables solution2) {
        Random random = new Random();
        int crossoverIndex = random.nextInt(solution1.getAttributeCount()); // Choose a random index
System.out.println("crossing over at  " + random);
        // Perform crossover by swapping attributes from two solutions
        for (int i = crossoverIndex; i < solution1.getAttributeCount(); i++) {
            // Swap the attributes after the crossover index
            switchAttributes(solution1, solution2, i);
        }

        // Randomly choose one of the children
        System.out.println("Choosing child randomely " );
        if (random.nextBoolean()) {
            return solution1;
        } else {
            return solution2;
        }
    }

    private void switchAttributes(SimulationVariables solution1, SimulationVariables solution2, int index) {
        // Switch attribute values between two solutions at the given index
    	 System.out.println("Switching chromosomes at locus   "+ index );
        switch (index) {
       
            case 0:
                int tempRobots = solution1.getNumRobots();
                solution1.setNumRobots(solution2.getNumRobots());
                solution2.setNumRobots(tempRobots);
                break;
            case 1:
                double tempSpeed = solution1.getSpeed();
                solution1.setSpeed(solution2.getSpeed());
                solution2.setSpeed(tempSpeed);
                break;
            case 2:
                int tempFire = solution1.getNumFire();
                solution1.setNumFire(solution2.getNumFire());
                solution2.setNumFire(tempFire);
                break;
            case 3:
                double tempProbability = solution1.getPropagationProbability();
                solution1.setPropagationProbability(solution2.getPropagationProbability());
                solution2.setPropagationProbability(tempProbability);
                break;
            case 4:
                int tempResistance = solution1.getResistanceCount();
                solution1.setResistanceCount(solution2.getResistanceCount());
                solution2.setResistanceCount(tempResistance);
                break;
            
            default:
                break;
        }
    }
    
    
    public SimulationVariables mutate(SimulationVariables solution) {
        Random random = new Random();
        int attributeToMutate = random.nextInt(solution.getAttributeCount());

        switch (attributeToMutate) {
            case 0:
                // Mutate numRobots
                solution.setNumRobots(random.nextInt(100)); // Change the range as needed
                break;
            case 1:
                // Mutate speed
                solution.setSpeed(random.nextDouble() * 100); // Change the range as needed
                break;
            case 2:
                // Mutate numFire
                solution.setNumFire(random.nextInt(50)); // Change the range as needed
                break;
            case 3:
                // Mutate propagationProbability
                solution.setPropagationProbability(random.nextDouble());
                break;
            case 4:
                // Mutate resistanceCount
                solution.setResistanceCount(random.nextInt(10)); // Change the range as needed
                break;
           
            default:
                break;
        }
System.out.println("mutation at "+ attributeToMutate);
        return solution;
    }

}

