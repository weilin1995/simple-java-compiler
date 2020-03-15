class ArrayTest {
    public static void main(String[] a){
	//System.out.println(new Animal().Start());
	System.out.println(new TestArray().Init());
    }
}

class TestArray {
   int[] nums;
   public boolean Compare(int num1 , int num2){
      boolean retval ;
      int aux02 ;

      retval = false ;
      aux02 = num2 + 1 ;
      if (num1 < num2) {
	 retval = false ;
	 System.out.println(1);
	 System.out.println(num1);
	 System.out.println(num2);
      }
      else if (!(num1 < aux02)) { 
	 retval = false ;
	 System.out.println(2);
      }
      else { 
	 retval = true ; 
	 System.out.println(3);
      }
      return retval ;
   }

   public int Init() {
      boolean crap;
      int x;
      nums = new int[1];
      nums[0] = 123;
      x = nums[0];
      crap = this.Compare(123, nums[0]); // this does not work
      //crap = this.Compare(123, x); // this does works
      System.out.println(nums[0]);
      return 999;
   }
}

