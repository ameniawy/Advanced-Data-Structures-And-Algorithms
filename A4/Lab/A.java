import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class A {


    static int [] volumes;
    public static void main (String [] args) throws Exception {
        
        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int n = sc.nextInt();
        int f = sc.nextInt();

        volumes = new int[n];

        int maxVolume = -1;

        for(int i = 0; i < n; i++) {
            volumes[i] = sc.nextInt();
            maxVolume = Math.max(maxVolume, volumes[i]);
        }

        int hi = (maxVolume / f) + 1;

        out.println(search(0, hi, f));



        out.flush();
        out.close();
    }

    public static final double precision = 10e-9/2.0;

    public static double search(double lo, double hi, int flow) {


        double rate = -1;

        while(hi - lo > precision) {
            double mid = (lo + hi) / 2.0;
            
            double capacityToFill = flow * mid;

            if(check(capacityToFill)) {
                hi = mid;
                rate = mid;
            } else {
                lo = mid;
            }
        }

        return rate;

    }

    public static boolean check(double capacityToFill) {

        double overflow = 0;
        double current =0;
        for(int i = 0; i < volumes.length; i++) {
            current = overflow + capacityToFill;
            
            if(volumes[i] - current > precision)
                return false;

            overflow = current - volumes[i];
            
        }

        return true;

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