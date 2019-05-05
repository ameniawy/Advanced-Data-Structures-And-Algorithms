import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class A {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        Point p1 = new Point();
        p1.x = sc.nextInt();
        p1.y = sc.nextInt();

        Point p2 = new Point();
        p2.x = sc.nextInt();
        p2.y = sc.nextInt();

        Point p3 = new Point();
        p3.x = sc.nextInt();
        p3.y = sc.nextInt();

        Point p4 = new Point();
        p4.x = sc.nextInt();
        p4.y = sc.nextInt();


        double d1 = distance(p1, p2);
        double d2 = distance(p3, p4);


        Vector v1 = new Vector(p2.x - p1.x, p2.y - p1.y);
        Vector v2 = new Vector(p4.x - p3.x, p4.y - p3.y);

        Vector sum = new Vector(v1.x + v2.x, v1.y + v2.y);



        double dot = v1.x * v2.x + v1.y * v2.y;

        double cross = v1.x * v2.y - v1.y * v2.x;

        DecimalFormat six_places = new DecimalFormat("#0.000000000");


        out.println(d1 + " " + d2);
        out.println(sum.x + " " + sum.y);
        out.println(six_places.format(dot) + " " + six_places.format(cross));
        out.println(Math.abs(cross / 2.0));

        out.flush();
        out.close();
    }

    static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    static class Vector {

        double x, y;

        public Vector() {

        }

        public Vector(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Point {
        int x, y;

        public Point() {

        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
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
