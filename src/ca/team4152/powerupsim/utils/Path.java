package ca.team4152.powerupsim.utils;

import ca.team4152.powerupsim.utils.math.Distance;
import ca.team4152.powerupsim.utils.math.VecPair;
import ca.team4152.powerupsim.utils.math.Vector2d;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

/*
This class holds all the possible paths robots can take.
All paths and their lengths are stored in pathlengths.txt.
Paths are all loaded when the program starts so they don't need to be calculated on the fly. (which is slow)
 */
public class Path {

    private static HashMap<VecPair, Double> pathLengths = new HashMap<>();

    public static double getPathLength(Vector2d start, Vector2d finish){
        VecPair path = new VecPair(start, finish);
        VecPair reversePath = new VecPair(finish, start);
        double result = 0;

        if(pathLengths.containsKey(path)){
            result = pathLengths.get(path);
        }else if(pathLengths.containsKey(reversePath)){
            result = pathLengths.get(reversePath);
        }else{
            result = Distance.calculateDistance(start, finish);
        }

        return result;
    }

    public static void initPaths(){
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(Path.class.getResourceAsStream("/pathlengths.txt")))){
            String line;
            StringTokenizer tokenizer;

            while ((line = reader.readLine()) != null){
                double x0 = -2;
                double y0 = -2;
                double x1 = -2;
                double y1 = -2;
                double length = -2;

                tokenizer = new StringTokenizer(line, ",");
                while(tokenizer.hasMoreTokens()){
                    String token = tokenizer.nextToken();
                    if(x0 == -2){
                        x0 = Double.parseDouble(token);
                    }else if(y0 == -2){
                        y0 = Double.parseDouble(token);
                    }else if(x1 == -2){
                        x1 = Double.parseDouble(token);
                    }else if(y1 == -2){
                        y1 = Double.parseDouble(token);
                    }else if(length == -2){
                        length = Double.parseDouble(token);
                    }
                }

                pathLengths.put(new VecPair(new Vector2d(x0, y0), new Vector2d(x1, y1)), length);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}