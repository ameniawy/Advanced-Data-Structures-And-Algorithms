import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class AssignmentA {

    static ArrayList<PSegmentTree> versions;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int n = sc.nextInt();
        int q = sc.nextInt();


        int[] values = new int[n + 1];

        for (int i = 1; i < values.length; i++)
            values[i] = sc.nextInt();

        PSegmentTree st = new PSegmentTree(n);
        st.build(values);

        versions = new ArrayList<PSegmentTree>();
        versions.add(st);

        int previous = Integer.MAX_VALUE;

        while (q-- > 0) {
            String op = sc.next();

            if (op.equals("grade")) {
                int day = sc.nextInt();
                int left = sc.nextInt();
                int right = sc.nextInt();

                Node res = versions.get(day).query(left, right);
                previous = res.max - res.min;
                out.println(previous);
            } else if (op.equals("changed")) {
                int day = sc.nextInt();
                int index = sc.nextInt();
                int h = sc.nextInt();

                int y = (previous == Integer.MAX_VALUE) ? 0 : previous;
                int value = h ^ y;

                PSegmentTree newTree = versions.get(day).update(index, value);
                versions.add(newTree);
            }

        }

        out.flush();
        out.close();
    }

    static class PSegmentTree {
        Node root;
        int N;
        int[] array;

        public PSegmentTree(int N) {
            this.N = N;
        }

        void build(int[] array) {
            this.array = array;
            this.root = build(1, N);
        }

        private Node build(int left, int right) {
            if (left == right) {
                return new Node(null, null, array[left], array[left], array[left]);
            } else {
                int mid = left + right >> 1;
                Node leftNode = build(left, mid);
                Node rightNode = build(mid + 1, right);
                Node currentNode =
                        new Node(leftNode, rightNode, Math.max(leftNode.max, rightNode.max),
                                Math.min(leftNode.min, rightNode.min), -1);
                return currentNode;
            }
        }

        Node query(int wantedLeft, int wantedRight) {
            return query(this.root, 1, N, wantedLeft, wantedRight);
        }

        private Node query(Node node, int left, int right, int wantedLeft, int wantedRight) {
            if (wantedLeft > right || wantedRight < left || node == null)
                return new Node(null, null, Integer.MIN_VALUE, Integer.MAX_VALUE, -1);
            if (left >= wantedLeft && right <= wantedRight)
                return node;

            int mid = left + right >> 1;

            Node leftQuery = query(node.left, left, mid, wantedLeft, wantedRight);
            Node rightQuery = query(node.right, mid + 1, right, wantedLeft, wantedRight);

            return new Node(null, null, Math.max(leftQuery.max, rightQuery.max),
                    Math.min(leftQuery.min, rightQuery.min), -1);

        }

        PSegmentTree update(int index, int value) {
            Node newRoot = update(this.root, 1, N, index, value);
            PSegmentTree resTree = new PSegmentTree(N);
            resTree.root = newRoot;
            return resTree;
        }

        private Node update(Node node, int left, int right, int wantedIndex, int value) {
            if (wantedIndex < left || wantedIndex > right)
                return new Node(null, null, Integer.MIN_VALUE, Integer.MAX_VALUE, -1);

            if (left == right && left == wantedIndex) {
                Node newNode = new Node(null, null, value, value, value);
                return newNode;
            }
            int mid = left + right >> 1;

            if (wantedIndex > mid) {
                // take left of this node to be my left node
                Node updatedRight = update(node.right, mid + 1, right, wantedIndex, value);

                int min = (node.left == null) ? updatedRight.min
                        : Math.min(node.left.min, updatedRight.min);
                int max = (node.left == null) ? updatedRight.max
                        : Math.max(node.left.max, updatedRight.max);

                return new Node(node.left, updatedRight, max, min, -1);
            } else {
                // take right of this node to be my right node
                Node updatedLeft = update(node.left, left, mid, wantedIndex, value);

                int min = (node.right == null) ? updatedLeft.min
                        : Math.min(node.right.min, updatedLeft.min);
                int max = (node.right == null) ? updatedLeft.max
                        : Math.max(node.right.max, updatedLeft.max);

                return new Node(updatedLeft, node.right, max, min, -1);
            }


        }
    }

    static class Node {
        Node left;
        Node right;
        int max;
        int min;
        int value;

        public Node(Node left, Node right, int max, int min, int value) {
            this.left = left;
            this.right = right;
            this.max = max;
            this.min = min;
            this.value = value;
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
