javac -d ../bin FibHeap.java FibHeapNaive.java

#./heapgen -s 23 -x | java -cp ../bin FibHeapNaive
ulimit -t unlimited && nice -n 19 ./heapgen -s 22 -x | java -cp ../bin FibHeapNaive

