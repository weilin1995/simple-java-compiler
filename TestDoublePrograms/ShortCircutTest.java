class ShortCircutTest {
    public static void main(String[] a){
        System.out.println(new Tester().start());
    }
}

class Tester {
    int a;
    public int start() {
        a = 0;

        if (1 < 0 && this.PrintOne()) {
            System.out.println(2);
        }
        else {
            System.out.println(3);
        }
        System.out.println(4);
        return 1;
    }
    public boolean PrintOne() {
        System.out.println(1); 
        return false;
    }
}
