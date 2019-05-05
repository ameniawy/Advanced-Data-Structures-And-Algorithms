import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class AssignmentA {

    static ArrayList<Integer>[] graph;
    // static int parent[];

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int V = sc.nextInt();
        int k = sc.nextInt();
        int E = V - 1;

        graph = new ArrayList[V + 1];
        for (int i = 0; i <= V; i++)
            graph[i] = new ArrayList<Integer>();

        while (E-- > 0) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            graph[u].add(v);
            graph[v].add(u);
        }

        int size_of_sub = V / k;

        // System.out.println(size_of_sub);
        int res = dfs(1, 0, size_of_sub);

        out.println((res == 0 && V % k == 0) ? "Yes" : "No");

        out.flush();
        out.close();
    }

    static int dfs(int u, int parent, int size_of_sub) {

        int size = 1;
        for (int v : graph[u]) {
            if (v != parent)
                size += dfs(v, u, size_of_sub);
        }

        return (size == size_of_sub) ? 0 : size;
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
