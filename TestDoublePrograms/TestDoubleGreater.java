class TestDoubleGreater {
    public static void main(String[] a){
        System.out.println(new DoublePrinter().start());
    }
}

class DoublePrinter {
    double[] zNums;
    double z;
    public int start() {
        double[] xNums;
        double y;
        int j;


        y = 123.0;
        z = 0.0;
        xNums = new double[10];
        zNums = new double[123];
        j = 0;

        // the problem here is something is wrong with the andq
        while ((this.DoubleMethod() < 10.0) && (j < 10)) {
            System.out.println(999);
            xNums[j] = y + y * 0.1;
            y = y + 0.5;
            j = j + 1;
        }
        
        j = 0; 
        System.out.println(xNums[0]);
        while (xNums[0] < 123.0 && j < 123) {
            zNums[j] = y * 0.1 + Math.sqrt(y) + 0.2;
            System.out.println(zNums[j]);
            y = y + .873;
            j = j + 2;
        }
        
        //j = 0; 
        //while (j < 10) {
            //System.out.println(xNums[j]);
            //j = j + 1;
        //} 

        //j = 0;
        //while (j < 123) {
            //System.out.println(zNums[j]);
            //j = j + 1;
        //}

        return 1;
    }

    public double DoubleMethod()
    {
        z = z + 1.0;
        return z;
    }
}
