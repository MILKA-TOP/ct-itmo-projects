public class Sum {
    public static void main(String[] args) {
	   int sumArgs = 0;
	   for (int i=0; i<args.length; i++){
		   System.out.println(args[i]);
	   }
	   System.out.println(args[1].split(" ").length);		
	   for (int i = 0; i < args.length; i++){
			for (int j = 0; j < args[i].split(" ").length; j++){
				if (args[i].split(" ")[j]!=" "){
					sumArgs += Integer.parseInt(args[i].split(" ")[j]);
				}
			}
		}
		
		System.out.println(sumArgs);
    }
}