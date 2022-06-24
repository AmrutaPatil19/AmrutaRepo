public class Main
{
	public static void main(String[] args) {
	    int[] arr = {2,2};
	   System.out.println(average(arr));
 	}
	
	public static int average(int[] arr){
	    int sum = 0;
	    for (int i=0;i<arr.length;i++ ){
	        
	        sum = sum + arr[i];
	        
	        
	    } 
	    int average = sum/arr.length;
	    return average;
	}
}
