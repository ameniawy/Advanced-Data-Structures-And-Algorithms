import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class AssignmentA {

    static long B = 67;
    static long MOD = ((long) 10e9) + 7;
    static int D = 63;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        String s = sc.next();
        String t = sc.next();

        System.out.println(rabinKarp(s, t));


        out.flush();
        out.close();
    }

    public static int rabinKarp(String s, String t) {

        if (s.length() != t.length())
            return -1;

        char text[] = s.toCharArray();

        long s_hash = calculateHash(text);
        long t_hash = calculateHash(t.toCharArray());

        long E = 1;

        ArrayList<Character> shift = new ArrayList<>();

        // The value of h would be "pow(d, M-1)%q"
        for (int i = 0; i < s.length(); i++)
            E = (E * D) % MOD;

        long saved = s_hash;

        for (int i = 0; i < s.length(); i++) {
            if (s_hash == t_hash)
                return i; // TODO: MIGHT NEED TO CHECK MANUALLY

            System.out.println(s_hash + " " + t_hash);

            shift.add(text[i]);
            saved = long_mod(saved - long_mod(text[i] * E));
            // System.out.println(saved);
            s_hash = saved;
            for (int z = shift.size() - 1; z >= 0; z--) {
                s_hash = long_mod(s_hash * B);
                s_hash = long_mod(s_hash + shift.get(z));
            }

            // s_hash = long_mod((B * (s_hash - text[i] * E) + text[i]));


        }



        return -1;
    }

    // correctly calculates a mod b even if a < 0
    public static long long_mod(long a) {
        return (a % MOD + MOD) % MOD;
    }

    public static long calculateHash(char s[]) {
        long hp = 0;
        for (int i = s.length - 1; i >= 0; i--)
            hp = long_mod(hp * B + s[i]);
        // hp = long_mod((B * hp + s[i]));
        return hp;
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
