package RobotsEssaim_Projet_Nagati_Ahmed;

import javax.swing.*;
import java.awt.*;

import java.util.Random;

public class FireSimulation extends JFrame {
	 private static final int WIDTH = 700;
	    private static final int HEIGHT = 600;
    private  int numRobots ;
    private  double speed;
    private  int numFire;
    private  double propagationProbability;
    private double[][] robotPositions;
    private boolean[][] fireGrid;
    private int resistanceCount;
    private Random rand = new Random();
    private InitialSetupWindow initialSetupWindow; 
    private volatile boolean running = false;
    private int length;
    double extinguishedFires = 0; 
    private int[][] robotWaterReservoir;
    private double[][] pheromoneGrid;
    private int[][] rechargeBases = {
    	    { 300, 350 }           
    	};

//-------------------------------------------Constructeur------------------------------------------------------------------//
	public FireSimulation(InitialSetupWindow initialSetupWindow,int robotCount, 
			double propagationProbability2, int fireCount, double speed, int resistanceCount , int length) {
		// TODO Auto-generated constructor stub
		this.length=length;
		this.robotWaterReservoir= new int[numRobots][2];
		this.initialSetupWindow = initialSetupWindow;
		this.resistanceCount=resistanceCount;
		this.numRobots=robotCount;
		this.speed=speed;
    	this.propagationProbability=propagationProbability2;
    	this.numFire=fireCount;
    	fireGrid = new boolean[WIDTH][HEIGHT];
    	 if (initialSetupWindow == null) {
    	       
    	        this.running = false;
    	        this.setVisible(false);
    	        this.dispose();
    	    } else {
    	        setTitle("Fire Simulation");
    	        setSize(WIDTH, HEIGHT);
    	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	        setLocationRelativeTo(null);
    	        this.running = false;
    	        initSimulation();
    	        initRobotPositions();
    	        startSimulation();
    	        setVisible(true);
    	    }
    	}
        
	//-------------------------------------------Initialisation et update------------------------------------------------------------------//
       

	public void initializeFire() {
        fireGrid = new boolean[WIDTH][HEIGHT];
        Random random = new Random();

        // Initialisation du feu selon une méthode spécifique
        for (int i = 0; i < numFire; i++) {
        	
        	 int x = random.nextInt(580) +20; // Random x between 20 and 600 (600 - 20 = 580)
             int y = random.nextInt(580) +20; // Random y between 20 and 600

              System.out.println("fire  "+i+" at "+ x+" "+ y);

              fireGrid[x][y] = true;

            //fireGrid[x][y] = true;
        }
    }

