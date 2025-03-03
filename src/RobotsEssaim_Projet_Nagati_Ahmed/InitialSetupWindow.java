package RobotsEssaim_Projet_Nagati_Ahmed;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;


public class InitialSetupWindow extends JFrame {
    private JTextField robotField;
    private JTextField probField;
    private JTextField fireField;
    private JTextField speedField;
    private JTextField resistanceField;
    private JTextField lengthField;
    private FireSimulation simulation;
    public List<SimulationVariables> maliste;

    public InitialSetupWindow() {
        setTitle("Simulation Setup");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(100, 100);
        maliste = new ArrayList<>();
        JPanel panel = new JPanel(new GridLayout(8, 1));

        JLabel robotLabel = new JLabel("Number of Robots:");
        robotField = new JTextField();
        panel.add(robotLabel);
        panel.add(robotField);

        JLabel probLabel = new JLabel("Propagation Probability:");
        probField = new JTextField();
        panel.add(probLabel);
        panel.add(probField);

        JLabel fireLabel = new JLabel("Number of Fires:");
        fireField = new JTextField();
        panel.add(fireLabel);
        panel.add(fireField);

        JLabel speedLabel = new JLabel("Robot speed:");
        speedField = new JTextField();
        panel.add(speedLabel);
        panel.add(speedField);
        
        JLabel lengthLabel = new JLabel("Water Shooting length:");
        lengthField = new JTextField();
        panel.add(lengthLabel);
        panel.add(lengthField);
        
        
        
        
        JLabel resistanceLabel = new JLabel("Fire Resistance:");
        resistanceField = new JTextField();
        panel.add(resistanceLabel);
        panel.add(resistanceField);
        
        
        
        
        robotField.setText("1"); // Default number of robots
        probField.setText("0.2"); // Default propagation probability
        fireField.setText("1"); // Default number of fires
        speedField.setText("20.0"); // Default robot speed
        resistanceField.setText("1"); // Default fire resistance
        lengthField.setText("0"); // Default water shooting length
        JButton startButton = new JButton("Start Simulation");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (simulation == null || !simulation.isRunning()) {
                    int robotCount = Integer.parseInt(robotField.getText());
                    double propagationProbability = Double.parseDouble(probField.getText());
                    int fireCount = Integer.parseInt(fireField.getText());
                    double speed = Double.parseDouble(speedField.getText());
                    int resistanceCount = Integer.parseInt(fireField.getText());
                    int lengthCount = Integer.parseInt(lengthField.getText());
                    startSimulation(robotCount, propagationProbability, fireCount, speed,resistanceCount,lengthCount);
                }
            }
        });
        panel.add(startButton);

        JButton stopButton = new JButton("Stop Simulation");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (simulation != null && simulation.isRunning()) {
                    simulation.stopSimulation();
                   // System.out.println("closing");
                    SimulationVariables var=  simulation.stopSimulation();
                    System.out.println("total fires= "+ var.getExtinguishedFires()+" % ");
                    maliste.add(var);
                   
                }
            }
        });
        panel.add(stopButton);
        
        JButton startGAButton = new JButton("Start Genetic Algorithm");
        startGAButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            GeneticAlgorithm ga= new  GeneticAlgorithm();
            	ga.runGeneticAlgorithmWithoutGUI(maliste) ;
            }
        });
        panel.add(startGAButton);

      

       
        add(panel);
        setVisible(true);
    }
    
    
    
    

    public void closeSimulationWindow(FireSimulation w) {
        SwingUtilities.invokeLater(() -> {
            if (w != null) {
            	//System.out.print("closing");
               SimulationVariables var=  w.stopSimulation();
               
               maliste.add(var);
          
                w.setVisible(false);
                w.dispose();
            }
        });
    }

    private void startSimulation(int robotCount, double propagationProbability, int fireCount, double speed, int resistanceCount,int length) {
        if (simulation != null && simulation.isRunning()) {
            simulation.startSimulation();
            closeSimulationWindow(simulation);
        }
        simulation = new FireSimulation(this, robotCount, propagationProbability, fireCount, speed,resistanceCount, length);
        simulation.start();

        simulation.setLocation(getX() + getWidth() + 20, getY());
    }
    
   

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InitialSetupWindow setupWindow = new InitialSetupWindow();
        });
    }
}
