// IMPORT LIBRARY PACKAGES NEEDED BY YOUR PROGRAM
// SOME CLASSES WITHIN A PACKAGE MAY BE RESTRICTED
// DEFINE ANY CLASS AND METHOD NEEDED
// CLASS BEGINS, THIS CLASS IS REQUIRED
public class Solution {
    // METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED

    int dp[][];

    public int maxOfMinAltitudes(int columnCount, int rowCount, int[][] mat) {
        // WRITE YOUR CODE HERE

        // if (columnCount <= 0 || rowCount <= 0)
        // return 0;


        dp = new int[columnCount][rowCount];

        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[i].length; j++)
                dp[i][j] = Integer.MIN_VALUE;
        }

        int res = checkPath(0, 0, columnCount, rowCount, mat, Integer.MAX_VALUE);

        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[i].length; j++)
                System.out.print(dp[i][j] + " ");

            System.out.println();
        }


        return res;
    }

    public int checkPath(int c, int r, int maxC, int maxR, int[][] mat, int min) {

        if (c >= maxC || r >= maxR)
            return Integer.MIN_VALUE;


        int currentMin = Math.min(min, mat[c][r]);

        if (dp[c][r] != Integer.MIN_VALUE) {
            return dp[c][r];
        }

        if (c + 1 >= maxC && r + 1 >= maxR)
            return dp[c][r] = mat[c][r];

        int resRight = Math.min(checkPath(c + 1, r, maxC, maxR, mat, currentMin), mat[c][r]);
        int resBelow = Math.min(checkPath(c, r + 1, maxC, maxR, mat, currentMin), mat[c][r]);

        dp[c][r] = Math.max(resRight, resBelow);

        return dp[c][r];
    }
    // METHOD SIGNATURE ENDS

    public static void main(String[] args) {
        Solution sol = new Solution();

        int mat[][] = {{5, 4, 3}, {1, 6, 2}, {2, 7, 9}};
        System.out.println(sol.maxOfMinAltitudes(3, 3, mat));
    }
}
