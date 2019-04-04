import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;

public class A {


    static ArrayList<Integer>[] graph;
    static int globalTime = 0; 
    static final int NIL = -1; 

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int V = sc.nextInt() + 1;
        int E = sc.nextInt();

        // Graph INIT
        graph = new ArrayList[V];
        for (int i = 0; i < V; i++)
            graph[i] = new ArrayList<Integer>();

        while (E-- > 0) {
            int from = sc.nextInt();
            int to = sc.nextInt();
            graph[from].add(to);
            graph[to].add(from);
        }

        boolean visited[] = new boolean[V]; 
        int time[] = new int[V]; 
        int low[] = new int[V]; 
        int parent[] = new int[V]; 
        boolean ap[] = new boolean[V];
        int apAddition[] = new int[V];
        boolean isRoot[] = new boolean[V];

        for (int i = 0; i < V; i++) { 
            parent[i] = NIL; 
        } 

        for(int i = 1; i < V; i++) {
            if(!visited[i]) {
                // CALL DFS
                DFS(i, visited, time, low, parent, ap, apAddition, isRoot);
            }
        }

        int max_splits = 0;
        for(int i = 0; i < V; i++) {
            if(ap[i]) {
                int val = isRoot[i]? apAddition[i] - 1 : apAddition[i];
                max_splits = Math.max(max_splits, val);
            }
        }

        out.println(max_splits);

        out.flush();
        out.close();
    }

    static void DFS(int u, boolean visited[], int time[], int low[], int parent[], boolean ap[], int apAddition[], boolean isRoot[]) {
       
        int children = 0;
        visited[u] = true; 

        time[u] = low[u] = ++globalTime;

        for(int v: graph[u]) {
            if(!visited[v]) {

                children++; 
                parent[v] = u; 
                DFS(v, visited, time, low, parent, ap, apAddition, isRoot);
                
                low[u]  = Math.min(low[u], low[v]);
  
                
                // if not root and am articulation point
                if (parent[u] != NIL && low[v] >= time[u]) {
                    ap[u] = true;
                    // how many points depend on me?
                    apAddition[u] += 1;
                }
                
            }
            else if (v != parent[u]) {
                low[u]  = Math.min(low[u], time[v]);
            }
        }
        
        // if root
        if (parent[u] == NIL && children > 1) {
            ap[u] = true; 
            isRoot[u] = true;
            apAddition[u] = children;
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
