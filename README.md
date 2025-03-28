Traffic Ensemble Simulations

- Idea:
  - For a given binary array for the traffic situation (+ current speed limits & incident information) create and investigate random ensembles of traffic simulations to investigate the influence of different speed limit policies in case of an incident on the road. 


- Given:
  - Binary array for the positions of vehicles produced by sensors (5 m distance)
  - Current speed limits (to create restrained policies around the previous policies)
  - Incident information (type, lane, position, …)


- Target:
  - Use the binary data to get density of segments (100-meter length, 2x2 lane elements)
  - Create randomly based input data ensembles for different policies and simulate them. Extract the binary data from that simulation and calculate the traffic flow. 
  - Compare the performances to find the best policies for best traffic flow 
  

- Workflow:
  - rou.xml (Ensemble 1, perform that x times)
    - Take the binary data of the last 10 seconds (100 steps) of the parts of the road of interest 
    - Calculate target densities (ratio 1 to 0) averaged over the steps for each segment 
    - Start with an empty road 
    - For each segment:
    - Add vehicles to an array (or dict/hashmap/…) till target density is reached 
    - Random vehicle types that fit into the remaining space in every step 
    - Create the rou.xml file with the vehicles (“Random” SUMO parameters)
  - net.xml file (Ensemble 2, perform that y times)
    - Apply different policies (speed limits) on the control segments by adjusting the speed parameters in the file for the contributing elements 
  - Additional files
    - Create the incident in a e.g. blocked lanes file
  - Create the sumocfg files
  - Perform the simulations using the Java project (in case the previous methods are not part of it)
  - Calculate the flow for each simulation
  - Evaluation


- Functions needed:
  - Read vehicle types (for their lengths)
  - Binary to target density
  - Vehicle placing to dictionary
    - Position is based on route given
    - Route determines the starting lane element for the vehicle
    - For each 100-meter segment 2 routes are possible
  - Possible vehicle types for given lengths
  - Dictionary/Array to rou.xml
  - Policy to net.xml
  - Incident to .xml files
  - Flow (Q) calculation


- To be done:
  - Convert code to Java
  - Ensemble 2 (net.xml) creation
  - Incident creation
  - Evaluation
  - Case adjustments
  - Integration into "traffic" project
  - Interface
