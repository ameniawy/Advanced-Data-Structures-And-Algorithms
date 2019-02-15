package Lab;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class A {

    static int [][] segmentTree;
    // static int [][] segmentTreeUnique;
    static int treeSize;

    // left child = i * 2
    // right child = (i * 2) + 1
    public static void main (String [] args) throws Exception {
        
        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int n = sc.nextInt();
        int q = sc.nextInt();

        treeSize = 4 * (n);

        segmentTree = new int[treeSize][];

        for(int i = 0; i < treeSize; i++) {
            segmentTree[i] = new int [51];
        }


        while(q-- > 0) {
            int op = sc.nextInt();

            if(op == 1) {
                // insert balloon
                int box = sc.nextInt();
                int color = sc.nextInt();
                insertBalloon(box, color, 1, 1, n);
            }
            else if(op == 2) {
                // blow up balloon
                int box = sc.nextInt();
                int color = sc.nextInt();
                popBalloon(box, color, 1, 1, n);
            }
            else if(op == 3) {
                // query
                int left = sc.nextInt();
                int right = sc.nextInt();

                int [] resArray = query(1, left, right, 1, n);

                ArrayList<Integer> uniqueElements = new ArrayList<>();

                for(int i = 0; i < 51; i++) {
                    if(resArray[i] > 0) {
                        uniqueElements.add(i);
                    }
                }

                StringBuilder resString = new StringBuilder();

                int arraySize = uniqueElements.size();
                resString.append(arraySize);
                if(arraySize > 0) {
                    resString.append(" ");
                }
                Collections.sort(uniqueElements);
                for(int i = 0; i < arraySize; i++) {
                    resString.append(uniqueElements.get(i));

                    if(i + 1 != arraySize){
                        resString.append(" ");
                    }

                }
                out.println(resString);
            }
        }

        out.flush();
        out.close();
    }

    static void insertBalloon(int box, int color, int nodeIndex, int left, int right) {
        
        // out of range
        if(box > right || box < left)
            return ;

        // box is inside my range or I am the box
        if(box >= left && box <= right) {

            segmentTree[nodeIndex][color] = segmentTree[nodeIndex][color] + 1;
        }

        if(left != right) {
            int mid = (right + left) >> 1;
            insertBalloon(box, color, nodeIndex<<1, left, mid);
            insertBalloon(box, color, nodeIndex<<1|1, mid + 1, right);
        }

    }


    static void popBalloon(int box, int color, int nodeIndex, int left, int right) {
        

        if(box > right || box < left)
            return ;

        if(box >= left && box <= right) {

            segmentTree[nodeIndex][color] = segmentTree[nodeIndex][color] - 1;
        }

        if(left != right) {
            int mid = (right + left) >> 1;
            popBalloon(box, color, nodeIndex<<1, left, mid);
            popBalloon(box, color, nodeIndex<<1|1, mid + 1, right);

        }
        
    }


    static int [] query(int nodeIndex, int wantedLeft, int wantedRight, int left, int right) {

        if((right < wantedLeft) || (left > wantedRight))
        {
            return new int[51];
        }
        // If I am part of the solution or the whole solution
        if(left >= wantedLeft && right <= wantedRight) {
            return segmentTree[nodeIndex];
        }

        int mid = (right + left) >> 1;
        int [] leftArray = (left == right)? new int[51] : query(nodeIndex << 1, wantedLeft, wantedRight, left, mid);
        int [] rightArray = (left == right)? new int[51] : query(nodeIndex<<1|1, wantedLeft, wantedRight, mid + 1, right);
        int [] newArray = new int[51];
        for(int i = 1; i < leftArray.length; i++) {
            newArray[i] = leftArray[i] + rightArray[i];
        }
        return newArray;

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