	private void initSimulation() {
        robotPositions = new double[numRobots][2];
       
        initializeFire();
        pheromoneGrid = new double[WIDTH][HEIGHT]; // Initialisation de la grille de phéromones

        // Initialisation des phéromones sur la grille
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                pheromoneGrid[i][j] = 0.0; // Ajustez la valeur initiale si nécessaire
            }
        }
    }
	
	
	 public boolean isRunning() {
	        return running;
	    }
	 public void start() {
		    running = true;
		    startSimulation();
		}
	 public SimulationVariables stopSimulation() {
		// System.out.println("total ="+ extinguishedFires);
		 
		 double feuEteint=extinguishedFires/fireGrid.length;
		 SimulationVariables sim=new SimulationVariables(numRobots, speed, numFire, propagationProbability, resistanceCount, feuEteint);
		    running = false;
		    setVisible(false);
		    dispose();
			return sim;
		}
	 
	

	 private void initRobotPositions() {
		    double separationDistance = 10; // Adjust this distance as needed
		    int numColumns = (int) Math.sqrt(numRobots); // Distribute robots in a square grid

		    for (int i = 0; i < numRobots; i++) {
		        int row = i / numColumns;
		        int col = i % numColumns;

		        // Initialize robots near the charging bases
		        int baseIndex = i % rechargeBases.length;
		        int baseX = rechargeBases[baseIndex][0];
		        int baseY = rechargeBases[baseIndex][1];

		        robotPositions[i][0] = baseX + (col + 1) * separationDistance; // X position
		        robotPositions[i][1] = baseY + (row + 1) * separationDistance; // Y position

		        initRobotWaterReservoir();
		    }
		}


    
    private void initRobotWaterReservoir() {
        robotWaterReservoir = new int[numRobots][2]; // [][0] for capacity, [][1] for current level
        
        for (int i = 0; i < numRobots; i++) {
            // Set the capacity for each robot (e.g., 5 units)
            robotWaterReservoir[i][0] = 5;

            // Set the initial level for each robot's water reservoir (e.g., full capacity)
            robotWaterReservoir[i][1] = robotWaterReservoir[i][0];
        }
    }

    public void startSimulation() {
        Thread simulationThread = new Thread(() -> {
            while (running) { 
                moveRobotsToFire();
                propagateFire();
                repaint();
                try {
                    Thread.sleep(900);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
          
        });
        simulationThread.start();
    }

 //-------------------------------------------Comportements du robot------------------------------------------------------------------//


    void moveRobotsToFire() {
        for (int i = 0; i < numRobots; i++) {
        	 
            int currentX = (int) robotPositions[i][0];
            int currentY = (int) robotPositions[i][1];

            if (fireGrid[currentX][currentY]) {
                while (robotWaterReservoir[i][1] > 0 && fireGrid[currentX][currentY]) {
                    robotWaterReservoir[i][1]--;
                   
                    // Update current position after decrementing water reservoir
                    currentX = (int) robotPositions[i][0];
                    currentY = (int) robotPositions[i][1];
                }

                if (robotWaterReservoir[i][1] <= 0) {
                    // Robot's water is empty, move towards the charging station
                    // moveToBase(i, currentX, currentY);
                	
                    int[] nearestBase = findNearestRechargeBase(currentX, currentY);
                  //  System.out.println(" moving Robot  "+ i +" " + currentX+ " "+  currentY + " to water station");
                    robotPositions[i][0] = nearestBase[0];
                    robotPositions[i][1] = nearestBase[1];
                    continue; // Move to the next robot
                }
                updatePheromones();
            }
           
            int[] nearestFire = findNearestFire(currentX, currentY);
            int targetX = nearestFire[0];
            int targetY = nearestFire[1];

            double dx = targetX - currentX;
            double dy = targetY - currentY;

            double magnitude = Math.sqrt(dx * dx + dy * dy);
            double normalizedDx = (dx / magnitude) * speed;
            double normalizedDy = (dy / magnitude) * speed;

            robotPositions[i][0] += normalizedDx;
            robotPositions[i][1] += normalizedDy;

            robotPositions[i][0] = Math.max(0, Math.min(WIDTH, robotPositions[i][0]));
            robotPositions[i][1] = Math.max(0, Math.min(HEIGHT, robotPositions[i][1]));
        }
       
    }
    
    
    private void updatePheromones() {
        // Évaporation des phéromones sur toute la grille
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                pheromoneGrid[i][j] *= 0.1; // Taux d'évaporation 
            }
        }

        // Dépôt de phéromones par les robots
        for (int i = 0; i < numRobots; i++) {
            int x = (int) robotPositions[i][0];
            int y = (int) robotPositions[i][1];
            pheromoneGrid[x][y] += 1.0; // Ajouter une quantité de phéromone (peut être ajustée)
        }

        // Déplacement des robots basé sur les phéromones
        for (int i = 0; i < numRobots; i++) {
            int currentX = (int) robotPositions[i][0];
            int currentY = (int) robotPositions[i][1];
            
            // Trouver la meilleure direction en fonction des phéromones voisines
            int[] bestDirection = findBestPheromoneDirection(currentX, currentY);
            // Mettre à jour la position du robot en fonction de la meilleure direction
            robotPositions[i][0] += bestDirection[0] * speed;
            robotPositions[i][1] += bestDirection[1] * speed;

            // Assurez-vous que le robot reste dans les limites de la grille
            robotPositions[i][0] = Math.max(0, Math.min(WIDTH, robotPositions[i][0]));
            robotPositions[i][1] = Math.max(0, Math.min(HEIGHT, robotPositions[i][1]));
        }
    }

    private int[] findBestPheromoneDirection(int x, int y) {
        
        int[][] possibleMoves = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };
        return possibleMoves[rand.nextInt(possibleMoves.length)];
    }
    private int[] findNearestRechargeBase(int currentX, int currentY) {
		int[] nearestBase = new int[2];
		double minDistance = Double.MAX_VALUE;

		for (int[] base : rechargeBases) {
			double distance = Math.sqrt(Math.pow(base[0] - currentX, 2) + Math.pow(base[1] - currentY, 2));
			if (distance < minDistance) {
				minDistance = distance;
				nearestBase = base;
			}
		}
		return nearestBase;
	}

  

    private int[] findNearestFire(int currentX, int currentY) {
        int[] nearestFire = new int[2];
        double minDistance = Double.MAX_VALUE;

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (fireGrid[x][y]) {
                    double distance = Math.sqrt(Math.pow(x - currentX, 2) + Math.pow(y - currentY, 2));
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestFire[0] = x;
                        nearestFire[1] = y;
                    }
                }
            }
        }
        return nearestFire;
    }


    //-------------------------------------------Propagation du feu------------------------------------------------------------------//

    void propagateFire() {
        boolean[][] newFireGrid = new boolean[WIDTH][HEIGHT];
        int[][] nearbyRobots = new int[WIDTH][HEIGHT]; // Track the number of nearby robots
       
        for (int i = 0; i < numRobots; i++) {
            int x = (int) robotPositions[i][0];
            int y = (int) robotPositions[i][1];

            for (int dx = length*(-1); dx <= length; dx++) {
                for (int dy = length*(-1); dy <= length; dy++) {
                    int newX = x + dx;
                    int newY = y + dy;
                    if (newX >= 0 && newX < WIDTH && newY >= 0 && newY < HEIGHT) {
                        nearbyRobots[newX][newY]++;
                    }
                }
            }
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (fireGrid[i][j]) {
                    if (nearbyRobots[i][j] >= resistanceCount) {
                    	int nb = nearbyRobots[i][j]; 
                        // Fire is extinguished if there are 5 or more robots nearby
                        fireGrid[i][j] = false;
                        
                        extinguishedFires++;
                        System.out.println("nb fire"+extinguishedFires);
                        break;
                    } else {
                    	//System.out.println("propagate");
                        // Fire propagates randomly based on probability
                        for (int dx = -1; dx <= 1; dx++) {
                            for (int dy = -1; dy <= 1; dy++) {
                                int newX = i + dx;
                                int newY = j + dy;
                                if (newX >= 0 && newX < WIDTH && newY >= 0 && newY < HEIGHT) {
                                    //double probability = 0.2;
                                    if (!fireGrid[newX][newY] && Math.random() < propagationProbability) {
                                        newFireGrid[newX][newY] = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        fireGrid = newFireGrid;
    }
    
    

    //-------------------------------------------Update de l'inteface ------------------------------------------------------------------//

    public void paint(Graphics g) {
		super.paint(g);

		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				if (fireGrid[i][j]) {
					g.setColor(Color.RED);
					g.fillRect(i, j, 10, 10);
				} else {
					g.setColor(Color.WHITE);
					g.fillRect(i, j, 1, 1);
				}
			}
		}

		g.setColor(Color.BLUE);
		for (double[] position : robotPositions) {
			g.fillOval((int) position[0], (int) position[1], 10, 10);
		}
		
		  g.setColor(Color.GREEN);
		    for (int i = 0; i < WIDTH; i++) {
		        for (int j = 0; j < HEIGHT; j++) {
		            // Dessiner un petit cercle pour chaque valeur de phéromone non nulle
		            if (pheromoneGrid[i][j] > 0) {
		                int radius = 1; // Taille du cercle représentant la phéromone (à ajuster si nécessaire)
		                g.fillOval(i - radius / 2, j - radius / 2, radius, radius);
		            }
		        }
		    }


		g.setColor(Color.BLACK);
		for (int[] base : rechargeBases) {
			g.fillRect(base[0], base[1], 20, 20);
		}
	}

    public static void main(String[] args) {
    	 InitialSetupWindow setupWindow = new InitialSetupWindow();
    }
	public double extinguishedFiresPercentage() {
		// TODO Auto-generated method stub
		return 0;
	}
}

