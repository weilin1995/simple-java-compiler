class TestDoubleClassNames {
    public static void main(String[] a){
        System.out.println(new DoublePrinter().start(1.0, 2.0));
    }
}

class DoublePrinter {
    double x;
    public int start(double x, double y) {
        x = 1.5 + 0.5;
        System.out.println(x);
        return 1;
    }
}
