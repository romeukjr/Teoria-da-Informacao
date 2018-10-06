import java.text.DecimalFormat;

public class HuffmanTree {
	private static int totalBits = 0;
	private static int caracteres = 0;
	
	public static HuffmanNode buildHuffmanTree(HuffmanNode[] chars){
		HuffmanNode[] aux = chars;
		int charsCount = chars.length;
		for (int i = 0; i < charsCount; i++){
			HuffmanNode parent = new HuffmanNode(), r = new HuffmanNode(), l = new HuffmanNode();
			
			if (aux.length >= 1)
				parent.Left = l = extractMin(aux);
			if (aux.length >= 1)
				parent.Right = r = extractMin(aux);
			
			parent.Frequency = l.Frequency + r.Frequency;
			insert(aux, parent);
			sortByFrequency(aux, 0, aux.length-1);
		}
		
		return aux[aux.length-1];
	}
	
	public static HuffmanNode extractMin(HuffmanNode[] chars){
		HuffmanNode result = chars[0];
		int minIndex = 0;
		for (int i = 0; i < chars.length; i++){
			if ((chars[i].Frequency > 0 && result.Frequency > chars[i].Frequency) || (result.Frequency == 0)){
				result = chars[i];
				minIndex = i;
			}
		}
		
		for (int i = minIndex; i < chars.length - 1; i++){
			chars[i] = chars[i + 1];
		}
		chars[chars.length - 1] = new HuffmanNode();
		
		return result;
	}
	
	public static void insert(HuffmanNode[] chars, HuffmanNode node){	
		for (int i = 0; i < chars.length; i++){
			if(chars[i].Symbol == '\u0000' && chars[i].Frequency == 0){
				chars[i] = node;
				break;
			}			
		}
	}
	
	public static String findSymbolByChar(HuffmanNode root, char symbol){
		String bitPath = "";
		bitPath = findSymbolByChar(root, symbol, "", bitPath);
		
		return bitPath;
	}
	
	private static String findSymbolByChar(HuffmanNode root, char symbol, String currentBitPath, String result){
		
		if (!root.isLeaf()){
			if (root.Left != null){
				result = findSymbolByChar(root.Left, symbol, currentBitPath+"0", result);
			}
			if (root.Right != null){
				result = findSymbolByChar(root.Right, symbol, currentBitPath+"1", result);
			}
		}
		else if (root.Symbol == symbol){
			result = currentBitPath;
			return result;
		}
		
		return result;
		
	}

	public static char findSymbolByPath(HuffmanNode root, String path){
		char result = '\u0000';
		result = findSymbolByPath(root, path.toCharArray(), 0, result);
		
		return result;
	}
	
	private static char findSymbolByPath(HuffmanNode root, char[] path, int index, char result){
		if (!root.isLeaf()){
			if (root.Left != null){
				result = findSymbolByPath(root.Left, path, index++, result);
			}
			if (root.Right != null){
				result = findSymbolByPath(root.Left, path, index++, result);
			}
		}
		else{
			result = root.Symbol;
			return result;
		}
		
		return result;
	}
	
	public static void sortByFrequency(HuffmanNode[] chars, int left, int right){
	      int index = partition(chars, left, right);
	
	      if (left < index - 1){
	    	  sortByFrequency(chars, left, index - 1);
	      }
	
	      if (index < right){
	    	  sortByFrequency(chars, index, right);
	      }
	}
	
	private static int partition(HuffmanNode[] chars, int left, int right){
	      int i = left, j = right;
	      HuffmanNode aux;
	      HuffmanNode pivot = chars[(left + right) / 2];
	
	      while (i <= j) {
	            while (chars[i].Frequency < pivot.Frequency){
	                  i++;
	            }
	            while (chars[j].Frequency > pivot.Frequency){
	                  j--;
	            }
	            if (i <= j){
	                  aux = chars[i];
	                  chars[i] = chars[j];
	                  chars[j] = aux;
	                  i++;
	                  j--;
	            }
	      };
	
	      return i;
	}
	
	public static void printTree(HuffmanNode root){
		printTree(root, "");
		mediaBitsPerChar();
	}
	
	private static void printTree(HuffmanNode root, String bitPath){
		
		if (!root.isLeaf()){
			if (root.Left != null){
				printTree(root.Left, bitPath+"0");
			}
			if (root.Right != null){
				printTree(root.Right, bitPath+"1");
			}
		}
		else{
			System.out.println(root.Symbol+": "+bitPath);
			totalBits += bitPath.length();
			caracteres++;
		}
	}
	
	private static void mediaBitsPerChar(){
		double media = (totalBits/caracteres);
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("\nMédia de bits por caracter: " + df.format(media));
		
		totalBits = caracteres = 0;
	}
}
