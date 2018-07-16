package ca.team4152.powerupsim.utils;

import ca.team4152.powerupsim.utils.math.Distance;
import ca.team4152.powerupsim.utils.math.VecPair;
import ca.team4152.powerupsim.utils.math.Vector2d;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
This class is a program that finds the lengths of all possible paths robots can take.
It outputs this information into the pathlengths.txt file so that the Path class can read it when the simulator starts.

NOTE: Do NOT run this program. It takes several hours to complete, and stopping it before it's done will result in an inaccurate simulation.
*/
public class PathFinder {

    private static Vector2d[] points = {
            new Vector2d(17, 65),
            new Vector2d(17, 186),
            new Vector2d(17, 253),
            new Vector2d(132, 65),
            new Vector2d(132, 186),
            new Vector2d(132, 253),
            new Vector2d(629, 19),
            new Vector2d(629, 300),
            new Vector2d(122, 161),
            new Vector2d(529, 161),
            new Vector2d(205, 95),
            new Vector2d(205, 124),
            new Vector2d(205, 148),
            new Vector2d(205, 175),
            new Vector2d(205, 201),
            new Vector2d(205, 227),
            new Vector2d(445, 95),
            new Vector2d(445, 124),
            new Vector2d(445, 148),
            new Vector2d(445, 175),
            new Vector2d(445, 201),
            new Vector2d(445, 227),
            new Vector2d(3, 125),
            new Vector2d(140, 110),
            new Vector2d(168, 84),
            new Vector2d(480, 237),
            new Vector2d(512, 210),
            new Vector2d(325, 72),
            new Vector2d(305, 161)
    };

    private static ArrayList<VecPair> pathBases = new ArrayList<>();
    private static int[] collision;
    private static int width;
    private static int height;

    static {
        BufferedImage collisionImage;

        try{
            collisionImage = ImageIO.read(PathFinder.class.getResourceAsStream("/images/collision.png"));

            width = collisionImage.getWidth();
            height = collisionImage.getHeight();
            collision = collisionImage.getRGB(0, 0, width, height, null, 0, width);

            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    int currentPoint = x + y * width;

                    if(collision[currentPoint] == 0xff000000){
                        collision[currentPoint] = 2;
                    }else if(collision[currentPoint] == 0xff808080){
                        collision[currentPoint] = 1;
                    }else{
                        collision[currentPoint] = 0;
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void run(){
        calculatePathBases();

        long startTime = System.currentTimeMillis();
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("/pathlengths.txt"));

            for(int i = 0; i < pathBases.size(); i++){
                Vector2d vec0 = pathBases.get(i).getVec0();
                Vector2d vec1 = pathBases.get(i).getVec1();
                double length = findPath(vec0, vec1);

                if(length > 0){
                    writer.write("" + vec0.getX() + ",");
                    writer.write("" + vec0.getY() + ",");
                    writer.write("" + vec1.getX() + ",");
                    writer.write("" + vec1.getY() + ",");
                    writer.write("" + length);
                    writer.newLine();
                }

                System.out.println((i + 1) + " out of " + pathBases.size() + " paths computed.");
            }
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Path calculations took " + (endTime - startTime) + "ms");
    }

    private static void calculatePathBases(){
        for(int i = 0; i < points.length; i++){
            Vector2d currentPoint = points[i];

            for(int j = 0; j < points.length; j++){
                Vector2d comparePoint = points[j];

                if(comparePoint.equals(currentPoint)) {
                    continue;
                }

                VecPair pair = new VecPair(currentPoint, comparePoint);
                VecPair reversePair = new VecPair(comparePoint, currentPoint);
                if(!pathBases.contains(pair) && !pathBases.contains(reversePair)){
                    pathBases.add(pair);
                }
            }
        }
    }

    private static double findPath(Vector2d start, Vector2d finish){
        double pathLength = 0;

        List<Node> openList = new ArrayList<>();
        List<Node> closedList = new ArrayList<>();
        Node current = new Node(start, null, 0, Distance.calculateDistance(start, finish));
        openList.add(current);

        Comparator<Node> nodeSorter = new Comparator<Node>() {
            @Override
            public int compare(Node n0, Node n1) {
                if(n1.fCost < n0.fCost){
                    return -1;
                }
                if(n1.fCost > n0.fCost){
                    return 1;
                }
                return 0;
            }
        };

        while(openList.size() > 0){
            Collections.sort(openList, nodeSorter);
            current = openList.get(0);

            if(current.location.equals(finish)){
                while (current.parent != null){
                    pathLength += Distance.calculateDistance(current.location, current.parent.location);
                    current = current.parent;
                }

                return pathLength;
            }

            openList.remove(current);
            closedList.add(current);

            for(int i = 0; i < 9; i++){
                if(i == 4){
                    continue;
                }

                int x = (int) current.location.getX();
                int y = (int) current.location.getY();
                int xi = (i % 3) - 1;
                int yi = (i / 3) - 1;

                if(x <= 0 || y <= 0){
                    continue;
                }

                int collisionTile = collision[(x + xi) + (y + yi) * width];
                if(collisionTile == 2){
                    continue;
                }

                Vector2d a = new Vector2d(x + xi, y + yi);
                double gCost = current.gCost + Distance.calculateDistance(current.location, a);
                double hCost = Distance.calculateDistance(a, finish);
                Node node = new Node(a, current, gCost, hCost);

                if(vecInList(closedList, a) && gCost >= node.gCost){
                    continue;
                }

                if(!vecInList(openList, a) && gCost < node.gCost){
                    openList.add(node);
                }
            }
        }
        return 0;
    }

    private static boolean vecInList(List<Node> list, Vector2d vector){
        for(Node n : list){
            if(n.location.equals(vector)){
                return true;
            }
        }
        return false;
    }

    private static class Node{
        public Vector2d location;
        public Node parent;
        public double fCost;
        public double gCost;
        public double hCost;

        public Node(Vector2d location, Node parent, double gCost, double hCost){
            this.location = location;
            this.parent = parent;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = this.gCost + this.hCost;
        }
    }
}