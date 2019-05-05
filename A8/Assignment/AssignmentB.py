# your code goes here
from random import randint


class TrieNode(object):
    def __init__(self):
        self.left = None
        self.right = None
        self.parent = None
        # How many times this character appeared in the addition process
        self.counter = 0


class Trie(object):
    def __init__(self, root: TrieNode):
        self.root = root

    # def update(self, root):
    #     if (root.right and root.left):
    #         root.counter = root.right.counter + root.left.counter
    #     elif (root.left):
    #         root.counter = root.left.counter
    #     elif (root.right):
    #         root.counter = root.right.counter

    #     if (root.parent):
    #         self.update(root.parent)

    def insert(self, binary_num):
        self.insert_rec(binary_num, 0, self.root)

    def insert_rec(self, binary_num, level, current_node):
        if level >= len(binary_num):
            new_node = TrieNode()
            new_node.parent = current_node
            current_node.right = new_node
            current_node.counter += 1
            new_node.counter = 1
            return
        current_val = binary_num[level]
        current_node.counter += 1

        if current_val == '1':
            if current_node.right is None:
                new_node = TrieNode()
                new_node.parent = current_node
                current_node.right = new_node

            self.insert_rec(binary_num, level + 1, current_node.right)
        elif current_val == '0':
            if current_node.left is None:
                new_node = TrieNode()
                new_node.parent = current_node
                current_node.left = new_node

            self.insert_rec(binary_num, level + 1, current_node.left)

        # current_node.counter = 1
        # if current_node.left:
        #     current_node.counter += current_node.left.counter
        # if current_node.right:
        #     current_node.counter += current_node.right.counter

    def query(self, binary_num_query, binary_num_less_than):
        return self.query_rec(binary_num_query, binary_num_less_than, 0, self.root)

    def query_rec(self, binary_num_query, binary_num_less_than, level, current_node):
        if level >= len(binary_num_query) or current_node is None:
            return 0

        q = binary_num_query[level]
        k = binary_num_less_than[level]
        ans = 0

        # print(level, q, k)

        # if level == len(binary_num_query) - 1 and int(q) < int(k):
        #     return current_node.counter

        if q == '1' and k == '1':
            # we take right branch for sure
            ans += 0 if current_node.right is None else current_node.right.counter
            ans += self.query_rec(binary_num_query,
                                  binary_num_less_than, level + 1, current_node.left)

        elif q == '1' and k == '0':
            # we cannot take left branch, but we can investigate the right one
            ans += self.query_rec(binary_num_query,
                                  binary_num_less_than, level + 1, current_node.right)

        elif q == '0' and k == '1':
            # we take left branch for sure
            ans += 0 if current_node.left is None else current_node.left.counter
            ans += self.query_rec(binary_num_query,
                                  binary_num_less_than, level + 1, current_node.right)

        elif q == '0' and k == '0':
            # we cannot take righ branch, but we can investigate the left one
            ans += self.query_rec(binary_num_query,
                                  binary_num_less_than, level + 1, current_node.left)

        return ans


def xorLessK(arr, n, k):
    count = 0

    # check all subarrays
    for i in range(n):
        tempXor = 0
        for j in range(i, n):
            tempXor ^= arr[j]
            if (tempXor < k):
                count += 1

    return count


def get_binary_rep(number):
    INTEGER_LENGTH = 32

    binary_rep = bin(number)
    binary_rep = binary_rep[2:]

    binary_rep = ['0'] * (INTEGER_LENGTH - len(binary_rep)) + list(binary_rep)

    return binary_rep


def solve(arr, n, k):
    prev = 0

    root = TrieNode()
    trie = Trie(root)

    K = get_binary_rep(k)

    ans = 0

    for val in arr:
        prev = prev ^ val
        binary_rep = get_binary_rep(prev)
        # query here
        ans += trie.query(binary_rep, K)
        trie.insert(binary_rep)

    ans += trie.query(get_binary_rep(0), K)
    # print(trie.root.counter, trie.root.right, trie.root.left.counter)
    return ans


def generate_test_cases():

    trials = 1000
    while(trials > 0):
        print(trials)
        n = randint(0, 4)
        k = randint(0, n)

        arr = list()
        for i in range(n):
            arr.append(randint(0, 10))

        print("Took input")
        sol_naive = xorLessK(arr, n, k)
        print("solved naive")
        sol_real = solve(arr, n, k)

        if sol_naive != sol_real:
            print(n, k, arr)
            print("Naive: ", sol_naive, " Mine: ", sol_real)
            break
        trials -= 1


def main():
    n, k = map(int, input().split())

    arr = list()
    for _ in range(n):
        arr.append(int(input()))

    print(solve(arr, n, k))
    # generate_test_cases()


if __name__ == '__main__':
    main()
