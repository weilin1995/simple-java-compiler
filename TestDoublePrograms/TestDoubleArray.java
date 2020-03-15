class TestDoubleArray {
    public static void main(String[] a){
        System.out.println(new DoublePrinter().start());
    }
}

class DoublePrinter {
    double[] zNums;
    public int start() {
        double[] xNums;
        double y;
        int j;


        y = 123.0;
        xNums = new double[10];
        zNums = new double[123];
        j = 0;

        while (j < 10) {
            xNums[j] = y + y * 0.1;
            y = y + 0.5;
            j = j + 1;
        }
        
        j = 0; 
        while (j < 123) {
            zNums[j] = y * 0.1 + Math.sqrt(y) + 0.2;
            y = y + .873;
            j = j + 2;
        }
        
        j = 0; 
        while (j < 10) {
            System.out.println(xNums[j]);
            j = j + 1;
        } 

        j = 0;
        while (j < 123) {
            System.out.println(zNums[j]);
            j = j + 1;
        }

        return 1;
    }
}
