import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class B {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        while (sc.ready()) {
            String s = sc.next();

            int f[] = failureFunction(s.toCharArray());

            if (f[f.length - 1] == 0)
                out.println(s);
            else {
                out.println(s.substring(0, s.length() - f[f.length - 1]));
            }
        }


        out.flush();
        out.close();
    }

    public static int[] failureFunction(char[] P) {
        int m = P.length;
        int[] f = new int[m + 1];
        int i = 0, j = -1;
        f[0] = -1;
        while (i < m) {
            while (j >= 0 && P[i] != P[j])
                j = f[j];
            i++;
            j++;
            f[i] = j;
        }
        return f;
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
