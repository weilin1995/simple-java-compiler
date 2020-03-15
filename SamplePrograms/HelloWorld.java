class HelloWorld {
    public static void main(String[] a){
	System.out.println(new Animal().Start());
    }
}

class Animal {
    public int Start() {
    	int[] x;
	Dog d;
	int z;
	d = new Dog();
	z = d.Print();
	x = new int[5];
	x[0] = 10;
	return x[0];
    }
}

class Dog {
   public int Print() {
	return 20;	
   }
}
