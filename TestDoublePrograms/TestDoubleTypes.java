class TestDoubleTypes {
    public static void main(String[] a){
        System.out.println(new DoublePrinter().start());
    }
}

class DoublePrinter {
    public int start() {
        System.out.println(1.23);
        System.out.println(0.23);
        System.out.println(.23);
        System.out.println(1.23e45);
        System.out.println(1.23e-45);
        System.out.println(1.23e+45);
        System.out.println(.23e45);
        System.out.println(.23e-45);
        System.out.println(.23e+45);
        System.out.println(23e+45);
        System.out.println(23e-45);
        System.out.println(23e45);
        return 1;
    }
}
