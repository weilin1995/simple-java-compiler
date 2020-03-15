class HelloWorld {
    public static void main(String[] a){
	System.out.println(new Animal().Start());
    }
}

class Animal {
    public int Start() {
	Dog d;
	int z;
	d = new Dog();
	z = d.Print();
	return z;
    }
}

class Dog {
   public int Print() {
	return 20;	
   }
}
