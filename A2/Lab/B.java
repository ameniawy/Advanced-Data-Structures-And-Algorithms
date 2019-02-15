package Lab;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class B {

    static long [] segmentTree;
    static long [] segmentTreeUpdates;
    static int treeSize;

    // left child = i * 2
    // right child = (i * 2) + 1
    public static void main (String [] args) throws Exception {
        
        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int n = sc.nextInt();
        int q = sc.nextInt();
        int r = sc.nextInt();
        int iterations = q + r;

        treeSize = 4 * n;

        segmentTree = new long[treeSize];
        segmentTreeUpdates = new long[treeSize];


        while(iterations-- > 0) {
            int op = sc.nextInt();

            if(op == 1) {
                // mina is asking
                // insert
                int left = sc.nextInt();
                int right = sc.nextInt();
                long value = sc.nextLong() + 1;
                insertInTree2(1, left, right, 1, n, value);
                
            }
            else if(op == 2) {

                // mina is being asked
                // query
                int left = sc.nextInt();
                int right = sc.nextInt();
                long res = query2(1, left, right, 1, n);

                out.println(res);

            }

        }

        out.flush();
        out.close();
    }

    static void insertInTree2(int nodeIndex, int wantedLeft, int wantedRight, int left, int right, long updateValue) {

        // out of range
        if((right < wantedLeft) || (left > wantedRight)){
            return;
        }
        // in range
        if(left >= wantedLeft && right <= wantedRight) {

                segmentTree[nodeIndex] = (right - left + 1) * updateValue;
                segmentTreeUpdates[nodeIndex] = updateValue;
        }
        // part of the range
        else {
                
            int mid = (right + left) >> 1;
            propagate(nodeIndex, left, mid, right);
            insertInTree2(nodeIndex << 1, wantedLeft, wantedRight, left, mid, updateValue);
            insertInTree2((nodeIndex << 1) + 1, wantedLeft, wantedRight, mid + 1, right, updateValue);
            segmentTree[nodeIndex] = segmentTree[nodeIndex<<1] + segmentTree[nodeIndex<<1|1];
        
        }
        
        
    }

	static void propagate(int node, int left, int mid, int right)		
	{
        if(segmentTreeUpdates[node] != 0){
            segmentTreeUpdates[node<<1] = segmentTreeUpdates[node];
            segmentTreeUpdates[node<<1|1] = segmentTreeUpdates[node];
            segmentTree[node<<1] = (mid-left+1)*segmentTreeUpdates[node];		
            segmentTree[node<<1|1] = (right-mid)*segmentTreeUpdates[node];		
            segmentTreeUpdates[node] = 0;
        }
    }


    static long query2(int nodeIndex, int wantedLeft, int wantedRight, int left, int right) {

        // completely out of range
        if ((right < wantedLeft) || (left > wantedRight)) {
            return 0;
        }
        // I am in range or I am leaf
        if((left >= wantedLeft && right <= wantedRight)) {
            return segmentTree[nodeIndex];
        } 
        // I am partly in range and partly out of range

        int mid = (right + left) >> 1;
        propagate(nodeIndex, left, mid, right);
        
        return query2((nodeIndex << 1), wantedLeft, wantedRight, left, mid) + query2((nodeIndex << 1) + 1, wantedLeft, wantedRight, mid + 1, right);

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