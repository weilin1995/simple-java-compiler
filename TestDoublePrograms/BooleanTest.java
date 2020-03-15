class BooleanTest {
    public static void main(String[] a){
        System.out.println(new Tester().start());
    }
}

class Tester {
    int a;
    public int start() {
        a = 0;

        if (0 < 1 && true && a < 5) {
            System.out.println(1);
        }
        else {
            System.out.println(2);
        }
        System.out.println(3);
                

        return 1;
    }
}
