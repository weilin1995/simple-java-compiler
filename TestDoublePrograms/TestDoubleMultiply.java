class TestDoubleMultiply {
    public static void main(String[] a){
        System.out.println(new DoublePrinter().start());
    }
}

class DoublePrinter {
    double z;
    public int start() {
        double x;
        double y;
        x = .1e-9 * 0.1e+7 * .1e7;
        System.out.println(x);
        y = x + 0.1e3 - 0.13;
        z = x + y + .1;
        System.out.println(z);
        System.out.println(y);
        return 1;
    }
}
