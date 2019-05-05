import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.Arrays;

public class C {

    static final double PREC = 1e-6;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        Point A = new Point(sc.nextInt(), sc.nextInt());
        Point B = new Point(sc.nextInt(), sc.nextInt());
        Point C = new Point(sc.nextInt(), sc.nextInt());
        Point D = new Point(sc.nextInt(), sc.nextInt());

        Point points[] = new Point[2];
        points[0] = B;
        points[1] = D;

        Line l1 = new Line(A, B);
        Line l2 = new Line(C, D);

        DecimalFormat six_places = new DecimalFormat("#0.000000");



        if (A.x == B.x || C.x == D.x) {
            // one of them is vertical
            if (A.x == B.x && C.x == D.x && A.x != C.x) {
                out.println("EMPTY");
            } else if (A.x == B.x && C.x == D.x && A.x == C.x) {

            }
        }

        // same slope
        if (Math.abs(l1.m - l2.m) < PREC) {
            if (Math.abs(l1.c - l2.c) < PREC) {
                // lines are on the same infinite vector
                // print 2 points
                Arrays.sort(points);
                if (Math.abs(points[0].x - points[1].x) < PREC && points[0].y > points[1].y) {
                    // if (points[0].y < points[1].y) {
                    // out.println(points[0].x + " " + points[0].y);
                    // out.println(points[1].x + " " + points[1].y);
                    // } else {
                    out.println(
                            six_places.format(points[1].x) + " " + six_places.format(points[1].y));
                    out.println(
                            six_places.format(points[0].x) + " " + six_places.format(points[0].y));
                    // }

                } else {
                    out.println(
                            six_places.format(points[0].x) + " " + six_places.format(points[0].y));
                    out.println(
                            six_places.format(points[1].x) + " " + six_places.format(points[1].y));

                }

            } else {
                // parallel lines
                out.println("EMPTY");
            }

        } else if (Double.isInfinite(l1.m)) {
            // check for 0 and INFINITY slopes
            if (Double.isInfinite(l2.m)) {
                // 2 vertical lines parallel lines
                if (B.x - C.x < PREC) {
                    Arrays.sort(points);
                    if (Math.abs(points[0].x - points[1].x) < PREC && points[0].y > points[1].y) {

                        out.println(six_places.format(points[1].x) + " "
                                + six_places.format(points[1].y));
                        out.println(six_places.format(points[0].x) + " "
                                + six_places.format(points[0].y));

                    } else {
                        out.println(six_places.format(points[0].x) + " "
                                + six_places.format(points[0].y));
                        out.println(six_places.format(points[1].x) + " "
                                + six_places.format(points[1].y));

                    }
                } else {
                    out.println("EMPTY");
                }
            } else {
                // y = mx + c
                Point res = new Point(l1.p1.x, l2.getY(l1.p1.x));
                // check if intersection is within range of segments
                if (l1.pointInSegment(res) && l2.pointInSegment(res))
                    out.println(six_places.format(res.x) + " " + six_places.format(res.y));
                else
                    out.println("EMPTY");

            }
        } else if (Double.isInfinite(l2.m)) {
            if (Double.isInfinite(l1.m)) {
                // 2 vertical lines parallel lines
                if (B.x - C.x < PREC) {
                    Arrays.sort(points);
                    if (Math.abs(points[0].x - points[1].x) < PREC && points[0].y > points[1].y) {

                        out.println(six_places.format(points[1].x) + " "
                                + six_places.format(points[1].y));
                        out.println(six_places.format(points[0].x) + " "
                                + six_places.format(points[0].y));

                    } else {
                        out.println(six_places.format(points[0].x) + " "
                                + six_places.format(points[0].y));
                        out.println(six_places.format(points[1].x) + " "
                                + six_places.format(points[1].y));

                    }
                } else {
                    out.println("EMPTY");
                }
            } else {
                // y = mx + c
                Point res = new Point(l1.p1.x, l1.getY(l1.p1.x));
                // check if intersection is within range of segments
                if (l1.pointInSegment(res) && l2.pointInSegment(res))
                    out.println(six_places.format(res.x) + " " + six_places.format(res.y));
                else
                    out.println("EMPTY");
            }

        } else {
            Point res = getIntersection(l1, l2);

            // System.out.println(l1.m + " " + l2.m);
            // check if intersection is within range of segments
            if (l1.pointInSegment(res) && l2.pointInSegment(res))
                out.println(six_places.format(res.x) + " " + six_places.format(res.y));
            else
                out.println("EMPTY");

        }

        out.flush();
        out.close();
    }

    static Point getIntersection(Line l1, Line l2) {
        double x = (l2.c - l1.c) / (l1.m - l2.m);
        double y = l1.m * x + l1.c;

        return new Point(x, y);
    }

    static class Line {
        double m;
        double c;
        Point p1;
        Point p2;

        public Line(Point p1, Point p2) {
            m = (p2.y - p1.y) / ((p2.x - p1.x) + 0.0);
            c = p1.y - m * p1.x;

            if (p1.x < p2.x) {
                this.p1 = p1;
                this.p2 = p2;
            } else {
                this.p1 = p2;
                this.p2 = p1;
            }
        }

        public boolean pointInSegment(Point p) {
            // System.out.println("HERE");
            if (p.x >= p1.x - PREC && p.x <= p2.x + PREC)
                return true;
            return false;
        }

        public double getY(double x) {
            // System.out.println(m + " " + x + " " + c);

            return m * x + c;
        }

        public String toString() {
            return "Slope: " + m + " C: " + c;
        }
    }


    static class Point implements Comparable {
        double x, y;

        public Point() {

        }

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public int compareTo(Object p) {
            return ((int) (x - ((Point) p).x));

        }
    }


    static class Scanner {

        StringTokenizer st;
        BufferedReader br;

        public Scanner(InputStream s) {
            br = new BufferedReader(new InputStreamReader(s));
        }

        public String next() throws IOException {
            while (st == null || !st.hasMoreTokens())
                st = new StringTokenizer(br.readLine());
            return st.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        public long nextLong() throws IOException {
            return Long.parseLong(next());
        }

        public String nextLine() throws IOException {
            return br.readLine();
        }

        public double nextDouble() throws IOException {
            String x = next();
            StringBuilder sb = new StringBuilder("0");
            double res = 0, f = 1;
            boolean dec = false, neg = false;
            int start = 0;
            if (x.charAt(0) == '-') {
                neg = true;
                start++;
            }
            for (int i = start; i < x.length(); i++)
                if (x.charAt(i) == '.') {
                    res = Long.parseLong(sb.toString());
                    sb = new StringBuilder("0");
                    dec = true;
                } else {
                    sb.append(x.charAt(i));
                    if (dec)
                        f *= 10;
                }
            res += Long.parseLong(sb.toString()) / f;
            return res * (neg ? -1 : 1);
        }

        public boolean ready() throws IOException {
            return br.ready();
        }

        public boolean nextEmpty() throws IOException {
            String s = nextLine();
            st = new StringTokenizer(s);
            return s.isEmpty();
        }
    }
}
