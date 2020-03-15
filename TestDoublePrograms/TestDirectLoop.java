class TestDirectLoop {
    public static void main(String[] a){
        System.out.println(new Tester().start());
    }
}

class Tester {
    int x;
    public int start() {
        x = 1;

        while (x + 1 < 10) {
            x = x + 1;
            System.out.println(x);
        }
        System.out.println(x);
        
        return 1;
    }
}
