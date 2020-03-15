class OrderMultiply {
    public static void main(String[] a){
        System.out.println(new Tester().start());
    }
}

class Tester {
    int x;
    public int start() {
        int res;
        x = 0;
        
        res = this.One() * this.Two() * this.Three();
        System.out.println(res);
        return x;
    }

    public int One() {
        x = x + 1;
        return x;
    }

    public int Two() {
        x = x + 2;
        return x;
    }

    public int Three() {
        x = x + 3;
        return x;
    }
}
