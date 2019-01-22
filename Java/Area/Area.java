import java.io.File;
import java.util.Scanner;
import java.util.Objects;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/**
*   File: Area.java
*   Date: 12/12/18
*   @author Mike Cioce
*   purpose: This program accepts a set of coordinates and calculates the
*       largest "area" with respect to the number of other points that are
*       closest to it via the Manhatten distance d(a,b) = |ax - bx| + |ay - by|
*/

public class Area {

    interface PointDef {
        int [] getPoint();
    }

    private class Point implements Comparable<Point> {

        int x;
        int y;
        String pointLabel;
        Array<Point> closestNamedPoints;
        PointDef getPoint;
        int area;
        int index;

        protected Point(int x, int y) {
            this.x = x;
            this.y = y;
            this.pointLabel = "no label";
            this.closestNamedPoints = null;
            getPoint = () -> {
                int [] a = {this.x,this.y};
                return a;
            };
            this.area = 0;
            this.index = -1;
        }

        protected void setPointIndex(int i) {
            index = i;
        }

        protected int getPointIndex() {
            return index;
        }

        protected void incrementArea() {
            area++;
        }

        protected PointDef returnDef() {
            return getPoint;
        }

        protected void setPointLabel(String label) {
            pointLabel = label;
        }

        protected void setClosestPoints(Array<Point> cp) {
            closestNamedPoints = cp;
        }

        protected Array<Point> getClosestPoints() {
            return closestNamedPoints;
        }

        protected int getArea() {
            return area;
        }

        @Override
        public String toString() {
            String closestPoints = "Closest Points: ";
            if (!Objects.isNull(closestNamedPoints))
                for (Point p: closestNamedPoints) {
                        closestPoints = closestPoints.concat(p.toString() + "&&");
                }

            String point = String.format("(%d,%d) -- %s||%s\nArea: %d, Index: %d",x,y,pointLabel,closestPoints,area,index);
            return point;
        }

        @Override
        public int compareTo(Point b) {

            if (Objects.isNull(b))
                throw new NullPointerException();
            try {
                PointDef bDef = b.returnDef();
            } catch (Exception e) {
                throw new ClassCastException();
            }
            int result = this.x - b.x;
            if (result < 0) {
                return -1;
            }
            if (result == 0) {
                result = this.y - b.y;
                if (result < 0) {
                    return -1;
                }
                if (result == 0) {
                    return 0;
                } else { //if result > 0;
                    return 1;
                }
            } else { // if result > 0
                return 1;
            }
        }
    }

    private Array<Point> namedPoints;
    private Point[][] coordinatePlane;
    private int maxX, maxY;
    private int N,M;

