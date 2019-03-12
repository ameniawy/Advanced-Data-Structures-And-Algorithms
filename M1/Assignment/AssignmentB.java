import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.HashMap;

public class AssignmentB {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int q = sc.nextInt();

        PersistentSkipList sl = new PersistentSkipList(q);

        int lastVal = Integer.MIN_VALUE;
        int version = 1;

        while (q-- > 0) {
            String op = sc.next();
            if (op.equals("add")) {
                int val = sc.nextInt();
                if (lastVal != Integer.MIN_VALUE)
                    val = val ^ lastVal;
                else
                    val = val ^ 0;

                sl.insert(val, version++);
            } else if (op.equals("floor")) {
                int v = sc.nextInt();
                long x = sc.nextLong();

                int res = sl.floor(x, v);

                lastVal = res;
                if (res == Integer.MIN_VALUE) {
                    out.println("No Questions");
                } else {
                    out.println(res);
                }
            } else if (op.equals("remove")) {
                int x = sc.nextInt();
                sl.remove(x, version++);

            }

        }
        out.flush();
        out.close();
    }

    // Returns index of x if it is present in arr[],
    // else return -1
    static int binarySearch(ArrayList<Integer> arr, int x) {
        int l = 0, r = arr.size() - 1;
        int res = -1;
        while (l <= r) {
            int m = l + (r - l) / 2;

            int val = arr.get(m);
            // Check if x is present at mid
            if (val == x)
                return val;

            // If x greater, ignore left half
            if (val < x) {
                l = m + 1;
                res = val;
            }

            // If x is smaller, ignore right half
            else {
                r = m - 1;
            }
        }
        // if we reach here, then element was
        // not present
        return res;
    }

    static class Node {
        int value;
        Node down;
        HashMap<Integer, Node> rightNodes;
        HashMap<Integer, Node> leftNodes;
        ArrayList<Integer> leftArray;
        ArrayList<Integer> rightArray;

        public Node(int value, Node right, Node left, Node down, int version) {
            this.value = value;
            this.down = down;
            leftArray = new ArrayList<Integer>();
            rightArray = new ArrayList<Integer>();
            leftNodes = new HashMap<Integer, Node>();
            rightNodes = new HashMap<Integer, Node>();
            leftArray.add(version);
            rightArray.add(version);
            leftNodes.put(version, left);
            rightNodes.put(version, right);

        }
    }

    static class PersistentSkipList {
        int levels;
        Node head;

        public PersistentSkipList(int n) {
            this.levels = (int) (Math.log(n) / Math.log(2));
            head = new Node(Integer.MIN_VALUE, null, null, null, 0);
            Node currentNode = head;
            for (int i = 0; i < this.levels; i++) {
                currentNode.down = new Node(Integer.MIN_VALUE, null, null, null, 0);
                currentNode = currentNode.down;
            }
        }

        boolean remove(int value, int version) {
            int findRes = find(value, version);
            if (findRes == Integer.MIN_VALUE)
                return false;

            remove(this.head, value, version);
            return true;

        }

        private void remove(Node at, int value, int version) {

            if (at == null)
                return;

            Node currentNode = at;

            while (true) {
                int searchRes = binarySearch(currentNode.rightArray, version);
                Node right = currentNode.rightNodes.get(searchRes);
                if (searchRes == -1 || right == null)
                    break;

                if (value < right.value)
                    break;

                currentNode = right;
            }


            if (currentNode.value == value) {

                int searchResRight = binarySearch(currentNode.rightArray, version);
                int searchResLeft = binarySearch(currentNode.leftArray, version);
                Node rightNode = (searchResRight == Integer.MIN_VALUE) ? null
                        : currentNode.rightNodes.get(searchResRight);
                Node leftNode = (searchResLeft == Integer.MIN_VALUE) ? null
                        : currentNode.leftNodes.get(searchResLeft);


                if (rightNode != null) {
                    rightNode.leftNodes.put(version, leftNode);
                    rightNode.leftArray.add(version);
                }
                leftNode.rightArray.add(version);
                leftNode.rightNodes.put(version, rightNode);

            }

            remove(currentNode.down, value, version);
        }



        int find(int value, int version) {
            return find(this.head, value, version);
        }

        private int find(Node currentNode, int value, int version) {
            while (true) {
                int searchRes = binarySearch(currentNode.rightArray, version);
                Node right = currentNode.rightNodes.get(searchRes);
                if (searchRes == -1 || right == null)
                    break;

                if (value < right.value)
                    break;

                currentNode = right;
            }

            if (currentNode.value == value)
                return value;

            if (currentNode.down == null) {
                return Integer.MIN_VALUE;

            }

            return find(currentNode.down, value, version);
        }

        int floor(long value, int version) {
            return floor(this.head, value, version);
        }

        private int floor(Node currentNode, long value, int version) {
            if (currentNode == null) {
                return Integer.MIN_VALUE;

            }

            while (true) {
                int searchRes = binarySearch(currentNode.rightArray, version);
                Node right = currentNode.rightNodes.get(searchRes);
                if (searchRes == -1 || right == null)
                    break;

                if (value >= right.value) {
                    currentNode = right;
                } else
                    break;

            }
            if (currentNode.value == value)
                return currentNode.value;


            return Math.max(floor(currentNode.down, value, version), currentNode.value);
        }

        void insert(int value, int version) {
            insert(head, value, version);
        }

        private Node insert(Node currentNode, int value, int version) {

            int newVersionNum = version;

            while (true) {
                int searchRes = binarySearch(currentNode.rightArray, version);
                Node rightNode = currentNode.rightNodes.get(searchRes);
                if (searchRes == -1 || rightNode == null || rightNode.value > value)
                    break;

                currentNode = currentNode.rightNodes.get(searchRes);
            }


            if (currentNode.down != null) {
                Node res = insert(currentNode.down, value, version);
                if (res != null) {
                    int rightSearchRes = binarySearch(currentNode.rightArray, version);
                    Node rightNode = (rightSearchRes == -1) ? null
                            : currentNode.rightNodes.get(rightSearchRes);

                    Node newNode = new Node(value, rightNode, currentNode, res, newVersionNum);


                    if (rightNode != null) {
                        rightNode.leftArray.add(newVersionNum);
                        rightNode.leftNodes.put(newVersionNum, newNode);
                    }

                    currentNode.rightArray.add(newVersionNum);
                    currentNode.rightNodes.put(newVersionNum, newNode);

                    boolean coinFlip = flip();

                    if (coinFlip)
                        return newNode;
                    else
                        return null;

                }
            } else {
                int rightSearchRes = binarySearch(currentNode.rightArray, version);
                Node rightNode =
                        (rightSearchRes == -1) ? null : currentNode.rightNodes.get(rightSearchRes);
                Node newNode = new Node(value, rightNode, currentNode, null, newVersionNum);

                if (rightNode != null) {
                    rightNode.leftArray.add(newVersionNum);
                    rightNode.leftNodes.put(newVersionNum, newNode);
                }

                currentNode.rightArray.add(newVersionNum);
                currentNode.rightNodes.put(newVersionNum, newNode);

                boolean coinFlip = flip();
                if (coinFlip)
                    return newNode;
                else
                    return null;

            }

            return null;

        }

        boolean flip() {
            return Math.random() > 0.5;
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
