import java.util.HashMap;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.duckdb.DuckDBConnection;

import java.sql.*;
import java.util.Arrays;

/**
 * Created by Frederic on 15.03.2025.
 * ...
 * This class is used to create a HashMap out of the binary data of the traffic simulation.
 * ...
 * Steps:
 * - Read the binary data from a duckdb file
 * - Split the data into the different segments of the roads
 * - Extract the density by averaging over the last dt time steps (dt*10 seconds)
 * - Use these information to create random vehicles
 *        - In this step handle the min distance issue to avoid disappearing vehicles
 * - Create a HashMap with the information of the vehicles (parameters and position)
 */

public class HashMapCreation {

    private static final int binariesPerSegment = 100;
    private static final int segments_to_observe = 0;
    private static final int steps_to_observe = 100;

    private static final String DUCKDB_FILE = "traffic_simulation.duckdb";

    public static byte[][][] selectx(String tableName, int laneCount, int timeFrom, int timeTo, int detectorFrom, int detectorTo, Connection conn) throws SQLException {
        int timeSteps = timeTo - timeFrom;
        int detectors = detectorTo - detectorFrom;

        String what = "";
        for (int l1 = 0; l1 < laneCount; l1++) {
            if (l1 > 0) {
                what += ", ";
            }
            what += "lane" + l1;
        }

        String sql = String.format(
                "SELECT %s FROM %s WHERE time >= %d AND time < %d AND sensor >= %d AND sensor < %d ORDER BY time ASC, sensor ASC",
                what, tableName, timeFrom, timeTo, detectorFrom, detectorTo
        );

        ResultSet rs = conn.createStatement().executeQuery(sql);
        byte[][][] res = new byte[laneCount][timeSteps][detectors];

        while (rs.next()) {
            for (int l1 = 0; l1 < laneCount; l1++) {
                int timeIndex = rs.getInt("time") - timeFrom;
                int sensor = rs.getInt("sensor") - detectorFrom;
                res[l1][timeIndex][sensor] = rs.getByte(String.format("lane%d", l1));
            }
        }

        return transpose(res);
    }

    public static byte[][] selecty(String tableName, int laneId, int timeFrom, int timeTo, int timeFuture, int detectorFrom, int detectorTo, double quantile, Connection conn) throws SQLException {
        int timeIntervals = timeTo - timeFrom;
        int detectors = detectorTo - detectorFrom;

        String sql = String.format(
                "SELECT lane%d FROM %s WHERE time < %d AND time >= %d AND sensor < %d AND sensor >= %d",
                laneId, tableName, timeTo + timeFuture, timeFrom, detectorTo, detectorFrom
        );

        ResultSet rs = conn.createStatement().executeQuery(sql);
        byte[][] res = new byte[timeIntervals][detectors];

        int[] laneData = new int[timeIntervals * detectors];
        int index = 0;
        while (rs.next()) {
            laneData[index++] = rs.getInt(String.format("lane%d", laneId));
        }

        for (int i = 0; i < timeIntervals; i++) {
            byte[] iData = Arrays.copyOfRange(laneData, i * detectors, (i + timeFuture) * detectors);
            DescriptiveStatistics stats = new DescriptiveStatistics();
            for (byte value : iData) {
                stats.addValue(value);
            }
            res[i] = new byte[detectors];
            for (int j = 0; j < detectors; j++) {
                res[i][j] = (byte) stats.getPercentile(quantile * 100);
            }
        }

        return res;
    }

    private static byte[][][] transpose(byte[][][] array) {
        int lanes = array.length;
        int timeSteps = array[0].length;
        int detectors = array[0][0].length;

        byte[][][] result = new byte[timeSteps][lanes][detectors];
        for (int l1 = 0; l1 < lanes; l1++) {
            for (int t = 0; t < timeSteps; t++) {
                System.arraycopy(array[l1][t], 0, result[t][l1], 0, detectors);
            }
        }

        return result;
    }

    private void readBinaryData() {
        // Read the binary data from a duckdb file
    }

    private void splitData() {
        // Split the data into the different segments of the roads
    }

    private void extractInformation() {
        // Extract the density and speed information for each segment
    }

    private void createRandomVehicles() {
        // Use these information to create random vehicles
        // In this step handle the min distance issue to avoid disappearing vehicles
    }

    private void createHashMap() {
        // Create a HashMap with the information of the vehicles (parameters and position)
    }

    public static void main(String[] args) {
        HashMapCreation hashMapCreation = new HashMapCreation();
        hashMapCreation.readBinaryData();
        hashMapCreation.splitData();
        hashMapCreation.extractInformation();
        hashMapCreation.createRandomVehicles();
        hashMapCreation.createHashMap();
    }
}
