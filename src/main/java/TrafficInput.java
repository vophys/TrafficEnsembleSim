import org.eclipse.sumo.libtraci.*;

/**
 * This class contains the method for creating the input data for the traffic simulation.
 * The input data contains of the following:
 * - The config files for the simulation
 *
 * For that some things are needed:
 * - The information about the street (constant for all simulations)
 * - The information about the vehicles for the next simulation creation (vehicles, density, speed, ...)
 * - The traffic input matrix contains the information about the max_speeds for the different segments and timesteps
 *
 * Files to create:
 * - .sumocfg (config file that contains the paths to the other files and the time for the simulation)
 * - net-file .net.xml (contains the information about the streets)
 * - route-file .rou.xml (contains the information about the vehicles)
 * - additional-files .add.xml (contains the information about the traffic input matrix parameters)
 * - additional-files .add.xml for induction loops and bottleneck
 * - gui-settings-file .xml (contains the information about the gui settings)
 *
 * Functions in this class:
 * - Read the vehicle/traffic information
 * - Create the traffic input matrix by getting input values from the user/other classes
 * - Read the traffic input matrix and create the additional-files .add.xml
 * - Create the .sumocfg file
 * - Create the net-file .net.xml
 * - Create the other files as they should be fixed in all simulations
 * - Perform the simulation ?
 * - Calculation of the reward (traffic flow Q) for the simulation
 */

public class TrafficInput {
    public static void main(String[] args) {
        System.loadLibrary("libtracijni");
        Simulation.start(new StringVector(new String[] {"sumo", "-n", "net.net.xml"}));
        for (int i = 0; i < 5; i++) {
            Simulation.step();
        }
        Simulation.close();
    }
}