    public Area(String fileName) {
        Scanner csv = null;
        boolean zero = false;
        try {
            csv = new Scanner(new File(fileName));
            namedPoints = new Array<Point>(8);
            maxX = 0;
            maxY = 0;
            zero = checkForZero(csv);
            csv.close();
            csv = new Scanner(new File(fileName));
            buildCSV(csv,namedPoints,zero);
            if (maxX > maxY){
                N = maxX + 1;
                M = N;
            } else {
                N = maxY + 1;
                M = N;
            }
            // N = maxX + 1;
            // M = maxY + 1;
            coordinatePlane = new Point[N][M];
            //System.out.println("built csv");
            plotNamedPoints(namedPoints,coordinatePlane);
            //System.out.println("made it");
            calculateDistances(coordinatePlane,namedPoints,N,M);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private boolean checkForZero(Scanner csv) {
        csv.useDelimiter("[,\\s]+");
        int x = 0;
        int y = 0;
        try{
            while(csv.hasNextLine()){
                x = csv.nextInt();
                y = csv.nextInt();
                if (x == 0 || y == 0)
                    return true;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return false;
    }

    private void buildCSV(Scanner csv, Array<Point> np, boolean zero) {

        csv.useDelimiter("[,\\s]+");
        int x = 0;
        int y = 0;
        Point xy = null;
        int i = 0;
        try{
            while(csv.hasNextLine()) {
                x = csv.nextInt();
                y = csv.nextInt();
                // if (zero) {
                //     x++;
                //     y++;
                // }
                if (x > maxX)
                    maxX = x;
                if (y > maxY)
                    maxY = y;
                xy = new Point(x,y);
                xy.setPointLabel("point " + i);
                xy.setPointIndex(i++);
                //System.out.println(xy.toString());
                np.add(xy);
                //sort(np,i++);
            }
            // i = 0;
            // for (Point p: np) {
            //     p.setPointLabel("Point " + i);
            //     p.setPointIndex(i++);
            // }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void sort(Array<Point> np, int i) {
        if (i > 0) {
            Point a = np.getElement(i);
            Point b = np.getElement(i-1);
            int comparison = a.compareTo(b);
            if (comparison < 0) {
                np.exchange(a,b,i,i-1);
                sort(np,i-1);
            } else { //if comparison >= 0
                return;
            }
        }
        return;
    }

    public void enumerate() {
        // String dirPath = new String(System.getProperty("user.dir") + "/output.txt");
        // String nextLine = null;
        // try{
        //     BufferedWriter writer = new BufferedWriter(new FileWriter(dirPath));
        //     for (Point[] o: coordinatePlane)
        //         for (Point p: o){
        //             nextLine = p.toString();
        //             writer.write(nextLine,0,nextLine.length());
        //             writer.newLine();
        //         }
        //     writer.close();
        // } catch (Exception e) {
        //     System.out.println(e.toString());
        // }
        System.out.println("--------------------------");
        for (Point element: namedPoints) {
            System.out.println(element.toString());
        }
        System.out.println("coordinate plane: " + (N) + " by " + (M));
        System.out.println("--------------------------");

    }

    private void plotNamedPoints(Array<Point> np, Point[][] cp) {
        for (Point p: np) {
            PointDef getPoint = p.returnDef();
            int [] xy = getPoint.getPoint();
            int n = xy[0];
            int m = xy[1];
            cp[n][m] = p;
            System.out.println(n + "," + m + ":" + cp[n][m].toString());
        }
        // for (int i = 0; i < np.numberOfElements(); i++) {
        //     Point p = np.getElement(i);
        //     PointDef getPoint = p.returnDef();
        //     int [] xy = getPoint.getPoint();
        //     int n = xy[0];
        //     int m = xy[1];
        //     cp[n][m] = p;
        //     System.out.println(n + "," + m + ":" + cp[n][m].toString());
        // }
    }

    private void calculateDistances(Point[][] coordPlane, Array<Point> np,int N,int M) {
        int distance = 0;
        for (int n = 0; n < N; n++) {
            for (int m = 0; m < M; m++) {
                if (Objects.isNull(coordPlane[n][m])) {
                    Array<Point> cnp = new Array<Point>(2);
                    coordPlane[n][m] = new Point(n,m);
                    int closest = (N - 1) + (M - 1);
                    for (Point p: np) {
                        //System.out.println("Comparing " + coordPlane[n][m].toString() + " to " + p.toString());
                        distance = manhattenDistance(coordPlane[n][m], p);
                        //System.out.println("closest: " + closest + ":: distance: " + distance);
                        if (distance == closest) {
                            //System.out.println("adding " + p.toString());
                            cnp.add(p);
                        }
                        if (distance < closest){
                            closest = distance;
                            //System.out.println("cnp - " + cnp.numberOfElements() + " elements");
                            int num = cnp.numberOfElements();
                            for (int i = 0; i < num; i++) {
                                //System.out.println("removing");
                                cnp.remove();
                            }
                            //System.out.println("adding " + p.toString());
                            cnp.add(p);
                        }
                    }
                    coordPlane[n][m].setClosestPoints(cnp);
                    if (cnp.numberOfElements() == 1)
                        calculateArea(cnp);

                }
            }
        }
            for (int n = 0; n < N; n++) {
                for (int m = 0; m < M; m++) {
                    if ((m == 0) || (m == M -1) || (n == 0) || (n == N-1)) {
                        //System.out.println("Getting next point");
                        Point next = coordPlane[n][m];
                        //System.out.println("Getting closest named points");
                        if (next.getPointIndex() == -1) {
                            Array<Point> cnp = next.getClosestPoints();
                            if (cnp.numberOfElements() == 1) {
                                for (Point p: cnp) {
                                    //System.out.println("next point is" + Objects.isNull(p));
                                    int index = p.getPointIndex();
                                    System.out.println("removing " + p.toString());
                                    np.remove(index);
                                }
                            }
                        }
                    }
                }
            }

        //removeInifinite(np);
    }

    private void calculateArea(Array<Point> closestNamedPoints) {
        for (Point p: closestNamedPoints)
            p.incrementArea();
    }

    private int manhattenDistance(Point a, Point b) {
        PointDef getA = a.returnDef();
        PointDef getB = b.returnDef();
        int [] xyA = getA.getPoint();
        int [] xyB = getB.getPoint();
        int xDist = xyA[0] - xyB[0];
        int yDist = xyA[1] - xyB[1];
        if (xDist < 0)
            xDist*=-1;
        if (yDist < 0)
            yDist*=-1;
        return (xDist + yDist);
    }

    public static void main(String [] args) throws IOException {
        Scanner kb = new Scanner(System.in);
        String fileName = null;
        System.out.println("Please enter the directory path to the csv file" +
                             " that holds your points: ");
        try {
            fileName = kb.nextLine();
            //System.out.println(fileName);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        Area area = new Area(fileName);
        area.enumerate();
        System.out.println("That's all, folks!");
    }
}
