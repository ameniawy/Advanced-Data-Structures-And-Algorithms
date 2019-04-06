import java.util.HashSet;

// you can also use imports, for example:
// import java.util.*;

// you can write to stdout for debugging purposes, e.g.
// System.out.println("this is a debug message");

class Solution {
    public static int[][] solution(int[] indices, int K) {
        // write your code in Java SE 8

        int[][] res = new int[K << 1][];
        // HashSet<Integer> usedInTesting = new HashSet<Integer>();

        int groupSize = res.length / K;


        int testingStart = 0;
        int testingEnd = groupSize - 1;

        for (int i = 0; i < K; i++) {
            // System.out.println("TESTING START: " + testingStart);
            // System.out.println("TESTING END: " + testingEnd);
            int testingSize = testingEnd - testingStart + 1;
            res[i << 1] = new int[testingSize];
            res[i << 1 | 1] = new int[indices.length - testingSize];

            int testingIndex = 0;
            int validIndex = 0;



            for (int j = 0; j < indices.length; j++) {
                if (j >= testingStart && j <= testingEnd) {
                    // add index to testing
                    res[i << 1][testingIndex++] = indices[j];
                } else {
                    // add index to validation
                    res[i << 1 | 1][validIndex++] = indices[j];
                }
            }



            testingStart += groupSize;
            testingEnd += groupSize;
            if (testingEnd >= indices.length)
                testingEnd = indices.length - 1;


        }

        return res;
    }

    // public static void main(String[] args) {
    //     int arr[] = {1, 2, 3};

    //     int res[][] = solution(arr, 2);

    //     for (int[] ar : res) {
    //         // System.out.println(ar.toString());
    //         for (int i : ar) {
    //             System.out.print(i + " ");
    //         }
    //         System.out.println();
    //     }
    // }
}
