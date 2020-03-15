class TestDoubleAddition {
    public static void main(String[] a){
        System.out.println(new DoublePrinter().start());
    }
}

class DoublePrinter {
    public int start() {
        double x;
        x = 1.5 + 0.5;
        System.out.println(x);
        return 1;
    }
}
