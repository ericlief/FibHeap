javac -d ../bin FibHeap.java FibHeapNaive.java
./heapgen -s 23 -x | java -cp ../bin FibHeapNaive
#./heapgen -s 23 -x | java -cp ../bin FibHeap

#./heapgen -s 23 -x | head -1000 > test.txt
