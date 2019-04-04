import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.Iterator;

public class Fail {

    static ArrayList<Integer>[] graph;
    static int[] parent;
    static int[] level;
    static int[] a;
    static boolean[] visited;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int langs = sc.nextInt();
        int N = langs + 1;
        int q = sc.nextInt();
        int n = langs - 1;

        graph = new ArrayList[N];
        for (int i = 1; i < N; i++)
            graph[i] = new ArrayList<Integer>();

        while (n-- > 0) {
            int lang_1 = sc.nextInt();
            int lang_2 = sc.nextInt();

            graph[lang_1].add(lang_2);
            graph[lang_2].add(lang_1);

        }

        visited = new boolean[N];
        int height = findHeight(1);
        int block_size = (int) Math.sqrt(height);
        // System.out.println(block_size);

        visited = new boolean[N];
        parent = new int[N];
        level = new int[N];
        a = new int[N];
        preprocess(1, 0, 1, 1, 0, block_size);

        // System.out.println("LCA: " + LCA(2, 10));

        // System.out.println(a[7]);
        // System.out.println(level[9]);
        // System.out.println();

        while (q-- > 0) {
            int u = sc.nextInt();
            int v = sc.nextInt();

            // FIND LCA
            // calculate distance between u and LCA + v and LCA

            int lca = LCA(u, v);
            int res = (level[u] - level[lca]) + (level[v] - level[lca]);
            // System.out.println("U: " + u + " V: " + v);
            // System.out.println("LCA: " + lca + " RES: " + res + "\n");
            out.println(res);

        }

        out.flush();
        out.close();
    }

    public static int findHeight(int node) {

        int maxDepth = 0;
        visited[node] = true;

        Iterator<Integer> itr = graph[node].iterator();

        while (itr.hasNext()) {
            int next = itr.next();
            if (!visited[next])
                maxDepth = Math.max(maxDepth, findHeight(next));
        }

        return 1 + maxDepth;

    }


    public static void preprocess(int u, int l, int anc, int p, int block_length,
            int max_block_size) {

        parent[u] = p;
        a[u] = anc;
        level[u] = l;
        visited[u] = true;

        Iterator<Integer> itr = graph[u].iterator();

        if (block_length == max_block_size) {
            anc = u;
            block_length = 0;
        }

        while (itr.hasNext()) {
            int next = itr.next();
            if (!visited[next])
                preprocess(next, l + 1, anc, u, block_length + 1, max_block_size);
        }

    }

    public static int LCA(int u, int v) {
        if (level[v] > level[u]) {
            u ^= v;
            v ^= u;
            u ^= v;
        }

        while (a[u] != a[v]) {
            if (level[v] > level[u]) {
                u ^= v;
                v ^= u;
                u ^= v;
            }
            u = a[u];
        }

        while (level[u] != level[v]) {
            if (level[v] > level[u]) {
                u ^= v;
                v ^= u;
                u ^= v;
            }
            if (level[v] > level[u]) {
                v = parent[v];
            } else {
                u = parent[u];
            }
        }

        while (u != v) {
            v = parent[v];
            u = parent[u];
        }

        return u;
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
