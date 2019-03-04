import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class AssignmentA {

    static int[] speed;
    static int[] startingDistance;

    public static final double precision = 1e-6;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int n = sc.nextInt();
        int k = sc.nextInt();

        speed = new int[n];
        startingDistance = new int[n];


        for (int i = 0; i < n; i++) {
            speed[i] = sc.nextInt();
            startingDistance[i] = sc.nextInt();
        }

        // double maxTime = minSpeed * ;

        double lo = 0;
        double hi = k + 1;
        double left, right;


        double res = Double.MAX_VALUE;

        long x = (long) ((Math.log(1e5/precision) / Math.log(2 / 3.0)) + 1);
        // int x = 180;

        // System.out.println(x);

        while (x-- > 0) {

            // left = (hi + lo) / 3.0;
            // right = (hi + lo) * 2.0 / 3.0;
            right = hi - (hi - lo) / 3;
            left = lo + (hi - lo) / 3;

            double leftRes = f(left);
            double rightRes = f(right);

            if ((rightRes < leftRes)) {
                res = rightRes;
                lo = left;
            } else {
                res = leftRes;
                hi = right;
            }
            // System.out.println("\n\n");
        }
        // System.out.println("lo: " + lo + " hi: " + hi);
        // System.out.println("DIFF: " + (hi-lo));
        
        out.println(res);



        out.flush();
        out.close();
    }

    static double f(double time) {

        double first = Double.MIN_VALUE;
        double last = Double.MAX_VALUE;

        for (int i = 0; i < startingDistance.length; i++) {
            double distance = startingDistance[i] + (speed[i] * time);
            first = Math.max(first, distance);
            last = Math.min(last, distance);
        }

        // System.out.println("TIME: " + time);
        // System.out.println("FIRST: " + first + " LAST: " + last + " DIFF: " + (first - last));
        // System.out.println();
        return Math.abs(first - last);
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
