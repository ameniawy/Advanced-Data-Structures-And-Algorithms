import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.Iterator;

public class B {

    static int chain_index = 0;
    static ArrayList<Integer>[] graph;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int n = sc.nextInt();
        int N = n + 1;
        int q = sc.nextInt();

        int values[] = new int[N];

        for (int i = 1; i < N; i++) {
            values[i] = sc.nextInt();
        }

        graph = new ArrayList[N];
        for (int i = 1; i < N; i++)
            graph[i] = new ArrayList<Integer>();

        n = N - 2;
        while (n-- > 0) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            graph[u].add(v);
            graph[v].add(u);
        }

        LCATree lTree = new LCATree(graph, values, N);

        while (q-- > 0) {
            int op = sc.nextInt();
            int index = sc.nextInt();
            int value = sc.nextInt();
            if (op == 1) {
                lTree.update(index, value);
            } else {
                out.println(lTree.query(index, value));
            }
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
            return sub_query(p, lca) + sub_query(q, lca)
                    - sTree.query(HL_index_of_node[lca], HL_index_of_node[lca]);
        }

        private long sub_query(int end_node, int anc_node) {
            if (HL_head[end_node] == HL_head[anc_node]) {
                return sTree.query(HL_index_of_node[anc_node], HL_index_of_node[end_node]);
            } else {
                return sTree.query(HL_index_of_node[HL_head[end_node]], HL_index_of_node[end_node])
                        + sub_query(DP[0][HL_head[end_node]], anc_node);
            }
        }

        public void update(int node, int val) {
            sTree.update_point(HL_index_of_node[node], val);
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
                sTree[node] = sTree[node << 1] + sTree[node << 1 | 1];
            }
        }


        void update_point(int index, int val) // O(log n)
        {
            index += N - 1;
            sTree[index] = val;
            while (index > 1) {
                index >>= 1;
                sTree[index] = sTree[index << 1] + sTree[index << 1 | 1];
            }
        }


        void update_range(int i, int j, int val) // O(log n)
        {
            update_range(1, 1, N, i, j, val);
        }

        void update_range(int node, int b, int e, int i, int j, int val) {
            if (i > e || j < b)
                return;
            if (b >= i && e <= j) {
                sTree[node] += (e - b + 1) * val;
                lazy[node] += val;
            } else {
                int mid = b + e >> 1;
                propagate(node, b, mid, e);
                update_range(node << 1, b, mid, i, j, val);
                update_range(node << 1 | 1, mid + 1, e, i, j, val);
                sTree[node] = sTree[node << 1] + sTree[node << 1 | 1];
            }

        }

        void propagate(int node, int b, int mid, int e) {
            lazy[node << 1] += lazy[node];
            lazy[node << 1 | 1] += lazy[node];
            sTree[node << 1] += (mid - b + 1) * lazy[node];
            sTree[node << 1 | 1] += (e - mid) * lazy[node];
            lazy[node] = 0;
        }

        long query(int i, int j) {
            return query(1, 1, N, i, j);
        }

        long query(int node, int b, int e, int i, int j) // O(log n)
        {
            if (i > e || j < b)
                return 0;
            if (b >= i && e <= j)
                return sTree[node];
            int mid = b + e >> 1;
            propagate(node, b, mid, e);
            long q1 = query(node << 1, b, mid, i, j);
            long q2 = query(node << 1 | 1, mid + 1, e, i, j);
            return q1 + q2;

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
