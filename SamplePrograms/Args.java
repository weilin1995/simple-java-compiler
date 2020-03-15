class HelloWorld {
    public static void main(String[] a){
        // movq $16 , %rdi. Why only 16 bytes when constructing this object?
    System.out.println(new Animal().getAnimal());
    }
}

// 5 -> %rsi
// this -> %rdi
class Animal {
    int z;

    public int Start(int x, int y) {
        Animal a;
        z = 3; 
        // z = new Animal().getAnimal();
        return z + x + y;
    }

    public int One() {
      return 3;
    }

    public int getAnimal() {
    return new Animal().Start(5,6) + 2;
    }
}

class Cat extends Animal {
    public int End() {
        // this doesnt work.
       int a;
       // a = this.Start(5, 6);
       z = 4;
       // somehow this returns 2;
       return z;
    } 
}

