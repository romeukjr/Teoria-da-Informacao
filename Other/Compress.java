import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Compress {
	private static String fileFullText;
	
	public static ArrayList<Tuple<String, Integer>> lzw(String fPath) {
		Map<String, Tuple<Integer, Integer>> preDictionary = preProccessForLzw(fPath);
		
		char[] chars = fileFullText.toCharArray();
		int index = chars.length;
		String p = null;
		
		for (int i = 0; i < chars.length; i++) {
			String c = String.valueOf(chars[i]);
			
			if (preDictionary.containsKey(String.valueOf(p + c))) {
				p = p + c;
			} else {
				Tuple<Integer, Integer> oldIndexes = preDictionary.get(p);
				oldIndexes.y++;
				preDictionary.put(p + c, new Tuple<Integer, Integer>(index, 1));
				p = c;
				index++;
			}
		}
		
		return sortLzw(preDictionary);
	}
	
	private static Map<String, Tuple<Integer, Integer>> preProccessForLzw(String fPath) {
		Map<String, Tuple<Integer, Integer>> dictionary = new HashMap<String, Tuple<Integer, Integer>>(); 
		File file = new File(fPath);
		
		try{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			
			int index = 1;
			while (line != null){
				fileFullText += line;
				char[] chars = line.toCharArray();
				for	(int i = 0; i < chars.length; i++){
					if (!dictionary.containsKey(chars[i])) {
						Tuple<Integer, Integer> indexes = new Tuple<Integer,Integer>(index, 1);
						dictionary.put(String.valueOf(chars[i]), indexes);
						index++;
					} else {
						Tuple<Integer, Integer> indexes = dictionary.get((int)chars[i]);
						indexes.y++;
					}
				}
								
				line = br.readLine();
			}
			
			br.close();
		}
		catch (IOException e){
			System.out.println("Ocorreu um erro ao ler o arquivo "+file.getName());
			System.out.println("A seguinte exeção ocorreu: "+e.getMessage());
		}
		
		return dictionary;
	}
	
	private static ArrayList<Tuple<String, Integer>> sortLzw(Map<String, Tuple<Integer, Integer>> dictionary) {
		ArrayList<Tuple<String, Integer>> result = new ArrayList<Tuple<String, Integer>>();
		ArrayList<String> symbol = new ArrayList<String>();
		
		Map.Entry<String, Tuple<Integer, Integer>> oldValue =  null;
		for (Map.Entry<String, Tuple<Integer, Integer>> value : dictionary.entrySet()) {
			String key = value.getKey();
			Tuple<Integer, Integer> indexes = value.getValue();
			
			if (!symbol.contains(key)) {
				symbol.add(key);
				result.add(new Tuple<String, Integer>(key, indexes.x));
			} 
		}
		
		Collections.sort(result, new Comparator<Tuple<String, Integer>>() {
	        @Override
	        public int compare(Tuple<String, Integer> value1, Tuple<String, Integer> value2)
	        {
	
	            return  value2.y.compareTo(value1.y);
	        }
	    });
		
		return result;
	}

	public static void lzwToHuffman(ArrayList<Tuple<String, Integer>> lzw, String fPath, String fEncodedPath) {
		HuffmanNode indexTreeRoot = new HuffmanNode();
		HuffmanNode symbolTreeRoot = new HuffmanNode();
		int indexMaxSizeCounter = 0;
		int symbolMaxSizeCounter = 0;
		
		HuffmanNode[] indexNodeMap = new HuffmanNode[lzw.size()];
		for (int i = 0; i < indexNodeMap.length; i++){
			Tuple<String, Integer> tuple = lzw.get(i);
			
			indexNodeMap[i] = new HuffmanNode(tuple.y);
			
			if (tuple.y > indexMaxSizeCounter) {
				indexMaxSizeCounter = tuple.y;
			}
		}
		
		HuffmanNode[] symbolNodeMap = new HuffmanNode[lzw.size()];
		for (int i = 0; i < symbolNodeMap.length; i++){
			Tuple<String, Integer> tuple = lzw.get(i);
			
			symbolNodeMap[i] = new HuffmanNode(tuple.x, tuple.y);
			
			if (tuple.x.length() > symbolMaxSizeCounter) {
				symbolMaxSizeCounter = tuple.x.length();
			}
		}

		indexTreeRoot = HuffmanTree.buildHuffmanTree(indexNodeMap);
		HuffmanTree.printTree(indexTreeRoot);
		symbolTreeRoot = HuffmanTree.buildHuffmanTree(symbolNodeMap);
		HuffmanTree.printTree(symbolTreeRoot);
		
		try{
			FileOutputStream fos = new FileOutputStream(fPath);
			
			String indexHeader = writeHuffmanIndexHeader(indexTreeRoot, "");
			writeHuffman(indexTreeRoot, fileFullText, indexHeader, fos);
			
			String symbolHeader = writeHuffmanIndexHeader(symbolTreeRoot, "");
			writeHuffman(symbolTreeRoot, fileFullText, symbolHeader, fos);
			
			fos.close();
		}
		catch (IOException e){
			System.out.println("A seguinte exeção ocorreu: "+e.getMessage());
		}
	}
	
	public static void writeHuffman(HuffmanNode root, String text, String header, FileOutputStream fos)
		throws IOException {
			
			text = header.getBytes() + text;
			char[] chars = text.toCharArray();
			char[] bits = new char[0];
			for (int i = 0; i < chars.length; i++){
				char[] aux = bits;
				char[] temp = HuffmanTree.findSymbolByChar(root, chars[i]).toCharArray();
				
				bits = new char[aux.length+temp.length];
				System.arraycopy(aux, 0, bits, 0, aux.length);
				System.arraycopy(temp, 0, bits, aux.length, temp.length);
			}
						
			byte[] charBits = new byte[(int)Math.ceil(bits.length/8.0)];
			byte bitCounter = 0;
			int index = 0;
			for (int j = 0; j < bits.length; j++){
				if (bitCounter >= 8){
					bitCounter = 0;

		            index++;
		        }
		        byte bit = charBits[index];

		        int aux = (1 << (7 - bitCounter));

		        if(bits[j] == '1'){
		            bit = (byte)(bit | aux);
		        }
		        charBits[index] = bit;
		        bitCounter++;
			}
			
			fos.write(charBits);
	}
	
	public static String writeHuffmanSymbolHeader(HuffmanNode root, String header){
		if (root.isLeaf()){
			return root.Symbol+Integer.toString(root.Frequency);
		}
		else{
			if (root.Left != null){
				header += writeHuffmanSymbolHeader(root.Left, header);
			}
			if (root.Right != null){
				header += writeHuffmanSymbolHeader(root.Right, header);
			}
		}
		
		return header;
	}
	
	public static String writeHuffmanIndexHeader(HuffmanNode root, String header){
		if (root.isLeaf()){
			return root.Index+Integer.toString(root.Frequency);
		}
		else{
			if (root.Left != null){
				header += writeHuffmanIndexHeader(root.Left, header);
			}
			if (root.Right != null){
				header += writeHuffmanIndexHeader(root.Right, header);
			}
		}
		
		return header;
}
}
