import java.io.*;
import java.util.StringTokenizer;

public class A {
    public static void main (String [] args) throws Exception {
        
        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int n = sc.nextInt();
        int q = sc.nextInt();

        DisjointUnionSets set = new DisjointUnionSets(n);

        while(q-- > 0) {
            int len_query = sc.nextInt();

            if(len_query == 2) {
                // student i wants to know the number of students in his group
                int student_i = sc.nextInt();
                out.println(set.setSize(student_i - 1));
            } else {
                // Student si invited all the members of the team that student sj belongs to. 
                int student_i = sc.nextInt();
                int student_j = sc.nextInt();

                set.union(student_i - 1, student_j - 1);
                
            }
        }
        out.flush();
        out.close();
    }

    static class DisjointUnionSets 
    { 
        int[] rank, parent, size; 
        int n; 
      
        // Constructor 
        public DisjointUnionSets(int n) 
        { 
            rank = new int[n]; 
            parent = new int[n];
            size = new int[n];
            this.n = n; 
            makeSet(); 
        } 
      
        // Creates n sets with single item in each 
        void makeSet() 
        { 
            for (int i=0; i<n; i++) 
            { 
                // Initially, all elements are in 
                // their own set. 
                parent[i] = i; 
                size[i] = 1;
            } 
        }

        int setSize(int student) {
            return this.size[this.find(student)];
        }
      
        // Returns representative of x's set 
        int find(int x) 
        { 
            // Finds the representative of the set 
            // that x is an element of 
            if (parent[x]!=x) 
            { 
                // if x is not the parent of itself 
                // Then x is not the representative of 
                // his set, 
                parent[x] = find(parent[x]); 
      
                // so we recursively call Find on its parent 
                // and move i's node directly under the 
                // representative of this set 
            } 
      
            return parent[x]; 
        } 
      
        // Unites the set that includes x and the set 
        // that includes x 
        void union(int x, int y) 
        { 
            // Find representatives of two sets 
            int xRoot = find(x), yRoot = find(y); 
      
            // Elements are in the same set, no need 
            // to unite anything. 
            if (xRoot == yRoot) 
                return; 
      
             // If x's rank is less than y's rank 
            if (rank[xRoot] < rank[yRoot]) {
      
                // Then move x under y  so that depth 
                // of tree remains less 
                parent[xRoot] = yRoot; 
                size[yRoot] += size[xRoot];
            }
            // Else if y's rank is less than x's rank 
            else if (rank[yRoot] < rank[xRoot]) {
      
                // Then move y under x so that depth of 
                // tree remains less 
                parent[yRoot] = xRoot; 
                size[xRoot] += size[yRoot];
            }
      
            else // if ranks are the same 
            { 
                // Then move y under x (doesn't matter 
                // which one goes where) 
                parent[yRoot] = xRoot;
                size[xRoot] += size[yRoot];
      
                // And increment the the result tree's 
                // rank by 1 
                rank[xRoot] = rank[xRoot] + 1; 
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