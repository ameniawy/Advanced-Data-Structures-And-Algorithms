import java.io.*;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.HashMap;

public class B {

    static Matrix[] matrices;
    static HashMap<Long, Matrix> matrices2;
    static HashMap<Long, Long> matrices3;
    public static final long mod = 1000000007;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        long d = sc.nextLong();
        long m = sc.nextLong();
        long n = sc.nextLong();

        long f_0 = 0;
        long f_1 = 1;


        Matrix baseMatrix = new Matrix(m, -n, 1, 0);

        matrices2 = new HashMap<Long, Matrix>();
        matrices2.put(1l, baseMatrix);

        if (d >= 2) {
            Matrix leftMatrix = matrixToPower(baseMatrix, d-1);
            long res = (((leftMatrix.a * f_1) % mod) + ((leftMatrix.b * f_0) % mod)) % mod;


            out.println(res);

        } else if (d == 1) {
            out.println(f_1);
        } else if (d == 0) {
            out.println(f_0);
        }

        out.flush();
        out.close();
    }

    public static Matrix matrixToPower(Matrix currentMatrix, long power) {

        if (matrices2.containsKey(power))
            return matrices2.get(power);


        Matrix halfPowerMatrix = matrixToPower(currentMatrix, power >> 1);

        Matrix res = matrixMult(halfPowerMatrix, halfPowerMatrix);


        if (power % 2 != 0) {
            res = matrixMult(res, currentMatrix);
        }


        matrices2.put(power, res);

        return res;

    }

    public static long solveNaive(long i, long m, long n) {

        if(matrices3.containsKey(i))
            return matrices3.get(i);


        long leftSide = ((m % mod) * (solveNaive(i-1, m, n) % mod) % mod);
        long rightSide = ((n % mod) * (solveNaive(i-2, m, n) % mod) % mod);

        long res = (leftSide - rightSide);
        matrices3.put(i, res);

        return res;
    }

    public static Matrix matrixMult(Matrix A, Matrix B) {
        long a = (((((A.a % mod) * (B.a % mod)) % mod) + (((A.b % mod) * (B.c % mod)) % mod)) + mod) % mod;
        long b = (((((A.a % mod) * (B.b % mod)) % mod) + (((A.b % mod) * (B.d % mod)) % mod)) + mod) % mod;
        long c = (((((A.c % mod) * (B.a % mod)) % mod) + (((A.d % mod) * (B.c % mod)) % mod)) + mod) % mod;
        long d = (((((A.c % mod) * (B.b % mod)) % mod) + (((A.d % mod) * (B.d % mod)) % mod)) + mod) % mod;

        Matrix newMatrix = new Matrix(a, b, c, d);

        return newMatrix;
    }

    static class Matrix {
        /**
         * a b 
         * c d
         */
        long a, b, c, d;

        public Matrix(long a, long b, long c, long d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        public String toString() {
            String res = a + " " + b + "\n";
            res += c + " " + d;
            return res;
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
