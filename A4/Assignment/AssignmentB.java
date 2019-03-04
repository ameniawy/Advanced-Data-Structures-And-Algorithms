import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class AssignmentB {

    public static final int mod = 1000000007;
    public static int[] cumSum;

    // public static long[] sum;

    public static int[] blocks;


    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int n = sc.nextInt();

        blocks = new int[n];
        cumSum = new int[n];

        for (int i = 0; i < n; i++) {
            blocks[i] = sc.nextInt();
        }

        cumSum[n - 1] = 1;

        int final_res = 0;


        for (int i = n - 2; i >= 0; i--) {

            int range = blocks[i] + i + 1;

            int rightSide = (range >= blocks.length) ? 0 : cumSum[range];

            int res = (((cumSum[i + 1] - rightSide) % mod) + mod) % mod;

            final_res = res;

            cumSum[i] = (((cumSum[i + 1] + res) % mod) + mod) % mod;
        }

        out.println(final_res);

        out.flush();
        out.close();
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
