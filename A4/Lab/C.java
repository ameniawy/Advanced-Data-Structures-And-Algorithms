import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class C {

    public static final long mod = 1000000007;
    public static int[] nWays;

    public static int[] blocks;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int n = sc.nextInt();

        int N = 1;
        while (N < n)
            N <<= 1; // padding

        int[] in = new int[N + 1];
        blocks = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            blocks[i] = sc.nextInt();
        }

        in[n] = 1;

        SegmentTree tree = new SegmentTree(in);


        for (int i = n - 1; i > 0; i--) {
            int current = blocks[i];

            int range = current + i;

            if (range >= blocks.length)
                range = blocks.length - 1;

            int sum = tree.query(i + 1, range);

            tree.update_point(i, sum);
        }

        out.println(tree.query(1, 1));

        out.flush();
        out.close();
    }

    // public static long dp(int index) {

    // if(index >= blocks.length)
    // return 0l;

    // if(nWays[index] != -1)
    // return nWays[index];

    // if(index == blocks.length -1)
    // return 1;

    // long sum = 0;

    // for(int i = 1; i <= blocks[index]; i++) {
    // sum = ((sum % mod) + (dp(index + i) % mod)) % mod;
    // }

    // return nWays[index] = sum;

    // }

    static class SegmentTree { // 1-based DS, OOP

        int N; // the number of elements in the array as a power of 2 (i.e. after padding)
        int[] array, sTree, lazy;
        public static final int mod = 1000000007;

        SegmentTree(int[] in) {
            array = in;
            N = in.length - 1;
            sTree = new int[N << 1]; // no. of nodes = 2*N - 1, we add one to cross out index zero
            lazy = new int[N << 1];
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
            sTree[index] += val;
            while (index > 1) {
                index >>= 1;
                sTree[index] = ((sTree[index << 1] % mod) + (sTree[index << 1 | 1] % mod)) % mod;
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

        int query(int i, int j) {
            return query(1, 1, N, i, j);
        }

        int query(int node, int b, int e, int i, int j) // O(log n)
        {
            if (i > e || j < b)
                return 0;
            if (b >= i && e <= j)
                return sTree[node];
            int mid = b + e >> 1;
            propagate(node, b, mid, e);
            int q1 = query(node << 1, b, mid, i, j);
            int q2 = query(node << 1 | 1, mid + 1, e, i, j);
            return ((q1 % mod) + (q2 % mod)) % mod;

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