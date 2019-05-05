

def main():
    n = int(input())
    direction = 0
    for _ in range(n):
        op = input()
        if op == 'left':
            if direction == 0:
                direction = 3
            else:
                direction -= 1
        elif op == 'right':
            if direction == 3:
                direction = 0
            else:
                direction += 1
        else:
            if direction == 0:
                direction = 2
            elif direction == 2:
                direction = 0
            elif direction == 1:
                direction = 3
            else:
                direction = 1

    if direction == 0:
        print('north')
    elif direction == 1:
        print('east')
    elif direction == 2:
        print('south')
    elif direction == 3:
        print('west')


if __name__ == '__main__':
    main()
