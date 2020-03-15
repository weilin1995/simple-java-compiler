class TestSmallDouble {
    public static void main(String[] a){
        System.out.println(new DoublePrinter().start());
    }
}

class DoublePrinter {
    public int start() {
        double x;
        x = .1e-9 + 0.1e+7 + .1e7;
        System.out.println(x);
        x = Math.sqrt(x);
        System.out.println(x);
        return 1;
    }
}
