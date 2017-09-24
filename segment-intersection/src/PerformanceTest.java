import java.awt.*;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

public class PerformanceTest {

    public static void main(String[] args){
        Random rand = new Random();

        System.out.println("numberOfSegments\ttimeForBST(ms)\ttimeForAVL(ms)");
        int repeat = 20;
        for(int k = 1000; k <=100000; k+= 10000) {
            Long timeForBST=0L;
            Long timeForAVL= 0L;
            for( int times = 1; times < repeat; times ++) {
                SegmentIntersection segInt = new SegmentIntersection();
                ArrayList<GUIDriver.ShapeItem> shapes = new ArrayList<GUIDriver.ShapeItem>();

                for (int i = 0; i < k; i++) {
                    GUIDriver.ShapeItem currentShape = new GUIDriver.ShapeItem
                            (new Line2D.Double(new Point(rand.nextInt(520), rand.nextInt(600)), new Point(rand.nextInt(520), rand.nextInt(600))), Color.BLACK);

                    shapes.add(currentShape);
                }

                Long begin = System.currentTimeMillis();
                segInt.anySegmentsIntersect((ArrayList<GUIDriver.ShapeItem>) shapes, true);
                Long end = System.currentTimeMillis();

                timeForBST += end - begin;
                System.out.println((end-begin)+"ms");

                begin = System.currentTimeMillis();
                segInt.anySegmentsIntersect((ArrayList<GUIDriver.ShapeItem>) shapes, false);
                end = System.currentTimeMillis();
                System.out.println((end-begin)+"ms");
                timeForAVL += end - begin;
            }
            timeForAVL /= repeat;
            timeForBST /= repeat;
            System.out.println(k +"\t"+ timeForBST+"\t"+timeForAVL);

        }

    }
}
