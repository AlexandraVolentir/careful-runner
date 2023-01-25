package utils;

import exceptions.CsvParseException;
import exceptions.InvalidDataFormat;
import models.Route;
import repos.RouteRepository;

import java.io.*;
import java.util.ArrayList;

/**
 * utility class for reading city information from a csv file
 */
public class Reader {

    /**
     * reads the data from a csv file with the buffered reader
     * @param path the path of the csv file
     */
    public static void readData(String path) {
        if(!path.endsWith(".csv")) try {
            throw new CsvParseException("Unable to parse the file type");
        } catch (CsvParseException e) {
            throw new RuntimeException(e);
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line = "";

            while((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                if((values[0].split(" ").length != 1) || values[1].split(" ").length != 1){
                    try {
                        throw new InvalidDataFormat("File path or name with spaces");
                    } catch (InvalidDataFormat e) {
                        throw new RuntimeException(e);
                    }
                }
                insertData(values[0], values[1]);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * inserts the city in the database
     * @param
     */
    private static void insertData(String path, String name) {
        RouteRepository pathRepo = new RouteRepository();
        Route city = new Route(path, name);
//        city.setIsCapital(isCapital);
        pathRepo.create(city);
    }
}
