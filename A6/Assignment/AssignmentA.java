import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.Collections;

public class AssignmentA {

    static ArrayList<Edge>[] adjList;
    // static ArrayList<Edge>[] MST;
    static ArrayList<Integer>[] MST_adjList;
    static int V, parent_edge_values[];
    static long mst_value;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int n = sc.nextInt();
        int N = n + 1;
        V = sc.nextInt();

        adjList = new ArrayList[N];
        // MST = new ArrayList[N];
        MST_adjList = new ArrayList[N];

        for (int i = 0; i < N; i++) {
            adjList[i] = new ArrayList<Edge>();
            // MST[i] = new ArrayList<Edge>();
            MST_adjList[i] = new ArrayList<Integer>();
        }

        Edge edges[] = new Edge[V];
        for (int i = 0; i < V; i++) {
            int from = sc.nextInt();
            int to = sc.nextInt();
            int cost = sc.nextInt();
            Edge new_edge = new Edge(from, to, cost);
            Edge new_edge_2 = new Edge(to, from, cost);
            edges[i] = new_edge;
            adjList[from].add(new_edge);
            adjList[to].add(new_edge_2);
        }

        parent_edge_values = new int[N];
        mst_value = prim();

        LCATree lcaTree = new LCATree(MST_adjList, parent_edge_values, N);

        for (Edge e : edges) {
            long res = mst_value + e.cost - lcaTree.query(e.from, e.to);
            out.println(res);
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
        int heavy_child[];
        int HL_head[];
        int HL_index_of_node[];
        int values[];
        int current_HL_index;
        int segment_tree_values[];
        SegmentTree sTree;

        public LCATree(ArrayList<Integer>[] graph, int values[], int N) {
            this.graph = graph;
            this.max_depth = N - 1;
            this.level = new int[N];
            this.max_climb = (int) Math.ceil((Math.log(max_depth) / Math.log(2)));
            this.DP = new int[max_climb + 1][N];
            this.heavy_child = new int[N];
            this.values = values;
            this.HL_head = new int[N];
            this.HL_index_of_node = new int[N];
            this.segment_tree_values = new int[N];
            this.current_HL_index = 1;
            build(1, 0);
            HL_index(1, 0, 1);

            sTree = new SegmentTree(this.segment_tree_values);
        }

        private int build(int node, int parent) {
            DP[0][node] = parent;
            level[node] = level[parent] + 1;

            for (int i = 1; i <= max_climb; i++)
                DP[i][node] = DP[i - 1][DP[i - 1][node]];

            if (graph[node].size() == 0) {
                heavy_child[node] = -1;
                return 1;
            }

            int current_heavy = -1;
            int max_child = -1;
            int sum = 1;

            for (int next_node : graph[node]) {
                if (next_node != parent) {
                    int current_child_size = build(next_node, node);
                    if (current_child_size > current_heavy) {
                        current_heavy = current_child_size;
                        max_child = next_node;
                    }
                    sum += current_child_size;
                }
            }

            heavy_child[node] = max_child;
            return sum;
        }

        private void HL_index(int node, int parent, int current_head) {
            HL_head[node] = current_head;
            HL_index_of_node[node] = current_HL_index++;
            this.segment_tree_values[HL_index_of_node[node]] = values[node];

            if (heavy_child[node] != -1) {
                HL_index(heavy_child[node], node, current_head);
                for (int next_node : graph[node]) {

                    if (next_node != heavy_child[node] && next_node != DP[0][node]) {
                        HL_index(next_node, node, next_node);
                    }

                }
            }

        }

        public long query(int p, int q) {
            int lca = LCA(p, q);
            return Math.min(sub_query(p, lca), sub_query(q, lca));
        }

        private long sub_query(int end_node, int anc_node) {
            // Node is LCA value
            if (end_node == anc_node)
                return Long.MAX_VALUE;

            if (HL_head[end_node] == HL_head[anc_node]) {
                return sTree.query(HL_index_of_node[anc_node] + 1, HL_index_of_node[end_node]);
            } else {
                return Math.min(
                        sTree.query(HL_index_of_node[HL_head[end_node]],
                                HL_index_of_node[end_node]),
                        sub_query(DP[0][HL_head[end_node]], anc_node));
            }
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

    static class SegmentTree { // 1-based DS, OOP

        int N; // the number of elements in the array as a power of 2 (i.e. after padding)
        long[] array, sTree, lazy;

        SegmentTree(int[] in) {
            int b2Length = 1;

            while (b2Length < in.length)
                b2Length = b2Length << 1;

            long[] newAry = new long[b2Length + 1];

            for (int i = 0; i < newAry.length; i++)
                newAry[i] = Long.MAX_VALUE;

            for (int i = 1; i < in.length; i++)
                newAry[i] = in[i];

            array = newAry;
            N = newAry.length - 1;
            sTree = new long[N << 1]; // no. of nodes = 2*N - 1, we add one to cross out index zero
            lazy = new long[N << 1];
            build(1, 1, N);
        }

        void build(int node, int b, int e) // O(n)
        {
            if (b == e)
                sTree[node] = array[b];
            else {
                int mid = b + e >> 1;
                build(node << 1, b, mid);
                build(node << 1 | 1, mid + 1, e);
                sTree[node] = Math.min(sTree[node << 1], sTree[node << 1 | 1]);
            }
        }

        long query(int i, int j) {
            return query(1, 1, N, i, j);
        }

        long query(int node, int b, int e, int i, int j) // O(log n)
        {
            if (i > e || j < b)
                return Long.MAX_VALUE;
            if (b >= i && e <= j)
                return sTree[node];
            int mid = b + e >> 1;
            long q1 = query(node << 1, b, mid, i, j);
            long q2 = query(node << 1 | 1, mid + 1, e, i, j);
            return Math.min(q1, q2);

        }

    }

    static long prim() // O(E log E)
    {
        long mst = 0;
        boolean[] visited = new boolean[adjList.length];
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
        pq.add(new Edge(0, 1, 0));
        while (!pq.isEmpty()) {
            Edge cur = pq.remove();
            if (visited[cur.to])
                continue;
            visited[cur.to] = true;
            // MST[cur.from].add(cur);
            MST_adjList[cur.from].add(cur.to);
            MST_adjList[cur.to].add(cur.from);
            parent_edge_values[cur.to] = cur.cost;
            mst += cur.cost;
            for (Edge nxt : adjList[cur.to])
                if (!visited[nxt.to])
                    pq.add(nxt);
        }
        return mst;
    }

    static class Edge implements Comparable<Edge> {
        int to, from, cost;

        Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }

        public int compareTo(Edge e) {
            return e.cost - cost;
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
