import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class B {

    static boolean dp[][];
    static boolean dp_visited[][];
    static String lydia[];
    static int N;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int T = sc.nextInt();

        for (int i = 1; i <= T; i++) {

            N = sc.nextInt();
            lydia = sc.next().split("");
            // System.out.println("LYDIAAAA: " + lydia[0] + (lydia[0]==""));
            dp = new boolean[N][N];
            dp_visited = new boolean[N][N];
            String res = dfs(0, 0, 0, 0, 0);
            out.println("Case #" + i + ": " + res);
        }

        out.flush();
        out.close();
    }



    public static String dfs(int x, int y, int lydia_x, int lydia_y, int index) {
        if (x == N - 1 && y == N - 1)
            return "";


        int lydia_x_old = lydia_x;
        int lydia_y_old = lydia_y;
        if (lydia[index].equals("E")) {
            lydia_x++;
        } else {
            lydia_y++;
        }

        // Two options, take the valid one
        if (y + 1 < N && !(y == lydia_y_old && y + 1 == lydia_y)) {
            // go down
            if (dp_visited[x][y + 1]) {
                // visited before, check if its valid
                if (dp[x][y + 1]) {
                    return "S" + dfs(x, y + 1, lydia_x, lydia_y, index + 1);
                }
            } else {
                String res = dfs(x, y + 1, lydia_x, lydia_y, index + 1);
                dp_visited[x][y + 1] = true;
                if (res != null) {
                    dp[x][y + 1] = true;
                    return "S" + res;
                }
            }
        }
        if (x + 1 < N && !(x == lydia_x_old && x + 1 == lydia_x)) {
            // go down
            if (dp_visited[x + 1][y]) {
                // visited before, check if its valid
                if (dp[x + 1][y]) {
                    return "E" + dfs(x + 1, y, lydia_x, lydia_y, index + 1);
                }
            } else {
                String res = dfs(x + 1, y, lydia_x, lydia_y, index + 1);
                dp_visited[x + 1][y] = true;
                if (res != null) {
                    dp[x + 1][y] = true;
                    return "E" + res;
                }
            }
        }



        return null;
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
