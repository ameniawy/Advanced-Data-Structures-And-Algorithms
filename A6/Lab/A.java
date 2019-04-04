import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.Iterator;

public class A {
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int langs = sc.nextInt();
        int N = langs + 1;
        int q = sc.nextInt();
        int n = langs - 1;

        ArrayList<Integer>[] graph = new ArrayList[N];
        for (int i = 1; i < N; i++)
            graph[i] = new ArrayList<Integer>();


        while (n-- > 0) {
            int lang_1 = sc.nextInt();
            int lang_2 = sc.nextInt();

            graph[lang_1].add(lang_2);
            graph[lang_2].add(lang_1);

        }

        LCATree tree = new LCATree(graph, N);


        while (q-- > 0) {
            int first = sc.nextInt();
            int second = sc.nextInt();

            out.println(tree.query(first, second));

        }

        out.flush();
        out.close();
    }

    static class LCATree {
        ArrayList<Integer>[] graph;
        int[] level;
        int max_depth;
        int DP[][];
        int max_climb;

        public LCATree(ArrayList<Integer>[] graph, int N) {
            this.graph = graph;
            this.max_depth = N - 1;
            this.level = new int[N];
            this.max_climb = (int) Math.ceil((Math.log(max_depth) / Math.log(2)));
            this.DP = new int[max_climb + 1][N];
            build(1, 0);
        }

        private void build(int node, int parent) {
            DP[0][node] = parent;
            level[node] = level[parent] + 1;

            for (int i = 1; i <= max_climb; i++)
                DP[i][node] = DP[i - 1][DP[i - 1][node]];

            for (int next_node : graph[node])
                if (next_node != parent)
                    build(next_node, node);
        }

        public int query(int p, int q) {
            int lca = LCA(p, q);
            return (level[p] - level[lca]) + (level[q] - level[lca]);
        }

        public int LCA(int p, int q) {
            if (level[p] < level[q]) {
                p ^= q;
                q ^= p;
                p ^= q;
            }

            int k = 0;
            while (1 << k + 1 <= level[p])
                ++k;

            for (int i = k; i >= 0; --i)
                if (level[p] - (1 << i) >= level[q])
                    p = DP[i][p];

            if (p == q)
                return p;

            for (int i = k; i >= 0; --i)
                if (DP[i][p] != -1 && DP[i][p] != DP[i][q]) {
                    p = DP[i][p];
                    q = DP[i][q];
                }

            return DP[0][p];
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
