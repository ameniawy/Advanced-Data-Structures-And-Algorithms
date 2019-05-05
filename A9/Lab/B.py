
string = input()
stack = list()

operators = set(['+', '-', '*', '/'])

redundant = False
for char in string:
    if char.isalpha():
        continue

    if char != ')':
        stack.append(char)
    else:
        current = 'x'
        found_op = False
        while current != '(':
            current = stack.pop()
            if current in operators:
                found_op = True
        if found_op == False:
            redundant = True
            break

print('YES' if redundant else 'NO')

