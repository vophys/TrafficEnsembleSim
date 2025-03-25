import os

import numpy as np


def create_vehicle_id(vehicle_dict, vehicle_type):
    # This function creates a new vehicle id by checking the existing ones
    # The vehicle id is a string with the format 'vehicle_type' + 'number'
    vehicle_id = vehicle_type
    # Check the existing vehicle ids for the vehicle type and add 1 to the number
    if vehicle_id + '_0' in vehicle_dict:
        number = 1
        while vehicle_id + '_' + str(number) in vehicle_dict:
            number += 1
        vehicle_id += '_' + str(number)

    else:
        vehicle_id = vehicle_id + '_0'

    return vehicle_id


def randomly_place_vehicle(
        vehicle_types_df,
        density_target,
        segment_length,
        possible_routes,
        density_current=0.0,
        vehicle_dict=None,
        route_dict=None
):
    # This function randomly places a vehicle in the available space

    if route_dict is None:
        route_dict = {}
    if vehicle_dict is None:
        vehicle_dict = {}

    density_diff = density_target - density_current
    fitting_vehicles = check_which_vehicle_fits(segment_length, density_diff, vehicle_types_df)

    # Randomly select a vehicle type
    if fitting_vehicles and density_diff > 0:

        vehicle_type = np.random.choice(fitting_vehicles)

        # Randomly select a route
        route = np.random.choice(possible_routes)

        # Update the density field
        vehicle_length = float(vehicle_types_df.loc[vehicle_type]['length'])
        density_new = density_current + vehicle_length / segment_length / 2  # 2 lanes

        # Create the new vehicle id and update the dictionaries
        vehicle_id = create_vehicle_id(vehicle_dict, vehicle_type)

        # Update the vehicle dictionary by adding the new vehicle and its position
        vehicle_dict[vehicle_id] = vehicle_type

        # Update the route list by adding the new vehicle
        route_dict[vehicle_id] = route

        return randomly_place_vehicle(
            vehicle_types_df, density_target, segment_length, possible_routes, density_new, vehicle_dict, route_dict)

    else:
        x = 1
        print('No more vehicles can be placed')
        # print(vehicle_dict, route_dict)
        # print(density_current, density_target)
        return vehicle_dict, route_dict


def check_which_vehicle_fits(segment_length, density, vehicle_types_df):
    # This function checks which vehicle type fit in the available space for a given speed
    available_space = segment_length * density * 2  # 2 lanes

    # print(f'Available space: {available_space}')

    fitting_vehicles = []

    for vehicle_type in vehicle_types_df.iterrows():
        vehicle_length = vehicle_type[1]['length']
        vehicle_length = float(vehicle_length)

        if vehicle_length < available_space:
            fitting_vehicles.append(vehicle_type[1].name)

    if len(fitting_vehicles) == 0:
        print('No vehicles fit in the available space')

    return fitting_vehicles


def create_rou_xml(vehicle_dict, route_dict, file_template):
    # This function creates the .rou.xml file for the simulation
    # The file_template is the template for the .rou.xml file
    # This function uses the template and appends the vehicle information

    file_name_prefix = 'routes/test'
    last_string = '</routes>'
    # Check if the directory exists, if not create it
    if not os.path.exists('routes'):
        os.makedirs('routes')
    # Check if the file exists, if so add a number to the file name
    file_name = file_name_prefix + '.rou.xml'
    file_number = 0
    while os.path.exists(file_name):
        file_number += 1
        file_name = file_name_prefix + str(file_number) + '.rou.xml'

    # in file_template, all information before the vehicle information is stored.
    # The template is the beginning of the .rou.xml file and then the vehicle information is appended
    # The last_string is the last string in the template
    with open(file_template, 'r') as file:
        template = file.readlines()

    # Append the vehicle information to the template
    with open(file_name, 'w') as file:
        for line in template:
            file.write(line)
        for vehicle_id, vehicle_type in vehicle_dict.items():
            route = route_dict[vehicle_id]

            # <vehicle      id=vehicle_id
            #               type=vehicle_type
            #               route=route
            #               depart="0"
            #               departLane="best"
            #               departPos="random_free"
            #               departSpeed="random"
            #               />

            file.write(
                f'  <vehicle id="{vehicle_id}" type="{vehicle_type}" route="{route}" depart="0" departLane="best" '
                f'departPos="random_free" departSpeed="random" />\n')

        file.write(last_string)

    print(f'Created file {file_name}')
