import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.Stack;

public class B {

    static ArrayList<Integer>[] graph;
    static int values[];
    static int globalTime = 1; 
    static final int UNVISITED = 0;
    static int dfs_num[], dfs_low[], max_reach[];
    static boolean visited[];
    static Stack<Integer> s;
    public static void main (String [] args) throws Exception {
        
        Scanner sc = new Scanner(System.in);

        PrintWriter out = new PrintWriter(System.out);

        int V = sc.nextInt() + 1;
        int E = sc.nextInt();

        values = new int[V];

        for(int i = 1; i < V; i++) {
            values[i] = sc.nextInt();
        }

        graph = new ArrayList[V];
        for (int i = 0; i < V; i++)
            graph[i] = new ArrayList<Integer>();

        while(E-- > 0) {
            int from = sc.nextInt();
            int to = sc.nextInt();
            graph[from].add(to);
        }

        visited = new boolean[V]; 
        dfs_num = new int[V]; 
        dfs_low = new int[V];
        max_reach = new int[V];
        s = new Stack<Integer>();

        for(int i = 1; i < V; i++) {
            if(dfs_num[i] == UNVISITED) {
                tarjan(i);
            }
        }

        for(int i = 1; i < V; i++) {
            System.out.print(max_reach[i]);
            if(i + 1 < V) {
                System.out.print(" ");
            }
        }

        out.flush();
        out.close();
    }

    static void tarjan(int u) {

        dfs_low[u] = dfs_num[u] = globalTime++;
        max_reach[u] = values[u];
        s.push(u);
        visited[u] = true;

        for(int v: graph[u]) {
            if(dfs_num[v] == UNVISITED) {
                tarjan(v);
            }
            
            if(visited[v]) {
                dfs_low[u] = Math.min(dfs_low[u], dfs_low[v]);
            }

            max_reach[u] = Math.max(max_reach[u], max_reach[v]);
        }


        // root of SCC
        if(dfs_low[u] == dfs_num[u]) {
            while(true) {
                int v = s.pop();
                visited[v] = false;
                max_reach[u] = Math.max(max_reach[u], max_reach[v]);
                max_reach[v] = max_reach[u];                  
                if(u == v)
                    break;
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