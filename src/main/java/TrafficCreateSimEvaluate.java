import it.polito.appeal.traci.SumoTraciConnection;

import java.io.File;
import java.util.HashMap;

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

public class TrafficCreateSimEvaluate {

    static String sumo_exec = "D:\\extern3\\SUMO\\bin\\sumo.exe";
    static String output_path = "output";
    static String config_path = "D:\\extern3\\Traffic_input_method\\DemoCase\\50kmMeasurement";

    static String sumo_cfg = "Linear_2000_2_represent_traffic_5_0.1sec.sumocfg";
    static String net_file = "DemoCase/Linear_2000_2_1.net.xml";

    static String default_route_file = "DemoCase/Routes2000_2_Krauss_represent_traffic_BBV_30.rou.xml";
    static String default_bottleneck_file = "DemoCase/BlockLane0_1500meter.add.xml";
    static String default_bottleneck_sub_file = "BlockedSegments_1.xml";
    static String default_induction_loop_file = "DemoCase/Linear_InductionLoops1000_2_freq_0.1_indloopDistance_5.0_STAND.add.xml";
    static String default_settings_file = "DemoCase/viewSettings.xml";

    private boolean[][][] traffic_input_matrix;

    private HashMap<String, Integer> vehicle_types = new HashMap<String, Integer>();

    private void createTrafficInputMatrix() {
        // Create the traffic input matrix by getting input values from the user/other classes

        // The matrix itself is a 3D array with the following dimensions:
        // - The first dimension is the timestep
        // - The second dimension is the segment
        // - The third dimension is the speed limit for the segment

    }

    private static void createConfigFiles(String config_path) {
        // Check if the config files are already created, if yes, skip this step
        if (config_path != null) {
            // Check if the sumocfg file is already created
            File sumocfg_file = new File(config_path + "\\" + sumo_cfg);
            if (sumocfg_file.exists()) {
                // Skip the creation of the config files
                System.out.println("The config files are already created.");
                return;

            } else {
                // Create a new .sumocfg file
                System.out.println("Creating the config files for the simulation...");
                createSumoCfgFile(config_path);
            }
        }



        // Create the config files for the simulation
        // Create the .sumocfg file
        // Create all contributing files
    }

    private static void createSumoCfgFile(String config_path) {
        // Create the files
        String net_file = createNetFile();
        String route_file = createRouteFile();

        String induction_loop_file = createInductionLoopFile();


        // Headers

        // input files

        // time parameters

        //processing parameters

        //report parameters
    }

    private static String createNetFile() {
        // Create the net-file .net.xml (contains the information about the streets)

        // Headers

        // edges (id, index, speed, length, shape, ...)

        // junctions (id, type, x, y, incLanes, intLanes, shape, ...)

        // connections (from, to, fromLane, toLane, via, dir, state, ...)

        // For now just use the existing net-file as we don't change it for the simulation
        return net_file;
    }

    private static String createRouteFile() {
        // Create the route-file .rou.xml (contains the information about the vehicles)

        // Headers

        // routes
        // vType (lcStrategic, tau, id, accel, decel, sigma, length, width, maxSpeed, vClass, carFollowModel, color, guiShape)
        // route (id, edges, ...)
        // vehicle (id, type, route, depart, departLane, departPos, departSpeed)

        // Here the position and velocity of every vehicle can be set for the simulation
        return default_route_file;

    }

    private static String createInductionLoopFile() {
        // Create the additional-file .add.xml for induction loops

        // Header (1 line)

        // induction loops (id, lane, pos, freq, file, ...)

        // For now use the default induction loop file as we don't change it for the simulation
        return default_induction_loop_file;
    }

    private static String createBottleneckFile() {
        // Create the additional-file .add.xml for bottleneck (and the additional Segments file)

        // ....add.xml
        // Header (1 line)
        // <additional><rerouter id="Block_1" edges, file="BlockedSegments.xml" />

        // BlockedSegments.xml
        // <rerouter><interval begin, end><closingLaneReroute id>
        return default_bottleneck_file;

    }

    private static String createSettingsFile() {
        // Create the gui-settings-file .xml (contains the information about the gui settings)

        // <viewsettings><viewport y, x, zoom, ...>
        // <delay value>
        return default_settings_file;

    }

    static double[][] runSimulation(String sumo_exec, String sumo_cfg, String output_path) throws Exception {

        double[][] rho_v = new double[0][]; // Initialize the array
        // Run the simulation
        SumoTraciConnection conn = new SumoTraciConnection(sumo_exec, sumo_cfg);

        // Start the simulation
        conn.runServer();

        for (int i = 0; i <= 30; i++) {
            conn.do_timestep();
            // Additional code for the simulation
            if (i % 10 == 0) {
                System.out.println("Timestep: " + i);
            }
        }

        // Stop the simulation
        conn.close();

        // Extract rho and v from the simulation

        // Return rho and v as an array

        return rho_v;
    }

    private static double calculateReward(double[][] rho_v) {
        double reward = 0.0;
        // Calculate the reward (traffic flow Q) for the simulation

        // Return the reward
        return reward;
    }

    static void createRunEvaluate() {
        // Call the functions to prepare everything for the simulation
        System.out.println("Creating the config files for the simulation...");

        createConfigFiles(config_path);

        // Run the simulation
        System.out.println("Running the simulation...");

        double[][] rho_v = new double[0][]; // Initialize the array
        try {
            String sumo_config_path = config_path + "\\" + sumo_cfg;
            rho_v = runSimulation(sumo_exec, sumo_config_path, output_path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Evaluate the simulation
        System.out.println("Evaluating the simulation...");

        // Return the reward (traffic flow Q)
        double reward = calculateReward(rho_v);
        System.out.println("The reward for the simulation is: " + reward);

    }

    public static void main(String[] args) {

        createRunEvaluate();
    }
}