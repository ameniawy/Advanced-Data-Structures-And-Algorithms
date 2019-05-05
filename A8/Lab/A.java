import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class A {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        String s = sc.next();

        Character chars[] = new Character[s.length() << 1 | 1];

        int i = 0;
        while (i < s.length())
            chars[i] = s.charAt(i++);

        chars[i++] = '*';

        for (int z = s.length() - 1; z >= 0; z--)
            chars[i++] = s.charAt(z);

        int f[] = failureFunction(chars);


        // for (int val : f)
        // System.out.print(val + " ");
        // System.out.println();

        // for (char c : chars)
        // System.out.print(c + " ");
        // System.out.println();

        int half = f.length >> 1;
        for (int idx = f.length - 1; idx > half + 1; idx--) {
            if (f[idx] < half) {
                out.println(f[idx]);
                break;
            }
        }

        if (s.length() <= 1)
            out.println(1);



        out.flush();
        out.close();
    }

    public static int[] failureFunction(Character[] P) {
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
