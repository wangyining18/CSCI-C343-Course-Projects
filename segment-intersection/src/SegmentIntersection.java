import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

class EndPoint {
	 int side;
	 Point2D point;

	 EndPoint(Point2D p, int s) {
		  point = p;
		  side = s;
	 }
}

class CustomComparator implements Comparator<EndPoint> {

	private int getInt(double x) {
		return Integer.parseInt("" + Math.round(x));
	}

	@Override
	public int compare(EndPoint o1, EndPoint o2) {
		 if (o1.point.getX() < o2.point.getX()) {
			  return -1;
		 } else if (o1.point.getX() == o2.point.getX()) {
			  if (o1.side < o2.side) {
					return -1;
			  } else if (o1.side == o2.side) {
					if (o1.point.getY() < o2.point.getY()) {
						 return -1;
					} else if (o1.point.getY() == o2.point.getY()) {
						 return 0;
					} else {
						 return 1;
					}
			  } else {
					return 1;
			  }
		 } else {
			  return 1;
		 }
	}
}

public class SegmentIntersection {

	 private static int current_x;

	private int getInt(double x) {
		return Integer.parseInt("" + Math.round(x));
	}

	private double crossProduct(Point2D p1, Point2D p2) {
		return (p1.getX() * p2.getY()) - (p2.getX() * p1.getY());
	}

	private Point2D difference(Point2D p1, Point2D p2) {
		double x1 = p2.getX() - p1.getX();
		double y1 = p2.getY() - p1.getY();
		return new Point2D.Double(x1, y1);
	}

	private double direction(Point2D p0, Point2D p1, Point2D p2) {
		return crossProduct(difference(p2, p0), difference(p1, p0));
	}

	private Boolean straddle(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
		double d1 = direction(p3, p4, p1);
		double d2 = direction(p3, p4, p2);
		return (d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0);
	}

	private Boolean onSegment(Point2D p, Point2D q, Point2D r) {
		if (q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX())
				&& q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY()))
			return true;

		return false;
	}

	private Boolean segmentsIntersect(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
		return (straddle(p1, p2, p3, p4) && straddle(p3, p4, p1, p2))
				|| (direction(p3, p4, p1) == 0 && onSegment(p3, p4, p1))
				|| (direction(p3, p4, p2) == 0 && onSegment(p3, p4, p2))
				|| (direction(p1, p2, p3) == 0 && onSegment(p1, p2, p3))
				|| (direction(p1, p2, p4) == 0 && onSegment(p1, p2, p4));
	}

	private EndPoint createEndpoint(Point2D point, Point2D otherPoint) {
		 int side;
		 if (point.getX() < otherPoint.getX()) {
			  side = 0; // Left
		 } else {
			  side = 1; // Right
		 }
		 return new EndPoint(point, side);
	}

	 static Point2D left_end(Line2D seg) {
		  if (seg.getP1().getX() <= seg.getP2().getX()) {
				return seg.getP1();
		  } else {
				return seg.getP2();
		  }
	 }

	 static Point2D right_end(Line2D seg) {
		  if (seg.getP1().getX() <= seg.getP2().getX()) {
				return seg.getP2();
		  } else {
				return seg.getP1();
		  }
	 }
	 
	 static boolean segment_less(Line2D s1, Line2D s2) {
		  Point2D left1 = left_end(s1);
		  Point2D right1 = right_end(s1);
		  Point2D left2 = left_end(s2);
		  Point2D right2 = right_end(s2);
		  // I'm cheating by using division. -Jeremy
		  double m1 = (right1.getY() - left1.getY()) / (right1.getX() - left1.getX());
		  double m2 = (right2.getY() - left2.getY()) / (right2.getX() - left2.getX());
		  double y1 = left1.getY() + m1 * (current_x - left1.getX());
		  double y2 = left2.getY() + m2 * (current_x - left2.getX());
		  return y1 < y2;
	 }

	 
	public Object[] anySegmentsIntersect(ArrayList<GUIDriver.ShapeItem> shapes, Boolean BST) {
		Object[] ans = new Object[3];
		List<EndPoint> endpoints = new ArrayList<EndPoint>();
		HashMap<EndPoint, Line2D> segmentsOf = new HashMap<EndPoint, Line2D>();

		for (Iterator iterator = shapes.iterator(); iterator.hasNext();) {
			GUIDriver.ShapeItem shape = (GUIDriver.ShapeItem) iterator.next();
			Line2D line = (Line2D) shape.getShape();
			EndPoint seg = createEndpoint(line.getP1(), line.getP2());
			segmentsOf.put(seg, line);
			endpoints.add(seg);

			seg = createEndpoint(line.getP2(), line.getP1());
			segmentsOf.put(seg, line);
			endpoints.add(seg);
		}

		BiPredicate<Line2D,Line2D> key_lt =
		    (Line2D p, Line2D q) -> segment_less(p, q);

		SearchTree<Line2D> T;
		if (BST) {
			T = new BinarySearchTree<Line2D>(key_lt);
		} else {
			T = new AVLTree<Line2D>(key_lt);
		}

		Collections.sort(endpoints, new CustomComparator());

		for (Iterator iterator = endpoints.iterator(); iterator.hasNext();) {
			EndPoint p = (EndPoint) iterator.next();
			current_x = getInt(p.point.getX());
			Line2D s = segmentsOf.get(p);

			// P is a left endpoint
			if (p.side == 0) {
				Node<Line2D> sn = T.insert(s);
				Node<Line2D> above = sn.after();
				Node<Line2D> below = sn.before();
				Point2D p1 = s.getP1();
				Point2D p2 = s.getP2();

				if (above != null) {
					Line2D above_line = (Line2D) above.getKey();
					if (above_line != null && segmentsIntersect(p1, p2, above_line.getP1(), above_line.getP2())) {
						ans[0] = true;
						ans[1] = new Line2D.Double(p1, p2);
						ans[2] = new Line2D.Double(above_line.getP1(), above_line.getP2());
						return ans; // Return True,Line2D,Line2D
					}
				}
				if (below != null) {
					Line2D below_line = (Line2D) below.getKey();
					if (below_line != null && segmentsIntersect(p1, p2, below_line.getP1(), below_line.getP2())) {
						ans[0] = true;
						ans[1] = new Line2D.Double(p1, p2);
						ans[2] = new Line2D.Double(below_line.getP1(), below_line.getP2());
						return ans; // Return True,Line2D,Line2D
					}
				}

			} else {
				Node<Line2D> sn = T.search(s);
				if (sn != null) {
					Node<Line2D> above = sn.after();
					Node<Line2D> below = sn.before();
					if (above != null && below != null) {
						Line2D above_line = (Line2D) above.getKey();
						Line2D below_line = (Line2D) below.getKey();
						if (above_line != null && below_line != null && segmentsIntersect(above_line.getP1(),
								above_line.getP2(), below_line.getP1(), below_line.getP2())) {
							ans[0] = true;
							ans[1] = new Line2D.Double(above_line.getP1(), above_line.getP2());
							ans[2] = new Line2D.Double(below_line.getP1(), below_line.getP2());
							return ans; // Return True,Line2D,Line2D
						}
						T.delete(sn.getKey());
					}
				}
			}

		}
		ans[0] = false;
		ans[1] = null;
		ans[2] = null;
		return ans; // Return False,Null,Null
	}
}
