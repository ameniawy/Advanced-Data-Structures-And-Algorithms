import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class AssignmentB {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int q = sc.nextInt();

        while (q-- > 0) {

        }

        out.flush();
        out.close();
    }

    public static class TrieNode {
        int count;
        TrieNode left;
        TrieNode right;

        public TrieNode() {
        }

        public TrieNode(int count) {
            this.count = count;
        }

    }

    public static class BitTrie {
        TNode head;
        int bitComp;
        int bitCompp;

        public BitTrie(int numBits) {
            this.bitComp = 1 << (numBits - 1);
            this.bitCompp = this.bitComp << 1;
            this.head = new TrieNode();
        }


        public void insert(int value) {
            this.insert(value, this.bitComp, head);
        }

        private TNode insert(int value, int comparator, TNode node) {
            if (node == null)
                node = new TNode();
            node.count += 1;

            if (comparator == 0)
                return node;

            if (value >= comparator)
                node.oneChild = insert(value - comparator, comparator >> 1, node.oneChild);
            else
                node.zeroChild = insert(value, comparator >> 1, node.zeroChild);

            return node;
        }

        public long query(int value, int limit) {
            return query(value, limit, this.head.zeroChild, this.bitComp, 0)
                    + query(value, limit, this.head.oneChild, this.bitComp, 1);
        }

        private long query(int value, int limit, TNode currentNode, int comparator,
                int enteringVal) {
            if (currentNode == null || currentNode.count == 0)
                return 0;

            int xorBit = value >= comparator ? 1 : 0;
            int limitCurrentBit = limit >= comparator ? 1 : 0;
            int currentBit = xorBit ^ enteringVal;

            int nextValue = xorBit == 1 ? value - comparator : value;
            int nextLimit = limitCurrentBit == 1 ? limit - comparator : limit;

            if (limitCurrentBit == 1) {
                if (currentBit == 0)
                    return currentNode.count;
                else
                    return query(nextValue, nextLimit, currentNode.oneChild, comparator >> 1, 1)
                            + query(nextValue, nextLimit, currentNode.zeroChild, comparator >> 1,
                                    0);
            } else {
                if (currentBit == 0)
                    return query(nextValue, nextLimit, currentNode.oneChild, comparator >> 1, 1)
                            + query(nextValue, nextLimit, currentNode.zeroChild, comparator >> 1,
                                    0);
                else
                    return 0;
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
