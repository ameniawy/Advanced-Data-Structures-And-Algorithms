
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.Random;
import java.util.TreeSet;

public class AssignmentA {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int q = sc.nextInt();

        int latestNth = Integer.MIN_VALUE;

        SkipList sl = new SkipList(q);

        while (q-- > 0) {
            String op = sc.next();

            if (op.equals("add")) {

                int y = (latestNth != Integer.MIN_VALUE) ? latestNth : 0;
                int v = sc.nextInt() ^ y;
                sl.insert(v);
            } else if (op.equals("nth")) {
                latestNth = sl.nth(sc.nextInt());
                if (latestNth == Integer.MIN_VALUE)
                    out.println("null");
                else
                    out.println(latestNth);

            } else if (op.equals("remove")) {
                sl.remove(sc.nextInt());
            }

        }
        out.flush();
        out.close();
    }

    static class Node {
        int value;
        int skips;
        int skipTemp;
        Node right;
        Node down;
        Node left;

        public Node(int value, int skips, Node right, Node left, Node down) {
            this.value = value;
            this.skips = skips;
            this.right = right;
            this.down = down;
            this.left = left;
        }
    }

    static class SkipList {
        int levels;
        Node head;

        public SkipList(int n) {
            this.levels = (int) (Math.log(n) / Math.log(2));
            head = new Node(Integer.MIN_VALUE, 0, null, null, null);
            Node currentNode = head;
            for (int i = 0; i < this.levels; i++) {
                currentNode.down = new Node(Integer.MIN_VALUE, 0, null, null, null);
                currentNode = currentNode.down;
            }
        }

        boolean search(int value) {
            return search(this.head, value);
        }

        private boolean search(Node currentNode, int value) {
            while (currentNode.value != value && currentNode.right != null && value >= currentNode.right.value)
                currentNode = currentNode.right;

            if (currentNode.value == value)
                return true;

            if (currentNode.down == null)
                return false;

            return search(currentNode.down, value);
        }

        int nth(int index) {
            return nth(this.head, index);
        }

        private int nth(Node currentNode, int indexRemaining) {
            while (currentNode.right != null && indexRemaining - currentNode.skips >= 0) {
                indexRemaining -= currentNode.skips;
                currentNode = currentNode.right;
            }

            if (indexRemaining == 0)
                return currentNode.value;

            if (currentNode.down == null)
                return Integer.MIN_VALUE;

            return nth(currentNode.down, indexRemaining);
        }

        void insert(int value) {
            if (!search(value))
                insert(head, value);
        }

        private Node insert(Node at, int value) {

            Node currentNode = at;
            int nodesSkipped = 0;

            while (currentNode.right != null && value >= currentNode.right.value) {
                nodesSkipped += currentNode.skips;
                currentNode = currentNode.right;
            }

            if (currentNode.down != null) {
                Node res = insert(currentNode.down, value);
                // means that recursive call sent a node up
                if (res != null) {

                    int skips = (currentNode.right != null) ? currentNode.skips - (res.skipTemp) : 0;

                    Node newNode = new Node(value, skips, currentNode.right, currentNode, res);
                    if (currentNode.right != null)
                        currentNode.right.left = newNode;
                    currentNode.right = newNode;

                    currentNode.skips = res.skipTemp + 1;

                    newNode.skipTemp = nodesSkipped + res.skipTemp;

                    boolean coinFlip = flip();

                    if (coinFlip)
                        return newNode;
                    else
                        return null;

                } else {
                    if (currentNode.right != null)
                        currentNode.skips += 1;
                }
            } else {
                int skips = (currentNode.right != null) ? 1 : 0;
                Node newNode = new Node(value, skips, currentNode.right, currentNode, null);
                if (currentNode.right != null)
                    currentNode.right.left = newNode;

                currentNode.right = newNode;
                currentNode.skips = 1;

                newNode.skipTemp = nodesSkipped;
                boolean coinFlip = flip();
                if (coinFlip)
                    return newNode;
                else
                    return null;

            }

            return null;

        }

        void remove(int value) {
            if (search(value))
                remove(this.head, value);
        }

        private void remove(Node at, int value) {

            if (at == null)
                return;

            Node currentNode = at;

            while (currentNode.right != null && value >= currentNode.right.value) {
                currentNode = currentNode.right;
            }

            if (currentNode.right != null)
                currentNode.skips--;

            if (currentNode.value == value) {
                currentNode.left.skips += currentNode.skips;
                currentNode.left.right = currentNode.right;
                if (currentNode.right != null)
                    currentNode.right.left = currentNode.left;
            }

            remove(currentNode.down, value);
        }

        boolean flip() {
            return Math.random() > 0.5;
        }

        void display() {
            Node curr = this.head;
            while (curr != null) {
                display(curr);
                curr = curr.down;
            }
            System.out.println();
        }

        void display(Node node) {
            while (node != null) {
                System.out.print("(" + node.value + ", S:" + node.skips + "), ");
                node = node.right;
            }
            System.out.println();
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
