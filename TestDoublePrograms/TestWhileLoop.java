class TestWhileLoop {
    public static void main(String[] a){
        System.out.println(new Tester().start());
    }
}

class Tester {
    int x;
    public int start() {
        x = 1;

        while (this.checkCond()) {
            System.out.println(x);
        }
        System.out.println(x);
        
        return 1;
    }

    public boolean checkCond() {
        boolean t;
        x = x + 1;
        if (x < 10) {
            t = true;
        }
        else {
            t = false;
        }

        return t;
    }
}
