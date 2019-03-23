import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.Stack;

public class AssignmentB {

    static ArrayList<Integer>[] graph;
    static int SCC, findSCC[], dfs_num[], dfs_low[];
    static Stack<Integer> s;
    static int globalTime = 1;
    static final int UNVISITED = 0;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int N = sc.nextInt() + 1;
        int V = N << 2;
        int E = sc.nextInt();

        graph = new ArrayList[V];
        for (int i = 0; i < V; i++)
            graph[i] = new ArrayList<Integer>();

        while (E-- > 0) {
            int t = sc.nextInt();
            int gate_1 = sc.nextInt();
            int gate_2 = sc.nextInt();

            // g1 == gate_1<<1
            // ~g1 == gate_1<<1|1
            if (t == 1) {
                // (~g1 -> g2)
                graph[gate_1 << 1 | 1].add(gate_2 << 1);
                // (~g2 -> g1)
                graph[gate_2 << 1 | 1].add(gate_1 << 1);
            } else if (t == 2) {
                // (g1 -> ~g2)
                graph[gate_1 << 1].add(gate_2 << 1 | 1);
                // g2 -> ~g1
                graph[gate_2 << 1].add(gate_1 << 1 | 1);
                // ~g1 -> g2
                graph[gate_1 << 1 | 1].add(gate_2 << 1);
                // ~g2 -> g1
                graph[gate_2 << 1 | 1].add(gate_1 << 1);

            } else if (t == 3) {
                // g1-> g2
                graph[gate_1 << 1].add(gate_2 << 1);
                // g2 -> g1
                graph[gate_2 << 1].add(gate_1 << 1);

            } else if (t == 4) {
                // g1 -> g2
                graph[gate_1 << 1].add(gate_2 << 1);
            } else if (t == 5) {
                // g1 -> ~g2
                graph[gate_1 << 1].add(gate_2 << 1 | 1);
                // g2 -> ~g1
                graph[gate_2 << 1].add(gate_1 << 1 | 1);
            }

        }

        dfs_num = new int[V];
        dfs_low = new int[V];
        findSCC = new int[V];
        s = new Stack<Integer>();
        SCC = 0;

        for (int i = 1; i < V; i++) {
            if (dfs_num[i] == UNVISITED) {
                tarjan(i);
            }
        }

        boolean sat = true;

        for (int i = 0; i < N; i++) {
            if (findSCC[i << 1] == findSCC[i << 1 | 1]) {
                sat = false;
                break;
            }
        }

        out.println(sat ? "YES" : "NO");

        out.flush();
        out.close();
    }

    static void tarjan(int u) {

        dfs_low[u] = dfs_num[u] = globalTime++;
        s.push(u);

        for (int v : graph[u]) {
            if (dfs_num[v] == UNVISITED) {
                tarjan(v);
            }

            if (findSCC[v] == UNVISITED) {
                dfs_low[u] = Math.min(dfs_low[u], dfs_low[v]);
            }

        }

        // root of SCC
        if (dfs_low[u] == dfs_num[u]) {
            SCC++;
            while (true) {
                int v = s.pop();
                findSCC[v] = SCC;

                if (u == v)
                    break;
            }

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
