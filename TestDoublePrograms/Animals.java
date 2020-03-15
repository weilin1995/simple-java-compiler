class Animals {
    public static void main(String[] a){
        System.out.println(new Animal().doSomething(1, 2));
    }
}
class Animal {
    int x;
    public int doSomething(int a, int b) {
        return a;
    }
}
class Cat extends Animal {
    public Animal returnAnimal() {
        return new Cat();
    }
}
class Zebra extends Animal {
    int z;
}
class TestClass {
    public Animal returnTest() {
        return new Zebra();
    }
}
