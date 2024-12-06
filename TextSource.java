import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class TextSource {
	private Vector<String> v = new Vector<String>();
	
	public TextSource() { //word.txt파일에서 단어를 읽어오고 v에 저장
		try (Scanner scanner = new Scanner(new FileReader("C:\\자바학습\\words.txt"))){
			while(scanner.hasNextLine()) {
				String word = scanner.nextLine().trim();
				v.add(word);
			}
		} 
		catch (FileNotFoundException e) {
			System.out.println("word.txt 파일 오류발생");
			}
		}
	
	public String get() {
		int index = (int)(Math.random()*v.size());
		return v.get(index);
	}
	
	public void add(String word) {
		FileWriter fout = null;
		try {
			fout = new FileWriter("C:\\\\자바학습\\\\words.txt", true);
			fout.write(word + "\n");
			fout.close();
		} catch (IOException e) {
			System.out.println("word.txt 파일 오류발생");
		}
	}
}
