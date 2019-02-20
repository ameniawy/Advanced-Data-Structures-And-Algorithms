
echo "Enter module number."
read MODULE
mkdir A$MODULE
cd A$MODULE
mkdir Assignment
mkdir Lab
cp ../Template.java ./Assignment/AssignmentA.java
cp ../Template.java ./Assignment/AssignmentB.java
cp ../Template.java ./Lab/A.java
cp ../Template.java ./Lab/B.java
sed -i 's/Template/AssignmentA/g' ./Assignment/AssignmentA.java
sed -i 's/Template/AssignmentB/g' ./Assignment/AssignmentB.java
sed -i 's/Template/A/g' ./Lab/A.java
sed -i 's/Template/B/g' ./Lab/B.java
