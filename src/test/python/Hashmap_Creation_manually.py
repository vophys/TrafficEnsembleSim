import numpy as np


def calculate_minimum_distance(v_0=27.77, tau=1.0, a=7.5, min_gap=2.5):
    # This function calculates the minimum distance between vehicles
    # Parameters:
    # - v_0: desired velocity
    # - tau: time headway
    # - a: acceleration
    # - min_gap: minimum gap
    # Returns:
    # - s_min: minimum distance between vehicles

    s_min = v_0 * tau + v_0 * v_0 / (2 * a)

    if s_min < min_gap:
        s_min = min_gap

    return s_min


def calculate_velocity(random_method='same', v_orientation=6.0, v_min=0.0, v_max=27.77):
    # Gives a random velocity to the vehicle based on the method
    # Parameters:
    # - random_method: method to generate the random velocity, choices: 'same', 'random', 'gaussian'
    # - v_orientation: orientation velocity for gaussian method
    # - v_min: minimum velocity
    # - v_max: maximum velocity
    # Returns:
    # - velocity: velocity of the vehicle

    if random_method == 'same':
        velocity = v_orientation
    elif random_method == 'random':
        velocity = np.random.uniform(v_min, v_max)
    elif random_method == 'gaussian':
        velocity = np.random.normal(v_orientation, 2.0)

        if velocity < v_min:
            velocity = v_min
        elif velocity > v_max:
            velocity = v_max

    return velocity


def density_check(density_field, density, tolerance):
    if np.mean(density_field) < density - tolerance:
        return True
    else:
        return False


def place_vehicle():
    return None


def update_fields_and_dict(
        density_field, safety_field, vehicle_dict, vehicle_type, vehicle_position, vehicle_speed=6.0, tau=1.0, sigma=0
):
    # This function updates the fields and the dictionary of the vehicles
    # Parameters:
    # - df: space to update
    # - safety_df: space incl. safety space to update
    # - vehicle_dict: dictionary of the vehicles
    # - vehicle_type: type of the vehicle
    # - vehicle_position: position of the vehicle
    # - vehicle_speed: speed of the vehicle
    # - tau: reaction time of the driver
    # - sigma: craziness of the driver
    # Returns:
    # - sdf: updated space
    # - safety_sdf: updated space incl. safety space
    # - vehicle_dict: updated dictionary of the vehicles

    return density_field, safety_sdf, vehicle_dict


def split_available_space(global_sdf_list, sdf, vehicle_types_df, speed=6.0):
    # This function splits the available space in the global_sdf_list and adds the new space to the list
    # Parameters:
    # - global_sdf_list: list of the global spaces
    # - sdf: space to split
    tmp = global_sdf_list.copy()

    # The sdf consists of zeros and ones.
    # The zeros are cut in two parts by the ones, so the zeros represent the new spaces.
    first_one_idx = np.where(sdf == 1)[0][0]
    last_one_idx = np.where(sdf == 1)[0][-1]

    if first_one_idx != 0:
        sub_sdf_one = sdf[:first_one_idx]
        if check_which_vehicle_fits(sub_sdf_one, vehicle_types_df, speed):
            tmp.append(sub_sdf_one)
    if last_one_idx != len(sdf) - 1:
        sub_sdf_two = sdf[last_one_idx + 1:]
        if check_which_vehicle_fits(sub_sdf_two, vehicle_types_df, speed):
            tmp.append(sub_sdf_two)

    return tmp


def check_which_vehicle_fits(sdf, vehicle_types_df, speed=6.0):
    # This function checks which vehicle type fit in the available space for a given speed
    sdf_length = len(sdf) * 0.1

    fitting_vehicles = []

    for vehicle_type in vehicle_types_df.iterrows():
        vehicle_length = vehicle_type[1]['length']
        if vehicle_type[1]['max_speed'] < speed:
            vehicle_speed = vehicle_type[1]['max_speed']
        else:
            vehicle_speed = speed

        if vehicle_length < sdf_length:
            safety_distance = calculate_minimum_distance(
                v_0=vehicle_speed,
                tau=vehicle_type[1]['tau'],
                a=vehicle_type[1]['a']
            )

            if sdf_length > safety_distance + vehicle_length:
                fitting_vehicles.append(vehicle_type[1]['vehicle_type'])

    return fitting_vehicles
