package Assignment;
import java.io.*;
// import java.util.ArrayList;
import java.util.StringTokenizer;
// import java.util.Collections;

public class AssignmentA {

    static Node [] segmentTree;

    static int array [];
    static int treeSize;

    // left child = i * 2
    // right child = (i * 2) + 1
    public static void main (String [] args) throws Exception {
        
        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int n = sc.nextInt();
        int q = sc.nextInt();

        treeSize = 4 * (n);

        array = new int[n + 1];

        for(int i = 1; i < n + 1; i++) array[i] = sc.nextInt();

        segmentTree = new Node[treeSize];

        build(1, 1, n);

        // for(int i = 0; i < treeSize; i++) {
        //     out.println(segmentTree[i] + "" + i + " ");
        // }
        // out.println();


        while(q-- > 0) {
            int op = sc.nextInt();

            if(op == 1) {
                // set value a[i] to x
                int i = sc.nextInt();
                int x = sc.nextInt();
                array[i] = x;
                update(1, i, 1, n);

            }
            else if(op == 2) {
                // print length of longest interesting subarray
                int left = sc.nextInt();
                int right = sc.nextInt();
                int res = query(1, left, right, 1, n);
                out.println(res);
            }
        }

        out.flush();
        out.close();
    }




    static void build(int nodeIndex, int left, int right) {
        
        // I'm a leaf
        if(left == right) {
            segmentTree[nodeIndex] = new Node(1, 1, 1);
        }
        else {
            int mid = (left + right) >> 1;
            // System.out.println("(" + left + "," + mid + "," + right + ")");
            build(nodeIndex<<1, left, mid);
            build(nodeIndex<<1|1, mid+1, right);
            

            int lengthOfConcat = (array[mid] <= array[mid+1])? segmentTree[nodeIndex<<1].maxSequenceFromRight + segmentTree[nodeIndex<<1|1].maxSequenceFromLeft : -1;
            int lengthOfLeftTree = segmentTree[nodeIndex<<1].size;
            int lengthOfRightTree = segmentTree[nodeIndex<<1|1].size;

            if(lengthOfConcat >= lengthOfLeftTree && lengthOfConcat >= lengthOfRightTree) {
                // concat is bigger
                // TODO: POSSIBLE BUG HERE
                int leftSequence = segmentTree[nodeIndex<<1].maxSequenceFromLeft;
                int rightSequence = segmentTree[nodeIndex<<1|1].maxSequenceFromRight;

                // if left sequence covers whole left tree, then it can join the left from the right tree
                if(segmentTree[nodeIndex<<1].maxSequenceFromLeft == mid - left + 1) {
                    leftSequence = segmentTree[nodeIndex<<1].maxSequenceFromLeft + segmentTree[nodeIndex<<1|1].maxSequenceFromLeft;

                }

                if(segmentTree[nodeIndex<<1|1].maxSequenceFromRight == right - (mid + 1) + 1) {
                    rightSequence = segmentTree[nodeIndex<<1].maxSequenceFromRight + segmentTree[nodeIndex<<1|1].maxSequenceFromRight;
                }


                segmentTree[nodeIndex] = new Node(lengthOfConcat, leftSequence, rightSequence);
            }
            else if(lengthOfLeftTree >= lengthOfConcat && lengthOfLeftTree >= lengthOfRightTree) {
                // left tree is bigger
                segmentTree[nodeIndex] = new Node(segmentTree[nodeIndex<<1].size, segmentTree[nodeIndex<<1].maxSequenceFromLeft, segmentTree[nodeIndex<<1|1].maxSequenceFromRight);

            }
            else {
                // right tree is bigger
                segmentTree[nodeIndex] = new Node(segmentTree[nodeIndex<<1|1].size, segmentTree[nodeIndex<<1].maxSequenceFromLeft, segmentTree[nodeIndex<<1|1].maxSequenceFromRight);
            }

        }
    }

