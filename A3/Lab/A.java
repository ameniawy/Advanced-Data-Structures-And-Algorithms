
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.Random;
import java.util.TreeSet;

public class A {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int q = sc.nextInt();

        SkipList sl = new SkipList(q);

        while (q-- > 0) {
            String op = sc.next();

            if (op.equals("add")) {
                sl.insert(sc.nextInt());
            } else if (op.equals("remove")) {
                sl.remove(sc.nextInt());
            } else if (op.equals("first")) {
                int res = sl.first();
                if (res == Integer.MIN_VALUE)
                    out.println("null");
                else
                    out.println(res);
            } else if (op.equals("last")) {
                int res = sl.last();
                if (res == Integer.MIN_VALUE)
                    out.println("null");
                else
                    out.println(res);
            } else if (op.equals("contains")) {
                out.println(sl.search(sc.nextInt()));
            } else if (op.equals("floor")) {
                int res = sl.floor(sc.nextInt());
                if (res == Integer.MIN_VALUE)
                    out.println("null");
                else
                    out.println(res);
            }

        }
        // testProblem();
        out.flush();
        out.close();
    }

    public static void testProblem() {
        Random rnd = new Random();
        TreeSet<Integer> treeSet = new TreeSet<Integer>();
        int maxCapacity = rnd.nextInt(1000);
        SkipList sList = new SkipList(maxCapacity);
        int count = 0;
        int q = 0;
        while (q++ < 1000) {
            double someOp = Math.random();
            if (someOp < 0.15) {
                int toAdd = rnd.nextInt(350);
                sList.insert(toAdd);
                treeSet.add(toAdd);

                System.out.println("ADDED: " + toAdd);
            } else if (someOp < 0.3) {
                int toRemove = rnd.nextInt(350);
                sList.remove(toRemove);
                treeSet.remove(toRemove);

                System.out.println("REMOVED: " + toRemove);
            } else if (someOp < 0.45) {
                try {
                    int treeFirst = treeSet.first();
                    int sListFirst = sList.first();
                    if (treeFirst != sListFirst) {
                        System.out.println("FTS FIRST" + count + "   " + treeFirst + "  " + sListFirst);
                        break;
                    } else
                        count++;
                } catch (Exception exc) {
                    int sListFirst = sList.first();
                    if (sListFirst != Integer.MIN_VALUE) {
                        System.out.println("FTS FIRST" + count);
                        break;
                    }
                }
            } else if (someOp < 0.6) {
                try {
                    int treeLast = treeSet.last();
                    int sListLast = sList.last();
                    if (treeLast != sListLast) {
                        System.out.println("FTS LAST " + count);
                        break;
                    } else
                        count++;
                } catch (Exception exc) {
                    int sListFirst = sList.last();
                    if (sListFirst != Integer.MIN_VALUE) {
                        System.out.println("FTS LAST EXCEPTION " + count);
                        break;
                    }

                }
            } else if (someOp < 0.75) {
                int cInt = rnd.nextInt(350);
                boolean treeContains = treeSet.contains(cInt);
                boolean sListContains = sList.search(cInt);
                if (treeContains != sListContains) {
                    System.out.println("FTS CON " + count);
                    break;
                } else
                    count++;
            } else if (someOp < 1) {
                int floorOf = rnd.nextInt(350);
                try {
                    int treeFloor = treeSet.floor(floorOf);
                    int sListFloor = sList.floor(floorOf);
                    if (treeFloor != sListFloor) {
                        sList.display();
                        System.out.println(treeSet.toString());
                        System.out.println("FTS FLOOR " + floorOf + "   " + treeFloor + "  " + sListFloor);
                        break;
                    } else
                        count++;
                } catch (Exception exc) {
                    int sListFloor = sList.floor(floorOf);
                    if (sListFloor != Integer.MIN_VALUE) {
                        System.out.println("FTS FLOOR EXCEPTION " + count + "  " + sListFloor);
                        break;
                    }
                }
            }
        }
    }

    static class Node {
        int value;
        Node right;
        Node down;
        Node left;

        public Node(int value, Node right, Node left, Node down) {
            this.value = value;
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
            head = new Node(Integer.MIN_VALUE, null, null, null);
            Node currentNode = head;
            for (int i = 0; i < this.levels; i++) {
                currentNode.down = new Node(Integer.MIN_VALUE, null, null, null);
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

        int floor(int value) {
            return floor(this.head, value);
        }

        private int floor(Node at, int value) {

            if (at == null)
                return Integer.MIN_VALUE;

            Node currentNode = at;

            while (currentNode.right != null && value >= currentNode.right.value)
                currentNode = currentNode.right;

            if (currentNode.down == null)
                return currentNode.value;

            return floor(currentNode.down, value);

        }

        void insert(int value) {
            if (!search(value))
                insert(head, value);
        }

        private Node insert(Node at, int value) {

            Node currentNode = at;

            while (currentNode.right != null && value >= currentNode.right.value) {
                currentNode = currentNode.right;
            }

            if (currentNode.down != null) {
                Node res = insert(currentNode.down, value);
                if (res != null) {
                    Node newNode = new Node(value, currentNode.right, currentNode, res);
                    if (currentNode.right != null)
                        currentNode.right.left = newNode;
                    currentNode.right = newNode;

                    boolean coinFlip = flip();

                    if (coinFlip)
                        return newNode;
                    else
                        return null;

                }
            } else {
                Node newNode = new Node(value, currentNode.right, currentNode, null);
                if (currentNode.right != null)
                    currentNode.right.left = newNode;
                currentNode.right = newNode;

                boolean coinFlip = flip();
                if (coinFlip)
                    return newNode;
                else
                    return null;

            }

            return null;

        }

        void remove(int value) {
            remove(this.head, value);
        }

        private void remove(Node at, int value) {

            if (at == null)
                return;

            Node currentNode = at;

            while (currentNode.right != null && value >= currentNode.right.value) {
                currentNode = currentNode.right;
            }

            if (currentNode.value == value) {
                currentNode.left.right = currentNode.right;
                if (currentNode.right != null)
                    currentNode.right.left = currentNode.left;
            }

            remove(currentNode.down, value);
        }

        int first() {
            Node currentNode = this.head;

            while (currentNode.down != null)
                currentNode = currentNode.down;

            if (currentNode.right != null)
                return currentNode.right.value;

            return currentNode.value;
        }

        int last() {
            return last(this.head);
        }

        private int last(Node at) {
            if (at == null)
                return Integer.MIN_VALUE;

            Node currentNode = at;

            while (currentNode.right != null)
                currentNode = currentNode.right;

            if (currentNode.down == null)
                return currentNode.value;

            return last(currentNode.down);
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
                System.out.print(node.value + ", ");
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