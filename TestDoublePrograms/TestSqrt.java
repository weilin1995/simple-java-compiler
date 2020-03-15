class TestSqrt {
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

        zNums = new double[3];
        zNums[0] = 1.5;
        zNums[1] = 2.3;
        zNums[2] = 5.1;
        
        System.out.println(Math.sqrt(zNums[0]) * Math.sqrt(zNums[1]) * Math.sqrt(zNums[2]));
        return 1;
    }
}
