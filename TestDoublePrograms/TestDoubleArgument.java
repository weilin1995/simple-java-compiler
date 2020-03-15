class TestDoubleArgument {
    public static void main(String[] a){
        System.out.println(new DoublePrinter().printDouble(1.1));
    }
}

class DoublePrinter {
    public int printDouble(double d) {
        double x;
        x = this.returnDouble();
        System.out.println(x);
        return 1;
    }

    public double returnDouble() {
        return 1.3;
    }
}