    static int query(int nodeIndex, int wantedLeft, int wantedRight, int left, int right) {
        // out of range
        if((right < wantedLeft) || (left > wantedRight))
            return -1;    

        if(left >= wantedLeft && right <= wantedRight) {
            return segmentTree[nodeIndex].size;
        }
        
        int mid = (left + right) >> 1;
        int lengthOfLeftTree = query(nodeIndex<<1, wantedLeft, wantedRight, left, mid);
        int lengthOfRightTree = query(nodeIndex<<1|1, wantedLeft, wantedRight, mid + 1, right);

        int leftPart = segmentTree[nodeIndex<<1].maxSequenceFromRight;
        int rightPart = segmentTree[nodeIndex<<1|1].maxSequenceFromLeft;

        if(segmentTree[nodeIndex<<1].maxSequenceFromRight > mid - wantedLeft + 1) {
            
            leftPart = mid - wantedLeft + 1;
        }

        if(segmentTree[nodeIndex<<1|1].maxSequenceFromLeft > wantedRight - (mid + 1) + 1) {
            
            rightPart = wantedRight - (mid + 1) + 1;
        }

        int lengthOfConcat = (array[mid] <= array[mid+1]) ? leftPart + rightPart : -1;
        
        
        // System.out.println("("  + wantedLeft + " " + wantedRight + " " + left + " " + right + " SIZE:" + segmentTree[nodeIndex].size + ")");
        // System.out.println("("  + lengthOfLeftTree + " " + lengthOfRightTree + " " + lengthOfConcat + ")");

        if(lengthOfConcat >= lengthOfLeftTree && lengthOfConcat >= lengthOfRightTree) {
            // concat is bigger
            return lengthOfConcat;
        }
        else if(lengthOfLeftTree >= lengthOfConcat && lengthOfLeftTree >= lengthOfRightTree) {
            // left tree is bigger
            return lengthOfLeftTree;
        } 
        else {
            // right tree is bigger
            return lengthOfRightTree;
        }

    }

    static void update(int nodeIndex, int wantedIndex, int left, int right) {
        if(wantedIndex < left || wantedIndex > right)
            return;

        if(left == right)
            return;

        int mid = (left + right) >> 1;
        update(nodeIndex<<1, wantedIndex, left, mid);
        update(nodeIndex<<1|1, wantedIndex, mid+1, right);

        int lengthOfLeftTree = segmentTree[nodeIndex<<1].size;
        int lengthOfRightTree = segmentTree[nodeIndex<<1|1].size;

        int leftPart = segmentTree[nodeIndex<<1].maxSequenceFromRight;
        int rightPart = segmentTree[nodeIndex<<1|1].maxSequenceFromLeft;

        // if(segmentTree[nodeIndex<<1].maxSequenceFromRight > mid - wantedLeft + 1) {
            
        //     leftPart = mid - wantedLeft + 1;
        // }

        // if(segmentTree[nodeIndex<<1|1].maxSequenceFromLeft > wantedRight - (mid + 1) + 1) {
            
        //     rightPart = wantedRight - (mid + 1) + 1;
        // }

        int lengthOfConcat = (array[mid] <= array[mid+1]) ? leftPart + rightPart : -1;
        

        if(lengthOfConcat >= lengthOfLeftTree && lengthOfConcat >= lengthOfRightTree) {
            // concat is bigger
            int leftSequence = segmentTree[nodeIndex<<1].maxSequenceFromLeft;
            int rightSequence = segmentTree[nodeIndex<<1|1].maxSequenceFromRight;

            // if left sequence covers whole left tree, then it can join the left from the right tree
            if(segmentTree[nodeIndex<<1].maxSequenceFromLeft == mid - left + 1) {
                leftSequence = segmentTree[nodeIndex<<1].maxSequenceFromLeft + segmentTree[nodeIndex<<1|1].maxSequenceFromLeft;

            }

            if(segmentTree[nodeIndex<<1|1].maxSequenceFromRight == right - (mid + 1) + 1) {
                rightSequence = segmentTree[nodeIndex<<1].maxSequenceFromRight + segmentTree[nodeIndex<<1|1].maxSequenceFromRight;
            }


            segmentTree[nodeIndex] = new Node(lengthOfConcat, leftSequence, rightSequence);
        }
        else if(lengthOfLeftTree >= lengthOfConcat && lengthOfLeftTree >= lengthOfRightTree) {
            // left tree is bigger
            segmentTree[nodeIndex] = new Node(segmentTree[nodeIndex<<1].size, segmentTree[nodeIndex<<1].maxSequenceFromLeft, segmentTree[nodeIndex<<1|1].maxSequenceFromRight);
        } 
        else {
            // right tree is bigger
            segmentTree[nodeIndex] = new Node(segmentTree[nodeIndex<<1|1].size, segmentTree[nodeIndex<<1].maxSequenceFromLeft, segmentTree[nodeIndex<<1|1].maxSequenceFromRight);
        }

        
    }

    static class Node {
        int maxSequenceFromLeft;
        int maxSequenceFromRight;
        int size;

        public Node(int size, int maxSequenceFromLeft, int maxSequenceFromRight) {
            this.size = size;
            this.maxSequenceFromLeft = maxSequenceFromLeft;
            this.maxSequenceFromRight = maxSequenceFromRight;
        }


        public String toString() {
            return "(" + size + ")[" + maxSequenceFromLeft + "," + maxSequenceFromRight + "]";
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