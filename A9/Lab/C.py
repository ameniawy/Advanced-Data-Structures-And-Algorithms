
N = int(input())
target = int(input())
arr = list(map(int, input().split()))


# Finding pivot using ternary search
lo = 0
hi = N - 1
pivot = 0
while(lo <= hi):
    right = hi - (hi - lo) // 3
    left = lo + (hi - lo) // 3

    if lo == hi:
        pivot = lo
        break

    if arr[right] < arr[left]:
        hi = right - 1
        pivot = left
    else:
        lo = left + 1
        pivot = right


def binary_search(lo, hi, target, arr):
    if lo == hi:
        return lo if arr[lo] == target else -1

    mid = (lo + hi) // 2

    if arr[mid] == target:
        return mid

    if arr[mid] > target:
        hi = mid
    else:
        lo = mid + 1
    return binary_search(lo, hi, target, arr)


res = binary_search(0, pivot, target, arr)

if res == -1:
    if pivot != N-1:
        res = binary_search(pivot+1, N-1, target, arr)
    print(res)
else:
    print(res)
