class TestDoubleArrays {
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

        y = this.DoubleMethodOne(zNums)[0] + this.DoubleMethodTwo(zNums)[1] + this.DoubleMethodThree(zNums)[2];
        System.out.println(y);
        return 1;
    }

    public double[] DoubleMethodOne(double[] x)
    {
        x[0] = x[0] + 1.0;
        return x;
    }
    public double[] DoubleMethodTwo(double[] x)
    {
        double[] newNums;
        x[1] = x[1] + 1.0;
        newNums = new double[2];
        newNums[0] = x[1];
        newNums[1] = x[0];
        return newNums;
    }
    public double[] DoubleMethodThree(double[] x)
    {
        x[2] = x[2] + 1.0;
        return x;
    }
}
