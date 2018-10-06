import java.io.File;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		System.out.println("1 - Compactar \n2 - Descompactar");
		System.out.println("Digite a opção de operação: ");
		String option = sc.nextLine();
		
		if (option.equals("1")) {
			System.out.println("Digite o caminho do arquivo que será compactado: ");
			String fPath = sc.nextLine();
			System.out.println("Digite o caminho (e nome) do arquivo compactado: ");
			String fPathEncoded = sc.nextLine();
			File file = new File(fPath);
			
			In in = new In(file);
			Out out = new Out(fPathEncoded);
			
			LZW.compress();
			//Huffman.compress();
		} else if (option.equals("2")) {
			System.out.println("Digite o caminho do arquivo que será descompactado: ");
			String fPath = sc.nextLine();
			System.out.println("Digite o caminho (e nome) do arquivo descompactado: ");
			String fPathDecoded = sc.nextLine();
			File file = new File(fPath);
			
			In in = new In(file);
			Out out = new Out(fPathDecoded);
			
			//Huffman.expand();
			LZW.expand();
		}
		
	}

}
