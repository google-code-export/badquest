package util;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Graphics Override
 * @author Mike
 * 	Program for creating a wrapper graphics context using the method stubs generated by eclipse.
 */
public class GraphicsOverride {
	public static void main(String[] args){
		Scanner reader = new Scanner(System.in);
		while(reader.hasNext()){
			reader.nextLine(); //override
			String head = reader.nextLine();
			StringTokenizer st = new StringTokenizer(head, " ,()");
			st.nextToken(); //public
			String type = st.nextToken();
			String method = st.nextToken();
			int cnt = 0;
			while(st.countTokens() > 1){
				st.nextToken();
				st.nextToken();
				cnt++;
			}
			
			System.out.println(head.trim());
			if(type.equals("void")){
				System.out.print("    g."+method+"(");
				for(int i = 0; i < cnt; i++)
					System.out.print("arg"+i + (i < cnt-1?",":""));
				System.out.println(");");
			}else{
				System.out.print("    return g."+method+"(");
				for(int i = 0; i < cnt; i++)
					System.out.print("arg"+i + (i < cnt-1?",":""));
				System.out.println(");");
			}
			System.out.println("}");
			System.out.println();
			
			reader.nextLine(); //TODO
			reader.nextLine(); //blank, or return
			reader.nextLine(); //}
			reader.nextLine(); //blank
		}
	}
	
/*
 * 	@Override
	public void drawString(AttributedCharacterIterator arg0, float arg1, float arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fill(Shape arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Color getBackground() {
		// TODO Auto-generated method stub
		return null;
	}
 */
}
