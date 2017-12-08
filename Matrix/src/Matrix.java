import java.util.Random;

public class Matrix {

    private int[][] A;
    private int N;
    private static int CONST = 4;

    public Matrix(int n) {
	this.N = n;
	//		Create nxn Matrix
	System.out.printf("Creating an %dx%d matrix\n", n, n);

	A = new int[n][n];

    }

    public void randInit() {
	Random rand = new Random();
	for (int i = 0; i < N; i++) {
	    for (int j = 0; j < N; j++)
		A[i][j] = rand.nextInt() * Integer.MAX_VALUE;
	}
    }

    public void ordInit() {
	int x = 1;
	for (int i = 0; i < N; i++) {
	    for (int j = 0; j < N; j++) {
		A[i][j] = x;
		x++;
	    }
	}
    }

    public void print() {
	System.out.println();
	for (int i = 0; i < N; i++) {
	    for (int j = 0; j < N; j++)
		System.out.print(A[i][j] + "\t");
	    System.out.println();
	    System.out.println();
	    System.out.println();

	}
    }

    public void transpose(int[][] A, int m, int n, int N) {
	//	for (int i = 0; i < N - 1; i++) {
	//	    for (int j = i + 1; j < N; j++) {
	//		System.out.println("transposing " + A[i][j] + " with " + A[j][i]);
	//		int tmp = A[i][j];
	//		A[i][j] = A[j][i];
	//		A[j][i] = tmp;
	//		System.out.println();
	//	    }
	//	    
	//	}

	System.out.println("in transpose");
	int endRow = m + N;
	int endCol = n + N;
	System.out.println("row end " + endRow + " " + "end col " + endCol);
	for (int i = m; i < endRow - 1; i++) {
	    for (int j = n + i - m + 1; j < endCol; j++) {
		System.out.println("transposing " + A[i][j] + " with " + A[j][i]);
		int tmp = A[i][j];
		A[i][j] = A[j][i];
		A[j][i] = tmp;
		System.out.println();
	    }
	}
    }

    public void transposeOnDiag(int[][] A, int m, int n, int N) {

	System.out.printf("transpose on diag %dx%d @(%d,%d)\n", N, N, m, n);
	if (N < CONST) {
	    System.out.println(N + " less than " + CONST);
	    //transpose(A);
	    transpose(A, m, n, N);

	}

	else {

	    //	    if (N % 2 != 0) {
	    //		
	    //		
	    //	    }
	    System.out.println(Math.ceil(N / 2));
	    transposeOnDiag(A, m, n, N / 2);		// upper left submatrix
	    //transposeOnDiag(A, m + (int) Math.ceil(N / 2.0), n + (int) Math.ceil(N / 2.0), (int) Math.ceil(N / 2.0));	// lower right submatrix
	    transposeOnDiag(A, m + N / 2, n + N / 2, (int) Math.ceil(N / 2.0));	// lower right submatrix

	    //	    swap(A, m, N / 2, N / 2, N / 2, n, N / 2);
	    //	    swap(A, m, N / 2, N / 2, N / 2, n, N / 2);
	    //int M = N / 2;
	    //	    int rowA = 0; 	// first row pos for matrix A
	    //	    int colA = N / 2; 	// first col pos for matrix A
	    //	    int nRowsA = N / 2;
	    //	    int nColsA = (int) Math.ceil(N / 2.0);
	    transposeAndSwap(A, m, n + N / 2, N / 2, (int) Math.ceil(N / 2.0));
	    // 			m    n     M		N
	}
    }

    //    public void swap(int[][] A, int m, int n, int N, int r, int l, int L) {
    //    public void transposeAndSwap(int[][] A, int rowA, int colA, int nRowsA, int nColsA, int rowB, int colB, int nRowsB, int nColsB)
    public void transposeAndSwap(int[][] A, int m, int n, int M, int N) {
	System.out.printf("transpose and swap %dx%d @(%d,%d)\n", M, N, m, n);
	//	if (nRowsA < CONST && nColsA < CONST && nRowsB < CONST && nColsB < CONST) {
	if (M < CONST && N < CONST) {
	    System.out.println("base swap");
	    int endRow = m + M;
	    int endCol = n + N;
	    for (int i = m; i < endRow; i++) {
		for (int j = n; j < endCol; j++) {
		    System.out.println("transposing " + A[i][j] + " with " + A[j][i]);
		    int tmp = A[i][j];
		    A[i][j] = A[j][i];
		    A[j][i] = tmp;
		}
	    }
	}

	else {

	    transposeAndSwap(A, m, n, M / 2, (int) Math.ceil(N / 2.0));	// uper right submatrix
	    transposeAndSwap(A, m, n + (int) Math.ceil(N / 2.0), M / 2, N / 2);	// lower left submatrix
	    transposeAndSwap(A, m + M / 2, n, (int) Math.ceil(M / 2.0), (int) Math.ceil(N / 2.0));	// uper right submatrix
	    transposeAndSwap(A, m + M / 2, n + (int) Math.ceil(N / 2.0), (int) Math.ceil(M / 2.0), N / 2);	// lower left submatrix

	}

    }

    public static void main(String[] args) {
	//	int x = 2999999999;
	//	int y = 2147483647;   // max int 
	//int n = Integer.parseInt(args[0]);
	int n = 100;
	//	for (int i = 0; i < args.length; i++)
	//	    System.out.println(i + args[i]);
	Matrix M = new Matrix(n);
	M.ordInit();

	//	M.print();
	//	M.transpose(M.A, 0, 0, n);
	//	M.print();
	//	M.transpose(M.A, 0, 0, n);
	M.print();
	M.transposeOnDiag(M.A, 0, 0, n);
	M.print();

    }
}
