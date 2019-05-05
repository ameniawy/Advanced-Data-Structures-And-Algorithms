import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class AssignmentA {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        String s = sc.next();
        String t = sc.next();

        char s_rev_char[] = new StringBuilder(s).reverse().toString().toCharArray();

        char s_char[] = s.toCharArray();
        char t_char[] = t.toCharArray();
        int f[] = failureFunction(t_char);

        // int counter = 0;
        boolean found = false;
        int steps = 0;

        for (int i = 0; i < s.length() && s.length() == t.length() && !found; i++) {
            // i for different starting points
            int pattern_index = 0;
            for (int j = i; i < s.length() + i; j++) {
                char current = 'x';
                if (j >= s.length()) {
                    // use the reverse pointer
                    current = s_rev_char[s.length() - i + (j % s.length())];
                } else {
                    // use index j normally
                    current = s_char[j];
                }

                // do check here
                if (current != t_char[pattern_index]) {
                    // System.out.println(i);
                    i = i + f[pattern_index + 1];
                    break;
                }

                pattern_index++;
                if (pattern_index == s.length()) {
                    found = true;
                    steps = i;
                    break;
                }
            }
        }

        if (found) {
            out.println("Yes");
            out.println(steps);
        } else {
            out.println("No");
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
